package fx_Affichage;

import fx_Recrutement.Personnel;
import fx_Task.Task;
import lombok.Data;

public @Data class Affichage_Tasks_Class {
	
	public Affichage_Tasks_Class(Task elm_tsk,Personnel elm_prs) {
	  tsk=new Task();
	  tsk=elm_tsk;
	  prs=new Personnel();
	  prs=elm_prs;
	}

	public Affichage_Tasks_Class() {
		// TODO Auto-generated constructor stub
	}
   private Task tsk;
   private Personnel prs;
}
