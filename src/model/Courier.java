package model;

/**
 * Courier Model
 * 
 * Model ini merepresentasikan entitas Courier (Kurir) dalam sistem JoymarKet.
 * Courier bertugas melakukan pengiriman order dan memiliki informasi terkait
 * identitas user serta kendaraan yang digunakan.
 */
public class Courier {
    private String idCourier;      // ID unik kurir
    private String idUser;			// ID unik user
    private String vehicleType;    // Tipe kendaraan (Motor/Mobil)
    private String vehiclePlate;   // Plat nomor kendaraan

    /**
     * Constructor default untuk Courier
     */
    public Courier() {
    }

    public Courier(String idCourier, String idUser, String vehicleType, String vehiclePlate) {
        this.idCourier = idCourier;
        this.idUser = idUser;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }

    public String getIdCourier() {
        return idCourier;
    }

    public void setIdCourier(String idCourier) {
        this.idCourier = idCourier;
    }
    
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
}
