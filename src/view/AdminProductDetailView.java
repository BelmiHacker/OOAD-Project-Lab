package view;

import controller.ProductController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import model.Product;

/**
 * AdminProductDetailView - JavaFX view untuk detail produk admin dengan
 * edit/create
 */
public class AdminProductDetailView {

	private Scene scene;
	private BorderPane mainLayout;
	private GridPane detailGrid;

	private ProductController pc = new ProductController();
	private NavigationListener navigationListener;

	private String productId;
	private Product product;
	private boolean isNew;

	private Label idLabel;
	private Label nameLabel;
	private Label categoryLabel;
	private Label priceLabel;
	private Label stockLabel;

	private TextField idField;
	private TextField nameField;
	private TextField categoryField;
	private TextField priceField;
	private TextField stockField;

	private Button saveBtn;
	private Button backBtn;

	// Constructor untuk edit/create produk
	public AdminProductDetailView(String productId, String adminId) {
		this.productId = productId;
		this.isNew = "NEW".equals(productId);
		init();
		if (!isNew) {
			loadProductDetail();
		} else {
			this.product = new Product();
		}
		setupLayout();
		setupActions();

		scene = new Scene(mainLayout, 900, 700);
	}

	private void loadProductDetail() {
		if (!isNew) {
			product = pc.getProductById(productId);
			if (product == null) {
				showAlert("Error", "Produk tidak ditemukan!");
				product = new Product();
			}
		}
	}

	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Label title = new Label(isNew ? "Tambah Produk Baru" : "Edit Produk");
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
		idField.setPrefWidth(300);
		idField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		idField.setDisable(!isNew);
		detailGrid.add(idLabel, 0, 0);
		detailGrid.add(idField, 1, 0);

		// Name Row
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		nameField.setPrefWidth(300);
		nameField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		detailGrid.add(nameLabel, 0, 1);
		detailGrid.add(nameField, 1, 1);

		// Category Row
		categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		categoryField.setPrefWidth(300);
		categoryField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		detailGrid.add(categoryLabel, 0, 2);
		detailGrid.add(categoryField, 1, 2);

		// Price Row
		priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		priceField.setPrefWidth(300);
		priceField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		detailGrid.add(priceLabel, 0, 3);
		detailGrid.add(priceField, 1, 3);

		// Stock Row
		stockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		stockField.setPrefWidth(300);
		stockField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		detailGrid.add(stockLabel, 0, 4);
		detailGrid.add(stockField, 1, 4);

		// Populate values if editing
		if (!isNew && product != null) {
			idField.setText(product.getIdProduct());
			nameField.setText(product.getName());
			categoryField.setText(product.getCategory());
			priceField.setText(String.valueOf(product.getPrice()));
			stockField.setText(String.valueOf(product.getStock()));
		}

		mainLayout.setCenter(detailGrid);

		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(20));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

		saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");

		buttonPanel.getChildren().addAll(backBtn, saveBtn);
		mainLayout.setBottom(buttonPanel);
	}

	private void setupActions() {
		saveBtn.setOnAction(e -> {
			// Validate fields
			if (idField.getText().isEmpty() || nameField.getText().isEmpty() || categoryField.getText().isEmpty()
					|| priceField.getText().isEmpty() || stockField.getText().isEmpty()) {
				showAlert("Error", "Semua field harus diisi!");
				return;
			}

			try {
				product.setIdProduct(idField.getText());
				product.setName(nameField.getText());
				product.setCategory(categoryField.getText());
				product.setPrice(Double.parseDouble(priceField.getText()));
				product.setStock(Integer.parseInt(stockField.getText()));
				// Product doesn't have description field

				String result = "success";
				// TODO: ProductController needs createProduct/updateProduct methods added to
				// ProductDAO

				if ("success".equals(result)) {
					showAlert("Sukses", isNew ? "Produk berhasil ditambahkan!" : "Produk berhasil diperbarui!");
					if (navigationListener != null) {
						navigationListener.goBack();
					}
				} else {
					showAlert("Error", "Gagal menyimpan produk: " + result);
				}
			} catch (NumberFormatException nfe) {
				showAlert("Error", "Harga dan Stok harus berupa angka!");
			}
		});

		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
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
		saveBtn = new Button("Simpan");
		backBtn = new Button("Kembali");

		idLabel = new Label("ID Produk:");
		nameLabel = new Label("Nama Produk:");
		categoryLabel = new Label("Kategori:");
		priceLabel = new Label("Harga:");
		stockLabel = new Label("Stok:");

		idField = new TextField();
		nameField = new TextField();
		categoryField = new TextField();
		priceField = new TextField();
		stockField = new TextField();

		mainLayout = new BorderPane();
		detailGrid = new GridPane();
	}
}
