package com.vinhlam.tour.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tour.DTO.PriceOpenDTO;
import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.repository.PriceOpenRepository;

@Service
public class PriceOpenService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PriceOpenRepository priceOpenRepository;
	
	public boolean insertPriceOpen(PriceOpenDTO priceOpenDTO) {
		PriceOpen priceOpen = modelMapper.map(priceOpenDTO, PriceOpen.class);
		
		PriceOpen priceOpenResult = priceOpenRepository.save(priceOpen);
		if(priceOpenResult == null) {
			return false;
		} else {
			return true;
		}
	}
}
