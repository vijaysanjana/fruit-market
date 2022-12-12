import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive data management system for the MarketPlace application.
 * Includes methods necessary to save & load stores and histories into field for restart persistence.
 * Includes methods to create & update user login data and registration.
 * Includes methods to import & export files as CSV from data storage.
 * Architecture of file storage and file formatting is in the comment below.
 */
public class DataManager {
    private static final File userDataFile = new File("userData");
    private static final String sellerDataFolder = "seller_data";
    private static final String customerDataFolder = "customer_data";

    /**
     * Formatting as follows
     * seller_data/[individual_seller_name]
     * -- [store_name]_store: productName;description;price;quantity
     * -- [store_name]_sales: productName;numberItemSold;customerName;totalCost
     * <p>
     * customer_data/[individual_customer_name]
     * -- purchase_history: productName;numberItemsBought;totalCost
     * -- shopping_cart: productName;numberItemsCarted
     */

    public static void main(String[] args) {
        MarketPlace mp = new MarketPlace();
        loadEverything(mp);

        Product p1 = new Product("RGB Apples", "It's apples but with a hint of artificial coloring!", 29.99, 5);
        Product p2 = new Product("Gamer Fuel", "It's fuel, but for gamers!", 5.99, 100);
        Store s1 = new Store("Gamer Store", "");
        Seller sel = new Seller("jack", "jack@gmail.com", "pw");
        Customer cus = new Customer("john", "john@gmail.com", "pw");

        for(Store s : mp.getStores()) {
            System.out.println("STORE: " + s.getName());
        }
        for(Product p : mp.getProducts()) {
            System.out.println("PROD: " + p.getName());
        }

//        ShoppingCart sc = mp.getCustomer(0).getShoppingCart();
//        ArrayList<Sale> ss = sc.getHeldPurchases();
//        ss.add(new Sale(mp.getCustomer(0), mp.getProduct("busan berry"), 27));
//        mp.getCustomer(0).setShoppingCart(sc);

//        ArrayList<Sale> pur = new ArrayList<>();
//        pur.add(new Sale(mp.getCustomer(0), mp.getProduct("rgb apples"), 3));
//        mp.getCustomer(0).setPurchases(pur);

//        for(Sale s : mp.getCustomer(0).getShoppingCart().getHeldPurchases()) {
//            System.out.println("CUST: " + s.getCustomer());
//            System.out.println("PRODNAME: " + s.getProduct().getName());
//            System.out.println("QUANT: " + s.getQuantity());
//        }

        for(Sale s : mp.getCustomer(0).getPurchases()) {
            System.out.println(s.getProduct().getName());
            System.out.println();
        }

        saveEverything(mp);
    }

    /**
     * Single method to load all data (should only be performed ONCE)
     *
     * @param mp
     */
    public static synchronized void loadEverything(MarketPlace mp) {
        System.out.println("Loading literally everything");

        loadCustomers(mp);
        loadSellers(mp);
        loadStores(mp);
        loadShoppingCarts(mp);
        loadHistorySales(mp);
    }

    /**
     * Single method to save all data
     *
     * @param mp
     */
    public static synchronized void saveEverything(MarketPlace mp) {
        System.out.println("Saving literally everything");

        for (Seller s : mp.getSellers()) {
            createNecessaryFolders(s);
            saveSellerStores(s);
            saveSellerSales(s);
        }

        for (Customer c : mp.getCustomers()) {
            createNecessaryFolders(c);
            saveCustomerCart(c);
            saveCustomerHistory(c);
        }
    }

    /**
     * Loads all customers into marketplace fields
     *
     * @param mp
     */
    private static synchronized void loadCustomers(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles() != null) {
                if (customerLocation.listFiles().length != 0) {
                    for (File f : customerLocation.listFiles()) {
                        String customerName = f.getName();
                        ArrayList<ArrayList<String>> logins = getUserLogins();
                        Customer customer = null;
                        for (ArrayList<String> logs : logins) {
                            if (logs.get(0).equalsIgnoreCase("C")
                                    && logs.get(1).equalsIgnoreCase(customerName)) {
                                customer = new Customer(logs.get(1), logs.get(2), logs.get(3));
                            }
                        }
                        if(customer != null) {
                            mp.addCustomer(customer);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer list loading", customerLocation)
            );
        }
    }

    /**
     * Loads all sellers into marketplace fields
     *
     * @param mp
     */
    private static synchronized void loadSellers(MarketPlace mp) {
        File sellerLocation = new File(sellerDataFolder + File.separatorChar);
        try {
            if(sellerLocation.listFiles() != null) {
                if (sellerLocation.listFiles().length != 0) {
                    for (File f : sellerLocation.listFiles()) {
                        String sellerName = f.getName();
                        ArrayList<ArrayList<String>> logins = getUserLogins();
                        Seller seller = null;
                        for (ArrayList<String> logs : logins) {
                            if (logs.get(0).equalsIgnoreCase("S")
                                    && logs.get(1).equalsIgnoreCase(sellerName)) {
                                seller = new Seller(logs.get(1), logs.get(2), logs.get(3));
                            }
                        }
                        if(seller != null) {
                            mp.addSeller(seller);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Seller list loading", sellerLocation)
            );
        }
    }

    /**
     * Searches through all sellers and loads their appropriate stores and products
     *
     * @param mp
     */
    private static synchronized void loadStores(MarketPlace mp) {
        File sellerLocation = new File(sellerDataFolder + File.separatorChar);
        try {
            if(sellerLocation.listFiles() != null) {
                if (sellerLocation.listFiles().length != 0) {
                    for (File f : sellerLocation.listFiles()) {
                        if(f.listFiles() != null) {
                            if (f.listFiles().length != 0) {
                                for (Seller s : mp.getSellers()) {
                                    if (s.getUsername().equalsIgnoreCase(f.getName())) {
                                        for (File ff : f.listFiles()) {
                                            if (ff.getName().contains("_store")) {
                                                Store store = new Store(ff.getName().replace("_store", "")
                                                        , "");
                                                BufferedReader br = new BufferedReader(new FileReader(ff));
                                                String line = br.readLine();

                                                while (line != null) {
                                                    String[] prod = line.split(";");
                                                    Product product = new Product(prod[0], prod[1],
                                                            Double.parseDouble(prod[2]), Integer.parseInt(prod[3]));
                                                    store.addProduct(product);

                                                    line = br.readLine();
                                                }
                                                s.addStore(store);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Store loading", sellerLocation)
            );
        }
    }

    /**
     * Searches through all customers and loads their appropriate shopping carts
     *
     * @param mp
     */
    private static synchronized void loadShoppingCarts(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles() != null) {
                if (customerLocation.listFiles().length != 0) {
                    for (File f : customerLocation.listFiles()) {
                        if(f.listFiles() != null) {
                            if (f.listFiles().length != 0) {
                                for (Customer c : mp.getCustomers()) {
                                    if (c.getUsername().equalsIgnoreCase(f.getName())) {
                                        for (File ff : f.listFiles()) {
                                            if (ff.getName().contains("shopping_cart")) {
                                                BufferedReader br = new BufferedReader(new FileReader(ff));
                                                String line = br.readLine();

                                                while (line != null) {
                                                    String[] cart = line.split(";");
                                                    String name = cart[0];

                                                    for (Product p : mp.getProducts()) {
                                                        if (p.getName().equalsIgnoreCase(name)) {
                                                            Sale sale = new Sale(c, p, Integer.parseInt(cart[1]));
                                                            c.getShoppingCart().addPurchase(sale);
                                                            break;
                                                        }
                                                    }

                                                    line = br.readLine();
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Shopping cart loading", customerLocation)
            );
        }
    }

    /**
     * Searches through all sellers and loads their appropriate sales history
     *
     * @param mp
     */
    private static synchronized void loadHistorySales(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles() != null) {
                if (customerLocation.listFiles().length != 0) {
                    for (File f : customerLocation.listFiles()) {
                        if(f.listFiles() != null) {
                            if (f.listFiles().length != 0) {
                                for (Customer c : mp.getCustomers()) {
                                    if (c.getUsername().equalsIgnoreCase(f.getName())) {
                                        for (File ff : f.listFiles()) {
                                            if (ff.getName().contains("purchase_history")) {
                                                BufferedReader br = new BufferedReader(new FileReader(ff));
                                                String line = br.readLine();

                                                while (line != null) {
                                                    String[] hist = line.split(";");
                                                    String name = hist[0];

                                                    boolean hasProduct = false;
                                                    Product prod = null;
                                                    Store store = null;

                                                    store_loop:
                                                    for (Store s : mp.getStores()) {
                                                        for (Product p : s.getProducts()) {
                                                            if (p.getName().equalsIgnoreCase(name)) {
                                                                hasProduct = true;
                                                                prod = p;
                                                                store = s;
                                                                break store_loop;
                                                            }
                                                        }
                                                    }

                                                    if(hasProduct && prod != null) {
                                                        Sale sale = new Sale(c, prod,
                                                                Integer.parseInt(hist[1]),
                                                                Double.parseDouble(hist[2]));
                                                        c.addSale(sale);
                                                        store.addSale(sale);
                                                    } else {
                                                        prod = new Product(name, "",
                                                                (Double.parseDouble(hist[2])
                                                                        /Integer.parseInt(hist[1])),
                                                                Integer.parseInt(hist[1]));
                                                        Sale sale = new Sale(c, prod,
                                                                Integer.parseInt(hist[1]),
                                                                Double.parseDouble(hist[2]));
                                                        c.addSale(sale);
                                                    }

                                                    line = br.readLine();
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Purchase history loading", customerLocation)
            );
        }
    }

    /**
     * Saves a seller's stores and products into file
     *
     * @param seller
     */
    public static synchronized void saveSellerStores(Seller seller) {
        for (Store s : seller.getStores()) {
            File file = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + s.getName() + "_store");

            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller store file creation", file)
                );
            }

            try (FileWriter fw = new FileWriter(file, false)) {
                for (Product p : s.getProducts()) {
                    fw.write(p.getName() + ";"
                            + p.getDescription() + ";"
                            + p.getPrice() + ";"
                            + p.getQuantity());
                    fw.write("\n");
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller store saving", file)
                );
            }
        }
    }

    /**
     * Saves a seller's sales history into file
     *
     * @param seller
     */
    public static synchronized void saveSellerSales(Seller seller) {
        for (Store s : seller.getStores()) {
            File file = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + s.getName() + "_sales");

            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller sales file creation", file)
                );
            }

            try (FileWriter fw = new FileWriter(file, false)) {
                for (Sale sa : s.getSales()) {
                    fw.write(sa.getProduct().getName() + ";"
                            + sa.getQuantity() + ";"
                            + sa.getCustomer().getUsername() + ";"
                            + sa.getTotalCost());
                    fw.write("\n");
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller sales saving", file)
                );
            }
        }
    }

    /**
     * Saves a customer's purchase history into file
     *
     * @param customer
     */
    public static synchronized void saveCustomerHistory(Customer customer) {
        File file = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "purchase_history");

        try {
            file.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer history file creation", file)
            );
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            for (Sale s : customer.getPurchases()) {
                fw.write(s.getProduct().getName() + ";"
                        + s.getQuantity() + ";"
                        + s.getTotalCost());
                fw.write("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer history saving", file)
            );
        }
    }

    /**
     * Saves a customer's shopping cart into file
     *
     * @param customer
     */
    public static synchronized void saveCustomerCart(Customer customer) {
        File file = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");

        try {
            file.createNewFile();
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer shopping cart file creation", file)
            );
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            for (Sale s : customer.getShoppingCart().getHeldPurchases()) {
                fw.write(s.getProduct().getName() + ";"
                        + s.getQuantity());
                fw.write("\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer shopping cart saving", file)
            );
        }
    }


    /**
     * Creates all of the appropriate folders for a user
     *
     * @param user
     */
    private static synchronized void createNecessaryFolders(User user) {
        if (new File(customerDataFolder + File.separatorChar).mkdir()) {
            //System.out.println("Customer data folder created");
        }
        if (new File(sellerDataFolder + File.separatorChar).mkdir()) {
            //System.out.println("Seller data folder created");
        }
        if (user instanceof Customer) {
            if (new File(customerDataFolder + File.separatorChar
                    + user.getUsername() + File.separatorChar).mkdir()) {
                //System.out.println("Individual customer folder created");
            }
        }
        if (user instanceof Seller) {
            if (new File(sellerDataFolder + File.separatorChar
                    + user.getUsername() + File.separatorChar).mkdir()) {
                //System.out.println("Individual seller folder created");
            }
        }
    }

    /**
     * Import a CSV and input it into seller's store data
     *
     * @param seller
     * @param store
     * @param file
     */
    public static synchronized void importStoreCSV(Seller seller, Store store, File file) {
        ArrayList<String> current = new ArrayList<>();
        File storeFile = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar + store.getName() + "_store");
        try {
            BufferedReader br = new BufferedReader(new FileReader(storeFile));
            String line = br.readLine();

            while (line != null) {
                current.add(line.substring(0, line.indexOf(";")));
                line = br.readLine();
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Reading of current store file", storeFile)
            );
        }

        try {
            FileWriter fw = new FileWriter(storeFile, true);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                if (line.split(",").length != 4) {
                    System.out.println("Invalid format; skipping line: " + line);
                } else if (current.contains(line.substring(0, line.indexOf(",")))) {
                    System.out.println("Product name already in use; skipping line: " + line);
                } else {
                    System.out.println("Adding new Product: " + line.substring(0, line.indexOf(",")));
                    String[] arr = line.split(",");
                    store.addProduct(new Product(arr[0], arr[1],
                            Double.parseDouble(arr[2]), Integer.parseInt(arr[3])));
                    fw.write(arr[0] + ";" + arr[1] + ";" + arr[2] + ";" + arr[3]);
                    fw.write("\n");
                }
                line = br.readLine();
            }

            System.out.println("Successfully imported CSV");
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Reading of CSV file", file)
            );
        }
    }

    /**
     * Export's seller's particular store as CSV
     *
     * @param seller
     * @param store
     */
    public static synchronized void exportStoreCSV(Seller seller, Store store) {
        File output = new File(seller.getUsername().replaceAll(" ", "_") + "_"
                + store.getName().replaceAll(" ", "_") + ".csv");
        try {
            FileWriter fw = new FileWriter(output);
            for (Product p : store.getProducts()) {
                fw.write(p.getName() + "," + p.getDescription()
                        + "," + p.getPrice() + "," + p.getQuantity());
                fw.write("\n");
            }

            System.out.println("Store CSV outputted successfully");
            System.out.println("File Format: name,description,price,quantity");
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Output of store CSV file", output)
            );
        }
    }

    /**
     * Exports customer's purchase history as CSV
     *
     * @param customer
     */
    public static synchronized void exportCustomerHistory(Customer customer) {
        File output = new File(customer.getUsername().replaceAll(" ", "_") + "_"
                + "purchase_history.csv");

        try {
            FileWriter fw = new FileWriter(output);
            for (Sale s : customer.getPurchases()) {
                fw.write(s.getProduct().getName() + "," + s.getQuantity() + "," + s.getTotalCost());
                fw.write("\n");
            }

            System.out.println("History CSV outputted successfully");
            System.out.println("File Format: name,quantityBought,totalCost");
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Output of history CSV file", output)
            );
        }
    }

    /**
     * Grabs a list of user logins
     *
     * @return ArrayList<String> {type[0], username[1], email[2], password[3]}
     */
    public static synchronized ArrayList<ArrayList<String>> getUserLogins() {
        if (!userDataFile.exists()) {
            try {
                userDataFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "User data file creation", userDataFile)
                );
            }
        }

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(userDataFile))) {
            String line = br.readLine();
            while (line != null) {
                String type = String.valueOf(line.charAt(0));
                String username = line.substring(2, line.indexOf(";"));
                String email = line.substring(line.indexOf(";") + 1,
                        line.indexOf(";", line.indexOf(";") + 1));
                String password = line.substring(line.indexOf(";",
                        line.indexOf(";", line.indexOf(";") + 1)) + 1);

                data.add(new ArrayList<>(Arrays.asList(type, username, email, password)));

                line = br.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "User data loading", userDataFile)
            );
        }
        return data;
    }

    /**
     * Stores a registered user's logins
     *
     * @param username
     * @param email
     * @param password
     * @param type
     * @return
     */
    public static synchronized boolean writeUserSignup(String username, String email,
                                          String password, String type) {
        if (username.contains(";") || username.contains(",")
                || email.contains(";") || email.contains(",")
                || password.contains(";") || password.contains(",")) {
            return false;
        }

        ArrayList<ArrayList<String>> data = getUserLogins();

        for (ArrayList<String> arr : data) {
            if (arr.get(1).equalsIgnoreCase(username)) {
                return false;
            }
            if (arr.get(2).equalsIgnoreCase(email)) {
                return false;
            }
        }
        if (type.equalsIgnoreCase("customer")) {
            writeCustomer(username, email, password);
            return true;
        } else if (type.equalsIgnoreCase("seller")) {
            writeSeller(username, email, password);
            return true;
        }
        return false;
    }

    /**
     * Write customer login information
     *
     * @param username
     * @param email
     * @param password
     */
    public static synchronized void writeCustomer(String username, String email, String password) {
        try {
            File f = userDataFile;
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("C:%s;%s;%s",
                    username, email, password));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "User data of customer saving", userDataFile)
            );
        }
    }

    /**
     * Write seller login information
     *
     * @param username
     * @param email
     * @param password
     */
    public static synchronized void writeSeller(String username, String email, String password) {
        try {
            File f = userDataFile;
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            lines.add(String.format("S:%s;%s;%s",
                    username, email, password));
            Files.write(f.toPath(), lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "User data of seller saving", userDataFile)
            );
        }
    }

    /**
     * Removes a user's account and deletes all of their data
     *
     * @param user
     */
    public static synchronized void removeAccount(User user) {
        File f = null;
        if (user instanceof Customer) {
            f = new File(customerDataFolder + File.separatorChar + user.getUsername());
        } else if (user instanceof Seller) {
            f = new File(sellerDataFolder + File.separatorChar + user.getUsername());
        }
        for (File t : f.listFiles()) {
            try {
                PrintWriter pw = new PrintWriter(t);
                pw.print("");
                pw.close();
                t.delete();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Customer/seller data deletion", t)
                );
            }
        }
        f.delete();

        ArrayList<ArrayList<String>> accounts = getUserLogins();
        ArrayList<ArrayList<String>> tempAcc = new ArrayList<>(accounts);
        for (int i = 0; i < accounts.size(); i++) {
            ArrayList<String> arr = accounts.get(i);
            if (arr.get(1).equalsIgnoreCase(user.getUsername())
                    && arr.get(2).equalsIgnoreCase(user.getEmail())) {
                tempAcc.remove(i);
            }
        }

        try {
            PrintWriter pw = new PrintWriter(userDataFile);
            for (ArrayList<String> arr : tempAcc) {
                pw.write(arr.get(0) + ":" + arr.get(1) + ";" + arr.get(2)
                        + ";" + arr.get(3) + "\n");
            }
            pw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "User data account deletion", userDataFile)
            );
        }
    }
}
