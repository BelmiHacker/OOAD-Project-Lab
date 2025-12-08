package database;

import model.CartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CartItemDAO
 * 
 * Data Access Object untuk operasi CRUD pada entitas CartItem.
 * Menangani semua transaksi database untuk tabel CartItem.
 */
public class CartItemDAO {
    private Connection connection;

    /**
     * Constructor untuk CartItemDAO
     * Menginisialisasi koneksi dari DatabaseConnection singleton
     */
    public CartItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Generate unique ID untuk CartItem
     * Format: CART_XXXXX (diikuti dengan nomor urut)
     * 
     * @return String ID yang unik
     */
    public String generateId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(idCartItem, 6) AS UNSIGNED)) as maxId FROM CartItem";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                int maxId = rs.getInt("maxId");
                if (maxId > 0) {
                    nextId = maxId + 1;
                }
            }
            return String.format("CART_%05d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return "CART_00001";
        }
    }

    /**
     * Insert CartItem baru ke database
     *
     * @param cartItem CartItem object yang akan disimpan
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean insertCartItem(CartItem cartItem) {
        String sql = "INSERT INTO CartItem (idCartItem, idCustomer, idProduct, count) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cartItem.getIdCartItem());
            ps.setString(2, cartItem.getIdCustomer());
            ps.setString(3, cartItem.getIdProduct());
            ps.setInt(4, cartItem.getCount());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mendapatkan CartItem berdasarkan idCartItem
     *
     * @param idCartItem ID dari CartItem yang dicari
     * @return CartItem object jika ditemukan, null jika tidak ditemukan
     */
    public CartItem getCartItemById(String idCartItem) {
        String sql = "SELECT * FROM CartItem WHERE idCartItem = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCartItem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CartItem(
                        rs.getString("idCartItem"),
                        rs.getString("idCustomer"),
                        rs.getString("idProduct"),
                        rs.getInt("count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan CartItem berdasarkan idCustomer
     *
     * @param idCustomer ID dari Customer yang dicari
     * @return CartItem object jika ditemukan, null jika tidak ditemukan
     */
    public CartItem getCartItemByCustomerId(String idCustomer) {
        String sql = "SELECT * FROM CartItem WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CartItem(
                        rs.getString("idCartItem"),
                        rs.getString("idCustomer"),
                        rs.getString("idProduct"),
                        rs.getInt("count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan semua CartItem dari database
     *
     * @return List<CartItem> daftar semua CartItem
     */
    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItem";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cartItems.add(new CartItem(
                        rs.getString("idCartItem"),
                        rs.getString("idCustomer"),
                        rs.getString("idProduct"),
                        rs.getInt("count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    /**
     * Update CartItem di database
     *
     * @param cartItem CartItem object yang akan diupdate
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updateCartItem(CartItem cartItem) {
        String sql = "UPDATE CartItem SET idCustomer = ?, idProduct = ?, count = ? WHERE idCartItem = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cartItem.getIdCustomer());
            ps.setString(2, cartItem.getIdProduct());
            ps.setInt(3, cartItem.getCount());
            ps.setString(4, cartItem.getIdCartItem());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update count dari CartItem di database
     *
     * @param idCartItem ID dari CartItem yang akan diupdate
     * @param newCount nilai count baru
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean updateCount(String idCartItem, int newCount) {
        String sql = "UPDATE CartItem SET count = ? WHERE idCartItem = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newCount);
            ps.setString(2, idCartItem);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete CartItem dari database
     *
     * @param idCustomer ID Customer dari CartItem yang akan dihapus
     * @param idProduct ID Product dari CartItem yang akan dihapus
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean deleteCartItem(String idCustomer, String idProduct) {
        String sql = "DELETE FROM CartItem WHERE idCustomer = ? AND idProduct = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            ps.setString(2, idProduct);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete semua CartItem berdasarkan idCustomer
     *
     * @param idCustomer ID Customer dari CartItem yang akan dihapus
     * @return boolean true jika berhasil, false jika gagal
     */
    public boolean deleteCartItemByCustomerId(String idCustomer) {
        String sql = "DELETE FROM CartItem WHERE idCustomer = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
