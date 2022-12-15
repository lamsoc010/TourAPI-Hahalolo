package com.vinhlam.tour.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collation = "dateOpen")
public class DateOpen {

	@Id
	private String _id;
	private String tourId;
	private Date dateAvaiable;
	private int status;
	
}
