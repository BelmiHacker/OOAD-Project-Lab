package view;

import controller.CartItemHandler;
import controller.ProductHandler;
import controller.CustomerHandler;
import controller.OrderHandler;
import controller.PromoHandler;
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
import model.CartItem;
import model.Product;
import model.Promo;
import java.util.List;

/**
 * CartView - JavaFX view untuk keranjang belanja customer
 */
public class CartView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;
	private TableView<CartItem> cartTable;

	// Handlers
	private CartItemHandler cic = new CartItemHandler();
	private ProductHandler pc = new ProductHandler();
	private CustomerHandler cc = new CustomerHandler();
	private OrderHandler oc = new OrderHandler();
	private PromoHandler promoC = new PromoHandler();

	// State
	private String customerId;
	private NavigationListener navigationListener;

	// UI Elements
	private Label totalLabel;
	private Label balanceLabel;
	private Label discountLabel;
	private ComboBox<Promo> promoCombo;

	// Constructor
	public CartView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadCartItems();
		
		scene = new Scene(mainLayout, 1000, 700);
	}

	// Setup layout dan styling JavaFX
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
		
		// Footer Panel with Total and Promo
		HBox footerPanel = new HBox(20);
		footerPanel.setPadding(new Insets(20));
		footerPanel.setStyle("-fx-background-color: #f0f0f0;");
		footerPanel.setAlignment(Pos.CENTER_RIGHT);
		
		VBox promoBox = new VBox(10);
		promoBox.setAlignment(Pos.CENTER_LEFT);
		promoBox.setPrefWidth(300);
		
		Label promoLabel = new Label("Pilih Promo (Opsional):");
		promoLabel.setFont(Font.font("Arial", 11));
		
		promoCombo.setPrefWidth(250);
		
		// Load promos
		List<Promo> promos = promoC.getAllPromos();
		if (promos != null) {
			ObservableList<Promo> promoList = FXCollections.observableArrayList(promos);
			promoCombo.setItems(promoList);
			promoCombo.setCellFactory(col -> new javafx.scene.control.ListCell<Promo>() {
				@Override
				protected void updateItem(Promo promo, boolean empty) {
					super.updateItem(promo, empty);
					setText(empty ? null : promo.getCode() + " - " + promo.getHeadline() + " (" + promo.getDiscountPercentage() + "%)");
				}
			});
			promoCombo.setButtonCell(new javafx.scene.control.ListCell<Promo>() {
				@Override
				protected void updateItem(Promo promo, boolean empty) {
					super.updateItem(promo, empty);
					setText(empty ? null : promo.getCode() + " - " + promo.getHeadline() + " (" + promo.getDiscountPercentage() + "%)");
				}
			});
			promoCombo.setOnAction(e -> updateTotalAndBalance());
		}
		
		promoBox.getChildren().addAll(promoLabel, promoCombo);
		footerPanel.getChildren().add(promoBox);
		
		VBox infoBox = new VBox(10);
		infoBox.setAlignment(Pos.CENTER_RIGHT);
		
		balanceLabel.setFont(Font.font("Arial", 12));
		discountLabel.setFont(Font.font("Arial", 11));
		discountLabel.setTextFill(Color.web("#FF9800"));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		
		infoBox.getChildren().addAll(balanceLabel, discountLabel, totalLabel);
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
				String result = cic.deleteCartItem(customerId, selected.getIdProduct());
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

	// Setup table columns
	private void setupTable() {
		TableColumn<CartItem, String> idCol = new TableColumn<>("ID Item");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idCartItem"));
		idCol.setPrefWidth(100);
		
		TableColumn<CartItem, String> productCol = new TableColumn<>("Produk");
		productCol.setCellValueFactory(cellData -> {
			Product p = pc.getProduct(cellData.getValue().getIdProduct());
			return new javafx.beans.property.SimpleStringProperty(p != null ? p.getName() : "N/A");
		});
		productCol.setPrefWidth(200);
		
		TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Jumlah");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("count"));
		qtyCol.setPrefWidth(80);
		
		TableColumn<CartItem, Double> priceCol = new TableColumn<>("Harga Satuan");
		priceCol.setCellValueFactory(cellData -> {
			Product p = pc.getProduct(cellData.getValue().getIdProduct());
			return new javafx.beans.property.SimpleDoubleProperty(p != null ? p.getPrice() : 0).asObject();
		});
		priceCol.setPrefWidth(120);
		
		TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
		subtotalCol.setCellValueFactory(cellData -> {
			Product p = pc.getProduct(cellData.getValue().getIdProduct());
			double subtotal = (p != null ? p.getPrice() : 0) * cellData.getValue().getCount();
			return new javafx.beans.property.SimpleDoubleProperty(subtotal).asObject();
		});
		subtotalCol.setPrefWidth(120);
		
		@SuppressWarnings("unchecked")
		TableColumn<CartItem, ?>[] columns = new TableColumn[] {idCol, productCol, qtyCol, priceCol, subtotalCol};
		cartTable.getColumns().addAll(columns);
	}

	// Load cart items from handler
	private void loadCartItems() {
		// Get cart items for this customer
		CartItem cartItems = cic.getCartItems(customerId);

		if (cartItems != null) {
			ObservableList<CartItem> items = FXCollections.observableArrayList(cartItems);
			cartTable.setItems(items);
		}

		updateTotalAndBalance();
	}

	// Update total amount and customer balance display
	private void updateTotalAndBalance() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;
		
		// Calculate total from all items in table
		for (CartItem item : cartTable.getItems()) {
			Product p = pc.getProduct(item.getIdProduct());
			if (p != null) {
				total += p.getPrice() * item.getCount();
			}
		}
		
		// Apply promo if selected
		Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
		if (selectedPromo != null) {
			discount = total * (selectedPromo.getDiscountPercentage() / 100.0);
			total = total - discount;
		}
		
		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		if (discount > 0) {
			discountLabel.setText("Diskon: -Rp " + String.format("%.0f", discount));
		} else {
			discountLabel.setText("Diskon: Rp 0");
		}
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", total));
		
		if (balance < total) {
			totalLabel.setTextFill(Color.RED);
		} else {
			totalLabel.setTextFill(Color.GREEN);
		}
	}

	// Handle checkout process
	private void checkout() {
		List<CartItem> cartItems = cartTable.getItems();
		
		if (cartItems.isEmpty()) {
			showAlert("Warning", "Keranjang belanja kosong!");
			return;
		}
		
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;
		String idPromo = null;
		
		// Calculate total amount from cart items
		for (CartItem item : cartItems) {
			Product p = pc.getProduct(item.getIdProduct());
			if (p != null) {
				total += p.getPrice() * item.getCount();
			}
		}
		
		// Apply promo if selected
		Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
		if (selectedPromo != null) {
			idPromo = selectedPromo.getIdPromo();
			discount = total * (selectedPromo.getDiscountPercentage() / 100.0);
			total = total - discount;
		}
		
		// Check balance
		if (balance < total) {
			showAlert("Error", "Saldo tidak mencukupi! Silakan top-up terlebih dahulu.");
			return;
		}
		
		// Generate order ID
		String orderId = "ORD_" + System.currentTimeMillis();
		
		// Checkout with promo
		String result = oc.checkout(orderId, customerId, idPromo, total);
		
		if ("success".equals(result)) {
			// Create order details for each cart item
			for (CartItem item : cartItems) {
				String orderDetailId = "ORDDET_" + System.currentTimeMillis() + "_" + item.getIdProduct();
				oc.addOrderDetail(orderDetailId, orderId, item.getIdProduct(), item.getCount());
			}
			
			showAlert("Success", "Checkout berhasil! Order ID: " + orderId);
			cartTable.getItems().clear();
			promoCombo.getSelectionModel().clearSelection();
			updateTotalAndBalance();
		} else {
			showAlert("Error", "Checkout gagal: " + result);
		}
	}

	// Show alert dialog
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Getter dan Setter
	public Scene getScene() {
		return scene;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		mainLayout = new BorderPane();
		cartTable = new TableView<>();
		totalLabel = new Label();
		balanceLabel = new Label();
		discountLabel = new Label();
		promoCombo = new ComboBox<>();
	}
}
