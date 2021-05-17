package fx_Accueil;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
	
	//Définir deux variables afin de manipuler les coordonnées de la scène par rapport à  l'écran .
	private static double xcord = 0;
    private static double ycord = 0;
    
	   @Override
	    public void start(Stage primaryStage) {
	        try {
	            Parent root = FXMLLoader.load(getClass().getResource("/fx_Login/login.fxml"));
	            primaryStage.setTitle("My Application");
	            primaryStage.setMaxHeight(600);
	            primaryStage.setMaxWidth(1000);
	            Scene scene=new Scene(root);
	            
	          //Définir un style de scène avec un fond blanc uni et sans décorations .
	            primaryStage.initStyle(StageStyle.UNDECORATED);
	            primaryStage.setScene(scene);
	            
	            //Appliquer un style CSS
	            scene.getStylesheets().add(getClass().getResource("/FXpack/application.css").toExternalForm());
	            
	            //Rendre la scène déplaçable.
	            drag(root,primaryStage);
	            primaryStage.show(); 
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	 //Rendre la scène déplaçable .
	   public static void drag(Parent root,Stage stage) {
		 //Lorsque la souris est enfoncée,les coordonnées définies ultèrieurement prennent les coordonnées de la scène .
	        root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xcord = event.getSceneX();
                    ycord = event.getSceneY();
                }
            });
            
	      //lorsque la souris est déplacée, la scène est définie avec les nouveaux coordonnées .
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                	 stage.setX(event.getScreenX() - xcord);
                	 stage.setY(event.getScreenY() - ycord);
                }
            });
	   }
	    public static void main(String[] args) {
	        launch(args);
	    }
	    
	}
