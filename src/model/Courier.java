package model;

/**
 * Courier Model
 * 
 * Merepresentasikan entitas Kurir dalam sistem JoymarKet.
 * Menyimpan informasi kurir termasuk kendaraan dan plat nomor.
 */
public class Courier {
    private String idCourier;      // ID unik kurir
    private String vehicleType;    // Tipe kendaraan (Motor/Mobil)
    private String vehiclePlate;   // Plat nomor kendaraan

    /**
     * Constructor default untuk Courier
     */
    public Courier() {
    }

    public Courier(String idCourier, String idUser, String vehicleType, String vehiclePlate) {
        this.idCourier = idCourier;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }

    public String getIdCourier() {
        return idCourier;
    }

    public void setIdCourier(String idCourier) {
        this.idCourier = idCourier;
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
