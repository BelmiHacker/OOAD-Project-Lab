package model;

import java.time.LocalDateTime;

/**
 * OrderHeader Model
 * 
 * Merepresentasikan header/ringkasan dari sebuah pesanan.
 * Menyimpan informasi utama order seperti customer, status, dan total.
 */
public class OrderHeader {
    private String idOrder;         // ID unik order
    private String idCustomer;      // ID customer pemesan
    private String idPromo;         // ID promo yang digunakan (nullable)
    private String status;          // Status order (pending/processing/delivered)
    private LocalDateTime orderedAt; // Waktu order dibuat
    private double totalAmount;     // Total harga order (Rp)

    /**
     * Constructor default untuk OrderHeader
     */
    public OrderHeader() {
    }

    public OrderHeader(String idOrder, String idCustomer, String idPromo, 
                       String status, LocalDateTime orderedAt, double totalAmount) {
        this.idOrder = idOrder;
        this.idCustomer = idCustomer;
        this.idPromo = idPromo;
        this.status = status;
        this.orderedAt = orderedAt;
        this.totalAmount = totalAmount;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(String idPromo) {
        this.idPromo = idPromo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
