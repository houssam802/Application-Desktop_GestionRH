package fx_Affichage;

import fx_Recrutement.Departement;
import fx_Recrutement.Personnel;
import lombok.Data;

public @Data class Affichage_Personnels_Class {
    public Affichage_Personnels_Class(Departement elm_dept, Personnel elm_prs) {
		pers=new Personnel();
		pers=elm_prs;
		dept=new Departement();
		dept=elm_dept;
	}
	public Affichage_Personnels_Class() {
		// TODO Auto-generated constructor stub
	}
	private Personnel pers;
    private Departement dept;
}
