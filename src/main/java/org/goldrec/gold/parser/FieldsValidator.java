package org.goldrec.gold.parser;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsValidator {

	private static final String EMAIL_REGEX =
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

	private static boolean isValidEmail(String email) {
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.find();
	}

	public static String getEmailField(String email) {
		return isValidEmail(email) ? email : null;
	}


	public static String getInnField(String inn) {
		if (containsOnlyDigits(inn))
			return inn.length() == 12 ? inn : null;
		else
			return null;
	}

	public static boolean containsOnlyDigits(String str) {
		return str != null && str.length() > 0 &&
				str.chars().allMatch(Character::isDigit);
	}
	public static String getIntegerField(String str) {
		return containsOnlyDigits(str) ? str : null;
	}

}