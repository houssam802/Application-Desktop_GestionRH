package baseDonnees;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.BitSet;

public class Login {
	
	byte[] Ary_oct;
	BitSet mon_bs;
	
	//bloc initial m0 .
	static final String m0="park";
	static final String clef="soad";
	
	//Constructeur
    public Login(String var){
		 byte[] oct = new byte[32];
		 Arrays.fill(oct, (byte) 0);
		 mon_bs=new BitSet();
		 
		 Ary_oct=new byte[32];
	     Ary_oct=var.getBytes();
		 for (int i = 0; i<Ary_oct.length ;i++) {
			 oct[31-i]=Ary_oct[Ary_oct.length-i-1];
		 }
		 mon_bs=convert_bytetobitSet(oct);
    } 
    
    public Login(){
    	Ary_oct=new byte[32];
    	mon_bs=new BitSet();
   } 
    
	 public static byte[] inverser_bytes(byte[] val){
		 byte tmp;
		 for (int i = 0; i < val.length/2;i++) {
	         tmp=val[val.length-i-1];
	         val[val.length-i-1]=val[i];
	         val[i]=tmp;
	     }
		 return val;
	 }
	 
	 
	public BitSet convert_bytetobitSet(byte[] val){
	    BitSet bits = new BitSet();
		for (int i = 0; i < val.length*8 ;i++) {
		   if ((val[val.length - i / 8 - 1] & (1 << (i % 8))) > 0){
	             bits.set(i);
           }
		 }
	    return bits;
	 }
	
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////Opérations sur l'ensemble du mot
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String XOR(String var,String var1){
		  Login tmp=new Login(var);
		  Login tmp1=new Login(var1);
		  BitSet bs1 = new BitSet();
		  for(int i=256;i>=0;i--) {
			  bs1.set(i,Xor_bit(tmp.mon_bs.get(i),tmp1.mon_bs.get(i)));
		  }
		  byte[] tab_oct=new byte[32];
		  tab_oct=inverser_bytes(bs1.toByteArray());
		  String s = supprime_espaces(tab_oct);
		  return s;
	 }
	
	 public static String Addition(String var,String var1){
		  Login tmp=new Login(var);
		  Login tmp1=new Login(var1);
		  BitSet bs1 = new BitSet();
		  boolean retenue=false;
		  for(int i=0;i<32;i++){
			  bs1.set(i,addition_Bit(addition_Bit(tmp.mon_bs.get(i),tmp1.mon_bs.get(i)),retenue));
			  retenue=return_addition(tmp.mon_bs.get(i),tmp1.mon_bs.get(i),retenue);
		  }
		  byte[] tab_oct=new byte[32];
		  tab_oct=inverser_bytes(bs1.toByteArray());
		  String s = decimal_positive(tab_oct);
		  return s;
	 }
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	 
	 //Application de chiffrement CBC
		public String[] Crypter(String var) {
			String[] bloc_mots= decouper_mot(var);
			String[] bloc_mot_crypter=new String[bloc_mots.length+1];
			String[] tmp=new String[bloc_mots.length];
			for(int i=0;i<bloc_mots.length+1;i++) {
				if(i==0) {
					bloc_mot_crypter[i]=Addition(clef,m0);
				}else {
					bloc_mot_crypter[i]=Addition(clef,XOR(bloc_mots[i-1],bloc_mot_crypter[i-1]));
					tmp[i-1]=bloc_mot_crypter[i];
				}
				
			}
			return tmp;	
		}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//découpe un mot en ensemble des mots de taille 4 .
	public static String[] decouper_mot(String var) {
		String ll="";
		for(int i=0;i<var.length();i++) {
			ll+=var.substring(i,i+1);
			if((i+1)%4==0) ll+=" ";
		}
		String[] tab_mot=ll.split(" ");
		return tab_mot;
	}	
	
	//Regroupe les éléments du tableau de type string en un seul mot . 
	public static String getMot(String[] var) {
		String tmp="";
		for(int i=0;i<var.length;i++) {
			tmp+=var[i];
		}
		return tmp;
	}
	
	//Rendre les octets positives ,puisque on ne peut pas représenter les valeurs négatives par des caractères .
	public static String decimal_positive(byte[] tab_oct) {
		for(int i=0;i<tab_oct.length;i++) {
			if((tab_oct[i]+128)<77) tab_oct[i]=(byte) ((tab_oct[i]+128)%128 +50);
			else tab_oct[i]=(byte) ((tab_oct[i]+128)%128);
		}
		String tmp=new String(tab_oct);
		return tmp;
	}
	
	//Supprime les 26 premiers octets (code ASCII), qui représentent l'espace,tabulation,..
	//Afin d'obtenir des caractères alphanumériques .
	public static String supprime_espaces(byte[] tab_oct) {
		for(int i=0;i<tab_oct.length;i++) {
			if((tab_oct[i]+50)<128) tab_oct[i]=(byte) (tab_oct[i]+50);
		}
		String tmp=new String(tab_oct);
		return tmp;
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////
	///////Opérations sur un bit
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	 public static boolean addition_Bit(boolean a,boolean b){
		 if(a ^ b) return true;
		 else return false;
	 }
	 
	 public static boolean return_addition(boolean a,boolean b,boolean c){
		 if((a & c) | (b & c) | (a & b)) return true;
		 else return false;
	 }
	
	 public static boolean Xor_bit(boolean a,boolean b) {
		 if(a==b) return false;
		 else return true;
	 }
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	 //////Tester la saisission
	 ////////////////////////////////////////////////////////////////////////////////////////////////
	 
	 
	//Vérifier si le nom existe dans la table login .
	 public boolean tester_saisission_Nom(String nom) {
		String sql = "SELECT * FROM Login WHERE Nom=\""+nom+"\""; 
	      try {  
	          Connection conn = Connect.connecter();  
	          Statement stmt  = conn.createStatement();  
	          ResultSet rs    = stmt.executeQuery(sql); 
	          if (!rs.next()) {
		    	  stmt.close();
		    	  conn.close();
	     		  return false;
		      }else {
		    	  stmt.close();
		    	  conn.close();
		    	  return true;
		      }
	      } catch (SQLException e) {  
	          System.out.println(e.getMessage());  
	      } 
       return false;
	 }
	 
	 
	 //Vérifier si le mot de passe est bien celui associé au utilisateur .
	 public boolean tester_saisission_Pdm(String nom,String pdm) {
		 String tmp=getMot(Crypter(pdm));
		 String sql = "SELECT * FROM Login WHERE Password=\""+tmp+"\" and Nom=\""+nom+"\""; 
	        try {  
	            Connection conn = Connect.connecter(); 
	            Statement stmt  = conn.createStatement();  
	            ResultSet rs    = stmt.executeQuery(sql);  
	            if (!rs.next()) {
			    	  stmt.close();
			    	  conn.close();
					  return false;
				}else {
			    	stmt.close();
			    	conn.close();
					return true;
				}
		     } catch (SQLException e) {  
		        System.out.println(e.getMessage());  
		     } 
		  return false;
	 }
	 
        //remplissage du tableau de login
	    public void insert(String nom,String mdp) {  
	        String sql = "INSERT INTO Login(Nom,Password) VALUES(?,?)";  
	    
	        try{  
	            Connection conn = Connect.connecter();  
	            PreparedStatement pstmt = conn.prepareStatement(sql);  
	            pstmt.setString(2,mdp);  
	            pstmt.setString(1,nom);  
	            pstmt.executeUpdate();  
	        } catch (SQLException e) {  
	            System.out.println(e.getMessage());  
	        }  
	    }
	    
	///housni : tester ////houssam : login  ///ayman : hipmaze   ///dauha : ELAMRANI  ///nejeoui : responsable
	
/*	public static void main(String[] args) {
		Login l=new Login();
		l.insert("nejeoui",getMot(l.Crypter("responsable")));
		
	}*/
}
