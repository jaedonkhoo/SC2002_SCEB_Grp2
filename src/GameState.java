import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameState {
    private final ArrayList<Customer> waitingCustomers;
    private final ArrayList<Customer> customers;
    private final ArrayList<Table> tables;
    private final Set<Integer> recordedPaymentCustomerIds;
    private int currentTurn;
    private int revenueCents;
    private int totalPaidSatisfaction;
    private int servedCount;
    private int unhappyDepartures;

    public GameState() {
        this.currentTurn = 1;
        this.waitingCustomers = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.recordedPaymentCustomerIds = new HashSet<>();
        for (int i = 1; i <= GameConfig.TABLE_COUNT; i++) {
            tables.add(new Table(i));
        }
        this.revenueCents = 0;
        this.totalPaidSatisfaction = 0;
        this.servedCount = 0;
        this.unhappyDepartures = 0;
    }

    public List<Customer> getWaitingCustomers() {
        // Returns an unmodifiable list view, the customer objects themselves remain mutable.
        return Collections.unmodifiableList(waitingCustomers);
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCustomerCount() {
        return recordedPaymentCustomerIds.size();
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public double getAverageSatisfaction() {
        int paidCustomerCount = getPaidCustomerCount();
        if (paidCustomerCount > 0) {
            return (double) totalPaidSatisfaction / paidCustomerCount;
        }
        // No payment average exists yet; the CLI displays this as N/A.
        return -1;
    }

    public boolean addCustomer(Customer customer) {
        if (customer == null || customer.getStatus() != CustomerStatus.WAITING) {
            return false;
        }
        if (findCustomerById(customer.getId()) != null) {
            return false;
        }

        customers.add(customer);
        waitingCustomers.add(customer);
        return true;
    }

    public boolean removeWaitingCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }

        CustomerStatus status = customer.getStatus();
        if (status != CustomerStatus.SEATED && status != CustomerStatus.LEFT) {
            return false;
        }
        return waitingCustomers.remove(customer);
    }

    public Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    public Table findTableById(int id) {
        for (Table table : tables) {
            if (table.getId() == id) {
                return table;
            }
        }
        return null;
    }

    public Table findTableByCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }

        for (Table table : tables) {
            if (table.getOccupant() == customer) {
                return table;
            }
        }
        return null;
    }

    // Records a customer's bill and payment-time satisfaction at most once per customer ID.
    public boolean recordPayment(Customer customer) {
        if (customer == null || customer.getStatus() != CustomerStatus.PAID) {
            return false;
        }
        // Set.add returns false for an existing ID; its size also supplies the paid count.
        if (!recordedPaymentCustomerIds.add(customer.getId())) {
            return false;
        }

        revenueCents += customer.getOrder().getFinalPriceCents();
        totalPaidSatisfaction += customer.getSatisfaction();
        return true;
    }

    public void recordServedCustomer() {
        servedCount += 1;
    }

    public void recordUnhappyDeparture() {
        unhappyDepartures += 1;
    }

    public boolean advanceTurn() {
        if (currentTurn < GameConfig.MAX_TURNS) {
            currentTurn += 1;
            return true;
        }
        return false;
    }
}
