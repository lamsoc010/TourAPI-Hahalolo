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

@Document(collation = "priceTour")
public class PriceTour {

	@Id
	private String _id;
	private String tourId;
	private Float price;
	private String currency;
	private Date dateApplyStart;
	private Date dateApplyEnd;
}
