package com.vinhlam.tour.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.vinhlam.tour.entity.DateOpen;
import com.vinhlam.tour.entity.PriceOpen;
import com.vinhlam.tour.entity.PriceTour;
import com.vinhlam.tour.entity.Tour;

@Repository
public class TourRepository {
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<Tour> tourCollection;
	
	
	@Autowired
	public void TourRepository() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		tourCollection = mongoDatabase.getCollection("tour", Tour.class).withCodecRegistry(cRegistry);
		
	}
	
//	Get All List Tour is pagination
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
	
	// 3. Tour: getListTour by language, listTourId(2) and numSlot lte slot in tour
			// Input: listTourId(2), lang, numSlot
			// Output: listTour
	public List<Document> getListTourIsFilter(List<ObjectId> listTourId, String lang, int numSlot) {
		List<Bson> pipeline3 = new ArrayList<>();
		Bson match3 = new Document("$match", 
				new Document("_id", new Document("$in", listTourId))
				.append("slot", new Document("$gte", numSlot) ));
		
		Bson project3 = new Document("$project", 
				new Document("infos", new Document("$cond", 
						Arrays.asList(new Document("$eq", Arrays.asList(new Document("$size", new Document("$filter", new Document("input", "$infos").append("cond", new Document("$eq", Arrays.asList("$$this.lang", lang))) )), 0) ),
									  new Document("$slice", Arrays.asList("$infos", 1)),
									  new Document("$filter", new Document("input", "$infos").append("cond", new Document("$eq", Arrays.asList("$$this.lang", lang))) )  ) ) )
//				.append("slot", 1)
				.append("_id", new Document("$toString", "$_id")));
		
		pipeline3.add(match3);
		pipeline3.add(project3);
		
			//Create ArrayList
		List<Document> listTours = new ArrayList<>();
		
		tourCollection.aggregate(pipeline3, Document.class).into(listTours);
		
		return listTours;
	}
}
