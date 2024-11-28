package org.goldrec.gold;

import com.opencsv.exceptions.CsvException;
import org.goldrec.gold.internal.CsvRecord;
import org.goldrec.gold.internal.GoldRecord;
import org.goldrec.gold.service.CompareResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.goldrec.gold.service.CompareRules.fioSnilsInnTelMail;
import static org.goldrec.gold.service.CsvGenerator.writeCsv;
import static org.goldrec.gold.service.CsvReader.RECORD_FIELDS;
import static org.goldrec.gold.service.CsvReader.fillCsvRecord;
import static org.goldrec.gold.service.CsvReader.readCsvFile;

@SpringBootApplication
public class GoldApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(GoldApplication.class, args);
//	}


	public static void main(String[] args) throws IOException, IllegalAccessException, CsvException {
//		String filePath = "/Users/elena/hack/test4.csv";

//		String filePath = "/Users/elena/hack/50000data.csv";
		String filePath = args[0];
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


}
