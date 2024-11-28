package org.goldrec.gold.service;

import org.goldrec.gold.internal.CsvRecord;

import java.lang.reflect.Field;

import static org.goldrec.gold.service.CsvReader.RECORD_FIELDS;
import static org.hibernate.graph.EntityGraphs.areEqual;


public class CompareRules {

//	может по полному совпадению 2х из набора (ФИО или дата рождения), телефон, почта?

	/**
	 * Совпадение по ключевым полям
	 * 0 - совпадение полностью,
	 * 1 - совпадение частично = одна группа записей,
	 * 2 - несовпадение = разные группы,
	 * 3 - ошибка
	 */
	public static CompareResult fioSnilsInnTelMail(
			CsvRecord record1, CsvRecord record2
	) {

		if (record1 == null || record2 == null) {
			return CompareResult.ERROR;
		}

		int ch = 0;
		ch += findCompleteMatch(cp(record1.getClient_first_name(), record2.getClient_first_name(), "first_name"));
		ch += findCompleteMatch(cp(record1.getClient_middle_name(), record2.getClient_middle_name(), "middle_name"));
		ch += findCompleteMatch(cp(record1.getClient_last_name(), record2.getClient_last_name(), "last_name"));
		ch += findCompleteMatch(cp(String.valueOf(record1.getClient_bday()), String.valueOf(record2.getClient_bday()), "bday"));
		ch += findCompleteMatch(cp(record1.getClient_inn(), record2.getClient_inn(), "inn"));
		ch += findCompleteMatch(cp(record1.getClient_snils(), record2.getClient_snils(), "snils"));
		ch += findCompleteMatch(cp(record1.getContact_phone(), record2.getContact_phone(), "phone"));
		ch += findCompleteMatch(cp(record1.getContact_email(), record2.getContact_email(), "email"));

		if (ch == 8)
			if (searchForCompleteDuplicates(record1, record2)) {
				return CompareResult.DUPLICATE;
			}
		if (ch >= 4) {
			return CompareResult.COINCIDENCE;
		}
		return CompareResult.NOT_EQUAL;
	}

	/** Поиск полных дублей */
	public static boolean searchForCompleteDuplicates(
			CsvRecord record1, CsvRecord record2
	) {

		if (record1 == null || record2 == null) {
			return false;
		}

		boolean areDuplicates = true;

		// Compare all fields
		for (Field field : RECORD_FIELDS) {
			String value1 = null;
			String value2 = null;

			// Устанавливаем поле как доступное
			field.setAccessible(true);

			try {
				value1 = field.get(record1) != null ? field.get(record1).toString() : null;
				value2 = field.get(record1) != null ? field.get(record2).toString() : null;

				// Остальной код логики здесь
				if (cp(value1, value2, "") != 1.0) {
					areDuplicates = false;
					break;
				}
			} catch (IllegalAccessException e) {
				// Обрабатываем случай, когда get() не удается
				System.out.println("Не удалось получить доступ к полю: " + field.getName());
			}
		}
		return areDuplicates;
	}

	private static double cp(String field1, String field2, String name) {
		if (field1 == null && field2 == null) {
			return 1.0;
		}
		if (field1 != null && field2 != null) {
			double sim = CompareFields.similarity(field1, field2);
//			System.out.print(name + ": " + sim + " ");
			return sim;
		}
		return -100.0;
	}

	private static int findCompleteMatch(double field) {
		if (field >= -1.0 && field <= 1.0) {
			return 1;
		}
		return 0;
	}
}
