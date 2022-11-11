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

// IMPORTANT: THIS CLASS IS NO LONGER AS FRAGILE. BUT PLEASE STILL DON'T BREAK!
class Core {
    private static final String separator = "---------------------------";
    private static Scanner sc = new Scanner(System.in);
    private static User user;
    private static ShoppingCart shoppingCart;

    /**
     * Main method for the marketplace system. Arranges login/signup and customer/seller menus.
     * @param args
     */
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

                    if (user instanceof Customer) {
                        System.out.println(separator);
                        System.out.println("Welcome customer: " + user.getUsername());
                        shoppingCart = ((Customer) user).getShoppingCart();
                        customerMainMenu();
                    } else if (user instanceof Seller) {
                        System.out.println(separator);
                        System.out.println("Welcome seller: " + user.getUsername());
                        sellerMainMenu();
                    } else {
                        throw new RuntimeException(
                                new Exception("FATAL ERROR OCCURRED! LOGGED IN USER IS NEITHER CUSTOMER NOR SELLER!"));
                        // TODO: NEED TESTING
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
                            if(AccountManager.signup(email, password, username, "customer") != null) {
                                System.out.println("Successfully signed up! Logging you in...");
                                user = AccountManager.login(email, password);
                                if (user instanceof Customer) {
                                    System.out.println(separator);
                                    System.out.println("Welcome customer: " + user.getUsername());
                                    shoppingCart = ((Customer) user).getShoppingCart();
                                    customerMainMenu();
                                } else {
                                    throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! REGISTERED CUSTOMER IS NOT A CUSTOMER!"));
                                }
                            } else {
                                throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! CUSTOMER REGISTRATION FAILED!"));
                                // TODO: NEED TESTING
                            }
                        } else if (customerSeller.equals("2")) {
                            if(AccountManager.signup(email, password, username, "seller") != null) {
                                System.out.println("Successfully signed up! Logging you in...");
                                user = AccountManager.login(email, password);
                                if (user instanceof Seller) {
                                    System.out.println(separator);
                                    System.out.println("Welcome seller: " + user.getUsername());
                                    sellerMainMenu();
                                } else {
                                    throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! REGISTERED SELLER IS NOT A SELLER!"));
                                }
                            } else {
                                throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! SELLER REGISTRATION FAILED!"));
                                // TODO: NEED TESTING
                            }
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
        System.out.println("Please enter [Y] or [Yes] to try again, or [Anything Else] to quit.");
        String yn = sc.nextLine();
        return (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes"));
    }

    /**
     * Prints out the customer main menu for users to view.
     * Allows for navigation of the menu to see marketplace; view, search, and purchase products; and view history.
     */
    public static void customerMainMenu() {
        System.out.println("What would you like to do today?");
        System.out.println("Please enter");
        System.out.println("[M] Open Marketplace");
        System.out.println("[S] Search for Product");
        System.out.println("[C] View Shopping Cart (" + shoppingCart.getHeldPurchases().size() + " Products)");
        System.out.println("[P] View Purchase History");
        System.out.println("[Q] Logout & Quit");

        String action = sc.nextLine();
        if (action.equalsIgnoreCase("m")) {
            marketplaceMenu();
        } else if (action.equalsIgnoreCase("s")) {
            searchMenu();
        } else if(action.equalsIgnoreCase("c")) {
            cartMenu();
        } else if (action.equalsIgnoreCase("p")) {
            historyMenu();
        } else if (action.equalsIgnoreCase("q")) {

        } else {
            if(tryAgain("Invalid menu selection!")) {
                customerMainMenu();
            } else {
                printFarewell();
            }
        }
    }

    public static void marketplaceMenu() {
        MarketPlace mp = new MarketPlace();
        ArrayList<Store> stores = mp.getStores();
        ArrayList<Product> products = new ArrayList<Product>();

        System.out.println(separator);
        System.out.println("All available stores:");

        for (Store s : stores) {
            int counter = 0;
            ArrayList<Product> tempProds = s.getProducts();
            System.out.println("- " + s.getName());
            if (stores.isEmpty()) {
                System.out.println("--- No products");
            } else {
                for (Product p : tempProds) {
                    products.add(p);
                    System.out.println("--- #" + (counter + 1) + " " +
                            p.getName() + " (Price: " + p.getPrice() + ")");
                }
            }
        }

        System.out.println(separator);
        System.out.println("Please enter");
        System.out.println("[Correspond #] View Product Info");
        System.out.println("[Anything Else] Return to Customer Menu");

        String productPick = sc.nextLine();
        if (productPick.matches("-?\\d+(\\.\\d+)?")) {
            Product p = products.get(Integer.parseInt(productPick) - 1);
            if (p != null) {
                System.out.println(separator);
                System.out.println("Product: " + p.getName());
                System.out.println("Price: " + p.getPrice());
                System.out.println("Quantity: " + p.getQuantity());
                System.out.println("Description: " + p.getDescription());
                System.out.println(separator);
                System.out.println("Please enter");
                System.out.println("[A] Add to Shopping Cart");
                System.out.println("[Anything Else] Return to All Products Page");

                String productAction = sc.nextLine();
                if(productAction.equalsIgnoreCase("a")) {
                    System.out.println(separator);
                    System.out.println("Please enter a purchase quantity: ");
                    String purchaseQuantity = sc.nextLine();
                    do {
                        System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                        purchaseQuantity = sc.nextLine();
                    } while(!purchaseQuantity.matches("-?\\d+(\\.\\d+)?"));

                    shoppingCart.addPurchase(new Sale((Customer) user, p, Integer.parseInt(purchaseQuantity)));
                    System.out.println("Successfully added " + purchaseQuantity + " to your shopping cart." +
                            " Returning to available products page...");
                    marketplaceMenu();
                } else {
                    marketplaceMenu();
                }
            } else {
                do {
                    System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                    productPick = sc.nextLine();
                    if(!productPick.matches("-?\\d+(\\.\\d+)?")) {
                        continue;
                    }
                    p = products.get(Integer.parseInt(productPick) - 1);
                } while(p == null);
            }
        } else {
            customerMainMenu();
        }
    }

    public static void searchMenu() {
        String searchParam;
        MarketPlace mp = new MarketPlace();

        System.out.println(separator);
        System.out.println("Please enter your search parameter: ");
        searchParam = sc.nextLine();

    }

    public static void cartMenu() {
        int counter = 0;
        System.out.println(separator);
        System.out.println("Your shopping cart:");
        if(shoppingCart.getHeldPurchases().size() == 0) {
            System.out.println("- Empty");
        } else {
            for(Sale s : shoppingCart.getHeldPurchases()) {
                String totalPrice = String.format(
                        String.valueOf((s.getProduct().getQuantity()*s.getProduct().getPrice())),
                        "%.2f");
                System.out.println("- #" + (counter+1) + " " + s.getProduct().getName()
                        + " (" + s.getProduct().getQuantity() + " for $"
                        + s.getProduct().getPrice() + " each; Total $"
                        + totalPrice + ")");
            }
        }

        System.out.println(separator);
        System.out.println("Please enter");
        System.out.println("[Corresponding #] View Product Info");
        System.out.println("[Anything Else] Return to Customer Menu");

        String cartPick = sc.nextLine();
        if(cartPick.matches("-?\\d+(\\.\\d+)?")) {
            Sale s = shoppingCart.getHeldPurchases().get(Integer.parseInt(cartPick)-1);
            Product p = s.getProduct();
            if(p != null) {
                System.out.println(separator);
                System.out.println("Product: " + p.getName());
                System.out.println("Price: " + p.getPrice());
                System.out.println("Quantity: " + p.getQuantity());
                System.out.println("Description: " + p.getDescription());
                System.out.println(separator);
                System.out.println("Please enter");
                System.out.println("[R] Remove from Shopping Cart");
                System.out.println("[K] Change Purchase Quantity");
                System.out.println("[Anything Else] Return to All Products Page");

                String cartAction = sc.nextLine();
                if(cartAction.equalsIgnoreCase("r")) {
                    System.out.println("Successfully removed " + p.getName() + " from your shopping cart." +
                            " Returning to shopping cart page...");
                    cartMenu();
                } else if(cartAction.equalsIgnoreCase("k")) {
                    System.out.println(separator);
                    System.out.println("Please enter a new quantity: ");
                    String changeQuantity = sc.nextLine();
                    do {
                        System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                        changeQuantity = sc.nextLine();
                    } while(!changeQuantity.matches("-?\\d+(\\.\\d+)?"));

                    shoppingCart.getHeldPurchases().set((Integer.parseInt(cartPick)-1),
                            new Sale(s.getCustomer(), p, Integer.parseInt(changeQuantity)));
                    System.out.println("Successfully changed purchase quantity to " + changeQuantity + "." +
                            " Returning to shopping cart page...");
                    cartMenu();
                } else {
                    cartMenu();
                }
            } else {
                do {
                    System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                    cartPick = sc.nextLine();
                    if(!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                        continue;
                    }
                    p = shoppingCart.getHeldPurchases().get(Integer.parseInt(cartPick)-1).getProduct();
                } while(p == null);
            }
        } else {
            customerMainMenu();
        }
    }

    public static void historyMenu() {

    }

    public static void sellerMainMenu() {
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