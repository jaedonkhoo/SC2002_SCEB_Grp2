import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class GameCLI {
    private static final int DISPLAY_WIDTH = 79;

    private static final String CUSTOMER_ROW_FORMAT =
            "%-4s %-9s %-14s %5s %-24s %-12s %5s%n";

    private final GameController controller;
    private final Scanner scanner;

    public GameCLI() {
        this.controller = new GameController();
        this.scanner = new Scanner(System.in);
    }

    public void runGame() {
        boolean moreTurns;
        do {
            controller.startTurn(promptHappyHourActivation());
            displayGameStatus();

            staffSelectionInput();

            int completedTurn = controller.getGameState().getCurrentTurn();
            moreTurns = controller.endTurn();
            displayTurnSummary(completedTurn);
        } while (moreTurns);

        displayFinalResult();
    }

    private void displayGameStatus() {
        GameState state = controller.getGameState();

        System.out.println();
        displayBanner(" TURN " + state.getCurrentTurn() + " / " + GameConfig.MAX_TURNS + " ");
        System.out.println();

        System.out.printf("Revenue: $%.2f | Paid-customer satisfaction: %s%n",
                state.getRevenueCents() / 100.0, averageSatisfactionText(state));
        System.out.printf("Served: %d | Paid: %d | Unhappy departures: %d%n",
                state.getServedCount(), state.getPaidCustomerCount(),
                state.getUnhappyDepartures());

        int turnsRemaining = state.getHappyHourTurnsRemaining();

        if (turnsRemaining > 0) {
            System.out.println("Happy Hour: ACTIVE (" + turnsRemaining + " turn(s) remaining)");
        } else if (state.hasUsedHappyHour()) {
            System.out.println("Happy Hour: Used");
        } else {
            System.out.println("Happy Hour: Available");
        }

        displayWaitingQueue(state);
        displayTables(state);
        displayCustomers(state);

        System.out.println(
                "\nStaff: Host 1 seating task | Waiter 1 task | Chef 1 prep unit | Cashier 1 automatic payment");
    }

    private void displayWaitingQueue(GameState state) {
        System.out.println("\nWaiting customer IDs: " + customerIds(state.getWaitingCustomers()));
    }

    private void displayTables(GameState state) {
        StringJoiner tables = new StringJoiner(" | ");

        for (Table table : state.getTables()) {
            Customer occupant = table.getOccupant();
            tables.add("Table " + table.getId() + ": "
                    + (occupant == null ? "Empty" : "Customer " + occupant.getId()));
        }

        System.out.println(tables);
    }

    private void displayCustomers(GameState state) {
        System.out.println("\nCustomers:");

        if (state.getCustomers().isEmpty()) {
            System.out.println("  None");
            return;
        }

        System.out.printf(CUSTOMER_ROW_FORMAT,
                "ID", "Type", "State", "Sat.", "Food", "Order", "Prep");

        System.out.println("-".repeat(DISPLAY_WIDTH));

        for (Customer customer : state.getCustomers()) {
            Order order = customer.getOrder();

            System.out.printf(CUSTOMER_ROW_FORMAT,
                    customer.getId(),
                    customer.getCustomerType(),
                    customer.getStatus(),
                    customer.getSatisfaction(),
                    order == null ? "-" : order.getMenuItem().getName(),
                    order == null ? "-" : order.getOrderStatus(),
                    order == null ? "-" : preparationText(order));
        }
    }

    private void displayTurnSummary(int completedTurn) {
        GameState state = controller.getGameState();

        System.out.printf("%nEnd of turn %d | Served: %d | Paid: %d | Unhappy departures: %d%n",
                completedTurn,
                state.getServedCount(),
                state.getPaidCustomerCount(),
                state.getUnhappyDepartures());

        System.out.printf("Revenue: $%.2f | Paid-customer satisfaction: %s%n",
                state.getRevenueCents() / 100.0,
                averageSatisfactionText(state));

        System.out.println();
    }

    private void displayFinalResult() {
        GameState state = controller.getGameState();

        System.out.println();
        displayBanner(" GAME OVER ");

        if (controller.hasWon()) {
            System.out.println("RESULT: VICTORY!");
        } else {
            System.out.println("RESULT: DEFEAT!");
        }

        System.out.println("\nFinal Statistics:");

        System.out.printf("Turns Played: %d / %d%n",
                state.getCurrentTurn(),
                GameConfig.MAX_TURNS);

        System.out.println("Customers Served: " + state.getServedCount());
        System.out.println("Customers Paid: " + state.getPaidCustomerCount());
        System.out.println("Unhappy Departures: " + state.getUnhappyDepartures());

        System.out.printf("Total Revenue: $%.2f%n",
                state.getRevenueCents() / 100.0);

        double average = state.getAverageSatisfaction();

        if (average < 0) {
            System.out.println("Average Satisfaction: N/A");
        } else {
            System.out.printf("Average Satisfaction: %.1f%n", average);
        }

        System.out.println("=".repeat(DISPLAY_WIDTH));
    }

    private boolean promptHappyHourActivation() {
        GameState state = controller.getGameState();

        if (state.hasUsedHappyHour()) {
            return false;
        }

        while (true) {
            int choice = readInt(
                    "\nTurn " + state.getCurrentTurn()
                            + " - Activate Happy Hour? (1 = Yes, 0 = No): ");

            if (choice == 0 || choice == 1) {
                return choice == 1;
            }

            System.out.println("Please enter 0 or 1.");
        }
    }

    private void staffSelectionInput() {
        while (true) {
            // Skip staff with no eligible tasks.
            autoSkipUnavailableStaff();

            // End staff selection once everyone has finished.
            if (controller.hasStaffActed(StaffRole.HOST)
                    && controller.hasStaffActed(StaffRole.WAITER)
                    && controller.hasStaffActed(StaffRole.CHEF)) {
                return;
            }

            displayStaffSelectionMenu();
            int choice = readInt("Select staff: ");

            switch (choice) {
                case 1:
                    if (controller.hasStaffActed(StaffRole.HOST)) {
                        System.out.println("Host has already finished this turn.");
                        break;
                    }

                    hostInput();
                    break;

                case 2:
                    if (controller.hasStaffActed(StaffRole.WAITER)) {
                        System.out.println("Waiter has already finished this turn.");
                        break;
                    }

                    waiterInput();
                    break;

                case 3:
                    if (controller.hasStaffActed(StaffRole.CHEF)) {
                        System.out.println("Chef has already finished this turn.");
                        break;
                    }

                    if (!controller.hasStaffActed(StaffRole.WAITER)) {
                        System.out.println("The Waiter must finish before the Chef.");
                        break;
                    }

                    chefInput();
                    break;

                default:
                    System.out.println("Invalid choice. Please choose 1, 2 or 3.");
                    break;
            }
        }
    }

    private void autoSkipUnavailableStaff() {
        // Automatically skip Host if no seating tasks are available.
        if (!controller.hasStaffActed(StaffRole.HOST)
                && controller.getCustomersToSeat(StaffRole.HOST).isEmpty()) {
            controller.staffWait(StaffRole.HOST);
            System.out.println("Host: no available tasks; waiting automatically.");
        }

        // Automatically skip Waiter if no tasks are available.
        if (!controller.hasStaffActed(StaffRole.WAITER)
                && controller.getCustomersToSeat(StaffRole.WAITER).isEmpty()
                && controller.getCustomersToOrder().isEmpty()
                && controller.getCustomersToServe().isEmpty()) {
            controller.staffWait(StaffRole.WAITER);
            System.out.println("Waiter: no available tasks; waiting automatically.");
        }

        // Chef must wait until Waiter has finished before checking availability.
        if (controller.hasStaffActed(StaffRole.WAITER)
                && !controller.hasStaffActed(StaffRole.CHEF)
                && controller.getCustomersToPrepare().isEmpty()) {
            controller.staffWait(StaffRole.CHEF);
            System.out.println("Chef: no available tasks; waiting automatically.");
        }
    }

    private void displayStaffSelectionMenu() {
        System.out.println("\n--- Select Staff ---");

        System.out.println("1. Host   "
                + (controller.hasStaffActed(StaffRole.HOST)
                        ? "[completed]" : "[available]"));

        System.out.println("2. Waiter "
                + (controller.hasStaffActed(StaffRole.WAITER)
                        ? "[completed]" : "[available]"));

        System.out.println("3. Chef   "
                + (controller.hasStaffActed(StaffRole.CHEF)
                        ? "[completed]"
                        : !controller.hasStaffActed(StaffRole.WAITER)
                                ? "[locked: Waiter must finish first]"
                                : "[available]"));
    }

    private void hostInput() {
        List<Customer> toSeat = controller.getCustomersToSeat(StaffRole.HOST);

        if (toSeat.isEmpty()) {
            System.out.println("\nHost: no available seating tasks; waiting automatically.");
            controller.staffWait(StaffRole.HOST);
            return;
        }

        while (true) {
            displayHostMenu(toSeat);
            int choice = readInt("Choose an action: ");

            switch (choice) {
                case 1:
                    System.out.println("Free table IDs: " + freeTableIds());

                    int customerId = readInt("Input Customer ID: ");
                    int tableId = readInt("Input Table ID: ");

                    ActionResult result = controller.seatCustomer(
                            customerId, tableId, StaffRole.HOST);

                    if (result == ActionResult.SUCCESS) {
                        System.out.println("Customer seated successfully!");
                        return;
                    }

                    displayActionFailure(result);
                    break;

                case 0:
                    ActionResult waitResult = controller.staffWait(StaffRole.HOST);

                    if (waitResult == ActionResult.SUCCESS) {
                        System.out.println("Host: waiting.");
                        return;
                    }

                    displayActionFailure(waitResult);
                    break;

                default:
                    System.out.println("Invalid choice. Please choose 0 or 1.");
                    break;
            }
        }
    }

    private void displayHostMenu(List<Customer> toSeat) {
        System.out.println("\n--- Host: choose one task ---");

        System.out.println("1. Seat customer "
                + (toSeat.isEmpty()
                        ? "[unavailable]"
                        : "(customer IDs: " + customerIds(toSeat) + ")"));

        System.out.println("0. Wait");
    }

    private void waiterInput() {
        List<Customer> toSeat = controller.getCustomersToSeat(StaffRole.WAITER);
        List<Customer> toOrder = controller.getCustomersToOrder();
        List<Customer> toServe = controller.getCustomersToServe();

        if (toSeat.isEmpty() && toOrder.isEmpty() && toServe.isEmpty()) {
            System.out.println("\nWaiter: no available tasks; waiting automatically.");
            controller.staffWait(StaffRole.WAITER);
            return;
        }

        while (true) {
            displayWaiterMenu(toSeat, toOrder, toServe);
            int choice = readInt("Choose an action: ");

            switch (choice) {
                case 1:
                    if (toSeat.isEmpty()) {
                        System.out.println(
                                "Seating is unavailable: it requires a waiting customer and a free table.");
                        break;
                    }

                    System.out.println("Free table IDs: " + freeTableIds());
                    int seatingCustomerId = readInt("Input Customer ID: ");
                    int tableId = readInt("Input Table ID: ");

                    ActionResult seatResult = controller.seatCustomer(seatingCustomerId, tableId, StaffRole.WAITER);

                    if (seatResult == ActionResult.SUCCESS) {
                        System.out.println("Customer seated successfully!");
                        return;
                    }

                    displayActionFailure(seatResult);
                    break;

                case 2:
                    if (toOrder.isEmpty()) {
                        System.out.println(
                                "Taking orders is unavailable: no seated customer needs an order.");
                        break;
                    }

                    int orderingCustomerId = readInt("Input Customer ID: ");
                    ActionResult orderResult = controller.takeOrder(orderingCustomerId);

                    if (orderResult == ActionResult.SUCCESS) {
                        System.out.println("Order taken successfully!");

                        Customer customer = controller.getGameState()
                                .findCustomerById(orderingCustomerId);
                        Order order = customer.getOrder();

                        System.out.printf(
                                "Customer %d (%s) ordered %s for $%.2f%n",
                                customer.getId(),
                                customer.getCustomerType(),
                                order.getMenuItem().getName(),
                                order.getFinalPriceCents() / 100.0);
                        return;
                    }

                    displayActionFailure(orderResult);
                    break;

                case 3:
                    if (toServe.isEmpty()) {
                        System.out.println(
                                "Serving is unavailable: no seated customer has a ready order.");
                        break;
                    }

                    int servingCustomerId = readInt("Input customer ID: ");
                    ActionResult serveResult = controller.serveOrder(servingCustomerId);

                    if (serveResult == ActionResult.SUCCESS) {
                        System.out.println("Order served successfully!");
                        return;
                    }

                    displayActionFailure(serveResult);
                    break;

                case 0:
                    controller.staffWait(StaffRole.WAITER);
                    System.out.println("Waiter: waiting.");
                    return;

                default:
                    System.out.println("Invalid choice. Please choose 0, 1, 2 or 3.");
                    break;
            }
        }
    }

    private void displayWaiterMenu(List<Customer> toSeat,
            List<Customer> toOrder,
            List<Customer> toServe) {
        System.out.println("\n--- Waiter: choose one task ---");

        System.out.println("1. Seat customer  "
                + (toSeat.isEmpty()
                        ? "[unavailable]"
                        : "(customer IDs: " + customerIds(toSeat) + ")"));

        System.out.println("2. Take order     "
                + (toOrder.isEmpty()
                        ? "[unavailable]"
                        : "(customer IDs: " + customerIds(toOrder) + ")"));

        System.out.println("3. Serve order    "
                + (toServe.isEmpty()
                        ? "[unavailable]"
                        : "(customer IDs: " + customerIds(toServe) + ")"));

        System.out.println("0. Wait");
    }

    private void chefInput() {
        List<Customer> toPrepare = controller.getCustomersToPrepare();

        if (toPrepare.isEmpty()) {
            System.out.println("\nChef: no orders need preparation; waiting automatically.");
            controller.staffWait(StaffRole.CHEF);
            return;
        }

        while (true) {
            displayChefMenu(toPrepare);
            int choice = readInt("Choose an action: ");

            switch (choice) {
                case 1:
                    int customerId = readInt("Input Customer ID: ");
                    ActionResult result = controller.prepareOrder(customerId);

                    if (result == ActionResult.SUCCESS) {
                        Order order = controller.getGameState()
                                .findCustomerById(customerId).getOrder();

                        System.out.printf(
                                "Customer %d: %s preparation %s (%s).%n",
                                customerId,
                                order.getMenuItem().getName(),
                                preparationText(order),
                                order.getOrderStatus());
                        return;
                    }

                    displayActionFailure(result);
                    break;

                case 0:
                    controller.staffWait(StaffRole.CHEF);
                    System.out.println("Chef: waiting.");
                    return;

                default:
                    System.out.println("Invalid choice. Please choose 0 or 1.");
                    break;
            }
        }
    }

    private void displayChefMenu(List<Customer> toPrepare) {
        System.out.println("\n--- Chef: orders needing preparation ---");

        for (Customer customer : toPrepare) {
            Order order = customer.getOrder();

            System.out.printf("Customer %d | %-24s | Prep %s%n",
                    customer.getId(),
                    order.getMenuItem().getName(),
                    preparationText(order));
        }

        System.out.println("1. Prepare Order");
        System.out.println("0. Wait");
    }

    private void displayActionFailure(ActionResult result) {
        switch (result) {
            case SUCCESS:
                break;

            case CUSTOMER_NOT_FOUND:
                System.out.println("Customer ID not found.");
                break;

            case TABLE_NOT_FOUND:
                System.out.println("Table ID not found.");
                break;

            case CUSTOMER_NOT_WAITING:
                System.out.println("This customer is no longer waiting.");
                break;

            case CUSTOMER_NOT_SEATED:
                System.out.println(
                        "This action requires a seated customer who has not yet been served.");
                break;

            case TABLE_OCCUPIED:
                System.out.println("This table is occupied.");
                break;

            case WAITER_NOT_FINISHED:
                System.out.println("The Waiter must act before the Chef.");
                break;

            case WAITER_ALREADY_ACTED:
                System.out.println("The Waiter has already acted this turn.");
                break;

            case CHEF_ALREADY_ACTED:
                System.out.println("The Chef has already acted this turn.");
                break;

            case HOST_ALREADY_ACTED:
                System.out.println("The Host has already acted this turn.");
                break;

            case ORDER_ALREADY_EXISTS:
                System.out.println("This customer already has an order.");
                break;

            case ORDER_NOT_FOUND:
                System.out.println("No order was found.");
                break;

            case ORDER_NOT_PLACED:
                System.out.println(
                        "This order is no longer new and cannot be assigned to a customer.");
                break;

            case ORDER_NOT_READY:
                System.out.println("This order is not ready to serve.");
                break;

            case ORDER_ALREADY_PREPARED:
                System.out.println("This order is already fully prepared.");
                break;

            case ORDER_CANCELLED:
                System.out.println("This order was cancelled and cannot be prepared.");
                break;
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // Centers headings using the same width as the customer table.
    private void displayBanner(String title) {
        int remaining = DISPLAY_WIDTH - title.length();
        int left = remaining / 2;
        int right = remaining - left;
        System.out.println("=".repeat(left) + title + "=".repeat(right));
    }

    private String customerIds(List<Customer> customers) {
        StringJoiner ids = new StringJoiner(", ");

        for (Customer customer : customers) {
            ids.add(Integer.toString(customer.getId()));
        }

        return customers.isEmpty() ? "None" : ids.toString();
    }

    private String freeTableIds() {
        StringJoiner ids = new StringJoiner(", ");

        for (Table table : controller.getFreeTables()) {
            ids.add(Integer.toString(table.getId()));
        }

        return ids.toString();
    }

    private String preparationText(Order order) {
        return order.getCompletedUnits() + "/"
                + order.getMenuItem().getPreparationUnits();
    }

    private String averageSatisfactionText(GameState state) {
        double average = state.getAverageSatisfaction();
        return average < 0 ? "N/A" : String.format("%.1f", average);
    }
}
