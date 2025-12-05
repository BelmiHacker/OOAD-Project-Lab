package controller;

import model.Courier;
import database.CourierDAO;

/**
 * CourierController
 * 
 * Controller untuk menangani business logic terkait Courier.
 * Bertanggung jawab untuk CRUD operation kurir dan delegasi ke CourierDAO.
 * 
 */
public class CourierController {
    private CourierDAO courierDAO;

    /**
     * Constructor untuk CourierController
     * Menginisialisasi CourierDAO untuk akses database
     */
    public CourierController() {
        this.courierDAO = new CourierDAO();
    }

    /**
     * Mendapatkan kurir berdasarkan ID
     * 
     * @param idCourier ID kurir yang dicari
     * @return Courier object jika ditemukan, null sebaliknya
     */
    public Courier getCourierById(String idCourier) {
        return courierDAO.getCourierById(idCourier);
    }

    /**
     * Mendapatkan kurir berdasarkan ID user
     * 
     * @param idUser ID user
     * @return Courier object jika ditemukan, null sebaliknya
     */
    public Courier getCourierByUserId(String idUser) {
        return courierDAO.getCourierByUserId(idUser);
    }

    /**
     * Mendapatkan semua kurir
     * 
     * @return List semua kurir di database
     */
    public java.util.List<Courier> getAllCouriers() {
        return courierDAO.getAllCouriers();
    }

    /**
     * Insert kurir baru ke database
     * 
     * @param courier Courier object yang akan diinsert
     * @return "success" jika insert berhasil, pesan error sebaliknya
     */
    public String insertCourier(Courier courier) {
        if (courierDAO.insertCourier(courier)) {
            return "success";
        }
        return "Tambah kurir gagal";
    }

    /**
     * Update data kurir
     * 
     * @param courier Courier object dengan data yang akan diupdate
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateCourier(Courier courier) {
        if (courierDAO.updateCourier(courier)) {
            return "success";
        }
        return "Update kurir gagal";
    }

    /**
     * Hapus kurir dari database
     * 
     * @param idCourier ID kurir yang akan dihapus
     * @return "success" jika delete berhasil, pesan error sebaliknya
     */
    public String deleteCourier(String idCourier) {
        if (courierDAO.deleteCourier(idCourier)) {
            return "success";
        }
        return "Hapus kurir gagal";
    }
}
