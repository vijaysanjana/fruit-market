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
        Seller s = new Seller("jack", "j@gmail.com", "pw");
        Store st = new Store("2nd store", "bbb");
        Product pr = new Product("new prod", "aaa", 1220, 5);
        //addUserHistory(s, st, pr);
        ArrayList<ArrayList<Object>> a = getSellerAllData(s);
        ArrayList<Product> t = getSellerData(s, st);

        for(int i = 0; i < a.size(); i++) {
            ArrayList<Object> aa = a.get(i);
            System.out.println((String) aa.get(0));
            Product pp = (Product) aa.get(1);
            System.out.println(pp.getName());
            System.out.println(pp.getDescription());
            System.out.println(pp.getPrice());
            System.out.println(pp.getQuantity());
        }
    }

    public static void loadAllFiles(MarketPlace mp) {
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Seller> sellers = new ArrayList<>();
        for(ArrayList<String> arr : getUserLogins()) {
            if(arr.get(0).equalsIgnoreCase("C")) {
                customers.add(new Customer(arr.get(1), arr.get(2), arr.get(3)));
            } else if(arr.get(0).equalsIgnoreCase("S")) {
                sellers.add(new Seller(arr.get(1), arr.get(2), arr.get(3)));
            }
        }

        mp.setCustomers(customers);
        mp.setSellers(sellers);
    }

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

    public static ArrayList<Product> getCustomerHistory(Customer customer) {
        createNecessaryFolders(customer);
        try {
            ArrayList<Product> products = new ArrayList<>();
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "purchase_history");
            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                products.add(new Product(arr[0], arr[1], Double.parseDouble(arr[2]), Integer.parseInt(arr[3])));
                line = br.readLine();
            }

            return products;
        } catch(Exception e) {
            throw new RuntimeException(new DataException());
        }
    }

    public static ArrayList<ArrayList<String>> getCustomerShoppingCart(Customer customer) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<String>> cart = new ArrayList<>();
        try {
            int counter = 0;
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "shopping_cart");

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                cart.get(counter).add(arr[0]);
                cart.get(counter).add(arr[1]);
                cart.get(counter).add(arr[2]);
                cart.get(counter).add(arr[3]);
                cart.get(counter).add(arr[4]);
                counter++;
                line = br.readLine();
            }

            return cart;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static ArrayList<Product> getSellerData(Seller seller, Store store) {
        createNecessaryFolders(seller);
        ArrayList<Product> products = new ArrayList<>();
        try {
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + store.getName() + "_data");

            BufferedReader br = new BufferedReader(new FileReader(histFile));
            String line = br.readLine();

            while(line != null) {
                String[] arr = line.split(";");
                products.add(new Product(arr[0], arr[1], Double.parseDouble(arr[2]), Integer.parseInt(arr[3])));
                line = br.readLine();
            }

            return products;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static ArrayList<ArrayList<Object>> getSellerAllData(Seller seller) {
        createNecessaryFolders(seller);
        ArrayList<ArrayList<Object>> products = new ArrayList<>();
        File sellerFolder = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar);

        for(File f : sellerFolder.listFiles()) {
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(f.getName());
            try {
                File histFile = new File(sellerDataFolder + File.separatorChar
                        + seller.getUsername() + File.separatorChar + f.getName());
                BufferedReader br = new BufferedReader(new FileReader(histFile));
                String line = br.readLine();
                while(line != null) {
                    String[] arr = line.split(";");
                    temp.add(new Product(arr[0], arr[1], Double.parseDouble(arr[2]), Integer.parseInt(arr[3])));
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

    public static void addCustomerData(Customer customer, Product product) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "purchase_history");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity());
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static void addCustomerShopppingCart(Customer customer, Product product, int quantity) {
        createNecessaryFolders(customer);
        try {
            File histFile = new File(customerDataFolder + File.separatorChar
                    + customer.getUsername() + File.separatorChar + "shopping_cart");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity() + ";" + quantity);
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static void removeCustomerShoppingCart(Customer customer, Product product, int quantity) {
        createNecessaryFolders(customer);
        ArrayList<ArrayList<String>> currentCart = getCustomerShoppingCart(customer);
        File histFile = new File(sellerDataFolder + File.separatorChar
                + customer.getUsername() + File.separatorChar + "shopping_cart");

        for(int i = 0; i < currentCart.size(); i++) {
            ArrayList<String> arr = currentCart.get(i);
            Product p = new Product(arr.get(0), arr.get(1),
                    Double.parseDouble(arr.get(2)), Integer.parseInt(arr.get(3)));

            if(quantity != 0) {
                if(p.getName().equals(product.getName())
                        && p.getDescription().equals(product.getDescription())
                        && p.getPrice() == product.getPrice()
                        && p.getQuantity() == product.getQuantity()) {
                    arr.set(4, String.valueOf(quantity));
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

    public static void replaceShoppingCart(ArrayList<ArrayList<String>> cart, File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            for(ArrayList<String> arr : cart) {
                if(arr != null) {
                    fw.write(arr.get(0) + ";" + arr.get(1)
                            + arr.get(2) + ";" + arr.get(3) + ";" + arr.get(4));
                }
            }
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static void addSellerData(Seller seller, Store store, Product product) {
        createNecessaryFolders(seller);
        try {
            File histFile = new File(sellerDataFolder + File.separatorChar
                    + seller.getUsername() + File.separatorChar + store.getName() + "_data");
            FileWriter fw = new FileWriter(histFile, true);
            fw.write(product.getName() + ";" + product.getDescription() + ";"
                    + product.getPrice() + ";" + product.getQuantity());
            fw.write("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(new DataException());
        }
    }

    public static void updateSellerData(Seller seller, Store store, Product oldProduct, Product newProduct) {
        createNecessaryFolders(seller);
        ArrayList<Product> currentProd = getSellerData(seller, store); // TODO
        File histFile = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar + store.getName() + "_data");

        for(int i = 0; i < currentProd.size(); i++) {
            Product p = currentProd.get(i);
            if(p.getName().equals(oldProduct.getName())
                    && p.getDescription().equals(oldProduct.getDescription())
                    && p.getPrice() == oldProduct.getPrice()
                    && p.getQuantity() == oldProduct.getQuantity()) {
                currentProd.remove(i);
                currentProd.set(i, newProduct);
            }
        }

        replaceSellerFile(currentProd, histFile);
    }

    public static void removeSellerData(Seller seller, Store store, Product product) {
        createNecessaryFolders(seller);
        ArrayList<Product> currentProd = getSellerData(seller, store); // TODO
        File histFile = new File(sellerDataFolder + File.separatorChar
                + seller.getUsername() + File.separatorChar + store.getName() + "_data");

        for(int i = 0; i < currentProd.size(); i++) {
            Product p = currentProd.get(i);
            if(p.getName().equals(product.getName())
                    && p.getDescription().equals(product.getDescription())
                    && p.getPrice() == product.getPrice()
                    && p.getQuantity() == product.getQuantity()) {
                currentProd.remove(i);
            }
        }

        replaceSellerFile(currentProd, histFile);
    }

    public static void replaceSellerFile(ArrayList<Product> product, File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            for(Product p : product) {
                if(p != null) {
                    fw.write(p.getName() + ";" + p.getDescription() + ";"
                            + p.getPrice() + ";" + p.getQuantity());
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