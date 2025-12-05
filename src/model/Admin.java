package model;

/**
 * Admin Model
 * 
 * Merepresentasikan entitas Admin dalam sistem JoymarKet.
 * Menyimpan informasi admin dan kontak darurat.
 */
public class Admin {
    private String idAdmin;            // ID unik admin
    private String idUser;             // ID user yang terkait dengan admin
    private String emergencyContact;   // Kontak darurat admin

    /**
     * Constructor default untuk Admin
     */
    public Admin() {
    }

    public Admin(String idAdmin, String idUser, String emergencyContact) {
        this.idAdmin = idAdmin;
        this.idUser = idUser;
        this.emergencyContact = emergencyContact;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
