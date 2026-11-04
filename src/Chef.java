public class Chef extends ControllableStaff {
    public ActionResult prepareOrder(Order order) {
        if (hasActed()) {
            return ActionResult.CHEF_ALREADY_ACTED;
        }
        if (order == null) {
            return ActionResult.ORDER_NOT_FOUND;
        }

        ActionResult result = order.prepareOneUnit();
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        markActed();
        return ActionResult.SUCCESS;
    }

    @Override
    protected ActionResult alreadyActedResult() {
        return ActionResult.CHEF_ALREADY_ACTED;
    }
}
