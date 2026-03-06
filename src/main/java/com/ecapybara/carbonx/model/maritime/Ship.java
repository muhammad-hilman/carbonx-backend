package com.ecapybara.carbonx.model.maritime;

import java.util.Collection;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;
import com.ecapybara.carbonx.model.basic.Node;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.processor.ConvertEmptyOrBlankStringsToNull;
import com.opencsv.bean.processor.PreAssignmentProcessor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true) 
@Document("ships")
@PersistentIndex(fields = {"id","key","name", "flag", "dateOnly"})
public class Ship extends Node {

	@NonNull
	@CsvBindByName(column = "MMSI")
	private String name;

	@NonNull
	@CsvBindByName
	private String latitude;

	@NonNull
	@CsvBindByName
	private String longitude;

	@NonNull
	@CsvBindByName
	private String speed;

	@NonNull
	@CsvBindByName
	private String course;

	@NonNull
	@CsvBindByName
	private String heading;

	@NonNull
	@CsvBindByName
	private String flag;

	@NonNull
	@CsvBindByName
	private String timestamp;

	@NonNull
	@CsvBindByName(column = "DateOnly")
	private String dateOnly;

	@Override
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);;
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			return super.toString(); // fallback
		}
	}
}
