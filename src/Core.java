import java.util.*;
import java.io.*;

/**
 * Marketplace main menu system used for logic control flow and to display login, signup, and user actions
 */

/**
 * Outline:
 * - Welcome screen -> Select to login or sign up.
 * -- If [login]: Select customer or seller
 * === If [Customer]: Ask for email and password. Calls AccountManager.login(email, password) in customerAccounts.txt list. Return true if login is successful and false if not.
 * === If [Seller]: Ask for email and password. Calls AccountManager.login(email, password) in sellerAccounts.txt list. Return true if login is successful and false if not.
 * -- If [signup]: Select customer or seller
 * === If [Customer]: Ask for email and password. Calls AccountManager.signup(email, password). Attempts to write to customerAccounts.txt file. Return true if successful and false if not.
 * === If [Seller]: Ask for email and password. Calls AccountManager.signup(email, password). Attempts to write to sellerAccounts.txt file. Return true if successful and false if not.
 * - Program displays customer or seller menus if login is successful.
 * - All loops will repeat until user cancels it.
 */

// IMPORTANT: THIS CLASS IS VERY FRAGILE. PLEASE DON'T BREAK!
class Core {
    private static final String separator = "---------------------------";
    private static Scanner sc = new Scanner(System.in);
    private static User user;

    public static void main(String[] args) {
        system_loop:
        while (true) {
            String email;
            String password;
            String loginSignup;
            String customerSeller;
            String username;

            System.out.println("Welcome to The Marketplace!");
            System.out.println(separator);
            System.out.println("Login or signup to use our service.");
            System.out.println("Please enter" + "\n[1] Login" + "\n[2] Signup");
            loginSignup = sc.nextLine();

            login_signup:
            while (true) {
                if (loginSignup.equals("1")) { // THIS IS THE LOGIN PART
                    System.out.println(separator);
                    System.out.println("Please enter your email: ");
                    email = sc.nextLine();
                    System.out.println("Please enter your password: ");
                    password = sc.nextLine();

                    user = AccountManager.login(email, password);
                    if (user == null) {
                        if (!tryAgain("Email and password combinations are invalid!")) {
                            break system_loop;
                        }
                    }

                    if (AccountManager.login(email, password) instanceof Customer) {
                        System.out.println(separator);
                        System.out.println("Welcome customer: " + user.getUsername());
                        customerMenu();
                    } else if (AccountManager.login(email, password) instanceof Seller) {
                        System.out.println(separator);
                        System.out.println("Welcome seller: " + user.getUsername());
                        sellerMenu();
                    }
                } else if (loginSignup.equals("2")) { // THIS IS THE SIGNUP PART
                    System.out.println(separator);
                    System.out.println("Please enter your email: ");
                    email = sc.nextLine();
                    System.out.println("Please enter your password: ");
                    password = sc.nextLine();
                    System.out.println("Please enter your username: ");
                    username = sc.nextLine();

                    customer_seller:
                    while (true) {
                        System.out.println("Are you signing up to be a customer or seller?");
                        System.out.println("Please enter" + "\n[1] Customer" + "\n[2] Seller");
                        customerSeller = sc.nextLine();

                        if (customerSeller.equals("1")) {
                            AccountManager.signup(email, password, username, "customer");
                            break customer_seller;
                        } else if (customerSeller.equals("2")) {
                            AccountManager.signup(email, password, username, "seller");
                            break customer_seller;
                        } else {
                            if (!tryAgain("Invalid customer/seller selection!")) {
                                break system_loop;
                            }
                        }
                    }
                } else {
                    if (!tryAgain("Invalid login/signup selection!")) {
                        break system_loop;
                    }
                }
            }
        }

        printFarewell();
    }

    /**
     * Asks user to try again if their choice is invalid and returns true/false based on their choice.
     * @param message
     * @return boolean
     */
    public static boolean tryAgain(String message) {
        System.out.println(separator);
        System.out.println(message);
        System.out.println("Please enter 'Y' or 'Yes' to try again, or anything else to quit.");
        String yn = sc.nextLine();
        return (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes"));
    }

    /**
     * Prints out the customer main menu for users to view.
     * Allows for navigation of the menu to see marketplace; view, search, and purchase products; and view history.
     */
    public static void customerMenu() {
        String action;

        System.out.println("What would you like to do today?");
        System.out.println("Please enter");
        System.out.println("[M] Open Marketplace");
        System.out.println("[S] Search for Product");
        System.out.println("[P] View Purchase History");
        System.out.println("[Q] Logout & Quit");

        action = sc.nextLine();
        if (action.equalsIgnoreCase("m")) {
            marketplaceMenu(true);
        } else if (action.equalsIgnoreCase("s")) {

        } else if (action.equalsIgnoreCase("p")) {

        } else if (action.equalsIgnoreCase("q")) {

        } else {
            if(tryAgain("Invalid menu selection!")) {
                customerMenu();
            } else {
                printFarewell();
            }
        }
    }

    public static void marketplaceMenu(boolean printProducts) {
        String productPick;
        MarketPlace mp = new MarketPlace();
        ArrayList<Store> stores = mp.getStores();
        ArrayList<Product> products = new ArrayList<Product>();

        if (printProducts) {
            System.out.println(separator);
            System.out.println("All available stores:");
        }

        for (Store s : stores) {
            int counter = 0;
            ArrayList<Product> tempProds = s.getProducts();
            System.out.println("- " + s.getName());
            if (stores.isEmpty()) {
                if (printProducts) {
                    System.out.println("--- No products");
                }
            } else {
                for (Product p : tempProds) {
                    products.add(p);
                    if (printProducts) {
                        System.out.println("--- #" + (counter + 1) + " " +
                                p.getName() + " (Price: " + p.getPrice() + ")");
                    }
                }
            }
        }
        if (printProducts) {
            System.out.println("To view more info, please enter the number corresponding to the product. " +
                    "Otherwise, enter anything else to go back to the customer menu.");
        } else {
            System.out.println("Please enter a product number.");
        }
        productPick = sc.nextLine();
        if (productPick.matches("-?\\d+(\\.\\d+)?")) {
            Product p = products.get(Integer.parseInt(productPick) - 1);
            if (p != null) {
                System.out.println(separator);
                System.out.println("Product: " + p.getName());
                System.out.println("Price: " + p.getPrice());
                System.out.println("Quantity: " + p.getQuantity());
                System.out.println("Description: " + p.getDescription());
            } else {
                if(tryAgain("Invalid product selection!")) {
                    marketplaceMenu(false);
                } else {
                    printFarewell();
                }
            }
        } else {
            customerMenu();
        }
    }

    public static void searchMenu() {

    }

    public static void historyMenu() {

    }

    public static void sellerMenu() {
        String action;
    }

    public static void updateSeller(Seller s) {
        try {
            File f = new File(s + ".csv");
            if (f.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(f, false);
                PrintWriter pw = new PrintWriter(fos);
                ArrayList<Store> stores = s.getStores();
                for (Store i : stores) {
                    ArrayList<Product> p = i.getProducts();
                    for (Product j : p) {
                        pw.println(j.getName() + "," + j.getDescription() + "," + j.getStore() + "," + j.getPrice() + "," + j.getQuantity());
                    }
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
                ArrayList<Store> stores = s.getStores();
                for (Store i : stores) {
                    ArrayList<Product> p = i.getProducts();
                    for (Product j : p) {
                        pw.println(j.getName() + "," + j.getDescription() + "," + j.getStore() + "," + j.getPrice() + "," + j.getQuantity());
                    }
                }
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the farewell message.
     */
    public static void printFarewell() {
        System.out.println("Thank you for visiting The MarketPlace!");
        System.out.println("Come again another day.");
    }
}