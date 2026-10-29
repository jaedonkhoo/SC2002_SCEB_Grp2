import java.util.List;

/** A VIP waits more patiently and receives 10% off the bill. */
public class VIPCustomer extends Customer implements Stage2CustomerBehavior {
    public VIPCustomer(int id) {
        super(id);
    }

    @Override
    public int getSatisfactionLoss() {
        return 5;
    }

    @Override
    public MenuItem chooseFood(List<MenuItem> menu, int customerId) {
        if (menu == null || menu.isEmpty()) {
            throw new IllegalArgumentException("Menu must contain at least one item.");
        }
        return menu.get((customerId - 1) % menu.size());
    }

    @Override
    public int billPriceCents(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        // Integer half-up rounding keeps monetary calculations in cents.
        return (item.getPriceCents() * 9 + 5) / 10;
    }

    @Override
    public String customerTypeName() {
        return "VIP";
    }
}
