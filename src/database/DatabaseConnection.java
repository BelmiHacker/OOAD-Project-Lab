package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection
 * 
 * Singleton class untuk mengelola koneksi ke database MySQL.
 * Menyediakan single instance koneksi yang digunakan oleh semua DAO classes.
 * Menggunakan Singleton pattern untuk memastikan hanya satu koneksi yang aktif.
 */
public class DatabaseConnection {
	 private final String USERNAME = "root";              // Username MySQL
	    private final String PASSWORD = "";                  // Password MySQL
	    private final String DATABASE = "joymarket";         // Nama database
	    private final String HOST = "localhost:3306";        // Host dan port MySQL
	    private final String CONNECTION = "jdbc:mysql://" + HOST + "/" + DATABASE;

	    private Connection con;      // Koneksi ke database
	    private Statement st;        // Statement untuk query

	    private static DatabaseConnection instance;  // Singleton instance

	    /**
	     * Constructor private untuk Singleton pattern
	     * Menginisialisasi koneksi ke MySQL
	     */
	    private DatabaseConnection() {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
	            st = con.createStatement();
	            System.out.println("Connected!");
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Connection Failed!");
	        }
	    }

	    /**
	     * Mendapatkan singleton instance dari DatabaseConnection
	     * Menginisialisasi koneksi jika belum ada
	     * 
	     * @return DatabaseConnection instance
	     */
	    public static DatabaseConnection getInstance() {
	        if (instance == null) {
	            instance = new DatabaseConnection();
	        }
	        return instance;
	    }
	    
	    /**
	     * Mendapatkan Connection object yang terhubung ke database
	     * 
	     * @return Connection object
	     */
	    public Connection getConnection() {
	        return con;
	    }

	    /**
	     * Menjalankan SELECT query
	     * 
	     * @param query SQL query string
	     * @return ResultSet hasil query
	     */
	    public ResultSet executeQuery(String query) {
	        try {
	            return st.executeQuery(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    /**
	     * Menjalankan INSERT/UPDATE/DELETE query
	     * 
	     * @param query SQL query string\n	     */
	    public void executeUpdate(String query) {
	        try {
	            st.executeUpdate(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Menyiapkan PreparedStatement untuk menghindari SQL injection
	     * 
	     * @param query SQL query template dengan parameter placeholder\n	     * @return PreparedStatement object
	     */
	    public PreparedStatement prepareStatement(String query) {
	        try {
	            return con.prepareStatement(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    /**
	     * Membuat Statement baru untuk query
	     * 
	     * @return Statement object
	     * @throws SQLException jika ada error\n	     */
	    public Statement getStatement() throws SQLException {
			
			return con.createStatement();
		}
    
}