class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");

        Store store = new Store("Denny's", "it's... Denny's");
        store.addProduct(new Product("Gamer Eggs", "Nature's Protein Shake", store, 19.99));


        System.out.print("\n\n\n");
        Seller user = new Seller("nathanWhitacre", "whitacre@purdue.edu", "gamerGod88");

        Store store = new Store("God's Soap Emporium", "Got bored, like soap - God", user);
        ArrayList<Store> stores = new ArrayList<>();
        stores.add(store);
        user.setStores(stores);

        Product product = new Product("Dawn PowerWash Dish Spray", "clean up you gross ass plate", store, 19.99);
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        store.setProducts(products);

        System.out.printf("%s presents,\na brand new %s product:\n%s\n", user.getUsername(), user.getStores().get(0).getName(), user.getStores().get(0).getProducts().get(0).getName());
    }
}