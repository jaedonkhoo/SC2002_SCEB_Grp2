import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class GameCLI {
    private final GameController controller;
    private final Scanner scanner;

    public GameCLI() {
        this.controller = new GameController();
        this.scanner = new Scanner(System.in);
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

    public void runGame() {
        boolean moreTurns;
        do {
            controller.startTurn();
            displayGameStatus();
            // Keeping this order prevents serving a dish that the Chef finishes this turn.
            waiterInput();
            chefInput();
            int completedTurn = controller.getGameState().getCurrentTurn();
            moreTurns = controller.endTurn();
            displayTurnSummary(completedTurn);
        } while (moreTurns);
        displayFinalResult();
    }

    private void displayGameStatus() {
        GameState state = controller.getGameState();

        System.out.printf("%n====================== TURN %d / %d ======================%n",
                state.getCurrentTurn(), GameConfig.MAX_TURNS);

        System.out.printf("Revenue: $%.2f | Paid-customer satisfaction: %s%n",
                state.getRevenueCents() / 100.0, averageSatisfactionText(state));
        System.out.printf("Served: %d | Paid: %d | Unhappy departures: %d%n",
                state.getServedCount(), state.getPaidCustomerCount(), state.getUnhappyDepartures());

        displayWaitingQueue(state);
        displayTables(state);
        displayCustomers(state);

        System.out.println("\nStaff: Waiter 1 task | Chef 1 prep unit | Cashier 1 automatic payment");
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

        System.out.printf("%-8s %-14s %5s %-9s %-10s %5s%n",
                "ID", "State", "Sat.", "Food", "Order", "Prep");
        System.out.println("--------------------------------------------------------");
        for (Customer customer : state.getCustomers()) {
            Order order = customer.getOrder();
            System.out.printf("%-8d %-14s %5d %-9s %-10s %5s%n",
                    customer.getId(), customer.getStatus(), customer.getSatisfaction(),
                    order == null ? "-" : order.getMenuItem().getName(),
                    order == null ? "-" : order.getOrderStatus(),
                    order == null ? "-" : preparationText(order));
        }
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
        return order.getCompletedUnits() + "/" + order.getMenuItem().getPreparationUnits();
    }

    private String averageSatisfactionText(GameState state) {
        double average = state.getAverageSatisfaction();
        return average < 0 ? "N/A" : String.format("%.1f", average);
    }

    private void displayTurnSummary(int completedTurn) {
        GameState state = controller.getGameState();
        System.out.printf("%nEnd of turn %d | Served: %d | Paid: %d | Unhappy departures: %d%n",
                completedTurn, state.getServedCount(), state.getPaidCustomerCount(),
                state.getUnhappyDepartures());
        System.out.printf("Revenue: $%.2f | Paid-customer satisfaction: %s%n",
                state.getRevenueCents() / 100.0, averageSatisfactionText(state));
    }

    private void waiterInput() {
        List<Customer> toSeat = controller.getCustomersToSeat();
        List<Customer> toOrder = controller.getCustomersToOrder();
        List<Customer> toServe = controller.getCustomersToServe();
        if (toSeat.isEmpty() && toOrder.isEmpty() && toServe.isEmpty()) {
            System.out.println("\nWaiter: no available tasks; waiting automatically.");
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
                    ActionResult seatResult = controller.seatCustomer(seatingCustomerId, tableId);
                    if (seatResult == ActionResult.SUCCESS) {
                        System.out.println("Customer seated successfully!");
                        return;
                    }
                    displayActionFailure(seatResult);
                    break;

                case 2:
                    if (toOrder.isEmpty()) {
                        System.out.println("Taking orders is unavailable: no seated customer needs an order.");
                        break;
                    }
                    int orderingCustomerId = readInt("Input Customer ID: ");
                    ActionResult orderResult = controller.takeOrder(orderingCustomerId);
                    if (orderResult == ActionResult.SUCCESS) {
                        System.out.println("Order taken successfully!");
                        Customer customer = controller.getGameState().findCustomerById(orderingCustomerId);
                        Order order = customer.getOrder();
                        System.out.println("Customer " + orderingCustomerId + " ordered: "
                                + order.getMenuItem().getName());
                        return;
                    }
                    displayActionFailure(orderResult);
                    break;

                case 3:
                    if (toServe.isEmpty()) {
                        System.out.println("Serving is unavailable: no seated customer has a ready order.");
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
                    System.out.println("Waiter: waiting.");
                    return;

                default:
                    System.out.println("Invalid choice. Please choose 0, 1, 2 or 3.");
                    break;
            }
        }
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
                System.out.println("This action requires a seated customer who has not yet been served.");
                break;
            case TABLE_OCCUPIED:
                System.out.println("This table is occupied.");
                break;
            case WAITER_ALREADY_ACTED:
                System.out.println("The Waiter has already acted this turn.");
                break;
            case CHEF_ALREADY_ACTED:
                System.out.println("The Chef has already acted this turn.");
                break;
            case ORDER_ALREADY_EXISTS:
                System.out.println("This customer already has an order.");
                break;
            case ORDER_NOT_FOUND:
                System.out.println("No order was found.");
                break;
            case ORDER_NOT_PLACED:
                System.out.println("This order is no longer new and cannot be assigned to a customer.");
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

    private void displayWaiterMenu(List<Customer> toSeat, List<Customer> toOrder,
            List<Customer> toServe) {
        System.out.println("\n--- Waiter: choose one task ---");
        System.out.println("1. Seat customer  "
                + (toSeat.isEmpty() ? "[unavailable]" : "(customer IDs: " + customerIds(toSeat) + ")"));
        System.out.println("2. Take order     "
                + (toOrder.isEmpty() ? "[unavailable]" : "(customer IDs: " + customerIds(toOrder) + ")"));
        System.out.println("3. Serve order    "
                + (toServe.isEmpty() ? "[unavailable]" : "(customer IDs: " + customerIds(toServe) + ")"));
        System.out.println("0. Wait");
    }

    private void chefInput() {
        // Query after the Waiter acts so an order taken this turn is immediately eligible.
        List<Customer> toPrepare = controller.getCustomersToPrepare();
        if (toPrepare.isEmpty()) {
            System.out.println("\nChef: no orders need preparation; waiting automatically.");
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
                        Order order = controller.getGameState().findCustomerById(customerId).getOrder();
                        System.out.printf("Customer %d: %s preparation %s (%s).%n",
                                customerId, order.getMenuItem().getName(), preparationText(order),
                                order.getOrderStatus());
                        return;
                    }
                    displayActionFailure(result);
                    break;

                case 0:
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
            System.out.printf("Customer %d | %-9s | Prep %s%n",
                    customer.getId(), order.getMenuItem().getName(), preparationText(order));
        }
        System.out.println("1. Prepare Order");
        System.out.println("0. Wait");
    }

    private void displayFinalResult() {
        GameState state = controller.getGameState();

        System.out.println("\n========== GAME OVER ==========");

        if (controller.hasWon()) {
            System.out.println("RESULT: VICTORY!");
        } else {
            System.out.println("RESULT: DEFEAT!");
        }

        System.out.println("\nFinal Statistics:");
        System.out.printf("Turns Played: %d / %d%n",
                state.getCurrentTurn(), GameConfig.MAX_TURNS);

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

        System.out.println("===============================");
    }
}
