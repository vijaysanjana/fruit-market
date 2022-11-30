import java.util.*;


/**
 * Sale class contains buying Customer, which Product and how many are to be sold, and the TotalCost of the Sale.
 */
public class Sale {

    private Customer customer; //The buying Customer
    private Product product; //The Product to be sold
    private int quantity; //The number of Products to be sold
    private double totalCost; //The total cost of the sale


    //Constructors
    public Sale(Customer customer, Product product) {
        this.customer = customer;
        this.product = product;
        this.quantity = 1;
        this.totalCost = product.getPrice();
    }

    public Sale(Customer customer, Product product, int quantity) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalCost = product.getPrice() * quantity;
    }


    //Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sale)) {
            return false;
        }
        Sale sale = (Sale) o;
        return (customer.equals(sale.getCustomer()) && product.equals(sale.getProduct()) &&
                quantity == sale.getQuantity() && totalCost == sale.getTotalCost());
    }
}