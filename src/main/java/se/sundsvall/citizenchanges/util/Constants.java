package se.sundsvall.citizenchanges.util;

import se.sundsvall.citizenchanges.api.model.FamilyType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {

	public static final int OEP_BACKTRACK_YEARS = 3; // Number of years back to search for errands in OeP

	public static final int META_BACKTRACK_DAYS_DEFAULT = 7; // Number of days back to search for changed addresses in Metakatalogen

	public static final int META_BACKTRACK_DAYS_MAX = 30; // Maximum number of days back to search for changed addresses in Metakatalogen (This is a restriction in Metakatalogen)

	public static final int IST_BACKTRACK_DAYS_DEFAULT = 7; // Number of days back to search for changed daycare scope in EduCloud/IST

	public static final String OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED = "Automatiskt beviljat";

	public static final String OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION = "Automatiskt beviljat delegationsbeslut";

	public static final String OEP_ERRAND_STATUS_DECIDED = "beslutad";

	public static final String OEP_ERRAND_STATUS_GRANTED = "Beviljat";

	public static final String OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION = "Beviljat delegationsbeslut";

	public static final String OEP_ERRAND_STATUS_READY = "Klart";

	public static final String OEP_ERRAND_SKOLSKJUTS_DENIED = "Avslås";

	public static final String OEP_ERRAND_ELEVRESA_DENIED = "Eleven har inte rätt till elevresa";

	public static final String SUNDSVALL_COUNTY_CODE = "22";

	public static final String SUNDSVALL_MUNICIPALITY_CODE = "81";

	public static final String STATUS_UTFLYTTAD = "Utflyttad";

	public static final String STATUS_YES = "Ja";

	public static final String STATUS_NO = "Nej";

	public static final String STATUS_SENT = "Skickat";

	public static final String STATUS_NOT_SENT = "Ej skickat";

	public static final String STATUS_FAILED = "Fel";

	public static final String GUARDIAN_NOTATION = "VH";

	public static final String MINOR_NOTATION = "Elev";

	public static final String TRUSTEE_NOTATION = "FGF";

	public static final String APPLICANT_CAPACITY_GUARDIAN = "Jag ansöker för ett barn som jag är vårdnadshavare för";

	public static final String PROTECTED_IDENTITY_INDICATOR = "Skyddad";

	public static final int REMINDER_MAX_AGE = 16;

	public static final String REMINDER_DATE_LIMIT_PATTERN_SPRING = "%s-08-31"; // The current year is prepended to this string (2022-08-31 etc.) to form the date limit.

	public static final String REMINDER_DATE_LIMIT_PATTERN_AUTUMN = "%s-01-31";

	public static final String REMINDER_LAST_DAY_SPRING = "30 april";

	public static final String REMINDER_LAST_DAY_AUTUMN = "15 november";

	public static final String REMINDER_TARGET_SEMESTER_SPRING = "höstterminen";

	public static final String REMINDER_TARGET_SEMESTER_AUTUMN = "vårterminen";

	private static final List<String> PROCESSABLE_SKOLSKJUTS_STATUSES = List.of(
		OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED,
		OEP_ERRAND_STATUS_AUTOMATICALLY_GRANTED_DELEGATION_DECISION,
		OEP_ERRAND_STATUS_DECIDED,
		OEP_ERRAND_STATUS_GRANTED,
		OEP_ERRAND_STATUS_GRANTED_DELEGATION_DECISION)
		.stream()
		.map(String::toLowerCase)
		.toList();

	/*
	 * Email parameters
	 */
	public static final String EMAIL_SENDER_ADDRESS = "noreply@sundsvall.se";

	public static final String EMAIL_SENDER_NAME = "Skolresekollen";

	public static final String REMINDER_REPORT_EMAIL_SUBJECT_SPRING = "Rapport för påminnelser om skolskjuts inför HT ";

	public static final String REMINDER_REPORT_EMAIL_SUBJECT_AUTUMN = "Rapport för påminnelser om skolskjuts inför VT ";

	public static final String REMINDER_EMAIL_SUBJECT_SPRING = "Påminnelse om att ansöka om skolskjuts inför höstterminens start %s";

	public static final String REMINDER_EMAIL_SUBJECT_AUTUMN = "Påminnelse om att ansöka om skolskjuts inför vårterminens start %s";

	public static final String REMINDER_EMAIL_SENDER = "Sundsvalls kommun";

	public static final String REMINDER_SMS_SENDER = "Sundsvall";

	public static final String DAYCARE_REPORT_EMAIL_SUBJECT = "Rapport för ändrad omfattning av fritidshemsplats";

	public static final String DAYCARE_REPORT_LEAD_TEXT = "Avstämning av ändrad omfattning av fritidshemsplats";

	public static final String DAYCARE_REPORT_START_POINT = "beslutsperiodens startdatum för varje ärende";

	/*
	 * HTML Template content
	 */
	public static final String HTML_TEMPLATE_MAIN_START = """
		<!DOCTYPE html>\r
		<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:o="urn:schemas-microsoft-com:office:office">\r
			<head>\r
				<meta charset="UTF-8">\r
					<meta name="viewport" content="width=device-width,initial-scale=1">\r
						<meta name="x-apple-disable-message-reformatting">\r
							<!--[if mso]>\r
				<noscript>\r
				<xml>\r
					<o:OfficeDocumentSettings>\r
					<o:PixelsPerInch>96</o:PixelsPerInch>\r
					</o:OfficeDocumentSettings>\r
				</xml>\r
				</noscript>\r
				<![endif]-->\r
							<style>\r
			table, td, div, h1, p {font-family: Arial, sans-serif;}\r
		</style>\r
			</head>\r
			<body style="margin:0;padding:0;">\r
				<table role="presentation" style="width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;">\r
					<tr>\r
						<td align="left" style="padding:0;">\r
							<table role="presentation" style="width:border-collapse:collapse;border:1px solid #ffffff;border-spacing:0;text-align:left;">\r
								<tr>\r
									<td align="left" style="padding:0px 0px 0px 30px;background:#005595;">%%logoImage%%</td>\r
								</tr>\r
								<tr>\r
									<td style="padding:36px 30px 42px 30px;">\r
										<table role="presentation" style="width:100%;border-collapse:collapse;border:0;border-spacing:0;">\r
		""";

	public static final String HTML_TEMPLATE_MAIN_END = """
										</table>\r
									</td>\r
								</tr>\r
							</table>\r
						</td>\r
					</tr>\r
				</table>\r
			</body>\r
		</html>\r
		""";

	public static final String HTML_TEMPLATE_INTRO = """
											<tr>\r
												<td style="color:#212121;">\r
													<h1 style="font-size:16px;margin:0 0 20px 0;font-family:Arial,sans-serif;">Automatgenererad rapport för %%familyType%%</h1>\r
													<p style="margin:0 0 12px 0;font-size:12px;line-height:24px;font-family:Arial,sans-serif;">Rapporten genererad: %%reportTimestamp%%<br>\r
														Omfattar registrerade ändringar i folkbokföringen från %%metaStartDate%% och ärenden från %%oepStartDate%%<br>\r
														Antal ändringar i folkbokföringen under perioden: %%changedAddressesCount%%<br>\r
														Antal beslutade ärenden som uppfyller kriterier för granskning: %%inspectErrandsCount%%<br>\r
													</p>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_TABLE_HEAD_STUDENT_TRAVEL = """
											<tr>\r
												<td style="color:#212121;">\r
													<h2 style="font-size:14px;margin:40px 0 5px 0;font-family:Arial,sans-serif;">%%headline%%</h2>\r
													<p style="margin:0 0 12px 0;font-size:12px;font-family:Arial,sans-serif;">%%leadText%%</p>\r
												</td>\r
											</tr>\r
											<tr>\r
												<td style="padding:0;">\r
													<table role="presentation" style="width:100%;border-collapse:collapse;border:1px solid #cccccc;font-size:12px;font-family:Arial,sans-serif;color:#212121">\r
														<tr style="background-color:#D9E6EF">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ärende</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Skola/Enhet</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Program</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod start</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod slut</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Flyttdatum</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Adress (ny)</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Adress (gammal)</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Status</td>\r
		%%extraHeaders%%                                                    <td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Sekretess</td>\r
														</tr>\r
		%%rowContent%%                                            </table>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_TABLE_ROW_STUDENT_TRAVEL = """
														<tr style="background-color:#ffffff">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%link%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%schoolUnit%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%program%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionStart%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionEnd%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%moveDate%%<nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%newAddress%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%oldAddress%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%status%%</td>\r
		%%extraColumns%%                                                    <td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%classified%%</td>\r
														</tr>\r
		""";

	public static final String HTML_TEMPLATE_TABLE_HEAD = """
											<tr>\r
												<td style="color:#212121;">\r
													<h2 style="font-size:14px;margin:40px 0 5px 0;font-family:Arial,sans-serif;">%%headline%%</h2>\r
													<p style="margin:0 0 12px 0;font-size:12px;font-family:Arial,sans-serif;">%%leadText%%</p>\r
												</td>\r
											</tr>\r
											<tr>\r
												<td style="padding:0;">\r
													<table role="presentation" style="width:100%;border-collapse:collapse;border:1px solid #cccccc;font-size:12px;font-family:Arial,sans-serif;color:#212121">\r
														<tr style="background-color:#D9E6EF">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ärende</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Skola/Enhet</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod start</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod slut</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Flyttdatum</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Adress (ny)</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Adress (gammal)</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Status</td>\r
		%%extraHeaders%%                                                    <td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Sekretess</td>\r
														</tr>\r
		%%rowContent%%                                            </table>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_TABLE_ROW = """
														<tr style="background-color:#ffffff">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%link%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%schoolUnit%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionStart%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionEnd%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%moveDate%%<nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%newAddress%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%oldAddress%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%status%%</td>\r
		%%extraColumns%%                                                    <td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%classified%%</td>\r
														</tr>\r
		""";

	// Templates for daycare scope report
	public static final String HTML_TEMPLATE_DAYCARE_INTRO = """
											<tr>\r
												<td style="color:#212121;">\r
													<h1 style="font-size:16px;margin:0 0 20px 0;font-family:Arial,sans-serif;">Automatgenererad rapport för %%familyType%% och ändrad omfattning av fritidshemsplats</h1>\r
													<p style="margin:0 0 12px 0;font-size:12px;line-height:24px;font-family:Arial,sans-serif;">Rapporten genererad: %%reportTimestamp%%<br>\r
														Omfattar senast registrerade ändringar i EduCloud/IST från %%startDate%% och ärenden från %%oepStartDate%%<br>\r
														Antal beslutade ärenden som uppfyller kriterier för granskning: %%inspectErrandsCount%%<br>\r
													</p>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_DAYCARE_TABLE_HEAD = """
											<tr>\r
												<td style="color:#212121;">\r
													<h2 style="font-size:14px;margin:40px 0 5px 0;font-family:Arial,sans-serif;">%%headline%%</h2>\r
													<p style="margin:0 0 12px 0;font-size:12px;font-family:Arial,sans-serif;">%%leadText%%</p>\r
												</td>\r
											</tr>\r
											<tr>\r
												<td style="padding:0;">\r
													<table role="presentation" style="width:100%;border-collapse:collapse;border:1px solid #cccccc;font-size:12px;font-family:Arial,sans-serif;color:#212121">\r
														<tr style="background-color:#D9E6EF">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ärende</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Skola/Enhet</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod start</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod slut</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Angiven omfattning i ansökan</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ny omfattning</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ändringsstart</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Fritidshem</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Handläggare</td>\r
														</tr>\r
		%%rowContent%%                                            </table>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_DAYCARE_TABLE_ROW = """
														<tr style="background-color:#ffffff">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%link%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%schoolUnit%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionStart%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionEnd%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%oldScope%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%newScope%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%placementStart%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%daycareUnit%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%administrator%%</td>\r
														</tr>\r
		""";

	public static final String HTML_TEMPLATE_EXTRA_HEADERCOL = "                                                    <td style=\"padding:5px;vertical-align:top;border:1px solid #cccccc\">%%extraValue%%</td>\r\n";

	public static final String HTML_TEMPLATE_EXTRA_ROWCOL = "                                                    <td style=\"padding:5px;vertical-align:top;border:1px solid #cccccc\">%%extraValue%%</td>\r\n";

	public static final String HTML_TEMPLATE_LINK = "<a href=\"%%link%%\">%%flowinstanceid%%</a>";

	public static final String HTML_TEMPLATE_IMG_CUSTOM = "<img src=\"data:image/png;base64,%%imageData%%\"/>";

	public static final String HTML_TEMPLATE_IMG_DEFAULT = "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==\"/>";

	public static final String HTML_TEMPLATE_NONBREAKINGHYPHEN = "&#8209;";

	public static final String HTML_TEMPLATE_REMINDER_REPORT_INTRO = """
											<tr>\r
												<td style="color:#212121;">\r
													<h1 style="font-size:16px;margin:0 0 20px 0;font-family:Arial,sans-serif;">Automatgenererad rapport för påminnelser om att ansöka om skolskjuts</h1>\r
													<p style="margin:0 0 12px 0;font-size:12px;line-height:24px;font-family:Arial,sans-serif;">Rapporten genererad: %%reportTimestamp%%<br>\r
														Antal ärenden som uppfyller kriterier för påminnelse: %%inspectErrandsCount%%<br>\r
													</p>\r
													<p style="margin:0 0 12px 0;font-size:12px;line-height:24px;font-family:Arial,sans-serif;">Kontaktpersonen kan ansöka i egenskap av följande:<br>\r
														VH: vårdnadshavare<br>\r
														FGF: familjehem/god man/förmyndare\r
													</p>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_REMINDER_REPORT_TABLE_HEAD = """
											<tr>\r
												<td style="color:#212121;">\r
													<p style="margin:0 0 12px 0;font-size:12px;font-family:Arial,sans-serif;">Påminnelser skickade för följande ärenden:</p>\r
												</td>\r
											</tr>\r
											<tr>\r
												<td style="padding:0;">\r
													<table role="presentation" style="width:100%;border-collapse:collapse;border:1px solid #cccccc;font-size:12px;font-family:Arial,sans-serif;color:#212121">\r
														<tr style="background-color:#D9E6EF">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Ärende</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Beslutsperiod slut</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Elev</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Kontaktperson</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Epostadress</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Mobilnummer</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">SMS tillåtet</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Status epost</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">Status SMS</td>\r
														</tr>\r
		%%rowContent%%                                            </table>\r
												</td>\r
											</tr>\r
		""";

	public static final String HTML_TEMPLATE_REMINDER_REPORT_TABLE_ROW = """
											<tr style="background-color:#ffffff">\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%link%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%decisionEnd%%</nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%nameMinor%%<nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc"><nobr>%%contactName%%<nobr></td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%emailAddress%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%mobileNumber%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%smsAllowed%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%statusEmail%%</td>\r
															<td style="padding:5px;vertical-align:top;border:1px solid #cccccc">%%statusSMS%%</td>\r
														</tr>\r
		""";

	public static final String HTML_TEMPLATE_REMINDER_TEXT_BODY = """
											<tr>\r
												<td style="color:#212121;">\r
													<p style="margin:0 0 12px 0;font-size:14px;line-height:24px;font-family:Arial,sans-serif;">\r
														Hej!<br>\r
														%%minorName%% har ett beslut om skolskjuts i ärende %%errandNumber%%, som upphör att gälla %%decisionEndDate%%.<br>\r
														Du kan se beslutet i sin helhet genom att logga in i e-tjänsteportalen och läsa under Mina sidor: <a href="https://e-tjanster.sundsvall.se/">https://e-tjanster.sundsvall.se/</a><br>\r
														<br>\r
														Om behovet av skolskjuts kvarstår inför %%semester%% måste du ansöka om skolskjuts på nytt. Ansökan gör du via vår e-tjänst.<br>\r
														E-tjänsten samt mer information om skolskjuts hittar du på vår hemsida: <a href="https://sundsvall.se/utbildning-och-forskola/skolskjuts/">https://sundsvall.se/utbildning-och-forskola/skolskjuts/</a><br>\r
														För att vi ska hinna hantera ditt ärende till %%semester%%s start ska ansökan om skolskjuts vara inskickad senast den %%lastDay%%.<br>\r
														Om du redan har skickat in en ansökan om skolskjuts inför %%semester%% kan du bortse från denna påminnelse.<br>\r
														<br>\r
														Vid frågor, kontakta i första hand skoladministratör på skolan alternativt skolskjutssamordnare på <br>\r
														mejladress skolskjuts@sundsvall.se eller telefon 060-19 14 87 på vardagar mellan kl. 11-12.<br>\r
														<br>\r
														Vänliga hälsningar,<br>\r
														Skolskjutssamordnare på barn- och utbildningskontoret, Sundsvalls kommun\r
													</p>\r
												</td>\r
											</tr>\r
		""";

	public static final String REMINDER_SMS_BODY = """
		Hej!\r
		Det här är ett automatiskt genererat sms skickat från Sundsvalls kommun. Det skickas till dig eftersom ditt barn har ett beslut om skolskjuts som upphör att gälla %%decisionEndDate%%.\r
		\r
		Vi vill därför påminna dig om att ansöka om skolskjuts för ditt barn inför %%semester%%s start %%thisYear%%.\r
		Du ansöker om skolskjuts via vår e-tjänst. E-tjänsten samt mer information om skolskjuts hittar du på kommunens hemsida.\r
		För att vi ska hinna hantera ditt ärende till %%semester%%s start ska ansökan om skolskjuts vara inskickad senast den %%lastDay%%.\r
		\r
		Om du redan har skickat in en ansökan om skolskjuts inför %%semester%% kan du bortse från denna påminnelse.\r
		\r
		Vid frågor, kontakta i första hand skoladministratör på skolan alternativt skolskjutssamordnare på mejladress skolskjuts@sundsvall.se eller telefon 060-19 14 87 på vardagar mellan kl. 11-12.\r
		\r
		Vänliga hälsningar,\r
		Skolskjutssamordnare på barn- och utbildningskontoret, Sundsvalls kommun""";

	/*
	 * OeP family id type conversion
	 */
	static final Map<String, FamilyType> familyType = new HashMap<>();

	static {
		familyType.put("344", FamilyType.SKOLSKJUTS);
		familyType.put("136", FamilyType.SKOLSKJUTS);
		familyType.put("349", FamilyType.ELEVRESA);
		familyType.put("261", FamilyType.ELEVRESA);
	}

	private Constants() {
		// Prevent instantiation
	}

	public static FamilyType getFamilyType(final String familyId) {
		return familyType.get(familyId);
	}

	public static List<String> getProcessableSkolskjutsStatuses() {
		return PROCESSABLE_SKOLSKJUTS_STATUSES;
	}
}
