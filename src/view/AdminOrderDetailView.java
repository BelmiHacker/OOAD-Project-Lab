package view;

import controller.OrderHandler;
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
import model.OrderDetail;
import model.OrderHeader;

import java.util.List;

/**
 * AdminOrderDetailView
 * View ini digunakan untuk menampilkan detail dari satu order tertentu berdasarkan yang dipilih.
 * Informasi yang ditampilkan meliputi ringkasan header order (ID Order, ID Customer, Status)
 * dan tabel detail item (ID Produk dan Qty).
 */

public class AdminOrderDetailView {
	
	// Scene utama untuk view ini (dipakai oleh Main untuk setScene)
    private Scene scene;
    
 // Root layout menggunakan BorderPane (Top: header info, Center: tabel detail, Bottom: tombol navigasi)
    private BorderPane mainLayout;
    
 // TableView untuk menampilkan detail item order (OrderDetail)
    private TableView<OrderDetail> detailTable;

 // Handler untuk mengambil data header order dan detail order
    private final OrderHandler orderHandler = new OrderHandler();
    
 // NavigationListener untuk menghubungkan view dengan Main (Navigation Controller)
    private NavigationListener navigationListener;

 // Menyimpan orderId yang dikirim dari halaman sebelumnya
    private final String orderId;
    
 // Menyimpan data OrderHeader yang ditemukan berdasarkan orderId
    private OrderHeader orderHeader;

 // Constructor menerima orderId, lalu menyiapkan UI dan memuat data order terkait
    public AdminOrderDetailView(String orderId) {
        this.orderId = orderId;

        init();              // inisialisasi layout & komponen dasar
        loadOrderHeader();   // ambil data header order berdasarkan orderId
        setupLayout();       // susun tampilan UI (header, tabel, tombol)
        loadOrderDetails();  // ambil dan tampilkan detail item order
        
        scene = new Scene(mainLayout, 900, 600);
    }

 // Inisialisasi komponen dasar view
    private void init() {
        mainLayout = new BorderPane();
        detailTable = new TableView<>();
    }

    // cari header berdasarkan idOrder
    private void loadOrderHeader() {
        List<OrderHeader> all = orderHandler.getAllOrders();
        if (all != null) {
            for (OrderHeader oh : all) {
                if (oh.getIdOrder().equals(orderId)) {
                    orderHeader = oh;
                    break;
                }
            }
        }

     // Jika order tidak ditemukan, tampilkan pesan error
        if (orderHeader == null) {
            showAlert("Error", "Order dengan ID " + orderId + " tidak ditemukan.");
        }
    }

    
    // Menyusun tampilan UI:
    // - Top: header informasi order (ID, customer, status)
    // - Center: tabel detail item
    // - Bottom: tombol kembali
    private void setupLayout() {
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // ==== HEADER ATAS ====
        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #c8dcfa;");

     // Judul halaman
        Label titleLbl = new Label("Detail Order");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLbl.setTextFill(Color.web("#333333"));

        
     // Info baris 1: ID Order dan ID Customer
        String info1Text = "ID Order: " + (orderHeader != null ? orderHeader.getIdOrder() : orderId)
                + "   |   ID Customer: " + (orderHeader != null ? orderHeader.getIdCustomer() : "-");

     // Info baris 2: Status order
        String info2Text = "Status: " + (orderHeader != null ? orderHeader.getStatus() : "-");

        Label info1 = new Label(info1Text);
        Label info2 = new Label(info2Text);

        headerBox.getChildren().addAll(titleLbl, info1, info2);
        mainLayout.setTop(headerBox);

        // ==== TABEL DETAIL ====
        setupTable();
        mainLayout.setCenter(detailTable);

        // ==== TOMBOL BAWAH ====
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(15));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setStyle("-fx-background-color: #f0f0f0;");

     // Tombol kembali ke halaman sebelumnya (navigasi ditangani oleh Main)
        Button backBtn = new Button("Kembali");
        backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; "
                + "-fx-background-color: #999999; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.goBack();
            }
        });

        bottomBox.getChildren().add(backBtn);
        mainLayout.setBottom(bottomBox);
    }
    
 // Menyiapkan kolom-kolom tabel untuk OrderDetail
    @SuppressWarnings("unchecked")
    private void setupTable() {
    	// Kolom ID Order
        TableColumn<OrderDetail, String> idOrderCol = new TableColumn<>("ID Order");
        idOrderCol.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        idOrderCol.setPrefWidth(150);

     // Kolom ID Produk
        TableColumn<OrderDetail, String> idProductCol = new TableColumn<>("ID Produk");
        idProductCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        idProductCol.setPrefWidth(150);

     // Kolom Qty
        TableColumn<OrderDetail, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qtyCol.setPrefWidth(80);

        detailTable.getColumns().addAll(idOrderCol, idProductCol, qtyCol);
    }

 // Mengambil detail item order berdasarkan orderId lalu menampilkan ke tabel
    private void loadOrderDetails() {
        List<OrderDetail> details = orderHandler.getOrderDetails(orderId);
        
        // Jika detail kosong, tampilkan info dan kosongkan tabel
        if (details == null || details.isEmpty()) {
            showAlert("Info", "Order ini belum memiliki detail item.");
            detailTable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<OrderDetail> items = FXCollections.observableArrayList(details);
        detailTable.setItems(items);
    }

 // Helper untuk menampilkan alert (info/warning/error) kepada user
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

 // Getter scene untuk dipakai oleh Main (primaryStage.setScene(...))
    public Scene getScene() {
        return scene;
    }

 // Diset dari Main agar tombol "Kembali" bisa menjalankan navigasi
    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
