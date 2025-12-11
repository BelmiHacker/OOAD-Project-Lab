package view;

import controller.OrderHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import model.OrderDetail;
import model.OrderHeader;

import java.util.List;

public class AdminOrderDetailView {

    private Scene scene;
    private BorderPane mainLayout;
    private TableView<OrderDetail> detailTable;

    private final OrderHandler orderHandler = new OrderHandler();
    private NavigationListener navigationListener;

    private final String orderId;
    private OrderHeader orderHeader;

    public AdminOrderDetailView(String orderId) {
        this.orderId = orderId;

        init();
        loadOrderHeader();
        setupLayout();
        loadOrderDetails();

        scene = new Scene(mainLayout, 900, 600);
    }

    private void init() {
        mainLayout = new BorderPane();
        detailTable = new TableView<>();
    }

    // cari header berdasarkan idOrder
    private void loadOrderHeader() {
        List<OrderHeader> all = orderHandler.getAllOrders();
        if (all != null) {
            for (OrderHeader oh : all) {
                if (oh.getIdOrder().equals(orderId)) {
                    orderHeader = oh;
                    break;
                }
            }
        }

        if (orderHeader == null) {
            showAlert("Error", "Order dengan ID " + orderId + " tidak ditemukan.");
        }
    }

    private void setupLayout() {
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // ==== HEADER ATAS ====
        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #c8dcfa;");

        Label titleLbl = new Label("Detail Order");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLbl.setTextFill(Color.web("#333333"));

        String info1Text = "ID Order: " + (orderHeader != null ? orderHeader.getIdOrder() : orderId)
                + "   |   ID Customer: " + (orderHeader != null ? orderHeader.getIdCustomer() : "-");

        String info2Text = "Status: " + (orderHeader != null ? orderHeader.getStatus() : "-");

        Label info1 = new Label(info1Text);
        Label info2 = new Label(info2Text);

        headerBox.getChildren().addAll(titleLbl, info1, info2);
        mainLayout.setTop(headerBox);

        // ==== TABEL DETAIL ====
        setupTable();
        mainLayout.setCenter(detailTable);

        // ==== TOMBOL BAWAH ====
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(15));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setStyle("-fx-background-color: #f0f0f0;");

        Button backBtn = new Button("Kembali");
        backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; "
                + "-fx-background-color: #999999; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.goBack();
            }
        });

        bottomBox.getChildren().add(backBtn);
        mainLayout.setBottom(bottomBox);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        TableColumn<OrderDetail, String> idOrderCol = new TableColumn<>("ID Order");
        idOrderCol.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        idOrderCol.setPrefWidth(150);

        TableColumn<OrderDetail, String> idProductCol = new TableColumn<>("ID Produk");
        idProductCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        idProductCol.setPrefWidth(150);

        TableColumn<OrderDetail, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qtyCol.setPrefWidth(80);

        detailTable.getColumns().addAll(idOrderCol, idProductCol, qtyCol);
    }

    private void loadOrderDetails() {
        List<OrderDetail> details = orderHandler.getOrderDetails(orderId);
        if (details == null || details.isEmpty()) {
            showAlert("Info", "Order ini belum memiliki detail item.");
            detailTable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<OrderDetail> items = FXCollections.observableArrayList(details);
        detailTable.setItems(items);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }
}
