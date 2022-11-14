import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * Product class containing descriptive information, owning Store, quantity, and price of the Product.
 *
 * @author Jack, Nathan, Sanj, Tommy, Adit
 * @version 11/14/2022
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

        Product p = (Product) o;
        String pPrice = String.format("%.2f", p.getPrice());
        String tPrice = String.format("%.2f", this.getPrice());
        return ((p.getName().equals(this.getName())) && (p.getDescription().equals(this.getDescription()))
                && (pPrice.equalsIgnoreCase(tPrice)) && (p.getQuantity() == this.getQuantity()));
    }

    public String toString() {
        String s = name + "," + description + "," + quantity + "," + price;
        return s;
    }

    public void pushToFile() {
        try {
            File f = new File("productData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("%s:%s;%.2f;%d", getName(), getDescription(), getPrice(), getQuantity()));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
