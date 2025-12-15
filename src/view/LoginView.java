package view;

import controller.UserHandler;
import controller.CourierHandler;
import controller.CustomerHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.User;
import model.Courier;
import model.Customer;

/**
 * LoginView - JavaFX view untuk halaman login
 */
public class LoginView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane gridLayout;

	// UI Elements
	private Label emailLabel;
	private Label passLabel;
	
	private TextField emailTF;
	private PasswordField passTF;
	private Hyperlink link;
	private Button button;

	// Handlers
	private UserHandler uc = new UserHandler();
	private CustomerHandler cc = new CustomerHandler();
	private CourierHandler courierController = new CourierHandler();
	private User loggedInUser;
	private NavigationListener navigationListener;

	// Constructor
	public LoginView() {
		init();
		setupLayout();
		setupActions();
		
		scene = new Scene(mainLayout, 800, 600);
	}

	// Setup layout dan styling JavaFX
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 20;");
		header.setAlignment(Pos.CENTER);
		
		Label mainTitle = new Label("JoymarKet");
		mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		mainTitle.setTextFill(Color.web("#333333"));
		
		Label subtitle = new Label("Digital Marketplace");
		subtitle.setFont(Font.font("Arial", 14));
		subtitle.setTextFill(Color.web("#666666"));
		
		header.getChildren().addAll(mainTitle, subtitle);
		mainLayout.setTop(header);
		
		// Setup Grid Layout
		gridLayout.setPadding(new Insets(50, 100, 50, 100));
		gridLayout.setHgap(15);
		gridLayout.setVgap(15);
		gridLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Email Row
		emailTF.setPrefWidth(300);
		emailTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(emailLabel, 0, 0);
		gridLayout.add(emailTF, 1, 0);
		
		// Password Row
		passTF.setPrefWidth(300);
		passTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(passLabel, 0, 1);
		gridLayout.add(passTF, 1, 1);
		
		// Button dan Link Row
		button.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		link.setStyle("-fx-font-size: 11;");
		gridLayout.add(link, 0, 2);
		gridLayout.add(button, 1, 2);
		
		mainLayout.setCenter(gridLayout);
	}

	// Setup actions untuk button dan link
	private void setupActions() {
		button.setOnAction(e -> {
			String result = uc.login(emailTF.getText(), passTF.getText());
			if ("success".equals(result)) {
				loggedInUser = uc.getUserByEmail(emailTF.getText());
				showAlert("Sukses", "Login berhasil! Selamat datang " + loggedInUser.getFullName());
				
				// Navigate based on role
				if (navigationListener != null) {
					if ("admin".equals(loggedInUser.getRole())) {
						navigationListener.navigateTo("ADMIN_LIST");
					} else if ("customer".equals(loggedInUser.getRole())) {
						// Get customer ID from user ID
						Customer customer = cc.getCustomerByUserId(loggedInUser.getIdUser());
						if (customer != null) {
							navigationListener.navigateTo("CUSTOMER_LIST", customer.getIdCustomer());
						} else {
							showAlert("Error", "Customer data tidak ditemukan!");
						}
					} else if ("courier".equals(loggedInUser.getRole())) {
						Courier courier = courierController.getCourierByUserId(loggedInUser.getIdUser());
						navigationListener.navigateTo("COURIER_LIST", courier.getIdCourier());
					}
				}
			} else {
				showAlert("Error", result);
			}
		});
		
		link.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.navigateTo("REGISTER");
			}
		});
	}

	// Tampilkan alert dialog
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
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		button = new Button("Login");
		link = new Hyperlink("Belum punya akun? Daftar di sini");
		
		passTF = new PasswordField();
		emailTF = new TextField();
		
		emailLabel = new Label("Email:");
		passLabel = new Label("Password:");
		
		mainLayout = new BorderPane();
		gridLayout = new GridPane();
	}
}
