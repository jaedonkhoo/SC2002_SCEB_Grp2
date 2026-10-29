public class Waiter {
    private boolean acted;

    public Waiter() {
        this.acted = false;
    }

    public boolean hasActed() {
        return acted;
    }

    public void resetForNewTurn() {
        acted = false;
    }

    public ActionResult seatCustomer(Customer customer, Table table) {
        if (hasActed()) {
            return ActionResult.WAITER_ALREADY_ACTED;
        }
        if (table == null) {
            return ActionResult.TABLE_NOT_FOUND;
        }

        ActionResult result = table.seatCustomer(customer);
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        // Successful table seating already established that the customer is WAITING.
        customer.markSeated();
        acted = true;
        return ActionResult.SUCCESS;
    }

    public ActionResult takeOrder(Customer customer, Order order) {
        if (hasActed()) {
            return ActionResult.WAITER_ALREADY_ACTED;
        }
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }

        ActionResult result = customer.placeOrder(order);
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        acted = true;
        return ActionResult.SUCCESS;
    }

    // Checks serving eligibility without serving food or using the Waiter's task.
    public ActionResult checkServeOrder(Customer customer) {
        if (hasActed()) {
            return ActionResult.WAITER_ALREADY_ACTED;
        }
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        if (customer.getStatus() != CustomerStatus.SEATED) {
            return ActionResult.CUSTOMER_NOT_SEATED;
        }

        Order order = customer.getOrder();
        if (order == null) {
            return ActionResult.ORDER_NOT_FOUND;
        }
        if (!order.isReadyToServe()) {
            return ActionResult.ORDER_NOT_READY;
        }
        return ActionResult.SUCCESS;
    }

    public ActionResult serveOrder(Customer customer) {
        ActionResult result = checkServeOrder(customer);
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        // The successful check establishes the prerequisites for both transitions.
        customer.getOrder().markServed();
        customer.markJustServed();
        acted = true;
        return ActionResult.SUCCESS;
    }
}
