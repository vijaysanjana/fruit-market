import java.util.*;


/**
 * MarketPlace class contains the all sellers and customers in the application.
 */
public class MarketPlace {

    private ArrayList<Seller> sellers; //ArrayList of seller users present in marketplace
    private ArrayList<Customer> customers; //ArrayList of customer users present in marketplace


    //Constructors
    public MarketPlace() {
        this.sellers = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public MarketPlace(ArrayList<Seller> sellers, ArrayList<Customer> customers) {
        this.sellers = sellers;
        this.customers = customers;
    }


    //Getters and Setters
    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Seller> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Store> getStores() {
        ArrayList<Store> stores = new ArrayList<>();
        for (Seller seller : sellers) {
            for (Store store : seller.getStores()) {
                stores.add(store);
            }
        }
        return stores;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MarketPlace)) {
            return false;
        }
        MarketPlace marketPlace = (MarketPlace) o;
        return (sellers.equals(marketPlace.getSellers()) && customers.equals(marketPlace.getCustomers()));
    }


    /**
     * Adds a new seller to the MarketPlace
     * @param seller the seller to add to the MarketPlace
     */
    public void addSeller(Seller seller) {
        sellers.add(seller);
    }

    /**
     * Gets the specified seller from the MarketPlace
     * @param index the index of the desired seller
     * @return the desired seller
     */
    public Seller getSeller(int index) {
        return sellers.get(index);
    }

    /**
     * Removes the specified seller from the MarketPlace
     * @param index the index of the desired seller
     * @return the removed seller
     */
    public Seller removeSeller(int index) {
        return sellers.remove(index);
    }

    /**
     * Removes the specified seller from the MarketPlace
     * @param seller the desired seller
     * @return the removed seller
     */
    public Seller removeSeller(Seller seller) {
        if (sellers.contains(seller)) {
            return sellers.remove(sellers.indexOf(seller));
        }
        return null;
    }


    /**
     * Adds a new customer to the MarketPlace
     * @param customer the customer to add to the MarketPlace
     */
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Gets the specified customer from the MarketPlace
     * @param index the index of the desired customer
     * @return the desired customer
     */
    public Customer getCustomer(int index) {
        return customers.get(index);
    }

    /**
     * Removes the specified customer from the MarketPlace
     * @param index the index of the desired customer
     * @return the removed customer
     */
    public Customer removeCustomer(int index) {
        return customers.remove(index);
    }

    /**
     * Removes the specified customer from the MarketPlace
     * @param customer the desired customer
     * @return the removed customer
     */
    public Customer removeCustomer(Customer customer) {
        if (customers.contains(customer)) {
            return customers.remove(customers.indexOf(customer));
        }
        return null;
    }
}