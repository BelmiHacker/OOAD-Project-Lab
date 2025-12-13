package view;

import controller.CartItemHandler;
import controller.ProductHandler;
import controller.CustomerHandler;
import controller.PromoHandler;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import model.CartItem;
import model.Product;
import model.Promo;

import java.util.List;

/*
 * CartItemDetailView - page to view/update/delete a single cart item.
 * Constructor requires customerId and productId. The view will load data for that product.
 */
public class CartItemDetailView {
	private Scene scene;
	private BorderPane mainLayout;

	private CartItemHandler cic = new CartItemHandler();
	private ProductHandler pc = new ProductHandler();
	private CustomerHandler cc = new CustomerHandler();
	private PromoHandler promoC = new PromoHandler();

	private String customerId;
	private CartItem currentItem;
	private Product currentProduct;

	// UI
	private Label productNameLabel;
	private Label priceLabel;
	private Label subtotalLabel;
	private TextField qtyField;
	private ComboBox<Promo> promoCombo;
	private Label totalLabel;
	private Label balanceLabel;
	private Label discountLabel;
	private Button updateBtn;
	private Button deleteBtn;
	private Button backBtn;

	// optional callbacks
	private Runnable onUpdated;
	private NavigationListener navigationListener;

	// constructor requires customerId and productId
	public CartItemDetailView(String customerId, String productId) {
		this.customerId = customerId;
		init();
		setupLayout();
		scene = new Scene(mainLayout, 600, 450);
		// load data for the requested product
		loadForProduct(productId);
	}

	private void init() {
		mainLayout = new BorderPane();
		productNameLabel = new Label();
		priceLabel = new Label();
		subtotalLabel = new Label();
		qtyField = new TextField();
		promoCombo = new ComboBox<>();
		totalLabel = new Label();
		balanceLabel = new Label();
		discountLabel = new Label();
		updateBtn = new Button("Update");
		deleteBtn = new Button("Hapus");
		backBtn = new Button("Kembali");
	}

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

		Label promoLbl = new Label("Pilih Promo (Opsional):");
		promoLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		promoCombo.setPrefWidth(250);
		grid.add(promoLbl, 0, 4);
		grid.add(promoCombo, 1, 4);

		VBox totalsBox = new VBox(6);
		totalsBox.setAlignment(Pos.CENTER_RIGHT);
		balanceLabel.setFont(Font.font("Arial", 12));
		discountLabel.setFont(Font.font("Arial", 11));
		discountLabel.setTextFill(Color.web("#FF9800"));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		totalsBox.getChildren().addAll(balanceLabel, discountLabel, totalLabel);
		grid.add(totalsBox, 1, 5);

		mainLayout.setCenter(grid);

		HBox bottom = new HBox(8, backBtn);
		bottom.setPadding(new Insets(12));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		mainLayout.setBottom(bottom);

		// load promos
		List<Promo> promos = promoC.getAllPromos();
		if (promos != null) {
			ObservableList<Promo> promoList = FXCollections.observableArrayList(promos);
			promoCombo.setItems(promoList);
			promoCombo.setEditable(false);
			promoCombo.setCellFactory(lv -> new ListCell<>() {
				@Override
				protected void updateItem(Promo item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty || item == null ? "" : item.getCode());
				}
			});
			promoCombo.setButtonCell(new ListCell<>() {
				@Override
				protected void updateItem(Promo item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty || item == null ? "" : item.getCode());
				}
			});
			promoCombo.setConverter(new StringConverter<>() {
				@Override
				public String toString(Promo promo) {
					return promo == null ? "" : promo.getCode();
				}
				@Override
				public Promo fromString(String string) {
					if (string == null) return null;
					return promoList.stream().filter(p -> string.equals(p.getCode())).findFirst().orElse(null);
				}
			});
			promoCombo.setOnAction(e -> updateTotals());
		}

		// actions
		updateBtn.setOnAction(e -> doUpdate());
		deleteBtn.setOnAction(e -> doDelete());
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

	public void setNavigationListener(NavigationListener navigationListener) {
		this.navigationListener = navigationListener;
	}

	/**
	 * Load cart item and product info for the given productId from DB.
	 * Returns true when item was found and UI populated.
	 */
	public boolean loadForProduct(String productId) {
		// try to fetch cart item for this customer/product from DB by scanning cart items
		this.currentItem = null;
		try {
			List<CartItem> items = cic.getCartItems(customerId);
			if (items != null) {
				for (CartItem ci : items) {
					if (ci.getIdProduct() == productId) {
						this.currentItem = ci;
						break;
					}
				}
			}
		} catch (Exception ex) {
			this.currentItem = null;
		}

		if (this.currentItem == null) {
			// still try to load product info so UI won't crash
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
		this.currentProduct = pc.getProduct(this.currentItem.getIdProduct());
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

	private void updateTotals() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;
		int qty = 0;
		double unitPrice = currentProduct != null ? currentProduct.getPrice() : 0;

		try {
			qty = Integer.parseInt(qtyField.getText());
			if (qty < 0) qty = 0;
		} catch (Exception ex) {
			qty = currentItem != null ? currentItem.getCount() : 0;
		}
		total = unitPrice * qty;
		Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
		if (selectedPromo != null) {
			discount = total * (selectedPromo.getDiscountPercentage() / 100.0);
			total = total - discount;
		}

		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		if (discount > 0) discountLabel.setText("Diskon: -Rp " + String.format("%.0f", discount));
		else discountLabel.setText("Diskon: Rp 0");
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", total));
		if (balance < total) totalLabel.setTextFill(Color.RED); else totalLabel.setTextFill(Color.GREEN);
	}

	private void doUpdate() {
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
		String res = cic.editCartItem(customerId, currentItem.getIdProduct(), newQty);
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

	private void doDelete() {
		if (currentItem == null) {
			showAlert("Warning", "Tidak ada item yang dipilih.");
			return;
		}
		String res = cic.deleteCartItem(customerId, currentItem.getIdProduct());
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

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public Scene getScene() {
		return scene;
	}
}