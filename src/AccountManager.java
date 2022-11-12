import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class AccountManager {


    public static User login(String email, String password) {
        try {
            File f = new File("userData");
            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = br.readLine();
                while(line != null) {
                    String tempEmail = line.substring(
                            line.indexOf(";")+1, line.indexOf(";", line.indexOf(";")+1)
                    );
                    if(tempEmail.equalsIgnoreCase(email)) {
                        String tempPass = line.substring(line.indexOf(";",
                                        line.indexOf(";", line.indexOf(";")+1))+1);
                        if(tempPass.equals(password)) {
                            String tempName = line.substring(2,line.indexOf(";"));

                            return new Customer(tempName, tempEmail, tempPass);
                        }
                    }
                    line = br.readLine();
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! LOGIN SYSTEM FAILED!"));
        }
        return null;
    }

    public static User signup(String email, String password, String username, String userType) {
        try {
            File f = new File("userData");
            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = br.readLine();
                while(line != null) {
                    String tempEmail = line.substring(
                            line.indexOf(";")+1, line.indexOf(";", line.indexOf(";")+1)
                    );
                    String tempName = line.substring(2,line.indexOf(";"));
                    if(tempEmail.equalsIgnoreCase(email)) {
                        return null;
                    }
                    if(tempName.equalsIgnoreCase(username)) {
                        return null;
                    }
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(new Exception("FATAL ERROR OCCURRED! SIGNUP SYSTEM FAILED!"));
        }

        if(userType.equalsIgnoreCase("customer")) {
             Customer customer = new Customer(username, email, password);
             customer.pushToFile();
             return customer;
        } else if(userType.equalsIgnoreCase("seller")) {
            Seller seller = new Seller(username, email, password);
            seller.pushToFile();
            return seller;
        }
        return null;
    }
}
