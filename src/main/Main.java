package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.*;

/**
 * Main - Entry point aplikasi JoymarKet dengan Navigation Controller
 */
public class Main extends Application implements NavigationListener {
    // State aplikasi saat ini
    private Stage primaryStage;
    private String currentView = "ADMIN_LIST";
    private String currentAdminId = "ADM_00001";
    private String currentUserId = "USR_00001";  // Bisa customer atau courier berdasarkan login
    private String currentCustomerId = "CUST_00001";  // Will be set by login
    private String currentCourierId = null;  // Will be set by login
    private String currentProductId = null;

    /**
     * Memulai aplikasi dan menampilkan jendela utama
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        primaryStage.setTitle("JoymarKet - Digital Marketplace");
        primaryStage.setWidth(1100);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);
        
        // Start with product list for admin (hardcoded adminId for testing)
        navigateTo("LOGIN");
        primaryStage.show();
    }

    /**
     * Menavigasi ke tampilan yang ditentukan
     */
    @Override
    public void navigateTo(String viewName, String... params) {
        try {
            currentView = viewName;
            
            switch(viewName) {
                case "ADMIN_LIST":
                    AdminProductListView adminListView = new AdminProductListView(currentAdminId);
                    adminListView.setNavigationListener(this);
                    primaryStage.setScene(adminListView.getScene());
                    primaryStage.setWidth(1100);
                    primaryStage.setHeight(700);
                    break;
                    
                case "ADMIN_DETAIL":
                    if (params.length > 0) {
                        currentProductId = params[0];
                        AdminProductDetailView adminDetailView = new AdminProductDetailView(currentProductId, currentAdminId);
                        adminDetailView.setNavigationListener(this);
                        primaryStage.setScene(adminDetailView.getScene());
                        primaryStage.setWidth(900);
                        primaryStage.setHeight(700);
                    }
                    break;
                    
                case "CUSTOMER_LIST":
                    if (params.length > 0) {
                        currentCustomerId = params[0];
                    }
                    CustomerProductListView custListView = new CustomerProductListView(currentCustomerId);
                    custListView.setNavigationListener(this);
                    primaryStage.setScene(custListView.getScene());
                    primaryStage.setWidth(1000);
                    primaryStage.setHeight(700);
                    break;
                    
                case "CUSTOMER_DETAIL":
                    if (params.length > 1) {
                        currentProductId = params[0];
                        currentCustomerId = params[1];
                        CustomerProductDetailView custDetailView = new CustomerProductDetailView(currentProductId, currentCustomerId);
                        custDetailView.setNavigationListener(this);
                        primaryStage.setScene(custDetailView.getScene());
                        primaryStage.setWidth(900);
                        primaryStage.setHeight(700);
                    }
                    break;
                    
                case "LOGIN":
                    LoginView loginView = new LoginView();
                    loginView.setNavigationListener(this);
                    primaryStage.setScene(loginView.getScene());
                    primaryStage.setWidth(800);
                    primaryStage.setHeight(600);
                    break;
                    
                case "REGISTER":
                    RegisterView registerView = new RegisterView();
                    registerView.setNavigationListener(this);
                    primaryStage.setScene(registerView.getScene());
                    primaryStage.setWidth(800);
                    primaryStage.setHeight(700);
                    break;
                    
                case "CART":
                    if (params.length > 0) {
                        currentCustomerId = params[0];
                        CartView cartView = new CartView(currentCustomerId);
                        cartView.setNavigationListener(this);
                        primaryStage.setScene(cartView.getScene());
                        primaryStage.setWidth(900);
                        primaryStage.setHeight(700);
                    }
                    break;
                    
                case "COURIER_LIST":
                    if (params.length > 0) {
                        currentCourierId = params[0];
                    }
                    CourierListView courierListView = new CourierListView(currentCourierId);
                    courierListView.setNavigationListener(this);
                    primaryStage.setScene(courierListView.getScene());
                    primaryStage.setWidth(1000);
                    primaryStage.setHeight(700);
                    break;
                    
                case "COURIER_DETAIL":
                    if (params.length > 1) {
                        String deliveryId = params[0];
                        currentCourierId = params[1];
                        CourierDetailView courierDetailView = new CourierDetailView(deliveryId, currentCourierId);
                        courierDetailView.setNavigationListener(this);
                        primaryStage.setScene(courierDetailView.getScene());
                        primaryStage.setWidth(900);
                        primaryStage.setHeight(700);
                    }
                    break;
                    
                case "TOPUP":
                    if (params.length > 0) {
                        currentCustomerId = params[0];
                        TopUpView topUpView = new TopUpView(currentCustomerId);
                        topUpView.setNavigationListener(this);
                        primaryStage.setScene(topUpView.getScene());
                        primaryStage.setWidth(700);
                        primaryStage.setHeight(500);
                    }
                    break;
                    
                case "ADMIN_ORDERS":
                    AdminOrderListView adminOrderView = new AdminOrderListView(currentAdminId);
                    adminOrderView.setNavigationListener(this);
                    primaryStage.setScene(adminOrderView.getScene());
                    primaryStage.setWidth(1100);
                    primaryStage.setHeight(700);
                    break;

                case "EDIT_PROFILE":
                    if (params.length > 0) {
                        currentUserId = params[0];
                        ProfileView profileView = new ProfileView(currentUserId);
                        profileView.setNavigationListener(this);
                        primaryStage.setScene(profileView.getScene());
                        primaryStage.setWidth(900);
                        primaryStage.setHeight(700);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Menavigasi kembali ke tampilan sebelumnya berdasarkan konteks saat ini
     */
    @Override
    public void goBack() {
        // Navigate based on current view
        if ("ADMIN_DETAIL".equals(currentView)) {
            navigateTo("ADMIN_LIST");
        } else if ("ADMIN_ORDERS".equals(currentView)) {
            navigateTo("ADMIN_LIST");
        } else if ("CUSTOMER_DETAIL".equals(currentView)) {
            navigateTo("CUSTOMER_LIST", currentCustomerId);
        } else if ("CART".equals(currentView)) {
            navigateTo("CUSTOMER_LIST", currentCustomerId);
        } else if ("TOPUP".equals(currentView)) {
            navigateTo("CUSTOMER_LIST", currentCustomerId);
        } else if ("COURIER_DETAIL".equals(currentView)) {
            navigateTo("COURIER_LIST", currentCourierId);
        } else if ("REGISTER".equals(currentView)) {
            navigateTo("LOGIN");
        } else if ("CUSTOMER_LIST".equals(currentView)) {
            navigateTo("LOGIN");
        } else if ("COURIER_LIST".equals(currentView)) {
            navigateTo("LOGIN");
        } else if ("EDIT_PROFILE".equals(currentView)) {
            navigateTo("CUSTOMER_LIST", currentCustomerId);
        }
    }

    /**
     * Metode utama untuk menjalankan aplikasi
     */
    public static void main(String[] args) {
        launch(args);
    }
}
