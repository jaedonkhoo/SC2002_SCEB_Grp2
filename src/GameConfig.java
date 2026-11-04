import java.util.List;

public final class GameConfig {
    public static final int MAX_TURNS = 18;
    public static final int TABLE_COUNT = 2;
    public static final List<Integer> ARRIVAL_TURNS = List.of(1, 4, 7, 10, 13, 16);

    public static final int TARGET_PAID_CUSTOMERS = 4;
    public static final int TARGET_REVENUE_CENTS = 4500;
    public static final int TARGET_AVG_SATISFACTION = 60;

    private GameConfig() {
    }

    public static List<MenuItem> createStartingMenu() {
        MenuItem salad = new MenuItem("Salad", 900, 1);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);

        return List.of(
                salad,
                burger,
                pasta,
                new ComboMeal(salad, burger),
                new ComboMeal(burger, pasta),
                new ComboMeal(salad, pasta));
    }
}
