import java.lang.reflect.Array;
import java.util.*;


/**
 * MarketPlace class contains the all sellers and customers in the application.
 *
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
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
    public synchronized ArrayList<Seller> getSellers() {
        return sellers;
    }

    public synchronized void setSellers(ArrayList<Seller> sellers) {
        this.sellers = sellers;
    }

    public synchronized ArrayList<Customer> getCustomers() {
        return customers;
    }

    public synchronized void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public synchronized ArrayList<Store> getStores() {
        ArrayList<Store> stores = new ArrayList<>();
        for (Seller seller : sellers) {
            for (Store store : seller.getStores()) {
                stores.add(store);
            }
        }
        return stores;
    }

    public synchronized ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Store store : getStores()) {
            for (Product product : store.getProducts()) {
                products.add(product);
            }
        }
        return products;
    }

    public synchronized ArrayList<Product> searchProducts(String type, String str) {
        ArrayList<Product> products = getProducts();
        if (type.equalsIgnoreCase("name")) {
            ArrayList<Product> results = new ArrayList<>();
            for (Product p : products) {
                if (p.getName().toLowerCase().contains(str.toLowerCase())) {
                    results.add(p);
                }
            }
            return results;
        } else if (type.equalsIgnoreCase("desc")) {
            ArrayList<Product> results = new ArrayList<>();
            for (Product p : products) {
                if (p.getDescription().toLowerCase().contains(str.toLowerCase())) {
                    results.add(p);
                }
            }
            return results;
        }
        return null;
    }

    public synchronized ArrayList<Store> searchStores(String str) {
        ArrayList<Store> stores = getStores();
        ArrayList<Store> results = new ArrayList<>();
        for (Store s : stores) {
            if (s.getName().toLowerCase().contains(str.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public synchronized ArrayList<ShoppingCart> getShoppingCarts() {
        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        for (Customer customer : customers) {
            shoppingCarts.add(customer.getShoppingCart());
        }
        return shoppingCarts;
    }

    public synchronized ArrayList<Sale> getPurchases() {
        ArrayList<Sale> purchases = new ArrayList<>();
        for (Customer customer : customers) {
            for (Sale purchase : customer.getPurchases()) {
                purchases.add(purchase);
            }
        }
        return purchases;
    }


    /**
     * Sorts a list of every Store in the MarketPlace based on sales.
     *
     * @param maxFirst true: sorts from high sales to low sales, false: sorts from low sales to high sales
     * @return the sorted list of Stores
     */
    public synchronized ArrayList<Store> getSalesSortedStores(boolean maxFirst) {
        ArrayList<Store> copiedStores = new ArrayList<>();
        for (Store store : getStores()) {
            copiedStores.add(store);
        }
        ArrayList<Store> sortedStores = new ArrayList<>();

        while (copiedStores.size() > 0) {
            Store min = copiedStores.get(0);
            for (int i = 1; i < copiedStores.size(); i++) {
                if (copiedStores.get(i).getTotalSoldProducts() < min.getTotalSoldProducts()) {
                    min = copiedStores.get(i);
                }
            }
            copiedStores.remove(min);
            sortedStores.add(min);
        }
        if (maxFirst) {
            Collections.reverse(sortedStores);
        }
        return sortedStores;
    }


    /**
     * Sorts a list of every Store in the MarketPlace based on sales to a specified user.
     *
     * @param maxFirst true: sorts from high sales to low sales, false: sorts from low sales to high sales
     * @return the sorted list of Stores
     */
    public synchronized ArrayList<Store> getUserSalesSortedStores(User user, boolean maxFirst) {
        ArrayList<Store> copiedStores = new ArrayList<>();
        for (Store store : getStores()) {
            copiedStores.add(store);
        }
        ArrayList<Store> sortedStores = new ArrayList<>();

        while (copiedStores.size() > 0) {
            Store min = copiedStores.get(0);
            for (int i = 1; i < copiedStores.size(); i++) {
                if (copiedStores.get(i).getQuantityOfProductsBoughtByCustomer((Customer) user) < min.getQuantityOfProductsBoughtByCustomer((Customer) user)) {
                    min = copiedStores.get(i);
                }
            }
            copiedStores.remove(min);
            sortedStores.add(min);
        }
        if (maxFirst) {
            Collections.reverse(sortedStores);
        }
        return sortedStores;
    }

    public synchronized ArrayList<Customer> getStoreSalesSortedCustomers(Store store, boolean maxFirst) {
        ArrayList<Customer> customers = store.getAllCustomers();
        ArrayList<Customer> copiedCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            copiedCustomers.add(customer);
        }
        ArrayList<Customer> sortedCustomers = new ArrayList<>();

        while (copiedCustomers.size() > 0) {
            Customer min = copiedCustomers.get(0);
            for (int i = 1; i < copiedCustomers.size(); i++) {
                if (store.getQuantityOfProductsBoughtByCustomer(copiedCustomers.get(i)) < store.getQuantityOfProductsBoughtByCustomer(min)) {
                    min = copiedCustomers.get(i);
                }
            }
            copiedCustomers.remove(min);
            sortedCustomers.add(min);
        }
        if (maxFirst) {
            Collections.reverse(sortedCustomers);
        }
        return sortedCustomers;
    }

    public synchronized ArrayList<Product> getStoreSalesSortedProducts(Store store, boolean maxFirst) {
        ArrayList<Product> products = store.getProducts();
        ArrayList<Product> copiedProducts = new ArrayList<>();
        for (Product product : products) {
            copiedProducts.add(product);
        }
        ArrayList<Product> sortedProducts = new ArrayList<>();

        while (copiedProducts.size() > 0) {
            Product min = copiedProducts.get(0);
            for (int i = 1; i < copiedProducts.size(); i++) {
                if (store.getNumberOfProductsSold(copiedProducts.get(i)) < store.getNumberOfProductsSold(min)) {
                    min = copiedProducts.get(i);
                }
            }
            copiedProducts.remove(min);
            sortedProducts.add(min);
        }
        if (maxFirst) {
            Collections.reverse(sortedProducts);
        }
        return sortedProducts;
    }


    //Equals
    @Override
    public synchronized boolean equals(Object o) {
        if (!(o instanceof MarketPlace)) {
            return false;
        }
        MarketPlace marketPlace = (MarketPlace) o;
        return (sellers.equals(marketPlace.getSellers()) && customers.equals(marketPlace.getCustomers()));
    }


    /**
     * Adds a new seller to the MarketPlace
     *
     * @param seller the seller to add to the MarketPlace
     */
    public synchronized void addSeller(Seller seller) {
        sellers.add(seller);
    }

    /**
     * Gets the specified seller from the MarketPlace
     *
     * @param index the index of the desired seller
     * @return the desired seller
     */
    public synchronized Seller getSeller(int index) {
        return sellers.get(index);
    }

    /**
     * Removes the specified seller from the MarketPlace
     *
     * @param index the index of the desired seller
     * @return the removed seller
     */
    public synchronized Seller removeSeller(int index) {
        return sellers.remove(index);
    }

    /**
     * Removes the specified seller from the MarketPlace
     *
     * @param seller the desired seller
     * @return the removed seller
     */
    public synchronized Seller removeSeller(Seller seller) {
        if (sellers.contains(seller)) {
            return sellers.remove(sellers.indexOf(seller));
        }
        return null;
    }


    /**
     * Adds a new customer to the MarketPlace
     *
     * @param customer the customer to add to the MarketPlace
     */
    public synchronized void addCustomer(Customer customer) {
        customers.add(customer);
    }

    /**
     * Gets the specified customer from the MarketPlace
     *
     * @param index the index of the desired customer
     * @return the desired customer
     */
    public synchronized Customer getCustomer(int index) {
        return customers.get(index);
    }

    /**
     * Removes the specified customer from the MarketPlace
     *
     * @param index the index of the desired customer
     * @return the removed customer
     */
    public synchronized Customer removeCustomer(int index) {
        return customers.remove(index);
    }

    /**
     * Removes the specified customer from the MarketPlace
     *
     * @param customer the desired customer
     * @return the removed customer
     */
    public synchronized Customer removeCustomer(Customer customer) {
        if (customers.contains(customer)) {
            return customers.remove(customers.indexOf(customer));
        }
        return null;
    }

    //get Store object by store name
    public synchronized Store getStore(String storeName) {
        for (Store s : getStores())
            if (s.getName().equalsIgnoreCase(storeName))
                return s;
        return null;
    }

    //gets user object by User's email
    public synchronized User getUser(String email) {
        for (User u : getCustomers())
            if (u.getEmail().equalsIgnoreCase(email))
                return u;
        for (User u : getSellers())
            if (u.getEmail().equalsIgnoreCase(email))
                return u;
        return null;
    }

    //gets product object by product's name
    public synchronized Product getProduct(String productName) {
        for (Product p : getProducts())
            if (p.getName().equalsIgnoreCase(productName))
                return p;
        return null;
    }

    public synchronized Sale getSale(String saleName) {

        for (Customer c : getCustomers()) {
            for (Sale s : c.getShoppingCart().getHeldPurchases()) {
                if (s.getName().equals(saleName)) {
                    return s;
                }
            }
            for (Sale s : c.getPurchases()) {
                if (s.getName().equals(saleName)) {
                    return s;
                }
            }
        }
        return null;
        /*
        for (Customer c : getCustomers())
            if (saleName.contains(c.getUsername()))
                return c.getShoppingCart().getHeldPurchases().get(c.getShoppingCart().getHeldPurchases().indexOf(saleName));
        return null;
        */
    }
}