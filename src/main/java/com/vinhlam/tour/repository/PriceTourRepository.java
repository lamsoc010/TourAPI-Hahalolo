package com.vinhlam.tour.repository;

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
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinhlam.tour.entity.PriceTour;

@Repository
public class PriceTourRepository {
	
	@Autowired
	public MongoDatabase mongoDatabase;
	
	public MongoCollection<PriceTour> priceTourCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		priceTourCollection = mongoDatabase.getCollection("priceTour", PriceTour.class).withCodecRegistry(cRegistry);

	}
	
	
//	Get price tour by tourId and date Open
	public PriceTour getPriceTourByTourIdAndDate(String tourId, String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		PriceTour priceTour = new PriceTour();
		List<Bson> pipeline = new ArrayList<>();
//		Bson match = Aggregates.match(Filters.eq("tourId", new ObjectId(id)));
		Bson match = new Document("$match", 
				new Document("$and", Arrays.asList(
						new Document("dateApplyStart", new Document("$lte", df.parse(date) )),
						new Document("dateApplyEnd", new Document("$gte", df.parse(date) )) ) )
				.append("tourId", new ObjectId(tourId)));
		Bson project = new Document("$project", 
				new Document("tourId", new Document("$toString", "$tourId") )
				.append("_id", 0)
				.append("price", 1)
				.append("currency", 1)
				.append("dateApplyStart", 1)
				.append("dateApplyEnd", 1) );
		
		pipeline.add(match);
		pipeline.add(project);
		
		priceTour = priceTourCollection.aggregate(pipeline).first();
		
		return priceTour;
	}

	
//	Get list priceTour => return List<Document>
// 1. PriceTour: getListPriceTour có ngày được setting
			// Input: date
			// Output: listPriceTour ==> listTourId(1)
	public List<Document> getListPriceTour(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Bson> pipeline1 = new ArrayList<>();
		Bson match1 = new Document("$match", 
				new Document("$and", Arrays.asList(
						new Document("dateApplyStart", new Document("$lte", df.parse(date) )),
						new Document("dateApplyEnd", new Document("$gte", df.parse(date) )) ) ) );
		Bson project1 = new Document("$project", 
				new Document("tourId", new Document("$toString", "$tourId") )
				.append("_id", 0)
				.append("price", 1)
				.append("currency", 1)
				.append("dateApplyStart", 1)
				.append("dateApplyEnd", 1) );
		
		pipeline1.add(match1);
		pipeline1.add(project1);
		
			//Create ArrayList
		List<Document> listPriceTours = new ArrayList<>();
		
		
		priceTourCollection.aggregate(pipeline1, Document.class).into(listPriceTours);
		return listPriceTours;
	}
}
