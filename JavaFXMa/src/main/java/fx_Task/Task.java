package fx_Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Task {
	private int iD_Task;

	@Pattern(regexp = "^[a-z( )?A-ZÜ-ü.-_]+$", message = "Nom invalid !")
	@NotBlank(message = "Champ vide !")
	private String nom_Task;

	private String statut_Task;
	private String description_Task;

	@Pattern(regexp = "^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/((20)|(19))[0-9]{2}$", message = "Format invalid !")
	@NotBlank(message = "Champ vide !")
	private String date_Debut_Task;

	@Pattern(regexp = "^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/((20)|(19))[0-9]{2}$", message = "Format invalid !")
	@NotBlank(message = "Champ vide !")
	private String date_Fin_Task;

	private int iD_Projet;

	@NotNull(message = "une persone soit etre specifiee !")
	private int iD_Personnel;

	@PastOrPresent(message = "Date invalid !")
	@NotNull(message = "Date invalid !")
	public Date getCheckDate_Debut() {
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date jour;
		try {
			jour = simpleDateFormat.parse(date_Debut_Task);
			return jour;
		} catch (ParseException e) {
		}
		return null;
	}

	@FutureOrPresent(message = "Date invalid !")
	@NotNull(message = "Date invalid !")
	public Date getCheckDate_Fin() {// verifier que date fin > date debut
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date jour;
		try {
			jour = simpleDateFormat.parse(date_Fin_Task);
			if (!jour.after(getCheckDate_Debut())) {
				return null;
			}
			return jour;
		} catch (ParseException e) {
		}
		return null;
	}
}
