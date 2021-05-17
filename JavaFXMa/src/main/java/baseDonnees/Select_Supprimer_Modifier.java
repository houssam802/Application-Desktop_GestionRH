package baseDonnees;

 
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;
import java.util.ArrayList;

import fx_Evaluation.Personnel_Evaluation;
import fx_Projet.Projet;
import fx_Recrutement.Departement;
import fx_Recrutement.Personnel;
import fx_Task.Task;

public class Select_Supprimer_Modifier extends Connect{  
     
	//Retourne liste des employés (List_personnes) existant dans la base de données .
	public ArrayList<Personnel> selectAll(){  
        String sql = "SELECT * FROM personnel";  
        try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            ArrayList<Personnel> List_personnes=new ArrayList<Personnel>(); 
            //Dans chaque itération , instancier un objet du classe "Personnel" et à l'aide de "rs" définir ses attributs.
            while (rs.next()) { 
            	Personnel prsn =new Personnel();
            	//Attributs du classe Personnel.
            	Field[] fields = prsn.getClass().getDeclaredFields();
            	//Méthodes du classe Personnel.
            	Method[] methodes = prsn.getClass().getMethods();
            	for(Field field : fields) {
            		for(Method methode : methodes) {
            			//Pour définir les attributs on s'intéresse uniquement au setters.
            			if(methode.getName().matches("set.+")) {
    						try {
    							//exemple ( field : Nom | method : setNom )
            				 if(methode.getName().toLowerCase().matches("set"+field.getName().toLowerCase())) {
            					if(!field.getName().matches("nombre_Enfant|iD_Departement|iD_Cv|salaire")) {
            						if(rs.getString(field.getName())!=null){
            							methode.invoke(prsn, rs.getString(field.getName()));
            						}else methode.invoke(prsn,"Indéterminé");
            		        	}
            		        	if(field.getName().matches("salaire")) {
            		        		methode.invoke(prsn, rs.getFloat(field.getName()));
            		        	}
            		        	if(field.getName().matches("nombre_Enfant|iD_Departement|iD_Cv")) {
            		        		methode.invoke(prsn, rs.getInt(field.getName()));
            		        	}
            			   	  }
	            			} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
            			}
            		}
            	}
                List_personnes.add(prsn);
            }  
            stmt.close();
            rs.close();
            return List_personnes;
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return null;
    } 
	
	public String selectPersonnelFromId(int id) {// retourne le nom complet de l'employe
		String sql = "SELECT Nom,Prenom FROM personnel WHERE iD_Personnel=" + id;
		String nom_Personnel = "";
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			nom_Personnel = rs.getString("Nom") + "_" + rs.getString("Prenom");
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return nom_Personnel;
	}
	
	
	public int selectIdFromPersonnel(String NumeroCin){
    	String sql = "SELECT iD_Personnel FROM personnel WHERE Numero_CIN='"+NumeroCin+"'";  
    	int id=0;
    	try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            id=rs.getInt("iD_Personnel"); 
            stmt.close();
            rs.close();
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    	return id;
    }
	
	//Cette fonction est utiliser lors du recrutement (classe "RecrutementController"), 
	//à partir du nom de département , elle retourne "id" associé au département sélectionner.
    public int selectIdFromDepartement(String departement){
    	String sql = "SELECT iD_Departement FROM département WHERE nom_Departement='"+departement+"'";  
    	int id=0;
    	try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            id=rs.getInt("iD_Departement"); 
            stmt.close();
            rs.close();
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    	return id;
    }
    
    
    public ArrayList<Departement> selectAlllDepartement(){
    	String sql = "SELECT * FROM département";
    	try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql); 
            ArrayList<Departement> List_dept=new ArrayList<Departement>();
            // loop through the result set  
            while (rs.next()) {  
            	Departement dept =new Departement();
            	dept.setId_Departement(rs.getInt("iD_Departement"));
            	dept.setNom_Departement(rs.getString("nom_Departement"));
                List_dept.add(dept);
            }
            stmt.close();
            rs.close();
            return List_dept;
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    	return null;
    }

	//selectDepartementFromId : retourne , d'après id de département , le nom du département.
    public String selectDepartementFromId(int id){
    	String sql = "SELECT nom_Departement FROM département WHERE iD_Departement="+id;  
    	String nomDepartement="";
    	try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            nomDepartement=rs.getString("nom_Departement"); 
            stmt.close();
            rs.close();
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    	return nomDepartement;
    }
    
  	//Selon le numéro CIN passé en argument , cette fonction select les informations sur l'évaluation du personnel, 
  	//instancier un objet de type "Personnel_Evaluation" et définir ses attributs . (retourne l'objet créer) 
    public Personnel_Evaluation selectEvaluation(String CIN){  
    	String sql = "SELECT * FROM personnel Where Numero_CIN=\""+CIN+"\"";    
        try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            Personnel_Evaluation personneEvaluation=new Personnel_Evaluation();
            while (rs.next()) { 
            	personneEvaluation.setEvaluation(rs.getString("Evaluation"));
            	personneEvaluation.setDate_Evaluation(rs.getString("Date_Evaluation"));
            	personneEvaluation.setDate_Prochaine_Evaluation(rs.getString("Date_Prochaine_Evaluation"));
            }
            stmt.close();
            rs.close();
            return personneEvaluation;
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return null;
    }
   
    /////////////////////////////////////////////////////////////////////
    public int selectIdDepartementFromidProjet(int id) {
		String sql = "SELECT iD_Departement FROM Projet WHERE iD_Projet=" + id;
		int idd = 0;
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			idd = rs.getInt("iD_Departement");
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return idd;
	}

	public ArrayList<String> selectAllPersonnel(int id) {
		int idd = selectIdDepartementFromidProjet(id);
		String sql = "SELECT Nom,Prenom FROM personnel WHERE iD_Departement=" + idd;// a verifier
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			String resultat = "";
			ArrayList<String> List_personnel = new ArrayList<String>();
			while (rs.next()) {
				resultat = rs.getString("Nom") + "_" + rs.getString("Prenom");
				List_personnel.add(resultat);
			}
			return List_personnel;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
    
    
    public ArrayList<Projet> selectAllPrj() {
		String sql = "SELECT * FROM Projet";// a verifier
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Projet> List_Projet = new ArrayList<Projet>();
			while (rs.next()) {
				Projet prj = new Projet();
				Field[] fields = prj.getClass().getDeclaredFields();
				Method[] methodes = prj.getClass().getMethods();
				for (Field field : fields) {
					for (Method methode : methodes) {
						if (methode.getName().matches("set.+")) {
							try {
								if (methode.getName().toLowerCase().matches("set" + field.getName().toLowerCase())) {
									if (!field.getName().matches("iD_Projet|iD_Departement")) {
										if(rs.getString(field.getName())!=null){
	            							methode.invoke(prj, rs.getString(field.getName()));
	            						}else methode.invoke(prj, "Non Décrit");
									}

									if (field.getName().matches("iD_Projet|iD_Departement")) {
										methode.invoke(prj, rs.getInt(field.getName()));
									}
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
				List_Projet.add(prj);
			}
			return List_Projet;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public ArrayList<Task> selectAllProjet_Tasks(int id_Projet) {
		String sql = "SELECT * FROM Task where iD_Projet="+id_Projet+";";// a verifier
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<Task> List_Task = new ArrayList<Task>();
			while (rs.next()) {
				Task tsk = new Task();
				Field[] fields = tsk.getClass().getDeclaredFields();
				Method[] methodes = tsk.getClass().getMethods();
				for (Field field : fields) {
					for (Method methode : methodes) {
						if (methode.getName().matches("set.+")) {
							try {
								if (methode.getName().toLowerCase().matches("set" + field.getName().toLowerCase())) {
									if (!field.getName().matches("iD_Projet|iD_Task|iD_Personnel")) {
										if(rs.getString(field.getName())!=null){
	            							methode.invoke(tsk, rs.getString(field.getName()));
	            						}else methode.invoke(tsk, "Non Décrit");
									}

									if (field.getName().matches("iD_Projet|iD_Task|iD_Personnel")) {
										methode.invoke(tsk, rs.getInt(field.getName()));
									}
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
				List_Task.add(tsk);
			}
			return List_Task;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public int selectIdFromPersonnel(String nom, String prenom) {
		String sql = "SELECT iD_Personnel FROM personnel WHERE Nom='" + nom + "'AND Prenom='" + prenom + "'";
		int id = 0;
		try {
			Connection conn = connecter();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			id = rs.getInt("iD_Personnel");
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return id;
	}


	
	////////////////////////////////////UPDATE--------------------------------
	
	public void updateProjet(int id, String champs, String valeur) throws SQLException {
		String[] champsTab = champs.split(", ");
		String[] valeurTab = valeur.split(", ");
		String sql = "UPDATE projet SET ";
		for(int i=0; i<champsTab.length; i++) {
 			if(i!=champsTab.length-1) {
 				sql+=champsTab[i]+"=\""+valeurTab[i]+"\",";
 			}else sql+=champsTab[i]+"=\""+valeurTab[i]+"\" ";
 		}
		sql += "WHERE iD_Projet=\"" + id + "\";";
		Connection conn = connecter();
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		stmt.close();
	}

	public void updateTask(int id, String champs, String valeur) throws SQLException {
		String[] champsTab = champs.split(", ");
		String[] valeurTab = valeur.split(", ");
		String sql = "UPDATE task SET ";
		for(int i=0; i<champsTab.length; i++) {
 			if(i!=champsTab.length-1) {
 				sql+=champsTab[i]+"=\""+valeurTab[i]+"\",";
 			}else sql+=champsTab[i]+"=\""+valeurTab[i]+"\" ";
 		}
		sql += "WHERE iD_Task=\"" + id + "\"";
		Connection conn = connecter();
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		stmt.close();
	}
	
	
	
    //Mettre à jour , dans la base de données (table : Personnel), les données du personnel 
    //associé au numéro de CIN passé dans l'argument .
    public void update(String numeroCIN,String champs,String valeur) throws SQLException{ 
    	String[] champsTab = champs.split(", ");
        String[] valeurTab = valeur.split(", ");
    	String sql = "UPDATE personnel SET ";
 		for(int i=0; i<champsTab.length; i++) {
 			if(i!=champsTab.length-1) {
 				sql+=champsTab[i]+"=\""+valeurTab[i]+"\",";
 			}else sql+=champsTab[i]+"=\""+valeurTab[i]+"\" ";
 		}
    	sql+= "Where Numero_CIN='"+numeroCIN+"'";
        Connection conn = connecter();  
        Statement stmt  = conn.createStatement(); 
        stmt.execute(sql);
        stmt.close();
    }
    
    
////////////////////////////////////DELETE--------------------------------
    
    public void delete_prs(String numeroCIN){ 
    	String sql="SELECT iD_Personnel FROM personnel Where Numero_CIN=\""+numeroCIN+"\"";
    	String sql1 = "DELETE FROM personnel Where Numero_CIN=\""+numeroCIN+"\"";  	
    	String sql2 = "UPDATE personnel SET iD_Personnel=iD_Personnel-1 Where iD_Personnel>=?";  
    	String sql3="ALTER TABLE personnel RENAME TO tmp";
    	String sql4="CREATE TABLE \"personnel\" (\r\n"
    			+ "	\"iD_Personnel\"	INTEGER PRIMARY KEY AUTOINCREMENT,\r\n"
    			+ "	\"Nom\"	TEXT NOT NULL,\r\n"
    			+ "	\"Prenom\"	TEXT NOT NULL,\r\n"
    			+ "	\"Titre\"	TEXT NOT NULL,\r\n"
    			+ "	\"Date_naissance\"	TEXT NOT NULL,\r\n"
    			+ "	\"Numero_CIN\"	TEXT NOT NULL UNIQUE,\r\n"
    			+ "	\"Tel_Personnel\"	TEXT NOT NULL UNIQUE,\r\n"
    			+ "	\"Adresse_Gmail\"	TEXT NOT NULL UNIQUE,\r\n"
    			+ "	\"situation_Familiale\"	TEXT NOT NULL,\r\n"
    			+ "	\"nombre_Enfant\"	INTEGER NOT NULL,\r\n"
    			+ "	\"Date_Embauche\"	TEXT NOT NULL,\r\n"
    			+ "	\"type_Contract\"	TEXT NOT NULL,\r\n"
    			+ "	\"Date_Fin_Contract\"	TEXT ,\r\n"
    			+ "	\"salaire\"	REAL NOT NULL,\r\n"
    			+ "	\"diplome\"	TEXT,\r\n"
    			+ "	\"iD_Departement\"	INTEGER NOT NULL,\r\n"
    			+ "	\"iD_Cv\"	INTEGER,\r\n"
    			+ "	\"Evaluation\"	TEXT,\r\n"
    			+ "	\"Date_Evaluation\"	TEXT,\r\n"
    			+ "	\"Sexe\"	TEXT NOT NULL,\r\n"
    			+ "	\"Date_Prochaine_Evaluation\"	TEXT,\r\n"
    			+ "	FOREIGN KEY(\"iD_Departement\") REFERENCES \"département\"(\"iD_Departement\")\r\n"
    			+ ");";
    	
    	String sql5="INSERT INTO personnel SELECT * FROM tmp";
    	String sql6="DROP TABLE tmp";
        try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement(); 
            ResultSet rs    = stmt.executeQuery(sql); 
            PreparedStatement pstmt = conn.prepareStatement(sql2); 
            
            int l=rs.getInt("iD_Personnel");
            stmt.execute(sql1);
            pstmt.setInt(1,l);  
            pstmt.executeUpdate();
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5); 
            stmt.execute(sql6); 
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }
    
    public void delete_Task(int id_Task){ 
    	String sql1 = "DELETE FROM Task Where iD_Task="+id_Task+";";  	
    	String sql2 = "UPDATE Task SET iD_Task=iD_Task-1 Where iD_Task>=?";  
    	String sql3="ALTER TABLE Task RENAME TO tmp";
    	String sql4="CREATE TABLE \"Task\" (\r\n"
    			+ "	\"iD_Task\"	INTEGER,\r\n"
    			+ "	\"nom_Task\"	TEXT NOT NULL,\r\n"
    			+ "	\"statut_Task\"	TEXT NOT NULL,\r\n"
    			+ "	\"description_Task\"	TEXT NOT NULL,\r\n"
    			+ "	\"date_Debut_Task\"	TEXT NOT NULL,\r\n"
    			+ "	\"date_Fin_Task\"	TEXT NOT NULL,\r\n"
    			+ "	\"iD_Projet\"	INTEGER NOT NULL,\r\n"
    			+ "	\"iD_personnel\"	INTEGER NOT NULL,\r\n"
    			+ "	FOREIGN KEY(\"iD_Projet\") REFERENCES \"Projet\"(\"iD_Projet\"),\r\n"
    			+ "	FOREIGN KEY(\"iD_personnel\") REFERENCES \"personnel\"(\"iD_Personnel\"),\r\n"
    			+ "	PRIMARY KEY(\"iD_Task\" AUTOINCREMENT)\r\n"
    			+ ");";
    	
    	String sql5="INSERT INTO Task SELECT * FROM tmp";
    	String sql6="DROP TABLE tmp";
        try {  
            Connection conn = connecter();  
            Statement stmt  = conn.createStatement(); 
            PreparedStatement pstmt = conn.prepareStatement(sql2); 
            stmt.execute(sql1);
            pstmt.setInt(1,id_Task);  
            pstmt.executeUpdate();
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5); 
            stmt.execute(sql6); 
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }

}  
