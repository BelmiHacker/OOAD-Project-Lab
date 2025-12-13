package view;

import controller.CartItemHandler;
import controller.ProductHandler;
import controller.CustomerHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.CartItem;
import model.Product;

import java.util.List;

/**
 * CartItemDetailView - page to view/update/delete a single cart item.
 * Constructor requires cartId and productId.
 * cartId is used to fetch cart quantity; productId is used to fetch product details.
 */
public class CartItemDetailView {
	// UI
	private Scene scene;
	private BorderPane mainLayout;

	// handlers
	private CartItemHandler cic = new CartItemHandler();
	private ProductHandler pc = new ProductHandler();
	private CustomerHandler cc = new CustomerHandler();

	// state
	private String cartId; // cart id used to fetch cart items and balance
	private CartItem currentItem;
	private Product currentProduct;

	// UI
	private Label productNameLabel;
	private Label priceLabel;
	private Label subtotalLabel;
	private TextField qtyField;
	private Label totalLabel;
	private Label balanceLabel;
	private Button updateBtn;
	private Button deleteBtn;
	private Button backBtn;

	// optional callbacks
	private Runnable onUpdated;
	private NavigationListener navigationListener;

	// constructor requires cartId and productId
	public CartItemDetailView(String cartId, String productId) {
		this.cartId = cartId;
		init();
		setupLayout();
		scene = new Scene(mainLayout, 600, 450);
		// load data for the requested product
		loadForProduct(productId);
	}

	// initialize UI components
	private void init() {
		mainLayout = new BorderPane();
		productNameLabel = new Label();
		priceLabel = new Label();
		subtotalLabel = new Label();
		qtyField = new TextField();
		totalLabel = new Label();
		balanceLabel = new Label();
		updateBtn = new Button("Update");
		deleteBtn = new Button("Hapus");
		backBtn = new Button("Kembali");
	}

	// setup layout and styling
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(30));
		grid.setVgap(12);
		grid.setHgap(12);

		Label prodLbl = new Label("Produk:");
		prodLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		productNameLabel.setFont(Font.font("Arial", 14));
		grid.add(prodLbl, 0, 0);
		grid.add(productNameLabel, 1, 0);

		Label priceLbl = new Label("Harga Satuan:");
		priceLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		priceLabel.setFont(Font.font("Arial", 12));
		grid.add(priceLbl, 0, 1);
		grid.add(priceLabel, 1, 1);

		Label qtyLbl = new Label("Quantity:");
		qtyLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		qtyField.setPrefWidth(120);
		HBox qtyBox = new HBox(8, qtyField, updateBtn, deleteBtn);
		qtyBox.setAlignment(Pos.CENTER_LEFT);
		grid.add(qtyLbl, 0, 2);
		grid.add(qtyBox, 1, 2);

		Label subtotalLbl = new Label("Subtotal:");
		subtotalLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		subtotalLabel.setFont(Font.font("Arial", 12));
		grid.add(subtotalLbl, 0, 3);
		grid.add(subtotalLabel, 1, 3);

		VBox totalsBox = new VBox(6);
		totalsBox.setAlignment(Pos.CENTER_RIGHT);
		balanceLabel.setFont(Font.font("Arial", 12));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		totalsBox.getChildren().addAll(balanceLabel, totalLabel);
		grid.add(totalsBox, 1, 4);

		mainLayout.setCenter(grid);

		HBox bottom = new HBox(8, backBtn);
		bottom.setPadding(new Insets(12));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		mainLayout.setBottom(bottom);

		// actions
		updateBtn.setOnAction(e -> editCartItem());
		deleteBtn.setOnAction(e -> deleteCartItem());
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
	}

	// caller should set this if they want notification after update/delete
	public void setOnUpdated(Runnable onUpdated) {
		this.onUpdated = onUpdated;
	}

	// caller should set this to enable navigation
	public void setNavigationListener(NavigationListener navigationListener) {
		this.navigationListener = navigationListener;
	}

	/**
	 * Load cart item and product info for the given productId from DB.
	 * Returns true when item was found and UI populated.
	 */
	public boolean loadForProduct(String productId) {
		this.currentItem = null;
		try {
			List<CartItem> items = cic.getCartItems(cartId);
			if (items != null) {
				for (CartItem ci : items) {
					if (productId != null && productId.equals(String.valueOf(ci.getIdProduct()))) {
						this.currentItem = ci;
						break;
					}
				}
			}
		} catch (Exception ex) {
			this.currentItem = null;
		}

		if (this.currentItem == null) {
			// load product by string id
			this.currentProduct = pc.getProduct(productId);
			if (this.currentProduct == null) {
				productNameLabel.setText("Produk tidak ditemukan");
				priceLabel.setText("");
				qtyField.setText("0");
				subtotalLabel.setText("");
				updateTotals();
				return false;
			}
			// no cart item but product exists: show product with qty 0
			qtyField.setText("0");
			subtotalLabel.setText("");
			productNameLabel.setText(this.currentProduct.getName());
			priceLabel.setText("Rp " + String.format("%.0f", this.currentProduct.getPrice()));
			updateTotals();
			return false;
		}
		// found cart item -> load product and populate UI
		this.currentProduct = pc.getProduct(String.valueOf(this.currentItem.getIdProduct()));
		loadItem();
		return true;
	}

	private void loadItem() {
		if (currentProduct != null) {
			productNameLabel.setText(currentProduct.getName());
			priceLabel.setText("Rp " + String.format("%.0f", currentProduct.getPrice()));
			qtyField.setText(String.valueOf(currentItem.getCount()));
			subtotalLabel.setText("Rp " + String.format("%.0f", currentProduct.getPrice() * currentItem.getCount()));
		} else {
			productNameLabel.setText("Produk tidak ditemukan");
			priceLabel.setText("");
			qtyField.setText("0");
			subtotalLabel.setText("");
		}
		updateTotals();
	}

	// update balance and total labels
	private void updateTotals() {
		double balance = cc.getBalance(cartId);
		double total = 0;
		int qty = 0;
		double unitPrice = currentProduct != null ? currentProduct.getPrice() : 0;

		try {
			qty = Integer.parseInt(qtyField.getText());
			if (qty < 0) qty = 0;
		} catch (Exception ex) {
			qty = currentItem != null ? currentItem.getCount() : 0;
		}
		total = unitPrice * qty;

		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", total));
		if (balance < total) totalLabel.setTextFill(Color.RED); else totalLabel.setTextFill(Color.GREEN);
	}

	// update cart item quantity
	private void editCartItem() {
		if (currentItem == null || currentProduct == null) {
			showAlert("Warning", "Tidak ada item untuk diupdate.");
			return;
		}
		int newQty;
		try {
			newQty = Integer.parseInt(qtyField.getText());
			if (newQty <= 0) { showAlert("Warning", "Quantity harus lebih besar dari 0."); return; }
		} catch (NumberFormatException ex) {
			showAlert("Warning", "Masukkan angka valid untuk quantity."); return;
		}
		String res = cic.editCartItem(currentItem.getIdCustomer(), currentItem.getIdProduct(), newQty);
		if ("success".equals(res)) {
			showAlert("Sukses", "Quantity diperbarui.");
			if (onUpdated != null) onUpdated.run();
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		} else {
			showAlert("Error", "Gagal mengupdate: " + res);
		}
	}

	// delete cart item
	private void deleteCartItem() {
		if (currentItem == null) {
			showAlert("Warning", "Tidak ada item yang dipilih.");
			return;
		}
		String res = cic.deleteCartItem(currentItem.getIdCustomer(), currentItem.getIdProduct());
		if ("success".equals(res)) {
			showAlert("Sukses", "Item dihapus dari keranjang");
			if (onUpdated != null) onUpdated.run();
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		} else {
			showAlert("Error", "Gagal menghapus item: " + res);
		}
	}

	// show alert dialog
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
}