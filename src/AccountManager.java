import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {


    public static User login(String email, String password) {
        ArrayList<ArrayList<String>> data = FileManager.getUserLogins();
        for(ArrayList<String> arr : data) {
            if(arr.get(2).equalsIgnoreCase(email)) {
                if(arr.get(3).equals(password)) {
                    if(arr.get(0).equalsIgnoreCase("C")) {
                        return new Customer(arr.get(1), arr.get(2), arr.get(3));
                    } if(arr.get(0).equalsIgnoreCase("S")) {
                        return new Seller(arr.get(1), arr.get(2), arr.get(3));
                    }
                }
            }
        }
        return null;
    }

    public static User signup(String username, String email, String password, String type) {
        if(FileManager.writeUserSignup(username, email, password, type)) {
            if(type.equalsIgnoreCase("customer")) {
                return new Customer(username, email, password);
            } else if(type.equalsIgnoreCase("seller")) {
                return new Seller(username, email, password);
            }
        }
        return null;
    }
}
