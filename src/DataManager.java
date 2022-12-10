import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DataManager {
    private static File userDataFile = new File("userData");
    private static String sellerDataFolder = "seller_data";
    private static String customerDataFolder = "customer_data";
    
    /**
    * Formatting as follows
    * seller_data/[individual_seller_name]
    * -- [store_name]_store: productName;description;price;quantity
    * -- [store_name]_sales: productName;numberItemSold;customerName;totalCost
    *
    * customer_data/[individual_customer_name]
    * -- purchase_history: productName;numberItemsBought;totalCost
    * -- shopping_cart: productName;numberItemsCarted
    */

    public static void main(String[] args) {
        MarketPlace mp = new MarketPlace();
        Product p1 = new Product("RGB Apples", "It's apples but with a hint of artificial coloring!", 29.99, 5);
        Product p2 = new Product("Gamer Fuel", "It's fuel, but for gamers!", 5.99, 100);
        Store s1 = new Store("Gamer Store", "");

        s1.addProduct(p1);
        s1.addProduct(p2);

        loadEverything(mp);
        saveEverything(mp);
    }

    public static void loadEverything(MarketPlace mp) {
        loadCustomers(mp);
        loadSellers(mp);
        loadStores(mp);
        loadShoppingCarts(mp);
        loadHistorySales(mp);
    }

    public static void saveEverything(MarketPlace mp) {
        for(Seller s : mp.getSellers()) {
            createNecessaryFolders(s);
            saveSellerStores(s);
            saveSellerSales(s);
        }

        for(Customer c : mp.getCustomers()) {
            createNecessaryFolders(c);
            saveCustomerCart(c);
            saveCustomerHistory(c);
        }
    }

    private static void loadCustomers(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles().length != 0) {
                for(File f : customerLocation.listFiles()) {
                    String customerName = f.getName();
                    ArrayList<ArrayList<String>> logins = getUserLogins();
                    Customer customer = null;
                    for(ArrayList<String> logs : logins) {
                        if(logs.get(0).equalsIgnoreCase("C")
                                && logs.get(1).equalsIgnoreCase(customerName)) {
                            customer = new Customer(logs.get(1), logs.get(2), logs.get(3));
                        }
                    }
                    mp.addCustomer(customer);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer list loading", customerLocation)
            );
        }
    }

    private static void loadSellers(MarketPlace mp) {
        File sellerLocation = new File(sellerDataFolder + File.separatorChar);
        try {
            if(sellerLocation.listFiles().length != 0) {
                for(File f : sellerLocation.listFiles()) {
                    String sellerName = f.getName();
                    ArrayList<ArrayList<String>> logins = getUserLogins();
                    Seller seller = null;
                    for(ArrayList<String> logs : logins) {
                        if(logs.get(0).equalsIgnoreCase("S")
                                && logs.get(1).equalsIgnoreCase(sellerName)) {
                            seller = new Seller(logs.get(1), logs.get(2), logs.get(3));
                        }
                    }
                    mp.addSeller(seller);
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Seller list loading", sellerLocation)
            );
        }
    }

    private static void loadStores(MarketPlace mp) {
        File sellerLocation = new File(sellerDataFolder + File.separatorChar);
        try {
            if(sellerLocation.listFiles().length != 0) {
                for(File f : sellerLocation.listFiles()) {
                    if(f.listFiles().length != 0) {
                        for(Seller s : mp.getSellers()) {
                            if(s.getUsername().equalsIgnoreCase(f.getName())) {
                                for(File ff : f.listFiles()) {
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
                                        //break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Store loading", sellerLocation)
            );
        }
    }

    private static void loadShoppingCarts(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles().length != 0) {
                for(File f : customerLocation.listFiles()) {
                    if(f.listFiles().length != 0) {
                        for(Customer c : mp.getCustomers()) {
                            if(c.getUsername().equalsIgnoreCase(f.getName())) {
                                for(File ff : f.listFiles()) {
                                    if(ff.getName().contains("shopping_cart")) {
                                        BufferedReader br = new BufferedReader(new FileReader(ff));
                                        String line = br.readLine();

                                        while(line != null) {
                                            String[] cart = line.split(";");
                                            String name = cart[0];

                                            for(Product p : mp.getProducts()) {
                                                if(p.getName().equalsIgnoreCase(name)) {
                                                    c.getShoppingCart().addPurchase(new Sale(c, p));
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
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Shopping cart loading", customerLocation)
            );
        }
    }

    private static void loadHistorySales(MarketPlace mp) {
        File customerLocation = new File(customerDataFolder + File.separatorChar);
        try {
            if(customerLocation.listFiles().length != 0) {
                for(File f : customerLocation.listFiles()) {
                    if(f.listFiles().length != 0) {
                        for(Customer c : mp.getCustomers()) {
                            if(c.getUsername().equalsIgnoreCase(f.getName())) {
                                for(File ff : f.listFiles()) {
                                    if(ff.getName().contains("purchase_history")) {
                                        BufferedReader br = new BufferedReader(new FileReader(ff));
                                        String line = br.readLine();

                                        while(line != null) {
                                            String[] hist = line.split(";");
                                            String name = hist[0];

                                            store_loop:
                                            for(Store s : mp.getStores()) {
                                                for(Product p : s.getProducts()) {
                                                    if(p.getName().equalsIgnoreCase(name)) {
                                                        Sale sale =  new Sale(c,p,Integer.parseInt(hist[1]));
                                                        c.addSale(sale);
                                                        s.addSale(sale);
                                                    }
                                                    break store_loop;
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
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Purchase history loading", customerLocation)
            );
        }
    }

    public static void saveSellerStores(Seller seller) {
        for(Store s : seller.getStores()) {
            File file = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + s.getName() + "_store");

            try {
                file.createNewFile();
            } catch(Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller store file creation", file)
                );
            }

            try(FileWriter fw = new FileWriter(file, false)) {
                for(Product p : s.getProducts()) {
                    fw.write(p.getName() + ";"
                            + p.getDescription() + ";"
                            + p.getPrice() + ";"
                            + p.getQuantity());
                    fw.write("\n");
                }
            } catch(Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller store saving", file)
                );
            }
        }
    }

    public static void saveSellerSales(Seller seller) {
        for(Store s : seller.getStores()) {
            File file = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + s.getName() + "_sales");

            try {
                file.createNewFile();
            } catch(Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "Seller sales file creation", file)
                );
            }

            try(FileWriter fw = new FileWriter(file, false)) {
                for(Sale sa : s.getSales()) {
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

    public static void saveCustomerHistory(Customer customer) {
        File file = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "purchase_history");

        try {
            file.createNewFile();
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer history file creation", file)
            );
        }

        try(FileWriter fw = new FileWriter(file, false)) {
            for(Sale s : customer.getPurchases()) {
                fw.write(s.getName() + ";"
                        + s.getQuantity() + ";"
                        + s.getTotalCost());
                fw.write("\n");
            }
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer history saving", file)
            );
        }
    }

    public static void saveCustomerCart(Customer customer) {
        File file = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");

        try {
            file.createNewFile();
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer shopping cart file creation", file)
            );
        }

        try(FileWriter fw = new FileWriter(file, false)) {
            for(Sale s : customer.getShoppingCart().getHeldPurchases()) {
                fw.write(s.getName() + ";"
                        + s.getQuantity());
                fw.write("\n");
            }
            fw.close();
        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Customer shopping cart saving", file)
            );
        }
    }


    private static void createNecessaryFolders(User user) {
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
     */
    public static void importStoreCSV(Seller seller, Store store, File file) {
        ArrayList<String> current = new ArrayList<>();
        File storeFile = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar + store.getName() + "_store");
        try {
            BufferedReader br = new BufferedReader(new FileReader(storeFile));
            String line = br.readLine();

            while(line != null) {
                current.add(line.substring(0,line.indexOf(";")));
                line = br.readLine();
            }

        } catch(Exception e) {
            throw new RuntimeException(
                    new DataException(e.getMessage(), "Reading of current store file", storeFile)
            );
        }

        try {
            FileWriter fw = new FileWriter(storeFile, true);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while(line != null) {
                if(line.split(",").length != 4) {
                    System.out.println("Invalid format; skipping line: " + line);
                } else if(current.contains(line.substring(0,line.indexOf(",")))) {
                    System.out.println("Product name already in use; skipping line: " + line);
                } else {
                    System.out.println("Adding new Product: " + line.substring(0,line.indexOf(",")));
                    String[] arr = line.split(",");
                    store.addProduct(new Product(arr[0], arr[1],
                            Double.parseDouble(arr[2]), Integer.parseInt(arr[3])));
                    fw.write(arr[0] + ";" + arr[1] + ";" + arr[2] + ";" + arr[3]);
                    fw.write("\n");
                }
                line = br.readLine();
            }

            System.out.println("Successfully imported CSV");
        } catch(Exception e) {
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
    public static void exportStoreCSV(Seller seller, Store store) {
        File output = new File(seller.getUsername().replaceAll(" ", "_") + "_"
                + store.getName().replaceAll(" ", "_") + ".csv");
        try {
            FileWriter fw = new FileWriter(output);
            for(Product p : store.getProducts()) {
                fw.write(p.getName() + "," + p.getDescription()
                        + "," + p.getPrice() + "," + p.getQuantity());
                fw.write("\n");
            }

            System.out.println("Store CSV outputted successfully");
            System.out.println("File Format: name,description,price,quantity");
        } catch(Exception e) {
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
    public static void exportCustomerHistory(Customer customer) {
        File output = new File(customer.getUsername().replaceAll(" ", "_") + "_"
                + "purchase_history.csv");

        try {
            FileWriter fw = new FileWriter(output);
            for(Sale s : customer.getPurchases()) {
                fw.write(s.getName() + "," + s.getQuantity() + "," + s.getTotalCost());
                fw.write("\n");
            }

            System.out.println("History CSV outputted successfully");
            System.out.println("File Format: name,quantityBought,totalCost");
        } catch(Exception e) {
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
    public static ArrayList<ArrayList<String>> getUserLogins() {
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
    public static boolean writeUserSignup(String username, String email,
                                          String password, String type) {
        if(!userDataFile.exists()) {
            try {
                userDataFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(
                        new DataException(e.getMessage(), "User data file creation", userDataFile)
                );
            }
        }

        if(username.contains(";") || username.contains(",")
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
    public static void writeCustomer(String username, String email, String password) {
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
    public static void writeSeller(String username, String email, String password) {
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
    public static void removeAccount(User user) {
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
