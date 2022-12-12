import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * Sale class contains buying Customer, which Product and how many are to be sold, and the TotalCost of the Sale.
 *
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
public class Sale {

    private Customer customer; //The buying Customer
    private Product product; //The Product to be sold
    private int quantity; //The number of Products to be sold
    private double totalCost; //The total cost of the sale
    private String name; //The customer username + (amount of purchases + 1)


    //Constructors
    public Sale(Customer customer, Product product) {
        this.customer = customer;
        this.product = product;
        this.quantity = 1;
        this.totalCost = product.getPrice();
        //this.name = customer.getUsername() + customer.getPurchases().size();
        this.name = customer.getUsername() + product.getName() + 1;
    }

    public Sale(Customer customer, Product product, int quantity) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalCost = product.getPrice() * quantity;
        //this.name = customer.getUsername() + customer.getPurchases().size();
        this.name = customer.getUsername() + product.getName() + quantity;
    }

    public Sale(Customer customer, Product product, int quantity, double totalCost) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalCost = totalCost;
        //this.name = customer.getUsername() + customer.getPurchases().size();
        this.name = customer.getUsername() + product.getName() + quantity;
    }


    //Getters and Setters
    public synchronized Customer getCustomer() {
        return customer;
    }

    public synchronized void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public synchronized Product getProduct() {
        return product;
    }

    public synchronized void setProduct(Product product) {
        this.product = product;
    }

    public synchronized int getQuantity() {
        return quantity;
    }

    public synchronized void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalCost = product.getPrice() * quantity;
    }

    public synchronized double getTotalCost() {
        return totalCost;
    }

    public synchronized void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    //Equals
    @Override
    public synchronized boolean equals(Object o) {
        if (!(o instanceof Sale)) {
            return false;
        }
        Sale sale = (Sale) o;
        return (customer.equals(sale.getCustomer()) && product.equals(sale.getProduct()) &&
                quantity == sale.getQuantity() && totalCost == sale.getTotalCost());
    }

    public synchronized void pushToFile() {
        try {
            File f = new File("saleData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("%s:%s;%d;%.2f", getName(), getProduct().getName(), getQuantity(), getTotalCost()));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }


}