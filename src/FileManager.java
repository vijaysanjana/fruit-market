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
        addSellerData(dendenMan, dennys, new Product("Gamer Eggs", "Nature's Protein Shake", 19.99));
        addSellerData(dendenMan, dennys, new Product("Gamer Sausage", "Nature's Protein... Protein", 5.99));
        addSellerData(dendenMan, dennys, new Product("Gamer Pancakes", "Nature's Gluten Shake", 13.99));


        Seller god = new Seller("God", "God@god.cod", "rapture");
        Store soapStore = new Store("God's Soap Emporium", "Got bored, like soap - God");
        addSellerData(god, soapStore, new Product("Dawn PowerWash Dish Spray", "clean up you gross ass plate", 4.99));
        addSellerData(god, soapStore, new Product("Lavender Ascent", "Isn't this a good name for soap?", 49.99));
        addSellerData(god, soapStore, new Product("Tide Pods", "50% more edible than competitors", 2.49));

        MarketPlace mp = new MarketPlace();
        loadAllStores(mp, true);
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
    public static void loadAllStores(MarketPlace mp, boolean loadEmpty) {
        File sellLocation = new File(sellerDataFolder + File.separatorChar);
        try {
            if(sellLocation.listFiles().length != 0) {
                for(File f : sellLocation.listFiles()) {
                    String sellerName = f.getName();
                    ArrayList<ArrayList<String>> logins = getUserLogins();
                    for(ArrayList<String> logs : logins) {
                        if(logs.get(0).equalsIgnoreCase("S") && logs.get(1).equalsIgnoreCase(sellerName)) {
                            Seller tempSeller = new Seller(logs.get(1), logs.get(2), logs.get(3));
                            ArrayList<ArrayList<Object>> allStores = getSellerAllData(tempSeller);
                            for(ArrayList<Object> arr : allStores) {
                                Store tempStore = new Store((String) arr.get(0), "");
                                for(int i = 2; i < arr.size(); i+=2) {
                                    Product p = (Product) arr.get(i);
                                    if(loadEmpty) {
                                        tempStore.addProduct(p);
                                    } else {
                                        if(p.getQuantity() >= 1) {
                                            tempStore.addProduct(p);
                                        }
                                    }
                                }
                                tempSeller.addStore(tempStore);
                                mp.addSeller(tempSeller);
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException("STORE LOADING FAILED!"));
        }
    }

    /**
     * Loads in all shopping carts for a specified customer and initializes into their object
     * @param customer
     * @param spc
     * @param mp
     */
    public static void loadAllCarts(Customer customer, ShoppingCart spc, MarketPlace mp) {
        File cartFile = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");
        try {
            ArrayList<ArrayList<Object>> cartData = getCustomerShoppingCart(customer);
            for(ArrayList<Object> arr : cartData) {
                for(Product prod : mp.getProducts()) {
                    if(prod.equals(arr.get(1))) {
                        if(prod.getQuantity()-Integer.parseInt((String) arr.get(0)) < 0) {
                            Product tempProd = new Product(prod.getName(),
                                    prod.getDescription(), prod.getPrice(), prod.getQuantity());
                            prod.setQuantity(0);
                            spc.addPurchase(new Sale(customer, prod, tempProd.getQuantity()));
                        } else {
                            prod.setQuantity(prod.getQuantity()-Integer.parseInt((String) arr.get(0)));
                            spc.addPurchase(new Sale(customer, prod, Integer.parseInt((String) arr.get(0))));
                        }
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(new DataException("SHOPPING CART LOADING FAILED!"));
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

            if(!histFile.exists()) {
                return data;
            }

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                data.add(new ArrayList<>());
                data.get(counter).add(arr[0]);
                data.get(counter).add(new Product(arr[1], arr[2],
                        Double.parseDouble(arr[3]), Integer.parseInt(arr[4])));
                counter++;
                line = br.readLine();
            }

            return data;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    /**
     * Returns a customer's shopping cart intended purchase quantity for a specified product
     * @param customer
     * @param product
     * @return
     */
    public static int getCustomerShoppingCartQuantity(Customer customer, Product product) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<Object>> cart = getCustomerShoppingCart(customer);
        for(ArrayList<Object> arr : cart) {
            Product p = (Product) arr.get(1);
            if(product.getName().equals(p.getName())
                    && product.getDescription().equals(p.getDescription())
                    && product.getPrice() == p.getPrice()) {
                return Integer.parseInt((String) arr.get(0));
            }
        }
        return -1;
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

            if(!histFile.exists()) {
                return cart;
            }

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
     * @return (String)purchasedQuantity[1 thru n] where n is an odd number
     * @return (Product)product_objects[2 thru n] where n is an even number
     */
    public static ArrayList<Object> getSellerData(Seller seller, Store store) {
        createNecessaryFolders(seller);
        ArrayList<Object> products = new ArrayList<>();
        products.add("");
        try {
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + store.getName() + "_data");

            if(!histFile.exists()) {
                return products;
            }

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
     * @return ArrayList<ArrayList<Object>> => {per store}
     * (String)store_name[0],
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
     * @param purchaseQuantity
     */
    public static void addCustomerData(Customer customer, Product product, int purchaseQuantity) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "purchase_history");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(purchaseQuantity + ";" + product.getName() + ";" + product.getDescription() + ";"
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
     * @param purchaseQuantity
     */
    public static void addCustomerShopppingCart(Customer customer, Product product, int purchaseQuantity) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "shopping_cart");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(purchaseQuantity + ";" + product.getName() + ";" + product.getDescription() + ";"
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
     * @param purchaseQuantity
     */
    public static void updateCustomerShoppingCart(Customer customer, Product product, int purchaseQuantity) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<Object>> currentCart = getCustomerShoppingCart(customer);
        File histFile = new File(customerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");

        for(int i = 0; i < currentCart.size(); i++) {
            ArrayList<Object> arr = currentCart.get(i);
            Product p = (Product) arr.get(1);

            if(purchaseQuantity != 0) {
                if(p.getName().equals(product.getName())
                        && p.getDescription().equals(product.getDescription())
                        && p.getPrice() == product.getPrice()) {
                    System.out.println(purchaseQuantity);
                    arr.set(0, String.valueOf(purchaseQuantity));
                }
            } else {
                if(p.getName().equals(product.getName())
                        && p.getDescription().equals(product.getDescription())
                        && p.getPrice() == product.getPrice()) {
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
            //ArrayList<Object> tempArr = new ArrayList<>(currentData.get(i));
            for(int j = 2; j < arr.size(); j+=2) {
                String q = (String) arr.get(j-1);
                Product p = (Product) arr.get(j);

                //if(productQuantity != 0) {
                    if(p.getName().equals(product.getName())
                            && p.getDescription().equals(product.getDescription())
                            && p.getPrice() == product.getPrice()) {
                        p.setQuantity(productQuantity);
                        arr.set((j-1), Integer.toString(soldQuantity));
                    }
                //}// else {
                 //   if(p.getName().equals(product.getName())
                 //           && p.getDescription().equals(product.getDescription())
                 //           && p.getPrice() == product.getPrice()) {
                 //       tempArr.remove(j-1);
                 //       tempArr.remove(j-1);
                 //   }
                //}
            }
            //currentData.set(i, tempArr);
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

    /**
     * Removes a user's account and deletes all of their data
     * @param user
     */
    public static void removeAccount(User user) {
        File f = null;
        if(user instanceof Customer) {
            f = new File(customerDataFolder + File.separatorChar + user.getUsername());
        } else if(user instanceof Seller) {
            f = new File(sellerDataFolder + File.separatorChar + user.getUsername());
        }
        for(File t : f.listFiles()) {
            try {
                PrintWriter pw = new PrintWriter(t);
                pw.print("");
                pw.close();
            } catch(FileNotFoundException e) {
                throw new RuntimeException(new DataException("ACCOUNT DELETION ERROR!"));
            }
        }
        ArrayList<ArrayList<String>> accounts = getUserLogins();
        ArrayList<ArrayList<String>> tempAcc = new ArrayList<>(accounts);
        for(int i = 0; i < accounts.size(); i++) {
            ArrayList<String> arr = accounts.get(i);
            if(arr.get(1).equalsIgnoreCase(user.getUsername())
                    && arr.get(2).equalsIgnoreCase(user.getEmail())) {
                tempAcc.remove(i);
            }
        }

        try {
            PrintWriter pw = new PrintWriter(userDataFile);
            for(ArrayList<String> arr : tempAcc) {
                pw.write(arr.get(0) + ":" + arr.get(1) + ";" + arr.get(2)
                        + ";" + arr.get(3) + "\n");
            }
            pw.close();
        } catch(FileNotFoundException e) {
            throw new RuntimeException(new DataException("ACCOUNT DELETION ERROR!"));
        }
    }

    /**
     * Creates an appropriate store file (empty) when making new stores
     * @param seller
     * @param store
     */
    public static void createStoreFile(Seller seller, Store store) {
        createNecessaryFolders(seller);
        try {
            if(new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar
                    + store.getName() + "_data").createNewFile()) {
                System.out.println("Customer data folder created");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }
}
