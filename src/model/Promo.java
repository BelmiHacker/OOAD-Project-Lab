package model;

/**
 * Promo Model
 * 
 * Merepresentasikan entitas Promo/Diskon dalam sistem JoymarKet.
 * Menyimpan informasi kode promo dan persentase diskon.
 */
public class Promo {
    private String idPromo;              // ID unik promo
    private String code;                 // Kode promo (unique)
    private double discountPercentage;   // Persentase diskon (0-100)
    private String headline;             // Deskripsi promo

    /**
     * Constructor default untuk Promo
     */
    public Promo() {
    }

    public Promo(String idPromo, String code, double discountPercentage, String headline) {
        this.idPromo = idPromo;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.headline = headline;
    }

    public String getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(String idPromo) {
        this.idPromo = idPromo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
