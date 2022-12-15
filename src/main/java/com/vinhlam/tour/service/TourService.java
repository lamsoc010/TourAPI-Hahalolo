package com.vinhlam.tour.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.vinhlam.tour.DTO.TourDTO;
import com.vinhlam.tour.entity.Tour;
import com.vinhlam.tour.repository.TourRepository;

@Service
public class TourService {

	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<Tour> tourCollection;
	
	
	@Autowired
	public void TourService() {
		tourCollection = mongoDatabase.getCollection("tour").withDocumentClass(Tour.class);
	}

//	Get All Tour
	public List<Tour> getAllTour(int pageSize, int pageNo) {
		List<Tour> listTours = new ArrayList<>();
		
		List<Bson> pipeline = new ArrayList<>();
		Bson skip = Aggregates.skip((pageNo - 1) * pageSize);
		Bson limit = Aggregates.limit(pageSize);
		
		pipeline.add(skip);
		pipeline.add(limit);
		
		tourCollection.aggregate(pipeline).into(listTours);
		return listTours;
	}
	
//	Get Tour By Filter
//	1.
	public List<TourDTO> getTourIsFilter(int numSlot, String lang, Date date, String currency) {
		List<TourDTO> listTourDTOs = new ArrayList<TourDTO>();
//		1. Get list tour is filter
		List<Bson> pipeline = new ArrayList<>();
		Bson match = new Document("$match", new Document("infos.lang", lang));
		Bson project = new Document("$project", 
				new Document("infos", new Document("$filter", 
						new Document("input", "$infos")
						.append("as", "info")
						.append("cond", new Document("$eq", Arrays.asList("$$info.lang", lang)))) ) );
		pipeline.add(match);
		pipeline.add(project);
		
		tourCollection.aggregate(pipeline, TourDTO.class).into(listTourDTOs);
		
		return listTourDTOs;
	}
	
	
}
