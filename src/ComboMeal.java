public class ComboMeal extends MenuItem {
    private final MenuItem firstItem;
    private final MenuItem secondItem;

    public ComboMeal(MenuItem firstItem, MenuItem secondItem) {
        super(
                createComboName(firstItem, secondItem),
                ((firstItem.getPriceCents() + secondItem.getPriceCents()) * 9 + 5) / 10,
                firstItem.getPreparationUnits() + secondItem.getPreparationUnits());

        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public MenuItem getFirstItem() {
        return firstItem;
    }

    public MenuItem getSecondItem() {
        return secondItem;
    }

    private static String createComboName(MenuItem first, MenuItem second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Combo items cannot be null.");
        }

        if (first.getName().equals(second.getName())) {
            throw new IllegalArgumentException("Combo items must be different.");
        }

        if (first instanceof ComboMeal || second instanceof ComboMeal) {
            throw new IllegalArgumentException("Combos must contain base menu items.");
        }

        return first.getName() + " + " + second.getName() + " Combo";
    }
}
