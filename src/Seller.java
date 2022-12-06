import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * Seller class that contains the Stores owned by the Seller.
 * Seller extends User.
 *
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
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

    public void pushToFile() {
        try {
            File f = new File("userData");
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

    public void importProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter file path of the file containing products to be imported: ");
        String filePath = sc.nextLine();
        try {
            FileReader f = new FileReader(filePath);
            BufferedReader bfr = new BufferedReader(f);
            ArrayList<String> list = new ArrayList<>();
            String line = bfr.readLine();
            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }
            for (int i = 0; i < list.size(); i++) {
                String[] arr = list.get(i).split(",");
                Product p = new Product(arr[0], arr[1], Double.parseDouble(arr[3]), Integer.parseInt(arr[4]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found - please enter a valid file path!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportProducts() throws IOException {
        ArrayList<Product> products = this.getAllProducts();
        String fileName = this.getUsername() + ".csv";
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f, false);
        PrintWriter pw = new PrintWriter(fos);
        for (Product p : products) {
            pw.println(p.toString());
        }
        fos.close();
        pw.close();
        System.out.println("A file titled " + this.getUsername() + ".csv has been created with your products!");
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();
        for (Store s : stores) {
            ArrayList<Product> products = s.getProducts();
            for (Product p : products) {
                allProducts.add(p);
            }
        }
        return allProducts;
    }

    public void updateSeller() {
        try {
            File f = new File(this.getUsername() + ".csv");
            if (f.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(f, false);
                PrintWriter pw = new PrintWriter(fos);
                ArrayList<Product> products = this.getAllProducts();
                for (Product p : products) {
                    pw.println(p.toString());
                }
                pw.close();
            } else {
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                ArrayList<String> list = new ArrayList<>();
                String line = bfr.readLine();
                while (line != null) {
                    list.add(line);
                    line = bfr.readLine();
                }
                bfr.close();
                FileOutputStream fos = new FileOutputStream(f, false);
                PrintWriter pw = new PrintWriter(fos);
                for (String i : list) {
                    pw.println(i);
                }
                ArrayList<Product> products = this.getAllProducts();
                for (Product p : products) {
                    pw.println(p.toString());
                }
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
