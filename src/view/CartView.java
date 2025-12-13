package view;

import controller.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
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

/**
 * CartView - JavaFX view untuk menampilkan dan mengelola keranjang belanja customer
 */
public class CartView {
	// UI
	private Scene scene;
	private BorderPane mainLayout;

	// Handlers
	private CartItemHandler cic = new CartItemHandler();
	private ProductHandler pc = new ProductHandler();
	private CustomerHandler cc = new CustomerHandler();
	private PromoHandler promoC = new PromoHandler();

	private String customerId;
	private Runnable onUpdated;

	// Navigation
	private NavigationListener navigationListener;

	// UI
	private TableView<CartItem> table;
	private Label totalLabel;
	private Label balanceLabel;
	private Label discountLabel;
	private Button checkoutBtn;
	private ComboBox<Promo> promoCombo;

	// Constructor
	public CartView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadCartItems();
		scene = new Scene(mainLayout, 900, 600);
	}

	// Initialize UI components
	private void init() {
		mainLayout = new BorderPane();

		table = new TableView<>();
		totalLabel = new Label();
		balanceLabel = new Label();
		discountLabel = new Label();
		checkoutBtn = new Button("Checkout");
		promoCombo = new ComboBox<>();
	}

	// Setup layout and styling
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		// Header
		HBox header = new HBox(12);
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Label title = new Label("Keranjang Belanja");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));

		header.getChildren().addAll(title);
		mainLayout.setTop(header);

		// Table columns
		TableColumn<CartItem, String> nameCol = new TableColumn<>("Produk");
		nameCol.setCellValueFactory(cell -> {
			Product p = pc.getProduct(cell.getValue().getIdProduct());
			String nm = p != null ? p.getName() : "Produk tidak ditemukan";
			return new javafx.beans.property.SimpleStringProperty(nm);
		});
		nameCol.setPrefWidth(300);

		TableColumn<CartItem, String> priceCol = new TableColumn<>("Harga");
		priceCol.setCellValueFactory(cell -> {
			Product p = pc.getProduct(cell.getValue().getIdProduct());
			double v = p != null ? p.getPrice() : 0;
			return new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", v));
		});
		priceCol.setPrefWidth(140);

		TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Qty");
		qtyCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getCount()).asObject());
		qtyCol.setPrefWidth(80);

		TableColumn<CartItem, String> subtotalCol = new TableColumn<>("Subtotal");
		subtotalCol.setCellValueFactory(cell -> {
			Product p = pc.getProduct(cell.getValue().getIdProduct());
			double v = p != null ? p.getPrice() * cell.getValue().getCount() : 0;
			return new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", v));
		});
		subtotalCol.setPrefWidth(160);

		// Action column with Edit and Delete buttons
		TableColumn<CartItem, Void> actionCol = new TableColumn<>("Aksi");
		actionCol.setPrefWidth(160);
		actionCol.setCellFactory(col -> new TableCell<>() {
			private final Button editBtn = new Button("Edit");
			private final Button delBtn = new Button("Hapus");
			private final HBox box = new HBox(8, editBtn, delBtn);

			{
				box.setAlignment(Pos.CENTER_LEFT);
				editBtn.setOnAction(e -> {
					CartItem item = getTableView().getItems().get(getIndex());
					openEdit(item);
				});
				delBtn.setOnAction(e -> {
					CartItem item = getTableView().getItems().get(getIndex());
					String res = cic.deleteCartItem(customerId, item.getIdProduct());
					if ("success".equals(res)) {
						loadCartItems();
						showAlert("Sukses", "Item dihapus dari keranjang");
					} else {
						showAlert("Error", "Gagal menghapus: " + res);
					}
				});
			}

			@Override
			protected void updateItem(Void unused, boolean empty) {
				super.updateItem(unused, empty);
				setGraphic(empty ? null : box);
			}
		});

		table.getColumns().addAll(nameCol, priceCol, qtyCol, subtotalCol, actionCol);

		VBox center = new VBox(12, table);
		center.setPadding(new Insets(20));
		mainLayout.setCenter(center);

		// Totals area at bottom
		VBox totalsBox = new VBox(6);
		totalsBox.setPadding(new Insets(10));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		balanceLabel.setFont(Font.font("Arial", 12));
		discountLabel.setFont(Font.font("Arial", 11));
		discountLabel.setTextFill(Color.web("#FF9800"));
		totalsBox.getChildren().addAll(balanceLabel, discountLabel, totalLabel);

		// Buttons
		checkoutBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FFEB3B; -fx-text-fill: black;");

		checkoutBtn.setOnAction(e -> {
			checkout();
		});

		Button backBtn = new Button("Kembali");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			} else {
				showAlert("Error", "Navigation listener not set. Cannot go back.");
			}
		});

		HBox buttonRow = new HBox(10);
		buttonRow.setAlignment(Pos.CENTER_RIGHT);
		Region btnSpacer = new Region();
		HBox.setHgrow(btnSpacer, Priority.ALWAYS);
		buttonRow.getChildren().addAll(btnSpacer, backBtn, checkoutBtn);

		// Promo box on the left (new)
		VBox promoBox = new VBox(6);
		promoBox.setPadding(new Insets(10, 0, 0, 10));
		Label promoLbl = new Label("Pilih Promo (Opsional):");
		promoLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		promoCombo.setPrefWidth(220);
		promoBox.getChildren().addAll(promoLbl, promoCombo);

		// Load promos into combo
		List<Promo> promos = promoC.getAllPromos();
		if (promos != null) {
			ObservableList<Promo> promoList = FXCollections.observableArrayList();
			// insert a null item representing "None" / no promo
			promoList.add(null);
			promoList.addAll(promos);
			promoCombo.setItems(promoList);
			promoCombo.setEditable(false);

			// show "(None)" when item is null
			promoCombo.setCellFactory(lv -> new ListCell<>() {
				@Override
				protected void updateItem(Promo item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText("");
					} else if (item == null) {
						setText("(None)");
					} else {
						setText(item.getCode());
					}
				}
			});
			promoCombo.setButtonCell(new ListCell<>() {
				@Override
				protected void updateItem(Promo item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText("");
					} else if (item == null) {
						setText("(None)");
					} else {
						setText(item.getCode());
					}
				}
			});
			promoCombo.setConverter(new StringConverter<>() {
				@Override
				public String toString(Promo promo) {
					return promo == null ? "(None)" : promo.getCode();
				}
				@Override
				public Promo fromString(String string) {
					if (string == null) return null;
					// try to match code to existing promos; fall back to null meaning none
					for (Promo p : promoCombo.getItems()) {
						if (p != null && string.equals(p.getCode())) return p;
					}
					return null;
				}
			});
			promoCombo.setOnAction(e -> updateTotals());
			// default to "(None)"
			promoCombo.getSelectionModel().selectFirst();
		}

		// Bottom layout: promo on left, spacer, totals on right
		HBox bottomRow = new HBox(10);
		bottomRow.setPadding(new Insets(0, 10, 0, 10));
		bottomRow.setAlignment(Pos.CENTER_LEFT);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		bottomRow.getChildren().addAll(promoBox, spacer, totalsBox);

		VBox bottom = new VBox(8);
		bottom.setPadding(new Insets(12));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		bottom.getChildren().addAll(bottomRow, buttonRow);

		mainLayout.setBottom(bottom);
	}

	// Load cart items from DB
	private void loadCartItems() {
		try {
			List<CartItem> items = cic.getCartItems(customerId);
			ObservableList<CartItem> list = FXCollections.observableArrayList();
			if (items != null) list.addAll(items);
			table.setItems(list);
		} catch (Exception ex) {
			table.setItems(FXCollections.observableArrayList());
		}
		updateTotals();
	}

	// Update totals display
	private void updateTotals() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;
		for (CartItem ci : table.getItems()) {
			Product p = pc.getProduct(ci.getIdProduct());
			if (p != null) total += p.getPrice() * ci.getCount();
		}

		// apply promo discount if any
		Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
		if (selectedPromo != null) {
			double pct = selectedPromo.getDiscountPercentage();
			discount = total * (pct / 100.0);
		}

		double totalAfterDiscount = total - discount;

		// update labels
		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		if (discount > 0) discountLabel.setText("Diskon: -Rp " + String.format("%.0f", discount));
		else discountLabel.setText("Diskon: Rp 0");
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", totalAfterDiscount));
		if (balance < totalAfterDiscount) totalLabel.setTextFill(Color.RED); else totalLabel.setTextFill(Color.GREEN);

		// Enable checkout only when there is a total and balance covers it
		if (checkoutBtn != null) {
			checkoutBtn.setDisable(totalAfterDiscount <= 0 || balance < totalAfterDiscount);
		}
	}

	// Open edit view for cart item
	private void openEdit(CartItem item) {
		if (navigationListener != null) {
			navigationListener.navigateTo("CART_DETAIL", item.getIdCartItem(), item.getIdProduct());
		} else {
			showAlert("Error", "Navigation listener not set. Cannot open detail.");
		}
	}

	// Handle checkout process
	private void checkout() {
		// ensure there are items
		if (table.getItems() == null || table.getItems().isEmpty()) {
			showAlert("Warning", "Keranjang belanja kosong!");
			return;
		}

		// compute totals
		double totalBefore = 0;
		for (CartItem ci : table.getItems()) {
			Product p = pc.getProduct(ci.getIdProduct());
			if (p != null) totalBefore += p.getPrice() * ci.getCount();
		}
		if (totalBefore <= 0) {
			showAlert("Warning", "Total belanja tidak valid.");
			return;
		}

		// apply promo if any
		Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
		double discount = 0;
		String promoId = null;
		if (selectedPromo != null) {
			promoId = selectedPromo.getIdPromo(); // use promo id for header
			discount = totalBefore * (selectedPromo.getDiscountPercentage() / 100.0);
		}

		// check balance
		double totalAfterDiscount = totalBefore - discount;
		double balance = cc.getBalance(customerId);
		if (balance < totalAfterDiscount) {
			showAlert("Error", "Saldo tidak mencukupi! Silakan top-up terlebih dahulu.");
			return;
		}

		// create order header first
		OrderHandler oc = new OrderHandler();
		String orderId = oc.saveDataOrderHeader(customerId, promoId, totalAfterDiscount);
		if (orderId == null || orderId.trim().isEmpty()) {
			showAlert("Error", "Gagal menyimpan order header: " + orderId);
			return;
		}

		// add each cart item as order detail
		int idx = 0;
		for (CartItem ci : table.getItems()) {
			idx++;
			Product p = pc.getProduct(ci.getIdProduct());
			if (p == null) {
				showAlert("Error", "Produk tidak ditemukan untuk item cart.");
				return;
			}
			String productIdStr = String.valueOf(ci.getIdProduct());
			String detailRes = oc.saveOrderDetail(orderId, productIdStr, ci.getCount());
			if (!"success".equals(detailRes)) {
				showAlert("Error", "Gagal menyimpan detail order untuk produk " + productIdStr + ": " + detailRes);
				return;
			}

			// update product stock
			String stockRes = pc.editProductStock(productIdStr, p.getStock() - ci.getCount());
			if (!"success".equals(stockRes)) {
				showAlert("Error", "Gagal mengupdate stok untuk produk " + productIdStr + ": " + stockRes);
				return;
			}
		}

		// on success remove all items from cart and refresh
		for (CartItem ci : table.getItems()) {
			cic.deleteCartItem(customerId, ci.getIdProduct());
		}
		loadCartItems();
		// reset promo to none
		if (promoCombo != null && !promoCombo.getItems().isEmpty()) {
			promoCombo.getSelectionModel().selectFirst();
		} else if (promoCombo != null) {
			promoCombo.getSelectionModel().clearSelection();
		}
		updateTotals();

		showAlert("Success", "Checkout berhasil! Order ID: " + orderId);
		if (onUpdated != null) onUpdated.run();
		if (navigationListener != null) navigationListener.goBack();
	}

	// Show alert dialog
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Getters dan Setters
	public Scene getScene() {
		return scene;
	}

	public void setOnUpdated(Runnable onUpdated) {
		this.onUpdated = onUpdated;
	}

	// Set navigation listener
	public void setNavigationListener(NavigationListener navigationListener) {
		this.navigationListener = navigationListener;
	}
}