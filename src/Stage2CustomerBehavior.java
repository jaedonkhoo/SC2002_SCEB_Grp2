import java.util.List;

/** This is optional Stage 2 behaviors implemented by customer types added in Stage 2. */
public interface Stage2CustomerBehavior {
    MenuItem chooseFood(List<MenuItem> menu, int customerId);

    int billPriceCents(MenuItem item);

    String customerTypeName();
}
