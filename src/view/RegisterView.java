package view;

import controller.UserController;
import controller.CustomerController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import model.User;
import model.Customer;

/**
 * RegisterView - JavaFX view untuk halaman registrasi pengguna baru
 */
public class RegisterView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane gridLayout;
	
	private Label fullNameLabel;
	private Label emailLabel;
	private Label passLabel;
	private Label phoneLabel;
	private Label addressLabel;
	
	private TextField fullNameTF;
	private TextField emailTF;
	private PasswordField passTF;
	private TextField phoneTF;
	private TextField addressTF;
	
	private Button registerBtn;
	private Button backBtn;
	
	private UserController uc = new UserController();
	private CustomerController cc = new CustomerController();
	private NavigationListener navigationListener;
	
	public RegisterView() {
		init();
		setupLayout();
		setupActions();
		
		scene = new Scene(mainLayout, 800, 700);
	}
	
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 20;");
		header.setAlignment(Pos.CENTER);
		
		Label mainTitle = new Label("JoymarKet");
		mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		mainTitle.setTextFill(Color.web("#333333"));
		
		Label subtitle = new Label("Daftar Akun Baru");
		subtitle.setFont(Font.font("Arial", 14));
		subtitle.setTextFill(Color.web("#666666"));
		
		header.getChildren().addAll(mainTitle, subtitle);
		mainLayout.setTop(header);
		
		// Setup Grid Layout
		gridLayout.setPadding(new Insets(40, 100, 40, 100));
		gridLayout.setHgap(15);
		gridLayout.setVgap(15);
		gridLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Full Name Row
		fullNameTF.setPrefWidth(300);
		fullNameTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(fullNameLabel, 0, 0);
		gridLayout.add(fullNameTF, 1, 0);
		
		// Email Row
		emailTF.setPrefWidth(300);
		emailTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(emailLabel, 0, 1);
		gridLayout.add(emailTF, 1, 1);
		
		// Password Row
		passTF.setPrefWidth(300);
		passTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(passLabel, 0, 2);
		gridLayout.add(passTF, 1, 2);
		
		// Phone Row
		phoneTF.setPrefWidth(300);
		phoneTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(phoneLabel, 0, 3);
		gridLayout.add(phoneTF, 1, 3);
		
		// Address Row
		addressTF.setPrefWidth(300);
		addressTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		gridLayout.add(addressLabel, 0, 4);
		gridLayout.add(addressTF, 1, 4);
		
		// Button Row
		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		registerBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		buttonBox.getChildren().addAll(backBtn, registerBtn);
		gridLayout.add(buttonBox, 0, 5, 2, 1);
		
		mainLayout.setCenter(gridLayout);
	}
	
	private void setupActions() {
		registerBtn.setOnAction(e -> {
			String fullName = fullNameTF.getText();
			String email = emailTF.getText();
			String password = passTF.getText();
			String phone = phoneTF.getText();
			String address = addressTF.getText();
			
			// Validate fields
			if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
				showAlert("Error", "Semua field harus diisi!");
				return;
			}
			
			// Create User - register takes 7 parameters: idUser, fullName, email, password, confirmPassword, phone, address
			String userResult = uc.register(email, fullName, email, password, password, phone, address);
			if ("success".equals(userResult)) {
				showAlert("Sukses", "Registrasi berhasil! Silakan login.");
			} else {
				showAlert("Error", userResult);
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
		registerBtn = new Button("Daftar");
		backBtn = new Button("Kembali");
		
		fullNameTF = new TextField();
		emailTF = new TextField();
		passTF = new PasswordField();
		phoneTF = new TextField();
		addressTF = new TextField();
		
		fullNameLabel = new Label("Nama Lengkap:");
		emailLabel = new Label("Email:");
		passLabel = new Label("Password:");
		phoneLabel = new Label("Nomor HP:");
		addressLabel = new Label("Alamat:");
		
		mainLayout = new BorderPane();
		gridLayout = new GridPane();
	}
}
