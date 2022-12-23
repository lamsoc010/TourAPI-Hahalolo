package com.vinhlam.tour.controller;

import java.text.ParseException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.entity.Tour;
import com.vinhlam.tour.service.PriceTourService;
import com.vinhlam.tour.shared.FunctionShared;

@RestController
@RequestMapping("/api/priceTours")
public class PriceTourController {
	
	@Autowired
	private PriceTourService priceTourService;

	@GetMapping("/test")
	public String test() {
		return "Test Connect";
	}
	
	//API Get Price Tour By tourId and Date: http://localhost:8080/api/priceTours/getPriceTourByTourId
	@GetMapping("/getPriceTourByTourId")
	public ResponseEntity<?> getPriceTourByTourId(@RequestParam(value="tourId") String tourId, @RequestParam(value = "date") String date) throws ParseException {
		try {
//			Check param
			if(!ObjectId.isValid(tourId) || !FunctionShared.isValidDate(date)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input don't correct");
			}
//			Handle Logic
			PriceTour priceTour = priceTourService.getPriceTourByTourId(tourId, date);
			
//			Handle Response
			if(priceTour == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Price Tour is null");
			} else {
				return ResponseEntity.ok(priceTour);
			}
		} catch (Exception e) {
//			Ở đây chưa biết trả về mã lỗi nào
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi catch");
		}

	}
}
