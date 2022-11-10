import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * Seller class that contains the Stores owned by the Seller.
 * Seller extends User.
 */
public class Seller extends User {

    ArrayList<Store> stores; //ArrayList of stores owned by the Seller


    //Constructors
    public Seller(String username, String email, String password) {
        super(username, email, password);
        this.stores = new ArrayList<>();
    }

    public Seller(String username, String email, String password, ArrayList<Store> stores) {
        super(username, email, password);
        this.stores = stores;
    }

    public void pushToFile(String fileName) {
        try {
            File f = new File(fileName);
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("S:%s;%s;%s", getUsername(), getEmail(), getPassword()));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Getter and Setter
    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }


    //Equals
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seller)) {
            return false;
        }
        Seller seller = (Seller) o;
        return (super.equals(seller) && stores.equals(seller.getStores()));
    }


    /**
     * Adds a new store to the seller
     *
     * @param store the store to add to the seller
     */
    public void addStore(Store store) {
        stores.add(store);
    }

    /**
     * Gets the specified store from the seller
     *
     * @param index the index of the desired store
     * @return the desired store
     */
    public Store getStore(int index) {
        return stores.get(index);
    }

    /**
     * Removes the specified store from the seller
     *
     * @param index the index of the desired store
     * @return the removed store
     */
    public Store removeStore(int index) {
        return stores.remove(index);
    }

    /**
     * Removes the specified store from the seller
     *
     * @param store the desired store
     * @return the removed store
     */
    public Store removeStore(Store store) {
        if (stores.contains(store)) {
            return stores.remove(stores.indexOf(store));
        }
        return null;
    }

}