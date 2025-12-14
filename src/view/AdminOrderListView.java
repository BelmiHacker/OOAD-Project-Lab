package view;

import controller.OrderHandler;
import controller.DeliveryController;
import controller.CourierController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
 * AdminOrderListView - JavaFX view untuk admin mengelola orders dan assign courier
 */
public class AdminOrderListView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private TableView<OrderHeader> orderTable;
	
	private OrderHandler oc = new OrderHandler();
	private DeliveryController dc = new DeliveryController();
	private CourierController cc = new CourierController();
	
	private NavigationListener navigationListener;
	
	public AdminOrderListView(String adminId) {
		init();
		setupLayout();
		loadOrders();
		
		scene = new Scene(mainLayout, 1100, 700);
	}
	
	private void setupLayout() {
	    mainLayout.setStyle("-fx-background-color: #f5f5f5;");

	    VBox header = new VBox();
	    header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	    header.setAlignment(Pos.CENTER_LEFT);

	    Label title = new Label("Kelola Order & Assign Courier");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    title.setTextFill(Color.web("#333333"));

	    header.getChildren().add(title);
	    mainLayout.setTop(header);

	    // Table
	    setupTable();
	    mainLayout.setCenter(orderTable);

	    // Panel assign courier (atas tombol)
	    VBox assignPanel = new VBox(10);
	    assignPanel.setPadding(new Insets(15));
	    assignPanel.setStyle("-fx-background-color: #f0f0f0;");

	    HBox courierPanel = new HBox(10);
	    courierPanel.setAlignment(Pos.CENTER_LEFT);

	    Label courierLabel = new Label("Pilih Courier:");
	    courierLabel.setFont(Font.font("Arial", 12));

	    ComboBox<Courier> courierCombo = new ComboBox<>();
	    courierCombo.setPrefWidth(250);

	    List<Courier> couriers = cc.getAllCouriers();
	    if (couriers != null) {
	        ObservableList<Courier> courierList = FXCollections.observableArrayList(couriers);
	        courierCombo.setItems(courierList);
	        courierCombo.setCellFactory(col -> new javafx.scene.control.ListCell<Courier>() {
	            @Override
	            protected void updateItem(Courier courier, boolean empty) {
	                super.updateItem(courier, empty);
	                setText(empty ? null : courier.getIdCourier() + " - " + courier.getVehicleType());
	            }
	        });
	        courierCombo.setButtonCell(new javafx.scene.control.ListCell<Courier>() {
	            @Override
	            protected void updateItem(Courier courier, boolean empty) {
	                super.updateItem(courier, empty);
	                setText(empty ? null : courier.getIdCourier() + " - " + courier.getVehicleType());
	            }
	        });
	    }

	    Button assignBtn = new Button("Assign Courier");
	    assignBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
	    assignBtn.setOnAction(e -> {
	        OrderHeader selected = orderTable.getSelectionModel().getSelectedItem();
	        Courier selectedCourier = courierCombo.getSelectionModel().getSelectedItem();

	        if (selected == null) {
	            showAlert("Warning", "Pilih order terlebih dahulu!");
	            return;
	        }

	        if (selectedCourier == null) {
	            showAlert("Warning", "Pilih courier terlebih dahulu!");
	            return;
	        }

	        String deliveryId = "DEL_" + System.currentTimeMillis();
	        String result = dc.assignCourier(deliveryId, selected.getIdOrder(), selectedCourier.getIdCourier());

	        if ("success".equals(result)) {
	            showAlert("Sukses", "Courier berhasil di-assign ke order!");
	            loadOrders();
	            courierCombo.getSelectionModel().clearSelection();
	        } else {
	            showAlert("Error", "Gagal assign courier: " + result);
	        }
	    });

	    courierPanel.getChildren().addAll(courierLabel, courierCombo, assignBtn);
	    assignPanel.getChildren().add(courierPanel);

	    // ==== PANEL TOMBOL BAWAH ====
	    HBox buttonPanel = new HBox(10);
	    buttonPanel.setPadding(new Insets(15));
	    buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
	    buttonPanel.setAlignment(Pos.CENTER_RIGHT);

	    // 1) Tombol LIHAT DETAIL ORDER
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


	    // 2) Tombol KEMBALI
	    Button backBtn = new Button("Kembali");
	    backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
	    backBtn.setOnAction(e -> {
	        if (navigationListener != null) {
	            navigationListener.goBack();
	        }
	    });

	    buttonPanel.getChildren().addAll(detailBtn, backBtn);

	    VBox bottomBox = new VBox();
	    bottomBox.getChildren().addAll(assignPanel, buttonPanel);
	    mainLayout.setBottom(bottomBox);
	}
	
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
	
	private void loadOrders() {
		List<OrderHeader> orders = oc.getAllOrders();
		if (orders != null) {
			ObservableList<OrderHeader> items = FXCollections.observableArrayList(orders);
			orderTable.setItems(items);
		}
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	private void init() {
		mainLayout = new BorderPane();
		orderTable = new TableView<>();
	}
}
