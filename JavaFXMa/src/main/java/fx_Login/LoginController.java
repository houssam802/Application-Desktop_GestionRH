package fx_Login;



import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import baseDonnees.Login; 

public class LoginController implements Initializable{
	
	@FXML
	private  AnchorPane myPan1;
	
	@FXML
	private  AnchorPane pane_window;
	
	@FXML
	private Button mybut1;
	
	@FXML
	private TextField mytxt1;
	
	@FXML
	private Label mylb1;
	
	@FXML
	private JFXPasswordField myPwdtxt;
	
	@FXML
	private Label mylb2;
	
	 @Override
	 public void initialize(URL location,ResourceBundle resources) {
		 AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("/fx_Accueil/window.fxml"));
			pane.setPrefSize(454,25);
			pane_window.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	
	public void Msgerr(String choix) {
		 switch(choix){
		 case "1": mylb1.setText("Nom introuvable");
		           break;
		 case "2": mylb2.setText("Mot de passe incorrect");
		 break;
		 default:break;
	 }
	}
	
	public void SetLabel() {
		mylb1.setText("");
	    mylb2.setText("");
	}

	//Action de cliquer sur le boutton "mybut1" (login)
	public void Login(ActionEvent event) throws IOException {
		SetLabel();
		Login Tester_login=new Login("");
		//Tester d'abord si le nom saisie déja exist .
		if(!Tester_login.tester_saisission_Nom(mytxt1.getText())) {
			Msgerr("1");
		}else {
			//Si le nom exist ,tester si le mot de passe saisie est celui associé aux utilisateur.
			if(!Tester_login.tester_saisission_Pdm(mytxt1.getText(),myPwdtxt.getText())) {
				Msgerr("2");
		    //Si toutes les conditions sont validées ,on passe à la page d'acceuil .
			}else {
				Parent root = FXMLLoader.load(getClass().getResource("/fx_Accueil/Accueil.fxml"));
				Scene scene = new Scene(root);
	            Stage appStage2 = new Stage();
	            ((Stage)((myPan1).getScene().getWindow())).close(); 
	            appStage2.setTitle("Acceuil");
	            appStage2.setScene(scene);
	            scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
	            appStage2.setMaximized(true);
	            appStage2.setMinHeight(650);
	            appStage2.setMinWidth(900);
	            appStage2.show();     
			}
		}
	}

}
