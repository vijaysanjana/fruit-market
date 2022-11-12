import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * ShoppingCart class contains the ShoppingCart's owning Customer and the current purchases waiting to be made.
 */
public class ShoppingCart {

    private ArrayList<Sale> heldPurchases; //The current purchases waiting to be made.


    //Constructors
    public ShoppingCart() {
        this.heldPurchases = new ArrayList<>();
    }

    public ShoppingCart(ArrayList<Sale> heldPurchases) {
        this.heldPurchases = heldPurchases;
    }


    //Getter and Setter
    public ArrayList<Sale> getHeldPurchases() {
        return heldPurchases;
    }

    public void setHeldPurchases(ArrayList<Sale> heldPurchases) {
        this.heldPurchases = heldPurchases;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShoppingCart)) {
            return false;
        }
        ShoppingCart shoppingCart = (ShoppingCart) o;
        return heldPurchases.equals(shoppingCart.getHeldPurchases());
    }


    /**
     * Adds a new sale to the ShoppingCart
     * @param sale the sale to add to the ShoppingCart
     */
    public void addPurchase(Sale sale) {
        heldPurchases.add(sale);
    }

    /**
     * Gets the specified sale from the ShoppingCart
     * @param index the index of the desired sale
     * @return the desired sale
     */
    public Sale getPurchase(int index) {
        return heldPurchases.get(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     * @param index the index of the desired sale
     * @return the removed sale
     */
    public Sale removePurchase(int index) {
        return heldPurchases.remove(index);
    }

    /**
     * Removes the specified sale from the ShoppingCart
     * @param sale the desired sale
     * @return the removed sale
     */
    public Sale removePurchase(Sale sale) {
        if (heldPurchases.contains(sale)) {
            return heldPurchases.remove(heldPurchases.indexOf(sale));
        }
        return null;
    }

    public void pushToFile() {
        try {
            File f = new File("storeData");
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            StringBuffer heldSales = new StringBuffer();
            String temp;
            String user = getPurchase(0).getCustomer().getUsername();
            for (Sale s : getHeldPurchases()) {
                heldSales.append(s.getName());
                if (getHeldPurchases().indexOf(s) < getHeldPurchases().size() - 1)
                    heldSales.append(",");
            }
            lines.add(String.format("%s:%s", user, heldSales));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}