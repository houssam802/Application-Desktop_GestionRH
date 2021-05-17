package fx_Accueil;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowController{
	@FXML
	private  AnchorPane pan_wind;
	
	@FXML
	private Button btn_fermer_wind;
	
	@FXML
	private Label titre_window;
	
	public void femer_window(ActionEvent e) {
		((Stage)((pan_wind).getScene().getWindow())).close(); 
	}

}
