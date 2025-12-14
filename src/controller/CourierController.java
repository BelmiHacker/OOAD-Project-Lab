package controller;

import model.Courier;

import java.util.List;

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
     * Mengambil seluruh ID Courier dari database.
     * Method ini biasanya digunakan untuk proses mapping
     * atau load data secara bertahap.
     *
     * @return List berisi idCourier
     */
    public List<String> getAllCourierIds() {
        return courierDAO.getAllCourierIds();
    }

    /**
     * Mengambil data Courier berdasarkan ID Courier.
     * yang digunakan dalam dokumentasi sistem.
     *
     * @param idCourier ID courier
     * @return objek Courier jika ditemukan, null jika tidak ditemukan
     */
    public Courier getCourier(String idCourier) {
        return courierDAO.getCourierById(idCourier);
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
