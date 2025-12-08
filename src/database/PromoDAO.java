package database;

import model.Promo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PromoDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas Promo.
 * Menangani semua transaksi database untuk tabel Promo.
 */
public class PromoDAO {
    private Connection connection;

    /**
     * Constructor untuk PromoDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public PromoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk Promo
     * Format: PROMO_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idPromo, 7) AS UNSIGNED)) as maxId FROM Promo";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("PROMO_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "PROMO_00001";
        }
    }

    /**
     * Insert Promo baru ke database
     *
     * @param promo Promo object yang akan disimpan
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean insertPromo(Promo promo) {
        String sql = "INSERT INTO Promo (idPromo, code, discountPercentage, headline) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, promo.getIdPromo());
            ps.setString(2, promo.getCode());
            ps.setDouble(3, promo.getDiscountPercentage());
            ps.setString(4, promo.getHeadline());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get Promo by ID
     *
     * @param idPromo ID dari Promo yang dicari
     * @return Promo object jika ditemukan, null jika tidak ditemukan
     */
    public Promo getPromoById(String idPromo) {
        String sql = "SELECT * FROM Promo WHERE idPromo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPromo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Promo(
                        rs.getString("idPromo"),
                        rs.getString("code"),
                        rs.getDouble("discountPercentage"),
                        rs.getString("headline")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Promo by Code
     *
     * @param code Code dari Promo yang dicari
     * @return Promo object jika ditemukan, null jika tidak ditemukan
     */
    public Promo getPromoByCode(String code) {
        String sql = "SELECT * FROM Promo WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Promo(
                        rs.getString("idPromo"),
                        rs.getString("code"),
                        rs.getDouble("discountPercentage"),
                        rs.getString("headline")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get semua Promo dari database
     *
     * @return List<Promo> daftar semua Promo
     */
    public List<Promo> getAllPromos() {
        List<Promo> promos = new ArrayList<>();
        String sql = "SELECT * FROM Promo";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                promos.add(new Promo(
                        rs.getString("idPromo"),
                        rs.getString("code"),
                        rs.getDouble("discountPercentage"),
                        rs.getString("headline")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promos;
    }

    /**
     * Update data Promo di database
     *
     * @param promo Promo object yang akan diupdate
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updatePromo(Promo promo) {
        String sql = "UPDATE Promo SET code = ?, discountPercentage = ?, headline = ? WHERE idPromo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, promo.getCode());
            ps.setDouble(2, promo.getDiscountPercentage());
            ps.setString(3, promo.getHeadline());
            ps.setString(4, promo.getIdPromo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete Promo dari database
     *
     * @param idPromo ID dari Promo yang akan dihapus
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean deletePromo(String idPromo) {
        String sql = "DELETE FROM Promo WHERE idPromo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idPromo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cek apakah promo dengan kode tertentu ada di database
     *
     * @param code Kode promo yang akan dicek
     * @return boolean true jika ada, false jika tidak ada
     */
    public boolean promoExists(String code) {
        String sql = "SELECT * FROM Promo WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan persentase diskon dari kode promo
     *
     * @param code Kode promo
     * @return double persentase diskon, atau 0 jika kode tidak ditemukan
     */
    public double getDiscountPercentage(String code) {
        String sql = "SELECT discountPercentage FROM Promo WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("discountPercentage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
