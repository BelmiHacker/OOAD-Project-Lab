package controller;

import model.Promo;
import database.PromoDAO;

/**
 * PromoController
 * 
 * Controller untuk menangani business logic terkait Promo/Diskon.
 * Bertanggung jawab untuk validasi promo dan kalkulasi diskon.
 * 
 */
public class PromoHandler {
    private PromoDAO promoDAO;

    /**
     * Constructor untuk PromoController
     * Menginisialisasi PromoDAO untuk akses database
     */
    public PromoHandler() {
        this.promoDAO = new PromoDAO();
    }

    /**
     * Mendapatkan promo berdasarkan kode
     * 
     * @param code Kode promo
     * @return Promo object jika ditemukan, null sebaliknya
     */
    public Promo getPromo(String code) {
        return promoDAO.getPromoByCode(code);
    }

    /**
     * Cek apakah promo dengan kode tertentu ada
     * 
     * @param code Kode promo
     * @return true jika promo ada, false sebaliknya
     */
    public boolean promoExists(String code) {
        return promoDAO.promoExists(code);
    }

    /**
     * Mendapatkan persentase diskon dari kode promo
     * 
     * @param code Kode promo
     * @return Persentase diskon
     */
    public double getDiscountPercentage(String code) {
        return promoDAO.getDiscountPercentage(code);
    }

    /**
     * Kalkulasi nilai diskon dari kode promo
     * 
     * @param code Kode promo
     * @param totalAmount Total amount yang akan didiskon
     * @return Nilai diskon, 0 jika promo tidak valid
     */
    public double calculateDiscount(String code, double totalAmount) {
        if (!promoExists(code)) {
            return 0;
        }

        double discountPercentage = getDiscountPercentage(code);
        return totalAmount * (discountPercentage / 100);
    }

    /**
     * Kalkulasi total amount setelah didiskon dengan promo
     * 
     * @param code Kode promo
     * @param totalAmount Total amount sebelum diskon
     * @return Total amount setelah diskon, atau original total jika promo tidak valid
     */
    public double calculateFinalAmount(String code, double totalAmount) {
        if (code == null || code.isEmpty()) {
            return totalAmount;
        }

        double discount = calculateDiscount(code, totalAmount);
        return totalAmount - discount;
    }

    /**
     * Mendapatkan semua promo
     * 
     * @return List semua promo di database
     */
    public java.util.List<Promo> getAllPromos() {
        return promoDAO.getAllPromos();
    }

    /**
     * Mendapatkan promo berdasarkan ID
     * 
     * @param idPromo ID promo
     * @return Promo object jika ditemukan, null sebaliknya
     */
    public Promo getPromoById(String idPromo) {
        return promoDAO.getPromoById(idPromo);
    }

    /**
     * Insert promo baru ke database
     * 
     * @param promo Promo object yang akan diinsert
     * @return "success" jika insert berhasil, pesan error sebaliknya
     */
    public String insertPromo(Promo promo) {
        if (promoDAO.insertPromo(promo)) {
            return "success";
        }
        return "Tambah promo gagal";
    }

    /**
     * Update data promo
     * 
     * @param promo Promo object dengan data yang akan diupdate
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updatePromo(Promo promo) {
        if (promoDAO.updatePromo(promo)) {
            return "success";
        }
        return "Update promo gagal";
    }

    /**
     * Hapus promo dari database
     * 
     * @param idPromo ID promo yang akan dihapus
     * @return "success" jika delete berhasil, pesan error sebaliknya
     */
    public String deletePromo(String idPromo) {
        if (promoDAO.deletePromo(idPromo)) {
            return "success";
        }
        return "Hapus promo gagal";
    }
}
