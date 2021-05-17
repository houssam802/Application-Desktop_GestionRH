package fx_Affichage;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import baseDonnees.*;
import fx_Recrutement.Departement;
import fx_Recrutement.Personnel;
import fx_Recrutement.RecrutementController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Affichage_Personnels implements Initializable{

	
	HashMap<String,String> champsEtvaleurs;
	
	@FXML
	private  AnchorPane Parent_Pan;
	
///Zone contient le tableau
	@FXML
    private  AnchorPane myPan;
	
//Définir un TableView charger par les attributs des objets de type Affichage_Class .	
    @FXML
    TableView<Affichage_Personnels_Class> myTable;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Nom;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Prenom;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Email;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Nmr_Tel;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_CIN;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_type_Contrat;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Date_Fin_Contrat;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Date_Embauche;
    
    @FXML
    TableColumn<Affichage_Personnels_Class,String> Col_Departement;
    
////////////////////////////////////////////////////

    @FXML
    private Button actualiser;
    
///Zone de recherche
   
	@FXML
	private  AnchorPane zone_recherche;
	
	@FXML
	private JFXComboBox<String> box_recherche;

	@FXML
    TextField txt_recherche;
	    
	@FXML
	Button btn_recherche;
	
////////////////////////////////////////////////////
	
///Zone de fenêtre pop-up 
	@FXML
	private  AnchorPane pop_up;
	
    @FXML
	Button  fermer_pop_up_btn;
    
    @FXML
	Button  Modification_btn;
    
    @FXML
	Button  Supprimation_btn;
    
////////////////////////////////////////////////////
    
    //Utiliser dans la fonction update (classe "Select") par la classe "EvaluationController".
    public static String Numero_CIN;    
    
    ///Retourner la liste de tous les personnels de l'entreprise .
    public ArrayList<Personnel> selectallPers() {
    	Select_Supprimer_Modifier l=new Select_Supprimer_Modifier();
    	return l.selectAll();
    }
    
    ///Retourner la liste de tous les départements de l'entreprise .
    public ArrayList<Departement> selectallDept() {
    	Select_Supprimer_Modifier l=new Select_Supprimer_Modifier();
    	return l.selectAlllDepartement();
    }
    
    
    //Initialisation de TableView avec tous les personnels
    @Override
    public void initialize(URL location,ResourceBundle resources) { 
      ///Initialisation du ComboBox
    	box_recherche.setItems(FXCollections.observableArrayList(
			    "Personnel", "Departement")
			);
    	box_recherche.setValue("Personnel");
      ////////////////////////////////////////////////////////////////
      ///Définir à chaque cellule les attributs qui les représentent ,des objets de type Affichage_Personnels_Class .
    	Col_Nom.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getNom()));
    	Col_Prenom.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getPrenom()));
    	Col_Email.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getAdresse_Gmail()));
    	Col_CIN.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getNumero_CIN()));
    	Col_Nmr_Tel.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getTel_Personnel()));
		Col_Departement.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getDept().getNom_Departement()));
    	Col_type_Contrat.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getType_Contract()));
    	Col_Date_Fin_Contrat.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getDate_Fin_Contract()));
    	Col_Date_Embauche.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPers().getDate_Embauche()));
      //////////////////////////////////////////////////////////////////////////
      ///Charger TableView
    	ObservableList<Personnel> employees=FXCollections.observableArrayList(
    			selectallPers()
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			selectallDept()
    			);
    	
    	ObservableList<Affichage_Personnels_Class> pers_dept=FXCollections.observableArrayList();
    	///Associé à chaque personnel son département .
    	for (Personnel elm_prs : employees) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_prs.getID_Departement() == elm_dept.getId_Departement()) {
        			 pers_dept.add(
     	                  	new Affichage_Personnels_Class(elm_dept,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(pers_dept);
      //////////////////////////////////////////////////////////////////////
    }
    
    ///Actualiser le Tableau d'affichage
    public void ActionActualiser(ActionEvent e) {
    	ObservableList<Personnel> employees=FXCollections.observableArrayList(
    			selectallPers()
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			selectallDept()
    			);
    	
    	ObservableList<Affichage_Personnels_Class> pers_dept=FXCollections.observableArrayList();
    	for (Personnel elm_prs : employees) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_prs.getID_Departement() == elm_dept.getId_Departement()) {
        			 pers_dept.add(
     	                  	new Affichage_Personnels_Class(elm_dept,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(pers_dept);
         txt_recherche.setText("");
    }
    
    
    ///Action de clicker sur "btn_recherche"
    public void Search(ActionEvent e) {	
    	//System.out.println(myTable.getVisibleLeafColumns().get(0).getCellData(0));
    	//Tableau qui va contenir les Affichage_Classs vérifiant la contrainte de recherche .
    	//Et par lequel "myTable" sera remplie.
    	ArrayList<Personnel> PersonnelChercher = new ArrayList<Personnel>();
    	ArrayList<Departement> DepartementChercher=new ArrayList<Departement>();
    	//Parcourir la liste des employés (Objet du classe "Affichage_Class").
    	if(box_recherche.getValue()=="Personnel") {
    	for (Personnel employe : selectallPers()) { 
    		//Table de méthodes du classe "Affichage_Class".
    		Method[] methodes = employe.getClass().getMethods();
    		bloc :{
    		for(Method methode : methodes) {
    			//Si une méthode est une "Getter" :
    			if(methode.getName().matches("get.+")) {
    				try {
    					//Si le retenue de la méthode n'est pas nulle :
    					if(methode.invoke(employe, (Object[])null) != null) {
    						String copy = methode.invoke(employe, (Object[])null).toString().toLowerCase() ;
    						//Vérifie si la valeur retenue correspond au text saisi .
    						//Une fois qu'il y a une correspondance ,ajouter l'élément au "Affichage_ClassChercher" et passer au Affichage_Class suivant.
    						if(copy.matches("^"+txt_recherche.getText().toLowerCase()+".*")) {
    							PersonnelChercher.add(employe);
    							break bloc;
    						}
    					}
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
    			}
    		}
    		}
    	}
    	
    	}else PersonnelChercher=selectallPers();
    	
    	if(box_recherche.getValue()=="Departement") {
    	for (Departement depart : selectallDept()) { 
    		//Table de méthodes du classe "Affichage_Class".
    		Method[] methodes = depart.getClass().getMethods();
    		bloc :{
    		for(Method methode : methodes) {
    			//Si une méthode est une "Getter" :
    			if(methode.getName().matches("get.+")) {
    				try {
    					//Si le retenue de la méthode n'est pas nulle :
    					if(methode.invoke(depart, (Object[])null) != null) {
    						String copy = methode.invoke(depart, (Object[])null).toString().toLowerCase() ;
    						//Vérifie si la valeur retenue correspond au text saisi .
    						//Une fois qu'il y a une correspondance ,ajouter l'élément au "Affichage_ClassChercher" et passer au Affichage_Class suivant.
    						if(copy.matches("^"+txt_recherche.getText().toLowerCase()+".*")) {
    							DepartementChercher.add(depart);
    							break bloc;
    						}
    					}
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
    			}
    		}
    		}
    	}
    	}else DepartementChercher=selectallDept();
    	 ///Charger TableView
    	ObservableList<Personnel> employees=FXCollections.observableArrayList(
    			PersonnelChercher
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			DepartementChercher
    			);
    	
    	ObservableList<Affichage_Personnels_Class> pers_dept=FXCollections.observableArrayList();
    	for (Personnel elm_prs : employees) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_prs.getID_Departement() == elm_dept.getId_Departement()) {
        			 pers_dept.add(
     	                  	new Affichage_Personnels_Class(elm_dept,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(pers_dept);
    }
    
  ///Action de clicker sur une ligne du tableau
    public void affiche_pop_up(MouseEvent e) {
      if(myTable.getSelectionModel().getSelectedItems().get(0)!=null) {
    	Affichage_Personnels_Class employe_Selectionner = new Affichage_Personnels_Class();
    	employe_Selectionner = myTable.getSelectionModel().getSelectedItems().get(0);
        champsEtvaleurs = new HashMap<String,String>();
        Field[] fieldsAffichage_Class=employe_Selectionner.getPers().getClass().getDeclaredFields();
    	Method[] methodsAffichage_Class = employe_Selectionner.getPers().getClass().getMethods();
    	//Remplissage du hashmap "champsEtvaleurs".
    	//key : field.getName() (exemple : "Nom").
    	//value : method.invoke(employe_Selectionner, (Object[])null) (exemple : getNom())
    	for(Method method : methodsAffichage_Class) {
    	   if(method.getName().matches("get.+")) {
    		   for (Field field: fieldsAffichage_Class) {
    			   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
    					try {
    						String resultat;
    						if(method.invoke(employe_Selectionner.getPers(), (Object[])null) != null) {
    							resultat = method.invoke(employe_Selectionner.getPers(), (Object[])null).toString();
    						}else resultat="";	
    						champsEtvaleurs.put(field.getName(), resultat);
    					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
    						e1.printStackTrace();
    					}
    				}
    		   }
    	   }
    	}
	        pop_up.setVisible(true);
	        myPan.setMouseTransparent(true);
	        zone_recherche.setMouseTransparent(true);
	        myPan.setOpacity(0.5);
	        Numero_CIN=employe_Selectionner.getPers().getNumero_CIN();
    	}
    }
    
    ///Action de clicker sur "fermer_pop_up_btn" .
    public void fermer_pop_up(ActionEvent e) {
    	pop_up.setVisible(false);
        myPan.setMouseTransparent(false);
        myPan.setOpacity(1);
        zone_recherche.setMouseTransparent(false);
    }
    
    
    ///Action de clicker sur "Modification_btn" .
    public void Modification(ActionEvent e) {
	    pop_up.setVisible(false);
        myPan.setMouseTransparent(false);
        zone_recherche.setMouseTransparent(false);
        myPan.setOpacity(1);
		try {
				Parent root;
				//Créer un nouveau "Stage" lier au "Stage" principale . 
				Stage stagesecondaire = new Stage();
		    	stagesecondaire.initOwner(Parent_Pan.getScene().getWindow());
                stagesecondaire.setTitle("MODIFICATION");
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_Recrutement/Recrutement.fxml"));
				root = loader.load();
				ScrollPane p=new ScrollPane(root);
				//Instancier un objet du classe "RecrutementController".
				RecrutementController controller = loader.getController();
				//Remplissage des champs par les données du Affichage_Class sélectionner .
				controller.chargerDonnées(champsEtvaleurs);
		        Scene scene = new Scene(p);
        	    scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
        	    stagesecondaire.setScene(scene);
        	    stagesecondaire.show();
        	    //Mettre à jour "myTable" après les modifications .
        	    stagesecondaire.setOnCloseRequest(event -> {
        	    	 ///Charger TableView
        	    	ObservableList<Personnel> employees=FXCollections.observableArrayList(
        	    			selectallPers()
        	    			);
        	    	ObservableList<Departement> depart=FXCollections.observableArrayList(
        	    			selectallDept()
        	    			);
        	    	
        	    	ObservableList<Affichage_Personnels_Class> pers_dept=FXCollections.observableArrayList();
        	    	for (Personnel elm_prs : employees) {
        	    	  for (Departement elm_dept : depart) {
        	        		 if(elm_prs.getID_Departement() == elm_dept.getId_Departement()) {
        	        			 pers_dept.add(
        	     	                  	new Affichage_Personnels_Class(elm_dept,elm_prs)
        	     	                  );
        	        		 }
        	        	 }
        		    }
        	  
        	         myTable.setItems(pers_dept);
        	    });
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    }
    
    
    ///Action de clicker sur "Supprimation_btn" .
    //
    public void Suppression(ActionEvent e) {
    	Select_Supprimer_Modifier app = new Select_Supprimer_Modifier();
    	app.delete_prs(Numero_CIN);
    	pop_up.setVisible(false);
        myPan.setMouseTransparent(false);
        zone_recherche.setMouseTransparent(false);
        myPan.setOpacity(1);
        ///Charger TableView
    	ObservableList<Personnel> employees=FXCollections.observableArrayList(
    			selectallPers()
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			selectallDept()
    			);
    	
    	ObservableList<Affichage_Personnels_Class> pers_dept=FXCollections.observableArrayList();
    	for (Personnel elm_prs : employees) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_prs.getID_Departement() == elm_dept.getId_Departement()) {
        			 pers_dept.add(
     	                  	new Affichage_Personnels_Class(elm_dept,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(pers_dept);
    }
    
}
