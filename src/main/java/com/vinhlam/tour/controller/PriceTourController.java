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
import com.vinhlam.tour.exception.PriceTourNotFoundException;
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
				throw new PriceTourNotFoundException("PriceTour not found 123");
			} else {
				return ResponseEntity.ok(priceTour);
			}
			
		// Ở đây có 2 hướng xử lý
			//1. Là xử lý thủ công catch như này, thì có thể bắt được cái catch tổng và nhiều catch cùng lúc
			//2. Là exception này đã được cấu hình bắt tự động trong advice thì chỉ cẩn throws vào là được, khỏi cần try catch
				//nhưng như vậy thì lỡ có những lỗi khác ngoài các lỗi đã bắt thì chịu luôn
		// Vì sao không return luôn ở trên priceTour == null mà phải tách ra Exception rồi xuống đây bắt catch
		} catch (PriceTourNotFoundException e) { 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} 
		catch (Exception e) {
//			Ở đây chưa biết trả về mã lỗi nào
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi catch");
		}

	}
}
