import java.util.*;


class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        Store store = new Store("Denny's", "it's... Denny's");
        store.addProduct(new Product("Gamer Eggs", "Nature's Protein Shake", 19.99));
        store.addProduct(new Product("Gamer Sausage", "Nature's Protein Shake", 19.99));
        store.addProduct(new Product("Gamer Pancakes", "Nature's Protein Shake", 19.99));

        new Product("Gamer Eggs", "Nature's Protein Shake", 19.99).pushToFile();
        new Product("Gamer Sausage", "Nature's Protein Shake", 19.99).pushToFile();
        new Product("Gamer Pancakes", "Nature's Protein Shake", 19.99).pushToFile();
        new Product("Gamer Waffles", "Nature's Protein Shake", 19.99).pushToFile();


        System.out.print("\n\n\n");
        Seller user = new Seller("nathanWhitacre", "whitacre@purdue.edu", "gamerGod88");

        Store store2 = new Store("God's Soap Emporium", "Got bored, like soap - God");
        ArrayList<Store> stores = new ArrayList<>();
        stores.add(store2);
        user.setStores(stores);

        Product product = new Product("Dawn PowerWash Dish Spray", "clean up you gross ass plate", 19.99);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        store2.setProducts(products);

        store.pushToFile();
        store2.pushToFile();

        System.out.printf("%s presents,\na brand new %s product:\n%s\n", user.getUsername(), user.getStores().get(0).getName(), user.getStores().get(0).getProducts().get(0).getName());
    }
}