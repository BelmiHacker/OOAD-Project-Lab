package model;

/**
 * Customer Model
 * 
 * Merepresentasikan entitas Customer dalam sistem JoymarKet.
 * Menyimpan informasi customer termasuk balance saldo dompet digital.
 */
public class Customer {
    private String idCustomer;    // ID unik customer
    private String idUser;        // ID user yang terkait dengan customer
    private double balance;       // Saldo dompet digital customer (Rp)

    /**
     * Constructor default untuk Customer
     */
    public Customer() {
    }

    public Customer(String idCustomer, String idUser, double balance) {
        this.idCustomer = idCustomer;
        this.idUser = idUser;
        this.balance = balance;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
