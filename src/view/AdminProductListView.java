package view;

import controller.ProductHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import model.Product;
import java.util.List;

/**
 * AdminProductListView - JavaFX view untuk daftar produk admin dengan edit/delete
 */
public class AdminProductListView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;
	private TextField searchField;
	private TableView<Product> productTable;

	// Handlers
	private ProductHandler pc = new ProductHandler();
	private NavigationListener navigationListener;

	// Constructor
	public AdminProductListView(String adminId) {
		init();
		setupLayout();
		loadProducts();

		scene = new Scene(mainLayout, 1100, 700);
	}

	// Inisialisasi komponen UI
	private void init() {
		mainLayout = new BorderPane();
		searchField = new TextField();
		productTable = new TableView<>();
	}

	/**
	 * Setup layout dan styling JavaFX
	 */
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Label title = new Label("Manajemen Produk");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));

		header.getChildren().add(title);
		mainLayout.setTop(header);

		// Search Box
		HBox searchBox = new HBox(10);
		searchBox.setPadding(new Insets(15));
		searchBox.setStyle("-fx-background-color: #f5f5f5;");

		Label searchLabel = new Label("Cari Produk:");
		searchField.setPrefWidth(300);
		searchField.setStyle("-fx-font-size: 12; -fx-padding: 8;");

		Button searchBtn = new Button("Cari");
		searchBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15;");
		searchBtn.setOnAction(e -> searchProducts());

		searchBox.getChildren().addAll(searchLabel, searchField, searchBtn);
		mainLayout.setTop(new VBox(header, searchBox));

		// Table
		setupTable();
		mainLayout.setCenter(productTable);

		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(15));
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

		//Button edit
		Button editBtn = new Button("Edit");
		editBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color: #2196F3; -fx-text-fill: white;");
		editBtn.setOnAction(e -> {
			Product selected = productTable.getSelectionModel().getSelectedItem();
			if (selected != null) {
				if (navigationListener != null) {
					navigationListener.navigateTo("ADMIN_DETAIL", selected.getIdProduct());
				}
			} else {
				showAlert("Warning", "Pilih produk terlebih dahulu!");
			}
		});

		Button ordersBtn = new Button("Kelola Order");
		ordersBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color: #FF9800; -fx-text-fill: white;");
		ordersBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("ADMIN_ORDERS");
			}
		});

		// ========== BUTTON LIHAT SEMUA COURIER / PENGIRIMAN ==========
		Button couriersBtn = new Button("Lihat Pengiriman");
		couriersBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		couriersBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("ADMIN_COURIER_LIST");
			}
		});

//		Button liat tipe kurir
		Button viewCouriersBtn = new Button("Lihat Kurir");
		viewCouriersBtn.setStyle("-fx-font-size: 12; -fx-padding: 6 20; -fx-background-color: #20C997; -fx-text-fill: white;");
		viewCouriersBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("ADMIN_VIEWALL_COURIERS");
			}
		});



		Button logoutBtn = new Button("Logout");
		logoutBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color:  #999999; -fx-text-fill: white;");
		logoutBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("LOGIN");
			}
		});

		buttonPanel.getChildren().addAll(editBtn, ordersBtn, couriersBtn, viewCouriersBtn, logoutBtn);
		mainLayout.setBottom(buttonPanel);
	}

	/**
	 * Setup kolom tabel produk
	 */
	private void setupTable() {
		TableColumn<Product, String> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
		idCol.setPrefWidth(80);

		TableColumn<Product, String> nameCol = new TableColumn<>("Nama");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(180);

		TableColumn<Product, String> categoryCol = new TableColumn<>("Kategori");
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol.setPrefWidth(120);

		TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setCellFactory(col -> new javafx.scene.control.TableCell<Product, Double>() {
			@Override
			protected void updateItem(Double price, boolean empty) {
				super.updateItem(price, empty);
				if (empty || price == null) {
					setText(null);
				} else {
					setText("Rp " + String.format("%,d", (long)price.doubleValue()));
				}
			}
		});
		priceCol.setPrefWidth(120);

		TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
		stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		stockCol.setPrefWidth(80);

		// Action column with a single Edit button
		TableColumn<Product, Void> actionCol = new TableColumn<>("Aksi");
		actionCol.setPrefWidth(120);
		actionCol.setCellFactory(col -> new javafx.scene.control.TableCell<Product, Void>() {
			private final Button editlBtn = new Button("Edit");
			{
				editlBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 12; -fx-background-color: #2196F3; -fx-text-fill: white;");
				editlBtn.setOnAction(e -> {
					int idx = getIndex();
					if (idx >= 0 && idx < getTableView().getItems().size()) {
						Product p = getTableView().getItems().get(idx);
						if (p != null && navigationListener != null) {
							navigationListener.navigateTo("ADMIN_DETAIL", p.getIdProduct());
						}
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : editlBtn);
			}
		});

		@SuppressWarnings("unchecked")
		TableColumn<Product, ?>[] columns = new TableColumn[] {idCol, nameCol, categoryCol, priceCol, stockCol, actionCol};
		productTable.getColumns().addAll(columns);
	}

	/**
	 * Load data produk ke tabel dengan memanggil ProductHandler
	 */
	private void loadProducts() {
		List<Product> products = pc.getAllProducts();
		if (products != null) {
			ObservableList<Product> items = FXCollections.observableArrayList(products);
			productTable.setItems(items);
		}
	}

	/**
	 * Cari produk berdasarkan keyword
	 */
	private void searchProducts() {
		String keyword = searchField.getText().toLowerCase();
		List<Product> allProducts = pc.getAllProducts();

		ObservableList<Product> filteredList = FXCollections.observableArrayList();
		if (allProducts != null) {
			for (Product p : allProducts) {
				if (p.getName().toLowerCase().contains(keyword) ||
					p.getCategory().toLowerCase().contains(keyword)) {
					filteredList.add(p);
				}
			}
		}
		productTable.setItems(filteredList);
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

	// Getter dan Setter
	public Scene getScene() {
		return scene;
	}

	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
}