package database;

import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas Customer.
 * Menangani semua transaksi database untuk tabel Customer termasuk manajemen saldo.
 */
public class CustomerDAO {
    private Connection connection;

    /**
     * Constructor untuk CustomerDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public CustomerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk Customer
     * Format: CUST_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idCustomer, 6) AS UNSIGNED)) as maxId FROM Customer";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("CUST_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "CUST_00001";
        }
    }

    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (idCustomer, idUser, balance) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getIdCustomer());
            ps.setString(2, customer.getIdUser());
            ps.setDouble(3, customer.getBalance());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerById(String idCustomer) {
        String sql = "SELECT * FROM Customer WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("idCustomer"),
                        rs.getString("idUser"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer getCustomerByUserId(String idUser) {
        String sql = "SELECT * FROM Customer WHERE idUser = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("idCustomer"),
                        rs.getString("idUser"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getString("idCustomer"),
                        rs.getString("idUser"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET idUser = ?, balance = ? WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getIdUser());
            ps.setDouble(2, customer.getBalance());
            ps.setString(3, customer.getIdCustomer());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String idCustomer) {
        String sql = "DELETE FROM Customer WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean topUpBalance(String idCustomer, double amount) {
        String sql = "UPDATE Customer SET balance = balance + ? WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deductBalance(String idCustomer, double amount) {
        String sql = "UPDATE Customer SET balance = balance - ? WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(String idCustomer) {
        String sql = "SELECT balance FROM Customer WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
