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
	
	private void init() {
		mainLayout = new BorderPane();
		orderTable = new TableView<>();
	}
	
	private void setupLayout() {
	    mainLayout.setStyle("-fx-background-color: #f5f5f5;");

	    VBox header = new VBox(5);
	    header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	    header.setAlignment(Pos.CENTER_LEFT);

	    Label title = new Label("Kelola Order & Assign Courier");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
	    title.setTextFill(Color.web("#333333"));

	    header.getChildren().add(title);
	    mainLayout.setTop(header);	    
	    
	    /* BAGIAN BARU */
	    // Table
	    setupTable();
	    mainLayout.setCenter(orderTable);
	    
	    // Button Panel
	    HBox buttonPanel = new HBox(10);
	    buttonPanel.setPadding(new Insets(15));
	    buttonPanel.setAlignment(Pos.CENTER_LEFT);
	    buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
	   
	    // Order Detail Button
	    Button orderDetailBtn = new Button("Order Detail");
	    orderDetailBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
	    orderDetailBtn.setOnAction(e -> {
	    	if (navigationListener != null) {
	    		navigationListener.navigateTo("ADMIN_ORDER_DETAIL");
	    	}
	    });
       
	    // Back Button
	    Button backBtn = new Button("Kembali");
	    backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
	    backBtn.setOnAction(e -> {
	    	if (navigationListener != null) {
	    		navigationListener.goBack();
	    	}
	    });
	    
	    buttonPanel.getChildren().addAll(orderDetailBtn, backBtn);
	    mainLayout.setBottom(buttonPanel);
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
	
}
