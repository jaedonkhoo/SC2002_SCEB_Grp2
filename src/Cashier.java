public class Cashier {
    public boolean processPayment(Customer customer) {
        if (customer == null || customer.getStatus() != CustomerStatus.READY_TO_PAY) {
            return false;
        }

        Order order = customer.getOrder();
        if (order == null) {
            return false;
        }
        if (!order.markPaid()) {
            return false;
        }

        return customer.markPaid();
    }
}
