package controller;

import model.CartItem;
import database.CartItemDAO;
import database.ProductDAO;

/**
 * CartItemController
 * 
 * Controller untuk menangani business logic terkait Cart Item.
 * Bertanggung jawab untuk validasi dan manajemen item dalam keranjang belanja customer.
 * 
 */
public class CartItemController {
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;

    /**
     * Constructor untuk CartItemController
     * Menginisialisasi CartItemDAO dan ProductDAO untuk akses database
     */
    public CartItemController() {
        this.cartItemDAO = new CartItemDAO();
        this.productDAO = new ProductDAO();
    }

    /**
     * Tambahkan produk ke keranjang belanja
     * Validasi memastikan jumlah produk valid dan stok tersedia
     * 
     * @param idCartItem ID cart item unik
     * @param idCustomer ID customer yang menambahkan ke cart
     * @param idProduct ID produk yang ditambahkan
     * @param count Jumlah produk yang ditambahkan
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String addProductToCart(String idCartItem, String idCustomer, String idProduct, int count) {
        if (count <= 0) {
            return "Jumlah harus lebih dari 0";
        }

        int availableStock = productDAO.getStock(idProduct);
        if (count > availableStock) {
            return "Jumlah melebihi stok yang tersedia";
        }

        CartItem cartItem = new CartItem(idCartItem, idCustomer, idProduct, count);
        if (cartItemDAO.insertCartItem(cartItem)) {
            return "success";
        }
        return "Tambah ke cart gagal";
    }

    /**
     * Update jumlah produk dalam cart item
     * Validasi memastikan jumlah valid dan stok tersedia
     * 
     * @param idCartItem ID cart item yang akan diupdate
     * @param newCount Jumlah baru
     * @param idProduct ID produk di cart item
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String updateCartItemCount(String idCartItem, int newCount, String idProduct) {
        if (newCount <= 0) {
            return "Jumlah harus lebih dari 0";
        }

        int availableStock = productDAO.getStock(idProduct);
        if (newCount > availableStock) {
            return "Jumlah melebihi stok yang tersedia";
        }

        if (cartItemDAO.updateCount(idCartItem, newCount)) {
            return "success";
        }
        return "Update cart gagal";
    }

    /**
     * Mendapatkan cart item berdasarkan ID
     * 
     * @param idCartItem ID cart item
     * @return CartItem object jika ditemukan, null sebaliknya
     */
    public CartItem getCartItemById(String idCartItem) {
        return cartItemDAO.getCartItemById(idCartItem);
    }

    /**
     * Mendapatkan cart item berdasarkan ID customer
     * 
     * @param idCustomer ID customer
     * @return CartItem object jika ditemukan, null sebaliknya
     */
    public CartItem getCartItemByCustomerId(String idCustomer) {
        return cartItemDAO.getCartItemByCustomerId(idCustomer);
    }

    /**
     * Mendapatkan semua cart item
     * 
     * @return List of CartItem objects
     */
    public java.util.List<CartItem> getAllCartItems() {
        return cartItemDAO.getAllCartItems();
    }

    /**
     * Hapus satu cart item
     * 
     * @param idCartItem ID cart item yang akan dihapus
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String deleteCartItem(String idCartItem) {
        if (cartItemDAO.deleteCartItem(idCartItem)) {
            return "success";
        }
        return "Hapus dari cart gagal";
    }

    /**
     * Hapus semua cart item milik customer
     * 
     * @param idCustomer ID customer
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String deleteCartItemByCustomerId(String idCustomer) {
        if (cartItemDAO.deleteCartItemByCustomerId(idCustomer)) {
            return "success";
        }
        return "Hapus cart gagal";
    }
}
