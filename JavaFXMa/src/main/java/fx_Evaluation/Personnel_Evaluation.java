package fx_Evaluation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Personnel_Evaluation { 
	
	@NotBlank(message="Choisir un choix !")
    private String Evaluation;
	
	@Pattern(regexp="^([0-2][0-9]{1}||3[0-1]{1})/(0[0-9]{1}||1[0-2]{1})/(20)[0-9]{2}$",message="Format invalid !")
	@NotBlank(message="Champ vide !")
    private String Date_Evaluation;
	
	@Pattern(regexp="^([0-2][0-9]{1}||3[0-1]{1})/(0[0-9]{1}||1[0-2]{1})/(20)[0-9]{2}$",message="Format invalid !")
	@NotBlank(message="Champ vide !")
    private String Date_Prochaine_Evaluation;
	
	@PastOrPresent(message="Date doit être dépasser !")
	@NotNull(message="Champ vide !")
	public Date getCheckDate_Evaluation(){
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date jour;
		try {
			jour = simpleDateFormat.parse(Date_Evaluation);
			return jour;
		} catch (ParseException e) {
		}
    	return null;
	}
	@Future(message="Date doit être future !")
	@NotNull(message="Champ vide !")
	public Date getCheckDate_Prochaine_Evaluation(){
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Date jour;
		try {
			jour = simpleDateFormat.parse(Date_Prochaine_Evaluation);
			return jour;
		} catch (ParseException e) {
		}
    	return null;
	}
}
