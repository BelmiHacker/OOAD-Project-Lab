package view;

import controller.CustomerHandler;
import controller.OrderHandler;
import controller.UserHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.OrderHeader;

public class CustomerOrderHistoryView {

	// UI Components
	private Scene scene;
    private BorderPane mainLayout;
    private TableView<OrderHeader> orderTable;
    
    // Handlers
    private OrderHandler oc = new OrderHandler();
    private UserHandler uc = new UserHandler();
    private CustomerHandler ch = new CustomerHandler();
    
    private NavigationListener navigationListener;
	
    // Constructor
    public CustomerOrderHistoryView() {
//		TODO ????
		
	}
    
    private void setupLayout() {
    	mainLayout.setStyle("-fx-background-color: #f5f5f5;");
    	
    	VBox header = new VBox();
	    header.setStyle("-fx-background-color: #c8dcfa; -fx-padding: 15;");
	    header.setAlignment(Pos.CENTER_LEFT);
	    
	    Label title = new Label("Sejarah Orderr");
	    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    title.setTextFill(Color.web("#333333"));

	    header.getChildren().add(title);
	    mainLayout.setTop(header);
    }
    
	
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
	
	private void init() {
		mainLayout = new BorderPane();
		orderTable = new TableView<>();
	}
}
