import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;

public class Tester {

    //customer class test
    @Test
    public void customerTest() throws FileNotFoundException {
        Customer customer = new Customer("johndoe", "johndoe@gmail.com", "password");
        Product p = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Sale s = new Sale(customer, p);
        customer.addSale(s);
        Assert.assertEquals(s, customer.getSale(0));
        Assert.assertEquals(s, customer.removeSale(0));
        customer.addSale(s);
        Assert.assertEquals(s, customer.removeSale(s));
        Product p2 = new Product("apple", "healthy fruit", 1.00, 12);
        Sale s2 = new Sale(customer, p2);
        ArrayList<Sale> incart = new ArrayList<Sale>();
        incart.add(s);
        incart.add(s2);
        ShoppingCart sc = new ShoppingCart(incart);
        customer.setShoppingCart(sc);
        customer.setPurchases(incart);
        Assert.assertEquals(incart, customer.getPurchases());
        Assert.assertEquals(sc, customer.getShoppingCart());
        Customer customer2 = new Customer("John Doe", "johndoe@gmail.com", "password", sc, new ArrayList<>());
        Customer customer3 = new Customer("John Doe", "johndoe@gmail.com", "password", sc, new ArrayList<>());
        Assert.assertEquals(true, customer3.equals(customer2));

        File f = new File("userData");
        int size = 0;
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }

            size = lines.size();
        } catch (IOException e) {
            e.printStackTrace();
        }

        customer.pushToFile();

        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            ArrayList<String> lines = new ArrayList<String>();
            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }

            Assert.assertEquals(size + 1, lines.size());
            Assert.assertEquals("C:johndoe;johndoe@gmail.com;password", lines.get(lines.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sale class test
    @Test
    public void saleTest() {
        Customer customer = new Customer("johndoe", "johndoe@gmail.com", "password");
        Product p = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Sale s = new Sale(customer, p, 3);

        Assert.assertEquals(customer, s.getCustomer());
        Assert.assertEquals(p, s.getProduct());
        Assert.assertEquals(3, s.getQuantity());
        Assert.assertEquals("johndoe0", s.getName());
        Assert.assertEquals(15.00, s.getTotalCost(), 1e-15);

        s.setQuantity(5);
        Assert.assertEquals(5, s.getQuantity());
        Assert.assertEquals(25.00, s.getTotalCost(), 1e-15);
        Sale s2 = new Sale(customer, p, 5);
        Assert.assertEquals(true, s.equals(s2));
    }

    //store class test
    @Test
    public void storeTest() {
    }

    //seller class test
    @Test
    public void sellerTest() {

    }

    //marketplace class test
    @Test
    public void marketPlaceTest() {
        Customer customer1 = new Customer("johndoe", "johndoe@gmail.com", "password");
        Customer customer2 = new Customer("janedoe", "janedoe@gmail.com", "password");
        Customer customer3 = new Customer("juandoe", "juandoe@gmail.com", "password");
        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);

    }

}
