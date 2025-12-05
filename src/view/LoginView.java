package view;

import controller.UserController;
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

/**
 * LoginView - JavaFX view untuk halaman login
 */
public class LoginView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane gridLayout;
	
	private Label title;
	private Label emailLabel;
	private Label passLabel;
	
	private TextField emailTF;
	private PasswordField passTF;
	private Hyperlink link;
	private Button button;
	
	private UserController uc = new UserController();
	private User loggedInUser;
	private NavigationListener navigationListener;
	
	public LoginView() {
		init();
		setupLayout();
		setupActions();
		
		scene = new Scene(mainLayout, 800, 600);
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
	
	private void setupActions() {
		button.setOnAction(e -> {
			String result = uc.login(emailTF.getText(), passTF.getText());
			if ("success".equals(result)) {
				loggedInUser = uc.getUserByEmail(emailTF.getText());
				showAlert("Sukses", "Login berhasil! Selamat datang " + loggedInUser.getFullName());
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
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	private void init() {
		button = new Button("Login");
		link = new Hyperlink("Belum punya akun? Daftar di sini");
		
		passTF = new PasswordField();
		emailTF = new TextField();
		
		emailLabel = new Label("Email:");
		passLabel = new Label("Password:");
		title = new Label("Login");
		
		mainLayout = new BorderPane();
		gridLayout = new GridPane();
	}
}
