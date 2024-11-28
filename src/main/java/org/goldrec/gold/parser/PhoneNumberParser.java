package org.goldrec.gold.parser;

//Принимаем, что телефон Российский
public class PhoneNumberParser {

	public static String parsePhoneNumber(String input) {
		// Удаляем все нецифровые символы
		String digitsOnly = input.replaceAll("\\D", "");

		if (digitsOnly.length() < 10)
			return null;

		// Извлекаем последние 10 цифр
		return digitsOnly.substring(Math.max(0, digitsOnly.length() - 10));
	}

}