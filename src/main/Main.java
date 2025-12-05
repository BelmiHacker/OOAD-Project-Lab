package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.NavigationListener;
import view.AdminProductListView;
import view.AdminProductDetailView;
import view.LoginView;
import view.RegisterView;
import view.CustomerProductListView;
import view.CustomerProductDetailView;

/**
 * Main - Entry point aplikasi JoymarKet dengan Navigation Controller
 */
public class Main extends Application implements NavigationListener {
    
    private Stage primaryStage;
    private String currentView = "ADMIN_LIST";
    private String currentAdminId = "ADM_00001";
    private String currentCustomerId = "CUST_00001";  // Will be set by login
    private String currentProductId = null;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        primaryStage.setTitle("JoymarKet - Digital Marketplace");
        primaryStage.setWidth(1100);
        primaryStage.setHeight(700);
        primaryStage.setResizable(false);
        
        // Start with product list for admin (hardcoded adminId for testing)
        navigateTo("CUSTOMER_LIST");
        primaryStage.show();
    }
    
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void goBack() {
        // Navigate based on current view
        if ("ADMIN_DETAIL".equals(currentView)) {
            navigateTo("ADMIN_LIST");
        } else if ("CUSTOMER_DETAIL".equals(currentView)) {
            navigateTo("CUSTOMER_LIST", currentCustomerId);
        } else if ("REGISTER".equals(currentView)) {
            navigateTo("LOGIN");
        } else if ("CUSTOMER_LIST".equals(currentView)) {
            navigateTo("LOGIN");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
