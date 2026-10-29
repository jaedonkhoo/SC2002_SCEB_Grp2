import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final GameState gameState;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private final List<MenuItem> menu;

    public GameController() {
        this.gameState = new GameState();
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.cashier = new Cashier();
        this.menu = List.of(
                new MenuItem("Salad", 900, 1),
                new MenuItem("Burger", 1500, 1),
                new MenuItem("Pasta", 1800, 2));
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean waiterAvailable() {
        return !waiter.hasActed();
    }

    public boolean chefAvailable() {
        return !chef.hasActed();
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

    public List<Customer> getCustomersToSeat() {
        if (!waiterAvailable() || getFreeTables().isEmpty()) {
            return List.of();
        }
        return gameState.getWaitingCustomers();
    }

    public List<Customer> getCustomersToOrder() {
        List<Customer> customers = new ArrayList<>();
        if (waiterAvailable()) {
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
        if (chefAvailable()) {
            for (Customer customer : gameState.getCustomers()) {
                Order order = customer.getOrder();
                if (order != null && order.checkPreparation() == ActionResult.SUCCESS) {
                    customers.add(customer);
                }
            }
        }
        return customers;
    }

    private void handleCustomerArrival() {
        int index = GameConfig.ARRIVAL_TURNS.indexOf(gameState.getCurrentTurn());
        if (index != -1) {
            gameState.addCustomer(new RegularCustomer(index + 1));
        }
    }

    public ActionResult seatCustomer(int customerId, int tableId) {
        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        Table table = gameState.findTableById(tableId);
        if (table == null) {
            return ActionResult.TABLE_NOT_FOUND;
        }

        ActionResult result = waiter.seatCustomer(customer, table);
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

        // Arrival IDs give a reproducible Salad -> Burger -> Pasta cycle.
        MenuItem item = menu.get((customerId - 1) % menu.size());
        Order order = new Order(item, item.getPriceCents());
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
        Customer customer = gameState.findCustomerById(customerId);
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        return chef.prepareOrder(customer.getOrder());
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

    private void processJustServedCustomers(List<Customer> justServed) {
        for (Customer customer : justServed) {
            customer.markEating();
        }
    }

    private void processEatingCustomers(List<Customer> eating) {
        for (Customer customer : eating) {
            customer.markReadyToPay();
        }
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

    public void startTurn() {
        handleCustomerArrival();
        chef.resetForNewTurn();
        waiter.resetForNewTurn();
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
}
