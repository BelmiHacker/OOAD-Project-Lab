package database;

import model.Courier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CourierDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas Courier.
 * Menangani semua transaksi database untuk tabel Courier.
 */
public class CourierDAO {
    private Connection connection;

    /**
     * Constructor untuk CourierDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public CourierDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk Courier
     * Format: COURIER_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idCourier, 9) AS UNSIGNED)) as maxId FROM Courier";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("COURIER_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "COURIER_00001";
        }
    }

    public boolean insertCourier(Courier courier) {
        String sql = "INSERT INTO Courier (idCourier, idUser, vehicleType, vehiclePlate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courier.getIdCourier());
            ps.setString(2, courier.getIdUser());
            ps.setString(3, courier.getVehicleType());
            ps.setString(4, courier.getVehiclePlate());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Courier getCourierById(String idCourier) {
        String sql = "SELECT * FROM Courier WHERE idCourier = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCourier);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Courier(
                        rs.getString("idCourier"),
                        rs.getString("idUser"),
                        rs.getString("vehicleType"),
                        rs.getString("vehiclePlate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Courier getCourierByUserId(String idUser) {
        String sql = "SELECT * FROM Courier WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Courier(
                        rs.getString("idCourier"),
                        rs.getString("idUser"),
                        rs.getString("vehicleType"),
                        rs.getString("vehiclePlate")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Courier> getAllCouriers() {
        List<Courier> couriers = new ArrayList<>();
        String sql = "SELECT * FROM Courier";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                couriers.add(new Courier(
                        rs.getString("idCourier"),
                        rs.getString("idUser"),
                        rs.getString("vehicleType"),
                        rs.getString("vehiclePlate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return couriers;
    }

    public boolean updateCourier(Courier courier) {
        String sql = "UPDATE Courier SET idUser = ?, vehicleType = ?, vehiclePlate = ? WHERE idCourier = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courier.getIdUser());
            ps.setString(2, courier.getVehicleType());
            ps.setString(3, courier.getVehiclePlate());
            ps.setString(4, courier.getIdCourier());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourier(String idCourier) {
        String sql = "DELETE FROM Courier WHERE idCourier = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCourier);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
