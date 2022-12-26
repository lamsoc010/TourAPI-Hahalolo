package com.vinhlam.tour.DTO;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PriceOpenDTO {
	
	@NotNull(message = "tourId shouldn't be null")
	@Size(
	            min = 24,
	            max = 24,
	            message = "tourId must be {min} characters long"
			)
	private String tourId;
	
	@NotNull(message = "dateOpen shouldn't be null")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOpen;
	
	@NotNull(message = "price shouldn't be null")
	private Float price;
	
	@NotNull(message = "currency shouldn't be null")
	private String currency;
}
