package com.vinhlam.tour.DTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vinhlam.tour.entity.tourObject.Infos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDTO {

	@Id
	private String id;
	
	private List<Infos> infos;
	
	private int slot;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate  dateOpen;
	
	private float priceCurrency;
}
