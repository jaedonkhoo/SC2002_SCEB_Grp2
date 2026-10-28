public class Chef {
    private boolean acted;

    public Chef() {
        this.acted = false;
    }

    public boolean hasActed() {
        return acted;
    }

    public void resetForNewTurn() {
        acted = false;
    }

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

        acted = true;
        return ActionResult.SUCCESS;
    }
}
