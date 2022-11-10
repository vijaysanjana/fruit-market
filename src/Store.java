import java.util.*;
import java.io.*;


/**
 * Store class contains descriptive information, owning Seller, list of Products, and list of Sales of the Store.
 */
public class Store {

    private String name; //Store's name
    private String description; //Store's description
    private ArrayList<Product> products; //ArrayList of products owned by the Store
    private ArrayList<Sale> sales; // ArrayList of sales made at the Store


    //Constructors
    public Store(String name, String description) {
        this.name = name;
        this.description = description;
        this.products = new ArrayList<>();
        this.sales = new ArrayList<>();
    }

    public Store(String name, String description, ArrayList<Product> products) {
        this.name = name;
        this.description = description;
        this.products = products;
        this.sales = new ArrayList<>();
    }

    public Store(String name, String description, ArrayList<Product> products, ArrayList<Sale> sales) {
        this.name = name;
        this.description = description;
        this.products = products;
        this.sales = sales;
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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Sale> sales) {
        this.sales = sales;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Store)) {
            return false;
        }
        Store store = (Store) o;
        return (name.equals(store.getName()) && description.equals(store.getDescription()) &&
                products.equals(store.getProducts()) && sales.equals(store.getSales()));
    }


    /**
     * Adds a new product to the store's catalogue
     * @param product the product to add to the store
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Gets the specified product from the store's catalogue
     * @param index the index of the desired product
     * @return the desired product
     */
    public Product getProduct(int index) {
        return products.get(index);
    }

    /**
     * Removes the specified product from the store's catalogue
     * @param index the index of the desired product
     * @return the removed product
     */
    public Product removeProduct(int index) {
        return products.remove(index);
    }

    /**
     * Removes the specified product from the store's catalogue
     * @param product the desired product
     * @return the removed product
     */
    public Product removeProduct(Product product) {
        if (products.contains(product)) {
            return products.remove(products.indexOf(product));
        }
        return null;
    }


    /**
     * Adds a new sale to the ShoppingCart
     * @param sale the sale to add to the ShoppingCart
     */
    public void addSale(Sale sale) {
        sales.add(sale);
    }

    /**
     * Gets the specified sale from the ShoppingCart
     * @param index the index of the desired sale
     * @return the desired sale
     */
    public Sale getSale(int index) {
        return sales.get(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     * @param index the index of the desired sale
     * @return the removed sale
     */
    public Sale removeSale(int index) {
        return sales.remove(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     * @param sale the desired sale
     * @return the removed sale
     */
    public Sale removeSale(Sale sale) {
        if (sales.contains(sale)) {
            return sales.remove(sales.indexOf(sale));
        }
        return null;
    }



}