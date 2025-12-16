package model;

/**
 * Customer Model
 * 
 * Merepresentasikan entitas Customer dalam sistem JoymarKet.
 * Menyimpan informasi customer termasuk balance saldo dompet digital.
 */
public class Customer extends User {
    private String idCustomer;    // ID unik customer
    private double balance;       // Saldo dompet digital customer (Rp)

    /**
     * Constructor default untuk Customer
     */
    public Customer() {
    }

    public Customer(String idCustomer, String idUser, double balance) {
        this.idCustomer = idCustomer;
        this.setIdUser(idUser);
        this.balance = balance;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
