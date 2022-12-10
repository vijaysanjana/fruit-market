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
        public ClientHandler(Socket client) {
            this.clientSocket = client;
        }

        public void run() {
            PrintWriter serverOut = null;
            BufferedReader clientIn = null;
            try {

                //Client Input and Output
                serverOut = new PrintWriter(clientSocket.getOutputStream(), true);
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println("Server looking for request..."); //Test
                String[] request = interpretRequest(clientIn.readLine());
                System.out.println("Server interpreted request"); //Test
                String response;
                while (request != null) {
                    System.out.println("Request is not null"); //Test

                    switch (request[0]) {
                        case "{Login}":
                            response = loginRequest(request);
                            serverOut.println(response);
                            System.out.println("Response sent"); //Test
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

                        case "{getUserBasicData}":
                            response = getUserBasicDataRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getAllStores}":
                            response = getAllStoresRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getStore}":
                            response = getStoreRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getAllProducts}":
                            response = getAllProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getProductByName}":
                            response = getProductByNameRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getProductByIndex}":
                            response = getProductByIndexRequest(request);
                            serverOut.println(response);
                            break;

                        case "{addToCustomerShoppingCart}":
                            response = addToCustomerShoppingCartRequest(request);
                            serverOut.println(response);
                            break;

                        case "{updatePriceSortedProducts}":
                            response = updatePriceSortedProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{updateQuantitySortedProducts}":
                            response = updateQuantitySortedProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{subtractProductQuantity}":
                            response = subtractProductQuantityRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getProducts}":
                            response = getProductsRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getSearchProduct}":
                            response = getSearchProductRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getCustomerShoppingCart}":
                            response = getCustomerShoppingCartRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getSaleByName}":
                            response = getSaleByNameRequest(request);
                            serverOut.println(response);
                            break;

                        case "{updateQuantityInShoppingCart}":
                            response = updateQuantityInShoppingCartRequest(request);
                            serverOut.println(response);
                            break;

                        case "{removeAllProductsFromShoppingCart}":
                            response = removeAllProductsFromShoppingCartRequest(request);
                            serverOut.println(response);
                            break;

                        case "{removeProductFromShoppingCart}":
                            response = removeProductFromShoppingCartRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getSalesSortedStores}":
                            response = getSalesSortedStoresRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getQuantityOfProductsBoughtByCustomer}":
                            response = getQuantityOfProductsBoughtByCustomerRequest(request);
                            serverOut.println(response);
                            break;

                        case "{getUserSalesSortedStoresRequest}":
                            response = getUserSalesSortedStoresRequest(request);
                            serverOut.println(response);
                            break;


                        default:
                    }

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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (serverOut != null) {
                        serverOut.close();
                    }
                    if (clientIn != null) {
                        clientIn.close();
                        clientSocket.close();
                    }
                    System.out.println("Closing client thread"); //Test
                } catch (IOException e) {
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

            user = AccountManager.login(ServerCore.mp, email, password);
            if (user != null) {
                String userType = (user instanceof Customer) ? "C" : "S";
                String response = "{Login}," + userType + "," + user.getUsername();
                System.out.println("Response compiled"); //Test
                //System.out.println(mp.getCustomer(0).getUsername());
                //System.out.println(mp.getCustomer(1).getUsername());
                //System.out.println(mp.getCustomer(2).getUsername());
                return response;
            }
            return null;
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
            return null;
        }

        public String getTotalHeldProductsRequest(String[] request) {
            return "{getTotalHeldProducts}," + ((Customer) user).getShoppingCart().getTotalheldProducts();
        }

        //public String getStoreNamesRequest(String[] request) {

        //}

        //gets all store names of initialized stores
        public String getAllStoresRequest(String[] request) {
            String response = "";
            ArrayList<Store> list = mp.getStores();
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getAllStores}," + response;
        }

        //gets all product names of all initialized stores
        public String getAllProductsRequest(String[] request) {
            String response = "";
            ArrayList<Store> list = mp.getStores();
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getAllProducts}," + response;
        }

        //gets all products and their data of certain store
        public String getProductsRequest(String[] request) {
            String temp = "";
            String storeName = request[1];
            Store store = mp.getStore(storeName);
            for (Product p : store.getProducts()) {
                temp += p.getName() + "|" + p.getDescription() + "|" + String.format("%.2f", p.getPrice()) + "|" + p.getQuantity();
                if (store.getProducts().indexOf(p) != store.getProducts().size() - 1) // not last item in list
                    temp += ",";
            }
            return "{getProducts}," + temp;
        }

        //gets all info of certain store
        public String getStoreRequest(String[] request) {
            String products = "";
            String sales = "";
            String storeName = request[1];
            Store store = mp.getStore(storeName);
            for (Product p : store.getProducts()) {
                if (store.getProducts().indexOf(p) == store.getProducts().size() - 1) //last item in list
                    products += p.getName();
                else products += p.getName() + "|";
            }
            for (Sale s : store.getSales()) {
                if (store.getSales().indexOf(s) == store.getSales().size() - 1) //last item in list
                    sales += s.getName();
                else sales += s.getName() + "|";
            }
            return "{getStore}," + store.getName() + "," + store.getDescription() + "," + products + "," + sales + "," + store.getTotalSoldProducts();
        }

        //gets product and its data from store name and array index of Store array
        public String getProductByIndexRequest(String[] request) {
            Store store = mp.getStore(request[1]);
            Product p = store.getProduct(Integer.parseInt(request[2]));
            return "{getProductByIndex}," + p.getName() + "," + p.getDescription() + "," + String.format("%.2f",
                    p.getPrice()) + "," + p.getQuantity();
        }

        //gets product and its info from given param(product name)
        public String getProductByNameRequest(String[] request) {
            Store store = mp.getStore(request[1]);
            Product p = store.getProduct(store.getProducts().indexOf(request[2]));
            return "{getProductByName}," + p.getName() + "," + p.getDescription() + "," + String.format("%.2f",
                    p.getPrice()) + "," + p.getQuantity();
        }

        //gets basic user data including usertype given param(user email)
        public String getUserBasicDataRequest(String[] request) {
            User givenUser = mp.getUser(request[1]);
            String userType;
            if (givenUser instanceof Customer)
                userType = "C";
            else userType = "S";
            return "{getUserBasicData}," + givenUser.getEmail() + "," + givenUser.getUsername() + "," + userType;
        }

        // adds product to customers shopping cart using param(email, product name, quantity being purchased)
        public String addToCustomerShoppingCartRequest(String[] request) {
            Customer customer = (Customer) mp.getUser(request[1]);
            Product product = mp.getProduct(request[2]);
            customer.addSale(new Sale(customer, product, Integer.parseInt(request[2])));
            return "{addToCustomerShoppingCart}";
        }

        // updates product quantity with given param(product name, quantity)
        public String subtractProductQuantityRequest(String[] request) {
            Product product = mp.getProduct(request[1]);
            product.setQuantity(Integer.parseInt(request[2]));
            return "{subtractProductQuantity}";
        }

        // updates product list in each store based price given param(true/false) => high to low/ low to high
        public String updatePriceSortedProductsRequest(String[] request) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getPriceSortedProducts(Boolean.parseBoolean(request[1])));
            }
            return "{updatePriceSortedProducts}";
        }

        // updates product list in each store based quantity given param(true/false) => high to low/ low to high
        public String updateQuantitySortedProductsRequest(String[] request) {
            for (Store store : mp.getStores()) {
                store.setProducts(store.getQuantitySortedProducts(Boolean.parseBoolean(request[1])));
            }
            return "{updateQuantitySortedProducts}";
        }

        //get the products in search from either name/description/stores given param(search type, phrase being searched)
        public String getSearchProductRequest(String[] request) {
            ArrayList<Product> result = new ArrayList<>();
            if (request[1].equals("name")) {
                result = mp.searchProducts("name", request[2]);
            } else if (request[1].equals("desc")) {
                result = mp.searchProducts("desc", request[2]);
            } else if (request[1].equals("stores")) {
                result = mp.searchProducts("stores", request[2]);
            }
            String products = "";
            for (Product p : result) {
                if (result.indexOf(p) == result.size() - 1) //last item in list
                    products += p.getName();
                else products += p.getName() + "|";
            }
            return "{getSearchProduct}," + products;
        }

        //get customer shopping cart info and cart size given param(user email)
        public String getCustomerShoppingCartRequest(String[] request) {
            Customer customer = (Customer) mp.getUser(request[1]);
            ArrayList<Sale> list = customer.getShoppingCart().getHeldPurchases();
            String response = "";
            for (Sale s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getCustomerShoppingCart}," + response + "," + customer.getShoppingCart().getTotalheldProducts();
        }

        //gets Sale info given param(sale name)
        public String getSaleByNameRequest(String[] request) {
            Sale sale = mp.getSale(request[1]);
            return "{getSaleByName}," + sale.getProduct().getName() + "," + String.format("%.2f",
                    sale.getTotalCost()) + "," + sale.getQuantity() + "," + String.format("%.2f",
                    sale.getProduct().getPrice());
        }

        //removes product from customers shopping cart given param(user email, product name)
        public String removeProductFromShoppingCartRequest(String[] request) {
            Customer customer = (Customer) mp.getUser(request[1]);
            Product product = mp.getProduct(request[2]);
            customer.getShoppingCart().removePurchase(product);
            return "{removeProductFromShoppingCart}";
        }

        //updates customers shopping cart given param(user email, sale index, new quantity amount wanted)
        public String updateQuantityInShoppingCartRequest(String[] request) {
            Customer customer = (Customer) mp.getUser(request[1]);
            customer.getShoppingCart().getPurchase(Integer.parseInt(request[2])).setQuantity(Integer.parseInt(request[3]));
            customer.getShoppingCart().recalculateTotalHeldProducts();
            return "{updateQuantityInShoppingCart}";
        }

        //removes all products from given users shopping cart param(user email)
        public String removeAllProductsFromShoppingCartRequest(String[] request) {
            Customer customer = (Customer) mp.getUser(request[1]);
            customer.getShoppingCart().setHeldPurchases(new ArrayList<>());
            return "{removeAllProductsFromShoppingCart}";
        }

        //gets sales sorted list of store names given param(user email)
        public String getSalesSortedStoresRequest(String[] request) {
            String response = "";
            ArrayList<Store> list;
            if (Boolean.parseBoolean(request[1]))
                list = mp.getSalesSortedStores(true);
            else
                list = mp.getSalesSortedStores(false);
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getSalesSortedStores}," + response;
        }

        //gets user sales sorted list of store names given param(user email)
        public String getUserSalesSortedStoresRequest(String[] request) {
            String response = "";
            User givenUser = mp.getUser(request[1]);
            ArrayList<Store> list;
            if (Boolean.parseBoolean(request[1]))
                list = mp.getUserSalesSortedStores(givenUser, true);
            else
                list = mp.getUserSalesSortedStores(givenUser, true);
            for (Store s : list) {
                if (list.indexOf(s) == list.size() - 1) response += s.getName();
                else response += s.getName() + "|";
            }
            return "{getUserSalesSortedStores}," + response;
        }

        //gets quantity of products bought by customer given param(user email, store name)
        public String getQuantityOfProductsBoughtByCustomerRequest(String[] request) {
            String response = "";
            Customer customer = (Customer) mp.getUser(request[1]);
            Store store = mp.getStore(request[2]);
            int soldToUser = store.getQuantityOfProductsBoughtByCustomer(customer);

            return "{getQuantityOfProductsBoughtByCustomer}," + soldToUser + "," + store.getTotalSoldProducts();
        }


    }
}