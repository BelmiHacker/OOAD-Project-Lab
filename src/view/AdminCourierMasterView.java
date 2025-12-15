package view;

import controller.CourierHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Courier;

import java.util.List;

/**
 * AdminCourierMasterView
 * View ini digunakan oleh admin untuk melihat daftar seluruh courier yang tersedia.
 * Data courier ditampilkan dalam bentuk tabel dan bersifat read-only (tanpa edit/delete).
 * Navigasi halaman dikontrol oleh Main melalui NavigationListener.
 */
public class AdminCourierMasterView {

	// Scene utama yang akan dipanggil oleh Main
    private Scene scene;
    
    // Root layout menggunakan BorderPane
    // Top    : Header judul
    // Center : Tabel daftar courier
    // Bottom : Tombol navigasi
    private BorderPane mainLayout;
    
 // TableView untuk menampilkan data courier
    private TableView<Courier> courierTable;

 // Controller untuk mengambil data courier
    private CourierHandler cc = new CourierHandler();
    
    // NavigationListener untuk menghubungkan View dengan Main (Navigation Controller)
    private NavigationListener navigationListener;

 // Constructor: inisialisasi komponen, susun layout, load data courier, lalu buat Scene
    public AdminCourierMasterView() {
        init();
        setupLayout();
        loadCouriers();

        scene = new Scene(mainLayout, 800, 600);
    }

    
 // Inisialisasi komponen dasar view
    private void init() {
        mainLayout = new BorderPane();
        courierTable = new TableView<>();
    }

    
    // Menyusun tampilan UI:
    // - Top: header judul
    // - Center: tabel courier
    // - Bottom: tombol kembali
    private void setupLayout() {
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // HEADER
        VBox header = new VBox(5);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #c8dcfa;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Daftar Kurir");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#333333"));

        header.getChildren().addAll(title);
        mainLayout.setTop(header);

     // ==== TABLE DAFTAR COURIER ====
        setupTable();
        mainLayout.setCenter(courierTable);

     // ==== PANEL TOMBOL BAWAH ====
        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(15));
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

        // Tombol untuk kembali ke halaman sebelumnya
        // Navigasi dikontrol oleh Main melalui NavigationListener
        Button backBtn = new Button("Kembali");
        backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.goBack();
            }
        });
        

        buttonPanel.getChildren().add(backBtn);
        mainLayout.setBottom(buttonPanel);
    }

 // Menyiapkan kolom-kolom tabel courier dan mapping ke field model Courier
    @SuppressWarnings("unchecked")
    private void setupTable() {
    	// Kolom ID Courier
        TableColumn<Courier, String> idCol = new TableColumn<>("ID Courier");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
        idCol.setPrefWidth(120);

     // Kolom tipe kendaraan courier
        TableColumn<Courier, String> typeCol = new TableColumn<>("Tipe Kendaraan");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        typeCol.setPrefWidth(150);

     // Kolom plat kendaraan courier
        TableColumn<Courier, String> plateCol = new TableColumn<>("Plat Kendaraan");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("vehiclePlate"));
        plateCol.setPrefWidth(150);

        courierTable.getColumns().addAll(idCol, typeCol, plateCol);
    }

    // Mengambil seluruh data courier dari CourierController
    // lalu menampilkannya ke dalam TableView
    private void loadCouriers() {
    	List<String> courierIds = cc.getAllCourierIds(); 

        ObservableList<Courier> items = FXCollections.observableArrayList();
        for (String id : courierIds) {
            Courier c = cc.getCourier(id); 
            if (c != null) items.add(c);
        }

        courierTable.setItems(items);
    }

 // Helper method untuk menampilkan alert informasi kepada user
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

 // Getter Scene agar Main dapat memanggil primaryStage.setScene(...)
    public Scene getScene() {
        return scene;
    }

 // Diset dari Main untuk mengaktifkan navigasi halaman
    // Jika tidak diset, tombol "Kembali" tidak akan berfungsi
    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
