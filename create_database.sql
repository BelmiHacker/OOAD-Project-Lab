CREATE DATABASE IF NOT EXISTS joymarket;
USE joymarket;

CREATE TABLE User (
    idUser VARCHAR(50) PRIMARY KEY,
    fullName VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE Admin (
    idAdmin VARCHAR(50) PRIMARY KEY,
    idUser VARCHAR(50) NOT NULL UNIQUE,
    emergencyContact VARCHAR(100),
    FOREIGN KEY (idUser) REFERENCES User(idUser) ON DELETE CASCADE
);

CREATE TABLE Customer (
    idCustomer VARCHAR(50) PRIMARY KEY,
    idUser VARCHAR(50) NOT NULL UNIQUE,
    balance DOUBLE DEFAULT 0,
    FOREIGN KEY (idUser) REFERENCES User(idUser) ON DELETE CASCADE
);

CREATE TABLE Product (
    idProduct VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(50)
);

CREATE TABLE CartItem (
    idCartItem VARCHAR(50) PRIMARY KEY,
    idCustomer VARCHAR(50) NOT NULL,
    idProduct VARCHAR(50) NOT NULL,
    count INT NOT NULL,
    FOREIGN KEY (idCustomer) REFERENCES Customer(idCustomer) ON DELETE CASCADE,
    FOREIGN KEY (idProduct) REFERENCES Product(idProduct) ON DELETE CASCADE
);

CREATE TABLE Courier (
    idCourier VARCHAR(50) PRIMARY KEY,
    idUser VARCHAR(50) NOT NULL UNIQUE,
    vehicleType VARCHAR(50),
    vehiclePlate VARCHAR(20),
    FOREIGN KEY (idUser) REFERENCES User(idUser) ON DELETE CASCADE
);

CREATE TABLE Promo (
    idPromo VARCHAR(50) PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discountPercentage DOUBLE NOT NULL,
    headline VARCHAR(255)
);

CREATE TABLE OrderHeader (
    idOrder VARCHAR(50) PRIMARY KEY,
    idCustomer VARCHAR(50) NOT NULL,
    idPromo VARCHAR(50),
    status VARCHAR(50),
    orderedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    totalAmount DOUBLE NOT NULL,
    FOREIGN KEY (idCustomer) REFERENCES Customer(idCustomer) ON DELETE CASCADE,
    FOREIGN KEY (idPromo) REFERENCES Promo(idPromo) ON DELETE SET NULL
);

CREATE TABLE OrderDetail (
    idOrderDetail VARCHAR(50) PRIMARY KEY,
    idOrder VARCHAR(50) NOT NULL,
    idProduct VARCHAR(50) NOT NULL,
    qty INT NOT NULL,
    FOREIGN KEY (idOrder) REFERENCES OrderHeader(idOrder) ON DELETE CASCADE,
    FOREIGN KEY (idProduct) REFERENCES Product(idProduct) ON DELETE CASCADE
);

CREATE TABLE Delivery (
    idDelivery VARCHAR(50) PRIMARY KEY,
    idOrder VARCHAR(50) NOT NULL,
    idCourier VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (idOrder) REFERENCES OrderHeader(idOrder) ON DELETE CASCADE,
    FOREIGN KEY (idCourier) REFERENCES Courier(idCourier) ON DELETE CASCADE
);

-- ==================== INSERT TEST DATA ====================

-- Insert Users
INSERT INTO User (idUser, fullName, email, password, phone, address, role) VALUES
('USR_00001', 'Admin JoymarKet', 'admin@joymarket.com', 'admin123', '08123456789', 'Jl. Admin No.1', 'admin'),
('USR_00002', 'Budi Santoso', 'customer1@joymarket.com', 'cust123', '08111111111', 'Jl. Sudirman No.100, Jakarta', 'customer'),
('USR_00003', 'Siti Nurhaliza', 'customer2@joymarket.com', 'cust123', '08222222222', 'Jl. Gatot Subroto No.50, Bandung', 'customer'),
('USR_00004', 'Hendra Wijaya', 'courier1@joymarket.com', 'courier123', '08333333333', 'Jl. Kurir No.1, Jakarta', 'courier'),
('USR_00005', 'Rahmat Hidayat', 'courier2@joymarket.com', 'courier123', '08444444444', 'Jl. Kurir No.2, Bandung', 'courier');

-- Insert Admin
INSERT INTO Admin (idAdmin, idUser, emergencyContact) VALUES
('ADM_00001', 'USR_00001', '08-9999-8888');

-- Insert Customers
INSERT INTO Customer (idCustomer, idUser, balance) VALUES
('CUST_00001', 'USR_00002', 5000000),
('CUST_00002', 'USR_00003', 3000000);

-- Insert Couriers
INSERT INTO Courier (idCourier, idUser, vehicleType, vehiclePlate) VALUES
('COUR_00001', 'USR_00004', 'Motor', 'B 1234 AB'),
('COUR_00002', 'USR_00005', 'Mobil', 'B 5678 CD');

-- Insert Products
INSERT INTO Product (idProduct, name, price, stock, category) VALUES
('PROD_00001', 'Laptop Dell XPS 13', 12000000, 10, 'Electronics'),
('PROD_00002', 'iPhone 14 Pro', 15000000, 5, 'Electronics'),
('PROD_00003', 'Samsung Galaxy S23', 10000000, 8, 'Electronics'),
('PROD_00004', 'Headphones Sony WH-1000XM5', 3500000, 15, 'Electronics'),
('PROD_00005', 'Meja Kayu Jati', 2000000, 20, 'Furniture'),
('PROD_00006', 'Kursi Gaming RGB', 1500000, 12, 'Furniture'),
('PROD_00007', 'Lemari Pakaian Minimalis', 3000000, 7, 'Furniture');

-- Insert Promos
INSERT INTO Promo (idPromo, code, discountPercentage, headline) VALUES
('PROM_00001', 'DISKON10', 10, 'Diskon 10% Elektronik'),
('PROM_00002', 'DISKON15', 15, 'Diskon 15% Furniture'),
('PROM_00003', 'CASHBACK5', 5, 'Cashback 5% All Items');

-- Verify data inserted
SELECT 'Users created' AS status, COUNT(*) as count FROM User
UNION ALL
SELECT 'Customers created', COUNT(*) FROM Customer
UNION ALL
SELECT 'Couriers created', COUNT(*) FROM Courier
UNION ALL
SELECT 'Products created', COUNT(*) FROM Product
UNION ALL
SELECT 'Promos created', COUNT(*) FROM Promo;
