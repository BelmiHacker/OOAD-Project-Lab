package view;

import controller.CourierController;
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
import model.Courier;

import java.util.List;

public class AdminCourierMasterView {

    private Scene scene;
    private BorderPane mainLayout;
    private TableView<Courier> courierTable;

    private CourierController cc = new CourierController();
    private NavigationListener navigationListener;

    public AdminCourierMasterView() {
        init();
        setupLayout();
        loadCouriers();

        scene = new Scene(mainLayout, 800, 600);
    }

    private void init() {
        mainLayout = new BorderPane();
        courierTable = new TableView<>();
    }

    private void setupLayout() {
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // HEADER
        VBox header = new VBox(5);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #c8dcfa;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Daftar Kurir");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#333333"));

        header.getChildren().addAll(title);
        mainLayout.setTop(header);

        // TABLE
        setupTable();
        mainLayout.setCenter(courierTable);

        // BUTTON PANEL
        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(15));
        buttonPanel.setAlignment(Pos.CENTER_RIGHT);
        buttonPanel.setStyle("-fx-background-color: #f0f0f0;");

        Button backBtn = new Button("Kembali");
        backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.goBack();
            }
        });
        

        buttonPanel.getChildren().add(backBtn);
        mainLayout.setBottom(buttonPanel);
    }

    @SuppressWarnings("unchecked")
    private void setupTable() {
        TableColumn<Courier, String> idCol = new TableColumn<>("ID Courier");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
        idCol.setPrefWidth(120);

        TableColumn<Courier, String> typeCol = new TableColumn<>("Tipe Kendaraan");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        typeCol.setPrefWidth(150);

        TableColumn<Courier, String> plateCol = new TableColumn<>("Plat Kendaraan");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("vehiclePlate"));
        plateCol.setPrefWidth(150);

        courierTable.getColumns().addAll(idCol, typeCol, plateCol);
    }

    private void loadCouriers() {
        List<Courier> couriers = cc.getAllCouriers();
        if (couriers != null) {
            ObservableList<Courier> items = FXCollections.observableArrayList(couriers);
            courierTable.setItems(items);
        }
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
