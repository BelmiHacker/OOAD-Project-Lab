package view;

import controller.CartItemHandler;
import controller.ProductHandler;
import controller.CustomerHandler;
import controller.PromoHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class CartView {
	private Scene scene;
	private BorderPane mainLayout;

	// Handlers
	private CartItemHandler cic = new CartItemHandler();
	private ProductHandler pc = new ProductHandler();
	private CustomerHandler cc = new CustomerHandler();
	private PromoHandler promoC = new PromoHandler();

	private String customerId;

	// Navigation
	private NavigationListener navigationListener;

	// UI
	private TableView<CartItem> table;
	private Label totalLabel;
	private Label balanceLabel;
	private Label discountLabel;

	public CartView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadCartItems();
		scene = new Scene(mainLayout, 900, 700);
	}

	public void setNavigationListener(NavigationListener navigationListener) {
		this.navigationListener = navigationListener;
	}

	private void init() {
		mainLayout = new BorderPane();

		table = new TableView<>();
		totalLabel = new Label();
		balanceLabel = new Label();
		discountLabel = new Label();
	}

	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		// Header with Back button
		HBox header = new HBox(12);
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Button backBtn = new Button("Kembali");
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			} else {
				showAlert("Error", "Navigation listener not set. Cannot go back.");
			}
		});

		Label title = new Label("Keranjang Belanja");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));

		header.getChildren().addAll(backBtn, title);
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

		HBox bottom = new HBox();
		bottom.setPadding(new Insets(12));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		bottom.getChildren().add(totalsBox);
		mainLayout.setBottom(bottom);
	}

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

	private void updateTotals() {
		double balance = cc.getBalance(customerId);
		double total = 0;
		double discount = 0;
		for (CartItem ci : table.getItems()) {
			Product p = pc.getProduct(ci.getIdProduct());
			if (p != null) total += p.getPrice() * ci.getCount();
		}
		balanceLabel.setText("Saldo Anda: Rp " + String.format("%.0f", balance));
		discountLabel.setText("Diskon: Rp 0");
		totalLabel.setText("Total Belanja: Rp " + String.format("%.0f", total));
		if (balance < total) totalLabel.setTextFill(Color.RED); else totalLabel.setTextFill(Color.GREEN);
	}

	private void openEdit(CartItem item) {
		if (navigationListener != null) {
			navigationListener.navigateTo("CART_DETAIL", customerId, String.valueOf(item.getIdProduct()));
		} else {
			showAlert("Error", "Navigation listener not set. Cannot open detail.");
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