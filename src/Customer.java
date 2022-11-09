import java.util.*;


/**
 * Customer class contains the ShoppingCart owned by the Customer and the Customer's purchase history.
 * Customer extends User.
 */
public class Customer extends User {

    private ShoppingCart shoppingCart; //ShoppingCart owned by the Customer
    private ArrayList<Sale> purchases; //ArrayList of the Customer's past purchases (Sale objects)


    //Constructor
    public Customer(String username, String email, String password, ShoppingCart shoppingCart) {
        super(username, email, password);
        this.shoppingCart = shoppingCart;
        this.purchases = new ArrayList<>();
    }


    //Getters and Setters
    public Customer(String username, String email, String password, ShoppingCart shoppingCart, ArrayList<Sale> purchases) {
        super(username, email, password);
        this.shoppingCart = shoppingCart;
        this.purchases = purchases;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<Sale> getPurchases() {
        return purchases;
    }

    public void setPurchases(ArrayList<Sale> purchases) {
        this.purchases = purchases;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) o;
        return (super.equals(customer) && shoppingCart.equals(customer.getShoppingCart()) &&
                purchases.equals(customer.getPurchases()));
    }

    /**
     * Adds a new sale to the purchase history
     * @param sale the sale to add to the purchase history
     */
    public void addSale(Sale sale) {
        purchases.add(sale);
    }

    /**
     * Gets the specified sale from the purchase history
     * @param index the index of the desired sale
     * @return the desired sale
     */
    public Sale getSale(int index) {
        return purchases.get(index);
    }

    /**
     * Removes the specified sale from the purchase history
     * @param index the index of the desired sale
     * @return the removed sale
     */
    public Sale removeSale(int index) {
        return purchases.remove(index);
    }

    /**
     * Removes the specified sale from the purchase history
     * @param sale the desired sale
     * @return the removed sale
     */
    public Sale removeSale(Sale sale) {
        if (purchases.contains(sale)) {
            return purchases.remove(purchases.indexOf(sale));
        }
        return null;
    }
}
