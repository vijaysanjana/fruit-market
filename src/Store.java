import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.io.*;


/**
 * Store class contains descriptive information, owning Seller, list of Products, and list of Sales of the Store.
 *
 * @author Jack, Nathan, Sanj, Tommy, Adit
 * @version 11/14/2022
 */
public class Store {

    private String name; //Store's name
    private String description; //Store's description
    private ArrayList<Product> products; //ArrayList of products owned by the Store
    private ArrayList<Sale> sales; // ArrayList of sales made at the Store
    private int totalSoldProducts = 0; //The number of products sold by the Store


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
        int totalSoldProducts = 0;
        for (Sale sale : sales) {
            totalSoldProducts += sale.getQuantity();
        }
        this.totalSoldProducts = totalSoldProducts;
    }

    public void pushToFile() {
        try {
            File f = new File("storeData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            StringBuffer salesStr = new StringBuffer();
            String temp;
            for (Sale s : getSales()) {
                temp = s.getCustomer().getUsername() + s.getCustomer().getPurchases().size();
                salesStr.append(temp);
                if (getSales().indexOf(s) < getSales().size() - 1)
                    salesStr.append(",");
            }
            StringBuffer productsStr = new StringBuffer();
            for (Product p : getProducts()) {
                productsStr.append(p.getName());
                if (getProducts().indexOf(p) < getProducts().size() - 1)
                    productsStr.append(",");
            }
            lines.add(String.format("%s:%s;%s;%s", getName(), getDescription(), productsStr, salesStr));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int totalSoldProducts = 0;
        for (Sale sale : sales) {
            totalSoldProducts += sale.getQuantity();
        }
        this.totalSoldProducts = totalSoldProducts;
    }

    public int getTotalSoldProducts() {
        return totalSoldProducts;
    }

    public void setTotalSoldProducts(int totalSoleProducts) {
        this.totalSoldProducts = totalSoleProducts;
    }

    /**
     * Sorts a list of every Product in the Store based on price.
     *
     * @param maxFirst true: sorts from high price to low price, false: sorts from low price to high price
     * @return the sorted list of Products
     */
    public ArrayList<Product> getPriceSortedProducts(boolean maxFirst) {
        ArrayList<Product> copiedProducts = new ArrayList<>();
        for (Product product : products) {
            copiedProducts.add(product);
        }
        ArrayList<Product> sortedProducts = new ArrayList<>();

        while (copiedProducts.size() > 0) {
            Product min = copiedProducts.get(0);
            for (int i = 1; i < copiedProducts.size(); i++) {
                if (copiedProducts.get(i).getPrice() < min.getPrice()) {
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


    /**
     * Sorts a list of every Product in the Store based on quantity.
     *
     * @param maxFirst true: sorts from high quantity to low quantity, false: sorts from low quantity to high quantity
     * @return the sorted list of Products
     */
    public ArrayList<Product> getQuantitySortedProducts(boolean maxFirst) {
        ArrayList<Product> copiedProducts = new ArrayList<>();
        for (Product product : products) {
            copiedProducts.add(product);
        }
        ArrayList<Product> sortedProducts = new ArrayList<>();

        while (copiedProducts.size() > 0) {
            Product min = copiedProducts.get(0);
            for (int i = 1; i < copiedProducts.size(); i++) {
                if (copiedProducts.get(i).getQuantity() < min.getQuantity()) {
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


    /**
     * Provides a list of the Store's sales by a specified customer.
     *
     * @param customer the customer to search for sales from
     * @return list of sales from the specified customer
     */
    public ArrayList<Sale> getSalesFromCustomer(Customer customer) {
        ArrayList<Sale> salesFromCustomer = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getCustomer().equals(customer)) {
                salesFromCustomer.add(sale);
            }
        }
        return salesFromCustomer;
    }

    /**
     * Provides the number of the Store's products bought by a specified user.
     *
     * @param customer the customer to search for sales from
     * @return number of products bought by the specified customers
     */
    public int getQuantityOfProductsBoughtByCustomer(Customer customer) {
        ArrayList<Sale> salesFromCustomer = getSalesFromCustomer(customer);
        int soldToCustomer = 0;
        for (Sale sale : salesFromCustomer) {
            if (sale.getCustomer().equals(customer)) {
                soldToCustomer += sale.getQuantity();
            }
        }
        return soldToCustomer;
    }

    public ArrayList<Sale> getSalesForProduct(Product product) {
        ArrayList<Sale> salesForProduct = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getProduct().equals(product)) {
                salesForProduct.add(sale);
            }
        }
        return salesForProduct;
    }

    public int getNumberOfProductsSold(Product product) {
        ArrayList<Sale> salesForProduct = getSalesForProduct(product);
        int productsSold = 0;
        for (Sale sale : salesForProduct) {
            productsSold += sale.getQuantity();
        }
        return productsSold;
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (Sale sale : sales) {
            if (!(customers.contains(sale.getCustomer()))) {
                customers.add(sale.getCustomer());
            }
        }
        return customers;
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
     *
     * @param product the product to add to the store
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Gets the specified product from the store's catalogue
     *
     * @param index the index of the desired product
     * @return the desired product
     */
    public Product getProduct(int index) {
        return products.get(index);
    }

    /**
     * Removes the specified product from the store's catalogue
     *
     * @param index the index of the desired product
     * @return the removed product
     */
    public Product removeProduct(int index) {
        return products.remove(index);
    }

    /**
     * Removes the specified product from the store's catalogue
     *
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
     *
     * @param sale the sale to add to the ShoppingCart
     */
    public void addSale(Sale sale) {
        totalSoldProducts += sale.getQuantity();
        sales.add(sale);
    }

    /**
     * Gets the specified sale from the ShoppingCart
     *
     * @param index the index of the desired sale
     * @return the desired sale
     */
    public Sale getSale(int index) {
        return sales.get(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     *
     * @param index the index of the desired sale
     * @return the removed sale
     */
    public Sale removeSale(int index) {
        return sales.remove(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     *
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