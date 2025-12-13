package model;

/**
 * Admin Model
 * 
 * Merepresentasikan entitas Admin dalam sistem JoymarKet.
 * Menyimpan informasi admin dan kontak darurat.
 */
public class Admin extends User {
    private String idAdmin;            // ID unik admin
    private String emergencyContact;   // Kontak darurat admin

    /**
     * Constructor default untuk Admin
     */
    public Admin() {
    }

    public Admin(String idAdmin, String idUser, String emergencyContact) {
        this.idAdmin = idAdmin;
        this.emergencyContact = emergencyContact;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
