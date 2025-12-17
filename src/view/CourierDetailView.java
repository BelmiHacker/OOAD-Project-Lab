package view;

import controller.DeliveryHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.Delivery;

/**
 * CourierDetailView - JavaFX view untuk detail pengiriman courier
 */
public class CourierDetailView {
	// UI Components
	private Scene scene;
	private BorderPane mainLayout;

	// Handler
	private DeliveryHandler dc = new DeliveryHandler();

	// State
	private String deliveryId;
	private NavigationListener navigationListener;
	
	private Delivery delivery;

	// Constructor
	public CourierDetailView(String deliveryId, String courierId) {
		this.deliveryId = deliveryId;
		this.delivery = dc.getDeliveryById(deliveryId);
		
		init();
		setupLayout();
		loadData();
		
		scene = new Scene(mainLayout, 900, 700);
	}

	/**
	 * Setup Layout
	 */
	private void setupLayout() {
		mainLayout.setStyle("-fx-background-color: #f5f5f5;");
		
		// Header
		VBox header = new VBox();
		header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
		header.setAlignment(Pos.CENTER_LEFT);
		
		Label title = new Label("Detail Pengiriman");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setTextFill(Color.web("#333333"));
		
		header.getChildren().add(title);
		mainLayout.setTop(header);
		
		// Content
		VBox content = new VBox(15);
		content.setPadding(new Insets(30, 50, 30, 50));
		content.setStyle("-fx-background-color: #f5f5f5;");
		
		// ID Pengiriman
		HBox idBox = new HBox(15);
		Label idLabel = new Label("ID Pengiriman:");
		idLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 150;");
		TextField idTF = new TextField();
		idTF.setEditable(false);
		idTF.setPrefWidth(300);
		idBox.getChildren().addAll(idLabel, idTF);
		
		// ID Order
		HBox orderBox = new HBox(15);
		Label orderLabel = new Label("ID Order:");
		orderLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 150;");
		TextField orderTF = new TextField();
		orderTF.setEditable(false);
		orderTF.setPrefWidth(300);
		orderBox.getChildren().addAll(orderLabel, orderTF);
		
		// Courier
		HBox courierBox = new HBox(15);
		Label courierLabel = new Label("ID Courier:");
		courierLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 150;");
		TextField courierTF = new TextField();
		courierTF.setEditable(false);
		courierTF.setPrefWidth(300);
		courierBox.getChildren().addAll(courierLabel, courierTF);
		
		// Status
		HBox statusBox = new HBox(15);
		Label statusLabel = new Label("Status:");
		statusLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 150;");
		TextField statusTF = new TextField();
		statusTF.setEditable(false);
		statusTF.setPrefWidth(300);
		statusBox.getChildren().addAll(statusLabel, statusTF);
		
		content.getChildren().addAll(idBox, orderBox, courierBox, statusBox);
		mainLayout.setCenter(content);
		
		// Button Panel
		HBox buttonPanel = new HBox(10);
		buttonPanel.setPadding(new Insets(15));
		buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		
		Button updateBtn = new Button("Update Status");
		updateBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FF9800; -fx-text-fill: white;");
		updateBtn.setOnAction(e -> {
		    if (delivery != null) {
		        String currentStatus = delivery.getStatus();
		        String newStatus;

		        if ("pending".equals(currentStatus)) {
		            newStatus = "in progress";
		        } else if ("in progress".equals(currentStatus)) {
		            newStatus = "delivered";
		        } else {
		            showAlert("Info", "Pengiriman sudah delivered, tidak bisa di-update lagi.");
		            return;
		        }

		        String result = dc.updateDeliveryStatus(deliveryId, newStatus);
		        if ("success".equals(result)) {
		            showAlert("Sukses", "Status pengiriman diperbarui menjadi " + newStatus);
		            delivery.setStatus(newStatus);
		            statusTF.setText(newStatus);
		        } else {
		            showAlert("Error", "Gagal update status: " + result);
		        }
		    }
		});

		
		Button backBtn = new Button("Kembali");
		backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
		backBtn.setOnAction(e -> {
			if (navigationListener != null) {
				navigationListener.goBack();
			}
		});
		
		buttonPanel.getChildren().addAll(updateBtn, backBtn);
		mainLayout.setBottom(buttonPanel);
		
		// Store references to fields for loading data
		idTF.setText(delivery != null ? delivery.getIdDelivery() : "");
		orderTF.setText(delivery != null ? delivery.getIdOrder() : "");
		courierTF.setText(delivery != null ? delivery.getIdCourier() : "");
		statusTF.setText(delivery != null ? delivery.getStatus() : "");
	}

	/**
	 * Load Data
	 */
	private void loadData() {
		// Data already loaded in setupLayout
	}

	/**
	 * Show Alert
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Getter and Setter
	public Scene getScene() {
		return scene;
	}
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}

	// Inisialisasi komponen UI
	private void init() {
		mainLayout = new BorderPane();
	}
}
