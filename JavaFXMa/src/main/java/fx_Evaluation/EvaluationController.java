package fx_Evaluation;

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
import com.jfoenix.controls.JFXTextField;

import baseDonnees.Select_Supprimer_Modifier;
import fx_Affichage.Affichage_Personnels;
import fx_Recrutement.RecrutementController;
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

public class EvaluationController implements Initializable {
	
	//Associer les attributs et quelques méthodes du classe "Personnel_Evaluation" avec leurs labels d'erreurs,
	//qui affiche les messages d'erreurs définies par les annotations de lombok.
	//Exemple : map_Labels.put("Date_Evaluation", DateEvErreur);
	private HashMap<String,Label> map_Labels = new HashMap<String,Label>();
		
	//"champetval" sera remplie dans la fonction "MiseAJour";
	//(les élément seront les valeurs retenues par les getters de chaque attribut).
	private HashMap<String,String> champetval = new HashMap<String,String>();
		
	//champsEvaluation : Tableau des champs (clés du "champetval").
	//valeursEvaluation : Tableau des valeurs (valeurs du "champetval")
	//Les deux seront remplies dans la fonction "MiseAJour".
	private ArrayList<String> champsEvaluation, valeursEvaluation;
		
	//champsPersonnel : Tableau des champs (attributs du "Personnel").
	//valeursPersonnel : Tableau des valeurs (valeur du chaque champs).
	//Remplie dans la fonction "initialize" d'après la variable static du classe "RecrutementController" : "champsetvaleurs_Evaluation"
	private ArrayList<String> champPersonnel ,valeurPersonnel;
	//Dans la fonction "MiseAJour", ils seront modifiés de telle façons qu'ils contiennent aussi les champsEvaluation et valeursEvaluation .
		
	@FXML
	private AnchorPane anchorpane ;
	
	@FXML
	private JFXButton submit;
	
	@FXML
	private Label EvaluationErreur;
	
	@FXML
	private Label DateEvErreur;
	
	@FXML
	private Label DateProEvErreur;
	
	@FXML
	private JFXComboBox<String> Evaluation;
	
	@FXML
	private JFXTextField Date_Evaluation;
	
	@FXML
	private JFXTextField Date_Prochaine_Evaluation;
	
	@Override
	public void initialize(URL local,ResourceBundle resources) {
		HashMap<ArrayList<String>, ArrayList<String>> u = RecrutementController.champsetvaleurs_Evaluation;
  	    for(ArrayList<String> key : u.keySet()) {
	    	champPersonnel=key;
	    	valeurPersonnel=u.get(key);
  	  	}
  	    map_Labels.put("Evaluation", EvaluationErreur);
		map_Labels.put("Date_Evaluation", DateEvErreur);
		map_Labels.put("Date_Prochaine_Evaluation", DateProEvErreur);
		map_Labels.put("checkDate_Evaluation", DateEvErreur);
		map_Labels.put("checkDate_Prochaine_Evaluation", DateProEvErreur);
		Evaluation.setItems(FXCollections.observableArrayList(
				"très insatisfaisant", "insatisfaisant", "satisfaisant", "très satisfaisant"
	    ));
		
		//Remplie en avance les champs par les données d'évaluation extrait du base de données,
		//du personnel associé au "Affichage.Numero_CIN" (c'est le numéro de CIN obtenu lorsqu'on clique 
		//sur un élement du liste des employés afficher)
		chargerEvaluation(Affichage_Personnels.Numero_CIN);
	}
	
	public void chargerEvaluation(String CIN) {
		Personnel_Evaluation PE = new Personnel_Evaluation();
		//selectEvaluation (définis dans la classe "Select") : 
		//Selon le numéro CIN passé en argument , cette fonction select les informations sur l'évaluation du personnel. 
		//(retourne un objet de type "Personnel_Evaluation"). 
		PE=new Select_Supprimer_Modifier().selectEvaluation(CIN);
		Field[] fieldsPersonnel=PE.getClass().getDeclaredFields();
		Method[] methodsPersonnel = PE.getClass().getMethods();
		//D'après "fieldsPersonnel" en indique le champ (field.getName()) et
		//d'après "methodsPersonnel" en indique sa valeur (method.invoke(PE, (Object[])null)).
		for(Method method : methodsPersonnel) {
			//"methodsPersonnel" : un tableau de tous les méthodes définis dans la classe "Personnel_Evaluation" ,
			//mais en s'intéresse uniquement au getters pour obtient les valeurs.
		   if(method.getName().matches("get.+")) {
			   for (Field field: fieldsPersonnel) {
				   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
						try {
							String resultat;
							if(method.invoke(PE, (Object[])null) != null) {
								resultat = method.invoke(PE, (Object[])null).toString();
							}else resultat="";
							if(!field.getName().matches("Date.+")) {
								Evaluation.setValue(resultat);
							}else if(field.getName().matches(".+Prochaine.+")) {
								Date_Prochaine_Evaluation.setText(resultat);
							}else {
								Date_Evaluation.setText(resultat);
							}
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
			   }
		   }
		}
	}
	
	//Fonction pour indiquer que les modifications étaient bien effectuer en affichant un pop-up qui se ferme 
	//automatiquement après 3 secondes.
	@SuppressWarnings("static-access")
	public void messageSucces() {
		anchorpane.setOpacity(0.5);
		Stage stagesecondaire = new Stage();
	   	stagesecondaire.initOwner(anchorpane.getScene().getWindow());
	   	stagesecondaire.initStyle(StageStyle.UNDECORATED);
	   	stagesecondaire.initModality(Modality.APPLICATION_MODAL);
		ImageView imagesucces = new ImageView("/FXpack/verifier.png");
		Label label = new Label("Les données était bien modifié !");
		label.setStyle("-fx-font-size:15pt;");
		imagesucces.setFitHeight(125);
		imagesucces.setFitWidth(125);
		StackPane stackpane = new StackPane();
		stackpane.setMinHeight(175);
		stackpane.setMinWidth(300);
		stackpane.getChildren().addAll(imagesucces, label);
		stackpane.setAlignment(imagesucces,Pos.TOP_CENTER);
		stackpane.setAlignment(label,Pos.BOTTOM_CENTER);
		stackpane.setPadding(new Insets(10.0));
		stackpane.setStyle("-fx-background-color:white; -fx-border-width:1px; -fx-border-color:#302E3E;");
		Scene scene = new Scene(stackpane);
		stagesecondaire.setScene(scene);
	    stagesecondaire.show();
	    PauseTransition delay = new PauseTransition(Duration.seconds(3));
	    delay.setOnFinished( event -> {
	    	stagesecondaire.close();
	    	((Stage) anchorpane.getScene().getWindow()).close();
	    } );
	    delay.play();
	}
	
	public void ActionSubmit(ActionEvent e) {
		for(String key : map_Labels.keySet()) {
			map_Labels.get(key).setText("");
   	 	}
		Personnel_Evaluation element = new Personnel_Evaluation();
		element.setEvaluation(Evaluation.getValue().toString());
		element.setDate_Evaluation(Date_Evaluation.getText());
		element.setDate_Prochaine_Evaluation(Date_Prochaine_Evaluation.getText());
		int tousVide=0;
		if(Evaluation.getValue().toString().isEmpty() && Date_Evaluation.getText().isEmpty() && Date_Prochaine_Evaluation.getText().isEmpty()) {
			tousVide=1;
		}
		//La validation des données saisies. 
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
	    Set<ConstraintViolation<Personnel_Evaluation>> contraint = validator.validate(element);
	    if ((contraint.size() > 0) && tousVide==0 ) {
		     for (ConstraintViolation<Personnel_Evaluation> contraintes : contraint) {
		    	 map_Labels.get(contraintes.getPropertyPath().toString()).setText(contraintes.getMessage());
		     } 
	    }else {
	    		//Cette fonction s'occupe de remplir "champsEvaluation" et "valeursEvaluation" ,
	    		//après ajouter "champsEvaluation" au "champPersonnel" et "valeursEvaluation" au "valeurPersonnel",
	    		//et finalement mettre à jour la base de données .
	  	    	MiseAJour(champPersonnel,valeurPersonnel, element); 
	  	    	messageSucces();
	    }
	}
	
	public void MiseAJour(ArrayList<String> champPersonnel, ArrayList<String> valeurPersonnel, Personnel_Evaluation PE) {
		champsEvaluation=new ArrayList<String>();
		valeursEvaluation=new ArrayList<String>();
		Field[] fieldsPersonnel=PE.getClass().getDeclaredFields();
		Method[] methodsPersonnel = PE.getClass().getMethods();
		//Remplir "champsEvaluation" et "valeursEvaluation" 
		//D'après "fieldsPersonnel" en indique le champ (field.getName()) et
		//d'après "methodsPersonnel" en indique sa valeur (method.invoke(PE, (Object[])null)).
		for(Method method : methodsPersonnel) {
		   if(method.getName().matches("get.+")) {
			   for (Field field: fieldsPersonnel) {
				   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
						try {
							String resultat;
							if(method.invoke(PE, (Object[])null) != null) {
								resultat = method.invoke(PE, (Object[])null).toString();
							}else resultat="";	
							champetval.put(field.getName(), resultat);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
			   }
		   }
		}
		
		for(String key : champetval.keySet()) {
			if(!champetval.get(key).isEmpty()) {
				champsEvaluation.add(key);
				valeursEvaluation.add(champetval.get(key));
			}
		}
		//Ajouter "champsEvaluation" au "champPersonnel" et "valeursEvaluation" au "valeurPersonnel".
		champPersonnel.addAll(champsEvaluation);
		valeurPersonnel.addAll(valeursEvaluation);
		//D'après ArrayList "champPersonnel" et "valeurPersonnel" ,
		//on crée deux chaînes de caractères contenant les champs et leurs valeurs séparés par des virgules.
 		String champ = champPersonnel.toString().replaceAll("\\[|\\]", "");
 		String valeur = valeurPersonnel.toString().replaceAll("\\[|\\]", "");
		try {
			//update (définis dans la classe "Select") : 
			//Mettre à jour dans la base de données (table : Personnel), les données du personnel associé au numéro de CIN 
			//passé dans l'argument .
			new Select_Supprimer_Modifier().update(Affichage_Personnels.Numero_CIN, champ, valeur);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
