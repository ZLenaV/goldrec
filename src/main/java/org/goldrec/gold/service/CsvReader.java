package org.goldrec.gold.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.goldrec.gold.internal.CsvRecord;
import org.goldrec.gold.internal.GoldRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.goldrec.gold.service.CompareRules.fioSnilsInnTelMail;
import static org.goldrec.gold.service.CsvGenerator.writeCsv;


public class CsvReader {

	public final static Field[] RECORD_FIELDS = CsvRecord.class.getDeclaredFields();

	public static List<CsvRecord> readCsvFile(String filePath) throws IOException, CsvException {
		List<CsvRecord> records = new ArrayList<>();

//		try (CSVReader reader = new CSVReaderBuilder(new FileReader("file.csv")).build()) {
//			List<String[]> records = reader.readAll();
//
//			for (String[] record : records) {
//				// Обработка записи
//				System.out.println(record[0]); // Первый столбец
//			}
//		}




		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			// Пропускаем первую строку
			reader.readLine();

			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				// Создаем новый объект CsvRecord
				CsvRecord record = new CsvRecord(fields);

				// Добавляем объект в список
				records.add(record);
			}
		}

		return records;
	}

	public static void main(String[] args) throws IOException, IllegalAccessException, CsvException {
//		String filePath = "/Users/elena/hack/test4.csv";

		String filePath = "/Users/elena/hack/ds_dirty_fin_202410041147.csv";
		List<CsvRecord> records = readCsvFile(filePath);

		// Обработка записей
		List<List<CsvRecord>> groups = new ArrayList<>();
		groups.add(new ArrayList<>(List.of(records.get(0))));
		for (CsvRecord newrecord : records) {
//			System.out.print("id: " + newrecord.getClient_id() + " ");
			CompareResult compare = CompareResult.DEF;
			//todo можно еще реализовать действия при частичном несовпадении - например сравнить еще с одной записью в списке, для упрощения сравниваются только первые записи
			for (List<CsvRecord> group : groups) {
				for (CsvRecord groupRecord : group) {
					compare = fioSnilsInnTelMail(groupRecord, newrecord);
					break; //пока заглушка, проверяем только одну запись из группы
				}
				// если не совпадение, смотрим запись из другой группы
				if (compare == CompareResult.NOT_EQUAL) {
					continue;
				}
				// если совпадение, добавляем запись в эту же группу
				if (compare == CompareResult.COINCIDENCE) {
					group.add(newrecord);
					break;
				}
				// если нашли дубликат, никуда его не добавляем и переходим к следующей newrecord
				if (compare == CompareResult.DUPLICATE) {
					break;
				}
			}
			// если после просмотра всех групп compare так и остался 2 создаем новую группу для записи
			if (compare == CompareResult.NOT_EQUAL) {
				groups.add(new ArrayList<>(List.of(newrecord)));
			}

//			System.out.println();
		}

		List<GoldRecord> goldRecords = new ArrayList<>();

		for (int i = 0; i < groups.size(); i++) {
			CsvRecord csvGoldRecord = new CsvRecord();
			List<CsvRecord> group = groups.get(i);
			List<Map<String, Integer>> rateList = new ArrayList<>();
			for (int j = 0; j < RECORD_FIELDS.length; j++) {
				rateList.add(new HashMap<>());
			}
			for (CsvRecord groupRecord : group) {
				readAllFieldsAndFillRate(rateList, groupRecord);
			}
			fillCsvRecord(csvGoldRecord, rateList);
			List<String> recordIds = group.stream().map(CsvRecord::getClient_id).toList();
			GoldRecord goldRecord = new GoldRecord(csvGoldRecord, recordIds);
			goldRecord.setClient_id(String.valueOf(i));
			goldRecords.add(goldRecord);
		}
		writeCsv(goldRecords);
	}


	// считать все значения полей из CsvRecord и заполнить частоты
	// todo в золотой записи должно быть поле списка id
	public static List<Map<String, Integer>> readAllFieldsAndFillRate(List<Map<String, Integer>> rateList, CsvRecord record) {

//		List<Map<String, Integer>> rateList = new ArrayList<>();


		// Iterate through all fields using reflection
		Field[] fields = RECORD_FIELDS;

		// тут пропускаются поля где значения == null. Проверить, что все корректно сопоставляется
		for (int i = 0; i < fields.length; i++) {
			try {
				Object value = fields[i].get(record);
				// Очень сомнительно, хорошо проверить
//				if (rateList.get(i) == null) {
//					rateList.add(new HashMap<>() {
//					});
//				}
				if (value != null) {
					String valueStr = value.toString();

					if (rateList.get(i).containsKey(valueStr)) {
						rateList.get(i).put(valueStr, rateList.get(i).get(valueStr) + 1);
					} else {
						rateList.get(i).put(valueStr, 1);
					}
				}
			} catch (IllegalAccessException e) {
				System.err.println("Error accessing field: " + fields[i].getName());
			}
		}
		return rateList;
	}

	public static void fillCsvRecord(CsvRecord record, List<Map<String, Integer>> rateList) throws IllegalAccessException {
		int index = 0;
		//todo размер rateList должен совпадать с количеством полей CsvRecord
		for (Field field : RECORD_FIELDS) {
			Map<String, Integer> stringIntegerMap = rateList.get(index);
			String maxKey = "";
			if (!stringIntegerMap.isEmpty()) {
				Map.Entry<String, Integer> maxEntry = Collections.max(stringIntegerMap.entrySet(), Comparator.comparing(Map.Entry::getValue));
				maxKey = maxEntry.getKey();
			}

			field.setAccessible(true);
			//todo перевести все LocalDate в строки в CsvRecord
			field.set(record, maxKey);
			index++;
		}
	}
}