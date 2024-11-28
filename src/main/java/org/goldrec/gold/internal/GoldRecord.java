package org.goldrec.gold.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class GoldRecord extends CsvRecord {
	List<String> relatedRecords;

	public GoldRecord(CsvRecord csvRecord, List<String> relatedRecords) {
		super(csvRecord);
		this.relatedRecords = relatedRecords;
	}
}
