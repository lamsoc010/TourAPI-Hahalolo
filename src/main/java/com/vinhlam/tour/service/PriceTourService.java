package com.vinhlam.tour.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.vinhlam.tour.entity.DateOpen;
import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.entity.Tour;
import com.vinhlam.tour.repository.PriceTourRepository;

@Service
public class PriceTourService {
	
	@Autowired
	private PriceTourRepository priceTourRepository;
	
	public PriceTour getPriceTourByTourId(String id, String date) throws ParseException {
		
		PriceTour priceTour = priceTourRepository.getPriceTourByTourIdAndDate(id, date);
		
		return priceTour;
	}
}
