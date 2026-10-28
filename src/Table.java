public class Table {
    private final int id;
    private Customer occupant;

    public Table(int id) {
        this.id = id;
        this.occupant = null;
    }

    public int getId() {
        return id;
    }

    public Customer getOccupant() {
        return occupant;
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    public ActionResult seatCustomer(Customer customer) {
        if (customer == null) {
            return ActionResult.CUSTOMER_NOT_FOUND;
        }
        if (customer.getStatus() != CustomerStatus.WAITING) {
            return ActionResult.CUSTOMER_NOT_WAITING;
        }
        if (isOccupied()) {
            return ActionResult.TABLE_OCCUPIED;
        }

        occupant = customer;
        return ActionResult.SUCCESS;
    }

    public boolean freeTable() {
        if (!isOccupied()) {
            return false;
        }

        CustomerStatus status = occupant.getStatus();
        if (status != CustomerStatus.PAID && status != CustomerStatus.LEFT) {
            return false;
        }

        occupant = null;
        return true;
    }
}
