import javax.swing.*;
import java.util.*;

/**
 * Marketplace main menu system used for logic control flow and to display login, signup, and user actions
 *
 * @author Jack, Nathan, Sanjana, Tommy, Aadit
 * @version 11/14/2022
 */
class Core {
    public static final String separator = "---------------------------";
    private static Scanner sc = new Scanner(System.in);
    private static User user;
    private static ShoppingCart shoppingCart;
    private static MarketPlace mp = new MarketPlace();

    /**
     * Main method for the marketplace system. Arranges login/signup and customer/seller menus
     *
     * @param args
     */
    public static void main(String[] args) {
        system_loop:
        while (true) {
            String email;
            String password;
            int loginSignup;
            int customerSeller;
            String username;

            JOptionPane.showMessageDialog(null, "Welcome to The Marketplace!", "Welcome", JOptionPane.PLAIN_MESSAGE);
            String[] options = {"Login", "Signup"};
            loginSignup = JOptionPane.showOptionDialog(null, "Login or signup to use our service.", "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            login_signup:
            while (true) {
                if (loginSignup == 0) { // THIS IS THE LOGIN PART
                    login_loop:
                    while (true) {
                        email = JOptionPane.showInputDialog("Please enter your email");
                        password = JOptionPane.showInputDialog("Please enter your password");

                        user = AccountManager.login(email, password);
                        if (user == null) {
                            if (!tryAgain("Email and password combinations are invalid!")) {
                                printFarewell();
                                break system_loop;
                            }
                        } else {
                            break;
                        }
                    }
                    if (user instanceof Customer) {
                        JOptionPane.showMessageDialog(null, "Welcome customer " + user.getUsername() + "!");
                        // TESTING
                        FileManager.loadAllStores(mp, true);
                        for (Customer customer : mp.getCustomers()) {
                            if (customer.getUsername().equalsIgnoreCase(user.getUsername())
                                    && customer.getEmail().equalsIgnoreCase(user.getEmail())) {
                                user = customer;
                            }
                        }
                        shoppingCart = ((Customer) user).getShoppingCart();
                        FileManager.loadAllCarts((Customer) user, shoppingCart, mp);
                        // TESTING

                        customerMainMenu();
                        break system_loop;
                    } else if (user instanceof Seller) {
                        System.out.println(separator);
                        System.out.println("Welcome seller: " + user.getUsername());

                        // TESTING
                        FileManager.loadAllStores(mp, true);
                        for (Seller seller : mp.getSellers()) {
                            if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                                    && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                                user = seller;
                            }
                        }
                        // TESTING

                        sellerMainMenu();
                        break system_loop;
                    } else {
                        throw new RuntimeException(
                                new AccountException("LOGGED IN USER IS NEITHER CUSTOMER NOR SELLER!"));
                    }
                } else if (loginSignup == 1) { // THIS IS THE SIGNUP PART
                    email = JOptionPane.showInputDialog("Please enter your email.");
                    password = JOptionPane.showInputDialog("Please enter your password.");
                    username = JOptionPane.showInputDialog("Please enter your username.");

                    customer_seller:
                    while (true) {
                        String[] options = {"Customer", "Seller"};
                        customerSeller = JOptionPane.showOptionDialog(null, "What would you like to sign up as?", "Signup", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if (customerSeller == 0) {
                            if (AccountManager.signup(username, email, password, "customer") != null) {
                                JOptionPane.showMessageDialog(null, "Successfully signed up! Logging you in...");
                                user = AccountManager.login(email, password);
                                if (user instanceof Customer) {
                                    JOptionPane.showMessageDialog(null, "Welcome customer " + user.getUsername() + "!");
                                    shoppingCart = ((Customer) user).getShoppingCart();
                                    customerMainMenu();
                                    break system_loop;
                                } else {
                                    throw new RuntimeException(
                                            new AccountException("REGISTERED CUSTOMER IS NOT A CUSTOMER!"));
                                }
                            } else {
                                if (!tryAgain("User already exists! Please login " +
                                        "or use another email and username.")) {
                                    printFarewell();
                                    break system_loop;
                                } else {
                                    main(new String[0]);
                                    break system_loop;
                                }
                            }
                        } else if (customerSeller == 1) {
                            if (AccountManager.signup(username, email, password, "seller") != null) {
                                System.out.println("Successfully signed up! Logging you in...");
                                user = AccountManager.login(email, password);
                                if (user instanceof Seller) {
                                    System.out.println(separator);
                                    System.out.println("Welcome seller: " + user.getUsername());
                                    sellerMainMenu();
                                    break system_loop;
                                } else {
                                    throw new RuntimeException(
                                            new AccountException("REGISTERED SELLER IS NOT A SELLER!"));
                                }
                            } else {
                                if (!tryAgain("User already exists! Please login " +
                                        "or use another email and username.")) {
                                    printFarewell();
                                    break system_loop;
                                } else {
                                    main(new String[0]);
                                    break system_loop;
                                }
                            }
                        } else {
                            printFarewell();
                            break system_loop;
                        }
                    }
                } else {
                    if (!tryAgain("Invalid login/signup selection!")) {
                        printFarewell();
                        break system_loop;
                    }
                }
            }
        }
    }

    /**
     * Asks user to try again if their choice is invalid and returns true/false based on their choice.
     *
     * @param message
     * @return boolean
     */
    public static boolean tryAgain(String message) {
        JOptionPane.showMessageDialog(null, message, "Try again", JOptionPane.ERROR_MESSAGE);
        String[] options = {"Yes", "No"};
        int yn = JOptionPane.showOptionDialog(null, "Please enter Yes to try again or No to quit.", "try again", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return (yn == 0);
    }

    /**
     * Prints out the customer main menu for users to view.
     * Allows for navigation of the menu to see marketplace; view, search, and purchase products; and view history.
     */
    public static void customerMainMenu() {
        System.out.println(separator);
        String[] options = {"Open Marketplace", "Search for Fruit", "View Shopping Cart ("
                + shoppingCart.getTotalheldProducts() + " Fruits)", "View Purchase History", "View Statistics Dashboard", "Delete Account", "Logout & Quit"};
        int action = JOptionPane.showOptionDialog(null, "What would you like to do today?", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (action == 0) {
            marketplaceMenu();
        } else if (action == 1) {
            searchMenu();
        } else if (action == 2) {
            cartMenu();
        } else if (action == 3) {
            historyMenu();
        } else if (action == 4) {
            dashboardMenu(0);
        } else if (action == 5) {
            deleteAccount(user);
        } else if (action == 6) {
            printFarewell();
        }
    }

    /**
     * Navigates through the marketplace menu for customers
     */
    public static void marketplaceMenu() {
        ArrayList<Store> stores = mp.getStores();
        ArrayList<Product> products = new ArrayList<Product>();
        String info = "";
        info += "All available stores:\n";


        int counter = 0;
        for (Store s : stores) {
            ArrayList<Product> tempProds = s.getProducts();
            info += "- " + s.getName() + "\n";
            if (stores.isEmpty()) {
                info += "--- No fruits found";
            } else {
                for (Product p : tempProds) {
                    products.add(p);
                    info += "--- #" + (counter + 1) + " " +
                            p.getName() + " (Price $" + p.getPrice() + " " +
                            "| Quantity Available " + p.getQuantity() + ")\n";
                    counter++;
                }
            }
        }
        if (!products.isEmpty()) {
            String[] options = {"Sort Fruits by Price (High to Low)", "Sort Fruits by Price (Low to High)", "Sort Fruits by Quantity Available (High to Low)", "Sort Fruits by Quantity Available (High to Low)", "View Fruit Info", "Return to Customer Menu"};
            int productPick = JOptionPane.showOptionDialog(null, info, "Stores", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[0]);
            if (productPick == 5) {
                customerMainMenu();
            } else {
                Product p = products.get(Integer.parseInt(productPick) - 1);
                if (p.getQuantity() <= 0) {
                    while (p.getQuantity() <= 0) {
                        System.out.println("There is no more of this fruit available to purchase! " +
                                "Please enter another option: ");
                        if (productPick.matches("-?\\d+(\\.\\d+)?") && !(Integer.parseInt(productPick) < 1)) {
                            p = products.get(Integer.parseInt(productPick) - 1);
                            if (p.getQuantity() >= 1) {
                                if (p != null) {
                                    showProductInfo(p);
                                    System.out.println(separator);
                                    String[] choices = {"Add to Shopping Cart", "Return to All Fruits Page"};
                                    int productAction = JOptionPane.showOptionDialog(null, "What would you like to do?", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, options[0]);
                                    if (productAction == 0) {
                                        System.out.println(separator);
                                        System.out.println("Please enter a purchase quantity: ");
                                        String purchaseQuantity = sc.nextLine();
                                        while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?") ||
                                                (Integer.parseInt(purchaseQuantity) < 1)) {
                                            System.out.println("Entered quantity is not a valid integer! " +
                                                    "Please enter a valid quantity: ");
                                            purchaseQuantity = sc.nextLine();
                                        }
                                        while (p.getQuantity() - Integer.parseInt(purchaseQuantity) < 0) {
                                            System.out.println("There is only " + p.getQuantity() + " of " +
                                                    "this fruit available to purchase.");
                                            System.out.println("Please try a smaller quantity: ");
                                            purchaseQuantity = sc.nextLine();
                                        }

                                        shoppingCart.addPurchase(new Sale((Customer) user, p,
                                                Integer.parseInt(purchaseQuantity)));

                                        // TESTING
                                        FileManager.addCustomerShopppingCart((Customer) user, p,
                                                Integer.parseInt(purchaseQuantity));
                                        p.setQuantity(p.getQuantity() - Integer.parseInt(purchaseQuantity));
                                        // TESTING

                                        System.out.println("Successfully added " + purchaseQuantity
                                                + " to your shopping cart." +
                                                " Returning to available fruits page...");
                                        marketplaceMenu();
                                        break;
                                    } else {
                                        marketplaceMenu();
                                        break;
                                    }
                                }
                            }
                        } else {
                            customerMainMenu();
                            break;
                        }
                    }
                } else {
                    if (p != null) {
                        showProductInfo(p);
                        System.out.println(separator);

                        System.out.println("Please enter: ");
                        System.out.println("[1] Add to Shopping Cart");
                        System.out.println("[Anything Else] Return to All Fruits Page");

                        String productAction = sc.nextLine();
                        if (productAction.equalsIgnoreCase("1")) {
                            System.out.println(separator);
                            System.out.println("Please enter a purchase quantity: ");
                            String purchaseQuantity = sc.nextLine();
                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                    || (Integer.parseInt(purchaseQuantity) < 1)) {
                                System.out.println("Entered quantity is not a valid integer! " +
                                        "Please enter a valid quantity: ");
                                purchaseQuantity = sc.nextLine();
                            }
                            while (p.getQuantity() - Integer.parseInt(purchaseQuantity) < 0) {
                                System.out.println("There is only " + p.getQuantity()
                                        + " of this fruit available to purchase.");
                                System.out.println("Please try a smaller quantity: ");
                                purchaseQuantity = sc.nextLine();
                            }

                            shoppingCart.addPurchase(new Sale((Customer) user,
                                    p, Integer.parseInt(purchaseQuantity)));

                            // TESTING
                            FileManager.addCustomerShopppingCart((Customer) user,
                                    p, Integer.parseInt(purchaseQuantity));
                            p.setQuantity(p.getQuantity() - Integer.parseInt(purchaseQuantity));
                            // TESTING

                            System.out.println("Successfully added " +
                                    purchaseQuantity + " to your shopping cart." +
                                    " Returning to available fruits page...");
                            marketplaceMenu();
                        } else {
                            marketplaceMenu();
                        }
                    }
                }
            }
        } else if (productPick.equalsIgnoreCase("ph")) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getPriceSortedProducts(true));
            }
            marketplaceMenu();
        } else if (productPick.equalsIgnoreCase("pl")) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getPriceSortedProducts(false));
            }
            marketplaceMenu();
        } else if (productPick.equalsIgnoreCase("qh")) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getQuantitySortedProducts(true));
            }
            marketplaceMenu();
        } else if (productPick.equalsIgnoreCase("ql")) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getQuantitySortedProducts(false));
            }
            marketplaceMenu();
        } else {
            customerMainMenu();
        }
    } else

    {
        System.out.println(separator);
        System.out.println("No fruits available.");
        System.out.println("Please enter: ");
        System.out.println("[1] Return to Customer Menu");
        System.out.println("[Anything Else] Logout & Quit");

        String noProdAction = sc.nextLine();
        if (noProdAction.equalsIgnoreCase("1")) {
            customerMainMenu();
        } else {
            printFarewell();
        }
    }

}

    /**
     * Displays information about a given product
     *
     * @param product
     */
    private static void showProductInfo(Product product) {
        System.out.println(separator);

        System.out.println("Fruit: " + product.getName());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + String.format("%.2f", product.getPrice()));
        System.out.println("Quantity Available: " + product.getQuantity());
    }

    public static void searchMenu() {
        ArrayList<Product> productsFound = new ArrayList<>();
        int counter = 0;

        System.out.println(separator);
        System.out.println("What would you like to search for?");
        System.out.println("Please enter: ");
        System.out.println("[1] Fruit Name");
        System.out.println("[2] Fruit Description");
        System.out.println("[3] Store Name");
        System.out.println("[Anything Else] Return to Customer Menu");

        String searchAction = sc.nextLine();
        if (searchAction.equalsIgnoreCase("1")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Product> result = mp.searchProducts("name", searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Fruit Name):");

            if (result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for (Product p : result) {
                    productsFound.add(p);
                    System.out.println("- #" + (counter + 1) + " " +
                            p.getName() + " (Price: " + p.getPrice()
                            + " | Quantity Available " + p.getQuantity() + ")");
                    counter++;
                }
            }
            addSearchProduct(productsFound);
        } else if (searchAction.equalsIgnoreCase("2")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Product> result = mp.searchProducts("desc", searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Fruit Description):");

            if (result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for (Product p : result) {
                    productsFound.add(p);
                    System.out.println("- #" + (counter + 1) + " " +
                            p.getName() + " (Price: " + p.getPrice()
                            + " | Quantity Available " + p.getQuantity() + ")");
                    counter++;
                }
            }
            addSearchProduct(productsFound);
        } else if (searchAction.equalsIgnoreCase("3")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Store> result = mp.searchStores(searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Store Name):");

            if (result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for (Store s : result) {
                    System.out.println("- " + s.getName());
                    if (result.isEmpty()) {
                        System.out.println("--- No fruits found");
                        System.out.println("Returning to search menu...");
                        searchMenu();
                    } else {
                        for (Product p : s.getProducts()) {
                            productsFound.add(p);
                            System.out.println("--- #" + (counter + 1) + " "
                                    + p.getName() + " (Price $" + p.getPrice()
                                    + " | Quantity Available " + p.getQuantity() + ")");
                            counter++;
                        }
                    }
                }
            }
            addSearchProduct(productsFound);
        } else {
            customerMainMenu();
        }
    }

    public static void addSearchProduct(ArrayList<Product> productsFound) {
        if (!productsFound.isEmpty()) {
            System.out.println(separator);
            System.out.println("Please enter: ");
            System.out.println("[Correspond #] View Fruit Info");
            System.out.println("[Anything Else] Return to Customer Menu");

            String productPick = sc.nextLine();

            if (productPick.matches("-?\\d+(\\.\\d+)?")
                    && !(Integer.parseInt(productPick) < 1)) { // TODO: needs testing
                while (Integer.parseInt(productPick) - 1 > productsFound.size()) {
                    System.out.println("Entered # is not a valid item! Please enter a valid item #: ");
                    productPick = sc.nextLine();
                    if (!productPick.matches("-?\\d+(\\.\\d+)?")
                            || (Integer.parseInt(productPick) < 1)) {
                        productPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if (productPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    Product p = productsFound.get(Integer.parseInt(productPick) - 1);
                    if (p.getQuantity() <= 0) {
                        while (p.getQuantity() <= 0) {
                            System.out.println("There is no more of this fruit available to purchase! " +
                                    "Please enter another option: ");
                            productPick = sc.nextLine();
                            if (productPick.matches("-?\\d+(\\.\\d+)?")
                                    && (Integer.parseInt(productPick) > 1)) {
                                p = productsFound.get(Integer.parseInt(productPick) - 1);
                                if (p.getQuantity() >= 1) {
                                    if (p != null) { // TODO: needs testing
                                        showProductInfo(p);
                                        System.out.println(separator);
                                        System.out.println("Please enter: ");
                                        System.out.println("[1] Add to Shopping Cart");
                                        System.out.println("[Anything Else] Return to Search Page");

                                        String productAction = sc.nextLine();
                                        if (productAction.equalsIgnoreCase("1")) {
                                            System.out.println(separator);
                                            System.out.println("Please enter a purchase quantity: ");
                                            String purchaseQuantity = sc.nextLine();
                                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                                    || (Integer.parseInt(purchaseQuantity) < 1)) {
                                                System.out.println("Entered quantity is not a valid integer! " +
                                                        "Please enter a valid quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }
                                            while (p.getQuantity() - Integer.parseInt(purchaseQuantity) < 0) {
                                                System.out.println("There is only " + p.getQuantity() + " of " +
                                                        "this fruit available to purchase.");
                                                System.out.println("Please try a smaller quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }

                                            shoppingCart.addPurchase(new Sale((Customer) user,
                                                    p, Integer.parseInt(purchaseQuantity)));

                                            // TESTING
                                            FileManager.addCustomerShopppingCart((Customer) user,
                                                    p, Integer.parseInt(purchaseQuantity));
                                            p.setQuantity(p.getQuantity() - Integer.parseInt(purchaseQuantity));
                                            // TESTING

                                            System.out.println("Successfully added " + purchaseQuantity
                                                    + " to your shopping cart." +
                                                    " Returning to search page...");
                                            searchMenu();
                                            break;
                                        } else {
                                            searchMenu();
                                            break;
                                        }
                                    }
                                }
                            } else {
                                customerMainMenu();
                                break;
                            }
                        }
                    } else {
                        if (p != null) { // TODO: needs testing
                            showProductInfo(p);
                            System.out.println(separator);
                            System.out.println("Please enter: ");
                            System.out.println("[1] Add to Shopping Cart");
                            System.out.println("[Anything Else] Return to Search Page");

                            String productAction = sc.nextLine();
                            if (productAction.equalsIgnoreCase("1")) {
                                System.out.println(separator);
                                System.out.println("Please enter a purchase quantity: ");
                                String purchaseQuantity = sc.nextLine();
                                while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                        || (Integer.parseInt(purchaseQuantity) < 1)) {
                                    System.out.println("Entered quantity is not a valid integer! " +
                                            "Please enter a valid quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }
                                while (p.getQuantity() - Integer.parseInt(purchaseQuantity) < 0) {
                                    System.out.println("There is only " + p.getQuantity() + " " +
                                            "of this fruit available to purchase.");
                                    System.out.println("Please try a smaller quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }

                                shoppingCart.addPurchase(new Sale((Customer) user,
                                        p, Integer.parseInt(purchaseQuantity)));

                                // TESTING
                                FileManager.addCustomerShopppingCart((Customer) user,
                                        p, Integer.parseInt(purchaseQuantity));
                                p.setQuantity(p.getQuantity() - Integer.parseInt(purchaseQuantity));
                                // TESTING

                                System.out.println("Successfully added "
                                        + purchaseQuantity + " to your shopping cart." +
                                        " Returning to search page...");
                                searchMenu();
                            } else {
                                searchMenu();
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println(separator);
            System.out.println("No fruits available");
            System.out.println("Please enter: ");
            System.out.println("[1] Return to Search Menu");
            System.out.println("[Anything Else] Return to Customer Menu");

            String noProdAction = sc.nextLine();
            if (noProdAction.equalsIgnoreCase("1")) {
                searchMenu();
            } else {
                customerMainMenu();
            }
        }
    }

    /**
     * Accesses cart menu to see shopping cart for users
     */
    public static void cartMenu() {
        int counter = 0;
        double allTotal = 0;
        System.out.println(separator);
        System.out.println("Your shopping cart:");
        if (shoppingCart.getHeldPurchases().size() == 0) {
            System.out.println("- Empty");
        } else {
            for (Sale s : shoppingCart.getHeldPurchases()) {
                counter++;
                allTotal += s.getTotalCost();
                String totalPrice = String.format("%.2f", s.getTotalCost());
                System.out.println("- #" + counter + " " + s.getProduct().getName()
                        + " (" + s.getQuantity() + " for $"
                        + s.getProduct().getPrice() + " each | Items Total $"
                        + totalPrice + ")");
            }
        }

        System.out.println(separator);
        System.out.println("Please enter: ");
        System.out.println("[CH] Checkout & Purchase");
        System.out.println("[Corresponding #] View Fruit Info");
        System.out.println("[Anything Else] Return to Customer Menu");

        String cartPick = sc.nextLine();
        if (cartPick.matches("-?\\d+(\\.\\d+)?")) {
            while (Integer.parseInt(cartPick) - 1 > shoppingCart.getHeldPurchases().size()) {
                System.out.println("Entered # is not a valid item! Please enter a valid item #: ");
                cartPick = sc.nextLine();
                if (!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                    cartPick = "QUIT_MENU_PLEASE";
                    break;
                }
            }
            if (cartPick.equals("QUIT_MENU_PLEASE")) {
                customerMainMenu();
            } else {
                Sale s = shoppingCart.getPurchase(Integer.parseInt(cartPick) - 1);
                Product p = s.getProduct();
                if (p != null) {
                    showProductInfo(p);
                    System.out.println(separator);
                    System.out.println("Please enter: ");
                    System.out.println("[1] Remove from Shopping Cart");
                    System.out.println("[2] Change Purchase Quantity");
                    System.out.println("[Anything Else] Return to All Fruits Page");

                    String cartAction = sc.nextLine();
                    if (cartAction.equalsIgnoreCase("1")) {

                        // TESTING
                        p.setQuantity(p.getQuantity() +
                                FileManager.getCustomerShoppingCartQuantity((Customer) user, p));
                        FileManager.updateCustomerShoppingCart((Customer) user, p, 0);
                        // TESTING

                        shoppingCart.removePurchase(p);
                        System.out.println("Successfully removed " + p.getName()
                                + " from your shopping cart." +
                                " Returning to shopping cart page...");
                        cartMenu();
                    } else if (cartAction.equalsIgnoreCase("2")) {
                        System.out.println(separator);
                        System.out.println("Please enter a new quantity: ");
                        String changeQuantity = sc.nextLine();
                        while (!changeQuantity.matches("-?\\d+(\\.\\d+)?")) {
                            System.out.println("Entered quantity is not an integer! " +
                                    "Please enter a valid quantity: ");
                            changeQuantity = sc.nextLine();
                        }
                        while (p.getQuantity() + (FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                - Integer.parseInt(changeQuantity)) < 0) {
                            System.out.println("There is only " + p.getQuantity()
                                    + " of this fruit available to purchase.");
                            System.out.println("You currently have " +
                                    FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                    + " held in your shopping cart.");
                            System.out.println("Please try a smaller quantity: ");
                            changeQuantity = sc.nextLine();
                        }

                        // TESTING
                        p.setQuantity(p.getQuantity() +
                                (FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                        - Integer.parseInt(changeQuantity)));
                        FileManager.updateCustomerShoppingCart((Customer) user,
                                p, Integer.parseInt(changeQuantity));
                        // TESTING

                        shoppingCart.getPurchase(
                                Integer.parseInt(cartPick) - 1).setQuantity(
                                Integer.parseInt(changeQuantity));
                        shoppingCart.recalculateTotalHeldProducts();
                        System.out.println("Successfully changed purchase quantity to "
                                + changeQuantity + "." +
                                " Returning to shopping cart page...");
                        cartMenu();
                    } else {
                        cartMenu();
                    }
                } else {
                    while (p == null) {
                        System.out.println("Entered # is not a valid fruit! " +
                                "Please enter a valid fruit #: ");
                        cartPick = sc.nextLine();
                        if (!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                            continue;
                        }
                        p = shoppingCart.getHeldPurchases().get(
                                Integer.parseInt(cartPick) - 1).getProduct();
                    }
                }
            }
        } else if (cartPick.equalsIgnoreCase("ch")) {
            String allTotalPrice = String.format("%.2f", allTotal);

            System.out.println(separator);
            System.out.println("You are purchasing "
                    + shoppingCart.getTotalheldProducts()
                    + " fruits for $" + allTotalPrice);
            System.out.println(separator);
            System.out.println("Would you like to complete the purchase?" +
                    "\nPlease enter [Y] or [Yes] to purchase, or [Anything Else] to return: ");

            String purchaseAction = sc.nextLine();
            if (purchaseAction.equalsIgnoreCase("y")
                    || purchaseAction.equalsIgnoreCase("yes")) {
                System.out.println(separator);
                for (Sale heldPurchase : shoppingCart.getHeldPurchases()) {
                    Product p = heldPurchase.getProduct();

                    int quantitySold = FileManager.getCustomerShoppingCartQuantity((Customer) user, p);
                    ((Customer) user).addSale(heldPurchase);
                    FileManager.addCustomerData((Customer) user,
                            heldPurchase.getProduct(), quantitySold); //add to history
                    System.out.println("Purchased " + quantitySold
                            + " " + p.getName() + "!"); //announce purchase
                    FileManager.updateCustomerShoppingCart((Customer)
                            user, p, 0); //remove from cart

                    Seller tempSeller = null;
                    Store tempStore = null;
                    seller_find_loop:
                    for (Seller seller : mp.getSellers()) {
                        for (Store store : seller.getStores()) {
                            for (Product prod : store.getProducts()) {
                                if (prod.getName().equals(p.getName())
                                        && prod.getDescription().equals(p.getDescription())
                                        && prod.getPrice() == p.getPrice()) {
                                    store.addSale(heldPurchase);
                                    tempSeller = seller;
                                    tempStore = store;
                                    break seller_find_loop;
                                }
                            }
                        }
                    }
                    FileManager.updateSellerData(tempSeller, tempStore,
                            p, p.getQuantity(), quantitySold);
                }
                shoppingCart.setHeldPurchases(new ArrayList<>());
                System.out.println("Returning to customer menu page...");
                customerMainMenu();
            } else {
                cartMenu();
            }
        } else {
            customerMainMenu();
        }
    }

    /**
     * Accesses purchase history menu
     */
    public static void historyMenu() {
        ArrayList<ArrayList<Object>> history =
                FileManager.getCustomerData((Customer) user);

        System.out.println(separator);
        System.out.println("Your purchase history:");
        if (history == null || history.size() == 0) {
            System.out.println("- No purchases found");
        } else {
            for (ArrayList<Object> arr : history) {
                int quant = Integer.parseInt((String) arr.get(0));
                Product prod = (Product) arr.get(1);
                System.out.println("- " + prod.getName());
                System.out.println("--- Price Each: " + prod.getPrice());
                System.out.println("--- Quantity Purchased: " + quant);
                System.out.println("--- Total Price: " +
                        String.format("%.2f", (prod.getPrice() * quant)));
            }
            System.out.println("You have purchased " +
                    ((Customer) user).getTotalPurchasedProducts() + "fruits in total!");
        }
        System.out.println(separator);
        System.out.println("Please enter:");
        System.out.println("[EX] Export Purchase History CSV");
        System.out.println("[Anything Else] Return to Customer Menu: ");
        String action = sc.nextLine();

        if (action.equalsIgnoreCase("ex")) {
            FileManager.exportCustomerHistory((Customer) user);
            System.out.println("Returning to customer menu...");
        }
        customerMainMenu();
    }

    public static void dashboardMenu(int sortMode) {
        if (user instanceof Customer) {
            System.out.println(separator);
            System.out.println("All Available Stores:");
            ArrayList<Store> stores = new ArrayList<>();
            switch (sortMode) {
                case 1:
                    stores = mp.getSalesSortedStores(true);
                    break;
                case 2:
                    stores = mp.getSalesSortedStores(false);
                    break;
                case 3:
                    stores = mp.getUserSalesSortedStores(user, true);
                    break;
                case 4:
                    stores = mp.getUserSalesSortedStores(user, false);
                    break;
                default:
                    stores = mp.getStores();
            }

            for (Store store : stores) {
                int soldToUser = store.getQuantityOfProductsBoughtByCustomer((Customer) user);
                System.out.println("- " + store.getName());
                System.out.println("--- Total Products Sold: " +
                        store.getTotalSoldProducts());
                System.out.println("--- Total Products Sold to You: " + soldToUser);
            }
            System.out.println(separator);
            System.out.println("Please enter:");
            System.out.println("[1] Sort Stores by Total Fruits Sold (High to Low)");
            System.out.println("[2] Sort Stores by Total Fruits Sold (Low to High)");
            System.out.println("[3] Sort Stores by Total Fruits Sold to You (High to Low)");
            System.out.println("[4] Sort Stores by Total Fruits Sold to You (Low to High)");
            System.out.println("[Anything Else] Return to Customer Menu");

            String productPick = sc.nextLine();
            if (productPick.equalsIgnoreCase("1")) {
                dashboardMenu(1);
            } else if (productPick.equalsIgnoreCase("2")) {
                dashboardMenu(2);
            } else if (productPick.equalsIgnoreCase("3")) {
                dashboardMenu(3);
            } else if (productPick.equalsIgnoreCase("4")) {
                dashboardMenu(4);
            } else {
                customerMainMenu();
            }
        } else if (user instanceof Seller) {
            System.out.println(separator);
            System.out.println("Please enter:");
            System.out.println("[1] View Sales Statistics");
            System.out.println("[2] View Customer Statistics");
            System.out.println("[Anything Else] Return to Seller Menu");

            String statChoice = sc.nextLine();
            if (statChoice.equalsIgnoreCase("1")) {
                productSalesStatsDashboard(0);
            } else if (statChoice.equalsIgnoreCase("2")) {
                customerSalesStatsDashboard(0);
            } else {
                sellerMainMenu();
            }
        }
    }

    /**
     * Displays customer sales statistics
     *
     * @param sortMode
     */
    public static void customerSalesStatsDashboard(int sortMode) {
        System.out.println(separator);
        System.out.println("Your Stores:");
        for (Store store : ((Seller) user).getStores()) {
            System.out.println("- " + store.getName());
            ArrayList<Customer> customers = new ArrayList<>();
            switch (sortMode) {
                case 1:
                    customers = mp.getStoreSalesSortedCustomers(store, true);
                    break;
                case 2:
                    customers = mp.getStoreSalesSortedCustomers(store, false);
                    break;
                default:
                    customers = store.getAllCustomers();
            }

            if (customers.size() > 0) {
                for (Customer customer : customers) {
                    System.out.println(customer.getUsername() + " ("
                            + store.getQuantityOfProductsBoughtByCustomer((Customer) user)
                            + " Fruits Purchased)");
                }
            } else {
                System.out.println("--- No customers found");
            }
        }

        System.out.println(separator);
        System.out.println("Please enter:");
        System.out.println("[1] Sort Customers by Purchases (High to Low)");
        System.out.println("[2] Sort Customers by Purchases (Low to High)");
        System.out.println("[Anything Else] Return to Statistics Dashboard Menu");

        String productPick = sc.nextLine();
        if (productPick.equalsIgnoreCase("1")) {
            customerSalesStatsDashboard(1);
        } else if (productPick.equalsIgnoreCase("2")) {
            customerSalesStatsDashboard(2);
        } else { // TODO: needs testing
            dashboardMenu(0);
        }
    }

    /**
     * Access product sales statistics
     *
     * @param sortMode
     */
    public static void productSalesStatsDashboard(int sortMode) {
        System.out.println(separator);
        System.out.println("Your Stores:");
        for (Store store : ((Seller) user).getStores()) {
            System.out.println("- " + store.getName());
            ArrayList<Product> products = new ArrayList<>();
            switch (sortMode) {
                case 1:
                    products = mp.getStoreSalesSortedProducts(store, true);
                    break;
                case 2:
                    products = mp.getStoreSalesSortedProducts(store, false);
                    break;
                default:
                    products = store.getProducts();
            }

            if (products.size() > 0) {
                for (Product product : products) {
                    System.out.println("--- " + product.getName() + " ("
                            + store.getNumberOfProductsSold(product) + " Sold)");
                }
            } else {
                System.out.println("--- No products found");
            }
        }

        System.out.println(separator);
        System.out.println("Please enter:");
        System.out.println("[1] Sort Products by Sales (High to Low)");
        System.out.println("[2] Sort Products by Sales (Low to High)");
        System.out.println("[Anything Else] Return to Statistics Dashboard Menu");

        String productPick = sc.nextLine();
        if (productPick.equalsIgnoreCase("1")) {
            productSalesStatsDashboard(1);
        } else if (productPick.equalsIgnoreCase("2")) {
            productSalesStatsDashboard(2);
        } else {
            dashboardMenu(0);
        }
    }


    /**
     * Deletes an account
     *
     * @param user
     */
    public static void deleteAccount(User user) {
        System.out.println(separator);
        System.out.println("WARNING: Are you sure you want to delete your account?");
        System.out.println("WARNING: All user data will be lost and will NOT be recoverable!");
        System.out.println("WARNING: THIS DECISION IS FINAL!");
        System.out.println(separator);
        System.out.println("Please enter: ");
        System.out.println("[DELETE] Delete Account (All Caps Required)");
        System.out.println("[Anything Else] Cancel & Return to Main Menu");

        String action = sc.nextLine();
        if (action.equals("DELETE")) {
            FileManager.removeAccount(user);
        } else {
            if (user instanceof Customer) {
                customerMainMenu();
            } else if (user instanceof Seller) {
                sellerMainMenu();
            }
        }
    }

    /**
     * Accesses seller main menu
     */
    public static void sellerMainMenu() {
        System.out.println(separator);
        System.out.println("What would you like to do today?");
        System.out.println("Please enter: ");
        System.out.println("[1] View Your Stores");
        System.out.println("[2] View Your Carted Fruits");
        System.out.println("[3] View Sales History");
        System.out.println("[4] View Statistics Dashboard");
        System.out.println("[D] Delete Account");
        System.out.println("[Q] Logout & Quit");

        String action = sc.nextLine();
        if (action.equalsIgnoreCase("1")) {
            storesMenu();
        } else if (action.equalsIgnoreCase("2")) {
            cartedProductsMenu();
        } else if (action.equalsIgnoreCase("3")) {
            salesMenu();
        } else if (action.equalsIgnoreCase("4")) {
            dashboardMenu(0);
        } else if (action.equalsIgnoreCase("d")) {
            deleteAccount(user);
        } else if (action.equalsIgnoreCase("q")) {
            printFarewell();
        } else {
            if (tryAgain("Invalid menu selection!")) {
                customerMainMenu();
            } else {
                printFarewell();
            }
        }
    }

    /**
     * Accesses all stores page
     */
    public static void storesMenu() {
        int counter = 0;
        System.out.println(separator);
        System.out.println("Your stores:");

        if (((Seller) user).getStores().isEmpty()) {
            System.out.println("- No stores found");
        } else {
            for (Store store : ((Seller) user).getStores()) {
                System.out.println("- #" + (counter + 1) + " " + store.getName());
                counter++;
            }
        }

        System.out.println(separator);
        System.out.println("Please enter: ");
        System.out.println("[Corresponding #] View Store Info");
        System.out.println("[AD] Add New Store");
        System.out.println("[Anything Else] Return to Seller Menu");

        String storeAction = sc.nextLine();
        if (storeAction.matches("-?\\d+(\\.\\d+)?")) {
            if (!((Seller) user).getStores().isEmpty()) {
                try {
                    int counterAgain = 0;
                    Store store = ((Seller) user).getStores().get(
                            Integer.parseInt(storeAction) - 1);
                    System.out.println(separator);
                    System.out.println("Store: " + store.getName());
                    System.out.println("Fruits: ");
                    if (store.getProducts().isEmpty()) {
                        System.out.println("- No fruits found");
                    } else {
                        for (Product prod : store.getProducts()) {
                            System.out.println("- #" + (counterAgain + 1) + " "
                                    + prod.getName());
                            counterAgain++;
                        }
                    }

                    System.out.println(separator);
                    System.out.println("Please enter: ");
                    System.out.println("[Corresponding #] View Fruit Info");
                    System.out.println("[AD] Add New Fruit");
                    System.out.println("[IM] Import Items CSV");
                    System.out.println("[EX] Export Items CSV");
                    System.out.println("[Anything Else] Return to Seller Menu");

                    String productPick = sc.nextLine();
                    if (productPick.matches("-?\\d+(\\.\\d+)?")) {
                        if (!store.getProducts().isEmpty()) {
                            Product prod = store.getProducts().get(
                                    Integer.parseInt(productPick) - 1);
                            showProductInfo(prod);

                            System.out.println(separator);
                            System.out.println("Please enter: ");
                            System.out.println("[QU] Change Quantity Available");
                            System.out.println("[DE] Change Description");
                            System.out.println("[PR] Change Price");
                            System.out.println("[RM] Remove Fruit");
                            System.out.println("[Anything Else] Return to Seller Menu");

                            String productAction = sc.nextLine();
                            if (productAction.equalsIgnoreCase("qu")) {
                                changeProductQuantity(store, prod);
                            } else if (productAction.equalsIgnoreCase("de")) {
                                changeProductDescription(store, prod);
                            } else if (productAction.equalsIgnoreCase("pr")) {
                                changeProductPrice(store, prod);
                            } else if (productAction.equalsIgnoreCase("rm")) {
                                removeProduct(store, prod);
                            } else {
                                sellerMainMenu();
                            }
                        } else {
                            sellerMainMenu();
                        }
                    } else if (productPick.equalsIgnoreCase("ad")) {
                        addNewProduct(store);
                    } else if (productPick.equalsIgnoreCase("im")) {
                        FileManager.importSellerCSV((Seller) user, store);

                        mp = new MarketPlace();
                        FileManager.loadAllStores(mp, true);
                        for (Seller seller : mp.getSellers()) {
                            if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                                    && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                                user = seller;
                            }
                        }

                        System.out.println("Returning to all stores menu...");
                        storesMenu();
                    } else if (productPick.equalsIgnoreCase("ex")) {
                        FileManager.exportSellerCSV((Seller) user, store);
                        System.out.println("Returning to all stores menu...");
                        storesMenu();
                    } else {
                        sellerMainMenu();
                    }
                } catch (Exception e) {
                    sellerMainMenu();
                }
            } else {
                sellerMainMenu();
            }
        } else if (storeAction.equalsIgnoreCase("ad")) {
            addNewStore();
        } else {
            sellerMainMenu();
        }
    }

    /**
     * Adds a new store
     */
    public static void addNewStore() {
        System.out.println(separator);
        System.out.println("Enter new store name: ");
        String name = sc.nextLine();
        if (name.contains(",") || name.contains(";")) {
            if (!tryAgain("Store name cannot contain ',' or ';'!")) {
                sellerMainMenu();
            } else {
                addNewStore();
            }
        } else {
            for (Store s : mp.getStores()) {
                if (s.getName().equalsIgnoreCase(name)) {
                    if (!tryAgain("Store name is already used!")) {
                        sellerMainMenu();
                    } else {
                        addNewStore();
                    }
                }
            }
            Store store = new Store(name, "");
            ((Seller) user).addStore(store);
            FileManager.createStoreFile((Seller) user, store);
            System.out.println("Successfully added new store! " +
                    "Returning to all stores menu...");
            storesMenu();
        }
    }

    /**
     * Adds a new product
     *
     * @param store
     */
    public static void addNewProduct(Store store) {
        System.out.println(separator);
        System.out.println("Enter new fruit name: ");
        String name = sc.nextLine();
        if (name.contains(",") || name.contains(";")) {
            if (!tryAgain("Fruit name cannot contain ',' or ';'!")) {
                sellerMainMenu();
            } else {
                addNewProduct(store);
            }
        } else {
            for (Product prod : mp.getProducts()) {
                if (prod.getName().equalsIgnoreCase(name)) {
                    if (!tryAgain("Fruit is already used!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(store);
                    }
                }
            }
            System.out.println("Enter fruit description: ");
            String desc = sc.nextLine();
            if (desc.contains(",") || desc.contains(";")) {
                if (!tryAgain("Fruit description cannot contain ',' or ';' ")) {
                    sellerMainMenu();
                } else {
                    addNewStore();
                }
            } else {
                System.out.println("Enter fruit price: ");
                double price = 0;
                String temp = sc.nextLine();
                try {
                    price = Double.parseDouble(temp);
                } catch (Exception e) {
                    if (!tryAgain("Price must be a double!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(store);
                    }
                }
                System.out.println("Enter fruit available quantity: ");
                int quant = 0;
                temp = sc.nextLine();
                try {
                    quant = Integer.parseInt(temp);
                } catch (Exception e) {
                    if (!tryAgain("Quantity must be an integer!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(store);
                    }
                }

                Product p = new Product(name, desc, price, quant);
                store.addProduct(p);
                System.out.println("Successfully added new fruit! " +
                        "Returning to all stores menu...");
                FileManager.addSellerData((Seller) user, store, p);
                storesMenu();
            }
        }
    }

    /**
     * Changes a product quantity to new one
     *
     * @param store
     * @param product
     */
    public static void changeProductQuantity(Store store, Product product) {
        System.out.println(separator);
        System.out.println("Please enter a new fruit quantity " +
                "(integer, at least 1): ");

        String quant = sc.nextLine();
        while (!quant.matches("-?\\d+(\\.\\d+)?")
                || (Integer.parseInt(quant) <= 0)) {
            if (!tryAgain("Invalid value! Quantity must be an integer and at least 1!")) {
                storesMenu();
                break;
            }
            System.out.println("Please enter a new fruit quantity " +
                    "(integer, at least 1): ");
            quant = sc.nextLine();
        }

        FileManager.updateSellerDataQuantity((Seller) user,
                store, product, Integer.parseInt(quant));
        product.setQuantity(Integer.parseInt(quant));
        System.out.println("Successfully updated " + product.getName()
                + "'s quantity available to: " + quant);
        System.out.println("Returning to all stores menu...");
        storesMenu();
    }

    /**
     * Changes product description to new one
     *
     * @param store
     * @param product
     */
    public static void changeProductDescription(Store store, Product product) {
        System.out.println(separator);
        System.out.println("Please enter a new fruit description: ");

        String desc = sc.nextLine();
        while (desc.contains(",") || desc.contains(";")) {
            if (!tryAgain("Description cannot contain ',' or ';'!")) ;
            storesMenu();
            break;
        }

        FileManager.updateSellerDataDescription((Seller) user, store, product, desc);
        product.setDescription(desc);
        System.out.println("Successfully updated " + product.getName()
                + "'s description to: " + desc);
        System.out.println("Returning to all stores menu...");
        storesMenu();
    }

    /**
     * Changes product price to new one
     *
     * @param store
     * @param product
     */
    public static void changeProductPrice(Store store, Product product) {
        System.out.println(separator);
        System.out.println("Please enter a new price (double, at least 0.00): ");

        String price = sc.nextLine();
        String formatted = String.format("%.2f", Double.parseDouble(price));
        while (!formatted.matches("-?\\d+(\\.\\d+)?")
                || (Double.parseDouble(formatted) < 0.00)) {
            if (!tryAgain("Invalid value! Price must be a double and at least 0.00!")) {
                storesMenu();
                break;
            }
            System.out.println("Please enter a new price (double, at least 0.00): ");
            price = sc.nextLine();
            formatted = String.format("%.2f", Double.parseDouble(price));
        }

        FileManager.updateSellerDataPrice((Seller) user, store,
                product, Double.parseDouble(formatted));
        product.setPrice(Double.parseDouble(formatted));
        System.out.println("Successfully updated " + product.getName()
                + "'s description to: " + formatted);
        System.out.println("Returning to all stores menu...");
        storesMenu();
    }

    /**
     * Removes a product
     *
     * @param store
     * @param product
     */
    public static void removeProduct(Store store, Product product) {
        System.out.println(separator);
        System.out.println("WARNING: Are you sure you want to remove this product?");
        System.out.println("WARNING: All seller data " +
                "corresponding with this product will be deleted.");
        System.out.println("WARNING: THIS DECISION IS FINAL");
        System.out.println(separator);
        System.out.println("Please enter: ");
        System.out.println("[DELETE] Delete Product (All Caps Required)");
        System.out.println("[Anything Else] Return to Seller Menu");
        System.out.println(separator);

        String action = sc.nextLine();
        if (action.equals("DELETE")) {
            FileManager.removeSellerDataProduct((Seller) user, store, product);

            mp = new MarketPlace();
            FileManager.loadAllStores(mp, true);
            for (Seller seller : mp.getSellers()) {
                if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                        && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                    user = seller;
                }
            }

            System.out.println("Successfully deleted " + product.getName());
            System.out.println("Returning to seller menu...");
            sellerMainMenu();
        } else {
            sellerMainMenu();
        }
    }

    /**
     * Displays all shopping carts
     */
    public static void cartedProductsMenu() {
        System.out.println(separator);
        System.out.println("All carted items:");
        ArrayList<ArrayList<String>> data = FileManager.getAllCarts();
        for (int j = 0; j < data.size(); j++) {
            ArrayList<String> arr = data.get(j);
            System.out.println(separator);
            System.out.println("- Customer: " + arr.get(0));
            for (int i = 1; i < arr.size(); i++) {
                String s = arr.get(i);
                String temp = s.substring(s.indexOf(';') + 1);
                String name = temp.substring(0, temp.indexOf(';'));
                String quant = s.substring(0, s.indexOf(';'));

                String temp2 = temp.substring(temp.indexOf(';') + 1);
                String desc = temp2.substring(0, temp2.indexOf(';'));

                String temp3 = temp2.substring(temp2.indexOf(';') + 1);
                String price = temp3.substring(0, temp3.indexOf(';'));

                String quantAvail = temp3.substring(temp3.indexOf(';') + 1);
                System.out.println("--- Item: " + name + " (#Held: " + quant + ")");
                System.out.println("----- Description: " + desc);
                System.out.println("----- Price: " + price);
                System.out.println("----- Quantity Available: " + quantAvail);
            }
        }

        System.out.println(separator);
        System.out.println("Type [Anything] to return to Seller Menu");
        String a = sc.nextLine();
        sellerMainMenu();
    }

    /**
     * Displays all product sales
     */
    public static void salesMenu() {
        System.out.println(separator);
        System.out.println("Your sales:");

        ArrayList<ArrayList<Object>> temp =
                FileManager.getSellerAllData((Seller) user);
        for (int i = 0; i < temp.size(); i++) {
            ArrayList<Object> arr = temp.get(i);
            System.out.println(separator);
            System.out.println("- Store: " + arr.get(0));
            for (int j = 2; j < arr.size(); j += 2) {
                String q = (String) arr.get(j - 1);
                Product p = (Product) arr.get(j);
                System.out.println("--- Item: " + p.getName());
                System.out.println("----- Price: " + p.getPrice());
                System.out.println("----- Quantity Sold: " + q);

                String formatted = String.format("%.2f",
                        Double.parseDouble(String.valueOf(Integer.parseInt(q) * p.getPrice())));
                System.out.println("----- Profit Made: " + formatted);
            }
        }

        System.out.println(separator);
        System.out.println("Type [Anything] to return to Seller Menu");
        String a = sc.nextLine();
        sellerMainMenu();
    }


    /**
     * Prints the farewell message.
     */
    public static void printFarewell() {
        System.out.println("Thank you for visiting The MarketPlace!");
        System.out.println("Come again another day.");
    }
}