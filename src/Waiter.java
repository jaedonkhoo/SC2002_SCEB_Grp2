public class Waiter extends SeatingStaff {
    public ActionResult takeOrder(Customer customer, Order order) {
        if (hasActed()) {
            return alreadyActedResult();
        }
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }

        ActionResult result = customer.placeOrder(order);
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        markActed();
        return ActionResult.SUCCESS;
    }

    // Checks serving eligibility without serving food or using the Waiter's task.
    public ActionResult checkServeOrder(Customer customer) {
        if (hasActed()) {
            return alreadyActedResult();
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
        markActed();
        return ActionResult.SUCCESS;
    }

    @Override
    protected ActionResult alreadyActedResult() {
        return ActionResult.WAITER_ALREADY_ACTED;
    }
}
