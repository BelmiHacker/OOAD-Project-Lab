package database;

import model.OrderHeader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderHeaderDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas OrderHeader.
 * Menangani semua transaksi database untuk tabel OrderHeader.
 */
public class OrderHeaderDAO {
    private Connection connection;

    /**
     * Constructor untuk OrderHeaderDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public OrderHeaderDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk OrderHeader
     * Format: ORDER_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idOrder, 7) AS UNSIGNED)) as maxId FROM OrderHeader";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("ORDER_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "ORDER_00001";
        }
    }

    /**
     * Insert OrderHeader baru ke database
     *
     * @param orderHeader OrderHeader object yang akan disimpan
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean saveDataOrderHeader(OrderHeader orderHeader) {
        String sqlInsert = "INSERT INTO OrderHeader (idOrder, idCustomer, idPromo, status, orderedAt, totalAmount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlInsert)) {
            ps.setString(1, orderHeader.getIdOrder());
            ps.setString(2, orderHeader.getIdCustomer());
            ps.setString(3, orderHeader.getIdPromo());
            ps.setString(4, orderHeader.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(orderHeader.getOrderedAt()));
            ps.setDouble(6, orderHeader.getTotalAmount());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update stok produk berdasarkan order yang telah dibuat
     *
     * @param idOrderDetail ID Order Detail generated
     * @param idOrder ID Order yang telah dibuat
     * @param idProduct ID produk yang dipesan
     * @param quantity Jumlah produk yang dipesan
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean saveDataOrderHeader(String idOrderDetail, String idOrder, String idProduct, int quantity) {
        String sql = "INSERT INTO OrderDetail (idOrderDetail, idOrder, idProduct, qty) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrderDetail);
            ps.setString(2, idOrder);
            ps.setString(3, idProduct);
            ps.setInt(4, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan OrderHeader berdasarkan idOrder dan idCustomer
     *
     * @param idOrder ID Order yang dicari
     * @param idCustomer ID Customer yang dicari
     * @return OrderHeader object jika ditemukan, null jika tidak ditemukan
     */
    public OrderHeader getOrderHeaderById(String idOrder, String idCustomer) {
        String sql = "SELECT * FROM OrderHeader WHERE idOrder = ? AND idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            ps.setString(2, idCustomer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new OrderHeader(
                        rs.getString("idOrder"),
                        rs.getString("idCustomer"),
                        rs.getString("idPromo"),
                        rs.getString("status"),
                        rs.getTimestamp("orderedAt").toLocalDateTime(),
                        rs.getDouble("totalAmount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan semua OrderHeader berdasarkan idCustomer
     *
     * @param idCustomer ID Customer yang dicari
     * @return List<OrderHeader> daftar OrderHeader yang ditemukan
     */
    public List<OrderHeader> getOrderHeadersByCustomerId(String idCustomer) {
        List<OrderHeader> orders = new ArrayList<>();
        String sql = "SELECT * FROM OrderHeader WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new OrderHeader(
                        rs.getString("idOrder"),
                        rs.getString("idCustomer"),
                        rs.getString("idPromo"),
                        rs.getString("status"),
                        rs.getTimestamp("orderedAt").toLocalDateTime(),
                        rs.getDouble("totalAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Mengambil satu OrderHeader berdasarkan idOrder dan idCustomer.
     *
     * @param idOrder    ID Order
     * @param idCustomer ID Customer
     * @return objek OrderHeader jika ditemukan, null jika tidak ditemukan
     */
    
    /**
     * Mengambil satu OrderHeader berdasarkan idOrder.
     *
     * @param idOrder ID Order
     * @return OrderHeader jika ditemukan, null jika tidak ditemukan
     */
    
    public OrderHeader getOrderHeaderById(String idOrder) {
        String sql = "SELECT * FROM OrderHeader WHERE idOrder = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new OrderHeader(
                    rs.getString("idOrder"),
                    rs.getString("idCustomer"),
                    rs.getString("idPromo"),
                    rs.getString("status"),
                    rs.getTimestamp("orderedAt").toLocalDateTime(),
                    rs.getDouble("totalAmount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan semua OrderHeader dari database
     *
     * @return List<OrderHeader> daftar semua OrderHeader
     */
    public List<OrderHeader> getAllOrderHeaders() {
        List<OrderHeader> orders = new ArrayList<>();
        String sql = "SELECT * FROM OrderHeader";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new OrderHeader(
                        rs.getString("idOrder"),
                        rs.getString("idCustomer"),
                        rs.getString("idPromo"),
                        rs.getString("status"),
                        rs.getTimestamp("orderedAt").toLocalDateTime(),
                        rs.getDouble("totalAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public List<String> getAllOrderIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT idOrder FROM OrderHeader";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getString("idOrder"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /**
     * Update data OrderHeader di database
     *
     * @param orderHeader OrderHeader object yang akan diupdate
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updateOrderHeader(OrderHeader orderHeader) {
        String sql = "UPDATE OrderHeader SET idCustomer = ?, idPromo = ?, status = ?, orderedAt = ?, totalAmount = ? WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, orderHeader.getIdCustomer());
            ps.setString(2, orderHeader.getIdPromo());
            ps.setString(3, orderHeader.getStatus());
            ps.setTimestamp(4, Timestamp.valueOf(orderHeader.getOrderedAt()));
            ps.setDouble(5, orderHeader.getTotalAmount());
            ps.setString(6, orderHeader.getIdOrder());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update status OrderHeader di database
     *
     * @param idOrder ID Order yang akan diupdate
     * @param status status baru
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updateOrderStatus(String idOrder, String status) {
        String sql = "UPDATE OrderHeader SET status = ? WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, idOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete OrderHeader dari database
     *
     * @param idOrder ID Order yang akan dihapus
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean deleteOrderHeader(String idOrder) {
        String sql = "DELETE FROM OrderHeader WHERE idOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan semua OrderHeader berdasarkan status
     *
     * @param status status yang dicari
     * @return List<OrderHeader> daftar OrderHeader yang ditemukan
     */
    public List<OrderHeader> getOrderHeadersByStatus(String status) {
        List<OrderHeader> orders = new ArrayList<>();
        String sql = "SELECT * FROM OrderHeader WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new OrderHeader(
                        rs.getString("idOrder"),
                        rs.getString("idCustomer"),
                        rs.getString("idPromo"),
                        rs.getString("status"),
                        rs.getTimestamp("orderedAt").toLocalDateTime(),
                        rs.getDouble("totalAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
