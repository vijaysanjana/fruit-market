import java.util.*;


/**
 * Product class containing descriptive information, owning Store, quantity, and price of the Product.
 */
public class Product {

    private String name; //Product's name
    private String description; //Product's description
    private double price; //Product's price
    private int quantity; //the number of this Product available for purchase


    //Constructors
    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 1;
    }

    public Product(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }


    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return (name.equals(product.getName()) && description.equals(product.getDescription()) &&
                quantity == product.getQuantity() && price == product.getPrice());
    }

    public String toString() {
        String s = name + "," + description + "," + quantity + "," + price;
        return s;
    }
}
