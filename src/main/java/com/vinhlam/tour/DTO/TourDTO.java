package com.vinhlam.tour.DTO;

import java.util.List;

import org.bson.types.ObjectId;

import com.vinhlam.tour.entity.tourObject.Infos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDTO {

	private ObjectId _id;
	
	private List<Infos> infos;
	
	private int slot;
	
	private float priceCurrency;
}
