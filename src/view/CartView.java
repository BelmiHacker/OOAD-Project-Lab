package view;

import controller.CartItemController;
import controller.ProductController;
import controller.CustomerController;
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
import model.CartItem;
import model.Product;
import java.util.List;

/**
 * CartView - JavaFX view untuk keranjang belanja customer
 */
public class CartView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private TableView<CartItem> cartTable;
	
	private CartItemController cic = new CartItemController();
	private ProductController pc = new ProductController();
	private CustomerController cc = new CustomerController();
	
	private String customerId;
	private NavigationListener navigationListener;
	
	private Label totalLabel;
	private Label balanceLabel;
	
	public CartView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadCartItems();
		
		scene = new Scene(mainLayout, 1000, 700);
	}
	
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);
		
		Label title = new Label("Keranjang Belanja");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));
		
		header.getChildren().add(title);
		mainLayout.setTop(header);
		
		// Table
		setupTable();
		mainLayout.setCenter(cartTable);
		
		// Footer Panel with Total
		HBox footerPanel = new HBox(20);
		footerPanel.setPadding(new Insets(20));
		footerPanel.setStyle("-fx-background-color: #f0f0f0;");
		footerPanel.setAlignment(Pos.CENTER_RIGHT);
		
		VBox infoBox = new VBox(10);
		infoBox.setAlignment(Pos.CENTER_RIGHT);
		
		balanceLabel.setFont(Font.font("Arial", 12));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		infoBox.getChildren().addAll(balanceLabel, totalLabel);
		footerPanel.getChildren().add(infoBox);
		
		mainLayout.setBottom(footerPanel);
		
		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(15));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
		
		Button checkoutBtn = new Button("Checkout");
		checkoutBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		checkoutBtn.setOnAction(e -> checkout());
		
		Button deleteBtn = new Button("Hapus Item");
		deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FF5252; -fx-text-fill: white;");
		deleteBtn.setOnAction(e -> {
			CartItem selected = cartTable.getSelectionModel().getSelectedItem();
			if (selected != null) {
				String result = cic.deleteCartItem(selected.getIdCartItem());
				if ("success".equals(result)) {
					showAlert("Sukses", "Item dihapus dari keranjang");
					loadCartItems();
				} else {
					showAlert("Error", "Gagal menghapus item: " + result);
				}
			} else {
				showAlert("Warning", "Pilih item terlebih dahulu!");
			}
		});
		
		Button backBtn = new Button("Kembali");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
		
		buttonPanel.getChildren().addAll(deleteBtn, backBtn, checkoutBtn);
		
		VBox bottomBox = new VBox();
		bottomBox.getChildren().addAll(footerPanel, buttonPanel);
		mainLayout.setBottom(bottomBox);
	}
	
	private void setupTable() {
		TableColumn<CartItem, String> idCol = new TableColumn<>("ID Item");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idCartItem"));
		idCol.setPrefWidth(100);
		
		TableColumn<CartItem, String> productCol = new TableColumn<>("Produk");
		productCol.setCellValueFactory(cellData -> {
			Product p = pc.getProductById(cellData.getValue().getIdProduct());
			return new javafx.beans.property.SimpleStringProperty(p != null ? p.getName() : "N/A");
		});
		productCol.setPrefWidth(200);
		
		TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Jumlah");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("count"));
		qtyCol.setPrefWidth(80);
		
		TableColumn<CartItem, Double> priceCol = new TableColumn<>("Harga Satuan");
		priceCol.setCellValueFactory(cellData -> {
			Product p = pc.getProductById(cellData.getValue().getIdProduct());
			return new javafx.beans.property.SimpleDoubleProperty(p != null ? p.getPrice() : 0).asObject();
		});
		priceCol.setPrefWidth(120);
		
		TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
		subtotalCol.setCellValueFactory(cellData -> {
			Product p = pc.getProductById(cellData.getValue().getIdProduct());
			double subtotal = (p != null ? p.getPrice() : 0) * cellData.getValue().getCount();
			return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
		});
		subtotalCol.setPrefWidth(120);
		
		@SuppressWarnings("unchecked")
		TableColumn<CartItem, ?>[] columns = new TableColumn[] {idCol, productCol, qtyCol, priceCol, subtotalCol};
		cartTable.getColumns().addAll(columns);
	}
	
	private void loadCartItems() {
		// Get all cart items from controller
		List<CartItem> cartItems = cic.getAllCartItems();
		
		if (cartItems != null) {
			// Filter by customer ID
			ObservableList<CartItem> items = FXCollections.observableArrayList();
			for (CartItem ci : cartItems) {
				if (customerId.equals(ci.getIdCustomer())) {
					items.add(ci);
				}
			}
			cartTable.setItems(items);
		}
		
		updateTotalAndBalance();
	}
	
	private void updateTotalAndBalance() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		
		// Calculate total from all items in table
		for (CartItem item : cartTable.getItems()) {
			Product p = pc.getProductById(item.getIdProduct());
			if (p != null) {
				total += p.getPrice() * item.getCount();
			}
		}
		
		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", total));
		
		if (balance < total) {
			totalLabel.setTextFill(Color.RED);
		} else {
			totalLabel.setTextFill(Color.GREEN);
		}
	}
	
	private void checkout() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		
		for (CartItem item : cartTable.getItems()) {
			Product p = pc.getProductById(item.getIdProduct());
			if (p != null) {
				total += p.getPrice() * item.getCount();
			}
		}
		
		if (balance < total) {
			showAlert("Error", "Saldo tidak mencukupi! Silakan top-up terlebih dahulu.");
			return;
		}
		
		if (cartTable.getItems().isEmpty()) {
			showAlert("Warning", "Keranjang belanja kosong!");
			return;
		}
		
		// TODO: Implement checkout logic in OrderController
		showAlert("Info", "Fitur checkout belum diimplementasikan. Coming soon!");
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
		cartTable = new TableView<>();
		totalLabel = new Label();
		balanceLabel = new Label();
	}
}
