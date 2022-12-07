import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * Customer class contains the ShoppingCart owned by the Customer and the Customer's purchase history.
 * Customer extends User.
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class Customer extends User {

    private ShoppingCart shoppingCart; //ShoppingCart owned by the Customer
    private ArrayList<
            Sale> purchases; //ArrayList of the Customer's past purchases (Sale objects)
    private int totalPurchasedProducts = 0; //The number of products purchased by the Customer

    /**
     * Constructs customer
     *
     * @param username
     * @param email
     * @param password
     */
    //Constructor
    public Customer(String username, String email, String password) {
        super(username, email, password);
        this.shoppingCart = new ShoppingCart();
        this.purchases = new ArrayList<>();
    }


    /**
     * Constructs customer
     *
     * @param username
     * @param email
     * @param password
     * @param shoppingCart
     * @param purchases
     */
    //Getters and Setters
    public Customer(String username, String email, String password,
                    ShoppingCart shoppingCart, ArrayList<Sale> purchases) {
        super(username, email, password);
        this.shoppingCart = shoppingCart;
        this.purchases = purchases;
    }

    public void pushToFile() {
        try {
            File f = new File("userData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("C:%s;%s;%s", getUsername(), getEmail(), getPassword()));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int totalPurchasedProducts = 0;
        for (Sale sale : purchases) {
            totalPurchasedProducts += sale.getQuantity();
        }
        this.totalPurchasedProducts = totalPurchasedProducts;
    }

    public int getTotalPurchasedProducts() {
        return totalPurchasedProducts;
    }

    public void setTotalPurchasedProducts(int totalPurchasedProducts) {
        this.totalPurchasedProducts = totalPurchasedProducts;
    }

    /**
     * Checks if 2 objects equal
     *
     * @param o
     * @return
     */
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
     *
     * @param sale the sale to add to the purchase history
     */
    public void addSale(Sale sale) {
        totalPurchasedProducts += sale.getQuantity();
        purchases.add(sale);
    }

    /**
     * Gets the specified sale from the purchase history
     *
     * @param index the index of the desired sale
     * @return the desired sale
     */
    public Sale getSale(int index) {
        return purchases.get(index);
    }

    /**
     * Removes the specified sale from the purchase history
     *
     * @param index the index of the desired sale
     * @return the removed sale
     */
    public Sale removeSale(int index) {
        return purchases.remove(index);
    }

    /**
     * Removes the specified sale from the purchase history
     *
     * @param sale the desired sale
     * @return the removed sale
     */
    public Sale removeSale(Sale sale) {
        if (purchases.contains(sale)) {
            return purchases.remove(purchases.indexOf(sale));
        }
        return null;
    }

    /**
     * Exports a purchase csv
     *
     * @throws IOException
     */
    public void exportPurchases() throws IOException {
        ArrayList<Sale> history = this.getPurchases();
        String fileName = this.getUsername() + ".txt";
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f, false);
        PrintWriter pw = new PrintWriter(fos);
        if (history == null || history.size() == 0) {
            JOptionPane.showMessageDialog(null, "You have not purchased anything yet!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            for (Sale s : history) {
                pw.println(s.getProduct().getName() + " (" + s.getQuantity() + " ct.) was purchased for $" + String.format("%.2f", s.getTotalCost()));
            }
        }
        fos.close();
        pw.close();
        JOptionPane.showConfirmDialog(null, "A file titled " + this.getUsername() + ".txt has been created with your purchase history!", "File created", JOptionPane.PLAIN_MESSAGE);
    }
}