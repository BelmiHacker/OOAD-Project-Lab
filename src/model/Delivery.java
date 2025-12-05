package model;

/**
 * Delivery Model
 * 
 * Merepresentasikan entitas Pengiriman dalam sistem JoymarKet.
 * Menyimpan informasi pengiriman pesanan termasuk kurir dan status.
 */
public class Delivery {
    private String idDelivery;     // ID unik delivery
    private String idOrder;        // ID order yang dikirim
    private String idCourier;      // ID kurir yang bertugas
    private String status;         // Status pengiriman (pending/in progress/delivered)

    /**
     * Constructor default untuk Delivery
     */
    public Delivery() {
    }

    public Delivery(String idDelivery, String idOrder, String idCourier, String status) {
        this.idDelivery = idDelivery;
        this.idOrder = idOrder;
        this.idCourier = idCourier;
        this.status = status;
    }

    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdCourier() {
        return idCourier;
    }

    public void setIdCourier(String idCourier) {
        this.idCourier = idCourier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
