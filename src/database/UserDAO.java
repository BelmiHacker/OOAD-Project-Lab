package database;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas User.
 * Menangani semua transaksi database untuk tabel User.
 */
public class UserDAO {
    private Connection connection;

    /**
     * Constructor untuk UserDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk User dengan prefix sesuai role
     * Format: USR_XXXXX (untuk customer), COUR_XXXXX (untuk courier), ADM_XXXXX (untuk admin)
     * 
     * @param role Role user (customer, courier, admin)
     * @return String ID yang unik
     */
    public String generateId(String role) {
        String prefix;
        switch (role.toLowerCase()) {
            case "courier":
                prefix = "COUR";
                break;
            case "admin":
                prefix = "ADM";
                break;
            case "customer":
            default:
                prefix = "USR";
                break;
        }
        
        String sql = "SELECT MAX(CAST(SUBSTRING(idUser, 6) AS UNSIGNED)) as maxId FROM User WHERE idUser LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("%s_%05d", prefix, nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return prefix + "_00001";
        }
    }

    /**
     * Generate unique ID untuk User (default untuk backward compatibility)
     * Format: USER_XXXXX
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        return generateId("customer");
    }

    /**
     * Insert user baru ke database
     * 
     * @param user User object yang akan diinsert
     * @return true jika berhasil, false sebaliknya
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO User (idUser, fullName, email, password, phone, address, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getIdUser());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get user by ID
     *
     * @param idUser ID user yang dicari
     * @return User object jika ditemukan, null jika tidak ditemukan
     */
    public User getUserById(String idUser) {
        String sql = "SELECT * FROM User WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("idUser"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get user by Email
     *
     * @param email Email user yang dicari
     * @return User object jika ditemukan, null jika tidak ditemukan
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("idUser"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get semua user dari database
     *
     * @return List<User> daftar semua user
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("idUser"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Update data user di database
     *
     * @param user User object dengan data yang sudah diperbarui
     * @return true jika berhasil, false sebaliknya
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE User SET fullName = ?, email = ?, password = ?, phone = ?, address = ?, role = ? WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getIdUser());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete user dari database
     *
     * @param idUser ID user yang akan dihapus
     * @return true jika berhasil, false sebaliknya
     */
    public boolean deleteUser(String idUser) {
        String sql = "DELETE FROM User WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cek apakah email sudah terdaftar di database
     *
     * @param email Email yang akan dicek
     * @return true jika email sudah ada, false jika belum
     */
    public boolean emailExists(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cek apakah ID user ada di database
     *
     * @param idUser ID user yang akan dicek
     * @return true jika ID user ada, false jika tidak ada
     */
    public boolean idExists(String idUser) {
        String sql = "SELECT * FROM User WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
