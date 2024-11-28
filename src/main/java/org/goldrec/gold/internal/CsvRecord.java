package org.goldrec.gold.internal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.goldrec.gold.parser.DateParser;
import org.goldrec.gold.parser.FieldsValidator;
import org.goldrec.gold.parser.PhoneNumberParser;

import java.time.LocalDate;
import java.util.function.Function;

@Getter
@Setter
@ToString
public class CsvRecord {
	String client_id;
	String client_first_name;
	String client_middle_name;
	String client_last_name;
	String client_fio_full;
	String client_bday;
	String client_bplace;
	String client_cityzen;
	String client_resident_cd;
	String client_gender;
	String client_marital_cd;
	String client_graduate;
	String client_child_cnt;
	String client_mil_cd;
	String client_zagran_cd;
	String client_inn;
	String client_snils;
	String client_vip_cd;
	String contact_vc;
	String contact_tg;
	String contact_other;
	String contact_email;
	String contact_phone;
	String addr_region;
	String addr_country;
	String addr_zip;
	String addr_street;
	String addr_house;
	String addr_body;
	String addr_flat;
	String addr_area;
	String addr_loc;
	String addr_city;
	String addr_reg_dt;
	String addr_str;
	String fin_rating;
	String fin_loan_limit;
	String fin_loan_value;
	String fin_loan_debt;
	String fin_loan_percent;
	String fin_loan_begin_dt;
	String fin_loan_end_dt;
	String stream_favorite_show;
	String stream_duration;
	String create_date;
	String update_date;
	String source_cd;

	public CsvRecord(String[] fields) {
		this.client_id = processField(fields[0], FieldsValidator::getIntegerField);
		this.client_first_name = processField(fields[1], String::toLowerCase);
		this.client_middle_name = processField(fields[2], String::toLowerCase);
		this.client_last_name = processField(fields[3], String::toLowerCase);
		this.client_fio_full = processField(fields[4], String::toLowerCase);
		this.client_bday = processField(fields[5], DateParser::parseDate);
		this.client_bplace = processField(fields[6], String::toLowerCase);
		this.client_cityzen = processField(fields[7], String::toLowerCase);
		this.client_resident_cd = processField(fields[8], String::toLowerCase);
		this.client_gender = processField(fields[9], String::toLowerCase);
		this.client_marital_cd = processField(fields[10], String::toLowerCase);
		this.client_graduate = processField(fields[11], String::toLowerCase);
		this.client_child_cnt = processField(fields[12], String::toLowerCase);
		this.client_mil_cd = processField(fields[13], String::toLowerCase);
		this.client_zagran_cd = processField(fields[14], String::toLowerCase);
		this.client_inn = processField(fields[15], FieldsValidator::getInnField);
		this.client_snils = processField(fields[16], FieldsValidator::getIntegerField);
		this.client_vip_cd = processField(fields[17], String::toLowerCase);
		this.contact_vc = processField(fields[18], String::toLowerCase);
		this.contact_tg = processField(fields[19], String::toLowerCase);
		this.contact_other = processField(fields[20], String::toLowerCase);
		this.contact_email = FieldsValidator.getEmailField(fields[21].toLowerCase());
		this.contact_phone = PhoneNumberParser.parsePhoneNumber(fields[22]);
		this.addr_region = processField(fields[23], String::toLowerCase);
		this.addr_country = processField(fields[24], String::toLowerCase);
		this.addr_zip = processField(fields[25], FieldsValidator::getIntegerField);
		this.addr_street = processField(fields[26], String::toLowerCase);
		this.addr_house = processField(fields[27], String::toLowerCase);
		this.addr_body = processField(fields[28], String::toLowerCase);
		this.addr_flat = processField(fields[29], String::toLowerCase);
		this.addr_area = processField(fields[30], String::toLowerCase);
		this.addr_loc = processField(fields[31], String::toLowerCase);
		this.addr_city = processField(fields[32], String::toLowerCase);
		this.addr_reg_dt = processField(fields[33], DateParser::parseDate);
		this.addr_str = processField(fields[34], String::toLowerCase);
		this.fin_rating = processField(fields[35], FieldsValidator::getIntegerField);
		this.fin_loan_limit = processField(fields[36], FieldsValidator::getIntegerField);
		this.fin_loan_value = processField(fields[37], FieldsValidator::getIntegerField);
		this.fin_loan_debt = processField(fields[38], FieldsValidator::getIntegerField);
		this.fin_loan_percent = processField(fields[39], FieldsValidator::getIntegerField);
		this.fin_loan_begin_dt = processField(fields[40], DateParser::parseDate);
		this.fin_loan_end_dt = processField(fields[41], DateParser::parseDate);
		this.stream_favorite_show = processField(fields[42], String::toLowerCase);
		this.stream_duration = processField(fields[43], String::toLowerCase);
		this.create_date = processField(fields[44], DateParser::parseDate);
		this.update_date = processField(fields[45], DateParser::parseDate);
		this.source_cd = processField(fields[46], DateParser::parseDate);
	}

	protected CsvRecord(CsvRecord csvRecord) {
		this.client_id = csvRecord.getClient_id();
		this.client_first_name = csvRecord.getClient_first_name();
		this.client_middle_name = csvRecord.getClient_middle_name();
		this.client_last_name = csvRecord.getClient_last_name();
		this.client_fio_full = csvRecord.getClient_fio_full();
		this.client_bday = csvRecord.getClient_bday();
		this.client_bplace = csvRecord.getClient_bplace();
		this.client_cityzen = csvRecord.getClient_cityzen();
		this.client_resident_cd = csvRecord.getClient_resident_cd();
		this.client_gender = csvRecord.getClient_gender();
		this.client_marital_cd = csvRecord.getClient_marital_cd();
		this.client_graduate = csvRecord.getClient_graduate();
		this.client_child_cnt = csvRecord.getClient_child_cnt();
		this.client_mil_cd = csvRecord.getClient_mil_cd();
		this.client_zagran_cd = csvRecord.getClient_zagran_cd();
		this.client_inn = csvRecord.getClient_inn();
		this.client_snils = csvRecord.getClient_snils();
		this.client_vip_cd = csvRecord.getClient_vip_cd();
		this.contact_vc = csvRecord.getContact_vc();
		this.contact_tg = csvRecord.getContact_tg();
		this.contact_other = csvRecord.getContact_other();
		this.contact_email = csvRecord.getContact_email();
		this.contact_phone = csvRecord.getContact_phone();
		this.addr_region = csvRecord.getAddr_region();
		this.addr_country = csvRecord.getAddr_country();
		this.addr_zip = csvRecord.getAddr_zip();
		this.addr_street = csvRecord.getAddr_street();
		this.addr_house = csvRecord.getAddr_house();
		this.addr_body = csvRecord.getAddr_body();
		this.addr_flat = csvRecord.getAddr_flat();
		this.addr_area = csvRecord.getAddr_area();
		this.addr_loc = csvRecord.getAddr_loc();
		this.addr_city = csvRecord.getAddr_city();
		this.addr_reg_dt = csvRecord.getAddr_reg_dt();
		this.addr_str = csvRecord.getAddr_str();
		this.fin_rating = csvRecord.getFin_rating();
		this.fin_loan_limit = csvRecord.getFin_loan_limit();
		this.fin_loan_value = csvRecord.getFin_loan_value();
		this.fin_loan_debt = csvRecord.getFin_loan_debt();
		this.fin_loan_percent = csvRecord.getFin_loan_percent();
		this.fin_loan_begin_dt = csvRecord.getFin_loan_begin_dt();
		this.fin_loan_end_dt = csvRecord.getFin_loan_end_dt();
		this.stream_favorite_show = csvRecord.getStream_favorite_show();
		this.stream_duration = csvRecord.getStream_duration();
		this.create_date = csvRecord.getCreate_date();
		this.update_date = csvRecord.getUpdate_date();
		this.source_cd = csvRecord.getSource_cd();
	}

	public static <T> T processField(String field, Function<String, T> operation) {
		field = StringUtils.trimToNull(field);
		return field != null ? operation.apply(field) : null;
	}

	public CsvRecord() {
	}
}
