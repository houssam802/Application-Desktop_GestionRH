package fx_Recrutement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data

public class Personnel {
	@Pattern(regexp="^[a-z( )?A-ZÜ-ü]+$",message="Nom invalid !")
	@NotBlank(message="Champ vide !")
    private String Nom; 
	
	@Pattern(regexp="^[a-z( )?A-ZÜ-ü]+$",message="Prénom invalid ! ")
	@NotBlank(message="Champ vide !")
    private String Prenom; 
	
	@Pattern(regexp="^[a-z( )?A-ZÜ-ü]+$",message="Fonction invalid !")@NotNull(message="Champ vide !")
    private String Titre; 
	
	@Pattern(regexp="^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/((20)|(19))[0-9]{2}$",message="Format invalid !")
	@NotBlank(message="Champ vide !")
    private String Date_naissance;
	
	@Pattern(regexp="^[A-Z]{1,2}[0-9]{6}$",message="Format invalid !")
	@NotBlank(message="Champ vide !")
    private String Numero_CIN; 
	
	@Pattern(regexp="^((\\+\\d{1,3}( )?)|0)([6-7])\\d{2}[- ]?(\\d{6})$",message="Format invalid !")@NotNull(message="Champ vide !")
    private String Tel_Personnel; 
	
	@Email(message="Email invalid !")@NotBlank(message="Champ vide !")
	private String Adresse_Gmail; 
	
	@NotNull(message="Champ vide !")
    private String situation_Familiale;
	
	@Min(value=0,message="Valeur doit être positive !")@NotNull(message="Champ vide !")
    private int nombre_Enfant;
	
    @Pattern(regexp="^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/(20)[0-9]{2}$",message="Format invalid !")@NotBlank(message="Champ vide !")
    private String Date_Embauche;
	
	@NotNull(message="Champ vide !")
    private String type_Contract;
	
	@Pattern(regexp="^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/(20)[0-9]{2}$",message="Format invalid !")
    private String Date_Fin_Contract;
	
	@Positive(message="Valeur doit être positive !")@NotNull(message="Champ vide !")
    private float salaire;
	
	@Pattern(regexp="^[a-zA-ZÜ-ü]*$",message="Invalid !")@NotNull(message="Champ vide !")
    private String diplome="jh";
	
	@Positive(message="15")
    private int iD_Departement;
	
	@Positive(message="16")
    private int iD_Cv=1;
	
	@NotNull(message="Saisir une option !")
	private String Sexe;
	
	@Past(message="Date invalid !")
	@NotNull(message="Champ vide !")
	public Date getCheckDate_naissance(){
    	DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date jour;
		try {
			jour = simpleDateFormat.parse(Date_naissance);
			return jour;
		} catch (ParseException e) {
		}
    	return null;
	}
	
	@PastOrPresent(message="Date invalid !")
	@NotNull(message="Champ vide !")
	public Date getCheckDate_Embauche(){
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date jour;
		try {
			jour = simpleDateFormat.parse(Date_Embauche);
			return jour;
		} catch (ParseException e) {
		}
    	return null;
	}
	
	@Future(message="Date invalid !")
	public Date getCheckDate_Fin_Contract(){
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date jour;
		try {
			jour = simpleDateFormat.parse(Date_Fin_Contract);
			return jour;
		} catch (ParseException e) {
		}
    	return null;
	}
}
