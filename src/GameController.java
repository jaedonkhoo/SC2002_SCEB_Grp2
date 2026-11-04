import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GameState gameState;
    private final Waiter waiter;
    private final Chef chef;
    private final Host host;
    private final Cashier cashier;
    private final List<MenuItem> menu;
    private int lastStartedTurn;

    public GameController() {
        this.gameState = new GameState();
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.host = new Host();
        this.cashier = new Cashier();
        this.menu = GameConfig.createStartingMenu();
        this.lastStartedTurn = 0;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean hasStaffActed(StaffRole role) {
        return getStaff(role).hasActed();
    }

    // These availability queries inspect the state without performing staff tasks.
    public List<Table> getFreeTables() {
        List<Table> freeTables = new ArrayList<>();
        for (Table table : gameState.getTables()) {
            if (!table.isOccupied()) {
                freeTables.add(table);
            }
        }
        return freeTables;
    }

    public List<Customer> getCustomersToSeat(StaffRole role) {
        SeatingStaff staff = getSeatingStaff(role);
        if (staff.hasActed() || getFreeTables().isEmpty()) {
            return List.of();
        }
        return gameState.getWaitingCustomers();
    }

    public List<Customer> getCustomersToOrder() {
        List<Customer> customers = new ArrayList<>();
        if (!waiter.hasActed()) {
            for (Customer customer : gameState.getCustomers()) {
                if (customer.checkOrderPlacement() == ActionResult.SUCCESS) {
                    customers.add(customer);
                }
            }
        }
        return customers;
    }

    public List<Customer> getCustomersToServe() {
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : gameState.getCustomers()) {
            if (waiter.checkServeOrder(customer) == ActionResult.SUCCESS) {
                customers.add(customer);
            }
        }
        return customers;
    }

    public List<Customer> getCustomersToPrepare() {
        List<Customer> customers = new ArrayList<>();
        if (!waiter.hasActed() || chef.hasActed()) {
            return List.of();
        }

        for (Customer customer : gameState.getCustomers()) {
            Order order = customer.getOrder();
            if (order != null && order.checkPreparation() == ActionResult.SUCCESS) {
                customers.add(customer);
            }
        }

        return customers;
    }

    public ActionResult seatCustomer(int customerId, int tableId, StaffRole role) {
        SeatingStaff staff = getSeatingStaff(role);
        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        Table table = gameState.findTableById(tableId);
        if (table == null) {
            return ActionResult.TABLE_NOT_FOUND;
        }

        ActionResult result = staff.seatCustomer(customer, table);
        if (result == ActionResult.SUCCESS) {
            gameState.removeWaitingCustomer(customer);
        }
        return result;
    }

    public ActionResult takeOrder(int customerId) {
        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }

        MenuItem item = customer.chooseMenuItem(menu);
        int price = customer.calculatePriceCents(item.getPriceCents());

        if (gameState.getHappyHourTurnsRemaining() > 0) {
            price = (price * 8 + 5) / 10;
        }

        Order order = new Order(item, price);
        return waiter.takeOrder(customer, order);
    }

    public ActionResult serveOrder(int customerId) {
        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }

        ActionResult result = waiter.serveOrder(customer);
        if (result == ActionResult.SUCCESS) {
            gameState.recordServedCustomer();
        }
        return result;
    }

    public ActionResult prepareOrder(int customerId) {
        if (!waiter.hasActed()) {
            return ActionResult.WAITER_NOT_FINISHED;
        }

        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        return chef.prepareOrder(customer.getOrder());
    }

    public ActionResult staffWait(StaffRole role) {
        if (role == StaffRole.CHEF && !waiter.hasActed()) {
            return ActionResult.WAITER_NOT_FINISHED;
        }

        return getStaff(role).skipTurn();
    }

    public void startTurn(boolean activateHappyHour) {
        if (lastStartedTurn == gameState.getCurrentTurn()) {
            throw new IllegalStateException("This turn has already started.");
        }

        lastStartedTurn = gameState.getCurrentTurn();

        handleCustomerArrival();
        waiter.resetForNewTurn();
        chef.resetForNewTurn();
        host.resetForNewTurn();

        if (activateHappyHour) {
            gameState.activateHappyHour();
        }

        applyHappyHourSatisfactionBonus();
    }

    public boolean endTurn() {
        // Process later lifecycle stages first so a customer cannot advance twice this turn.
        // Newly READY_TO_PAY customers wait until next turn; JUST_SERVED customers only start eating.
        processPayments(customersWithStatus(CustomerStatus.READY_TO_PAY));
        processEatingCustomers(customersWithStatus(CustomerStatus.EATING));
        processJustServedCustomers(customersWithStatus(CustomerStatus.JUST_SERVED));
        processSatisfaction();
        return gameState.advanceTurn();
    }

    public boolean hasWon() {
        return gameState.getPaidCustomerCount() >= GameConfig.TARGET_PAID_CUSTOMERS
                && gameState.getRevenueCents() >= GameConfig.TARGET_REVENUE_CENTS
                && gameState.getAverageSatisfaction() >= GameConfig.TARGET_AVG_SATISFACTION;
    }

    private ControllableStaff getStaff(StaffRole role) {
        switch (role) {
            case HOST:
                return host;
            case WAITER:
                return waiter;
            case CHEF:
                return chef;
            default:
                throw new IllegalArgumentException("Invalid staff role");
        }
    }

    private SeatingStaff getSeatingStaff(StaffRole role) {
        ControllableStaff staff = getStaff(role);
        if (!(staff instanceof SeatingStaff)) {
            throw new IllegalArgumentException("Staff role cannot seat customers: " + role);
        }
        return (SeatingStaff) staff;
    }

    private void handleCustomerArrival() {
        int index = GameConfig.ARRIVAL_TURNS.indexOf(gameState.getCurrentTurn());

        if (index == -1) {
            return;
        }

        int customerId = index + 1;
        Customer customer;

        switch (customerId) {
            case 3:
                customer = new VIPCustomer(customerId);
                break;
            case 5:
                customer = new CriticCustomer(customerId);
                break;
            default:
                customer = new RegularCustomer(customerId);
        }

        gameState.addCustomer(customer);
    }

    private void applyHappyHourSatisfactionBonus() {
        if (gameState.getHappyHourTurnsRemaining() == 0) {
            return;
        }

        for (Customer customer : gameState.getCustomers()) {
            CustomerStatus status = customer.getStatus();

            if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED) {
                customer.increaseSatisfaction(15);
            }
        }
    }

    private List<Customer> customersWithStatus(CustomerStatus status) {
        ArrayList<Customer> customers = new ArrayList<>();
        for (Customer customer : gameState.getCustomers()) {
            if (customer.getStatus() == status) {
                customers.add(customer);
            }
        }
        return customers;
    }

    private void processPayments(List<Customer> readyToPay) {
        if (readyToPay.isEmpty()) {
            return;
        }

        Customer oldest = readyToPay.get(0);
        if (!cashier.processPayment(oldest)) {
            return;
        }
        if (!gameState.recordPayment(oldest)) {
            return;
        }

        gameState.findTableByCustomer(oldest).freeTable();
    }

    private void processEatingCustomers(List<Customer> eating) {
        for (Customer customer : eating) {
            customer.markReadyToPay();
        }
    }

    private void processJustServedCustomers(List<Customer> justServed) {
        for (Customer customer : justServed) {
            customer.markEating();
        }
    }

    private void processSatisfaction() {
        for (Customer customer : gameState.getCustomers()) {
            customer.applyWaitingPenalty();
            processAbandonment(customer);
        }
    }

    private void processAbandonment(Customer customer) {
        // Only a new transition to LEFT counts, so departures cannot be counted again later.
        if (!customer.markLeft()) {
            return;
        }

        gameState.recordUnhappyDeparture();
        gameState.removeWaitingCustomer(customer);
        Order order = customer.getOrder();
        if (order != null) {
            order.cancel();
        }
        Table table = gameState.findTableByCustomer(customer);
        if (table != null) {
            table.freeTable();
        }
    }
}
