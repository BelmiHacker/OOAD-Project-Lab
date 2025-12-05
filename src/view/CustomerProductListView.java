package view;

import controller.UserController;
import controller.CartItemController;
import controller.CustomerController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import java.util.List;

/**
 * CustomerProductListView - JavaFX view untuk daftar produk customer
 */
public class CustomerProductListView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private TextField searchField;
	private TableView<Product> productTable;
	
	private UserController uc = new UserController();
	private controller.ProductController pc = new controller.ProductController();
	private CartItemController cartItemController = new CartItemController();
	private String customerId;
	private NavigationListener navigationListener;
	
	public CustomerProductListView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		loadProducts();
		
		scene = new Scene(mainLayout, 1000, 700);
	}
	
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);
		
		Label title = new Label("Daftar Produk JoymarKet");
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
		
		Button detailBtn = new Button("Lihat Detail");
		detailBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color: #2196F3; -fx-text-fill: white;");
		detailBtn.setOnAction(e -> {
			Product selected = productTable.getSelectionModel().getSelectedItem();
			if (selected != null) {
				if (navigationListener != null) {
					navigationListener.navigateTo("CUSTOMER_DETAIL", selected.getIdProduct(), customerId);
				}
			} else {
				showAlert("Warning", "Pilih produk terlebih dahulu!");
			}
		});
		
		Button cartBtn = new Button("Buka Cart");
		cartBtn.setStyle("-fx-font-size: 11; -fx-padding: 6 15; -fx-background-color: #FF9800; -fx-text-fill: white;");
		cartBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("CART", customerId);
			}
		});
		
		buttonPanel.getChildren().addAll(detailBtn, cartBtn);
		mainLayout.setBottom(buttonPanel);
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	private void setupTable() {
		TableColumn<Product, String> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
		idCol.setPrefWidth(100);
		
		TableColumn<Product, String> nameCol = new TableColumn<>("Nama");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(200);
		
		TableColumn<Product, String> categoryCol = new TableColumn<>("Kategori");
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol.setPrefWidth(150);
		
		TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setPrefWidth(100);
		
		TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
		stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
		stockCol.setPrefWidth(100);
		
		productTable.getColumns().addAll(idCol, nameCol, categoryCol, priceCol, stockCol);
	}
	
	private void loadProducts() {
		List<Product> products = pc.getAllProducts();
		if (products != null) {
			ObservableList<Product> items = FXCollections.observableArrayList(products);
			productTable.setItems(items);
		}
	}
	
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
	
	public Scene getScene() {
		return scene;
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private void init() {
		mainLayout = new BorderPane();
		searchField = new TextField();
		productTable = new TableView<>();
	}
}
