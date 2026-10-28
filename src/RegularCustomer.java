public class RegularCustomer extends Customer {
    public RegularCustomer(int id) {
        super(id);
    }

    @Override
    public int getSatisfactionLoss() {
        return 8;
    }
}
