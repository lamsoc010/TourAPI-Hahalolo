package com.vinhlam.tour.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.repository.PriceOpenRepository;

@Service
public class PriceOpenService {

	@Autowired
	private PriceOpenRepository priceOpenRepository;
	
	public boolean insertPriceOpen(PriceOpen priceOpen) {
		PriceOpen priceOpenResult = priceOpenRepository.save(priceOpen);
		if(priceOpenResult == null) {
			return false;
		} else {
			return true;
		}
	}
}
