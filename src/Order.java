public class Order {
    private final MenuItem menuItem;
    private final int finalPriceCents;
    private OrderStatus status;
    private int completedUnits;

    public Order(MenuItem menuItem, int finalPriceCents) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu Item cannot be null.");
        }

        if (finalPriceCents < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        this.menuItem = menuItem;
        this.finalPriceCents = finalPriceCents;
        this.status = OrderStatus.PLACED;
        this.completedUnits = 0;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getFinalPriceCents() {
        return finalPriceCents;
    }

    public OrderStatus getOrderStatus() {
        return status;
    }

    public int getCompletedUnits() {
        return completedUnits;
    }

    public ActionResult checkPreparation() {
        if (status == OrderStatus.CANCELLED) {
            return ActionResult.ORDER_CANCELLED;
        }
        if (status != OrderStatus.PLACED && status != OrderStatus.PREPARING) {
            return ActionResult.ORDER_ALREADY_PREPARED;
        }
        return ActionResult.SUCCESS;
    }

    public ActionResult prepareOneUnit() {
        ActionResult result = checkPreparation();
        if (result != ActionResult.SUCCESS) {
            return result;
        }

        completedUnits += 1;
        if (completedUnits == menuItem.getPreparationUnits()) {
            status = OrderStatus.READY;
        } else {
            status = OrderStatus.PREPARING;
        }
        return ActionResult.SUCCESS;
    }

    public boolean isReadyToServe() {
        return status == OrderStatus.READY;
    }

    public boolean markServed() {
        if (isReadyToServe()) {
            status = OrderStatus.SERVED;
            return true;
        }
        return false;
    }

    public boolean markPaid() {
        if (status == OrderStatus.SERVED) {
            status = OrderStatus.PAID;
            return true;
        }
        return false;
    }

    public boolean cancel() {
        if (status == OrderStatus.PLACED
                || status == OrderStatus.PREPARING
                || status == OrderStatus.READY) {
            status = OrderStatus.CANCELLED;
            return true;
        }
        return false;
    }
}
