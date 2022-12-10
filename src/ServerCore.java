import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Server class
class ServerCore {

    private static MarketPlace mp = new MarketPlace();

    public static void main(String[] args) {
        ServerSocket server = null;
        //FileManager.loadAllStores(mp, true);
        DataManager.loadEverything(mp);


        try {
            // server is listening on port 9999
            server = new ServerSocket(9999);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"); //Test

                // create a new thread object
                ClientHandler clientSocket = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private User user;
        private ShoppingCart shoppingCart;

        // Constructor
        public ClientHandler(Socket client)
        {
            this.clientSocket = client;
        }

        public void run()
        {
            PrintWriter serverOut = null;
            BufferedReader clientIn = null;
            try {

                //Client Input and Output
                serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println("Server looking for request..."); //Test
                String[] request = interpretRequest(clientIn.readLine());
                System.out.println("Server interpreted request"); //Test
                String response = "";
                while (request != null) {
                    System.out.println("Request is not null"); //Test

                    switch (request[0]) {
                        case "{Login}":
                            response = loginRequest(request);
                            serverOut.println(response);
                            //Interpret Request
                            //Update Data Classes
                            //Construct Necessary Data for Client in Response Format
                            //Send Response to ClientCore
                            break;


                        case "{Signup}":
                            response = signupRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getTotalHeldProducts}":
                            response = getTotalHeldProductsRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getStoreNames}":
                            response = getStoreNamesRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getProductNames}":
                            response = getProductNamesRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getProductInfo}":
                            response = getProductInfoRequest(request);
                            serverOut.println(response);
                            break;


                        case "{changeProductQuantity}":
                            response = changeProductQuantityRequest(request);
                            serverOut.println(response);
                            break;


                        case "{changeProductDescription}":
                            response = changeProductDescriptionRequest(request);
                            serverOut.println(response);
                            break;


                        case "{changeProductPrice}":
                            response = changeProductPriceRequest(request);
                            serverOut.println(response);
                            break;


                        case "{removeProduct}":
                            response = removeProductRequest(request);
                            serverOut.println(response);
                            break;


                        case "{addProduct}":
                            response = addProductRequest(request);
                            serverOut.println(response);
                            break;

                        case "{addStore}":
                            response = addStoreRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getCartedProducts}":
                            response = getCartedProductsRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getSales}":
                            response = getSalesRequest(request);
                            serverOut.println(response);
                            break;


                        case "{getCustomerStats}":
                            response = getCustomerStatsRequest(request);
                            serverOut.println(response);
                            break;


                        default:
                    }

                    System.out.println("Response sent: " + response); //Test
                    DataManager.saveEverything(mp);
                    System.out.println("Server looking for request..."); //Test
                    request = interpretRequest(clientIn.readLine());
                    System.out.println("Server interpreted request"); //Test
                }

                //REFERENCE CODE
                /*
                String line;
                while ((line = in.readLine()) != null) {

                    // writing the received message from
                    // client
                    System.out.printf(" Sent from the client: %s\n", line);
                    out.println(line);
                }
                */
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (serverOut != null) {
                        serverOut.close();
                    }
                    if (clientIn != null) {
                        clientIn.close();
                        clientSocket.close();
                    }
                    System.out.println("Closing client thread"); //Test
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public String[] interpretRequest(String request) {
            if (request == null) {
                return null;
            }
            return request.split(",");
        }

        public String loginRequest(String[] request) {
            System.out.println("Login request found"); //Test
            String email = request[1];
            String password = request[2];

            user = AccountManager.login(mp, email, password);
            if (user != null) {
                String userType = (user instanceof Customer) ? "C" : "S";
                String response = "{Login}," + userType + "," + user.getUsername();
                System.out.println("Response compiled"); //Test
                //System.out.println(mp.getCustomer(0).getUsername());
                //System.out.println(mp.getCustomer(1).getUsername());
                //System.out.println(mp.getCustomer(2).getUsername());
                return response;
            }
            return "null";
        }

        public String signupRequest(String[] request) {
            System.out.println("Signup request found"); //Test
            String username = request[1];
            String email = request[2];
            String password = request[3];
            String customerSeller = request[4];

            if (AccountManager.signup(username, email, password, customerSeller) != null) {
                if (customerSeller.equals("customer")) {
                    mp.addCustomer(new Customer(username, email, password));
                } else {
                    mp.addSeller(new Seller(username, email, password));
                }
                return "{Signup}";
            }
            return "null";
        }

        public String getTotalHeldProductsRequest(String[] request) {
            return "{getTotalHeldProducts}," + ((Customer) user).getShoppingCart().getTotalheldProducts();
        }

        public String getStoreNamesRequest(String[] request) {
            String response = "{getStoreNames}";
            for (Store store : ((Seller) user).getStores()) {
                response += "," + store.getName();
            }
            return response;
        }

        public String getProductNamesRequest(String[] request) {
            String response = "{getProductNames}";
            Store store = null;
            for (Store st : ((Seller) user).getStores()) {
                if (st.getName().equals(request[1])) {
                    store = st;
                }
            }
            if (store != null) {
                for (Product product : store.getProducts()) {
                    response += "," + product.getName();
                }
                return response;
            }
            return "null";
        }

        public String getProductInfoRequest(String[] request) {
            String response = "{getProductInfo}";
            Product product = null;
            for (Product pr : mp.getProducts()) {
                if (pr.getName().equals(request[1])) {
                    product = pr;
                }
            }
            if (product != null) {
                response += "," + product.getName();
                response += "," + product.getDescription();
                response += "," + String.format("%.2f", product.getPrice());
                response += "," + product.getQuantity();
                return response;
            }
            return "null";
        }

        public String changeProductQuantityRequest(String[] request) {
            String response = "{changeProductQuantity}";
            Product product = null;
            for (Product pr : mp.getProducts()) {
                if (pr.getName().equals(request[1])) {
                    product = pr;
                }
            }
            if (product != null) {
                product.setQuantity(Integer.parseInt(request[2]));
                return response;
            }
            return "null";
        }

        public String changeProductDescriptionRequest(String[] request) {
            String response = "{changeProductDescription}";
            Product product = null;
            for (Product pr : mp.getProducts()) {
                if (pr.getName().equals(request[1])) {
                    product = pr;
                }
            }
            if (product != null) {
                product.setDescription(request[2]);
                return response;
            }
            return "null";
        }

        public String changeProductPriceRequest(String[] request) {
            String response = "{changeProductPrice}";
            Product product = null;
            for (Product pr : mp.getProducts()) {
                if (pr.getName().equals(request[1])) {
                    product = pr;
                }
            }
            if (product != null) {
                product.setPrice(Double.parseDouble(request[2]));
                return response;
            }
            return "null";
        }

        public String removeProductRequest(String[] request) {
            String response = "{removeProduct}";
            Store store = null;
            for (Store st : ((Seller) user).getStores()) {
                if (st.getName().equals(request[1])) {
                    store = st;
                }
            }
            if (store != null) {
                for (Product pr : store.getProducts()) {
                    if (pr.getName().equals(request[2])) {
                        store.removeProduct(pr);
                        return response;
                    }
                }
            }
            return "null";
        }

        public String addProductRequest(String[] request) {
            String response = "{addProduct}";
            Store store = null;
            for (Store st : ((Seller) user).getStores()) {
                if (st.getName().equals(request[1])) {
                    store = st;
                }
            }
            if (store != null) {
                Product product = new Product(request[2], request[3], Double.parseDouble(request[4]), Integer.parseInt(request[5]));
                store.addProduct(product);
                return response;
            }
            return "null";
        }

        public String addStoreRequest(String[] request) {
            String response = "{addStore}";
            Store store = new Store(request[1], "");
            ((Seller) user).addStore(store);
            return response;
        }

        public String getCartedProductsRequest(String[] request) {
            String response = "{getCartedProducts}";
            for (Customer customer : mp.getCustomers()) {
                boolean cartedMine = false;
                for (Sale sale : customer.getShoppingCart().getHeldPurchases()) {
                    if (((Seller) user).getAllProducts().contains(sale.getProduct())) {
                        if (!cartedMine) {
                            response += ",;" + customer.getUsername();
                            cartedMine = true;
                        }
                        response += "," + sale.getProduct().getName() + "~" + sale.getQuantity();
                    }
                }
            }
            return response;
        }

        public String getSalesRequest(String[] request) {
            String response = "{getSales}";
            for (Store store : ((Seller) user).getStores()) {
                boolean saleSeen = false;
                for (Sale sale : store.getSales()) {
                    if (!saleSeen) {
                        response += ",;" + store.getName();
                        saleSeen = true;
                    }
                    response += "," + sale.getProduct().getName() + "~" + sale.getQuantity();
                }
            }
            return response;
        }

        public String getCustomerStatsRequest(String[] request) {
            String response = "{getCustomerStats}";
            for (Store store : ((Seller) user).getStores()) {
                ArrayList<Customer> customers;
                switch (Integer.parseInt(request[1])) {
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
                    response += ",;" + store.getName();
                    for (Customer customer : customers) {
                        response += "," + customer.getUsername() + "~" + store.getQuantityOfProductsBoughtByCustomer((customer));
                        //System.out.println(customer.getUsername() + " ("
                        //        + store.getQuantityOfProductsBoughtByCustomer((Customer) user)
                        //        + " Fruits Purchased)");
                    }
                }
            }
            return response;
        }

    }
}