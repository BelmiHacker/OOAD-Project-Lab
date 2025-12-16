package view;

import controller.OrderHandler;
import controller.DeliveryHandler;
import controller.CourierHandler;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderHeader;
import model.Courier;
import java.util.List;


/**
 * AdminOrderListView
 * ------------------
 * View untuk admin dalam mengelola data order.
 * Fitur utama:
 * 1. Menampilkan daftar order dalam TableView
 * 2. Melakukan assign courier ke order
 * 3. Navigasi ke halaman detail order
 */

public class AdminOrderListView {
	
	// Scene utama untuk view ini (dipakai oleh Main untuk setScene)
	private Scene scene;
	
	// Root layout menggunakan BorderPane (Top: header, Center: table, Bottom: panel aksi)
	private BorderPane mainLayout;
	
	// TableView untuk menampilkan daftar OrderHeader
	private TableView<OrderHeader> orderTable;
	
	// Handler/Controller untuk ambil data order dan melakukan proses assign courier
	private OrderHandler oc = new OrderHandler();
	private DeliveryHandler dc = new DeliveryHandler();
	private CourierHandler cc = new CourierHandler();
	
	// NavigationListener menghubungkan View dengan Main (Navigation Controller).
	private NavigationListener navigationListener;
	
	// Constructor: inisialisasi komponen, susun layout, load data order, lalu buat Scene.
    // Note: navigationListener akan diset dari Main melalui setNavigationListener(...)
	public AdminOrderListView(String adminId) {
		init();
		setupLayout();
		loadOrders();
		
		scene = new Scene(mainLayout, 1100, 700);
	}
	
	// Menyusun tampilan:
    // - Top: header judul
    // - Center: tabel order
    // - Bottom: panel assign courier + panel tombol navigasi
	private void setupLayout() {
	    mainLayout.setStyle("-fx-background-color: #f5f5f5;");

	    // ==== HEADER (judul halaman) ====
	    VBox header = new VBox();
	    header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	    header.setAlignment(Pos.CENTER_LEFT);

	    Label title = new Label("Kelola Order & Assign Courier");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    title.setTextFill(Color.web("#333333"));

	    header.getChildren().add(title);
	    mainLayout.setTop(header);

	    // ==== TABLE (daftar order) ====
	    setupTable();
	    mainLayout.setCenter(orderTable);

	    // ==== ASSIGN PANEL (popup) ====
	    VBox assignPanel = new VBox(10);
	    assignPanel.setPadding(new Insets(15));
	    assignPanel.setStyle("-fx-background-color: #f0f0f0;");

	    HBox courierPanel = new HBox(10);
	    courierPanel.setAlignment(Pos.CENTER_LEFT);

	    Label courierLabel = new Label("Assign Courier ke Order terpilih:");
	    courierLabel.setFont(Font.font("Arial", 12));

	    Button assignPopupBtn = new Button("Assign Courier (Popup)");
	    assignPopupBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; "
	            + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
	    assignPopupBtn.setOnAction(e -> openAssignCourierPopup());

	    courierPanel.getChildren().addAll(courierLabel, assignPopupBtn);
	    assignPanel.getChildren().add(courierPanel);

	    // ==== PANEL TOMBOL BAWAH ====
	    HBox buttonPanel = new HBox(10);
	    buttonPanel.setPadding(new Insets(15));
	    buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
	    buttonPanel.setAlignment(Pos.CENTER_RIGHT);

	    Button detailBtn = new Button("Lihat Detail Order");
	    detailBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #2196F3; -fx-text-fill: white;");
	    detailBtn.setOnAction(e -> {
	        OrderHeader selected = orderTable.getSelectionModel().getSelectedItem();
	        if (selected == null) {
	            showAlert("Warning", "Pilih order dulu.");
	            return;
	        }
	        if (navigationListener != null) {
	            navigationListener.navigateTo("ADMIN_ORDER_DETAIL", selected.getIdOrder());
	        }
	    });

	    Button backBtn = new Button("Kembali");
	    backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
	    backBtn.setOnAction(e -> {
	        if (navigationListener != null) {
	            navigationListener.goBack();
	        }
	    });

	    buttonPanel.getChildren().addAll(detailBtn, backBtn);

	    // ==== BOTTOM BOX (gabung assignPanel + buttonPanel) ====
	    VBox bottomBox = new VBox();
	    bottomBox.getChildren().addAll(assignPanel, buttonPanel);
	    mainLayout.setBottom(bottomBox);
	}
	
    private void openAssignCourierPopup() {
        OrderHeader selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert("Warning", "Pilih order terlebih dahulu!");
            return;
        }
        
     // CEK: kalau order sudah pernah di-assign, jangan boleh assign lagi

        javafx.stage.Stage popup = new javafx.stage.Stage();
        popup.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        popup.setTitle("Pilih Courier untuk Order: " + selectedOrder.getIdOrder());

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Pilih Courier");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Courier> courierTable = new TableView<>();
        courierTable.setPrefHeight(250);

        TableColumn<Courier, String> idCol = new TableColumn<>("ID Courier");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
        idCol.setPrefWidth(150);

        TableColumn<Courier, String> vehicleCol = new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        vehicleCol.setPrefWidth(200);

        courierTable.getColumns().addAll(idCol, vehicleCol);

        // Load courier list (pakai handler kamu yang sudah ada)
        ObservableList<Courier> courierList = FXCollections.observableArrayList();
        List<String> courierIds = cc.getAllCourierIds();

        if (courierIds != null) {
            for (String id : courierIds) {
                Courier c = cc.getCourierById(id);
                if (c != null) courierList.add(c);
            }
        }
        courierTable.setItems(courierList);

        Button assign = new Button("Assign");
        Button cancel = new Button("Cancel");

        assign.setOnAction(ev -> {
            Courier selectedCourier = courierTable.getSelectionModel().getSelectedItem();
            if (selectedCourier == null) {
                showAlert("Warning", "Pilih courier terlebih dahulu!");
                return;
            }

            boolean alreadyAssigned = dc.isOrderAlreadyAssigned(selectedOrder.getIdOrder());
            if (alreadyAssigned) {
                showAlert("Warning", "Order ini sudah di-assign ke courier. Tidak bisa di-assign lagi.");
                return;
            }

            String deliveryId = "DEL_" + System.currentTimeMillis();
            String result = dc.assignCourier(deliveryId, selectedOrder.getIdOrder(), selectedCourier.getIdCourier());

            if ("success".equals(result)) {
                showAlert("Sukses", "Courier berhasil di-assign ke order!");
                loadOrders();
                popup.close();
            } else {
                showAlert("Error", "Gagal assign courier: " + result);
            }
        });

        cancel.setOnAction(ev -> popup.close());

        HBox actions = new HBox(10, assign, cancel);
        actions.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(title, courierTable, actions);

        popup.setScene(new Scene(root, 420, 330));
        popup.showAndWait();
    }

	// Menyiapkan kolom-kolom TableView untuk OrderHeader dan mapping ke field model
	@SuppressWarnings("unchecked")
	private void setupTable() {
	    // ID ORDER
	    TableColumn<OrderHeader, String> idCol = new TableColumn<>("ID Order");
	    idCol.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
	    idCol.setPrefWidth(120);

	    // ID CUSTOMER
	    TableColumn<OrderHeader, String> custCol = new TableColumn<>("ID Customer");
	    custCol.setCellValueFactory(new PropertyValueFactory<>("idCustomer"));
	    custCol.setPrefWidth(120);

	    // ID PROMO
	    TableColumn<OrderHeader, String> promoCol = new TableColumn<>("ID Promo");
	    promoCol.setCellValueFactory(new PropertyValueFactory<>("idPromo"));
	    promoCol.setPrefWidth(120);

	    // STATUS
	    TableColumn<OrderHeader, String> statusCol = new TableColumn<>("Status");
	    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
	    statusCol.setPrefWidth(120);

	    // ORDER DATE
	    TableColumn<OrderHeader, String> dateCol = new TableColumn<>("Tanggal Order");
	    dateCol.setCellValueFactory(new PropertyValueFactory<>("orderedAt"));
	    dateCol.setPrefWidth(160);

	    // TOTAL AMOUNT
	    TableColumn<OrderHeader, Double> amountCol = new TableColumn<>("Total Amount");
	    amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
	    amountCol.setCellFactory(col -> new javafx.scene.control.TableCell<OrderHeader, Double>() {
	        @Override
	        protected void updateItem(Double value, boolean empty) {
	            super.updateItem(value, empty);
	            setText(empty || value == null ? null : "Rp " + String.format("%,.0f", value));
	        }
	    });
	    amountCol.setPrefWidth(150);

	    // TAMBAH SEMUA KOLOM KE TABLE
	    orderTable.getColumns().addAll(idCol, custCol, promoCol, statusCol, dateCol, amountCol);
	}
	
	// Mengambil semua order dari OrderHandler lalu menampilkan ke TableView
	private void loadOrders() {
	    List<String> orderIds = oc.getAllOrderIds(); // method yang kamu sudah punya

	    ObservableList<OrderHeader> items = FXCollections.observableArrayList();

	    if (orderIds != null) {
	        for (String idOrder : orderIds) {
	            OrderHeader oh = oc.getOrderHeader(idOrder); // method yang kamu sudah tambah
	            if (oh != null) items.add(oh);
	        }
	    }

	    orderTable.setItems(items);
	}
	
	// Helper untuk menampilkan alert informasi (warning/sukses/error)
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	// Getter scene agar Main bisa memanggil primaryStage.setScene(getScene())
	public Scene getScene() {
		return scene;
	}
	
	// Diset dari Main untuk mengaktifkan navigasi antar halaman.
	// Tanpa dipanggil, navigationListener akan null dan tombol Detail/Kembali tidak akan berfungsi.
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	// Inisialisasi komponen dasar view
	private void init() {
		mainLayout = new BorderPane();
		orderTable = new TableView<>();
	}
}