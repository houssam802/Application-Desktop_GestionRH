package fx_Recrutement;

import java.io.IOException;
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
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import baseDonnees.Inserer;
import baseDonnees.Select_Supprimer_Modifier;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class RecrutementController implements Initializable {


	//Pour associer les attributs et quelques méthodes du classe "Personnel" avec leurs labels d'erreurs,
	//qui affiche les messages d'erreurs définies par les annotations de lombok.
	//Exemple : map_Labels("Adresse_Gmail",Label : emailErreur)
	private HashMap<String, Label> map_Labels = new HashMap<String, Label>();
	
	//Les clés sont les choix possible du sexe(homme et femme) et les élément sont les deux checkbox définis. 
	//Exemple : map_Checkbox("homme",JFXCheckBox : homme)
	private HashMap<String, JFXCheckBox> map_Checkbox = new HashMap<String, JFXCheckBox>();
	
	//Les deux HashMap sont remplient dans la fonction remplir().
	
	
	//"champetval" sera remplie dans la fonction ChargerElement();
	//(les clés seront les attributs du classe "Personnel" et leurs valeurs sont
	//celles retenues par les getters de chaque attribut du classe "Personnel").
	private HashMap<String,String> champetval = new HashMap<String,String>();
	
	//Tableau des champs (attributs du "Personnel").
	//Remplie à partir du HashMap champetval
	private ArrayList<String> champs = new ArrayList<String>();
	
	//Tableau des valeurs (valeur du chaque champs).
	//Remplie à partir du HashMap champetval
	private ArrayList<String> valeurs = new ArrayList<String>();
	
	//"champs" et "valeurs" seront utilisé dans la fonction d'insertion (du classe "Inserer") dans la base de données, et
	//seront remplient dans la fonction ChargerElement();
	
	
	//Cette HashMap était créé pour associer les champs avec leurs valeurs.
	//Elle est static pour qu'on puisse passer les données saisies à la classe "EvaluationController".
	public static HashMap<ArrayList<String>, ArrayList<String>> champsetvaleurs_Evaluation= new HashMap<ArrayList<String>, ArrayList<String>>();
		
	
	@FXML
	private StackPane stackpane;
	
	@FXML
	private JFXButton submit;
	
	@FXML
	private JFXButton reset;
	
	@FXML
	private JFXButton suivant;
	
	@FXML
	private Label nomErreur;
	
	@FXML
	private Label prenomErreur;
	
	@FXML
	private Label fonctionErreur;	
	
	@FXML
	private Label dateNaissErreur;
	
	@FXML
	private Label cinErreur;
	
	@FXML
	private Label numTelErreur;
	
	@FXML
	private Label emailErreur;
	
	@FXML
	private Label SitErreur;
	
	@FXML
	private Label NbrEnfErreur;
	
	@FXML
	private Label dateEMErreur;
	
	@FXML
	private Label contratErreur;
	
	@FXML
	private Label dateFinCErreur;
	
	@FXML
	private Label salaireErreur;
	
	@FXML
	private Label sexeErreur;

	@FXML
	private JFXTextField Nom;
	
	@FXML
	private JFXTextField Prenom;
	
	@FXML
	private JFXTextField Adresse_Gmail;
	
	@FXML
	private JFXTextField Numero_CIN;
	
	@FXML
	private JFXTextField Tel_Personnel;
	
	@FXML
	private JFXTextField nombre_Enfant;
	
	@FXML
	private JFXTextField Titre;
	
	@FXML
	private JFXTextField salaire;
	
	@FXML
	private JFXTextField Date_naissance;
	
	@FXML
	private JFXTextField Date_Embauche;
	
	@FXML
	private JFXTextField Date_Fin_Contract;
	
	@FXML
	private JFXComboBox<String> type_Contract;
	
	@FXML
	private JFXComboBox<String> situation_Familiale;
	
	@FXML
	private JFXComboBox<String> departement;
	
	//Dans la classe "Personnel" , il existe une attribut "Sexe" de type string.
	//Pour le donner une valeur on est besoin d'un string qui prend comme valeur la valeur du case coché,
	//et cela le rôle du "Sexe".
	private String Sexe;
	
	@FXML
	private JFXCheckBox homme;
	
	@FXML
	private JFXCheckBox femme;
	
	
	@Override
	public void initialize(URL local,ResourceBundle resources) {
		//On remplie les hashmap.
		remplir();
		//On désactive le champ du date fin de contrat initialement (il sera activer lors de saisissement du contrat de durée déterminée (CDD) ).
		Date_Fin_Contract.setDisable(true);
		
		//On remplie les JFXComboBox.
		type_Contract.setItems(FXCollections.observableArrayList(
			    "CDI", "CDD", "CTT")
			);
		situation_Familiale.setItems(FXCollections.observableArrayList(
			    "Marié", "Divorcé", "Célibataire" )
			);
		departement.setItems(FXCollections.observableArrayList(
				"Achat", "Administration, Comptabilité et Finance", "IT et Télécommunications",
				"Ingénierie et Technique", "Management et Direction", "Marketing, Publicité et Evénement",
				"Ressources humaines", "Recherche et Développement", "Vente"
			));
		departement.setValue("Achat");
		nombre_Enfant.setText("0");
	}
	
	//Cette fonction est util lorsqu'on select à modifier les données d'un employer selectionner à partir du tableau,
	//et on veut charger ses données à partir du base de données et les mettre dans leurs champs.
	//On le fait appelle dans la classe "Affichage" lorsqu'on clique sur le bouton "modifier" (fonction Modification).
	//valeur ( key : champs | éléments : valeurs) 
	@SuppressWarnings("unchecked")
	public void chargerDonnées(HashMap<String, String> valeur) {
		submit.setVisible(false);
		reset.setVisible(false);
		suivant.setVisible(true);
        Field[] f1=this.getClass().getDeclaredFields();
        //On parcourir les attributs du cette classe.
		for (Field field: f1) {
			try {
				Object object = field.get(this);
					if( object instanceof JFXTextField) {
						((JFXTextField) object).setText(valeur.get(field.getName()));
					}
					if( object instanceof JFXComboBox<?>) {
						if(!field.getName().matches("departement")) {
							((JFXComboBox<String>) object).setValue(valeur.get(field.getName()));
							if(field.getName().matches("type_Contract")) {
								if(type_Contract.getValue()=="CDD") {
									Date_Fin_Contract.setDisable(false);
								}else {
									Date_Fin_Contract.setText("");
								}
							}
						}else {
							//selectDepartementFromId : retourne , d'après id de département , le nom du département. 
							((JFXComboBox<String>) object).setValue(new Select_Supprimer_Modifier().selectDepartementFromId(Integer.parseInt(valeur.get("iD_Departement"))));
						}
					}
					if( object instanceof JFXCheckBox) {
						if(((JFXCheckBox) object).getText().matches(valeur.get("Sexe"))) {
							((JFXCheckBox) object).setSelected(true);
							Sexe=valeur.get("Sexe");
						}
					}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//Remplissage de "map_Labels" et "checkbox" .
	public void remplir() {
		Personnel per = new Personnel();
		//Attributs du classe Personnel.
		Field[] fieldPersonnel=per.getClass().getDeclaredFields();
		//Attributs de cette classe.
		Field[] fieldRecrutement=this.getClass().getDeclaredFields();
		//On copie les attributs de type label.
		Field[] fieldRecrutement_Labels=new Field[14];
		int i=0;
		//Parcourir le tableau des attributs(Field) du classe courant
		//et insérer uniquement les attributs de type "Label" dans "fieldRecrutement_Labels"
		for (Field field: fieldRecrutement) {
			//Remarque : tous les labels se terminent par "Erreur".
			if(field.getName().matches(".+Erreur")) {
				fieldRecrutement_Labels[i]=field;
				i++;
			}
		}
		i=0;
		//Ce block est pour remplir map_Labels.
		//Tel que, on parcourir les attributs d'objet personnel un par un et on lui associé en utilisant "map_Labels" le label approprié.
		//Remarque : 
		//On a utilisé des abréviations pour définir nos labels (exemple : Personnel : Adresse_Gmail | Label : emailErreur) ,
		//Ce qui rendre le remplissage du notre "map_Labels" un peu difficile
		//C'est pour cela on avait définis les labels dans le même ordre que l'on avait définit les attributs du classe "Personnel",
		//et on va compter sur ca.
		for (Field field: fieldPersonnel) {
			if(!field.getName().matches("diplome|iD_Departement|iD_Cv")) {
				try {
					Object ji =fieldRecrutement_Labels[i].get(this);
					Label new_name = (Label) ji;
					map_Labels.put(field.getName(), new_name);
					i++;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		map_Labels.put("checkDate_naissance", dateNaissErreur);
		map_Labels.put("checkDate_Embauche", dateEMErreur);
		map_Labels.put("checkDate_Fin_Contract", dateFinCErreur);

		map_Checkbox.put("homme", homme);
		map_Checkbox.put("femme", femme);
	}
	
	//Fonction pour indiquer que le recrutement était bien effectuer en affichant un pop-up qui se ferme 
	//automatiquement après 3 secondes.
	@SuppressWarnings("static-access")
	public void messageSucces() {
		stackpane.setOpacity(0.5);
		Stage stagesecondaire = new Stage();
    	stagesecondaire.initOwner(stackpane.getScene().getWindow());
    	stagesecondaire.initStyle(StageStyle.UNDECORATED);
        stagesecondaire.initModality(Modality.APPLICATION_MODAL);
		ImageView imagesucces = new ImageView("/FXpack/verifier.png");
		Label label = new Label("Le recrutement était bien effectué !");
		label.setStyle("-fx-font-size:15pt;");
		imagesucces.setFitHeight(125);
		imagesucces.setFitWidth(125);
		StackPane stack_pane = new StackPane();
		stack_pane.setMinHeight(175);
		stack_pane.setMinWidth(300);
		stack_pane.getChildren().addAll(imagesucces, label);
		stack_pane.setAlignment(imagesucces,Pos.TOP_CENTER);
		stack_pane.setAlignment(label,Pos.BOTTOM_CENTER);
		stack_pane.setPadding(new Insets(10.0));
		stack_pane.setStyle("-fx-background-color:white; -fx-border-width:1px; -fx-border-color:#302E3E;");
		Scene scene = new Scene(stack_pane);
		stagesecondaire.setScene(scene);
	    stagesecondaire.show();
	    PauseTransition delay = new PauseTransition(Duration.seconds(3));
	    delay.setOnFinished( event -> {
	    	stagesecondaire.close();
	    	stackpane.setOpacity(1);
			reset();
	    } );
	    delay.play();
	}
	
	public void ActionSubmit(ActionEvent e) {
		//A chaque fois on clique sur submit , on vide les labels.
		for(String key : map_Labels.keySet()) {
			map_Labels.get(key).setText("");
   	 	}
		champetval.clear();
		champs.clear();
		valeurs.clear();
		//Voir bas de page.
	    if(ChargerElement()==1) {
	    	//D'après ArrayList "champs" on crée une chaîne de caractères contenant les champs séparés par des virgules.
	 		String champ = champs.toString().replaceAll("\\[|\\]", "");
	 		//même chose que champ.
	 		String valeur = valeurs.toString().replaceAll("\\[|\\]", "");
	 		//L'objet qui va insérer les données d'employer dans la base de données.
	 		Inserer ins = new Inserer();
	 		try {
	 			ins.insert(champ,valeur);
	 			messageSucces();
			} catch (SQLException e1) {
				//Si la contrainte unique est jeté .
				e1.printStackTrace();
				String mess = (e1.getMessage().split("\\(")[1]).replace(")", "");//exemple : UNIQUE constraint failed: personnel.Numero_CIN
				String mess2 = (mess.split(":")[1]).split("\\.")[1];//exemple : Numero_CIN
				map_Labels.get(mess2).setText("Valeur déjà existe !");
			}	
	     }
	}
	
	//Rendre tous les champs (JFXTextField, JFXComboBox, JFXCheckBox) à leur état initiale.
	public void reset() {
		Field[] f1=this.getClass().getDeclaredFields();
		for (Field field: f1) {
			try {
				Object object = field.get(this);
				if( object instanceof Label) {
					((Label) object).setText("");
				}
				if( object instanceof JFXTextField) {
					if(!field.getName().matches("NbrEnf")) {
						((JFXTextField) object).setText("");
					}else ((JFXTextField) object).setText("0");
				}
				if( object instanceof JFXComboBox<?>) {
					if(!field.getName().matches("departement")) {
						((JFXComboBox<?>) object).setValue(null);
					}
				}
				if( object instanceof JFXCheckBox) {
					((JFXCheckBox) object).setSelected(false);
				}
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//S'exécute lorsque le bouton de reset est cliqué.
	public void ActionReset(ActionEvent e) {
		reset();
	}
	
	//Cette fonction est exécuté lors de la selection d'une option du combobox "type_Contrat".
	//Si l'option choisit est "CDD" donc le champ date fin contrat sera activer , sinon il sera désactiver. 
	public void ActionContrat(ActionEvent e) {
		if(type_Contract.getValue()=="CDD") {
			Date_Fin_Contract.setDisable(false);
		}else {
			Date_Fin_Contract.setText("");
			Date_Fin_Contract.setDisable(true);
		}
	}
	
	//Cette fonction est exécuté si l'un des cases est cochés.
	//Elle nous permet de selectionner qu'une seule valeur.
	public void ActionCheck(ActionEvent e) {
		String id = (e.getSource().toString().split("\\[")[1]).split("\\,")[0].split("=")[1];//"homme" ou "femme"
		//Parcourir les clés du "map_Chechbox" : "homme" et "femme"
		for(String key : map_Checkbox.keySet()) {
			//Si la clé ne correspond pas à la valeur choisit (id), on désélectionne la case associé.
			if(!key.matches(id)) {
				map_Checkbox.get(key).setSelected(false);
			}else {
				//Sinon on donne au "Sexe" la valeur .
				Sexe=map_Checkbox.get(key).getText();
			}
   	 	}
	}
	
	//Si le bouton suivant est appuyé , la scène change et on passe au scène d'évaluation.
	public void ActionSuivant(ActionEvent e) {
		try {
			//Voir bas de page.
		    if(ChargerElement()==1) {
		    	Parent root = FXMLLoader.load(getClass().getResource("/fx_Evaluation/Evaluation.fxml"));
		    	stackpane.getChildren().clear();
		    	stackpane.getChildren().setAll(root);
		    }
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	//Elle instance un objet de type "Personnel" et définir des valeurs aux attributs associés à partir des champs  .
	//Cette fonction s'éxécute une fois on clique sur le bouton "submit" ou bien sur "suivant" .
	public int ChargerElement(){
		Personnel element = new Personnel();
		element.setNom(Nom.getText());
		element.setPrenom(Prenom.getText());
		element.setTitre(Titre.getText());
		element.setDate_naissance(Date_naissance.getText());
		element.setNumero_CIN(Numero_CIN.getText());
		element.setTel_Personnel(Tel_Personnel.getText());
		element.setAdresse_Gmail(Adresse_Gmail.getText());
		element.setSituation_Familiale(situation_Familiale.getValue());
		element.setNombre_Enfant(Integer.parseInt(nombre_Enfant.getText()));
		element.setDate_Embauche(Date_Embauche.getText());
		element.setType_Contract(type_Contract.getValue());
		element.setDate_Fin_Contract(Date_Fin_Contract.getText());
		element.setID_Departement(new Select_Supprimer_Modifier().selectIdFromDepartement(departement.getValue()));
		if(!salaire.getText().matches("")) {
			element.setSalaire(Float.parseFloat(salaire.getText()));
		}
		element.setSexe(Sexe);
		//Après l'instantion du notre objet , on déclenche les processus de validation sur ce dernier . 
		 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	     Validator validator = factory.getValidator();
	     Set<ConstraintViolation<Personnel>> contraint = validator.validate(element);
	     //Dans le cas où le type de contrat différent de "CDD" , même si le champ "Date_Fin_Contract" est désactiver
	     //les données à insérer sont valider , mais pourtant la contrainte lier au attribut "Date_Fin_Contract" 
   	  	 //dans la classe "Personnel" sera lancé puisque "Date_Fin_Contract" sera nulle .
	     //Alors pour éviter ce problème on introduit l'entière "valider" qui égale à 0 si une contrainte n'est pas valider.
	     int valider=1;
	     //Si la longueur du tableau des contraintes != 0 .
	     if (contraint.size() > 0 ) {
		      for (ConstraintViolation<Personnel> contraintes : contraint) {
		    	                                                                                 //JFXTextField
		    	  if(!(contraintes.getPropertyPath().toString().matches(".*Date_Fin_Contract") && Date_Fin_Contract.isDisable())) {
		    		  //map_Labels(nom d'attribut).setText(message d'erreur)
		    		  map_Labels.get(contraintes.getPropertyPath().toString()).setText(contraintes.getMessage());
		    		  valider=0;
		    	  }
		      } 
	     }
	     //Si les données saisissent sont valables alors on remplie "champs", "valeurs" et "champsetvaleurs_Evaluation" 
	     if(valider==1) {
				Field[] fieldsPersonnel=element.getClass().getDeclaredFields();
				Method[] methodsPersonnel = element.getClass().getMethods();
				for(Method method : methodsPersonnel) {
				   if(method.getName().matches("get.+")) {
					   for (Field field: fieldsPersonnel) {
						   //fieldname = Nom
						   //getNom
						   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
								try {
									String resultat;
									if(method.invoke(element, (Object[])null) != null) {
										resultat = method.invoke(element, (Object[])null).toString();
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
						champs.add(key);
						valeurs.add(champetval.get(key));
					}
				}
				champsetvaleurs_Evaluation.put(champs, valeurs);
		}
	return valider;
	}
}
