package com.vinhlam.tour.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.BigDecimalCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.vinhlam.tour.DTO.TourDTO;
import com.vinhlam.tour.entity.DateOpen;
import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.entity.Tour;
import com.vinhlam.tour.repository.DateOpenRepository;
import com.vinhlam.tour.repository.PriceTourRepository;
import com.vinhlam.tour.repository.TourRepository;
import com.vinhlam.tour.shared.FunctionShared;

import ch.qos.logback.core.model.Model;

@Service
public class TourService {

	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private PriceTourRepository priceTourRepository;
	
	@Autowired
	private DateOpenRepository dateOpenRepository;

//
////	Get All Tour
//	public List<Tour> getAllTour(int pageSize, int pageNo) {
//		
//		List<Tour> listTours = tourRepository.getAllTour(pageSize, pageNo);
//		return listTours;
//	}
	
//	1. Get list Tour by numSlot, lang, date, currency
	public List<Document> getListTours(int numSlot, String lang, String date, String currency) throws ParseException {
		// 1. PriceTour: getListPriceTour có ngày được setting
			// Input: date
			// Output: listPriceTour ==> listTourId(1)
		List<Document> listPriceTours = priceTourRepository.getListPriceTour(date);
		List<ObjectId> listTourId1 = new ArrayList<>();
			//Insert list listTourId(1)
		for (Document d : listPriceTours) {
			listTourId1.add(new ObjectId(d.getString("tourId")));
		}
		
		// 2. DateOpen: getListDateOpen có ngày mở bán trùng với ngày xem và nằm trong listTourId(1)
			// Input: date, listTourId(1)
			// Output: listDateOpen ==> listTourId(2)
		List<Document> listDateOpens = dateOpenRepository.getListDateOpen(listTourId1, date);
		List<ObjectId> listTourId2 = new ArrayList<>();
		
			//Insert list listTourId(1)
		for (Document d : listDateOpens) {
			listTourId2.add(new ObjectId(d.getString("tourId")));
		}

		// 3. Tour: getListTour by language, listTourId(2) and numSlot lte slot in tour
		// Input: listTourId(2), lang, numSlot
		// Output: listTour
		List<Document> listTours = tourRepository.getListTourIsFilter(listTourId2, lang, numSlot);
		
		// 4. Tính totalPrice, chuyển đổi giá tiền theo current, ghép giá tiền vào tour phù hợp
				// Input: listPriceTours, listTours, numSlot, currentcy
				// Output: listTours have numSlot and priceCurrent(priceCurrent = price * numSlot)
		for(Document priceTour: listPriceTours) {
			for(Document tour: listTours) {
				if(priceTour.getString("tourId").equals(tour.getString("_id"))) {
					tour.append("currency", currency);
					tour.append("price", FunctionShared.convertCurrencyPrice(priceTour.getString("currency"), priceTour.getInteger("price"), currency ));
					tour.append("numSlot", numSlot);
					tour.append("totalPrice", FunctionShared.convertCurrencyPrice(priceTour.getString("currency"), priceTour.getInteger("price"), currency ) * numSlot);
				}
			}
		}
		
		return listTours;
	}
	
	

	
}
