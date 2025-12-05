package controller;

import model.Product;
import database.ProductDAO;

/**
 * ProductController
 * 
 * Controller untuk menangani business logic terkait Product.
 * Bertanggung jawab untuk validasi data produk dan delegasi ke ProductDAO.
 * 
 */
public class ProductController {
    private ProductDAO productDAO;

    /**
     * Constructor untuk ProductController
     * Menginisialisasi ProductDAO untuk akses database
     */
    public ProductController() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Update stok produk
     * Validasi memastikan stok tidak negatif
     * 
     * @param idProduct ID produk yang stoknya akan diupdate
     * @param newStock Nilai stok baru
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateStock(String idProduct, int newStock) {
        if (newStock < 0) {
            return "Stok tidak boleh negatif";
        }

        if (productDAO.updateStock(idProduct, newStock)) {
            return "success";
        }
        return "Update stok gagal";
    }

    /**
     * Mendapatkan produk berdasarkan ID
     * 
     * @param idProduct ID produk yang dicari
     * @return Product object jika ditemukan, null sebaliknya
     */
    public Product getProductById(String idProduct) {
        return productDAO.getProductById(idProduct);
    }

    /**
     * Mendapatkan semua produk
     * 
     * @return List semua produk di database
     */
    public java.util.List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Mendapatkan produk berdasarkan kategori
     * 
     * @param category Kategori produk
     * @return List produk dengan kategori yang sesuai
     */
    public java.util.List<Product> getProductsByCategory(String category) {
        return productDAO.getProductsByCategory(category);
    }

    /**
     * Mendapatkan stok produk
     * 
     * @param idProduct ID produk
     * @return Jumlah stok produk
     */
    public int getStock(String idProduct) {
        return productDAO.getStock(idProduct);
    }

    /**
     * Mendapatkan harga produk
     * 
     * @param idProduct ID produk
     * @return Harga produk
     */
    public double getPrice(String idProduct) {
        return productDAO.getPrice(idProduct);
    }

    /**
     * Insert produk baru ke database
     * 
     * @param product Product object yang akan diinsert
     * @return "success" jika insert berhasil, pesan error sebaliknya
     */
    public String insertProduct(Product product) {
        if (productDAO.insertProduct(product)) {
            return "success";
        }
        return "Tambah produk gagal";
    }

    /**
     * Update data produk
     * 
     * @param product Product object dengan data yang akan diupdate
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateProduct(Product product) {
        if (productDAO.updateProduct(product)) {
            return "success";
        }
        return "Update produk gagal";
    }
}
