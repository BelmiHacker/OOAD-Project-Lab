package controller;

import model.OrderHeader;
import model.OrderDetail;
import database.OrderHeaderDAO;
import database.OrderDetailDAO;
import database.CartItemDAO;
import database.CustomerDAO;
import database.PromoDAO;
import model.Product;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderController
 * 
 * Controller untuk menangani business logic terkait Order.
 * Bertanggung jawab untuk manajemen pesanan, checkout, dan validasi transaksi.
 * 
 */
public class OrderHandler {
    private OrderHeaderDAO orderHeaderDAO;
    private OrderDetailDAO orderDetailDAO;
    private CartItemDAO cartItemDAO;
    private CustomerDAO customerDAO;
    private ProductHandler productHandler;
    private PromoHandler promoHandler;

    /**
     * Constructor untuk OrderController
     * Menginisialisasi semua DAO yang diperlukan untuk operasi order
     */
    public OrderHandler() {
        this.orderHeaderDAO = new OrderHeaderDAO();
        this.orderDetailDAO = new OrderDetailDAO();
        this.cartItemDAO = new CartItemDAO();
        this.customerDAO = new CustomerDAO();
        this.productHandler = new ProductHandler();
        this.promoHandler = new PromoHandler();
    }

    /**
     * Checkout/create pesanan baru
     * Memvalidasi saldo customer, promo jika ada, dan mendebit saldo
     * Setelah checkout sukses, cart item customer akan dihapus
     * 
     * @param idOrder ID order yang unik
     * @param idOrderDetail ID order detail yang unik
     * @param idCustomer ID customer yang checkout
     * @param idProduct ID produk yang dipesan
     * @param promoCode Kode promo (opsional, boleh null)
     * @param totalAmount Total amount yang harus dibayar
     * @param qty Jumlah item yang dipesan
     * @return "success" jika checkout berhasil, pesan error sebaliknya
     */
    public String checkout(String idOrder, String idOrderDetail, String idCustomer, String idProduct, String promoCode, double totalAmount, int qty) {
        // Validasi input
        if (idOrder == null || idOrder.isEmpty()) {
            return "Order ID tidak boleh kosong";
        }

        if (idCustomer == null || idCustomer.isEmpty()) {
            return "Customer ID tidak boleh kosong";
        }

        if (totalAmount <= 0) {
            return "Total harus lebih dari 0";
        }

        double customerBalance = customerDAO.getBalance(idCustomer);
        if (customerBalance < totalAmount) {
            return "Saldo tidak mencukupi";
        }

        // Get product berdasarkan idProduct
        Product currentProduct = productHandler.getProduct(idProduct);

        // Get promo ID berdasarkan kode promo jika ada
        String idPromo = promoCode != null ? promoHandler.getPromo(promoCode).getIdPromo() : null;

        // Buat order header baru
        OrderHeader orderHeader = new OrderHeader(idOrder, idCustomer, idPromo, "pending",
                                                   LocalDateTime.now(), totalAmount);

        if (!orderHeaderDAO.insertOrderHeader(orderHeader)) {
            return "Checkout gagal";
        }

        // Simpan data order header
        if (!orderHeaderDAO.saveDataOrderHeader(idOrderDetail, idOrder, idProduct, qty)) {
            return "Checkout gagal saat menyimpan data order";
        }

        // Update stock product
        if (!productHandler.editProductStock(idProduct, currentProduct.getStock() - qty).equals("success")) {
            return "Checkout gagal saat mengupdate stok produk";
        }

        cartItemDAO.deleteCartItemByCustomerId(idCustomer);
        customerDAO.deductBalance(idCustomer, totalAmount);
        return "success";
    }

    /**
     * Tambahkan detail item ke order
     * Validasi memastikan quantity lebih dari 0
     * 
     * @param idOrderDetail ID order detail yang unik
     * @param idOrder ID order
     * @param idProduct ID produk yang dipesan
     * @param qty Jumlah produk yang dipesan
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String addOrderDetail(String idOrderDetail, String idOrder, String idProduct, int qty) {
        if (qty <= 0) {
            return "Qty harus lebih dari 0";
        }

        OrderDetail orderDetail = new OrderDetail(idOrderDetail, idOrder, idProduct, qty);
        if (orderDetailDAO.insertOrderDetail(orderDetail)) {
            return "success";
        }
        return "Tambah detail order gagal";
    }

    /**
     * Mendapatkan order header milik customer berdasarkan ID order
     *
     * @param idOrder ID order
     * @param idCustomer ID customer
     * @return OrderHeader object jika ditemukan, null sebaliknya
     */
    public OrderHeader getCustomerOrderHeader(String idOrder, String idCustomer) {
        return orderHeaderDAO.getOrderHeaderById(idOrder, idCustomer);
    }

    /**
     * Mendapatkan semua order milik customer
     * 
     * @param idCustomer ID customer
     * @return List order yang dipesan oleh customer
     */
    public List<OrderHeader> getCustomerOrders(String idCustomer) {
        return orderHeaderDAO.getOrderHeadersByCustomerId(idCustomer);
    }

    /**
     * Mendapatkan semua order
     * 
     * @return List semua order di database
     */
    public List<OrderHeader> getAllOrders() {
        return orderHeaderDAO.getAllOrderHeaders();
    }

    /**
     * Mendapatkan detail items dari order
     * 
     * @param idOrder ID order
     * @return List detail items dari order
     */
    public List<OrderDetail> getOrderDetails(String idOrder) {
        return orderDetailDAO.getOrderDetailsByOrderId(idOrder);
    }

    /**
     * Update status order
     * 
     * @param idOrder ID order
     * @param status Status baru order
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String editOrderHeaderStatus(String idOrder, String status) {
        if (orderHeaderDAO.updateOrderStatus(idOrder, status)) {
            return "success";
        }
        return "Update status order gagal";
    }

    /**
     * Mendapatkan order berdasarkan status
     * 
     * @param status Status order
     * @return List order dengan status tertentu
     */
    public List<OrderHeader> getOrdersByStatus(String status) {
        return orderHeaderDAO.getOrderHeadersByStatus(status);
    }
}
