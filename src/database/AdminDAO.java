package database;
import model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas Admin.
 * Menangani semua transaksi database untuk tabel Admin.
 */
public class AdminDAO {
    private Connection connection;

    /**
     * Constructor untuk AdminDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public AdminDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk Admin
     * Format: ADM_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idAdmin, 5) AS UNSIGNED)) as maxId FROM Admin";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("ADM_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "ADM_00001";
        }
    }

    public boolean insertAdmin(Admin admin) {
        String sql = "INSERT INTO Admin (idAdmin, idUser, emergencyContact) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, admin.getIdAdmin());
            ps.setString(2, admin.getIdUser());
            ps.setString(3, admin.getEmergencyContact());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Admin getAdminById(String idAdmin) {
        String sql = "SELECT * FROM Admin WHERE idAdmin = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAdmin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getString("idAdmin"),
                        rs.getString("idUser"),
                        rs.getString("emergencyContact")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Admin getAdminByUserId(String idUser) {
        String sql = "SELECT * FROM Admin WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getString("idAdmin"),
                        rs.getString("idUser"),
                        rs.getString("emergencyContact")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM Admin";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                admins.add(new Admin(
                        rs.getString("idAdmin"),
                        rs.getString("idUser"),
                        rs.getString("emergencyContact")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE Admin SET idUser = ?, emergencyContact = ? WHERE idAdmin = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, admin.getIdUser());
            ps.setString(2, admin.getEmergencyContact());
            ps.setString(3, admin.getIdAdmin());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdmin(String idAdmin) {
        String sql = "DELETE FROM Admin WHERE idAdmin = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idAdmin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
