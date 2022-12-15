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

@Document(collation = "priceOpen")
public class PriceOpen {
	
	@Id
	private String _id;
	private String tourId;
	private Date dateOpen;
	private Float price;
	private String currency;
}
