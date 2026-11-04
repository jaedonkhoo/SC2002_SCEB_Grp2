import java.util.List;

public abstract class Customer {
    private final int id;
    private int satisfaction;
    private CustomerStatus status;
    private Order order;

    public Customer(int id) {
        this.id = id;
        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING;
        this.order = null;
    }

    public int getId() {
        return id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }

    public abstract String getCustomerType();

    public abstract int getSatisfactionLoss();

    public void increaseSatisfaction(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Satisfaction increase cannot be negative.");
        }

        int increase = Math.min(amount, 100 - satisfaction);
        satisfaction += increase;
    }

    public void applyWaitingPenalty() {
        // Even a READY dish still counts as waiting for food until the customer is served.
        if (status == CustomerStatus.WAITING || status == CustomerStatus.SEATED) {
            satisfaction = Math.max(0, satisfaction - getSatisfactionLoss());
        }
    }

    public MenuItem chooseMenuItem(List<MenuItem> menu) {
        return menu.get((id - 1) % menu.size());
    }

    public int calculatePriceCents(int basePriceCents) {
        return basePriceCents;
    }

    // Checks customer eligibility without assigning an order or changing the customer's state.
    public ActionResult checkOrderPlacement() {
        if (status != CustomerStatus.SEATED) {
            return ActionResult.CUSTOMER_NOT_SEATED;
        }
        if (this.order != null) {
            return ActionResult.ORDER_ALREADY_EXISTS;
        }
        return ActionResult.SUCCESS;
    }

    public ActionResult placeOrder(Order order) {
        ActionResult result = checkOrderPlacement();
        if (result != ActionResult.SUCCESS) {
            return result;
        }
        if (order == null) {
            return ActionResult.ORDER_NOT_FOUND;
        }
        if (order.getOrderStatus() != OrderStatus.PLACED) {
            return ActionResult.ORDER_NOT_PLACED;
        }

        this.order = order;
        return ActionResult.SUCCESS;
    }

    public boolean markSeated() {
        if (status == CustomerStatus.WAITING) {
            status = CustomerStatus.SEATED;
            return true;
        }
        return false;
    }

    public boolean markJustServed() {
        if (status != CustomerStatus.SEATED || order == null) {
            return false;
        }
        if (order.getOrderStatus() != OrderStatus.SERVED) {
            return false;
        }

        status = CustomerStatus.JUST_SERVED;
        return true;
    }

    public boolean markEating() {
        if (status == CustomerStatus.JUST_SERVED) {
            status = CustomerStatus.EATING;
            return true;
        }
        return false;
    }

    public boolean markReadyToPay() {
        if (status == CustomerStatus.EATING) {
            status = CustomerStatus.READY_TO_PAY;
            return true;
        }
        return false;
    }

    public boolean markPaid() {
        if (status == CustomerStatus.READY_TO_PAY) {
            status = CustomerStatus.PAID;
            return true;
        }
        return false;
    }

    public boolean markLeft() {
        if (satisfaction != 0) {
            return false;
        }
        if (status != CustomerStatus.WAITING && status != CustomerStatus.SEATED) {
            return false;
        }

        status = CustomerStatus.LEFT;
        return true;
    }
}
