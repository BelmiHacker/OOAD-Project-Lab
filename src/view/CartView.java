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
import javafx.scene.control.TextField;
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
import model.CartItem;
import model.Product;
import model.Promo;
import java.util.List;

/**
 * CartView - JavaFX view untuk keranjang belanja customer (single item)
 * Styled to match AdminProductDetailView / ProfileView consistency
 */
public class CartView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane gridLayout;

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

	// Buttons exposed as fields (bottom)
	private Button checkoutBtn;
	private Button backBtn;
	private Button updateBtn;
	private Button deleteBtn;

	// Product display & quantity
	private Label productNameLabel;
	private Label priceLabel;
	private Label subtotalLabel;
	private TextField qtyField;

	// currently displayed item/product
	private CartItem currentItem;
	private Product currentProduct;

	// Constructor
	public CartView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadCartItems();
		scene = new Scene(mainLayout, 900, 700);
	}

	// Setup layout dan styling JavaFX (consistent with AdminProductDetailView)
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		// Header (title only)
		HBox header = new HBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Label title = new Label("Keranjang Belanja");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));

		header.getChildren().add(title);
		mainLayout.setTop(header);

		// Grid layout centered content (match padding/gaps)
		gridLayout.setPadding(new Insets(40, 60, 40, 60));
		gridLayout.setHgap(20);
		gridLayout.setVgap(15);
		gridLayout.setStyle("-fx-background-color: #f5f5f5;");

		// Product Name row
		Label productLabel = new Label("Produk:");
		productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		productNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		gridLayout.add(productLabel, 0, 0);
		gridLayout.add(productNameLabel, 1, 0);

		// Price row
		Label priceLbl = new Label("Harga Satuan:");
		priceLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		priceLabel.setFont(Font.font("Arial", 12));
		gridLayout.add(priceLbl, 0, 1);
		gridLayout.add(priceLabel, 1, 1);

		// Quantity row with update and inline delete buttons
		Label qtyLabel = new Label("Quantity:");
		qtyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		qtyField.setPrefWidth(120);
		qtyField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		updateBtn.setStyle("-fx-font-size: 12; -fx-padding: 6 12; -fx-background-color: #2196F3; -fx-text-fill: white;");
		deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 6 12; -fx-background-color: #FF5252; -fx-text-fill: white;");
		HBox qtyBox = new HBox(8, qtyField, updateBtn, deleteBtn);
		qtyBox.setAlignment(Pos.CENTER_LEFT);
		gridLayout.add(qtyLabel, 0, 2);
		gridLayout.add(qtyBox, 1, 2);

		// Subtotal row
		Label subtotalLbl = new Label("Subtotal:");
		subtotalLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		subtotalLabel.setFont(Font.font("Arial", 12));
		gridLayout.add(subtotalLbl, 0, 3);
		gridLayout.add(subtotalLabel, 1, 3);

		// Promo selection row
		Label promoLbl = new Label("Pilih Promo (Opsional):");
		promoLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		promoCombo.setPrefWidth(250);
		gridLayout.add(promoLbl, 0, 4);
		gridLayout.add(promoCombo, 1, 4);

		// Totals area (checkout moved to bottom panel)
		VBox totalsBox = new VBox(8);
		totalsBox.setAlignment(Pos.CENTER_RIGHT);
		balanceLabel.setFont(Font.font("Arial", 12));
		discountLabel.setFont(Font.font("Arial", 11));
		discountLabel.setTextFill(Color.web("#FF9800"));
		totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		totalsBox.getChildren().addAll(balanceLabel, discountLabel, totalLabel);
		gridLayout.add(totalsBox, 1, 5);

		mainLayout.setCenter(gridLayout);

		// Button Panel at bottom (match TopUpView style: right aligned Back + Checkout)
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(15));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

		// Style bottom buttons to match TopUpView
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		checkoutBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");

		// Add Back then Checkout (same order as TopUpView)
		buttonPanel.getChildren().addAll(backBtn, checkoutBtn);
		mainLayout.setBottom(buttonPanel);

		// Load promos into combo
		List<Promo> promos = promoC.getAllPromos();
		if (promos != null) {
		    ObservableList<Promo> promoList = FXCollections.observableArrayList(promos);
		    promoCombo.setItems(promoList);

		    // Prevent user from typing arbitrary text (avoid mapping issues)
		    promoCombo.setEditable(false);

		    // Show promo.getCode() in dropdown cells
		    promoCombo.setCellFactory(listView -> new javafx.scene.control.ListCell<Promo>() {
		        @Override
		        protected void updateItem(Promo item, boolean empty) {
		            super.updateItem(item, empty);
		            setText(empty || item == null ? "" : item.getCode());
		        }
		    });

		    // Show promo.getCode() in the selected (button) area
		    promoCombo.setButtonCell(new javafx.scene.control.ListCell<Promo>() {
		        @Override
		        protected void updateItem(Promo item, boolean empty) {
		            super.updateItem(item, empty);
		            setText(empty || item == null ? "" : item.getCode());
		        }
		    });

		    // Converter so ComboBox displays code but value is a Promo object
		    promoCombo.setConverter(new javafx.util.StringConverter<Promo>() {
		        @Override
		        public String toString(Promo promo) {
		            return promo == null ? "" : promo.getCode();
		        }

		        @Override
		        public Promo fromString(String string) {
		            if (string == null) return null;
		            return promoList.stream()
		                            .filter(p -> string.equals(p.getCode()))
		                            .findFirst()
		                            .orElse(null);
		        }
		    });

		    promoCombo.setOnAction(e -> updateTotalAndBalance());
		}

		// Setup button actions
		updateBtn.setOnAction(e -> {
			if (currentItem == null || currentProduct == null) {
				showAlert("Warning", "Tidak ada item untuk diupdate.");
				return;
			}
			String txt = qtyField.getText();
			int newQty;
			try {
				newQty = Integer.parseInt(txt);
				if (newQty <= 0) {
					showAlert("Warning", "Quantity harus lebih besar dari 0.");
					return;
				}
			} catch (NumberFormatException ex) {
				showAlert("Warning", "Masukkan angka valid untuk quantity.");
				return;
			}

			String res = cic.editCartItem(customerId, currentItem.getIdProduct(), newQty);
			if ("success".equals(res)) {
				try {
					currentItem = cic.getCartItemById(currentItem.getIdCartItem());
				} catch (Exception ignore) {}
				if (currentItem != null) currentItem.setCount(newQty);
				showAlert("Sukses", "Quantity diperbarui.");
				updateTotalAndBalance();
			} else {
				showAlert("Error", "Gagal mengupdate: " + res);
			}
		});

		// Inline delete next to update and shared delete for bottom if needed
		deleteBtn.setOnAction(e -> performDelete());

		// back action (bottom)
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});

		// checkout (bottom)
		checkoutBtn.setOnAction(e -> checkout());
	}

	// shared delete implementation
	private void performDelete() {
		if (currentItem == null) {
			showAlert("Warning", "Tidak ada item yang dipilih.");
			return;
		}
		String result = cic.deleteCartItem(customerId, currentItem.getIdProduct());
		if ("success".equals(result)) {
			showAlert("Sukses", "Item dihapus dari keranjang");
			clearDisplayedItem();
			updateTotalAndBalance();
		} else {
			showAlert("Error", "Gagal menghapus item: " + result);
		}
	}

	// Load cart items from handler (expecting single item scenario)
	private void loadCartItems() {
		try {
			CartItem items = cic.getCartItems(customerId);
			if (items != null) {
				currentItem = items;
				currentProduct = pc.getProduct(currentItem.getIdProduct());
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
			} else {
				clearDisplayedItem();
			}
		} catch (Exception ex) {
			clearDisplayedItem();
		}
		updateTotalAndBalance();
	}

	private void clearDisplayedItem() {
		currentItem = null;
		currentProduct = null;
		productNameLabel.setText("Keranjang kosong");
		priceLabel.setText("");
		qtyField.setText("");
		subtotalLabel.setText("");
	}

	// Update total amount and customer balance display
	private void updateTotalAndBalance() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;

		int qty = 0;
		double unitPrice = 0;
		if (currentProduct != null) {
			try {
				qty = Integer.parseInt(qtyField.getText());
				if (qty < 0) qty = 0;
			} catch (Exception ex) {
				qty = currentItem != null ? currentItem.getCount() : 0;
			}
			unitPrice = currentProduct.getPrice();
			total = unitPrice * qty;
			subtotalLabel.setText("Subtotal: Rp " + String.format("%.0f", total));
		} else {
			subtotalLabel.setText("");
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
	    if (currentProduct == null || currentItem == null) {
	        showAlert("Warning", "Keranjang belanja kosong!");
	        return;
	    }

	    int qty;
	    try {
	        qty = Integer.parseInt(qtyField.getText());
	        if (qty <= 0) {
	            showAlert("Warning", "Quantity harus lebih besar dari 0.");
	            return;
	        }
	    } catch (NumberFormatException ex) {
	        showAlert("Warning", "Masukkan angka valid untuk quantity.");
	        return;
	    }

	    double balance = cc.getBalance(customerId);
	    double total = currentProduct.getPrice() * qty;
	    double discount = 0;
	    String idPromo = null;

	    Promo selectedPromo = promoCombo.getSelectionModel().getSelectedItem();
	    if (selectedPromo != null) {
	        idPromo = selectedPromo.getIdPromo();
	        discount = total * (selectedPromo.getDiscountPercentage() / 100.0);
	        total = total - discount;
	    }

	    // Normalize empty promo to null and log debug info
	    if (idPromo != null && idPromo.trim().isEmpty()) {
	        idPromo = null;
	    }

	    if (balance < total) {
	        showAlert("Error", "Saldo tidak mencukupi! Silakan top-up terlebih dahulu.");
	        return;
	    }

	    String orderId = "ORD_" + System.currentTimeMillis();
		String orderDetailId = "ORDDET_" + System.currentTimeMillis() + "_" + currentItem.getIdProduct();
		String codePromo = selectedPromo != null ? selectedPromo.getCode() : null;
		String result = oc.checkout(orderId, orderDetailId, customerId, currentItem.getIdProduct(), codePromo, total, qty);

	    if ("success".equals(result)) {
	        showAlert("Success", "Checkout berhasil! Order ID: " + orderId);
	        cic.deleteCartItem(customerId, currentItem.getIdProduct());
	        clearDisplayedItem();
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

	// Getter dan Setters
	public Scene getScene() {
		return scene;
	}

	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		mainLayout = new BorderPane();
		gridLayout = new GridPane();

		totalLabel = new Label();
		balanceLabel = new Label();
		discountLabel = new Label();
		promoCombo = new ComboBox<>();

		// initialize buttons as fields so they can be accessed from other methods/tests
		checkoutBtn = new Button("Checkout");
		backBtn = new Button("Kembali");
		updateBtn = new Button("Update");

		// inline delete next to update
		deleteBtn = new Button("Hapus");

		// product display components
		productNameLabel = new Label("Keranjang kosong");
		priceLabel = new Label();
		subtotalLabel = new Label();
		qtyField = new TextField();
	}
}