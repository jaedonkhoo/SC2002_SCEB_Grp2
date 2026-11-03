public class VIPCustomer extends Customer {
    public VIPCustomer(int id) {
        super(id);
    }

    @Override
    public String getCustomerType() {
        return "VIP";
    }

    @Override
    public int getSatisfactionLoss() {
        return 5;
    }

    @Override
    public int calculatePriceCents(int basePriceCents) {
        // Adding 5 before division rounds the discounted price to the nearest cent.
        return (basePriceCents * 9 + 5) / 10;
    }
}
