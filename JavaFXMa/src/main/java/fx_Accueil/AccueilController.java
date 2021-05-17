package fx_Accueil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class AccueilController implements Initializable{
	
	@FXML
	private StackPane stackpane;
	
	@FXML
	private Button recrutement;
	
	@FXML
	private Button affichagePer;
	
	@FXML
	private Button AddProjet;
	
	@FXML
	private Button affichagePro;
	
	@FXML
	private AnchorPane window_pane;
	
	
	public void Recrutement() {
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("/fx_Recrutement/Recrutement.fxml"));
			stackpane.getChildren().clear();
			stackpane.getChildren().setAll(pane);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void AffichagePer() {
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("/fx_Affichage/Affichage_tabPersonnels.fxml"));
			stackpane.getChildren().clear();
			stackpane.setAlignment(Pos.TOP_CENTER);
			stackpane.getChildren().setAll(pane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void AjouterProjet() {
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("/fx_Projet/Projet_inputs.fxml"));
			stackpane.getChildren().clear();
			stackpane.setAlignment(Pos.TOP_CENTER);
			stackpane.getChildren().setAll(pane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void AffichagePro() {
		try {
			Parent pane = FXMLLoader.load(getClass().getResource("/fx_Affichage/Affichage_Projets.fxml"));
			stackpane.getChildren().clear();
			stackpane.getChildren().setAll(pane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL local,ResourceBundle resources) {
		recrutement.setOnAction(event -> {
			recrutement.setStyle("-fx-background-color: #f4f4f4;"
					+ "	-fx-border-color:#f4f4f4;"
					+ "	-fx-text-fill:#5F5D73;"
			);
			affichagePer.setStyle("-fx-border-width:0;");
			affichagePro.setStyle("-fx-border-width:0;");
			AddProjet.setStyle("-fx-border-width:0;");
			Recrutement();
		});
		affichagePer.setOnAction(event -> {
			affichagePer.setStyle("-fx-background-color: #f4f4f4;"
					+ "	-fx-border-color:#f4f4f4;"
					+ "	-fx-text-fill:#5F5D73;"
			);
			recrutement.setStyle("-fx-border-width:0;");
			affichagePro.setStyle("-fx-border-width:0;");
			AddProjet.setStyle("-fx-border-width:0;");
			AffichagePer();
		});
		AddProjet.setOnAction(event -> {
			AddProjet.setStyle("-fx-background-color: #f4f4f4;"
					+ "	-fx-border-color:#f4f4f4;"
					+ "	-fx-text-fill:#5F5D73;"
			);
			recrutement.setStyle("-fx-border-width:0;");
			affichagePer.setStyle("-fx-border-width:0;");
			affichagePro.setStyle("-fx-border-width:0;");
			AjouterProjet();
		});
		affichagePro.setOnAction(event -> {
			affichagePro.setStyle("-fx-background-color: #f4f4f4;"
					+ "	-fx-border-color:#f4f4f4;"
					+ "	-fx-text-fill:#5F5D73;"
			);
			recrutement.setStyle("-fx-border-width:0;");
			affichagePer.setStyle("-fx-border-width:0;");
			AddProjet.setStyle("-fx-border-width:0;");
			AffichagePro();
		});
	}
	
}
