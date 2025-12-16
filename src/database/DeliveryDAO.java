package database;

import model.Delivery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DeliveryDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas Delivery.
 * Menangani semua transaksi database untuk tabel Delivery.
 */
public class DeliveryDAO {
    private Connection connection;

    /**
     * Constructor untuk DeliveryDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public DeliveryDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk Delivery
     * Format: DELIV_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idDelivery, 7) AS UNSIGNED)) as maxId FROM Delivery";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("DELIV_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "DELIV_00001";
        }
    }
    
    /**
     * Mengecek apakah sebuah order sudah pernah di-assign ke courier.
     * Digunakan untuk mencegah pembuatan delivery ganda untuk order yang sama.
     *
     * @param orderId ID order yang ingin dicek
     * @return true jika order sudah memiliki delivery, false jika belum
     */
    public boolean isOrderAlreadyAssigned(String orderId) {
        String sql = "SELECT 1 FROM Delivery WHERE idOrder = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderId);
            // Eksekusi query
            ResultSet rs = ps.executeQuery();
         // Jika ada hasil (rs.next() == true), berarti order sudah pernah di-assign
            return rs.next();
        } catch (SQLException e) {
        	// Menangani error database (misalnya query gagal atau koneksi bermasalah)
            e.printStackTrace();
        }
     // Default: order belum pernah di-assign
        return false;
    }


    /**
     * Insert Delivery baru ke database
     *
     * @param delivery Delivery object yang akan disimpan
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean insertDelivery(Delivery delivery) {
        String sql = "INSERT INTO Delivery (idDelivery, idOrder, idCourier, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, delivery.getIdDelivery());
            ps.setString(2, delivery.getIdOrder());
            ps.setString(3, delivery.getIdCourier());
            ps.setString(4, delivery.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan Delivery berdasarkan idDelivery
     *
     * @param idDelivery ID dari Delivery yang dicari
     * @return Delivery object jika ditemukan, null jika tidak ditemukan
     */
    public Delivery getDeliveryById(String idDelivery) {
        String sql = "SELECT * FROM Delivery WHERE idDelivery = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDelivery);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Delivery(
                        rs.getString("idDelivery"),
                        rs.getString("idOrder"),
                        rs.getString("idCourier"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan Delivery berdasarkan idOrder
     *
     * @param idOrder ID dari Order yang dicari
     * @return Delivery object jika ditemukan, null jika tidak ditemukan
     */
    public Delivery getDeliveryByOrderId(String idOrder) {
        String sql = "SELECT * FROM Delivery WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Delivery(
                        rs.getString("idDelivery"),
                        rs.getString("idOrder"),
                        rs.getString("idCourier"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan semua Delivery berdasarkan idCourier
     *
     * @param idCourier ID dari Courier yang dicari
     * @return List<Delivery> daftar Delivery yang ditemukan
     */
    public List<Delivery> getDeliveriesByCourierId(String idCourier) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Delivery WHERE idCourier = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCourier);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deliveries.add(new Delivery(
                        rs.getString("idDelivery"),
                        rs.getString("idOrder"),
                        rs.getString("idCourier"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    /**
     * Mendapatkan semua Delivery dari database
     *
     * @return List<Delivery> daftar semua Delivery
     */
    public List<Delivery> getAllDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Delivery";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deliveries.add(new Delivery(
                        rs.getString("idDelivery"),
                        rs.getString("idOrder"),
                        rs.getString("idCourier"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    /**
     * Update data Delivery di database
     *
     * @param delivery Delivery object yang akan diupdate
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updateDelivery(Delivery delivery) {
        String sql = "UPDATE Delivery SET idOrder = ?, idCourier = ?, status = ? WHERE idDelivery = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, delivery.getIdOrder());
            ps.setString(2, delivery.getIdCourier());
            ps.setString(3, delivery.getStatus());
            ps.setString(4, delivery.getIdDelivery());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDeliveryStatus(String idDelivery, String status) {
        String sql = "UPDATE Delivery SET status = ? WHERE idDelivery = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, idDelivery);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDelivery(String idDelivery) {
        String sql = "DELETE FROM Delivery WHERE idDelivery = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idDelivery);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Delivery> getDeliveriesByStatus(String status) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM Delivery WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deliveries.add(new Delivery(
                        rs.getString("idDelivery"),
                        rs.getString("idOrder"),
                        rs.getString("idCourier"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
}
