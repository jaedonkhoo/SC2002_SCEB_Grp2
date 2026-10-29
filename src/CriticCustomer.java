import java.util.List;

/** A Critic is impatient and has a documented preference for Pasta. */
public class CriticCustomer extends Customer implements Stage2CustomerBehavior {
    public CriticCustomer(int id) {
        super(id);
    }

    @Override
    public int getSatisfactionLoss() {
        return 12;
    }

    @Override
    public MenuItem chooseFood(List<MenuItem> menu, int customerId) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }
        for (MenuItem item : menu) {
            if (item != null && "Pasta".equals(item.getName())) {
                return item;
            }
        }
        throw new IllegalStateException("Critic preference requires Pasta on the menu.");
    }

    @Override
    public int billPriceCents(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        return item.getPriceCents();
    }

    @Override
    public String customerTypeName() {
        return "Critic";
    }
}
