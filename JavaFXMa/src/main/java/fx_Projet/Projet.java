package fx_Projet;

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
public class Projet {
	private int iD_Projet;

	@Pattern(regexp = "^[a-z( )?A-ZÜ-ü.-_]+$", message = "Nom invalid !")
	@NotBlank(message = "Champ vide !")
	private String nom_Projet;

	@Pattern(regexp = "^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/((20)|(19))[0-9]{2}$", message = "Format invalid !")
	@NotBlank(message = "Champ vide !")
	private String date_Debut;

	@Pattern(regexp = "^([0-2][0-9]{1}|3[0-1]{1})/(0[0-9]{1}|1[0-2]{1})/((20)|(19))[0-9]{2}$", message = "Format invalid !")
	@NotBlank(message = "Champ vide !")
	private String date_Fin;

	private String description_Projet;
	private String statut_Projet;
	private int iD_Departement;

	@PastOrPresent(message = "Date invalid !")
	@NotNull(message = "Date invalid !")
	public Date getCheckDate_Debut() {
		DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date jour;
		try {
			jour = simpleDateFormat.parse(date_Debut);
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
			jour = simpleDateFormat.parse(date_Fin);
			if (!jour.after(getCheckDate_Debut())) {
				return null;
			}
			return jour;
		} catch (ParseException e) {
		}
		return null;
	}
}
