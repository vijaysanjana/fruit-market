import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Account management system to handle logins/signups
 * @author Jack, Nathan, Sanj, Tommy, Adit
 * @version 11/14/2022
 */
public class AccountManager {
    /**
     * Method to attempt a login of user based on file data
     *
     * @param email
     * @param password
     * @return
     */
    public static User login(String email, String password) {
        ArrayList<ArrayList<String>> data = FileManager.getUserLogins();
        for (ArrayList<String> arr : data) {
            if (arr.get(2).equalsIgnoreCase(email)) {
                if (arr.get(3).equals(password)) {
                    if (arr.get(0).equalsIgnoreCase("C")) {
                        return new Customer(arr.get(1), arr.get(2), arr.get(3));
                    }
                    if (arr.get(0).equalsIgnoreCase("S")) {
                        return new Seller(arr.get(1), arr.get(2), arr.get(3));
                    }
                }
            }
        }
        return null;
    }

    /**
     * Signs up a user and saves their logins
     *
     * @param username
     * @param email
     * @param password
     * @param type
     * @return
     */
    public static User signup(String username, String email, String password, String type) {
        if (FileManager.writeUserSignup(username, email, password, type)) {
            if (type.equalsIgnoreCase("customer")) {
                return new Customer(username, email, password);
            } else if (type.equalsIgnoreCase("seller")) {
                return new Seller(username, email, password);
            }
        }
        return null;
    }
}
