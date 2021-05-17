package fx_Affichage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import baseDonnees.Select_Supprimer_Modifier;
import fx_Projet.Projet;
import fx_Recrutement.Departement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Controller_Projet_scene implements Initializable {
	
	static HashMap<String,String> champsEtvaleursProjet;

	@FXML
	private AnchorPane myPane;

	@FXML
	private JFXComboBox<String> type_rechercher;

	@FXML
	private JFXTextField field_recherche;
	
	@FXML
	JFXButton btn_chercher;

	@FXML
	TableView<Affichage_Projets_Class> myTable;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_Nom_Projet;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_Departements;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_date_Debut_Projet;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_date_Fin_Projet;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_description_Projet;

	@FXML
	TableColumn<Affichage_Projets_Class, String> Col_statut_Projet;
	
	

    @FXML
    private Button actualiser;
	

	static String Nom_Projet;
	public static int id_Projet=1;

	///////////////////////////////////////////////////////////////////////////
	// select all projects
	public ArrayList<Projet> selectallPrj() {
		Select_Supprimer_Modifier l = new Select_Supprimer_Modifier();
		return l.selectAllPrj();
	}
	
	 ///Retourner la liste de tous les départements de l'entreprise .
    public ArrayList<Departement> selectallDept() {
    	Select_Supprimer_Modifier l=new Select_Supprimer_Modifier();
    	return l.selectAlllDepartement();
    }

	@Override
	public void initialize(URL local, ResourceBundle resources) {
		type_rechercher.setItems(FXCollections.observableArrayList("Nom Projet", "Statut Projet", "Departement"));
		field_recherche.setDisable(true);
		
		Col_Nom_Projet.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrj().getNom_Projet()));
		Col_Departements.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getDept().getNom_Departement()));
		Col_date_Debut_Projet.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrj().getDate_Debut()));
		Col_date_Fin_Projet.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrj().getDate_Fin()));
	    Col_description_Projet.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrj().getDescription_Projet()));
	    Col_statut_Projet.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrj().getStatut_Projet()));
	    
	    
	  ///Charger TableView
    	ObservableList<Projet> Projets=FXCollections.observableArrayList(
    			selectallPrj()
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			selectallDept()
    			);
    	
    	ObservableList<Affichage_Projets_Class> proj_dept=FXCollections.observableArrayList();
    	///Associé à chaque projet son département .
    	for (Projet elm_proj : Projets) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_proj.getID_Departement() == elm_dept.getId_Departement()) {
        			 proj_dept.add(
     	                  	new Affichage_Projets_Class(elm_proj,elm_dept)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(proj_dept);
      //////////////////////////////////////////////////////////////////////
		
	}

	public void Choix_Type_chercher(ActionEvent e) {
		if (type_rechercher.getValue() == "") {
			field_recherche.setDisable(true);
		} else {
			field_recherche.setDisable(false);
		}
	}
	
	
	///Actualiser le Tableau d'affichage
    public void ActionActualiser(ActionEvent e) {
    	  ///Charger TableView
    	ObservableList<Projet> Projets=FXCollections.observableArrayList(
    			selectallPrj()
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			selectallDept()
    			);
    	
    	ObservableList<Affichage_Projets_Class> proj_dept=FXCollections.observableArrayList();
    	///Associé à chaque projet son département .
    	for (Projet elm_proj : Projets) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_proj.getID_Departement() == elm_dept.getId_Departement()) {
        			 proj_dept.add(
     	                  	new Affichage_Projets_Class(elm_proj,elm_dept)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(proj_dept);
         field_recherche.setText("");
    }

	public void Action_chercher(ActionEvent e) {
		//Et par lequel "myTable" sera remplie.
    	ArrayList<Projet> ProjetChercher = new ArrayList<Projet>();
    	ArrayList<Departement> DepartementChercher=new ArrayList<Departement>();
    	if(type_rechercher.getValue()=="Statut Projet" || type_rechercher.getValue()=="Nom Projet") {
    	for (Projet projt : selectallPrj()) { 
    		Method[] methodes = projt.getClass().getMethods();
    		bloc :{
    		for(Method methode : methodes) {
    			//Si une méthode est une "Getter" :
    			if(methode.getName().matches("get.+")) {
    				try {
    					//Si le retenue de la méthode n'est pas nulle :
    					if(methode.invoke(projt, (Object[])null) != null) {
    						String copy = methode.invoke(projt, (Object[])null).toString().toLowerCase() ;
    						if(copy.matches("^"+field_recherche.getText().toLowerCase()+".*")) {
    							ProjetChercher.add(projt);
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
    	
    	}else ProjetChercher=selectallPrj();
    	
    	if(type_rechercher.getValue()=="Departement") {
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
    						if(copy.matches("^"+field_recherche.getText().toLowerCase()+".*")) {
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
    	
    	ObservableList<Projet> Projets=FXCollections.observableArrayList(
    			ProjetChercher
    			);
    	ObservableList<Departement> depart=FXCollections.observableArrayList(
    			DepartementChercher
    			);
    	
    	ObservableList<Affichage_Projets_Class> proj_dept=FXCollections.observableArrayList();
    	///Associé à chaque projet son département .
    	for (Projet elm_proj : Projets) {
    	  for (Departement elm_dept : depart) {
        		 if(elm_proj.getID_Departement() == elm_dept.getId_Departement()) {
        			 proj_dept.add(
     	                  	new Affichage_Projets_Class(elm_proj,elm_dept)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(proj_dept);
	}

	public void affiche_Tasks(MouseEvent e) throws IOException {
	  if(myTable.getSelectionModel().getSelectedItems().get(0)!=null) {
		Affichage_Projets_Class Projet_Selectionner = new Affichage_Projets_Class();
    	Projet_Selectionner = myTable.getSelectionModel().getSelectedItems().get(0);
        champsEtvaleursProjet = new HashMap<String,String>();
        Field[] fieldsAffichage_Class=Projet_Selectionner.getPrj().getClass().getDeclaredFields();
    	Method[] methodsAffichage_Class = Projet_Selectionner.getPrj().getClass().getMethods();
    	//Remplissage du hashmap "champsEtvaleurs".
    	//key : field.getName() (exemple : "Nom").
    	//value : method.invoke(employe_Selectionner, (Object[])null) (exemple : getNom())
    	for(Method method : methodsAffichage_Class) {
    	   if(method.getName().matches("get.+")) {
    		   for (Field field: fieldsAffichage_Class) {
    			   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
    					try {
    						String resultat;
    						if(method.invoke(Projet_Selectionner.getPrj(), (Object[])null) != null) {
    							resultat = method.invoke(Projet_Selectionner.getPrj(), (Object[])null).toString();
    						}else resultat="";	
    						champsEtvaleursProjet.put(field.getName(), resultat);
    					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
    						e1.printStackTrace();
    					}
    				}
    		   }
    	   }
    	}
    	Nom_Projet=Projet_Selectionner.getPrj().getNom_Projet();
    	id_Projet=Projet_Selectionner.getPrj().getID_Projet();
		Parent root = FXMLLoader.load(getClass().getResource("Affichage_Tasks.fxml"));
		myPane.getChildren().clear();
		myPane.getChildren().setAll(root);
	  }
	}
}
