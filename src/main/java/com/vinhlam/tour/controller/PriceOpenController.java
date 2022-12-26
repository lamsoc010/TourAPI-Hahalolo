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

import com.vinhlam.tour.DTO.PriceOpenDTO;
import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.service.PriceOpenService;
import com.vinhlam.tour.shared.FunctionShared;

import jakarta.validation.Valid;

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
	public ResponseEntity<?> insertPriceOpen(@RequestBody @Valid PriceOpenDTO priceOpenDTO) {
		try {
//			Check param(Ở đây nên dùng Hibernate để check dữ liệu trong entity luôn, nhưng giờ demo check ở đây cũng được)
//			if(priceOpen.getCurrency() == null 
//					|| priceOpen.getDateOpen() == null 
//					|| priceOpen.getPrice() == null 
//					|| priceOpen.getTourId() == null ) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input don't correct");
//			}
			
//			Đã xử lý input ở advice, không xử lí ở đây nữa
			
//			Handle Logic
			boolean checkResult = priceOpenService.insertPriceOpen(priceOpenDTO);
			
//			Handle Response
			if(!checkResult) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insert not success");
			} else {
				return ResponseEntity.ok("Insert success");
			}
		} catch (Exception e) {
//			Ở đây chưa biết trả về mã lỗi nào
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi catch");
		}

	}

}
