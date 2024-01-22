package se.sundsvall.citizenchanges.util;

import lombok.Getter;

@Getter
public enum MessageConstants {

	HEADLINE_SKOLSKJUTS("Skolskjuts"),
	LEAD_TEXT_SKOLSKJUTS("Avstämning av adress för vårdnadshavare med beviljad skolskjuts<br><br>VH = vårdnadshavare som ansökt<br>VH2 = annan vårdnadshavare"),
	EMAIL_SUBJECT_SKOLSKJUTS("Rapport för skolskjuts"),
	HEADLINE_ELEVRESA("Elevresa"),
	LEAD_TEXT_ELEVRESA("Avstämning av adress för elev med beviljad elevresa"),
	EMAIL_SUBJECT_ELEVRESA("Rapport för elevresor");

	private final String text;

	MessageConstants(final String text) {
		this.text = text;
	}

}
