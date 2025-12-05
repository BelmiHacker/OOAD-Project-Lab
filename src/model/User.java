package model;

/**
 * User Model
 * 
 * Merepresentasikan entitas User dalam sistem JoymarKet.
 * Menyimpan informasi user termasuk autentikasi dan role.
 */
public class User {
    private String idUser;        // ID unik user
    private String fullName;      // Nama lengkap user
    private String email;         // Email user (unique)
    private String password;      // Password terenkripsi
    private String phone;         // Nomor telepon (10-13 digit)
    private String address;       // Alamat user
    private String role;          // Role user (customer/admin/courier)

    /**
     * Constructor default untuk User
     */
    public User() {
    }

    /**
     * Constructor dengan parameter untuk User
     */
    public User(String idUser, String fullName, String email, String password, 
                String phone, String address, String role) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
