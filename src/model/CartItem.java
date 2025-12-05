package model;

/**
 * CartItem Model
 * 
 * Merepresentasikan item yang ada di keranjang belanja customer.
 * Menyimpan referensi produk dan jumlah yang dipilih.
 */
public class CartItem {
    private String idCartItem;     // ID unik cart item
    private String idCustomer;     // ID customer pemilik cart
    private String idProduct;      // ID produk dalam cart
    private int count;             // Jumlah produk dalam cart

    /**
     * Constructor default untuk CartItem
     */
    public CartItem() {
    }

    public CartItem(String idCartItem, String idCustomer, String idProduct, int count) {
        this.idCartItem = idCartItem;
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.count = count;
    }

    public String getIdCartItem() {
        return idCartItem;
    }

    public void setIdCartItem(String idCartItem) {
        this.idCartItem = idCartItem;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
