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
import fx_Projet.Projet_inputs;
import fx_Recrutement.Personnel;
import fx_Task.Task;
import fx_Task.Tasks_inputs;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller_Tasks_scene implements Initializable {
	
	HashMap<String,String> champsEtvaleurs;
	
	@FXML
	private  AnchorPane Parent_Pan;
	
	///Zone de recherche
	@FXML
	private  AnchorPane zone_recherche;
	
	@FXML
	private JFXComboBox<String> type_rechercher;
	
	@FXML
    private JFXTextField field_recherche;	
	
    @FXML
    JFXButton btn_chercher;
    //////////////////////////////////////
	 
	    @FXML
		private AnchorPane myPane;
	
	   @FXML
	    TableView<Affichage_Tasks_Class> myTable;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_Nom_Task;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_Personnel;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_date_Debut_Task;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_date_Fin_Task;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_description_Task;
	    
	    @FXML
	    TableColumn<Affichage_Tasks_Class,String> Col_statut_Task;
	    
	    @FXML
	    private AnchorPane zone_gauche;
	    
	    @FXML
	    private Label nom_projet;
	    
	    @FXML
	    private Button icon_retour;
	    
	    @FXML
	    private Button icon_Ajouter;
	    
	    @FXML
	    private Button icon_Modifier;
	    
	    
		///Zone de fenêtre pop-up 
		@FXML
		private  AnchorPane pop_up;
		
	    @FXML
		Button  fermer_pop_up_btn;
	    
	    @FXML
		Button  Modification_btn;
	    
	    @FXML
		Button  Supprimation_btn;
     ///////////////////////////////////////////////////////////////
	    
	  public static int id_Task; 
	    
	  ///Retourner la liste de tous les personnels de l'entreprise .
	    public ArrayList<Personnel> selectallPers() {
	    	Select_Supprimer_Modifier l=new Select_Supprimer_Modifier();
	    	return l.selectAll();
	    }
	    
	  ///Retourner la liste de tous les Tasks associé au projet 
		public ArrayList<Task> selectallTasks() {
			Select_Supprimer_Modifier l = new Select_Supprimer_Modifier();
			return l.selectAllProjet_Tasks(Controller_Projet_scene.id_Projet);
		}
		
		 ///Retourner l'id du personnel ayant le numero de cin entrer en parametre 
		public int selectIdFromPersonnel(String Nmr_Cin) {
			Select_Supprimer_Modifier l = new Select_Supprimer_Modifier();
			return l.selectIdFromPersonnel(Nmr_Cin);
		}
		
	    
	    
	    
	@Override
	public void initialize(URL local,ResourceBundle resources) {
		type_rechercher.setItems(FXCollections.observableArrayList(
			    "Nom Task", "Statut Task" ,"Personnel")
			);
		field_recherche.setDisable(true);
		
		nom_projet.setText(Controller_Projet_scene.Nom_Projet);
		
		Col_Nom_Task.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getTsk().getNom_Task()));
		Col_Personnel.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getPrs().getNom()+" "+Data.getValue().getPrs().getPrenom()));
		Col_date_Debut_Task.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getTsk().getDate_Debut_Task()));
		Col_date_Fin_Task.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getTsk().getDate_Fin_Task()));
		Col_description_Task.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getTsk().getDescription_Task()));
		Col_statut_Task.setCellValueFactory(Data-> new SimpleStringProperty(Data.getValue().getTsk().getStatut_Task()));
		
		  ///Charger TableView
    	ObservableList<Personnel> personnels=FXCollections.observableArrayList(
    			selectallPers()
    			);
    	ObservableList<Task> Tasks=FXCollections.observableArrayList(
    			selectallTasks()
    			);
    	
    	ObservableList<Affichage_Tasks_Class> Task_pers=FXCollections.observableArrayList();
    	///Associé à chaque personnel son Task .
    	for (Task elm_tsk : Tasks) {
    	  for (Personnel elm_prs : personnels) {
        		 if(elm_tsk.getID_Personnel() == selectIdFromPersonnel(elm_prs.getNumero_CIN())) {
        			 Task_pers.add(
     	                  	new Affichage_Tasks_Class(elm_tsk,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(Task_pers);
      //////////////////////////////////////////////////////////////////////
	}
	
	
	public void Choix_Type_chercher(ActionEvent e) {
		if(type_rechercher.getValue()=="") {
			field_recherche.setDisable(true);
		}else {
			field_recherche.setText("");
			field_recherche.setDisable(false);
		}
	}
	
	public void Action_chercher(ActionEvent e) {
		//Et par lequel "myTable" sera remplie.
    	ArrayList<Task> TaskChercher = new ArrayList<Task>();
    	ArrayList<Personnel> PersonnelChercher=new ArrayList<Personnel>();
    	if(type_rechercher.getValue()=="Statut Task" || type_rechercher.getValue()=="Nom Task") {
    	for (Task tsk : selectallTasks()) { 
    		Method[] methodes = tsk.getClass().getMethods();
    		bloc :{
    		for(Method methode : methodes) {
    			//Si une méthode est une "Getter" :
    			if(methode.getName().matches("get.+")) {
    				try {
    					//Si le retenue de la méthode n'est pas nulle :
    					if(methode.invoke(tsk, (Object[])null) != null) {
    						String copy = methode.invoke(tsk, (Object[])null).toString().toLowerCase() ;
    						if(copy.matches("^"+field_recherche.getText().toLowerCase()+".*")) {
    							TaskChercher.add(tsk);
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
    	
    	}else TaskChercher=selectallTasks();
    	
    	if(type_rechercher.getValue()=="Personnel") {
    	for (Personnel prs : selectallPers()) { 
    		//Table de méthodes du classe "Affichage_Class".
    		Method[] methodes = prs.getClass().getMethods();
    		bloc :{
    		for(Method methode : methodes) {
    			//Si une méthode est une "Getter" :
    			if(methode.getName().matches("get.+")) {
    				try {
    					//Si le retenue de la méthode n'est pas nulle :
    					if(methode.invoke(prs, (Object[])null) != null) {
    						String copy = methode.invoke(prs, (Object[])null).toString().toLowerCase() ;
    						if(copy.matches("^"+field_recherche.getText().toLowerCase()+".*")) {
    							PersonnelChercher.add(prs);
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
    	
		
		  ///Charger TableView
	  	ObservableList<Personnel> personnels=FXCollections.observableArrayList(
	  			PersonnelChercher
	  			);
	  	ObservableList<Task> Tasks=FXCollections.observableArrayList(
	  			TaskChercher
	  			);
	  	
	  	ObservableList<Affichage_Tasks_Class> Task_pers=FXCollections.observableArrayList();
	  	///Associé à chaque personnel son département .
	  	for (Task elm_tsk : Tasks) {
	  	  for (Personnel elm_prs : personnels) {
	      		 if(elm_tsk.getID_Personnel() == selectIdFromPersonnel(elm_prs.getNumero_CIN())) {
	      			 Task_pers.add(
	   	                  	new Affichage_Tasks_Class(elm_tsk,elm_prs)
	   	                  );
	      		 }
	      	 }
		    }
	
	       myTable.setItems(Task_pers);
	       
	}
	
	
	
	public void affiche_pop_up(MouseEvent e) {
		if(myTable.getSelectionModel().getSelectedItems().get(0)!=null) {
			Affichage_Tasks_Class Task_Selectionner = new Affichage_Tasks_Class();
	    	Task_Selectionner = myTable.getSelectionModel().getSelectedItems().get(0);
	        champsEtvaleurs = new HashMap<String,String>();
	        Field[] fieldsAffichage_Class=Task_Selectionner.getTsk().getClass().getDeclaredFields();
	    	Method[] methodsAffichage_Class = Task_Selectionner.getTsk().getClass().getMethods();
	    	//Remplissage du hashmap "champsEtvaleurs".
	    	//key : field.getName() (exemple : "Nom").
	    	//value : method.invoke(employe_Selectionner, (Object[])null) (exemple : getNom())
	    	for(Method method : methodsAffichage_Class) {
	    	   if(method.getName().matches("get.+")) {
	    		   for (Field field: fieldsAffichage_Class) {
	    			   if(("get"+field.getName().toLowerCase()).matches(method.getName().toLowerCase())) {
	    					try {
	    						String resultat;
	    						if(method.invoke(Task_Selectionner.getTsk(), (Object[])null) != null) {
	    							resultat = method.invoke(Task_Selectionner.getTsk(), (Object[])null).toString();
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
		        myPane.setMouseTransparent(true);
		        zone_recherche.setMouseTransparent(true);
		        zone_gauche.setMouseTransparent(true);
		        myPane.setOpacity(0.5);
		        id_Task=Task_Selectionner.getTsk().getID_Task();
		}
	}
	
	
	
    ///Action de clicker sur "fermer_pop_up_btn" .
    public void fermer_pop_up(ActionEvent e) {
    	pop_up.setVisible(false);
        myPane.setMouseTransparent(false);
        myPane.setOpacity(1);
        zone_recherche.setMouseTransparent(false);
        zone_gauche.setMouseTransparent(false);
    }
    
    
	//Action de cliquer sur le bouton "Modification_btn"
    public void Modification(ActionEvent e) {
	    pop_up.setVisible(false);
        myPane.setMouseTransparent(false);
        zone_recherche.setMouseTransparent(false);
        zone_gauche.setMouseTransparent(false);
        myPane.setOpacity(1);
		try {
				Parent root;
				//Créer un nouveau "Stage" lier au "Stage" principale . 
				Stage stagesecondaire = new Stage();
		    	stagesecondaire.initOwner(Parent_Pan.getScene().getWindow());
                stagesecondaire.setTitle("MODIFICATION");
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_Task/Tasks_inputs.fxml"));
				root = loader.load();
				Tasks_inputs controller = loader.getController();
				controller.chargerDonnées(champsEtvaleurs);
		        Scene scene = new Scene(root);
        	    scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
        	    stagesecondaire.setScene(scene);
        	    stagesecondaire.show();
        	    //Mettre à jour "myTable" après les modifications .
        	    stagesecondaire.setOnCloseRequest(event -> {
        	    	 ///Charger TableView
        	    	ObservableList<Personnel> personnels=FXCollections.observableArrayList(
        	    			selectallPers()
        	    			);
        	    	ObservableList<Task> Tasks=FXCollections.observableArrayList(
        	    			selectallTasks()
        	    			);
        	    	
        	    	ObservableList<Affichage_Tasks_Class> Task_pers=FXCollections.observableArrayList();
        	    	///Associé à chaque personnel son Task .
        	    	for (Task elm_tsk : Tasks) {
        	    	  for (Personnel elm_prs : personnels) {
        	        		 if(elm_tsk.getID_Personnel() == selectIdFromPersonnel(elm_prs.getNumero_CIN())) {
        	        			 Task_pers.add(
        	     	                  	new Affichage_Tasks_Class(elm_tsk,elm_prs)
        	     	                  );
        	        		 }
        	        	 }
        		    }
        	  
        	         myTable.setItems(Task_pers);
        	    
        	    });
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    }
    
    
    ///Action de clicker sur "Supprimation_btn" .
    //Supprimer la Task
    public void Suppression(ActionEvent e) {
    	Select_Supprimer_Modifier app = new Select_Supprimer_Modifier();
    	app.delete_Task(id_Task);
    	pop_up.setVisible(false);
        myPane.setMouseTransparent(false);
        zone_recherche.setMouseTransparent(false);
        zone_gauche.setMouseTransparent(false);
        myPane.setOpacity(1);
        ///Charger TableView
        ObservableList<Personnel> personnels=FXCollections.observableArrayList(
    			selectallPers()
    			);
    	ObservableList<Task> Tasks=FXCollections.observableArrayList(
    			selectallTasks()
    			);
    	
    	ObservableList<Affichage_Tasks_Class> Task_pers=FXCollections.observableArrayList();
    	///Associé à chaque personnel son Task .
    	for (Task elm_tsk : Tasks) {
    	  for (Personnel elm_prs : personnels) {
        		 if(elm_tsk.getID_Personnel() == selectIdFromPersonnel(elm_prs.getNumero_CIN())) {
        			 Task_pers.add(
     	                  	new Affichage_Tasks_Class(elm_tsk,elm_prs)
     	                  );
        		 }
        	 }
	    }
  
         myTable.setItems(Task_pers);
    
    
   }


	public void Action_Ajouter_Task(ActionEvent e) throws IOException {
		try {
			Parent root;
			//Créer un nouveau "Stage" lier au "Stage" principale . 
			Stage stagesecondaire = new Stage();
	    	stagesecondaire.initOwner(Parent_Pan.getScene().getWindow());
            stagesecondaire.setTitle("Ajouter une task");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_Task/Tasks_inputs.fxml"));
			root = loader.load();
		        Scene scene = new Scene(root);
        	    scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
        	    stagesecondaire.setScene(scene);
        	    stagesecondaire.show();
        	    //Mettre à jour "myTable" après les modifications .
        	    stagesecondaire.setOnCloseRequest(event -> {
        	    	 ///Charger TableView
        	    	ObservableList<Personnel> personnels=FXCollections.observableArrayList(
        	    			selectallPers()
        	    			);
        	    	ObservableList<Task> Tasks=FXCollections.observableArrayList(
        	    			selectallTasks()
        	    			);
        	    	
        	    	ObservableList<Affichage_Tasks_Class> Task_pers=FXCollections.observableArrayList();
        	    	///Associé à chaque personnel son Task .
        	    	for (Task elm_tsk : Tasks) {
        	    	  for (Personnel elm_prs : personnels) {
        	        		 if(elm_tsk.getID_Personnel() == selectIdFromPersonnel(elm_prs.getNumero_CIN())) {
        	        			 Task_pers.add(
        	     	                  	new Affichage_Tasks_Class(elm_tsk,elm_prs)
        	     	                  );
        	        		 }
        	        	 }
        		    }
        	  
        	         myTable.setItems(Task_pers);
        	    
        	    });
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}
	
	public void Action_Modifier_Projet (ActionEvent e) throws IOException {
		try {
			Parent root;
			//Créer un nouveau "Stage" lier au "Stage" principale . 
			Stage stagesecondaire = new Stage();
	    	stagesecondaire.initOwner(Parent_Pan.getScene().getWindow());
            stagesecondaire.setTitle("MODIFICATION");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx_Projet/Projet_inputs.fxml"));
			root = loader.load();
			ScrollPane pn=new ScrollPane(root);
			Projet_inputs controller = loader.getController();
			controller.chargerDonnées(Controller_Projet_scene.champsEtvaleursProjet);
	        Scene scene = new Scene(pn);
    	    scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
    	    stagesecondaire.setScene(scene);
    	    stagesecondaire.show();
    	   
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			Parent root = FXMLLoader.load(getClass().getResource("Affichage_Projets.fxml"));
			Parent_Pan.getChildren().clear();
			Parent_Pan.getChildren().setAll(root);
		}
	}
	
	public void retourner_scene_projets(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Affichage_Projets.fxml"));
		Parent_Pan.getChildren().clear();
		Parent_Pan.getChildren().setAll(root);
	}
}

