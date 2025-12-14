package view;

import controller.DeliveryController;
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
import model.Delivery;

import java.util.Iterator;
import java.util.List;

/**
 * CourierListView - JavaFX view untuk daftar pengiriman courier
 */
	public class CourierListView {
	    private Scene scene;
	    private BorderPane mainLayout;
	    private TableView<Delivery> deliveryTable;

	    private DeliveryController dc = new DeliveryController();

	    private String courierId; // null = mode admin, != null = mode courier
	    private NavigationListener navigationListener;

	    public CourierListView(String courierId) {
	        this.courierId = courierId;
	        init();
	        setupLayout();
	        loadDeliveries();

	        scene = new Scene(mainLayout, 1000, 700);
	    }

	    private void setupLayout() {
	        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

	        VBox header = new VBox();
	        header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	        header.setAlignment(Pos.CENTER_LEFT);

	        Label title = new Label("Daftar Pengiriman");
	        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	        title.setTextFill(Color.web("#333333"));

	        header.getChildren().add(title);
	        mainLayout.setTop(header);

	        setupTable();
	        mainLayout.setCenter(deliveryTable);

	        HBox buttonPanel = new HBox(10);
	        buttonPanel.setPadding(new Insets(15));
	        buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
	        buttonPanel.setAlignment(Pos.CENTER_RIGHT);

	        Button detailBtn = new Button("Lihat Detail");
	        detailBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #2196F3; -fx-text-fill: white;");
	        detailBtn.setOnAction(e -> {
	            Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
	            if (selected != null) {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("COURIER_DETAIL", selected.getIdDelivery(), courierId);
	                }
	            } else {
	                showAlert("Warning", "Pilih pengiriman terlebih dahulu!");
	            }
	        });

	        Button updateBtn = new Button("Update Status");
	        updateBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FF9800; -fx-text-fill: white;");
	        updateBtn.setOnAction(e -> {
	            Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
	            if (selected != null) {
	                String currentStatus = selected.getStatus();
	                String newStatus;

	                if ("pending".equals(currentStatus)) {
	                    newStatus = "in progress";
	                } else if ("in progress".equals(currentStatus)) {
	                    newStatus = "delivered";
	                } else {
	                    showAlert("Info", "Pengiriman sudah delivered, tidak bisa di-update lagi.");
	                    return;
	                }

	                String result = dc.updateDeliveryStatus(selected.getIdDelivery(), newStatus);
	                if ("success".equals(result)) {
	                    showAlert("Sukses", "Status pengiriman diperbarui menjadi " + newStatus);
	                    loadDeliveries();
	                } else {
	                    showAlert("Error", "Gagal update status: " + result);
	                }
	            } else {
	                showAlert("Warning", "Pilih pengiriman terlebih dahulu!");
	            }
	        });

	        // Tombol kembali hanya untuk MODE ADMIN (courierId == null)
	        if (courierId == null) {
	            Button backBtn = new Button("Kembali");
	            backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #6c757d; -fx-text-fill: white;");
	            backBtn.setOnAction(e -> {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("ADMIN_LIST");
	                }
	            });
	            buttonPanel.getChildren().add(backBtn);
	        }

	        buttonPanel.getChildren().addAll(detailBtn, updateBtn);
	        
	        // Biar courier bisa logout juga dari app
	        if(courierId != null) {
	        	Button backBtn = new Button("Logout");
	            backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #6c757d; -fx-text-fill: white;");
	            backBtn.setOnAction(e -> {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("LOGIN");
	                }
	            });
	            buttonPanel.getChildren().add(backBtn);
	        }
	        
	        mainLayout.setBottom(buttonPanel);
	    }


	/**
	 * Setup Table
	 */
	private void setupTable() {
		TableColumn<Delivery, String> idCol = new TableColumn<>("ID Pengiriman");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idDelivery"));
		idCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> orderCol = new TableColumn<>("ID Order");
		orderCol.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
		orderCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> courierCol = new TableColumn<>("ID Courier");
		courierCol.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
		courierCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(100);
		
		TableColumn<Delivery, String> addressCol = new TableColumn<>("Alamat Tujuan");
		addressCol.setCellValueFactory(new PropertyValueFactory<>("addressDelivery"));
		addressCol.setPrefWidth(250);
		
		TableColumn<Delivery, String> dateCol = new TableColumn<>("Tanggal Pengiriman");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("dateDelivery"));
		dateCol.setPrefWidth(150);
		
		@SuppressWarnings("unchecked")
		TableColumn<Delivery, ?>[] columns = new TableColumn[] {idCol, orderCol, courierCol, statusCol, addressCol, dateCol};
		deliveryTable.getColumns().addAll(columns);
	}

	/**
	 * Load Deliveries
	 */
	private void loadDeliveries() {
	    List<Delivery> deliveries;

	    if (courierId == null || courierId.isEmpty()) {
	        deliveries = dc.getAllDeliveries();
	    } else {
	        deliveries = dc.getDeliveriesByCourierId(courierId);
	    }
	    
	    if (deliveries != null) {
	        ObservableList<Delivery> items = FXCollections.observableArrayList(deliveries);
	        deliveryTable.setItems(items);
	    }
	}

	/**
	 * Show Alert
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Getter and Setter
	public Scene getScene() {
		return scene;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		mainLayout = new BorderPane();
		deliveryTable = new TableView<>();
	}
}
