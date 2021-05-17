package fx_Affichage;

import fx_Projet.Projet;
import fx_Recrutement.Departement;
import lombok.Data;

public @Data class Affichage_Projets_Class {
	public Affichage_Projets_Class(Projet elm_prj,Departement elm_dept) {
		prj = new Projet();
		prj = elm_prj;
		dept=new Departement();
		dept=elm_dept;
	}

	public Affichage_Projets_Class() {
		// TODO Auto-generated constructor stub
	}

	private Projet prj;
	private Departement dept;
}

