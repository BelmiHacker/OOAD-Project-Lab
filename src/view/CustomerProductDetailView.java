package view;

import controller.ProductHandler;
import controller.CartItemHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.Product;
import model.CartItem;

/**
 * CustomerProductDetailView - JavaFX view untuk detail produk dan tambah ke keranjang
 */
public class CustomerProductDetailView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane detailGrid;

	// Handlers
	private ProductHandler pc = new ProductHandler();
	private CartItemHandler cic = new CartItemHandler();
	private NavigationListener navigationListener;

	// State
	private String productId;
	private String customerId;
	private Product product;

	// UI Elements
	private Label idLabel;
	private Label nameLabel;
	private Label categoryLabel;
	private Label priceLabel;
	private Label stockLabel;
	private Label quantityLabel;
	
	private Label idValue;
	private Label nameValue;
	private Label categoryValue;
	private Label priceValue;
	private Label stockValue;
	
	private Spinner<Integer> quantitySpinner;
	
	private Button addToCartBtn;
	private Button backBtn;

	// Constructor
	public CustomerProductDetailView(String productId, String customerId) {
		this.productId = productId;
		this.customerId = customerId;
		init();
		loadProductDetail();
		setupLayout();
		setupActions();
		
		scene = new Scene(mainLayout, 900, 700);
	}

	/**
	 * Load detail produk dari handler
	 */
	private void loadProductDetail() {
		product = pc.getProduct(productId);
		if (product == null) {
			showAlert("Error", "Produk tidak ditemukan!");
		}
	}

	/**
	 * Setup layout dan styling JavaFX
	 */
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);
		
		Label title = new Label("Detail Produk");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));
		
		header.getChildren().add(title);
		mainLayout.setTop(header);
		
		// Detail Grid
		detailGrid.setPadding(new Insets(40, 60, 40, 60));
		detailGrid.setHgap(20);
		detailGrid.setVgap(15);
		detailGrid.setStyle("-fx-background-color: #f5f5f5;");
		
		// ID Row
		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		idValue.setFont(Font.font("Arial", 12));
		detailGrid.add(idLabel, 0, 0);
		detailGrid.add(idValue, 1, 0);
		
		// Name Row
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		nameValue.setFont(Font.font("Arial", 12));
		detailGrid.add(nameLabel, 0, 1);
		detailGrid.add(nameValue, 1, 1);
		
		// Category Row
		categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		categoryValue.setFont(Font.font("Arial", 12));
		detailGrid.add(categoryLabel, 0, 2);
		detailGrid.add(categoryValue, 1, 2);
		
		// Price Row
		priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		priceValue.setFont(Font.font("Arial", 12));
		detailGrid.add(priceLabel, 0, 3);
		detailGrid.add(priceValue, 1, 3);
		
		// Stock Row
		stockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		stockValue.setFont(Font.font("Arial", 12));
		detailGrid.add(stockLabel, 0, 4);
		detailGrid.add(stockValue, 1, 4);
		
		// Quantity Row
		quantityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		detailGrid.add(quantityLabel, 0, 6);
		detailGrid.add(quantitySpinner, 1, 6);
		
		// Populate values
		if (product != null) {
			idValue.setText(product.getIdProduct());
			nameValue.setText(product.getName());
			categoryValue.setText(product.getCategory());
			priceValue.setText("Rp " + String.format("%,d", (long)product.getPrice()));
			stockValue.setText(String.valueOf(product.getStock()));
		}		mainLayout.setCenter(detailGrid);
		
		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(20));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
		
		addToCartBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		
		buttonPanel.getChildren().addAll(backBtn, addToCartBtn);
		mainLayout.setBottom(buttonPanel);
	}

	/**
	 * Setup action handlers untuk tombol
	 */
	private void setupActions() {
		addToCartBtn.setOnAction(e -> {
			int quantity = quantitySpinner.getValue();
			
			if (quantity <= 0) {
				showAlert("Error", "Kuantitas harus lebih dari 0!");
				return;
			}
			
			if (quantity > product.getStock()) {
				showAlert("Error", "Stok tidak cukup! Stok tersedia: " + product.getStock());
				return;
			}
			
			CartItem cartItem = new CartItem();
			cartItem.setIdProduct(productId);
			cartItem.setIdCustomer(customerId);
			cartItem.setCount(quantity);
			
			String result = cic.createCartItem(customerId, productId, quantity);
			if ("success".equals(result)) {
				showAlert("Sukses", "Produk berhasil ditambahkan ke keranjang!");
				quantitySpinner.getValueFactory().setValue(1);
			} else {
				showAlert("Error", "Gagal menambahkan ke keranjang: " + result);
			}
		});
		
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
	}

	/**
	 * Tampilkan alert dialog
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Getter Setter
	public Scene getScene() {
		return scene;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		addToCartBtn = new Button("Tambah ke Keranjang");
		backBtn = new Button("Kembali");
		
		idLabel = new Label("ID Produk:");
		nameLabel = new Label("Nama Produk:");
		categoryLabel = new Label("Kategori:");
		priceLabel = new Label("Harga:");
		stockLabel = new Label("Stok:");
		quantityLabel = new Label("Jumlah:");
		
		idValue = new Label();
		nameValue = new Label();
		categoryValue = new Label();
		priceValue = new Label();
		stockValue = new Label();
		
		quantitySpinner = new Spinner<>();
		quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
		quantitySpinner.setPrefWidth(100);
		
		mainLayout = new BorderPane();
		detailGrid = new GridPane();
	}
}
