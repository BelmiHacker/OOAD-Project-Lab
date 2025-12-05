package controller;

import model.User;
import database.UserDAO;

/**
 * UserController
 * 
 * Controller untuk menangani business logic terkait User.
 * Bertanggung jawab untuk validasi input dan delegasi ke UserDAO.
 * 
 */
public class UserController {
    private UserDAO userDAO;

    /**
     * Constructor untuk UserController
     * Menginisialisasi UserDAO untuk akses database
     */
    public UserController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Helper method untuk validasi nomor telepon
     * Nomor telepon harus 10-13 digit tanpa spasi atau karakter khusus
     * 
     * @param phone Nomor telepon yang akan divalidasi
     * @return true jika nomor telepon valid, false sebaliknya
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        if (phone.length() < 10 || phone.length() > 13) {
            return false;
        }
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Register user baru di sistem
     * Melakukan validasi lengkap terhadap input:
     * - User ID tidak boleh kosong dan harus unik
     * - Nama lengkap tidak boleh kosong
     * - Email harus valid dan berakhir dengan @gmail.com
     * - Password minimal 6 karakter dan harus sama dengan konfirmasi
     * - Nomor telepon harus 10-13 digit
     * - Alamat tidak boleh kosong
     * 
     * @param idUser ID user yang akan didaftarkan
     * @param fullName Nama lengkap user
     * @param email Email user (harus @gmail.com)
     * @param password Password user
     * @param confirmPassword Konfirmasi password
     * @param phone Nomor telepon user
     * @param address Alamat user
     * @return "success" jika registrasi berhasil, pesan error sebaliknya
     */
    public String register(String idUser, String fullName, String email, String password, 
                           String confirmPassword, String phone, String address) {
        if (idUser == null || idUser.isEmpty()) {
            return "User ID tidak boleh kosong";
        }

        if (userDAO.idExists(idUser)) {
            return "User ID sudah terdaftar";
        }

        if (fullName == null || fullName.isEmpty()) {
            return "Nama lengkap tidak boleh kosong";
        }

        if (email == null || email.isEmpty()) {
            return "Email tidak boleh kosong";
        }

        if (!email.endsWith("@gmail.com")) {
            return "Email harus berakhir dengan @gmail.com";
        }

        if (userDAO.emailExists(email)) {
            return "Email sudah terdaftar";
        }

        if (password == null || password.length() < 6) {
            return "Password minimal 6 karakter";
        }

        if (!password.equals(confirmPassword)) {
            return "Password dan konfirmasi password tidak cocok";
        }

        if (phone == null || phone.isEmpty()) {
            return "Nomor telepon tidak boleh kosong";
        }

        if (!isValidPhone(phone)) {
            return "Nomor telepon harus 10-13 digit";
        }

        if (address == null || address.isEmpty()) {
            return "Alamat tidak boleh kosong";
        }

        User user = new User(idUser, fullName, email, password, phone, address, "customer");
        if (userDAO.insertUser(user)) {
            return "success";
        }
        return "Registrasi gagal";
    }

    /**
     * Login user dengan email dan password
     * 
     * @param email Email user untuk login
     * @param password Password user
     * @return "success" jika login berhasil, pesan error sebaliknya
     */
    public String login(String email, String password) {
        if (email == null || email.isEmpty()) {
            return "Email tidak boleh kosong";
        }

        if (password == null || password.isEmpty()) {
            return "Password tidak boleh kosong";
        }

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return "Email tidak ditemukan";
        }

        if (!user.getPassword().equals(password)) {
            return "Password salah";
        }

        return "success";
    }

    /**
     * Update profil user
     * Melakukan validasi terhadap data yang akan diupdate:
     * - Nama lengkap tidak boleh kosong
     * - Nomor telepon harus 10-13 digit
     * - Alamat tidak boleh kosong
     * 
     * @param idUser ID user yang profil-nya akan diupdate
     * @param fullName Nama lengkap baru
     * @param phone Nomor telepon baru
     * @param address Alamat baru
     * @return "success" jika update berhasil, pesan error sebaliknya
     */
    public String updateProfile(String idUser, String fullName, String phone, String address) {
        if (fullName == null || fullName.isEmpty()) {
            return "Nama lengkap tidak boleh kosong";
        }

        if (phone == null || phone.isEmpty()) {
            return "Nomor telepon tidak boleh kosong";
        }

        if (!isValidPhone(phone)) {
            return "Nomor telepon harus 10-13 digit";
        }

        if (address == null || address.isEmpty()) {
            return "Alamat tidak boleh kosong";
        }

        User user = userDAO.getUserById(idUser);
        if (user == null) {
            return "User tidak ditemukan";
        }

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        
        if (userDAO.updateUser(user)) {
            return "success";
        }
        return "Update profil gagal";
    }

    /**
     * Mendapatkan user berdasarkan ID
     * 
     * @param idUser ID user yang dicari
     * @return User object jika ditemukan, null sebaliknya
     */
    public User getUserById(String idUser) {
        return userDAO.getUserById(idUser);
    }

    /**
     * Mendapatkan user berdasarkan email
     * 
     * @param email Email user yang dicari
     * @return User object jika ditemukan, null sebaliknya
     */
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
}
