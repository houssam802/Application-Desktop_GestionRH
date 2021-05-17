package fx_Projet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import baseDonnees.Inserer;
import baseDonnees.Select_Supprimer_Modifier;
import fx_Affichage.Controller_Projet_scene;
import fx_Affichage.Controller_Tasks_scene;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

@SuppressWarnings("unused")
public class Projet_inputs implements Initializable {

	private HashMap<String, Label> map_Labels = new HashMap<String, Label>();
	private HashMap<String, String> champetval = new HashMap<String, String>();
	private ArrayList<String> champs = new ArrayList<String>();
	private ArrayList<String> valeurs = new ArrayList<String>();

	int modification = 0;
	
	@FXML
	private AnchorPane myPane;

	@FXML
	private JFXTextField nom_Projet;

	@FXML
	private JFXTextField date_Debut;

	@FXML
	private JFXTextField date_Fin;

	@FXML
	private JFXComboBox<String> statut_Projet;

	@FXML
	private JFXComboBox<String> box_Departement;

	@FXML
	private JFXTextArea description_Projet;

	@FXML
	private Label err_txt_Projet;

	@FXML
	private Label err_txt_Date_debut;

	@FXML
	private Label err_txt_Date_fin;

	@FXML
	private Label err_box_Status;

	@FXML
	private Label err_box_Departement;

	@FXML
	private Label err_area_Description;

	@FXML
	private JFXButton btn_reset;

	@FXML
	private JFXButton btn_Submit;

////////////////////////////////////////////////////////////////////
	// initialisation
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		statut_Projet.setItems(FXCollections.observableArrayList("en Cours", "Finie", "en Retard"));
		statut_Projet.setValue("en Cours");

		box_Departement.setItems(FXCollections.observableArrayList("Achat", "Administration, Comptabilité et Finance",
				"IT et Télécommunications", "Ingénierie et Technique", "Management et Direction",
				"Marketing, Publicité et Evénement", "Ressources humaines", "Recherche et Développement", "Vente"));
		box_Departement.setValue("Achat");


		map_Labels.put("nom_Projet", err_txt_Projet);
		map_Labels.put("date_Debut", err_txt_Date_debut);
		map_Labels.put("date_Fin", err_txt_Date_fin);
		map_Labels.put("checkDate_Debut", err_txt_Date_debut);
		map_Labels.put("checkDate_Fin", err_txt_Date_fin);
	}
//////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public void chargerDonnées(HashMap<String, String> valeur) {
		modification = 1; 
		Field[] f1 = this.getClass().getDeclaredFields();
		// On parcourir les attributs du cette classe.
		for (Field field : f1) {
			try {
				Object object = field.get(this);
				if (object instanceof JFXTextField) {
					((JFXTextField) object).setText(valeur.get(field.getName()));
				}
				if (object instanceof JFXComboBox<?>) {
					if (!field.getName().matches("box_Departement")) {
						((JFXComboBox<String>) object).setValue(valeur.get(field.getName()));

						// selectDepartementFromId : retourne , d'après id de département , le nom du
						// département.
					} else {
						((JFXComboBox<String>) object).setValue(
								new Select_Supprimer_Modifier().selectDepartementFromId(Integer.parseInt(valeur.get("iD_Departement"))));
					}

				}
				if (object instanceof JFXTextArea) {
					((JFXTextArea) object).setText(valeur.get(field.getName()));
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
	}

/////////////////////////////////////////////////////
	@SuppressWarnings("static-access")
	public void messageSucces(String output) {
		myPane.setOpacity(0.5);
		Stage stagesecondaire = new Stage();
		stagesecondaire.initOwner(myPane.getScene().getWindow());
		stagesecondaire.initStyle(StageStyle.UNDECORATED);
		stagesecondaire.initModality(Modality.APPLICATION_MODAL);
		ImageView imagesucces = new ImageView("/FXpack/verifier.png");
		Label label = new Label(output);
		label.setStyle("-fx-font-size:15pt;");
		imagesucces.setFitHeight(125);
		imagesucces.setFitWidth(125);
		StackPane stack_pane = new StackPane();
		stack_pane.setMinHeight(175);
		stack_pane.setMinWidth(300);
		stack_pane.getChildren().addAll(imagesucces, label);
		stack_pane.setAlignment(imagesucces, Pos.TOP_CENTER);
		stack_pane.setAlignment(label, Pos.BOTTOM_CENTER);
		stack_pane.setPadding(new Insets(10.0));
		stack_pane.setStyle("-fx-background-color:white; -fx-border-width:1px; -fx-border-color:#302E3E;");
		Scene scene = new Scene(stack_pane);
		stagesecondaire.setScene(scene);
		stagesecondaire.show();
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(event -> {
			stagesecondaire.close();
			if(modification==0) {
				myPane.setOpacity(1);
				reset();
			} else {
		    	((Stage) myPane.getScene().getWindow()).close();
			}
		});
		delay.play();
	}

/////////////////////////////////////////////////
	public void reset() {
		Field[] f1 = this.getClass().getDeclaredFields();
		for (Field field : f1) {
			try {
				Object object = field.get(this);
				if (object instanceof Label) {
					((Label) object).setText("");
				}
				if (object instanceof JFXTextField) {
					((JFXTextField) object).setText("");
				}
				if (object instanceof JFXTextArea) {
					((JFXTextArea) object).setText("");
				}

			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
	}

//////////////////////////////////////////////////////	
	public void action_submit(ActionEvent e) {
		for (String key : map_Labels.keySet()) {
			map_Labels.get(key).setText("");
		}
		champetval.clear();
		champs.clear();
		valeurs.clear();
		if(modification==0) {
			// Voir bas de page.
			if (ChargerElement() == 1) {
				// D'après ArrayList "champs" on crée une chaîne de caractères contenant les
				// champs séparés par des virgules.
				String champ = champs.toString().replaceAll("\\[|\\]", "");
				// même chose que champ.
				String valeur = valeurs.toString().replaceAll("\\[|\\]", "");
				// L'objet qui va insérer les données d'employer dans la base de données.
				Inserer ins = new Inserer();
				try {
					ins.insertProjet(champ, valeur);
					messageSucces("Le Projet a été ajouté .");
				} catch (SQLException e1) {
					// Si la contrainte unique est jeté .
					String mess = (e1.getMessage().split("\\(")[1]).replace(")", "");// exemple : UNIQUE constraint failed:																// personnel.Numero_CIN
					String mess2 = (mess.split(":")[1]).split("\\.")[1];// exemple : Numero_CIN
					map_Labels.get(mess2).setText("Valeur déjà existe !");
				}
			}
		} else {
			if (ChargerElement() == 1) {
				// D'après ArrayList "champs" on crée une chaîne de caractères contenant les
				// champs séparés par des virgules.
				String champ = champs.toString().replaceAll("\\[|\\]", "");
				// même chose que champ.
				String valeur = valeurs.toString().replaceAll("\\[|\\]", "");
				// L'objet qui va insérer les données d'employer dans la base de données.
				Select_Supprimer_Modifier maj = new Select_Supprimer_Modifier();
				try {
					maj.updateProjet(Controller_Projet_scene.id_Projet,champ, valeur);
					messageSucces("Le Projet a été bien modifié .");
				} catch (SQLException e1) {
					// Si la contrainte unique est jeté .
					String mess = (e1.getMessage().split("\\(")[1]).replace(")", "");// exemple : UNIQUE constraint failed:																// personnel.Numero_CIN
					String mess2 = (mess.split(":")[1]).split("\\.")[1];// exemple : Numero_CIN
					map_Labels.get(mess2).setText("Valeur déjà existe !");
				}
			}
		}
	}

	// Rendre tous les champs (JFXTextField, JFXComboBox, JFXTextField) à leur �tat
	// initiale.
	public void action_reset(ActionEvent e) {
		reset();
	}

/////////////////////////////////////////////////////
	public int ChargerElement() {
		Projet element = new Projet();
		element.setNom_Projet(nom_Projet.getText());
		element.setDate_Debut(date_Debut.getText());
		element.setDate_Fin(date_Fin.getText());
		element.setDescription_Projet(description_Projet.getText());
		element.setStatut_Projet(statut_Projet.getValue());
		element.setID_Departement(new Select_Supprimer_Modifier().selectIdFromDepartement(box_Departement.getValue()));
		// Après l'instantion de notre objet , on déclenche les processus de validation
		// sur ce dernier .
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Projet>> contraint = validator.validate(element);
		// Dans le cas où le type de contrat différent de "CDD" , même si le champ
		// "Date_Fin_Contract" est désactiver
		// les données à insérer sont valider , mais pourtant la contrainte lier au
		// attribut "Date_Fin_Contract"
		// dans la classe "Personnel" sera lancé puisque "Date_Fin_Contract" sera nulle
		// .
		// Alors pour éviter ce problème on introduit l'entière "valider" qui égale à 0
		// si une contrainte n'est pas valider.
		int valider = 1;
		// Si la longueur du tableau des contraintes != 0 .
		if (contraint.size() > 0) {
			for (ConstraintViolation<Projet> contraintes : contraint) {
				// JFXTextField

				// map_Labels(nom d'attribut).setText(message d'erreur)
				map_Labels.get(contraintes.getPropertyPath().toString()).setText(contraintes.getMessage());
				valider = 0;

			}
		}
		// Si les données saisies sont valables alors on remplie "champs", "valeurs"
		// et "champsetvaleurs_Evaluation"
		if (valider == 1) {
			Field[] fieldsProjet = element.getClass().getDeclaredFields();
			Method[] methodsProjet = element.getClass().getMethods();
			for (Method method : methodsProjet) {
				if (method.getName().matches("get.+")) {
					for (Field field : fieldsProjet) {
						// fieldname = Nom
						// getNom
						if(field.getName()!="iD_Projet") {
							if (("get" + field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
								try {
									String resultat;
									if (method.invoke(element, (Object[]) null) != null) {
										resultat = method.invoke(element, (Object[]) null).toString();
									} else
										resultat = "";
									champetval.put(field.getName(), resultat);
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			for (String key : champetval.keySet()) {
				if (!champetval.get(key).isEmpty()) {
					champs.add(key);
					valeurs.add(champetval.get(key));
				}
			}
		}
		return valider;
	}
}
