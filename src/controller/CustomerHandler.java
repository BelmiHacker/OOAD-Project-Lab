package controller;

import model.Customer;
import database.CustomerDAO;

/**
 * CustomerController
 * 
 * Controller untuk menangani business logic terkait Customer.
 * Bertanggung jawab untuk validasi transaksi keuangan customer dan delegasi ke CustomerDAO.
 * 
 */
public class CustomerHandler {
    private CustomerDAO customerDAO;

    /**
     * Constructor untuk CustomerController
     * Menginisialisasi CustomerDAO untuk akses database
     */
    public CustomerHandler() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Top up saldo customer
     * Validasi memastikan nominal minimal 10.000 dan lebih dari 0
     * 
     * @param idCustomer ID customer yang akan top up
     * @param amount Jumlah nominal top up
     * @return "success" jika top up berhasil, pesan error sebaliknya
     */
    public String topUpBalance(String idCustomer, double amount) {
        if (amount <= 0) {
            return "Nominal harus lebih dari 0";
        }

        if (amount < 10000) {
            return "Minimal top up adalah 10.000";
        }

        if (customerDAO.topUpBalance(idCustomer, amount)) {
            return "success";
        }
        return "Top up gagal";
    }

    /**
     * Checkout/pembayaran pesanan
     * Memvalidasi saldo customer dan mendebit saldo sesuai total amount
     * 
     * @param idCustomer ID customer yang checkout
     * @param totalAmount Total amount yang harus dibayar
     * @param promoCode Kode promo (opsional)
     * @return "success" jika checkout berhasil, pesan error sebaliknya
     */
    public String checkout(String idCustomer, double totalAmount, String promoCode) {
        Customer customer = customerDAO.getCustomerById(idCustomer);
        if (customer == null) {
            return "Customer tidak ditemukan";
        }

        if (customer.getBalance() < totalAmount) {
            return "Saldo tidak mencukupi";
        }

        if (customerDAO.deductBalance(idCustomer, totalAmount)) {
            return "success";
        }
        return "Checkout gagal";
    }

    /**
     * Mendapatkan saldo customer
     * 
     * @param idCustomer ID customer
     * @return Saldo customer
     */
    public double getBalance(String idCustomer) {
        return customerDAO.getBalance(idCustomer);
    }

    /**
     * Mendapatkan data customer berdasarkan ID customer
     * 
     * @param idCustomer ID customer
     * @return Customer object jika ditemukan, null sebaliknya
     */
    public Customer getCustomer(String idCustomer) {
        return customerDAO.getCustomerById(idCustomer);
    }

    /**
     * Mendapatkan data customer berdasarkan ID user
     * 
     * @param idUser ID user
     * @return Customer object jika ditemukan, null sebaliknya
     */
    public Customer getCustomerByUserId(String idUser) {
        return customerDAO.getCustomerByUserId(idUser);
    }

    /**
     * Insert customer baru ke database
     *
     * @param customer Customer object yang akan diinsert
     * @return "success" jika insert berhasil, pesan error sebaliknya
     */
    //+registerAccount(fullName, email, password, phone, address)
    public String registerAccount(Customer customer) {
        if (customerDAO.insertCustomer(customer)) {
            return "success";
        }
        return "Tambah customer gagal";
    }

    /**
     * Generate unique ID untuk Customer
     * Format: CUST_XXXXX
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        return customerDAO.generateId();
    }

    /**
     * Create customer baru dengan balance awal
     * 
     * @param idCustomer ID customer baru
     * @param idUser ID user yang berelasi
     * @param initialBalance Saldo awal customer
     * @return "success" jika berhasil, pesan error sebaliknya
     */
    public String createCustomer(String idCustomer, String idUser, double initialBalance) {
        Customer customer = new Customer(idCustomer, idUser, initialBalance);
        return registerAccount(customer);
    }
}
