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

        File f = new File("saleData");
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

        s.pushToFile();

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
            Assert.assertEquals("johndoe0:juice;5;25.00", lines.get(lines.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //store class test
    @Test
    public void storeTest() {
        Product p = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Product a = new Product("apple", "a popular fruit", 1.00, 7);
        Product c = new Product("candy", "a sweet and tasty snack", 10.00, 5);

        ArrayList<Product> products = new ArrayList<>();
        products.add(p);
        products.add(a);

        Store s = new Store("Supermarket", "Food and Drinks", products);
        Store s2 = new Store("Supermarket", "Food and Drinks", products);
        Assert.assertEquals(true, s.equals(s2));

        Assert.assertEquals("Supermarket", s.getName());
        s.setName("Safeway");
        Assert.assertEquals("Safeway", s.getName());
        Assert.assertEquals("Food and Drinks", s.getDescription());
        Assert.assertEquals(products, s.getProducts());
        s.addProduct(c);
        Assert.assertEquals(products, s.getProducts());
        Assert.assertEquals(p, s.getProduct(0));

        ArrayList<Product> quantSortMaxLast = new ArrayList<>();
        quantSortMaxLast.add(c);
        quantSortMaxLast.add(a);
        quantSortMaxLast.add(p);

        ArrayList<Product> priceSortMaxLast = new ArrayList<>();
        priceSortMaxLast.add(a);
        priceSortMaxLast.add(p);
        priceSortMaxLast.add(c);

        ArrayList<Product> quantSortMaxFirst = new ArrayList<>();
        quantSortMaxFirst.add(p);
        quantSortMaxFirst.add(a);
        quantSortMaxFirst.add(c);

        ArrayList<Product> priceSortMaxFirst = new ArrayList<>();
        priceSortMaxFirst.add(c);
        priceSortMaxFirst.add(p);
        priceSortMaxFirst.add(a);

        Assert.assertEquals(priceSortMaxLast, s.getPriceSortedProducts(false));
        Assert.assertEquals(priceSortMaxFirst, s.getPriceSortedProducts(true));
        Assert.assertEquals(quantSortMaxFirst, s.getQuantitySortedProducts(true));
        Assert.assertEquals(quantSortMaxLast, s.getQuantitySortedProducts(false));

        int ogsize = s.getProducts().size();
        Assert.assertEquals(p, s.removeProduct(0));
        Assert.assertEquals(ogsize - 1, s.getProducts().size());
        Assert.assertEquals(c, s.removeProduct(c));
        Assert.assertEquals(ogsize - 2, s.getProducts().size());
        Assert.assertEquals(a, s.getProduct(0));

        Customer customer = new Customer("johndoe", "johndoe@gmail.com", "password");
        Product product = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Sale sale = new Sale(customer, product, 3);
        Sale sale2 = new Sale(customer, product, 5);

        s.addSale(sale);
        s.addSale(sale2);
        ArrayList<Sale> sales = new ArrayList<>();
        sales.add(sale);
        sales.add(sale2);
        Assert.assertEquals(sales, s.getSales());
        Assert.assertEquals(sale, s.getSale(0));
        Assert.assertEquals(sale2, s.removeSale(1));
        Assert.assertEquals(sale, s.removeSale(sale));
        Assert.assertEquals(0, s.getSales().size());

        File f = new File("storeData");
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

        s.addProduct(p);
        s.addProduct(c);
        s.pushToFile();

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
            Assert.assertEquals("Safeway:Food and Drinks;apple,juice,candy;", lines.get(lines.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //seller class test
    @Test
    public void sellerTest() {
        Product p = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Product a = new Product("apple", "a popular fruit", 1.00, 7);
        Product c = new Product("candy", "a sweet and tasty snack", 10.00, 5);

        ArrayList<Product> allProducts = new ArrayList<>();

        ArrayList<Product> products = new ArrayList<>();
        allProducts.add(p);
        products.add(p);
        allProducts.add(a);
        products.add(a);
        allProducts.add(c);
        products.add(c);

        Store s = new Store("Safeway", "Food and Drinks", products);

        Product h = new Product("hammer", "bangs nails into walls", 10.00, 12);
        Product n = new Product("nails", "holds things together", 0.05, 100);
        Product d = new Product("drill", "automated way to attach nails", 40.00, 6);

        ArrayList<Product> products2 = new ArrayList<>();
        allProducts.add(h);
        products2.add(h);
        allProducts.add(n);
        products2.add(n);
        allProducts.add(d);
        products2.add(d);

        Store s2 = new Store("Home Depot", "Hardware", products2);

        ArrayList<Store> stores = new ArrayList<>();
        stores.add(s);
        stores.add(s2);

        Seller seller = new Seller("johndoe", "johndoe@gmail.com", "password", stores);
        Seller seller2 = new Seller("johndoe", "johndoe@gmail.com", "password", stores);

        Assert.assertEquals(stores, seller.getStores());
        Assert.assertEquals(true, seller.equals(seller2));

        Store s3 = new Store("Lowe's", "Hardware", products2);

        allProducts.add(h);
        allProducts.add(n);
        allProducts.add(d);

        seller.addStore(s3);
        Assert.assertEquals(allProducts, seller.getAllProducts());
        Assert.assertEquals(3, seller.getStores().size());
        Assert.assertEquals(s3, seller.getStore(2));
        Assert.assertEquals(s3, seller.removeStore(2));
        Assert.assertEquals(s, seller.removeStore(s));
        Assert.assertEquals(1, seller.getStores().size());
        Assert.assertEquals(s2, seller.getStore(0));
        seller.updateSeller();
        File f = new File(seller.getUsername() + ".csv");

        ArrayList<String> checker = new ArrayList<>();
        checker.add("hammer,bangs nails into walls,12,10.0");
        checker.add("nails,holds things together,100,0.05");
        checker.add("drill,automated way to attach nails,6,40.0");

        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            ArrayList<String> lines = new ArrayList<>();

            String line = bfr.readLine();
            while (line != null) {
                lines.add(line);
                line = bfr.readLine();
            }

            for(int i = 0; i < lines.size(); i++) {
                Assert.assertEquals(checker.get(i), lines.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f2 = new File("userData");
        int size = 0;
        try {
            FileReader fr = new FileReader(f2);
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

        seller.pushToFile();

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
            Assert.assertEquals("S:johndoe;johndoe@gmail.com;password", lines.get(lines.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }



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

        Product p = new Product("juice", "sweet and fruity drink", 5.00, 12);
        Product a = new Product("apple", "a popular fruit", 1.00, 7);
        Product c = new Product("candy", "a sweet and tasty snack", 10.00, 5);

        ArrayList<Product> allProducts = new ArrayList<>();

        ArrayList<Product> products = new ArrayList<>();
        allProducts.add(p);
        products.add(p);
        allProducts.add(a);
        products.add(a);
        allProducts.add(c);
        products.add(c);

        Store s = new Store("Safeway", "Food and Drinks", products);

        Product h = new Product("hammer", "bangs nails into walls", 10.00, 12);
        Product n = new Product("nails", "holds things together", 0.05, 100);
        Product d = new Product("drill", "automated way to attach nails", 40.00, 6);

        ArrayList<Product> products2 = new ArrayList<>();
        allProducts.add(h);
        products2.add(h);
        allProducts.add(n);
        products2.add(n);
        allProducts.add(d);
        products2.add(d);

        Store s2 = new Store("Home Depot", "Hardware", products2);
        Store s3 = new Store("Lowe's", "Hardware", products2);

        ArrayList<Store> stores = new ArrayList<>();
        stores.add(s);
        stores.add(s2);
        stores.add(s3);

        ArrayList<Store> allStores = stores;
        allStores.add(s);
        allStores.add(s2);
        allStores.add(s3);

        Seller seller = new Seller("johndoe", "johndoe@gmail.com", "password", stores);
        Seller seller2 = new Seller("janedoe", "johndoe@gmail.com", "password", stores);

        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        sellers.add(seller2);

        MarketPlace mp = new MarketPlace(sellers, customers);

        Assert.assertEquals(sellers, mp.getSellers());
        Assert.assertEquals(customers, mp.getCustomers());
        Assert.assertEquals(allStores, mp.getStores());
        Assert.assertEquals(allProducts, mp.getProducts());

        ArrayList<Product> juice = new ArrayList<>();
        juice.add(p);
        ArrayList<Store> safeway = new ArrayList<>();
        safeway.add(s);
        Assert.assertEquals(juice, mp.searchProducts("name", "juice"));
        Assert.assertEquals(juice, mp.searchProducts("desc", "sweet and fruity"));
        Assert.assertEquals(safeway, mp.searchStores("Safe"));

    }

}
