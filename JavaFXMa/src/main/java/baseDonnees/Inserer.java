package baseDonnees;

import java.sql.Connection;   
import java.sql.PreparedStatement;  
import java.sql.SQLException;
import java.util.ArrayList;
   
public class Inserer extends Connect {  
   
	//champs = "Nom, Prenom, Adresse_Gmail..."
	//valeur = "x, y, z..."
	public void insert(String champs,String valeur) throws SQLException {
		///Cette partie pour créer un string = "?, ?; ?..."
		///tel que le nombre des points d'interrogation égale au nombre des champs.
		///Pour l'utiliser dans la syntaxe "INSERT" du SQL. 
		//Tableau de champs et valeur .
        String[] champsTab = champs.split(", ");
        String[] valeurTab = valeur.split(", ");
		ArrayList<String> valueQST = new ArrayList<String>();
		for(int i=0; i<(champs.split(", ")).length; i++) {
			valueQST.add("?");
		}
		//valueQSTStr = "?, ?, ?..."
		String valueQSTStr = valueQST.toString().replaceAll("\\[|\\]", "");
		/////////////////////////////////////////////////////////////////////
        String sql = "INSERT INTO personnel("+champs+") VALUES("+valueQSTStr+")"; 
        Connection conn = connecter(); 
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int j=0;
        for(int i=0 ; i<champsTab.length;i++) {
        	j=i+1;
        	if(!champsTab[i].matches("nombre_Enfant|iD_Departement|iD_Cv|salaire")) {
            	if(champsTab.length>valeurTab.length) {
            		if(champsTab[i].matches("Date_Fin_Contract")){
                		pstmt.setString(j,"");	
            		}else pstmt.setString(j,valeurTab[i]);
            	}else pstmt.setString(j,valeurTab[i]);
        	}
        	if(champsTab[i].matches("salaire")) {
        		pstmt.setFloat(j,Float.parseFloat(valeurTab[i]));
        	}
        	if(champsTab[i].matches("nombre_Enfant|iD_Departement|iD_Cv")) {
        		pstmt.setInt(j,Integer.parseInt(valeurTab[i]));
        	}
        }
        pstmt.executeUpdate();
		conn.close();
    }  
	
	public void insertTask(String champs, String valeur) throws SQLException {
		String[] champsTab = champs.split(", ");
		String[] valeurTab = valeur.split(", ");
		ArrayList<String> valueQST = new ArrayList<String>();
		for (int i = 0; i < (champs.split(", ")).length; i++) {
			valueQST.add("?");
		}
		String valueQSTStr = valueQST.toString().replaceAll("\\[|\\]", "");
		String sql = "INSERT INTO task(" + champs + ") VALUES(" + valueQSTStr + ")";
		Connection conn = connecter();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int j = 0;
		for (int i = 0; i < champsTab.length; i++) {
			j = i + 1;
			if (!champsTab[i].matches("iD_personnel|iD_Projet|iD_Task")) {
				pstmt.setString(j, valeurTab[i]);
			}
			if (champsTab[i].matches("iD_personnel|iD_Projet|iD_Task")) {
				pstmt.setInt(j, Integer.parseInt(valeurTab[i]));
			}
		}
		pstmt.executeUpdate();
	}

	public void insertProjet(String champs, String valeur) throws SQLException {
		String[] champsTab = champs.split(", ");
		String[] valeurTab = valeur.split(", ");
		ArrayList<String> valueQST = new ArrayList<String>();
		for (int i = 0; i < (champs.split(", ")).length; i++) {
			valueQST.add("?");
		}
		String valueQSTStr = valueQST.toString().replaceAll("\\[|\\]", "");
		String sql = "INSERT INTO projet(" + champs + ") VALUES(" + valueQSTStr + ")";
		Connection conn = connecter();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int j = 0;
		for (int i = 0; i < champsTab.length; i++) {
			j = i + 1;
			if (!champsTab[i].matches("iD_Projet")) {
				pstmt.setString(j, valeurTab[i]);
			}
			if (champsTab[i].matches("iD_Projet")) {
				pstmt.setInt(j, Integer.parseInt(valeurTab[i]));
			}
		}
		pstmt.executeUpdate();
	}

	public void fermer() {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("Connection fermé .");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}  
