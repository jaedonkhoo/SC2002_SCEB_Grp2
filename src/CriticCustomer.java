import java.util.List;

public class CriticCustomer extends Customer {
    public CriticCustomer(int id) {
        super(id);
    }

    @Override
    public String getCustomerType() {
        return "Critic";
    }

    @Override
    public int getSatisfactionLoss() {
        return 12;
    }

    @Override
    public MenuItem chooseMenuItem(List<MenuItem> menu) {
        // Critic chooses most expensive item on the menu.
        MenuItem mostExpensive = menu.get(0);
        for (MenuItem item : menu) {
            // If there are ties, the first occurring item is chosen.
            if (item.getPriceCents() > mostExpensive.getPriceCents()) {
                mostExpensive = item;
            }
        }
        return mostExpensive;
    }
}
