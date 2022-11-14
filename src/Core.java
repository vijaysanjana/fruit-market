import java.lang.reflect.Array;
import java.util.*;

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
    private static MarketPlace mp = new MarketPlace();

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
            System.out.println("Please enter: " + "\n[1] Login" + "\n[2] Signup");
            loginSignup = sc.nextLine();

            login_signup:
            while (true) {
                if (loginSignup.equals("1")) { // THIS IS THE LOGIN PART
                    login_loop:
                    while(true) {
                        System.out.println(separator);
                        System.out.println("Please enter your email: ");
                        email = sc.nextLine();
                        System.out.println("Please enter your password: ");
                        password = sc.nextLine();

                        user = AccountManager.login(email, password); // TODO: needs testing
                        if (user == null) { // TODO: needs testing (invalid logins)
                            if (!tryAgain("Email and password combinations are invalid!")) {
                                printFarewell();
                                break system_loop;
                            }
                        } else {
                            break;
                        }
                    }
                    if (user instanceof Customer) {
                        System.out.println(separator);
                        System.out.println("Welcome customer: " + user.getUsername());
                        shoppingCart = ((Customer) user).getShoppingCart();

                        // TESTING
                        FileManager.loadAllStores(mp, true);
                        FileManager.loadAllCarts((Customer) user, shoppingCart, mp);
                        // TESTING

                        customerMainMenu();
                        break system_loop;
                    } else if (user instanceof Seller) {
                        System.out.println(separator);
                        System.out.println("Welcome seller: " + user.getUsername());
                        sellerMainMenu();
                        break system_loop;
                    } else {
                        throw new RuntimeException(
                                new AccountException("LOGGED IN USER IS NEITHER CUSTOMER NOR SELLER!"));
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
                        System.out.println("Please enter: " + "\n[1] Customer" + "\n[2] Seller");
                        customerSeller = sc.nextLine();

                        if (customerSeller.equals("1")) { // TODO: needs testing
                            if(AccountManager.signup(username, email, password, "customer") != null) {
                                System.out.println("Successfully signed up! Logging you in...");
                                user = AccountManager.login(email, password);
                                if (user instanceof Customer) {
                                    System.out.println(separator);
                                    System.out.println("Welcome customer: " + user.getUsername());
                                    shoppingCart = ((Customer) user).getShoppingCart();
                                    customerMainMenu();
                                    break system_loop;
                                } else {
                                    throw new RuntimeException(
                                            new AccountException("REGISTERED CUSTOMER IS NOT A CUSTOMER!"));
                                }
                            } else {
                                if(!tryAgain("User already exists! Please login or use another email and username.")) {
                                    printFarewell();
                                    break system_loop;
                                } else {
                                    main(new String[0]);
                                    break system_loop;
                                }
                            }
                        } else if (customerSeller.equals("2")) { // TODO: needs testing
                            if(AccountManager.signup(username, email, password, "seller") != null) {
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
                                if(!tryAgain("User already exists! Please login or use another email and username.")) {
                                    printFarewell();
                                    break system_loop;
                                } else {
                                    main(new String[0]);
                                    break system_loop;
                                }
                            }
                        } else {
                            if (!tryAgain("Invalid customer/seller selection!")) { // TODO: needs testing
                                printFarewell();
                                break system_loop;
                            }
                        }
                    }
                } else {
                    if (!tryAgain("Invalid login/signup selection!")) { // TODO: needs testing
                        printFarewell();
                        break system_loop;
                    }
                }
            }
        }
    }

    /**
     * Asks user to try again if their choice is invalid and returns true/false based on their choice.
     * @param message
     * @return boolean
     */
    public static boolean tryAgain(String message) {
        System.out.println(separator);
        System.out.println(message);
        System.out.println("Please enter [Y] or [Yes] to try again, or [Anything Else] to quit: ");
        String yn = sc.nextLine();
        return (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes"));
    }

    /**
     * Prints out the customer main menu for users to view.
     * Allows for navigation of the menu to see marketplace; view, search, and purchase products; and view history.
     */
    public static void customerMainMenu() {
        System.out.println(separator);
        System.out.println("What would you like to do today?");
        System.out.println("Please enter: ");
        System.out.println("[1] Open Marketplace");
        System.out.println("[2] Search for Product");
        System.out.println("[3] View Shopping Cart (" + shoppingCart.getTotalheldProducts() + " Products)");
        System.out.println("[4] View Purchase History");
        System.out.println("[5] View Statistics Dashboard");
        System.out.println("[Q] Logout & Quit");

        String action = sc.nextLine();
        if (action.equalsIgnoreCase("1")) { // TODO: needs testing
            marketplaceMenu();
        } else if (action.equalsIgnoreCase("2")) { // TODO: needs testing
            searchMenu();
        } else if(action.equalsIgnoreCase("3")) { // TODO: needs testing
            cartMenu();
        } else if (action.equalsIgnoreCase("4")) { // TODO: needs testing
            historyMenu();
        } else if (action.equalsIgnoreCase("q")) { // TODO: needs testing
            printFarewell();
        } else {
            if(tryAgain("Invalid menu selection!")) { // TODO: needs testing
                customerMainMenu();
            }
        }
    }

    public static void marketplaceMenu() {
        ArrayList<Store> stores = mp.getStores();
        ArrayList<Product> products = new ArrayList<Product>();

        System.out.println(separator);
        System.out.println("All available stores:");

        int counter = 0;
        for (Store s : stores) {
            ArrayList<Product> tempProds = s.getProducts();
            System.out.println("- " + s.getName());
            if (stores.isEmpty()) {
                System.out.println("--- No products");
            } else {
                for (Product p : tempProds) {
                    products.add(p);
                    System.out.println("--- #" + (counter + 1) + " " +
                            p.getName() + " (Price $" + p.getPrice() + " | Quantity Available " + p.getQuantity() + ")");
                    counter++;
                }
            }
        }

        if(!products.isEmpty()) {
            System.out.println(separator);
            System.out.println("Please enter: ");
            System.out.println("[PH] Sort Products by Price (High to Low)");
            System.out.println("[PL] Sort Products by Price (Low to High)");
            System.out.println("[QH] Sort Products by Quantity Available (High to Low)");
            System.out.println("[QL] Sort Products by Quantity Available (High to Low)");
            System.out.println("[Correspond #] View Product Info");
            System.out.println("[Anything Else] Return to Customer Menu");

            String productPick = sc.nextLine();
            if (productPick.matches("-?\\d+(\\.\\d+)?")) { // TODO: needs testing
                while(Integer.parseInt(productPick)-1 > products.size()) {
                    System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                    productPick = sc.nextLine();
                    if(!productPick.matches("-?\\d+(\\.\\d+)?")) {
                        productPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if(productPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    Product p = products.get(Integer.parseInt(productPick) - 1);
                    if(p.getQuantity() <= 0) {
                        while(p.getQuantity() <= 0) {
                            System.out.println("There is no more of this product available to purchase! Please enter another option: ");
                            productPick = sc.nextLine();
                            if(productPick.matches("-?\\d+(\\.\\d+)?")) {
                                p = products.get(Integer.parseInt(productPick) - 1);
                                if(p.getQuantity() >= 1) {
                                    if (p != null) { // TODO: needs testing
                                        showProductInfo(p);
                                        System.out.println(separator);
                                        System.out.println("Please enter: ");
                                        System.out.println("[1] Add to Shopping Cart");
                                        System.out.println("[Anything Else] Return to All Products Page");

                                        String productAction = sc.nextLine();
                                        if (productAction.equalsIgnoreCase("1")) {
                                            System.out.println(separator);
                                            System.out.println("Please enter a purchase quantity: ");
                                            String purchaseQuantity = sc.nextLine();
                                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")) {
                                                System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }
                                            while(p.getQuantity()-Integer.parseInt(purchaseQuantity) < 0) {
                                                System.out.println("There is only " + p.getQuantity() + " of this product available to purchase.");
                                                System.out.println("Please try a smaller quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }

                                            shoppingCart.addPurchase(new Sale((Customer) user, p, Integer.parseInt(purchaseQuantity)));

                                            // TESTING
                                            FileManager.addCustomerShopppingCart((Customer) user, p, Integer.parseInt(purchaseQuantity));
                                            p.setQuantity(p.getQuantity()-Integer.parseInt(purchaseQuantity));
                                            // TESTING

                                            System.out.println("Successfully added " + purchaseQuantity + " to your shopping cart." +
                                                    " Returning to available products page...");
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
                        if (p != null) { // TODO: needs testing
                            showProductInfo(p);
                            System.out.println(separator);
                            System.out.println("Please enter: ");
                            System.out.println("[1] Add to Shopping Cart");
                            System.out.println("[Anything Else] Return to All Products Page");

                            String productAction = sc.nextLine();
                            if (productAction.equalsIgnoreCase("1")) {
                                System.out.println(separator);
                                System.out.println("Please enter a purchase quantity: ");
                                String purchaseQuantity = sc.nextLine();
                                while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")) {
                                    System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }
                                while(p.getQuantity()-Integer.parseInt(purchaseQuantity) < 0) {
                                    System.out.println("There is only " + p.getQuantity() + " of this product available to purchase.");
                                    System.out.println("Please try a smaller quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }

                                shoppingCart.addPurchase(new Sale((Customer) user, p, Integer.parseInt(purchaseQuantity)));

                                // TESTING
                                FileManager.addCustomerShopppingCart((Customer) user, p, Integer.parseInt(purchaseQuantity));
                                p.setQuantity(p.getQuantity()-Integer.parseInt(purchaseQuantity));
                                // TESTING

                                System.out.println("Successfully added " + purchaseQuantity + " to your shopping cart." +
                                        " Returning to available products page...");
                                marketplaceMenu();
                            } else {
                                marketplaceMenu();
                            }
                        }
                    }
                }
            } else if (productPick.equalsIgnoreCase("ph")) { // TODO: needs testing
                for (Store store : mp.getStores()) {
                    store.setProducts(store.getPriceSortedProducts(true));
                }
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("pl")) { // TODO: needs testing
                for (Store store : mp.getStores()) {
                    store.setProducts(store.getPriceSortedProducts(false));
                }
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("qh")) { // TODO: needs testing
                for (Store store : mp.getStores()) {
                    store.setProducts(store.getQuantitySortedProducts(true));
                }
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("ql")) { // TODO: needs testing
                for (Store store : mp.getStores()) {
                    store.setProducts(store.getQuantitySortedProducts(false));
                }
                marketplaceMenu();
            } else { // TODO: needs testing
                customerMainMenu();
            }
        } else {
            System.out.println(separator);
            System.out.println("No products available.");
            System.out.println("Please enter: ");
            System.out.println("[1] Return to Customer Menu");
            System.out.println("[Anything Else] Logout & Quit");

            String noProdAction = sc.nextLine();
            if(noProdAction.equalsIgnoreCase("1")) {
                customerMainMenu();
            } else {
                printFarewell();
            }
        }
    }

    private static void showProductInfo(Product product) {
        System.out.println(separator);
        System.out.println("Product: " + product.getName());
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
        System.out.println("[1] Product Name");
        System.out.println("[2] Product Description");
        System.out.println("[3] Store Name");
        System.out.println("[Anything Else] Return to Customer Menu");

        String searchAction = sc.nextLine();
        if(searchAction.equalsIgnoreCase("1")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Product> result = mp.searchProducts("name", searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Product Name):");

            if(result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for(Product p : result) {
                    productsFound.add(p);
                    System.out.println("- #" + (counter + 1) + " " +
                            p.getName() + " (Price: " + p.getPrice()
                            + " | Quantity Available " + p.getQuantity() + ")");
                    counter++;
                }
            }
            addSearchProduct(productsFound);
        } else if(searchAction.equalsIgnoreCase("2")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Product> result = mp.searchProducts("desc", searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Product Description):");

            if(result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for(Product p : result) {
                    productsFound.add(p);
                    System.out.println("- #" + (counter + 1) + " " +
                            p.getName() + " (Price: " + p.getPrice()
                            + " | Quantity Available " + p.getQuantity() + ")");
                    counter++;
                }
            }
            addSearchProduct(productsFound);
        } else if(searchAction.equalsIgnoreCase("3")) {
            System.out.println(separator);
            System.out.println("Please enter your search parameter: ");
            String searchParam = sc.nextLine();

            ArrayList<Store> result = mp.searchStores(searchParam);
            System.out.println(separator);
            System.out.println("Your search results (via Store Name):");

            if(result.isEmpty()) {
                System.out.println("- No results found");
            } else {
                for(Store s : result) {
                    System.out.println("- " + s.getName());
                    if(result.isEmpty()) {
                        System.out.println("--- No products");
                        System.out.println("Returning to search menu...");
                        searchMenu();
                    } else {
                        for(Product p : s.getProducts()) {
                            productsFound.add(p);
                            System.out.println("--- #" + (counter+1) + " "
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
        if(!productsFound.isEmpty()) {
            System.out.println(separator);
            System.out.println("Please enter: ");
            System.out.println("[Correspond #] View Product Info");
            System.out.println("[Anything Else] Return to Customer Menu");

            String productPick = sc.nextLine();

            if (productPick.matches("-?\\d+(\\.\\d+)?")) { // TODO: needs testing
                while(Integer.parseInt(productPick)-1 > productsFound.size()) {
                    System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                    productPick = sc.nextLine();
                    if(!productPick.matches("-?\\d+(\\.\\d+)?")) {
                        productPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if(productPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    Product p = productsFound.get(Integer.parseInt(productPick) - 1);
                    if(p.getQuantity() <= 0) {
                        while(p.getQuantity() <= 0) {
                            System.out.println("There is no more of this product available to purchase! Please enter another option: ");
                            productPick = sc.nextLine();
                            if(productPick.matches("-?\\d+(\\.\\d+)?")) {
                                p = productsFound.get(Integer.parseInt(productPick) - 1);
                                if(p.getQuantity() >= 1) {
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
                                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")) {
                                                System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }
                                            while(p.getQuantity()-Integer.parseInt(purchaseQuantity) < 0) {
                                                System.out.println("There is only " + p.getQuantity() + " of this product available to purchase.");
                                                System.out.println("Please try a smaller quantity: ");
                                                purchaseQuantity = sc.nextLine();
                                            }

                                            shoppingCart.addPurchase(new Sale((Customer) user, p, Integer.parseInt(purchaseQuantity)));

                                            // TESTING
                                            FileManager.addCustomerShopppingCart((Customer) user, p, Integer.parseInt(purchaseQuantity));
                                            p.setQuantity(p.getQuantity()-Integer.parseInt(purchaseQuantity));
                                            // TESTING

                                            System.out.println("Successfully added " + purchaseQuantity + " to your shopping cart." +
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
                                while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")) {
                                    System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }
                                while(p.getQuantity()-Integer.parseInt(purchaseQuantity) < 0) {
                                    System.out.println("There is only " + p.getQuantity() + " of this product available to purchase.");
                                    System.out.println("Please try a smaller quantity: ");
                                    purchaseQuantity = sc.nextLine();
                                }

                                shoppingCart.addPurchase(new Sale((Customer) user, p, Integer.parseInt(purchaseQuantity)));

                                // TESTING
                                FileManager.addCustomerShopppingCart((Customer) user, p, Integer.parseInt(purchaseQuantity));
                                p.setQuantity(p.getQuantity()-Integer.parseInt(purchaseQuantity));
                                // TESTING

                                System.out.println("Successfully added " + purchaseQuantity + " to your shopping cart." +
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
            System.out.println("No products available.");
            System.out.println("Please enter: ");
            System.out.println("[1] Return to Search Menu");
            System.out.println("[Anything Else] Return to Customer Menu");

            String noProdAction = sc.nextLine();
            if(noProdAction.equalsIgnoreCase("1")) {
                searchMenu();
            } else {
                customerMainMenu();
            }
        }
    }

    public static void cartMenu() {
        int counter = 0;
        double allTotal = 0;
        System.out.println(separator);
        System.out.println("Your shopping cart:");
        if(shoppingCart.getHeldPurchases().size() == 0) { // TODO: needs testing
            System.out.println("- Empty");
        } else { // TODO: needs testing
            for(Sale s : shoppingCart.getHeldPurchases()) {
                counter++;
                allTotal += s.getTotalCost();
                String totalPrice = String.format("%.2f", s.getTotalCost());
                System.out.println("- #" + counter + " " + s.getProduct().getName()
                        + " (" + s.getQuantity() + " for $"
                        + s.getProduct().getPrice() + " each | Purchase Total $"
                        + totalPrice + ")");
            }
        }

        System.out.println(separator);
        System.out.println("Please enter: ");
        System.out.println("[CH] Checkout & Purchase");
        System.out.println("[Corresponding #] View Product Info");
        System.out.println("[Anything Else] Return to Customer Menu");

        String cartPick = sc.nextLine();
        if(cartPick.matches("-?\\d+(\\.\\d+)?")) { // TODO: needs testing
            while(Integer.parseInt(cartPick)-1 > shoppingCart.getHeldPurchases().size()) {
                System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                cartPick = sc.nextLine();
                if(!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                    cartPick = "QUIT_MENU_PLEASE";
                    break;
                }
            }
            if(cartPick.equals("QUIT_MENU_PLEASE")) {
                customerMainMenu();
            } else {
                Sale s = shoppingCart.getPurchase(Integer.parseInt(cartPick) - 1);
                Product p = s.getProduct();
                if(p != null) { // TODO: needs testing
                    showProductInfo(p);
                    System.out.println(separator);
                    System.out.println("Please enter: ");
                    System.out.println("[1] Remove from Shopping Cart");
                    System.out.println("[2] Change Purchase Quantity");
                    System.out.println("[Anything Else] Return to All Products Page");

                    String cartAction = sc.nextLine();
                    if(cartAction.equalsIgnoreCase("1")) { // TODO: needs testing

                        // TESTING
                        p.setQuantity(p.getQuantity() + FileManager.getCustomerShoppingCartQuantity((Customer) user, p));
                        FileManager.updateCustomerShoppingCart((Customer) user, p, 0);
                        // TESTING

                        shoppingCart.removePurchase(p);
                        System.out.println("Successfully removed " + p.getName() + " from your shopping cart." +
                                " Returning to shopping cart page...");
                        cartMenu();
                    } else if(cartAction.equalsIgnoreCase("2")) { // TODO: needs testing
                        System.out.println(separator);
                        System.out.println("Please enter a new quantity: ");
                        String changeQuantity = sc.nextLine();
                        while(!changeQuantity.matches("-?\\d+(\\.\\d+)?")) { // TODO: needs testing
                            System.out.println("Entered quantity is not an integer! Please enter a valid quantity: ");
                            changeQuantity = sc.nextLine();
                        }
                        while(p.getQuantity()+(FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                -Integer.parseInt(changeQuantity)) < 0) {
                            System.out.println("There is only " + p.getQuantity() + " of this product available to purchase.");
                            System.out.println("You currently have " +
                                    FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                    + " held in your shopping cart.");
                            System.out.println("Please try a smaller quantity: ");
                            changeQuantity = sc.nextLine();
                        }

                        // TESTING
                        p.setQuantity(p.getQuantity() +
                                (FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                        -Integer.parseInt(changeQuantity)));
                        FileManager.updateCustomerShoppingCart((Customer) user, p, Integer.parseInt(changeQuantity));
                        // TESTING

                        shoppingCart.getPurchase(Integer.parseInt(cartPick) - 1).setQuantity(Integer.parseInt(changeQuantity));
                        shoppingCart.recalculateTotalHeldProducts();
                        System.out.println("Successfully changed purchase quantity to " + changeQuantity + "." +
                                " Returning to shopping cart page...");
                        cartMenu();
                    } else { // TODO: needs testing
                        cartMenu();
                    }
                } else {
                    while(p == null) { // TODO: needs testing
                        System.out.println("Entered # is not a valid product! Please enter a valid product #: ");
                        cartPick = sc.nextLine();
                        if(!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                            continue;
                        }
                        p = shoppingCart.getHeldPurchases().get(Integer.parseInt(cartPick)-1).getProduct();
                    }
                }
            }
        } else if(cartPick.equalsIgnoreCase("c")) { // TODO: needs testing
            String allTotalPrice = String.format("%.2f", allTotal);

            System.out.println(separator);
            System.out.println("You are purchasing " + shoppingCart.getTotalheldProducts() + " products for $" + allTotalPrice);
            System.out.println(separator);
            System.out.println("Would you like to complete the purchase?" +
                    "\nPlease enter [Y] or [Yes] to purchase, or [Anything Else] to return: ");

            String purchaseAction = sc.nextLine();
            if(purchaseAction.equalsIgnoreCase("y")
                    || purchaseAction.equalsIgnoreCase("yes")) { // TODO: needs testing
                System.out.println(separator);
                for (Sale heldPurchase : shoppingCart.getHeldPurchases()) {
                    if (heldPurchase.getQuantity() <= heldPurchase.getProduct().getQuantity()) {
                        ((Customer) user).addSale(heldPurchase);
                        heldPurchase.getProduct().setQuantity(heldPurchase.getProduct().getQuantity() - heldPurchase.getQuantity());
                        for (Store store : mp.getStores()) {
                            if (store.getProducts().contains(heldPurchase.getProduct())) {
                                store.addSale(heldPurchase);
                            }
                        }
                        System.out.println("Purchased " + heldPurchase.getProduct().getName() + "!");
                    } else {
                        System.out.println("Failed to purchase " + heldPurchase.getProduct().getName() + ": You ordered more products than were available.");
                    }
                }
                shoppingCart.setHeldPurchases(new ArrayList<>());
                System.out.println("Returning to customer menu page...");
                customerMainMenu();
            } else { // TODO: needs testing
                cartMenu();
            }
        } else { // TODO: needs testing
            customerMainMenu();
        }
    }

    public static void historyMenu() {
        ArrayList<Sale> history = ((Customer) user).getPurchases();
        if (history == null || history.size() == 0) {
            System.out.println("You have not purchased anything yet!");
        } else {
            System.out.println(separator);
            System.out.println("You have purchased " + ((Customer) user).getTotalPurchasedProducts() + " products:");
            for (Sale s : history) {
                System.out.println(s.getProduct().getName() + " (" + s.getQuantity() + " ct.) purchased for $" + String.format("%.2f", s.getTotalCost()));
            }
        }
        customerMainMenu();
    }

    public static void sellerMainMenu() {
        System.out.println("What would you like to do today?");
        System.out.println("Please enter: ");
        System.out.println("[1] View Your Stores");
        System.out.println("[Q] Logout & Quit");

        String action = sc.nextLine();
        if (action.equalsIgnoreCase("1")) {
            storesMenu();
        } else if (action.equalsIgnoreCase("q")) {
            printFarewell();
        } else {
            if(tryAgain("Invalid menu selection!")) {
                customerMainMenu();
            } else {
                printFarewell();
            }
        }
    }

    public static void storesMenu() {
        MarketPlace mp = new MarketPlace();
        ArrayList<Store> stores = new ArrayList<>();

        for(Store s : mp.getStores()) {
        }

        System.out.println(separator);
        System.out.println("Your stores:");

    }


    /**
     * Prints the farewell message.
     */
    public static void printFarewell() {
        System.out.println("Thank you for visiting The MarketPlace!");
        System.out.println("Come again another day.");
    }
}