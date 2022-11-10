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
    public static Scanner sc = new Scanner(System.in);

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

                    User user = AccountManager.login(email, password);
                    if (user == null) {
                        if(!tryAgain(sc.nextLine(), "Email and password combinations are invalid!", false)) {
                            break system_loop;
                        }
                    }

                    if (AccountManager.login(email, password) instanceof Customer) {
                        System.out.println(separator);
                        System.out.println("Welcome customer: " + user.getUsername());
                        customerMenu((Customer) user);
                    } else if (AccountManager.login(email, password) instanceof Seller) {
                        System.out.println(separator);
                        System.out.println("Welcome seller: " + user.getUsername());
                        sellerMenu((Seller) user);
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
                    while(true) {
                        System.out.println("Are you signing up to be a customer or seller?");
                        System.out.println("Please enter" + "\n[1] Customer" + "\n[2] Seller");
                        customerSeller = sc.nextLine();

                        if(customerSeller.equals("1")) {
                            AccountManager.signup(email, password, username, "customer");
                            break customer_seller;
                        } else if(customerSeller.equals("2")) {
                            AccountManager.signup(email, password, username, "seller");
                            break customer_seller;
                        } else {
                            if(!tryAgain(sc.nextLine(), "Invalid customer/seller selection!", false)) {
                                break system_loop;
                            }
                        }
                    }
                } else {
                    if(!tryAgain(sc.nextLine(), "Invalid login/signup selection!", false)) {
                        break system_loop;
                    }
                }
            }
        }
    }

    public static boolean tryAgain(String str, String message, boolean menu) {
        System.out.println(separator);
        System.out.println(message);
        if(!menu) {
            System.out.println("Please enter 'Y' or 'Yes' to try again, or anything else to cancel.");
            return (str.equalsIgnoreCase("y") || str.equalsIgnoreCase("yes"));
        } else {
            return true;
        }
    }

    public static void customerMenu(Customer customer) {
        String action;

        System.out.println("What would you like to do today?");
        System.out.println("Please enter");
        System.out.println("[M] Open Marketplace");
        System.out.println("[S] Search for Product");
        System.out.println("[P] View Purchase History");
        System.out.println("[Q] Logout & Quit");

        action = sc.nextLine();
        if (action.equalsIgnoreCase("m")) {

        } else if(action.equalsIgnoreCase("s")) {

        } else if(action.equalsIgnoreCase("p")) {

        } else if(action.equalsIgnoreCase("q")) {

        } else {
            tryAgain("", "Invalid selection! Please try again.", true);
        }
    }

    public static void sellerMenu(Seller seller) {
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
}