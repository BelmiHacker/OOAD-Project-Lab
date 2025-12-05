package database;

import model.OrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDetailDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas OrderDetail.
 * Menangani semua transaksi database untuk tabel OrderDetail.
 */
public class OrderDetailDAO {
    private Connection connection;

    /**
     * Constructor untuk OrderDetailDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public OrderDetailDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk OrderDetail
     * Format: DETAIL_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idOrderDetail, 8) AS UNSIGNED)) as maxId FROM OrderDetail";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("DETAIL_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "DETAIL_00001";
        }
    }

    public boolean insertOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO OrderDetail (idOrderDetail, idOrder, idProduct, qty) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderDetail.getIdOrderDetail());
            ps.setString(2, orderDetail.getIdOrder());
            ps.setString(3, orderDetail.getIdProduct());
            ps.setInt(4, orderDetail.getQty());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public OrderDetail getOrderDetailById(String idOrderDetail) {
        String sql = "SELECT * FROM OrderDetail WHERE idOrderDetail = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrderDetail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new OrderDetail(
                        rs.getString("idOrderDetail"),
                        rs.getString("idOrder"),
                        rs.getString("idProduct"),
                        rs.getInt("qty")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDetail> getOrderDetailsByOrderId(String idOrder) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderDetails.add(new OrderDetail(
                        rs.getString("idOrderDetail"),
                        rs.getString("idOrder"),
                        rs.getString("idProduct"),
                        rs.getInt("qty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderDetails.add(new OrderDetail(
                        rs.getString("idOrderDetail"),
                        rs.getString("idOrder"),
                        rs.getString("idProduct"),
                        rs.getInt("qty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public boolean updateOrderDetail(OrderDetail orderDetail) {
        String sql = "UPDATE OrderDetail SET idOrder = ?, idProduct = ?, qty = ? WHERE idOrderDetail = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderDetail.getIdOrder());
            ps.setString(2, orderDetail.getIdProduct());
            ps.setInt(3, orderDetail.getQty());
            ps.setString(4, orderDetail.getIdOrderDetail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderDetail(String idOrderDetail) {
        String sql = "DELETE FROM OrderDetail WHERE idOrderDetail = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrderDetail);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderDetailByOrderId(String idOrder) {
        String sql = "DELETE FROM OrderDetail WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
