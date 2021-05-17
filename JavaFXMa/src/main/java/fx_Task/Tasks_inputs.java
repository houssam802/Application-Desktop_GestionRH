package fx_Task;


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
import fx_Affichage.Controller_Projet_scene;
import fx_Affichage.Controller_Tasks_scene;


public class Tasks_inputs implements Initializable {

	private HashMap<String, Label> map_Labels = new HashMap<String, Label>();
	private HashMap<String, String> champetval = new HashMap<String, String>();
	private ArrayList<String> champs = new ArrayList<String>();
	private ArrayList<String> valeurs = new ArrayList<String>();

	int modification = 0;
	
	@FXML
	private AnchorPane myPane;

	@FXML
	private JFXTextField nom_Task;

	@FXML
	private JFXTextField date_Debut_Task;

	@FXML
	private JFXTextField date_Fin_Task;

	@FXML
	private JFXComboBox<String> statut_Task;

	@FXML
	private JFXComboBox<String> box_Personnel;

	@FXML
	private JFXTextArea description_Task;

	@FXML
	private Label err_nom_Task;

	@FXML
	private Label err_date_Debut_Task;;

	@FXML
	private Label err_Date_fin_Task;

	@FXML
	private Label err_statut_Task;

	@FXML
	private Label err_Description_Task;

	@FXML
	private Label err_txt_Personnel;

	@FXML
	private JFXButton btn_reset;

	@FXML
	private JFXButton btn_Submit;

	// add combobox personnel
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		statut_Task.setItems(FXCollections.observableArrayList("en Cours", "Finie", "en Retard"));
		box_Personnel.setItems(
				FXCollections.observableArrayList(new Select_Supprimer_Modifier().selectAllPersonnel(Controller_Projet_scene.id_Projet)));
		statut_Task.setValue("en Cours");
		map_Labels.put("nom_Task", err_nom_Task);
		map_Labels.put("date_Debut_Task", err_date_Debut_Task);
		map_Labels.put("date_Fin_Task", err_Date_fin_Task);
		map_Labels.put("checkDate_Debut", err_date_Debut_Task);
		map_Labels.put("checkDate_Fin", err_Date_fin_Task);
		map_Labels.put("iD_Personnel", err_txt_Personnel);

	}

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
					ins.insertTask(champ, valeur);
					messageSucces("La Tache a été ajouté avec succes !");
				} catch (SQLException e1) {
					// Si la contrainte unique est jeté .
					String mess = (e1.getMessage().split("\\(")[1]).replace(")", "");
																						
					String mess2 = (mess.split(":")[1]).split("\\.")[1];
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
					maj.updateTask(Controller_Tasks_scene.id_Task,champ, valeur);
					messageSucces("La Tache a été bien modifié .");
				} catch (SQLException e1) {
					// Si la contrainte unique est jeté .
					String mess = (e1.getMessage().split("\\(")[1]).replace(")", "");// exemple : UNIQUE constraint failed:
																						// personnel.Numero_CIN
					String mess2 = (mess.split(":")[1]).split("\\.")[1];// exemple : Numero_CIN
					map_Labels.get(mess2).setText("Valeur déjà existe !");
				}
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
					if (!field.getName().matches("box_Personnel")) {
						((JFXComboBox<String>) object).setValue(valeur.get(field.getName()));

					} else {
						((JFXComboBox<String>) object).setValue(
								new Select_Supprimer_Modifier().selectPersonnelFromId(Integer.parseInt(valeur.get("iD_Personnel"))));
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

////////////////////////////////////////////////////////////

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
				if (object instanceof JFXComboBox<?>) {
					if (!field.getName().matches("statut_Task")) {
						((JFXComboBox<?>) object).setValue(null);
					}
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

//////////////////////////////////////////////////////////

	// Rendre tous les champs (JFXTextField, JFXComboBox, JFXTextField) à leur état
	// initiale.
	public void action_reset(ActionEvent e) {
		reset();
	}

	//////////////////////////////////////////////////
	public int ChargerElement() {
		Task element = new Task();
		element.setNom_Task(nom_Task.getText());
		element.setDate_Debut_Task(date_Debut_Task.getText());
		element.setDate_Fin_Task(date_Fin_Task.getText());
		element.setDescription_Task(description_Task.getText());
		element.setStatut_Task(statut_Task.getValue());
		if(box_Personnel.getValue()!=null) {
			element.setID_Personnel(new Select_Supprimer_Modifier().selectIdFromPersonnel(box_Personnel.getValue().split("\\_")[0],
				box_Personnel.getValue().split("\\_")[1]));
		}
		// Après l'instantion de notre objet , on déclenche les processus de validation
		// sur ce dernier .
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Task>> contraint = validator.validate(element);
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
			for (ConstraintViolation<Task> contraintes : contraint) {
				// JFXTextField

				// map_Labels(nom d'attribut).setText(message d'erreur)
				map_Labels.get(contraintes.getPropertyPath().toString()).setText(contraintes.getMessage());
				valider = 0;

			}
		}
		// Si les données saisies sont valables alors on remplie "champs", "valeurs"
		// et "champsetvaleurs_Evaluation"
		if (valider == 1) {
			Field[] fieldsTask = element.getClass().getDeclaredFields();
			Method[] methodsTask = element.getClass().getMethods();
			for (Method method : methodsTask) {
				if (method.getName().matches("get.+")) {
					for (Field field : fieldsTask) {
						// fieldname = Nom
						// getNom
						if(field.getName()!="iD_Task") {
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
							}else champetval.put(field.getName(), String.valueOf(Controller_Projet_scene.id_Projet));
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
