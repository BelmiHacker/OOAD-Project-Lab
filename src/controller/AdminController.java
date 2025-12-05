package controller;

import model.Admin;
import database.AdminDAO;

/**
 * AdminController
 * 
 * Controller untuk menangani business logic terkait Admin.
 * Bertanggung jawab untuk CRUD operation admin dan delegasi ke AdminDAO.
 * 
 */
public class AdminController {
    private AdminDAO adminDAO;

    /**
     * Constructor untuk AdminController
     * Menginisialisasi AdminDAO untuk akses database
     */
    public AdminController() {
        this.adminDAO = new AdminDAO();
    }

    /**
     * Mendapatkan admin berdasarkan ID
     * 
     * @param idAdmin ID admin yang dicari
     * @return Admin object jika ditemukan, null sebaliknya
     */
    public Admin getAdminById(String idAdmin) {
        return adminDAO.getAdminById(idAdmin);
    }

    /**
     * Mendapatkan admin berdasarkan ID user
     * 
     * @param idUser ID user
     * @return Admin object jika ditemukan, null sebaliknya
     */
    public Admin getAdminByUserId(String idUser) {
        return adminDAO.getAdminByUserId(idUser);
    }

    /**
     * Mendapatkan semua admin
     * 
     * @return List semua admin di database
     */
    public java.util.List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    /**
     * Insert admin baru ke database
     * 
     * @param admin Admin object yang akan diinsert
     * @return "success" jika insert berhasil, pesan error sebaliknya
     */
    public String insertAdmin(Admin admin) {
        if (adminDAO.insertAdmin(admin)) {
            return "success";
        }
        return "Tambah admin gagal";
    }

    /**
     * Update data admin
     * 
     * @param admin Admin object dengan data yang akan diupdate
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateAdmin(Admin admin) {
        if (adminDAO.updateAdmin(admin)) {
            return "success";
        }
        return "Update admin gagal";
    }

    /**
     * Hapus admin dari database
     * 
     * @param idAdmin ID admin yang akan dihapus
     * @return "success" jika delete berhasil, pesan error sebaliknya
     */
    public String deleteAdmin(String idAdmin) {
        if (adminDAO.deleteAdmin(idAdmin)) {
            return "success";
        }
        return "Hapus admin gagal";
    }
}
