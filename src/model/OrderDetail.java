package model;

/**
 * OrderDetail Model
 * 
 * Merepresentasikan detail/item dari sebuah pesanan.
 * Menyimpan informasi produk dan kuantitas dalam order.
 */
public class OrderDetail {
    private String idOrderDetail;   // ID unik order detail
    private String idOrder;         // ID order yang terkait
    private String idProduct;       // ID produk dalam order
    private int qty;                // Kuantitas produk

    /**
     * Constructor default untuk OrderDetail
     */
    public OrderDetail() {
    }

    public OrderDetail(String idOrderDetail, String idOrder, String idProduct, int qty) {
        this.idOrderDetail = idOrderDetail;
        this.idOrder = idOrder;
        this.idProduct = idProduct;
        this.qty = qty;
    }

    public String getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(String idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
