package org.goldrec.gold.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateParser {

	private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
			DateTimeFormatter.ofPattern("yyyy-MM-dd"),
			DateTimeFormatter.ofPattern("dd.MM.yyyy"),
			DateTimeFormatter.ofPattern("MM/dd/yyyy"),
			DateTimeFormatter.ofPattern("MMM dd, yyyy"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
	);

	public static String parseDate(String dateString) {

		if (dateString == null || dateString.isEmpty()) {
			return null;
		}
		if (dateString.startsWith("\"") && dateString.length() > 1){
			dateString = dateString.substring(1, dateString.length() - 1);
		}
		for (DateTimeFormatter formatter : FORMATTERS) {
			try {
				return LocalDate.parse(dateString, formatter).toString();
			} catch (DateTimeParseException e) {
				// Переходим к следующему форматеру
			}
		}
		return null; // Не удалось распарсить дату ни одним форматом
	}

}