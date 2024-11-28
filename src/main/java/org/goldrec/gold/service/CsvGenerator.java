package org.goldrec.gold.service;

import com.opencsv.CSVWriter;
import org.goldrec.gold.internal.CsvRecord;
import org.goldrec.gold.internal.GoldRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.goldrec.gold.service.CsvReader.RECORD_FIELDS;

public class CsvGenerator {

	public static void writeCsv(List<GoldRecord> records) {

		String fileName = "gold_records_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
		String filePath = fileName;
		try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
			// Запись заголовка
			String[] header = Arrays.stream(RECORD_FIELDS).map(Field::getName).toArray(String[]::new);

			writer.writeNext(header);

			// Запись данных
			for (GoldRecord record : records) {

				String[] data = {
						String.valueOf(record.getClient_id()),
						record.getClient_first_name(),
						record.getClient_middle_name(),
						record.getClient_last_name(),
						record.getClient_fio_full(),
						String.valueOf(record.getClient_bday()),
						record.getClient_bplace(),
						record.getClient_cityzen(),
						record.getClient_resident_cd(),
						record.getClient_gender(),
						record.getClient_marital_cd(),
						record.getClient_graduate(),
						record.getClient_child_cnt(),
						record.getClient_mil_cd(),
						record.getClient_zagran_cd(),
						record.getClient_inn(),
						record.getClient_snils(),
						record.getClient_vip_cd(),
						record.getContact_vc(),
						record.getContact_tg(),
						record.getContact_other(),
						record.getContact_email(),
						record.getContact_phone(),
						record.getAddr_region(),
						record.getAddr_country(),
						record.getAddr_zip(),
						record.getAddr_street(),
						record.getAddr_house(),
						record.getAddr_body(),
						record.getAddr_flat(),
						record.getAddr_area(),
						record.getAddr_loc(),
						record.getAddr_city(),
						String.valueOf(record.getAddr_reg_dt()),
						record.getAddr_str(),
						record.getFin_rating(),
						record.getFin_loan_limit(),
						record.getFin_loan_value(),
						record.getFin_loan_debt(),
						record.getFin_loan_percent(),
						String.valueOf(record.getFin_loan_begin_dt()),
						String.valueOf(record.getFin_loan_end_dt()),
						record.getStream_favorite_show(),
						record.getStream_duration(),
						String.valueOf(record.getCreate_date()),
						String.valueOf(record.getUpdate_date()),
						String.valueOf(record.getSource_cd()),
						record.getRelatedRecords().toString()
				};
				writer.writeNext(data);
			}

			System.out.println("CSV файл успешно создан: " + filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
