package view;

import controller.DeliveryHandler;
import controller.OrderHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import model.Delivery;

import java.util.List;

/**
 * CourierListView - JavaFX view untuk daftar pengiriman courier
 */
	public class CourierListView {
	    private Scene scene;
	    private BorderPane mainLayout;
	    private TableView<Delivery> deliveryTable;

	    private DeliveryHandler dc = new DeliveryHandler();
	    private OrderHandler oc = new OrderHandler();

	    private String courierId; // null = mode admin, != null = mode courier
	    private NavigationListener navigationListener;

	    public CourierListView(String courierId) {
	        this.courierId = courierId;
	        init();
	        setupLayout();
	        loadDeliveries();

	        scene = new Scene(mainLayout, 1000, 700);
	    }

	    private void setupLayout() {
	        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

	        VBox header = new VBox();
	        header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	        header.setAlignment(Pos.CENTER_LEFT);

	        Label title = new Label("Daftar Pengiriman");
	        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	        title.setTextFill(Color.web("#333333"));

	        header.getChildren().add(title);
	        mainLayout.setTop(header);

	        setupTable();

	        Label emptyLabel = new Label("No Order Available");
	        emptyLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #666;");
	        deliveryTable.setPlaceholder(emptyLabel);

	        mainLayout.setCenter(deliveryTable);


	        HBox buttonPanel = new HBox(10);
	        buttonPanel.setPadding(new Insets(15));
	        buttonPanel.setStyle("-fx-background-color: #f0f0f0;");
	        buttonPanel.setAlignment(Pos.CENTER_RIGHT);

	        Button deliveryDetailBtn = new Button("Lihat Detail Pengiriman");
	        deliveryDetailBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #2196F3; -fx-text-fill: white;");
	        deliveryDetailBtn.setOnAction(e -> {
	            Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
	            if (selected != null) {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("COURIER_DETAIL", selected.getIdDelivery(), courierId);
	                }
	            } else {
	                showAlert("Warning", "Pilih pengiriman terlebih dahulu!");
	            }
	        });
	        
	        Button orderDetailBtn = new Button("Lihat Detail Order");
	        orderDetailBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #3370d4; -fx-text-fill: white;");
	        orderDetailBtn.setOnAction(e -> {
		        Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
		        if (selected == null) {
		            showAlert("Warning", "Pilih order dulu.");
		            return;
		        }
		        if (navigationListener != null) {
		            navigationListener.navigateTo("COURIER_ORDER_DETAIL", selected.getIdOrder());
		        }
		    });
	       	        

	        // Tombol kembali hanya untuk MODE ADMIN (courierId == null)
	        if (courierId == null) {
	            Button backBtn = new Button("Kembali");
	            backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #6c757d; -fx-text-fill: white;");
	            backBtn.setOnAction(e -> {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("ADMIN_LIST");
	                }
	            });
	            buttonPanel.getChildren().add(backBtn);
	        }

	        buttonPanel.getChildren().addAll(deliveryDetailBtn, orderDetailBtn);
	        
	        if (courierId != null) {
	        	Button updateBtn = new Button("Update Status");
	        	updateBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #FF9800; -fx-text-fill: white;");
	        	updateBtn.setOnAction(e -> {
	        		Delivery selected = deliveryTable.getSelectionModel().getSelectedItem();
	        		
	        		if (selected == null) {
	        			showAlert("Warning", "Pilih pengiriman terlebih dahulu!");
	        			return;
	        		}
	        		
	        		showUpdateStatusAlert(selected);
	        	});
	        	
	        	buttonPanel.getChildren().add(updateBtn);
			}
	        
	        // Biar courier bisa logout juga dari app
	        if(courierId != null) {
	        	Button logoutBtn = new Button("Logout");
	            logoutBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #6c757d; -fx-text-fill: white;");
	            logoutBtn.setOnAction(e -> {
	                if (navigationListener != null) {
	                    navigationListener.navigateTo("LOGIN");
	                }
	            });
	            buttonPanel.getChildren().add(logoutBtn);
	        }
	        
	        mainLayout.setBottom(buttonPanel);
	    }


	/**
	 * Setup Table
	 */
	private void setupTable() {
		TableColumn<Delivery, String> idCol = new TableColumn<>("ID Pengiriman");
		idCol.setCellValueFactory(new PropertyValueFactory<>("idDelivery"));
		idCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> orderCol = new TableColumn<>("ID Order");
		orderCol.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
		orderCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> courierCol = new TableColumn<>("ID Courier");
		courierCol.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
		courierCol.setPrefWidth(120);
		
		TableColumn<Delivery, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(100);
		
		@SuppressWarnings("unchecked")
		TableColumn<Delivery, ?>[] columns = new TableColumn[] {idCol, orderCol, courierCol, statusCol};
		deliveryTable.getColumns().addAll(columns);
	}

	/**
	 * Load Deliveries
	 */
	private void loadDeliveries() {
	    List<Delivery> deliveries;

	    if (courierId == null || courierId.isEmpty()) {
	        deliveries = dc.getAllDeliveries();
	    } else {
	        deliveries = dc.getDeliveriesByCourierId(courierId);
	    }

	    ObservableList<Delivery> items = FXCollections.observableArrayList();

	    if (deliveries != null) {
	        items.addAll(deliveries);
	    }

	    deliveryTable.setItems(items); 
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
	
	private void showUpdateStatusAlert(Delivery selected) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Update Status");
	    alert.setHeaderText("Update Delivery Status");
	    
	    ComboBox<String> statusComboBox = new ComboBox<>();
	    statusComboBox.getItems().addAll("pending", "in progress", "delivered");
	    statusComboBox.setValue(selected.getStatus());
	    statusComboBox.setPrefWidth(200);
	    
	    VBox dialogContent = new VBox(10);
	    dialogContent.setPadding(new Insets(10));
	    dialogContent.getChildren().addAll(
	        new Label("Delivery ID: " + selected.getIdDelivery()),
	        new Label("Order ID: " + selected.getIdOrder()),
	        new Label("Current Status: " + selected.getStatus()),
	        new Label("Select New Status:"),
	        statusComboBox
	    );
	    
	    alert.getDialogPane().setContent(dialogContent);
	    
	    ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
	    if (result == ButtonType.OK) {
	        String newStatus = statusComboBox.getValue();
	        
	        if (newStatus != null && !newStatus.isEmpty()) {
	            String deliveryUpdateResult = dc.updateDeliveryStatus(selected.getIdDelivery(), newStatus);
	            
	            if ("success".equals(deliveryUpdateResult)) {
	                String orderUpdateResult = oc.editOrderHeaderStatus(selected.getIdOrder(), newStatus);
	                
	                if ("success".equals(orderUpdateResult)) {
	                    loadDeliveries();
	                    showAlert("Sukses", "Status pengiriman dan order diperbarui menjadi " + newStatus);
	                } else {
	                    showAlert("Warning", "Status pengiriman berhasil diperbarui, tetapi gagal update status order: " + orderUpdateResult);
	                    loadDeliveries();
	                }
	            } else {
	                showAlert("Error", "Gagal update status pengiriman: " + deliveryUpdateResult);
	            }
	        } else {
	            showAlert("Warning", "Please select a status from the dropdown.");
	        }
	    }
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
		deliveryTable = new TableView<>();
	}
}