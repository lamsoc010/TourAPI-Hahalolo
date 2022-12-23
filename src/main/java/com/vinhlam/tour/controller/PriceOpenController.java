package com.vinhlam.tour.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.service.PriceOpenService;

@RestController
@RequestMapping("/api/priceOpens")
public class PriceOpenController {
	
	@Autowired
	private PriceOpenService priceOpenService;
	
	@GetMapping("/test")
	public String test() {
		return "Test Connect";
	}
	
	//API Insert PriceOpen: http://localhost:8080/api/priceOpens/insertPriceOpen
	@PostMapping("/insertPriceOpen")
	public ResponseEntity<?> insertPriceOpen(@ModelAttribute("priceOpen") PriceOpen priceOpen) {
		boolean checkResult = priceOpenService.insertPriceOpen(priceOpen);
		
		if(!checkResult) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Insert not success");
		} else {
			return ResponseEntity.ok("Insert success");
		}
	}

}
