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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        system_loop:
        while(true) {
            String email;
            String password;
            String response;

            System.out.println("Welcome to The Marketplace!");
            System.out.println(separator);
            System.out.println("Login or signup to use our service.");
            System.out.println("Please enter" + "\n[1] to Login" + "\n[2] to Signup");
            response = sc.nextLine();

            // THIS IS THE ACCOUNT LOOP
            // WILL REPEAT UNTIL USER MAKES SELECTION BETWEEN [LOGIN] or [SIGNUP]
            // CAN BE CANCELLED TO QUIT PROGRAM
            account_loop:
            while(true) {
                System.out.println(separator);
                System.out.println("Are you a Customer or Seller?");
                System.out.println("Please enter" + "\n[1] for Customer" + "\n[2] for Seller");
                response = sc.nextLine();

                if(response.equals("1")) { // login
                    // THIS IS THE LOGIN SYSTEM LOOP
                    // WILL REPEAT UNTIL USER MAKES SELECTION BETWEEN [CUSTOMER] or [SELLER] LOGIN TYPES
                    // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (account_loop)
                    login_type_loop:
                    while(true) {
                        if(response.equals("1")) { // login as customer
                            System.out.println(separator);
                            System.out.println("Logging in as a Customer...");
                            System.out.println("Please enter your email: ");
                            email = sc.nextLine();
                            System.out.println("Please enter your password: ");
                            password = sc.nextLine();

                            // TODO: insert login system here

                            // THIS IS THE CUSTOMER LOGIN LOOP
                            // WILL REPEAT UNTIL USER SUCCESSFULLY [LOGS IN]
                            // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (login_type_loop)
                            customer_login_loop:
                            while(true) {
                                if(AccountManager.login(email, password)) {
                                    System.out.println("Customer login successful!");
                                    break customer_login_loop;
                                } else {
                                    System.out.println("Invalid email and password combination. Try again?");
                                    System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                                    response = sc.nextLine();
                                    if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                        break system_loop;
                                    }
                                }
                            }

                            // THIS ONLY OCCURS IF LOGIN IS SUCCESSFUL
                            Customer customer = new Customer(email, password);
                            // TODO: add customer stuff
                            //  Core.customerMenu(customer);
                        } if(response.equals("2")) { // login as seller
                            System.out.println(separator);
                            System.out.println("Logging in as a Seller...");
                            System.out.println("Please enter your email: ");
                            email = sc.nextLine();
                            System.out.println("Please enter your password: ");
                            password = sc.nextLine();

                            // TODO: insert login system here

                            // THIS IS THE SELLER LOGIN LOOP
                            // WILL REPEAT UNTIL USER SUCCESSFULLY [LOGS IN]
                            // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (login_type_loop)
                            seller_login_loop:
                            while(true) {
                                if(AccountManager.login(email, password)) {
                                    System.out.println("Seller login successful!");
                                    break seller_login_loop;
                                } else {
                                    System.out.println("Invalid email and password combination. Try again?");
                                    System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                                    response = sc.nextLine();
                                    if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                        break system_loop;
                                    }
                                }
                            }

                            // THIS ONLY OCCURS IF LOGIN IS SUCCESSFUL
                            Seller seller = new Seller(email, password);
                            // TODO: add seller stuff
                            //  Core.sellerMenu(seller);
                        } else {
                            System.out.println("Invalid selection. Try again?");
                            System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                            response = sc.nextLine();
                            if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                break system_loop;
                            }
                        }
                    }
                } else if(response.equals("2")) { // signup
                    // THIS IS THE SIGNUP SYSTEM LOOP
                    // WILL REPEAT UNTIL USER MAKES SELECTION BETWEEN [CUSTOMER] or [SELLER] SIGNUP TYPES
                    // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (account_loop)
                    signup_type_loop:
                    while(true) {
                        if(response.equals("1")) { // signup as customer
                            // THIS IS THE CUSTOMER SIGNUP LOOP
                            // WILL REPEAT UNTIL USER SUCCESSFULLY [SIGNS IN]
                            // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (signup_type_loop)
                            customer_login_loop:
                            while(true) {
                                System.out.println(separator);
                                System.out.println("Signing up as a Customer...");
                                System.out.println("Please enter your email: ");
                                email = sc.nextLine();
                                System.out.println("Please enter your password: ");
                                password = sc.nextLine();

                                // TODO: insert signup system here

                                if(AccountManager.signup(email, password)) {
                                    System.out.println(separator);
                                    System.out.println("Sign up successful! Returning to main menu...");
                                    break account_loop;
                                } else {
                                    System.out.println("An error occurred! Try again?");
                                    System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                                    response = sc.nextLine();
                                    if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                        break system_loop;
                                    }
                                }
                            }
                        } if(response.equals("2")) { // signup as seller
                            // THIS IS THE SELLER LOGIN LOOP
                            // WILL REPEAT UNTIL USER SUCCESSFULLY [LOGS IN]
                            // CAN BE CANCELLED TO RETURN TO PREVIOUS LOOP (signup_type_loop)
                            seller_login_loop:
                            while(true) {
                                System.out.println(separator);
                                System.out.println("Signing up as a Seller...");
                                System.out.println("Please enter your email: ");
                                email = sc.nextLine();
                                System.out.println("Please enter your password: ");
                                password = sc.nextLine();

                                // TODO: insert signup system here

                                if(AccountManager.signup(email, password)) {
                                    System.out.println(separator);
                                    System.out.println("Sign up successful! Returning to main menu...");
                                    break account_loop;
                                } else {
                                    System.out.println("An error occurred! Try again?");
                                    System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                                    response = sc.nextLine();
                                    if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                        break system_loop;
                                    }
                                }
                            }
                        } else {
                            System.out.println("Invalid selection. Try again?");
                            System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                            response = sc.nextLine();
                            if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                                break system_loop;
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid selection. Try again?");
                    System.out.println("Enter [Y or Yes] to try again, or anything else to cancel.");
                    response = sc.nextLine();
                    if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")) {
                        break system_loop;
                    }
                }
            }

            System.out.println("Thank you for using The Marketplace!");
            System.out.println("Please come back another time!");
        }
    }

    public static void customerMenu(Customer customer) {
        // TODO
    }

    public static void sellerMenu(Seller seller) {
        // TODO
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