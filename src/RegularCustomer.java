public class RegularCustomer extends Customer {
    public RegularCustomer(int id) {
        super(id);
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }

    @Override
    public int getSatisfactionLoss() {
        return 8;
    }
}
