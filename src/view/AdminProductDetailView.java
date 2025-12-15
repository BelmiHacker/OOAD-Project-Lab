package view;

import controller.ProductHandler;
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
 * AdminProductDetailView - JavaFX view untuk edit stok dan hapus produk (no add)
 */
public class AdminProductDetailView {
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane detailGrid;

	private ProductHandler pc = new ProductHandler();
	private NavigationListener navigationListener;

	private String productId;
	private Product product;

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
	private Button deleteBtn;

	// Keep constructor signature for compatibility; adminId is unused
	public AdminProductDetailView(String productId, String adminId) {
		this.productId = productId;
		init();
		loadProductDetail();
		setupLayout();
		setupActions();
		scene = new Scene(mainLayout, 900, 700);
	}

    /**
     * Mengambil detail produk berdasarkan productId.
     * Jika productId tidak valid atau produk tidak ditemukan,
     * akan menampilkan alert error.
     */
	private void loadProductDetail() {
		if (productId == null || productId.isEmpty()) {
			showAlert("Error", "Product ID tidak valid.");
			product = new Product();
			return;
		}
		product = pc.getProduct(productId);
		if (product == null) {
			showAlert("Error", "Produk tidak ditemukan!");
			product = new Product();
		}
	}

    /**
     * Menyusun layout tampilan:
     * - Header judul
     * - Grid detail produk
     * - Panel tombol aksi (Simpan, Hapus, Kembali)
     */
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");

		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);

		Label title = new Label("Edit Produk (Hanya Stok)");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));

		header.getChildren().add(title);
		mainLayout.setTop(header);

		detailGrid.setPadding(new Insets(40, 60, 40, 60));
		detailGrid.setHgap(20);
		detailGrid.setVgap(15);
		detailGrid.setStyle("-fx-background-color: #f5f5f5;");

		idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		idField.setPrefWidth(300);
		idField.setDisable(true);
		detailGrid.add(idLabel, 0, 0);
		detailGrid.add(idField, 1, 0);

		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		nameField.setPrefWidth(300);
		nameField.setDisable(true);
		detailGrid.add(nameLabel, 0, 1);
		detailGrid.add(nameField, 1, 1);

		categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		categoryField.setPrefWidth(300);
		categoryField.setDisable(true);
		detailGrid.add(categoryLabel, 0, 2);
		detailGrid.add(categoryField, 1, 2);

		priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		priceField.setPrefWidth(300);
		priceField.setDisable(true);
		detailGrid.add(priceLabel, 0, 3);
		detailGrid.add(priceField, 1, 3);

		stockLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		stockField.setPrefWidth(300);
		detailGrid.add(stockLabel, 0, 4);
		detailGrid.add(stockField, 1, 4);

		if (product != null) {
			idField.setText(product.getIdProduct());
			nameField.setText(product.getName());
			categoryField.setText(product.getCategory());
			priceField.setText(String.valueOf(product.getPrice()));
			stockField.setText(String.valueOf(product.getStock()));
		}

		mainLayout.setCenter(detailGrid);

		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(20));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

		saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		deleteBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FF5252; -fx-text-fill: white;");

		buttonPanel.getChildren().addAll(backBtn, deleteBtn, saveBtn);
		mainLayout.setBottom(buttonPanel);
	}

    /**
     * Mengatur event handler untuk tombol:
     * - Simpan: update stok
     * - Kembali: kembali ke halaman sebelumnya
     * - Hapus: hapus produk
     */
	private void setupActions() {
		saveBtn.setOnAction(e -> {
			if (stockField.getText().isEmpty()) {
				showAlert("Error", "Field stok harus diisi!");
				return;
			}
			try {
				int newStock = Integer.parseInt(stockField.getText().trim());
				if (newStock < 0) {
					showAlert("Error", "Stok tidak boleh negatif!");
					return;
				}
				String result = pc.editProductStock(productId, newStock);
				if ("success".equals(result)) {
					showAlert("Sukses", "Stok produk berhasil diperbarui!");
					if (navigationListener != null) navigationListener.goBack();
				} else {
					showAlert("Error", "Gagal mengupdate stok: " + result);
				}
			} catch (NumberFormatException nfe) {
				showAlert("Error", "Stok harus berupa angka!");
			}
		});

		backBtn.setOnAction(e -> {
			if (navigationListener != null) navigationListener.goBack();
		});

		deleteBtn.setOnAction(e -> {
			if (productId == null || productId.isEmpty()) {
				showAlert("Error", "Produk tidak valid untuk dihapus.");
				return;
			}
			String result = pc.deleteProduct(productId);
			if ("success".equals(result)) {
				showAlert("Sukses", "Produk berhasil dihapus!");
				if (navigationListener != null) navigationListener.goBack();
			} else {
				showAlert("Error", "Gagal menghapus produk: " + result);
			}
		});
	}

    /**
     * Menampilkan alert informasi.
     */
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

	  /**
     * Inisialisasi seluruh komponen UI.
     */
	private void init() {
		saveBtn = new Button("Simpan");
		backBtn = new Button("Kembali");
		deleteBtn = new Button("Hapus");

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