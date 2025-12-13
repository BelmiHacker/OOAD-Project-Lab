package view;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CustomerOrderHistory {

	// UI Components
	private Scene scene;
    private BorderPane mainLayout;
    private GridPane gridLayout;
    
    // Handlers
    private NavigationListener navigationListener;
	
    // Constructor
    public CustomerOrderHistory() {
//		TODO ????
		
	}
    
	// Getter Setter
	public void setNavigationListener(NavigationListener listener) {
		this.navigationListener = listener;
	}
}
