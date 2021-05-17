package baseDonnees;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
   
public class Connect {
    static Connection conn = null;  
    public static Connection connecter() { 
        try { 
            String url = "jdbc:sqlite:test1.db"; 
            conn = DriverManager.getConnection(url); 
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }
        return conn;
    }
}  
