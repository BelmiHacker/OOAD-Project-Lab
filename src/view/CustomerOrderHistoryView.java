package view;

import java.util.List;

import controller.CustomerHandler;
import controller.OrderHandler;
import controller.UserHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.OrderHeader;

public class CustomerOrderHistoryView {

	// UI Components
	private Scene scene;
    private BorderPane mainLayout;
    private TableView<OrderHeader> orderTable;
    
    // Handlers
    private OrderHandler oc = new OrderHandler();
    private UserHandler uc = new UserHandler();
    private CustomerHandler ch = new CustomerHandler();
    
    private NavigationListener navigationListener;
	
    // Constructor
    public CustomerOrderHistoryView(String customerId) {
//		TODO ????
    	init();
		setupLayout();
		loadOrders(customerId);
		
		scene = new Scene(mainLayout, 850, 600);
	}
    
    private void setupLayout() {
    	mainLayout.setStyle("-fx-background-color: #f5f5f5;");
    	
    	VBox header = new VBox();
	    header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	    header.setAlignment(Pos.CENTER_LEFT);
	    
	    Label title = new Label("Sejarah Belanja");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    title.setTextFill(Color.web("#333333"));

	    header.getChildren().add(title);
	    mainLayout.setTop(header);
	    
	    setupTable();
	    mainLayout.setCenter(orderTable);
	    
	    HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(15));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setStyle("-fx-background-color: #f0f0f0;");

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

    
    private void loadOrders(String customerId) {
		List<OrderHeader> orders = oc.getCustomerOrders(customerId);
		if (orders != null) {
			ObservableList<OrderHeader> items = FXCollections.observableArrayList(orders);
			orderTable.setItems(items);
		}
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
