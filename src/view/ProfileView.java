package view;

import controller.UserHandler;
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
 * ProfileView - JavaFX view untuk halaman edit profil pengguna
 */
public class ProfileView {
    // UI Components
    private Scene scene;
    private BorderPane mainLayout;
    private GridPane gridLayout;

    // UI Elements
    private Label fullNameLabel;
    private Label emailLabel;
    private Label emailNoteLabel;
    private Label passLabel;
    private Label passNoteLabel;
    private Label phoneLabel;
    private Label addressLabel;

    private TextField fullNameTF;
    private TextField emailTF;
    private TextField passTF;        // disabled input (kept for consistency)
    private TextField phoneTF;
    private TextField addressTF;

    private Button saveBtn;
    private Button backBtn;

    // Handlers
    private UserHandler uc = new UserHandler();
    private NavigationListener navigationListener;

    // Current user id (harus di-set oleh controller)
    private String userId;

    // Constructor that accepts userId and loads data immediately
    public ProfileView(String userId) {
        this.userId = userId;
        init();
        setupLayout();
        loadUserData();
        setupActions();
        scene = new Scene(mainLayout, 800, 700);
    }

    private void loadUserData() {
        if (userId == null || userId.isEmpty()) return;
        try {
            model.User user = uc.getUser(userId);
            if (user == null) return;

            String fullName = user.getFullName();
            String email = user.getEmail();
            String phone = user.getPhone();
            String address = user.getAddress();
            String password = user.getPassword();

            // populate fields (populateFields expects fullName, email, phone, address, password)
            populateFields(fullName, email, phone, address, password);
        } catch (Exception ex) {
            showAlert("Error", "Gagal memuat data user: " + ex.getMessage());
        }
    }

    // Setup layout dan styling JavaFX
    private void setupLayout() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = new VBox();
        header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 20;");
        header.setAlignment(Pos.CENTER);

        Label mainTitle = new Label("JoymarKet");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        mainTitle.setTextFill(Color.web("#333333"));

        Label subtitle = new Label("Edit Profil");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#666666"));

        header.getChildren().addAll(mainTitle, subtitle);
        mainLayout.setTop(header);

        // Setup Grid Layout
        gridLayout = new GridPane();
        gridLayout.setPadding(new Insets(40, 100, 40, 100));
        gridLayout.setHgap(15);
        gridLayout.setVgap(15);
        gridLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Full Name Row (row 0)
        fullNameTF.setPrefWidth(300);
        fullNameTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        gridLayout.add(fullNameLabel, 0, 0);
        gridLayout.add(fullNameTF, 1, 0);

        // Email Row (disabled) (row 1)
        emailTF.setPrefWidth(300);
        emailTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        emailTF.setDisable(true);
        gridLayout.add(emailLabel, 0, 1);
        gridLayout.add(emailTF, 1, 1);

        // Email note row (row 2)
        emailNoteLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #666666;");
        gridLayout.add(emailNoteLabel, 1, 2);

        // Password Row (row 3) - input disabled
        passTF.setPrefWidth(300);
        passTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        passTF.setDisable(true);
        gridLayout.add(passLabel, 0, 3);
        gridLayout.add(passTF, 1, 3);

        // Email note row (row 4)
        passNoteLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #666666;");
        gridLayout.add(passNoteLabel, 1, 4);

        // Phone Row (row 5)
        phoneTF.setPrefWidth(300);
        phoneTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        gridLayout.add(phoneLabel, 0, 5);
        gridLayout.add(phoneTF, 1, 5);

        // Address Row (row 6)
        addressTF.setPrefWidth(300);
        addressTF.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        gridLayout.add(addressLabel, 0, 6);
        gridLayout.add(addressTF, 1, 6);

        // Button Row (row 7)
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        saveBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        backBtn.setStyle("-fx-font-size: 12; -fx-padding: 8 25; -fx-background-color: #999999; -fx-text-fill: white;");
        buttonBox.getChildren().addAll(backBtn, saveBtn);
        gridLayout.add(buttonBox, 0, 7, 2, 1);

        mainLayout.setCenter(gridLayout);
    }

    // Setup actions untuk button
    private void setupActions() {
        saveBtn.setOnAction(e -> {
            if (userId == null || userId.isEmpty()) {
                showAlert("Error", "User ID belum diset. Tidak dapat menyimpan perubahan.");
                return;
            }

            String fullName = fullNameTF.getText().trim();
            String phone = phoneTF.getText().trim();
            String address = addressTF.getText().trim();

            String result;
            try {
                result = uc.updateProfile(userId, fullName, phone, address);
            } catch (Exception ex) {
                result = "Exception: " + ex.getMessage();
            }

            if ("success".equalsIgnoreCase(result)) {
                showAlert("Sukses", "Profil berhasil diperbarui.");
                if (navigationListener != null) {
                    navigationListener.goBack();
                }
            } else {
                showAlert("Error", result);
            }
        });

        backBtn.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.goBack();
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

    // Set user id agar form tahu user mana yang diedit
    public void setUserId(String userId) {
        this.userId = userId;
        loadUserData();
    }

    // Populate fields dari controller
    public void populateFields(String fullName, String email, String phone, String address, String password) {
        fullNameTF.setText(fullName != null ? fullName : "");
        emailTF.setText(email != null ? email : "");
        emailTF.setDisable(true);
        phoneTF.setText(phone != null ? phone : "");
        addressTF.setText(address != null ? address : "");
        passTF.setText(password != null ? password : "");
        passTF.setDisable(true);
    }

    // Inisialisasi komponen UI
    private void init() {
        saveBtn = new Button("Simpan");
        backBtn = new Button("Kembali");

        fullNameTF = new TextField();
        emailTF = new TextField();
        passTF = new TextField();
        phoneTF = new TextField();
        addressTF = new TextField();

        fullNameLabel = new Label("Nama Lengkap:");
        emailLabel = new Label("Email:");
        emailNoteLabel = new Label("Email tidak dapat diubah.");
        passLabel = new Label("Password:");
        passNoteLabel = new Label("Password tidak dapat diubah.");
        phoneLabel = new Label("Nomor HP:");
        addressLabel = new Label("Alamat:");

        mainLayout = new BorderPane();
        gridLayout = new GridPane();
    }
}