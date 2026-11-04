public abstract class SeatingStaff extends ControllableStaff {
    public ActionResult seatCustomer(Customer customer, Table table) {
        if (hasActed()) {
            return alreadyActedResult();
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
        markActed();
        return ActionResult.SUCCESS;
    }
}
