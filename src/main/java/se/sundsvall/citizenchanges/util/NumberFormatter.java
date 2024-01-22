package se.sundsvall.citizenchanges.util;

public final class NumberFormatter {

	private static final String COUNTRY_CODE = "+46";

	private NumberFormatter() {
	}

	public static String formatMobileNumber(final String mobileNumber) {
		if (mobileNumber == null) {
			return "";
		}
		if (mobileNumber.startsWith("0")) {
			var formattedNumber = mobileNumber.replaceAll("\\D", "");
			formattedNumber = formattedNumber.replaceFirst("0", COUNTRY_CODE);
			return formattedNumber;
		}
		return mobileNumber;
	}


}
