import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private static File userDataFile = new File("userData");
    private static String sellerDataFolder = "seller_data";
    private static String customerDataFolder = "customer_data";

    public static void main(String[] args) {
        Seller dendenMan = new Seller("DendenMan", "denny@dennys.gov", "secret");
        Store dennys = new Store("Denny's", "it's... Denny's");
        //addSellerData(dendenMan, dennys, new Product("Gamer Eggs", "Nature's Protein Shake", 19.99));
        //addSellerData(dendenMan, dennys, new Product("Gamer Sausage", "Nature's Protein... Protein", 5.99));
        //addSellerData(dendenMan, dennys, new Product("Gamer Pancakes", "Nature's Gluten Shake", 13.99));


        Seller god = new Seller("God", "God@god.cod", "rapture");
        Store soapStore = new Store("God's Soap Emporium", "Got bored, like soap - God");
        //addSellerData(god, soapStore, new Product("Dawn PowerWash Dish Spray", "clean up you gross ass plate", 4.99));
        //addSellerData(god, soapStore, new Product("Lavender Ascent", "Isn't this a good name for soap?", 49.99));
        //addSellerData(god, soapStore, new Product("Tide Pods", "50% more edible than competitors", 2.49));

        MarketPlace mp = new MarketPlace();
        loadAllStores(mp);
        ArrayList<Product> st = mp.getProducts();
        for(Store s : mp.getStores()) {
            System.out.println("===============================");
            System.out.println(s.getName());
            for(Product p : s.getProducts()) {
                System.out.println("NAME: " + p.getName());
                System.out.println("DESC: " + p.getDescription());
                System.out.println("PRICE: " + p.getPrice());
                System.out.println("QUANT: " + p.getQuantity());
            }
        }

        //Seller seller = new Seller("jack", "jack@gmail.com", "pw");
        //Store store = new Store("1st store", "this is a really cool 1st store");
        //Product prod = new Product("gamer tea", "tea just for gamers", 85, 10);
        //Product prod = new Product("game", "some game", 60, 100);
        //addSellerData(seller, store, prod);

        //int counter = 0;
        //ArrayList<ArrayList<Object>> allStore = getSellerAllData(seller);
        //for(ArrayList<Object> arr : allStore) {
            //String s = (String) arr.get(0);
            //System.out.println("STORE: " + s);
            //for(int i = 2; i < arr.size(); i+=2) {
                //int q = Integer.parseInt((String) arr.get(i-1));
                //Product p = (Product) arr.get(i);
                //System.out.println("QUANT SOLD: " + q);
                //System.out.println("PRODUCT: " + p.getName() + ";" + p.getDescription() + ";" + p.getPrice() + ";" + p.getQuantity());
            //}
        //}

        //ArrayList<Object> specificStore = getSellerData(seller, store);
        //for(int i = 2; i < specificStore.size(); i+=2) {
            //int q = Integer.parseInt((String) specificStore.get(i-1));
            //Product p = (Product) specificStore.get(i);
            //System.out.println("QUANT SOLD: " + q);
            //System.out.println("PRODUCT: " + p.getName() + ";" + p.getDescription() + ";" + p.getPrice() + ";" + p.getQuantity());
        //}
        //updateSellerData(seller, store, prod, 75, 25);

        //Customer customer =  new Customer("bob", "bob@hotmail.com", "p2as");
        //addCustomerData(customer, prod, 1);
        //for(ArrayList<Object> arr : allStore) {
            //for(int i = 1; i < arr.size(); i++) {
                //Product p = (Product) arr.get(i);
                //if(p.equals(prod)) {
                    //updateSellerData(seller, store, p, 100);
                //}
            //}
        //}
        //updateSellerData(seller, store, prod, 5);
        //addCustomerShopppingCart(customer, prod, 2);
        //ArrayList<ArrayList<Object>> allCart = getCustomerShoppingCart(customer);
        //for(ArrayList<Object> arr : allCart) {
            //int q = Integer.parseInt((String) arr.get(0));
            //Product p = (Product) arr.get(1);
            //System.out.println("QUANTITY: " + q);
            //System.out.println("PRODUCT: " + p.getName() + " " + p.getDescription() + " " + p.getPrice() + " " + p.getQuantity());
        //}

        //updateCustomerShoppingCart(customer, prod, 69);
    }

    /**
     * Loads in all stores that have been saved and initializes it into the provided MarketPlace object
     * @param mp
     */
    public static void loadAllStores(MarketPlace mp) {
        File sellData = new File(sellerDataFolder + File.separatorChar);
        if(sellData.listFiles().length != 0) {
            for(File f : sellData.listFiles()) {
                String sellerName = f.getName();
                ArrayList<ArrayList<String>> logins = getUserLogins();
                for(ArrayList<String> logs : logins) {
                    if(logs.get(0).equalsIgnoreCase("S") && logs.get(1).equalsIgnoreCase(sellerName)) {
                        Seller tempSeller = new Seller(logs.get(1), logs.get(2), logs.get(3));
                        File storeData = new File(sellerDataFolder + File.separatorChar
                                + sellerName + File.separatorChar);
                        ArrayList<ArrayList<Object>> allStores = getSellerAllData(tempSeller);
                        for(ArrayList<Object> arr : allStores) {
                            Store tempStore = new Store((String) arr.get(0), "");
                            for(int i = 2; i < arr.size(); i+=2) {
                                Product p = (Product) arr.get(i);
                                tempStore.addProduct(p);
                            }
                            tempSeller.addStore(tempStore);
                            mp.addSeller(tempSeller);
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates all the necessary file storage folders for based on a specified user type
     * @param user
     */
    private static void createNecessaryFolders(User user) {
        if(new File(customerDataFolder + File.separatorChar).mkdir()) {
            System.out.println("Customer data folder created");
        }
        if(new File(sellerDataFolder + File.separatorChar).mkdir()) {
            System.out.println("Seller data folder created");
        }
        if(user instanceof Customer) {
            if(new File(customerDataFolder + File.separatorChar
                    + user.getUsername() + File.separatorChar).mkdir()) {
                System.out.println("Individual customer folder created");
            }
        }
        if(user instanceof Seller) {
            if(new File(sellerDataFolder + File.separatorChar
                    + user.getUsername() + File.separatorChar).mkdir()) {
                System.out.println("Individual seller folder created");
            }
        }
    }

    /**
     * Returns all of a customer's purchase history data in an arraylist with quantity and product object
     * @param customer
     * @return ArrayList<ArrayList<Object>> => (String)quantity[0], (Product)product[1]
     */
    public static ArrayList<ArrayList<Object>> getCustomerData(Customer customer) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        try {
            int counter = 0;
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "purchase_history");

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                data.get(counter).add(arr[0]);
                data.get(counter).add(new Product(arr[1], arr[2],
                        Double.parseDouble(arr[3]), Integer.parseInt(arr[4])));
                counter++;
                line = br.readLine();
            }

            return data;
        } catch(Exception e) {
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Returns all of a customer's current shopping cart data with quantity and product object
     * @param customer
     * @return ArrayList<ArrayList<Object>> => (String)quantity[0], (Product)product_obj[1]
     */
    public static ArrayList<ArrayList<Object>> getCustomerShoppingCart(Customer customer) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<Object>> cart = new ArrayList<>();
        try {
            int counter = 0;
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "shopping_cart");

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                cart.add(new ArrayList<Object>());
                String[] arr = line.split(";");
                cart.get(counter).add(arr[0]);
                cart.get(counter).add(new Product(arr[1], arr[2],
                        Double.parseDouble(arr[3]), Integer.parseInt(arr[4])));
                counter++;
                line = br.readLine();
            }

            return cart;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Returns all of a seller's products in a specified store
     * @param seller
     * @param store
     * (String)purchasedQuantity[1 thru n] where n is an odd number
     * (Product)product_objects[2 thru n] where n is an even number
     */
    public static ArrayList<Object> getSellerData(Seller seller, Store store) {
        createNecessaryFolders(seller);
        ArrayList<Object> products = new ArrayList<>();
        products.add("");
        try {
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + store.getName() + "_data");

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                products.add(arr[0]);
                products.add(new Product(arr[1], arr[2], Double.parseDouble(arr[3]), Integer.parseInt(arr[4])));
                line = br.readLine();
            }

            return products;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Returns all of a seller's products in all of their owned stores
     * @param seller
     * @return ArrayList<ArrayList<Object>> => {per store} (String)store_name[0],
     * (String)purchasedQuantity[1 thru n] where n is an odd number
     * ArrayList<(Product)>product_objects[2 thru n] where n is an even number
     */
    public static ArrayList<ArrayList<Object>> getSellerAllData(Seller seller) {
        createNecessaryFolders(seller);
        ArrayList<ArrayList<Object>> products = new ArrayList<>();
        File sellerFolder = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar);

        for(File f : sellerFolder.listFiles()) {
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(f.getName().replace("_data", ""));
            try {
                File histFile = new File(sellerDataFolder + File.separatorChar
                        + seller.getUsername() + File.separatorChar + f.getName());
                BufferedReader br = new BufferedReader(new FileReader(histFile));
                String line = br.readLine();
                while(line != null) {
                    String[] arr = line.split(";");
                    temp.add(arr[0]);
                    temp.add(new Product(arr[1], arr[2], Double.parseDouble(arr[3]), Integer.parseInt(arr[4])));
                    line = br.readLine();
                }
            } catch(Exception e) {
                e.printStackTrace();
                throw new RuntimeException(new DataException());
            }
            products.add(temp);
        }
        return products;
    }

    /**
     * Adds a purchase to a customer's history with a specified purchase quantity
     * @param customer
     * @param product
     * @param quantity
     */
    public static void addCustomerData(Customer customer, Product product, int quantity) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "purchase_history");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(quantity + ";" + product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity());
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Adds a porduct to a customer's shopping cart data with a specified quantity
     * @param customer
     * @param product
     * @param quantity
     */
    public static void addCustomerShopppingCart(Customer customer, Product product, int quantity) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "shopping_cart");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(quantity + ";" + product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity());
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Updates a customer's shopping cart data with a new quantity; set quantity to 0 to remove
     * @param customer
     * @param product
     * @param quantity
     */
    public static void updateCustomerShoppingCart(Customer customer, Product product, int quantity) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<Object>> currentCart = getCustomerShoppingCart(customer);
        File histFile = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");

        for(int i = 0; i < currentCart.size(); i++) {
            ArrayList<Object> arr = currentCart.get(i);
            Product p = (Product) arr.get(1);

            if(quantity != 0) {
                if(p.getName().equals(product.getName())
                        && p.getDescription().equals(product.getDescription())
                        && p.getPrice() == product.getPrice()
                        && p.getQuantity() == product.getQuantity()) {
                    arr.set(0, String.valueOf(quantity));
                }
            } else {
                if(p.getName().equals(product.getName())
                        && p.getDescription().equals(product.getDescription())
                        && p.getPrice() == product.getPrice()
                        && p.getQuantity() == product.getQuantity()) {
                    currentCart.remove(i);
                }
            }
        }

        replaceShoppingCart(currentCart, histFile);
    }

    /**
     * Shopping cart helper method to replace specific product in a customer's cart
     * @param cart
     * @param file
     */
    public static void replaceShoppingCart(ArrayList<ArrayList<Object>> cart, File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            for(ArrayList<Object> arr : cart) {
                if(arr != null) {
                    Product p = (Product) arr.get(1);
                    fw.write(arr.get(0) + ";" + p.getName() + ";"
                            + p.getDescription() + ";" + p.getPrice() + ";" + p.getQuantity());
                    fw.write("\n");
                }
            }
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Adds a seller's product into a specified store's data
     * @param seller
     * @param store
     * @param product
     */
    public static void addSellerData(Seller seller, Store store, Product product) {
        createNecessaryFolders(seller);
        try {
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + store.getName() + "_data");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(0 + ";" + product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity());
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Updates a seller's product in a specified store with a new have quantity and sold quantity; set have quantity to 0 to remove
     * @param seller
     * @param store
     * @param product
     * @param productQuantity
     * @param soldQuantity
     */
    public static void updateSellerData(Seller seller, Store store, Product product, int productQuantity, int soldQuantity) {
        createNecessaryFolders(seller);
        ArrayList<ArrayList<Object>> currentData = getSellerAllData(seller);
        File histFile = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar + store.getName() + "_data");

        for(int i = 0; i < currentData.size(); i++) {
            ArrayList<Object> arr = currentData.get(i);
            for(int j = 2; j < arr.size(); j+=2) {
                String q = (String) arr.get(j-1);
                Product p = (Product) arr.get(j);

                if(productQuantity != 0) {
                    if(p.getName().equals(product.getName())
                            && p.getDescription().equals(product.getDescription())
                            && p.getPrice() == product.getPrice()
                            && p.getQuantity() == product.getQuantity()) {
                        p.setQuantity(productQuantity);
                        arr.set((j-1), Integer.toString(soldQuantity));
                    }
                } else {
                    if(p.getName().equals(product.getName())
                            && p.getDescription().equals(product.getDescription())
                            && p.getPrice() == product.getPrice()
                            && p.getQuantity() == product.getQuantity()) {
                        currentData.remove(i);
                    }
                }
            }
        }

        replaceSellerData(currentData, histFile);
    }

    /**
     * Seller data helper method to update specified lines
     * @param data
     * @param file
     */
    public static void replaceSellerData(ArrayList<ArrayList<Object>> data, File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            for(ArrayList<Object> arr : data) {
                if(arr != null && !arr.isEmpty()) {
                    for(int i = 2; i < arr.size(); i+=2) {
                        String q = (String) arr.get(i-1);
                        Product p = (Product) arr.get(i);
                        fw.write(q + ";" + p.getName() + ";"
                                + p.getDescription() + ";" + p.getPrice() + ";" + p.getQuantity());
                        fw.write("\n");
                    }
                }
            }
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Grabs a list of user logins
     * @return ArrayList<String> {type[0], username[1], email[2], password[3]}
     */
    public static ArrayList<ArrayList<String>> getUserLogins() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(userDataFile))) {
            int counter = 0;
            String line = br.readLine();
            while(line != null) {
                String type = String.valueOf(line.charAt(0));
                String username = line.substring(2,line.indexOf(";"));
                String email = line.substring(line.indexOf(";")+1,
                        line.indexOf(";", line.indexOf(";")+1));
                String password = line.substring(line.indexOf(";",
                        line.indexOf(";", line.indexOf(";")+1))+1);

                data.add(new ArrayList<>(Arrays.asList(type, username, email, password)));

                counter++;
                line = br.readLine();
            }
        } catch(Exception e) {
            throw new RuntimeException(new AccountException());
        }
        return data;
    }

    /**
     * Stores a registered user's logins
     * @param username
     * @param email
     * @param password
     * @param type
     * @return
     */
    public static boolean writeUserSignup(String username, String email, String password, String type) {
        ArrayList<ArrayList<String>> data = getUserLogins();

        for(ArrayList<String> arr : data) {
            if(arr.get(1).equalsIgnoreCase(username)) {
                return false;
            } if(arr.get(2).equalsIgnoreCase(email)) {
                return false;
            }
        }
        if(type.equalsIgnoreCase("customer")) {
            writeCustomer(username, email, password);
            return true;
        } else if(type.equalsIgnoreCase("seller")) {
            writeSeller(username, email, password);
            return true;
        }
        return false;
    }

    /**
     * Write customer login information
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
            e.printStackTrace();
            throw new RuntimeException(new AccountException());
        }
    }

    /**
     * Write seller login information
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
            e.printStackTrace();
            throw new RuntimeException(new AccountException());
        }
    }
}
