package view;

import controller.CustomerController;
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

/**
 * TopUpView - JavaFX view untuk top up balance customer
 */
public class TopUpView {
	
	private Scene scene;
	private BorderPane mainLayout;
	private GridPane formLayout;
	
	private CustomerController cc = new CustomerController();
	
	private String customerId;
	private NavigationListener navigationListener;
	
	private TextField amountField;
	private Button topUpBtn;
	private Button backBtn;
	
	public TopUpView(String customerId) {
		this.customerId = customerId;
		init();
		setupLayout();
		
		scene = new Scene(mainLayout, 700, 500);
	}
	
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 20;");
		header.setAlignment(Pos.CENTER_LEFT);
		
		Label title = new Label("Top Up Saldo");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		title.setTextFill(Color.web("#333333"));
		
		Label subtitle = new Label("Tambah saldo dompet digital Anda");
		subtitle.setFont(Font.font("Arial", 12));
		subtitle.setTextFill(Color.web("#666666"));
		
		header.getChildren().addAll(title, subtitle);
		mainLayout.setTop(header);
		
		// Form
		formLayout.setPadding(new Insets(50, 100, 50, 100));
		formLayout.setHgap(15);
		formLayout.setVgap(20);
		formLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Current Balance
		Label balanceLabel = new Label("Saldo Saat Ini:");
		balanceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
		double currentBalance = cc.getBalance(customerId);
		Label balanceValue = new Label("Rp " + String.format("%,.0f", currentBalance));
		balanceValue.setStyle("-fx-font-size: 14; -fx-text-fill: #4CAF50;");
		formLayout.add(balanceLabel, 0, 0);
		formLayout.add(balanceValue, 1, 0);
		
		// Amount Row
		Label amountLabel = new Label("Jumlah Top Up:");
		amountLabel.setStyle("-fx-font-weight: bold;");
		amountField.setPrefWidth(300);
		amountField.setStyle("-fx-font-size: 12; -fx-padding: 10;");
		amountField.setPromptText("Masukkan jumlah (minimal Rp 100.000)");
		formLayout.add(amountLabel, 0, 1);
		formLayout.add(amountField, 1, 1);
		
		// Info
		Label infoLabel = new Label("Informasi:");
		infoLabel.setStyle("-fx-font-weight: bold;");
		Label infoText = new Label("• Minimal top up: Rp 100.000\n• Maksimal top up: Rp 50.000.000\n• Top up langsung masuk ke saldo Anda");
		infoText.setStyle("-fx-font-size: 11; -fx-text-fill: #666666;");
		formLayout.add(infoLabel, 0, 2);
		formLayout.add(infoText, 1, 2);
		
		mainLayout.setCenter(formLayout);
		
		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(15));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
		
		topUpBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
		topUpBtn.setOnAction(e -> handleTopUp());
		
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
		
		buttonPanel.getChildren().addAll(backBtn, topUpBtn);
		mainLayout.setBottom(buttonPanel);
	}
	
	private void handleTopUp() {
		String amountStr = amountField.getText().trim();
		
		if (amountStr.isEmpty()) {
			showAlert("Error", "Masukkan jumlah top up!");
			return;
		}
		
		try {
			double amount = Double.parseDouble(amountStr);
			
			if (amount < 100000) {
				showAlert("Error", "Minimal top up adalah Rp 100.000");
				return;
			}
			
			if (amount > 50000000) {
				showAlert("Error", "Maksimal top up adalah Rp 50.000.000");
				return;
			}
			
			// Top up
			String result = cc.topUpBalance(customerId, amount);
			
			if ("success".equals(result)) {
				double newBalance = cc.getBalance(customerId);
				showAlert("Sukses", "Top up sebesar Rp " + String.format("%,.0f", amount) + " berhasil!\n\nSaldo baru: Rp " + String.format("%,.0f", newBalance));
				amountField.clear();
			} else {
				showAlert("Error", "Gagal melakukan top up: " + result);
			}
			
		} catch (NumberFormatException e) {
			showAlert("Error", "Jumlah harus berupa angka!");
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
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	private void init() {
		mainLayout = new BorderPane();
		formLayout = new GridPane();
		amountField = new TextField();
		topUpBtn = new Button("Top Up Sekarang");
		backBtn = new Button("Kembali");
	}
}
