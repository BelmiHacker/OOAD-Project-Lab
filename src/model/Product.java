package model;

/**
 * Product Model
 * 
 * Merepresentasikan entitas Produk dalam sistem JoymarKet.
 * Menyimpan informasi produk yang dijual di marketplace.
 */
public class Product {
    private String idProduct;     // ID unik produk
    private String name;          // Nama produk
    private double price;         // Harga produk (Rp)
    private int stock;            // Stok produk
    private String category;      // Kategori produk

    /**
     * Constructor default untuk Product
     */
    public Product() {
    }

    public Product(String idProduct, String name, double price, int stock, String category) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
