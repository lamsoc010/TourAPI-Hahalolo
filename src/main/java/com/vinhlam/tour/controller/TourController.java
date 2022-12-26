package com.vinhlam.tour.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinhlam.tour.DTO.TourDTO;
import com.vinhlam.tour.entity.Tour;
import com.vinhlam.tour.service.TourService;
import com.vinhlam.tour.shared.FunctionShared;

@RestController
@RequestMapping("/api/tours")
public class TourController {
	
	@Autowired
	private TourService tourService;
	
	@GetMapping("/test")
	public String test() {
		return "Test Connect";
	}
	
	//API Get all Tour: http://localhost:8080/api/tours/getAll
//	@GetMapping("/getAll")
//	public ResponseEntity<?> getAll(@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
//			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
//		//Check param
//		
//		//Handle logic
//		List<Tour> listTours = tourService.getAllTour(pageSize, pageNo);
//		
//		//Handle Reponse
//		if(listTours == null || listTours.size() == 0) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List tours is null");
//		} else {
//			return ResponseEntity.ok(listTours);
//		}
//	}
	
	
	//API Get all Tour: http://localhost:8080/api/tours/getListTours
		@GetMapping("/getListTours")
		public ResponseEntity<?> getListTourIs(@RequestParam(value = "numSlot", defaultValue = "1", required = false) String numSlot,
				@RequestParam(value = "lang", defaultValue = "vn", required = false) String lang,
				@RequestParam(value = "date", defaultValue = "12/16/2022", required = false) String date,
				@RequestParam(value = "currency", defaultValue = "VND", required = false) String currency) throws ParseException {
			try {
				//Check param
				if(!FunctionShared.isNumeric(numSlot) || Integer.parseInt(numSlot) <= 0  || !FunctionShared.isValidDate(date)) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input don't correct");
				}
				
				//Handle Logic
				List<Document> listTours = tourService.getListTours(Integer.parseInt(numSlot), lang, date, currency);
				
				//Handle Reponse
				if(listTours == null || listTours.size() == 0) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List tours is null");
				} else {
					return ResponseEntity.ok(listTours);
				}
			} catch (Exception e) {
//				Ở đây chưa biết trả về mã lỗi nào
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi catch");
			}
			
		}
}
