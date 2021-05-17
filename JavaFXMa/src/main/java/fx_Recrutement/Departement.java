package fx_Recrutement;

import lombok.Data;

public @Data class Departement {
    
	public Departement(int i, String var) {
		Id_Departement=i;
		Nom_Departement=var;
	}
	public Departement() {
		// TODO Auto-generated constructor stub
	}
	private Integer Id_Departement;
	private String Nom_Departement;
}
