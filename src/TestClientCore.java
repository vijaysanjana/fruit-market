import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * Marketplace main menu system used for logic control flow and to display login, signup, and user actions
 *
 * @author Jack, Nathan, Sanj, Tommy, Adit
 * @version 11/14/2022
 */
class TestClientCore {
    public static final String separator = "---------------------------";
    private static Scanner sc = new Scanner(System.in);
    private static User user;
    private static String userEmail;
    private static ShoppingCart shoppingCart;
    private static MarketPlace mp = new MarketPlace();
    private static String request; //Request to be sent to ServerCore
    private static String[] response; //Response to be sent from ServerCore
    private static PrintWriter clientOut;
    private static BufferedReader serverIn;
    private static String email;
    private static String password;
    private static String loginSignup;
    private static String customerSeller;
    private static String username;

    /**
     * Main method for the marketplace system. Arranges login/signup and customer/seller menus
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9999)) {

            //Server Input and Output
            clientOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //String request; //Request to be sent to ServerCore
            //String[] response; //Response to be sent from ServerCore

            system_loop:
            while (true) {
                String email;
                String password;
                int loginSignup;
                String customerSeller;
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
                            userEmail = email;
                            request = "{Login}," + email + "," + password;
                            clientOut.println(request);
                            response = interpretResponse(serverIn.readLine());
                            //customerSeller = response[0];
                            //username = response[1];


                            //user = AccountManager.login(email, password); //TODO: Remove user
                            //if (user == null) {
                            if (response == null) {
                                if (!tryAgain("Email and password combinations are invalid!")) {
                                    printFarewell();
                                    break system_loop;
                                }
                            } else {
                                customerSeller = response[1];
                                username = response[2];
                                break;
                            }
                        }
                        //if (user instanceof Customer) {
                        if (customerSeller.equals("C")) {
                            JOptionPane.showMessageDialog(null, "Welcome customer: " + username);
                            //System.out.println("Welcome customer: " + user.getUsername());

                            // TESTING
                            /*
                            FileManager.loadAllStores(mp, true);
                            for (Customer customer : mp.getCustomers()) {
                                if (customer.getUsername().equalsIgnoreCase(user.getUsername())
                                        && customer.getEmail().equalsIgnoreCase(user.getEmail())) {
                                    user = customer;
                                }
                            }
                            shoppingCart = ((Customer) user).getShoppingCart();
                            FileManager.loadAllCarts((Customer) user, shoppingCart, mp);
                            */
                            // TESTING

                            customerMainMenu();
                            break system_loop;
                            //} else if (user instanceof Seller) {
                        } else if (customerSeller.equals("S")) {
                            JOptionPane.showMessageDialog(null, "Welcome seller: " + username);
                            //System.out.println("Welcome seller: " + user.getUsername());

                            // TESTING
                            /*
                            FileManager.loadAllStores(mp, true);
                            for (Seller seller : mp.getSellers()) {
                                if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                                        && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                                    user = seller;
                                }
                            }
                            */
                            // TESTING

                            sellerMainMenu();
                            break system_loop;
                        } else {
                            throw new RuntimeException(
                                    new AccountException("LOGGED IN USER IS NEITHER CUSTOMER NOR SELLER!"));
                        }
                    } else if (loginSignup == 1) { // THIS IS THE SIGNUP PART
                        email = JOptionPane.showInputDialog("Please enter your email");
                        password = JOptionPane.showInputDialog("Please enter your password");
                        username = JOptionPane.showInputDialog("Please enter your username");
                        userEmail = email;
                        customer_seller:
                        while (true) {
                            String[] options1 = {"Customer", "Seller"};
                            customerSeller = String.valueOf(JOptionPane.showOptionDialog(null, "Are you signing up to be a customer or seller?", "Signup", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options1, options1[0]));

                            if (customerSeller.equals("0")) {
                                request = "{Signup}," + username + "," + email + "," + password + ",customer";
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                if (response != null) {
                                    JOptionPane.showMessageDialog(null, "Successfully signed up! Logging you in...");
                                    request = "{Login}," + email + "," + password;
                                    clientOut.println(request);
                                    response = interpretResponse(serverIn.readLine());
                                    customerSeller = response[1];
                                    if (customerSeller.equals("C")) {
                                        JOptionPane.showMessageDialog(null, "Welcome customer: " + username);
                                        customerMainMenu();
                                        //System.out.println("Welcome customer: " + user.getUsername());
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
                            } else if (customerSeller.equals("1")) {

                                request = "{Signup}," + username + "," + email + "," + password + ",seller";
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                if (response != null) {
                                    JOptionPane.showMessageDialog(null, "Successfully signed up! Logging you in...");
                                    request = "{Login}," + email + "," + password;
                                    clientOut.println(request);
                                    response = interpretResponse(serverIn.readLine());
                                    customerSeller = response[1];
                                    if (customerSeller.equals("S")) {
                                        JOptionPane.showMessageDialog(null, "Welcome seller: " + username);
                                        sellerMainMenu();
                                        //System.out.println("Welcome seller: " + user.getUsername());
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


                                /*
                                if (AccountManager.signup(username, email, password, "customer") != null) {
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
                                    if (!tryAgain("User already exists! Please login " +
                                            "or use another email and username.")) {
                                        printFarewell();
                                        break system_loop;
                                    } else {
                                        main(new String[0]);
                                        break system_loop;
                                    }
                                }
                            } else if (customerSeller.equals("2")) {
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
                            */
                        }
                    } else {
                        if (!tryAgain("Invalid login/signup selection!")) {
                            printFarewell();
                            break system_loop;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    public static void customerMainMenu() throws IOException {
        response = null;

        request = "{getTotalHeldProducts}," + userEmail;
        System.out.println("getTotalHeldProducts Request: " + request);
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        for (String s : response) {
            System.out.println("getTotalHeldProducts response: " + s);
        }
        String totalHeldProducts = response[1];

        String[] options = {"Open Marketplace", "Search for Fruit", "View Shopping Cart ("
                + totalHeldProducts + " Fruits)", "View Purchase History", "View Statistics Dashboard", "Delete Account", "Logout & Quit"};
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
            deleteAccount(userEmail);
        } else if (action == 6) {
            printFarewell();
        } else {
            if (tryAgain("Invalid menu selection!")) {
                customerMainMenu();
            }
        }
    }

    /**
     * Navigates through the marketplace menu for customers
     */
    public static void marketplaceMenu() throws IOException {
        request = "{getAllStores}";
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        String[] storeNames;
        if (response.length == 1) {
            System.out.println("no stores found");
            storeNames = new String[0];
        } else
            storeNames = interpretListedResponse(response[1]);
        ArrayList<String> products = new ArrayList<>(); //temp holder for products
        ArrayList<String> allProducts = new ArrayList<>(); //all products names
        String availStores = "";

        availStores += "All available stores:";

        for (String s : storeNames) {
            System.out.println("Store Name: " + s);
        }

        int counter = 0;
        System.out.println("found the stores"); //TODO: TEST
        for (String s : storeNames) {
            System.out.println("finding products in store"); //TODO: TEST
            request = "{getProducts}," + s;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            products = new ArrayList<>();
            for (String listedResponse : response) {
                System.out.println("finding products in store");
                if (!listedResponse.equals("{getProducts}")) {
                    products.add(interpretListedResponse(listedResponse)[0]);
                }
            }
            //products.addAll(Arrays.asList(interpretListedResponse(response[1])));
            for (String p : products) {
                System.out.println("product: " + p);
            }
            availStores += "\n- " + s;
            if (storeNames.length == 0) {
                availStores += "\n--- No fruits found";
            } else {
                if (products.isEmpty())
                    availStores += "\n--- No fruits found";
                for (String p : products) {
                    System.out.println("goin through the products");
                    allProducts.add(p);
                    //request = "{getProductByName}," + s + p;
                    request = "{getProductInfo}," + p;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    availStores += "\n--- #" + (counter + 1) + " " +
                            response[1] + " (Price $" + response[3] + " " +
                            "| Quantity Available " + response[4] + ")";
                    counter++;
                }
            }
        }
        System.out.println("bouta print some info"); //TODO: TEST
        JOptionPane.showMessageDialog(null, availStores);

        if (!products.isEmpty()) {
            String choices = "";
            for (String p : allProducts) {
                choices += "#" + (allProducts.indexOf(p) + 1) + " " + p;
                if (allProducts.indexOf(p) != allProducts.size() - 1) // not last item in list
                    choices += ", ";
            }
            choices += "\nPlease enter: ";
            choices += "\n[PH] Sort Fruits by Price (High to Low)";
            choices += "\n[PL] Sort Fruits by Price (Low to High)";
            choices += "\n[QH] Sort Fruits by Quantity Available (High to Low)";
            choices += "\n[QL] Sort Fruits by Quantity Available (High to Low)";
            choices += "\n[Correspond #] View Fruit Info";
            choices += "\n[Anything Else] Return to Customer Menu";

            String productPick = JOptionPane.showInputDialog(choices);
            if (productPick.matches("-?\\d+(\\.\\d+)?")) {
                while (Integer.parseInt(productPick) - 1 > allProducts.size()
                        || Integer.parseInt(productPick) < 1) {
                    productPick = JOptionPane.showInputDialog("Entered # is not a valid item! " +
                            "Please enter a valid item #: ");
                    if (!productPick.matches("-?\\d+(\\.\\d+)?")) {
                        productPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if (productPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    String[] currentProduct;
                    request = "{getProductByName}," + allProducts.get(Integer.parseInt(productPick) - 1);
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    currentProduct = response;
                    if (Integer.parseInt(response[4]) <= 0) { //quantity <= 0
                        while (Integer.parseInt(response[4]) <= 0) { //quantity <= 0
                            productPick = JOptionPane.showInputDialog("There is no more of this fruit available to purchase! " +
                                    "Please enter another option: ");
                            if (productPick.matches("-?\\d+(\\.\\d+)?")
                                    && (Integer.parseInt(productPick) > 1)) {
                                request = "{getProductByName}," + allProducts.get(Integer.parseInt(productPick) - 1);
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                if (Integer.parseInt(response[4]) >= 1) {
                                    if (response[0].equals("{getProductByName}")) { // TODO: needs testing
                                        showProductInfo(response);
                                        String[] options = {"Add to Shopping Cart", "Return to Search Page"};
                                        int productAction = JOptionPane.showOptionDialog(null, "Please choose.", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                        if (productAction == 0) {
                                            String purchaseQuantity = JOptionPane.showInputDialog("Please enter a purchase quantity: ");
                                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                                    || (Integer.parseInt(purchaseQuantity) < 1)) {
                                                purchaseQuantity = JOptionPane.showInputDialog("Entered quantity is not a valid integer! " +
                                                        "Please enter a valid quantity: ");
                                            }
                                            while (Integer.parseInt(response[4]) - Integer.parseInt(purchaseQuantity) < 0) {
                                                purchaseQuantity = JOptionPane.showInputDialog("There is only " + Integer.parseInt(response[4]) + " of " +
                                                        "this fruit available to purchase. Please try a smaller quantity:");
                                            }

                                            request =
                                                    "{addToCustomerShoppingCart}," + userEmail + "," + allProducts.get(Integer.parseInt(productPick) - 1) + "," + purchaseQuantity;
                                            clientOut.println(request);
                                            serverIn.readLine();
                                            request =
                                                    "{subtractProductQuantity}," + response[1] + "," + Integer.parseInt(purchaseQuantity);
                                            clientOut.println(request);
                                            serverIn.readLine();
                                            // TESTING

                                            JOptionPane.showMessageDialog(null, "Successfully added " + purchaseQuantity
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
                        if (currentProduct != null) {
                            showProductInfo(currentProduct);
                            String[] options = {"Add to Shopping Cart", "Return to All Fruits Page"};
                            int productAction = JOptionPane.showOptionDialog(null, "Please choose one.", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if (productAction == 0) {
                                String purchaseQuantity = JOptionPane.showInputDialog("Please enter a purchase quantity.");
                                while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                        || (Integer.parseInt(purchaseQuantity) < 1)) {
                                    purchaseQuantity = JOptionPane.showInputDialog("Entered quantity is not a valid integer! Please enter a valid quantity:");
                                }
                                while (Integer.parseInt(currentProduct[4]) - Integer.parseInt(purchaseQuantity) < 0) { // quantity - purchase quantity
                                    purchaseQuantity = JOptionPane.showInputDialog("There is only " + currentProduct[4] + " of " +
                                            "this fruit available to purchase. Please try a smaller quantity: ");
                                }

                                request =
                                        "{addToCustomerShoppingCart}," + userEmail + "," + allProducts.get(Integer.parseInt(productPick) - 1) + "," + purchaseQuantity;
                                clientOut.println(request);
                                serverIn.readLine();
                                request =
                                        "{subtractProductQuantity}," + currentProduct[1] + "," + Integer.parseInt(purchaseQuantity);
                                clientOut.println(request);
                                serverIn.readLine();
                                // TESTING
                                JOptionPane.showConfirmDialog(null, "Successfully added " +
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
                request =
                        "{updatePriceSortedProducts}," + true;
                clientOut.println(request);
                serverIn.readLine();
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("pl")) {
                request =
                        "{updatePriceSortedProducts}," + false;
                clientOut.println(request);
                serverIn.readLine();
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("qh")) {
                request =
                        "{updateQuantitySortedProducts}," + true;
                clientOut.println(request);
                serverIn.readLine();
                marketplaceMenu();
            } else if (productPick.equalsIgnoreCase("ql")) {
                request =
                        "{updateQuantitySortedProducts}," + false;
                clientOut.println(request);
                serverIn.readLine();
                marketplaceMenu();
            } else {
                customerMainMenu();
            }
        } else {
            String[] options = {"Return to Customer Menu", "Logout & Quit"};

            int noProdAction = JOptionPane.showOptionDialog(null, "No fruits available. Please choose one.", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (noProdAction == 0) {
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
        String info = "";
        info += "Fruit: " + product.getName();
        info += "\nDescription: " + product.getDescription();
        info += "\nPrice: " + String.format("%.2f", product.getPrice());
        info += "\nQuantity Available: " + product.getQuantity();
        JOptionPane.showMessageDialog(null, info, "Product", JOptionPane.PLAIN_MESSAGE);

    }

    private static void showProductInfo(String[] response) {
        String info = "";
        info += "Fruit: " + response[1];
        info += "\nDescription: " + response[2];
        info += "\nPrice: " + response[3];
        info += "\nQuantity Available: " + response[4];
        JOptionPane.showMessageDialog(null, info, "Product", JOptionPane.PLAIN_MESSAGE);
    }

    public static void searchMenu() throws IOException {
        int counter = 0;
        String[] options = {"Fruit Name", "Fruit Description", "Store Name", "Return to Customer Menu"};
        int searchAction = JOptionPane.showOptionDialog(null, "What would you like to search for?", "Search", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (searchAction == 0) { //search by name
            String searchParam = JOptionPane.showInputDialog("Please enter your search parameter.");
            request = "{getSearchProduct}," + "name" + "," + searchParam;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            ArrayList<String> productNames = new ArrayList<>();
            if (response.length != 1)
                if (response[1].contains("|"))
                    productNames.addAll(Arrays.asList(interpretListedResponse(response[1])));
                else productNames.add(response[1]);
            String results = "";

            results += "Your search results (via Fruit Name):";

            if (productNames.isEmpty()) {
                results += "\n- No results found";
            } else {
                for (String p : productNames) {
                    request = "{getProductByName}," + p;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    results += "\n--- #" + (counter + 1) + " " +
                            response[1] + " (Price $" + response[3] + " " +
                            "| Quantity Available " + response[4] + ")";
                    counter++;
                }
            }
            JOptionPane.showMessageDialog(null, results);
            addSearchProduct(productNames);
        } else if (searchAction == 1) { //search by description
            String searchParam = JOptionPane.showInputDialog("Please enter your search parameter.");

            request = "{getSearchProduct}," + "desc" + "," + searchParam;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            ArrayList<String> productNames = new ArrayList<>();
            if (response.length != 1)
                productNames.addAll(Arrays.asList(interpretListedResponse(response[1])));
            String results = "";

            results += "Your search results (via Fruit Description):";

            if (productNames.isEmpty()) {
                results += "\n- No results found";
            } else {
                for (String p : productNames) {
                    request = "{getProductByName}," + p;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    results += "\n--- #" + (counter + 1) + " " +
                            response[1] + " (Price $" + response[3] + " " +
                            "| Quantity Available " + response[4] + ")";
                    counter++;
                }
            }
            JOptionPane.showMessageDialog(null, results);
            addSearchProduct(productNames);
        } else if (searchAction == 2) { //search by store name
            String searchParam = JOptionPane.showInputDialog("Please enter your search parameter.");

            request = "{getSearchProduct}," + "stores" + "," + searchParam;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            ArrayList<String> storeNames = new ArrayList<>();
            if (response.length != 1)
                storeNames.addAll(Arrays.asList(interpretListedResponse(response[1])));
            ArrayList<String> productsFound = new ArrayList<>();
            String results = "";

            results += "Your search results (via Store Name):";

            if (storeNames.isEmpty()) {
                results += "\n- No results found";
            } else {
                for (String s : storeNames) {
                    results += "- " + s;
                    if (storeNames.isEmpty()) {
                        results += "\n--- No fruits found";
                        results += "\nReturning to search menu...";
                        searchMenu();
                    } else {
                        for (String p : storeNames) {
                            productsFound.add(p);
                            request = "{getProductByName}," + p;
                            clientOut.println(request);
                            response = interpretResponse(serverIn.readLine());
                            results += "\n--- #" + (counter + 1) + " " +
                                    response[1] + " (Price $" + response[3] + " " +
                                    "| Quantity Available " + response[4] + ")";
                            counter++;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, results);
            addSearchProduct(productsFound);
        } else {
            customerMainMenu();
        }
    }

    public static void addSearchProduct(ArrayList<String> productsFound) throws IOException {
        if (!productsFound.isEmpty()) {
            String temp = "";
            for (String p : productsFound) {
                temp += "#" + (productsFound.indexOf(p) + 1) + " " + p;
                if (productsFound.indexOf(p) != productsFound.size() - 1) // not last item in list
                    temp += ", ";
            }
            String productPick = JOptionPane.showInputDialog(temp + "\nPlease enter:" + "\n[Correspond #] View Fruit " +
                    "Info" + "\n[Anything Else] Return to Customer Menu");

            if (productPick.matches("-?\\d+(\\.\\d+)?")
                    && !(Integer.parseInt(productPick) < 1)) { // TODO: needs testing
                while (Integer.parseInt(productPick) - 1 > productsFound.size()) {
                    productPick = JOptionPane.showInputDialog("Entered # is not a valid item! Please enter a valid item #: ");
                    if (!productPick.matches("-?\\d+(\\.\\d+)?")
                            || (Integer.parseInt(productPick) < 1)) {
                        productPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if (productPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    request = "{getProductByName}," + productsFound.get(Integer.parseInt(productPick) - 1);
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (Integer.parseInt(response[4]) <= 0) {
                        while (Integer.parseInt(response[4]) <= 0) {
                            productPick = JOptionPane.showInputDialog("There is no more of this fruit available to purchase! " +
                                    "Please enter another option: ");
                            if (productPick.matches("-?\\d+(\\.\\d+)?")
                                    && (Integer.parseInt(productPick) > 1)) {
                                request = "{getProductByName}," + productsFound.get(Integer.parseInt(productPick) - 1);
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                if (Integer.parseInt(response[4]) >= 1) {
                                    if (response[0].equals("{getProductByName}")) { // TODO: needs testing
                                        showProductInfo(response);
                                        String[] options = {"Add to Shopping Cart", "Return to Search Page"};
                                        int productAction = JOptionPane.showOptionDialog(null, "Please choose.", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                        if (productAction == 0) {
                                            String purchaseQuantity = JOptionPane.showInputDialog("Please enter a purchase quantity: ");
                                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                                    || (Integer.parseInt(purchaseQuantity) < 1)) {
                                                purchaseQuantity = JOptionPane.showInputDialog("Entered quantity is not a valid integer! " +
                                                        "Please enter a valid quantity: ");
                                            }
                                            while (Integer.parseInt(response[4]) - Integer.parseInt(purchaseQuantity) < 0) {
                                                purchaseQuantity = JOptionPane.showInputDialog("There is only " + Integer.parseInt(response[4]) + " of " +
                                                        "this fruit available to purchase. Please try a smaller quantity:");
                                            }

                                            request =
                                                    "{addToCustomerShoppingCart}," + userEmail + "," + productsFound.get(Integer.parseInt(productPick) - 1) + "," + purchaseQuantity;
                                            clientOut.println(request);
                                            serverIn.readLine();
                                            request =
                                                    "{subtractProductQuantity}," + response[1] + "," + Integer.parseInt(purchaseQuantity);
                                            clientOut.println(request);
                                            serverIn.readLine();
                                            // TESTING
                                            JOptionPane.showConfirmDialog(null, "Successfully added " + purchaseQuantity
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
                    } else { // TODO: needs testing
                        showProductInfo(response);
                        String[] options = {"Add to Shopping Cart", "Return to Search Page"};
                        int productAction = JOptionPane.showOptionDialog(null, "Please choose one.", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (productAction == 0) {
                            String purchaseQuantity = JOptionPane.showInputDialog("Please enter a purchase quantity: ");
                            while (!purchaseQuantity.matches("-?\\d+(\\.\\d+)?")
                                    || (Integer.parseInt(purchaseQuantity) < 1)) {

                                purchaseQuantity = JOptionPane.showInputDialog("Entered quantity is not a valid integer! " +
                                        "Please enter a valid quantity: ");
                            }
                            while (Integer.parseInt(response[4]) - Integer.parseInt(purchaseQuantity) < 0) {
                                purchaseQuantity = JOptionPane.showInputDialog("There is only " + Integer.parseInt(response[4]) + " " +
                                        "of this fruit available to purchase. Please try a smaller quantity: ");
                            }

                            request =
                                    "{addToCustomerShoppingCart}," + userEmail + "," + productsFound.get(Integer.parseInt(productPick) - 1) + "," + purchaseQuantity;
                            clientOut.println(request);
                            serverIn.readLine();
                            request =
                                    "{subtractProductQuantity}," + response[1] + "," + Integer.parseInt(purchaseQuantity);
                            clientOut.println(request);
                            serverIn.readLine();
                            // TESTING

                            JOptionPane.showConfirmDialog(null, "Successfully added "
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
        String[] options = {"Return to Search Menu", "Return to Customer Menu"};
        int noProdAction = JOptionPane.showOptionDialog(null, "No fruits available. Please choose one:", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (noProdAction == 0) {
            searchMenu();
        } else {
            customerMainMenu();
        }
    }

    /**
     * Accesses cart menu to see shopping cart for users
     */
    public static void cartMenu() throws IOException {
        String heldProducts = "";
        int counter = 0;
        double allTotal = 0;
        String info = "";
        info += "Your shopping cart:";
        ArrayList<String> salePrints = new ArrayList<>();

        request = "{getCustomerShoppingCart}," + userEmail;
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        ArrayList<String> heldSales = new ArrayList<>();
        if (response.length != 1) {
            heldProducts = response[2];
            heldSales = new ArrayList<String>(Arrays.asList(interpretListedResponse(response[1])));
        }
        if (heldSales.isEmpty()) {
            info += "\n- Empty";
        } else {
            for (String s : heldSales) {
                counter++;
                request = "{getSaleByName}," + s;
                clientOut.println(request);
                response = interpretResponse(serverIn.readLine());
                allTotal += Double.parseDouble(response[2]);
                String totalPrice = String.format("%.2f", allTotal);
                info += "\n- #" + counter + " " + response[1]
                        + " (" + response[3] + " for $"
                        + response[4] + " each | Items Total $"
                        + totalPrice + ")";
                salePrints.add(response[1] + " (" + response[3] + ")");
            }
        }

        System.out.println("bouta print some info"); //TODO: TEST
        if (heldSales.isEmpty()) {
            String[] options = {"Return to Customer Menu", "Logout & Quit"};

            int noProdAction = JOptionPane.showOptionDialog(null, "Your shopping cart is empty. Please choose one.",
                    "Choice",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (noProdAction == 0) {
                customerMainMenu();
            } else {
                printFarewell();
            }
        } else {
            JOptionPane.showMessageDialog(null, info);

            String choices = "";
            choices += "\nShopping Cart: ";
            for (String s : salePrints) {
                choices += "#" + (salePrints.indexOf(s) + 1) + " " + s;
                if (salePrints.indexOf(s) != salePrints.size() - 1) // not last item in list
                    choices += ", ";
            }
            //for (String p : heldSales) {
            //    choices += "#" + (heldSales.indexOf(p) + 1) + " " + p;
            //    if (heldSales.indexOf(p) != heldSales.size() - 1) // not last item in list
            //        choices += ", ";
            //}
            if (heldSales.isEmpty())
                choices += "empty";
            choices += "\nPlease enter: ";
            choices += "\n[CH] Checkout & Purchase";
            choices += "\n[Corresponding #] View Fruit Info";
            choices += "\n[Anything Else] Return to Customer Menu";
            String cartPick = JOptionPane.showInputDialog(choices);
            if (cartPick.matches("-?\\d+(\\.\\d+)?")) {
                while (Integer.parseInt(cartPick) - 1 > heldSales.size()) {
                    cartPick = JOptionPane.showInputDialog("Entered # is not a valid item! Please enter a valid item #: ");
                    if (!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                        cartPick = "QUIT_MENU_PLEASE";
                        break;
                    }
                }
                if (cartPick.equals("QUIT_MENU_PLEASE")) {
                    customerMainMenu();
                } else {
                    request = "{getSaleByName}," + heldSales.get(Integer.parseInt(cartPick) - 1);
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    request = "{getProductByName}," + response[1];
                    clientOut.println(request);
                    String[] currentProduct = interpretResponse(serverIn.readLine());
                    if (response[0].equals("{getSaleByName}")) {
                        showProductInfo(currentProduct);
                        String[] options = {"Remove from Shopping Cart", "Change Purchase Quantity", "Return to All Fruits Page"};
                        int cartAction = JOptionPane.showOptionDialog(null, "Please choose one", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                        if (cartAction == 0) {

                            // TESTING
                            //p.setQuantity(p.getQuantity() +
                            //        FileManager.getCustomerShoppingCartQuantity((Customer) user, p)); //FILE MANAGER
                            //FileManager.updateCustomerShoppingCart((Customer) user, p, 0); //FILE MANAGER
                            // TESTING

                            request = "{removeProductFromShoppingCart}," + userEmail + "," + currentProduct[1];
                            clientOut.println(request);
                            serverIn.readLine();
                            JOptionPane.showConfirmDialog(null, "Successfully removed " + currentProduct[1]
                                    + " from your shopping cart." +
                                    " Returning to shopping cart page...");
                            cartMenu();
                        } else if (cartAction == 1) {
                            String changeQuantity = JOptionPane.showInputDialog("Please enter a new quantity: ");
                            while (!changeQuantity.matches("-?\\d+(\\.\\d+)?")) {
                                changeQuantity = JOptionPane.showInputDialog("Entered quantity is not an integer! " +
                                        "Please enter a valid quantity: ");
                            }
                        /* //FILE MANAGER
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
                         */

                            // TESTING
                        /* //FILE MANAGER
                        p.setQuantity(p.getQuantity() +
                                (FileManager.getCustomerShoppingCartQuantity((Customer) user, p)
                                        - Integer.parseInt(changeQuantity)));
                        FileManager.updateCustomerShoppingCart((Customer) user,
                                p, Integer.parseInt(changeQuantity));
                        // TESTING
                         */
                            request = "{updateQuantityInShoppingCart}," + userEmail + "," + (Integer.parseInt(cartPick) - 1) + "," + Integer.parseInt(changeQuantity);
                            clientOut.println(request);
                            response = interpretResponse(serverIn.readLine());
                            JOptionPane.showConfirmDialog(null, "Successfully changed purchase quantity to "
                                    + changeQuantity + "." +
                                    " Returning to shopping cart page...");
                            cartMenu();
                        } else {
                            cartMenu();
                        }
                    } else {
                        while (currentProduct[0].equals("{getProductByName}")) {
                            cartPick = JOptionPane.showInputDialog("Entered # is not a valid fruit! " +
                                    "Please enter a valid fruit #: ");
                            if (!cartPick.matches("-?\\d+(\\.\\d+)?")) {
                                continue;
                            }
                            request = "{getProductByName}," + heldSales.get(Integer.parseInt(cartPick) - 1);
                            clientOut.println(request);
                            currentProduct = interpretResponse(serverIn.readLine());
                        }
                    }
                }
            } else if (cartPick.equalsIgnoreCase("ch")) {
                String allTotalPrice = String.format("%.2f", allTotal);
                String[] options = {"Yes", "Cancel and Return to Menu"};

                request = "{getCustomerShoppingCart}," + userEmail;
                clientOut.println(request);
                response = interpretResponse(serverIn.readLine());
                heldSales = new ArrayList<String>(Arrays.asList(interpretListedResponse(response[1])));

                int purchaseAction = JOptionPane.showOptionDialog(null, "You are purchasing "
                        + heldProducts
                        + " fruits for $" + allTotalPrice + ". Would you like to complete the purchase?", "Choose", JOptionPane.YES_NO_OPTION, JOptionPane.NO_OPTION, null, options, options[0]);
                if (purchaseAction == 0) {
                    //for (String sale : heldSales) { //purchase sales add to customer and store
                        request = "{purchaseAllHeldSales}," + userEmail;
                        clientOut.println(request);
                        //String bruh = serverIn.readLine();
                        response = interpretResponse(serverIn.readLine());
                        //try {
                        //    Thread.sleep(1000);
                        //} catch (InterruptedException e) {
                        //}
                    //}
                    request = "{removeAllProductsFromShoppingCart}," + userEmail;
                    clientOut.println(request);
                    //bruh = serverIn.readLine();
                    response = interpretResponse(serverIn.readLine());
                    //for (String s : response) {
                    //    System.out.println("removeAllProductsFromShoppingCart Response: " + s);
                    //}
                    JOptionPane.showMessageDialog(null, "Returning to customer menu page...");
                    customerMainMenu();
                } else {
                    cartMenu();
                }
            } else {
                customerMainMenu();
            }
        }
    }

    /**
     * Accesses purchase history menu
     */
    public static void historyMenu() throws IOException {
        request = "{getCustomerPurchaseHistory}," + userEmail;
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        ArrayList<String> pastPurchases = new ArrayList<>();
        if (!response[1].isEmpty())
            pastPurchases.addAll(Arrays.asList(interpretListedResponse(response[1])));
        String hist = "";
        hist += "Your purchase history:";
        if (response[2].equals("0")) {
            hist += "\n- No purchases found";
        } else {
            for (String sale : pastPurchases) {
                request = "{getSaleByName}," + sale;
                clientOut.println(request);
                response = interpretResponse(serverIn.readLine());
                hist += "\n- " + response[1];
                hist += "\n--- Price Each: " + response[4];
                hist += "\n--- Quantity Purchased: " + response[3];
                hist += "\n--- Total Price: " + response[2];
            }
            hist += "\nYou have purchased " + pastPurchases.size() + " fruits in total!";
        }
        JOptionPane.showMessageDialog(null, hist);
        String[] options = {"Export Purchase History CSV", "Return to Customer Menu"};
        int action = JOptionPane.showOptionDialog(null, "Please choose:", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (action == 0) {
            //FileManager.exportCustomerHistory((Customer) user); //FILE MANAGER
            JOptionPane.showMessageDialog(null, "Returning to customer menu...");
        }
        customerMainMenu();
    }

    public static void dashboardMenu(int sortMode) throws IOException {
        System.out.println("dashboard attempted");
        request = "{getUserBasicData}," + userEmail;
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        String userType = response[3];
        if (userType.equals("C")) {
            String availStores = "";
            availStores += "All Available Stores:";
            ArrayList<String> stores = new ArrayList<>();
            switch (sortMode) {
                case 1:
                    request = "{getSalesSortedStores}," + true;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (response.length == 2)
                        stores.addAll(Arrays.asList(interpretListedResponse(response[1])));
                    break;
                case 2:
                    request = "{getSalesSortedStores}," + false;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (response.length == 2)
                        stores.addAll(Arrays.asList(interpretListedResponse(response[1])));
                    break;
                case 3:
                    request = "{getUserSalesSortedStores}," + true;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (response.length == 2)
                        stores.addAll(Arrays.asList(interpretListedResponse(response[1])));
                    break;
                case 4:
                    request = "{getUserSalesSortedStores}," + false;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (response.length == 2)
                        stores.addAll(Arrays.asList(interpretListedResponse(response[1])));
                    break;
                default:
                    request = "{getAllStores}";
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    if (response.length == 2)
                        stores.addAll(Arrays.asList(interpretListedResponse(response[1])));
                    break;
            }
            if (stores.isEmpty()) {
                String[] options = {"Return to Customer Menu", "Logout & Quit"};

                int noProdAction = JOptionPane.showOptionDialog(null, "There are currently no stores on The " +
                                "MarketPlace. Please choose one.", "Choice",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (noProdAction == 0) {
                    customerMainMenu();
                } else {
                    printFarewell();
                }
            } else {

                for (String store : stores) {
                    request = "{getQuantityOfProductsBoughtByCustomer}," + userEmail + "," + store;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    availStores += "\n- " + store;
                    availStores += "\n--- Total Products Sold: " +
                            response[2];
                    availStores += "\n--- Total Products Sold to You: " + response[1];
                }

                String[] options = {"Sort Stores by Total Fruits Sold (High to Low)", "Sort Stores by Total Fruits Sold (Low to High)", "Sort Stores by Total Fruits Sold to You (High to Low)", "Sort Stores by Total Fruits Sold to You (Low to High)", "Return to Customer Menu"};
                int productPick = JOptionPane.showOptionDialog(null, availStores + "\nPlease choose:", "Choice",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (productPick == 0) {
                    dashboardMenu(1);
                } else if (productPick == 1) {
                    dashboardMenu(2);
                } else if (productPick == 2) {
                    dashboardMenu(3);
                } else if (productPick == 3) {
                    dashboardMenu(4);
                } else {
                    customerMainMenu();
                }
            }
        } else if (userType.equals("S")) {
            System.out.println("seller detected");
            String[] options = {"View Sales Statistics", "View Customer Statistics", "Return to Seller Menu"};
            int statChoice = JOptionPane.showOptionDialog(null, "Please Choose:", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (statChoice == 0) {
                productSalesStatsDashboard(0);
            } else if (statChoice == 1) {
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
    public static void customerSalesStatsDashboard(int sortMode) throws IOException {
        String show = "Your Stores: \n";

        request = "{getCustomerStats}," + sortMode;
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());

        for (int i = 1; i < response.length; i++) {
            if (response[i].contains(";")) {
                show = show + " - " + response[i].substring(1);
            } else {
                String customerName = response[i].substring(0, response[i].indexOf("~"));
                String saleQuantity = response[i].substring(response[i].indexOf("~") + 1);
                show = show + ", cusomters:";
                show = show + " - " + customerName + " ("
                        + saleQuantity + " Fruits Purchased)";
            }
            show = show + "\n";
        }

        JOptionPane.showMessageDialog(null, show, "Statistics", JOptionPane.INFORMATION_MESSAGE);

        String[] options = {"Sort Products by Sales (High to Low)", "Sort Products by Sales (Low to High)", "Return to Statistics Dashboard Menu"};
        int productPick = JOptionPane.showOptionDialog(null, "Please enter:", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (productPick == 0) {
            customerSalesStatsDashboard(1);
        } else if (productPick == 1) {
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
    public static void productSalesStatsDashboard(int sortMode) throws IOException {
        String show = "Your Stores: \n";

        request = "{getProductStats}," + sortMode;
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());

        for (int i = 1; i < response.length; i++) {
            if (response[i].contains(";")) {
                show = show + " - " + response[i].substring(1);
            } else {
                String productName = response[i].substring(0, response[i].indexOf("~"));
                String saleQuantity = response[i].substring(response[i].indexOf("~") + 1);
                show = show + ", products:";
                show = show + " - " + productName + " ("
                        + saleQuantity + " Sold)";
            }
            show = show + "\n";
        }

        JOptionPane.showMessageDialog(null, show, "Statistics", JOptionPane.INFORMATION_MESSAGE);

        String[] options = {"Sort Products by Sales (High to Low)", "Sort Products by Sales (Low to High)", "Return to Statistics Dashboard Menu"};
        int productPick = JOptionPane.showOptionDialog(null, "Please enter:", "Choice", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (productPick == 0) {
            productSalesStatsDashboard(1);
        } else if (productPick == 1) {
            productSalesStatsDashboard(2);
        } else {
            dashboardMenu(0);
        }
    }


    /**
     * Deletes an account
     *
     * @param userEmail
     */
    public static void deleteAccount(String userEmail) throws IOException {
        String warning = "";
        warning += "WARNING: Are you sure you want to delete your account?";
        warning += "\nAll user data will be lost and will NOT be recoverable!";
        warning += "\nTHIS DECISION IS FINAL!";
        String[] options = {"Delete Account", "Cancel & Return to Main Menu"};
        int action = JOptionPane.showOptionDialog(null, warning, "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (action == 0) {
            request = "{getDeleteAccount}," + userEmail;
            clientOut.println(request);
            serverIn.readLine();
        } else {
            request = "{getUserBasicData}," + userEmail;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            if (response[3].equals("C")) { //user is customer
                customerMainMenu();
            } else if (response[3].equals("S")) { //user is seller
                sellerMainMenu();
            }
        }
    }

    /**
     * Accesses seller main menu
     */
    public static void sellerMainMenu() throws IOException {
        String[] options = {"View Your Stores", "View Your Carted Fruits", "View Sales History", "View Statistics Dashboard", "Delete Account", "Logout & Quit"};
        int action = JOptionPane.showOptionDialog(null, "What would you like to do today? \n Please enter: ", "Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (action == 0) {
            storesMenu();
        } else if (action == 1) {
            cartedProductsMenu();
        } else if (action == 2) {
            salesMenu();
        } else if (action == 3) {
            dashboardMenu(0);
        } else if (action == 4) {
            deleteAccount(userEmail);
        } else if (action == 5) {
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
    public static void storesMenu() throws IOException {

        request = "{getStoreNames}";
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());
        String[] yourStores = new String[response.length - 1];

        //int counter = 0;
        String show = "Your stores:";

        if (response.length <= 1) {
            show = show + " - No stores found";
        } else {
            for (int i = 1; i < response.length; i++) {
                show = show + (" - #" + (i) + " " + response[i]);
                yourStores[i - 1] = response[i];
            }
        }

        /*
        if (((Seller) user).getStores().isEmpty()) {
            System.out.println("- No stores found");
        } else {
            for (Store store : ((Seller) user).getStores()) {
                System.out.println("- #" + (counter + 1) + " " + store.getName());
                counter++;
            }
        }
        */

        show = show + ("\nPlease enter: ");
        show = show + ("\n[Corresponding #] View Store Info");
        show = show + ("\n[AD] Add New Store");
        show = show + ("\n[Anything Else] Return to Seller Menu");

        String storeAction = JOptionPane.showInputDialog(show);
        if (storeAction.matches("-?\\d+(\\.\\d+)?")) {
            if (yourStores != null) {
                //if (!((Seller) user).getStores().isEmpty()) {
                try {
                    //int counterAgain = 0;
                    String storeName = yourStores[Integer.parseInt(storeAction) - 1];
                    //Store store = ((Seller) user).getStores().get( //TODO: Remove this
                    //        Integer.parseInt(storeAction) - 1);
                    show = "Store: " + storeName + "\n";
                    show = show + "Fruits:";

                    request = "{getProductNames}," + storeName;
                    clientOut.println(request);
                    response = interpretResponse(serverIn.readLine());
                    String[] products = null;

                    if (response.length <= 1) {
                        //if (store.getProducts().isEmpty()) {
                        show = show + " - No fruits found";
                    } else {
                        products = new String[response.length - 1];
                        for (int i = 1; i < response.length; i++) {
                            products[i - 1] = response[i];
                            show = show + " - #" + i + " "
                                    + response[i];
                        }
                        //for (Product prod : store.getProducts()) {
                        //    System.out.println("- #" + (counterAgain + 1) + " "
                        //            + prod.getName());
                        //    counterAgain++;
                        //}
                    }


                    show = show + ("\nPlease enter: ");
                    show = show + ("\n[Corresponding #] View Fruit Info");
                    show = show + ("\n[AD] Add New Fruit");
                    show = show + ("\n[IM] Import Items CSV");
                    show = show + ("\n[EX] Export Items CSV");
                    show = show + ("\n[Anything Else] Return to Seller Menu");

                    String productPick = JOptionPane.showInputDialog(show);

                    if (productPick.matches("-?\\d+(\\.\\d+)?")) {
                        if (products != null) {
                            //if (!store.getProducts().isEmpty()) {
                            String productName = products[Integer.parseInt(productPick) - 1];
                            request = "{getProductInfo}," + productName;
                            clientOut.println(request);
                            response = interpretResponse(serverIn.readLine());
                            showProductInfo(response);
                            //Product prod = store.getProducts().get(
                            //        Integer.parseInt(productPick) - 1);
                            //showProductInfo(prod);


                            String[] options = {"Change Quantity Available", "Change Description", "Change Price", "Remove Fruit", "Return to Seller Menu"};
                            int productAction = JOptionPane.showOptionDialog(null, "Please enter: ", "Product", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if (productAction == 0) {
                                String quant = JOptionPane.showInputDialog("Please enter a new fruit quantity " +
                                        "(integer, at least 1): ");
                                while (!quant.matches("-?\\d+(\\.\\d+)?")
                                        || (Integer.parseInt(quant) <= 0)) {
                                    if (!tryAgain("Invalid value! Quantity must be an integer and at least 1!")) {
                                        storesMenu();
                                        break;
                                    }
                                    quant = JOptionPane.showInputDialog("Please enter a new fruit quantity " +
                                            "(integer, at least 1): ");
                                }

                                request = "{changeProductQuantity}," + productName + "," + quant;
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                //changeProductQuantity(store, prod);

                                JOptionPane.showMessageDialog(null, "Successfully updated " + productName
                                        + "'s quantity available to: " + quant + "\nReturning to all stores menu...", "returning", JOptionPane.INFORMATION_MESSAGE);
                                storesMenu();


                            } else if (productAction == 1) {

                                String desc = JOptionPane.showInputDialog("Please enter a new fruit description: ");
                                while (desc.contains(",") || desc.contains(";")) {
                                    if (!tryAgain("Description cannot contain ',' or ';'!")) ;
                                    storesMenu();
                                    break;
                                }

                                request = "{changeProductDescription}," + productName + "," + desc;
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                //changeProductDescription(store, prod);
                                //FileManager.updateSellerDataDescription((Seller) user, store, product, desc); //FILE MANAGER
                                //product.setDescription(desc);

                                JOptionPane.showMessageDialog(null, "Successfully updated " + productName
                                        + "'s description to: " + desc + "\nReturning to all stores menu...", "returning", JOptionPane.INFORMATION_MESSAGE);
                                storesMenu();


                            } else if (productAction == 2) {

                                String price = JOptionPane.showInputDialog("Please enter a new price (double, at least 0.00): ");
                                String formatted = String.format("%.2f", Double.parseDouble(price));
                                while (!formatted.matches("-?\\d+(\\.\\d+)?")
                                        || (Double.parseDouble(formatted) < 0.00)) {
                                    if (!tryAgain("Invalid value! Price must be a double and at least 0.00!")) {
                                        storesMenu();
                                        break;
                                    }
                                    price = JOptionPane.showInputDialog("Please enter a new price (double, at least 0.00): ");
                                    formatted = String.format("%.2f", Double.parseDouble(price));
                                }

                                request = "{changeProductPrice}," + productName + "," + formatted;
                                clientOut.println(request);
                                response = interpretResponse(serverIn.readLine());
                                //changeProductPrice(store, prod);
                                //FileManager.updateSellerDataPrice((Seller) user, store, //FILE MANAGER
                                //        product, Double.parseDouble(formatted));
                                //product.setPrice(Double.parseDouble(formatted));
                                JOptionPane.showMessageDialog(null, "Successfully updated " + productName
                                        + "'s price to: " + formatted + "\nReturning to all stores menu...", "returning", JOptionPane.INFORMATION_MESSAGE);
                                storesMenu();


                            } else if (productAction == 3) {
                                show = ("WARNING: Are you sure you want to remove this product?");
                                show = show + ("\nWARNING: All seller data " +
                                        "corresponding with this product will be deleted.");
                                show = show + ("\nWARNING: THIS DECISION IS FINAL");

                                String[] optionsTwo = {"Delete Product", "Return to Seller Menu"};
                                int action = JOptionPane.showOptionDialog(null, show, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsTwo, optionsTwo[0]);


                                if (action == 0) {
                                    request = "{removeProduct}," + storeName + "," + productName;
                                    clientOut.println(request);
                                    response = interpretResponse(serverIn.readLine());
                                    //removeProduct(store, prod);
                                    //FileManager.removeSellerDataProduct((Seller) user, store, product); //FILE MANAGER

                                    //mp = new MarketPlace();
                                    //FileManager.loadAllStores(mp, true); //FILE MANAGER
                                    /*
                                    for (Seller seller : mp.getSellers()) {
                                        if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                                                && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                                            user = seller;
                                        }
                                    }
                                    */
                                    JOptionPane.showMessageDialog(null, "Successfully deleted " + productName + "\nReturning to seller menu...", "success", JOptionPane.DEFAULT_OPTION);
                                    sellerMainMenu();
                                } else {
                                    sellerMainMenu();
                                }

                            } else {
                                sellerMainMenu();
                            }
                            /*
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
                            */
                        } else {
                            sellerMainMenu();
                        }
                    } else if (productPick.equalsIgnoreCase("ad")) {
                        addNewProduct(storeName);
                    } else if (productPick.equalsIgnoreCase("im")) {
                        //FileManager.importSellerCSV((Seller) user, store); //FILE MANAGER

                        //mp = new MarketPlace();
                        //FileManager.loadAllStores(mp, true); //FILE MANAGER
                        /*
                        for (Seller seller : mp.getSellers()) {
                            if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                                    && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                                user = seller;
                            }
                        }
                        */

                        JOptionPane.showMessageDialog(null, "Returning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
                        storesMenu();
                    } else if (productPick.equalsIgnoreCase("ex")) {
                        //FileManager.exportSellerCSV((Seller) user, store); //FILE MANAGER
                        JOptionPane.showMessageDialog(null, "Returning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
                        storesMenu();
                    } else {
                        sellerMainMenu();
                    }

                    //sellerMainMenu();
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
    public static void addNewStore() throws IOException {
        String name = JOptionPane.showInputDialog("Enter new store name: ");
        if (name.contains(",") || name.contains(";")) {
            if (!tryAgain("Store name cannot contain ',' or ';'!")) {
                sellerMainMenu();
            } else {
                addNewStore();
            }
        } else {

            request = "{getStoreNames}";
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            for (int i = 1; i < response.length; i++) {
                if (response[i].equalsIgnoreCase(name)) {
                    if (!tryAgain("Store name is already used!")) {
                        sellerMainMenu();
                    } else {
                        addNewStore();
                    }
                }
            }
            request = "{addStore}," + name;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            //Store store = new Store(name, "");
            //((Seller) user).addStore(store);
            //FileManager.createStoreFile((Seller) user, store); //FILE MANAGER
            JOptionPane.showMessageDialog(null, "Successfully added new store! " +
                    "Returning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
            storesMenu();
        }
    }

    /**
     * Adds a new product
     *
     * @param storeName
     */
    public static void addNewProduct(String storeName) throws IOException {
        String name = JOptionPane.showInputDialog("Enter new fruit name: ");
        //String name = sc.nextLine();
        if (name.contains(",") || name.contains(";")) {
            if (!tryAgain("Fruit name cannot contain ',' or ';'!")) {
                sellerMainMenu();
            } else {
                addNewProduct(storeName);
            }
        } else {
            request = "{getProductNames}," + storeName;
            clientOut.println(request);
            response = interpretResponse(serverIn.readLine());
            for (int i = 1; i < response.length; i++) {
                if (response[i].equalsIgnoreCase(name)) {
                    if (!tryAgain("Fruit is already used!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(storeName);
                    }
                }
            }
            String desc = JOptionPane.showInputDialog("Enter fruit description: ");
            if (desc.contains(",") || desc.contains(";")) {
                if (!tryAgain("Fruit description cannot contain ',' or ';' ")) {
                    sellerMainMenu();
                } else {
                    addNewStore();
                }
            } else {
                String temp = JOptionPane.showInputDialog("Enter fruit price");
                double price = 0;
                try {
                    price = Double.parseDouble(temp);
                } catch (Exception e) {
                    if (!tryAgain("Price must be a double!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(storeName);
                    }
                }
                int quant = 0;
                temp = JOptionPane.showInputDialog("Enter fruit available quantity: ");
                try {
                    quant = Integer.parseInt(temp);
                } catch (Exception e) {
                    if (!tryAgain("Quantity must be an integer!")) {
                        sellerMainMenu();
                    } else {
                        addNewProduct(storeName);
                    }
                }

                request = "{addProduct}," + storeName + "," + name + "," + desc + "," + price + "," + quant;
                clientOut.println(request);
                response = interpretResponse(serverIn.readLine());

                //Product p = new Product(name, desc, price, quant);
                //store.addProduct(p);
                JOptionPane.showMessageDialog(null, "Successfully added new fruit! " +
                        "Returning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
                //FileManager.addSellerData((Seller) user, store, p); //FILE MANAGER
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
    public static void changeProductQuantity(Store store, Product product) throws IOException {
        String quant = JOptionPane.showInputDialog("Please enter a new fruit quantity " +
                "(integer, at least 1): ");
        while (!quant.matches("-?\\d+(\\.\\d+)?")
                || (Integer.parseInt(quant) <= 0)) {
            if (!tryAgain("Invalid value! Quantity must be an integer and at least 1!")) {
                storesMenu();
                break;
            }
            quant = JOptionPane.showInputDialog("Please enter a new fruit quantity " +
                    "(integer, at least 1): ");
        }

        //FileManager.updateSellerDataQuantity((Seller) user, //FILE MANAGER
        //        store, product, Integer.parseInt(quant));
        product.setQuantity(Integer.parseInt(quant));
        JOptionPane.showMessageDialog(null, "Successfully updated " + product.getName()
                + "'s quantity available to: " + quant + "\nReturning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
        storesMenu();
    }

    /**
     * Changes product description to new one
     *
     * @param store
     * @param product
     */
    public static void changeProductDescription(Store store, Product product) throws IOException {
        String desc = JOptionPane.showInputDialog("Please enter a new fruit description: ");
        while (desc.contains(",") || desc.contains(";")) {
            if (!tryAgain("Description cannot contain ',' or ';'!")) ;
            storesMenu();
            break;
        }

        //FileManager.updateSellerDataDescription((Seller) user, store, product, desc); //FILE MANAGER
        product.setDescription(desc);
        JOptionPane.showMessageDialog(null, "Successfully updated " + product.getName()
                + "'s description available to: " + desc + "\nReturning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
        storesMenu();
    }

    /**
     * Changes product price to new one
     *
     * @param store
     * @param product
     */
    public static void changeProductPrice(Store store, Product product) throws IOException {
        String price = JOptionPane.showInputDialog("Please enter a new price (double, at least 0.00): ");
        String formatted = String.format("%.2f", Double.parseDouble(price));
        while (!formatted.matches("-?\\d+(\\.\\d+)?")
                || (Double.parseDouble(formatted) < 0.00)) {
            if (!tryAgain("Invalid value! Price must be a double and at least 0.00!")) {
                storesMenu();
                break;
            }
            price = JOptionPane.showInputDialog("Please enter a new price (double, at least 0.00): ");
            formatted = String.format("%.2f", Double.parseDouble(price));
        }

        //FileManager.updateSellerDataPrice((Seller) user, store, //FILE MANAGER
        //        product, Double.parseDouble(formatted));
        product.setPrice(Double.parseDouble(formatted));
        JOptionPane.showMessageDialog(null, "Successfully updated " + product.getName()
                + "'s price available to: " + formatted + "\nReturning to all stores menu...", "menu", JOptionPane.INFORMATION_MESSAGE);
        storesMenu();
    }

    /**
     * Removes a product
     *
     * @param store
     * @param product
     */
    public static void removeProduct(Store store, Product product) throws IOException {
        String show = ("WARNING: Are you sure you want to remove this product?");
        show = show + ("\nWARNING: All seller data " +
                "corresponding with this product will be deleted.");
        show = show + ("\nWARNING: THIS DECISION IS FINAL");

        String[] optionsTwo = {"Delete Product", "Return to Seller Menu"};
        int action = JOptionPane.showOptionDialog(null, show, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsTwo, optionsTwo[0]);

        if (action == 0) {
            //FileManager.removeSellerDataProduct((Seller) user, store, product); //FILE MANAGER

            mp = new MarketPlace();
            //FileManager.loadAllStores(mp, true); //FILE MANAGER
            for (Seller seller : mp.getSellers()) {
                if (seller.getUsername().equalsIgnoreCase(user.getUsername())
                        && seller.getEmail().equalsIgnoreCase(user.getEmail())) {
                    user = seller;
                }
            }

            JOptionPane.showMessageDialog(null, "Successfully deleted " + product.getName() + "\nReturning to seller menu...", "success", JOptionPane.DEFAULT_OPTION);
            sellerMainMenu();
        } else {
            sellerMainMenu();
        }
    }

    /**
     * Displays all shopping carts
     */
    public static void cartedProductsMenu() throws IOException {
        String show = ("All carted items:\n");
        ArrayList<ArrayList<String>> data = null; //(Added "null;" so this could run)
        //FileManager.getAllCarts(); //FILE MANAGER

        request = "{getCartedProducts}";
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());

        for (int i = 1; i < response.length; i++) {
            if (response[i].contains(";")) {
                show = show + (" - Customer: " + response[i].substring(1));
            } else {
                String productName = response[i].substring(0, response[i].indexOf("~"));
                String heldQuantity = response[i].substring(response[i].indexOf("~") + 1);

                request = "{getProductInfo}," + productName;
                clientOut.println(request);
                String[] infoResponse = interpretResponse(serverIn.readLine());

                show = show + ("--- Item: " + productName + " (#Held: " + heldQuantity + ")");
                show = show + ("----- Description: " + infoResponse[2]);
                show = show + ("----- Price: " + infoResponse[3]);
                show = show + ("----- Quantity Available: " + infoResponse[4]);
            }
            show = show + "\n";
        }

        /*
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
        */

        JOptionPane.showMessageDialog(null, show, "Carted Items", JOptionPane.INFORMATION_MESSAGE);
        sellerMainMenu();
    }

    /**
     * Displays all product sales
     */
    public static void salesMenu() throws IOException {
        String show = "Your sales:\n";

        request = "{getSales}";
        clientOut.println(request);
        response = interpretResponse(serverIn.readLine());

        for (int i = 1; i < response.length; i++) {
            if (response[i].contains(";")) {
                show = show + (" - Store: " + response[i].substring(1));
            } else {
                String productName = response[i].substring(0, response[i].indexOf("~"));
                String saleQuantity = response[i].substring(response[i].indexOf("~") + 1);

                request = "{getProductInfo}," + productName;
                clientOut.println(request);
                String[] infoResponse = interpretResponse(serverIn.readLine());

                show = show + ("--- Item: " + productName);
                show = show + ("----- Price: " + infoResponse[3]);
                show = show + ("----- Quantity Sold: " + saleQuantity);

                String formatted = String.format("%.2f",
                        Double.parseDouble(String.valueOf(Integer.parseInt(saleQuantity) * Double.parseDouble(infoResponse[3]))));
                show = show + ("----- Profit Made: " + formatted);
            }
            show = show + "\n";
        }

        /*
        ArrayList<ArrayList<Object>> temp = null; //(Added "null;" so this could run)
        //        FileManager.getSellerAllData((Seller) user); //FILE MANAGER
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
        */

        JOptionPane.showMessageDialog(null, show, "Sales", JOptionPane.INFORMATION_MESSAGE);
        sellerMainMenu();
    }


    /**
     * Prints the farewell message.
     */
    public static void printFarewell() {
        JOptionPane.showMessageDialog(null, "Thank you for visiting The MarketPlace!\nCome again another day.", "Farewell", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String[] interpretResponse(String response) {
        System.out.println(response);
        if (response.equals("null")) {
            return null;
        }
        return response.split(",");
    }

    public static String[] interpretListedResponse(String response) {
        if (response.equals("null")) {
            return null;
        }
        return response.split("\\|");
    }
}