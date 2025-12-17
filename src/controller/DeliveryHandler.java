package controller;

import model.Delivery;
import database.DeliveryDAO;

/**
 * DeliveryController
 * 
 * Controller untuk menangani business logic terkait Delivery.
 * Bertanggung jawab untuk manajemen pengiriman dan validasi status pengiriman.
 * 
 */
public class DeliveryHandler {
    private DeliveryDAO deliveryDAO;

    /**
     * Constructor untuk DeliveryController
     * Menginisialisasi DeliveryDAO untuk akses database
     */
    public DeliveryHandler() {
        this.deliveryDAO = new DeliveryDAO();
    }
    
    public boolean isOrderAlreadyAssigned(String orderId) {
        return deliveryDAO.isOrderAlreadyAssigned(orderId);
    }

    /**
     * Assign kurir untuk pengiriman
     * Validasi memastikan kurir dipilih sebelum assign
     * 
     * @param idDelivery ID delivery yang unik
     * @param idOrder ID order yang akan dikirim
     * @param idCourier ID kurir yang ditugaskan
     * @return "success" jika assign berhasil, pesan error sebaliknya
     */
    public String assignCourier(String idDelivery, String idOrder, String idCourier) {
        if (idCourier == null || idCourier.isEmpty()) {
            return "Kurir harus dipilih";
        }

        // CEK DOUBLE ASSIGN (server-side validation)
        if (deliveryDAO.isOrderAlreadyAssigned(idOrder)) {
            return "Order sudah pernah di-assign";
        }

        Delivery delivery = new Delivery(idDelivery, idOrder, idCourier, "pending");
        if (deliveryDAO.insertDelivery(delivery)) {
            return "success";
        }
        return "Assign kurir gagal";
    }


    /**
     * Update status pengiriman
     * Status yang valid: pending, in progress, delivered
     * 
     * @param idDelivery ID delivery
     * @param status Status baru (pending/in progress/delivered)
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateDeliveryStatus(String idDelivery, String status) {
        if (status == null || status.isEmpty()) {
            return "Status harus dipilih";
        }

        if (!status.equals("pending") && !status.equals("in progress") && !status.equals("delivered")) {
            return "Status harus Pending, In Progress, atau Delivered";
        }

        if (deliveryDAO.updateDeliveryStatus(idDelivery, status)) {
            return "success";
        }
        return "Update status pengiriman gagal";
    }
    
    

    /**
     * Mendapatkan delivery berdasarkan ID
     * 
     * @param idDelivery ID delivery
     * @return Delivery object jika ditemukan, null sebaliknya
     */
    public Delivery getDeliveryById(String idDelivery) {
        return deliveryDAO.getDeliveryById(idDelivery);
    }

    /**
     * Mendapatkan delivery berdasarkan ID order
     * 
     * @param idOrder ID order
     * @return Delivery object jika ditemukan, null sebaliknya
     */
    public Delivery getDeliveryByOrderId(String idOrder) {
        return deliveryDAO.getDeliveryByOrderId(idOrder);
    }

    /**
     * Mendapatkan semua delivery yang ditugaskan ke kurir tertentu
     * 
     * @param idCourier ID kurir
     * @return List delivery yang ditugaskan ke kurir
     */
    public java.util.List<Delivery> getDeliveriesByCourierId(String idCourier) {
        return deliveryDAO.getDeliveriesByCourierId(idCourier);
    }

    /**
     * Mendapatkan semua delivery
     * 
     * @return List semua delivery di database
     */
    public java.util.List<Delivery> getAllDeliveries() {
        return deliveryDAO.getAllDeliveries();
    }

    /**
     * Mendapatkan delivery berdasarkan status
     * 
     * @param status Status pengiriman
     * @return List delivery dengan status tertentu
     */
    public java.util.List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryDAO.getDeliveriesByStatus(status);
    }
}
