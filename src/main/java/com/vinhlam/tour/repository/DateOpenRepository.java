package com.vinhlam.tour.repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.vinhlam.tour.entity.DateOpen;
import com.vinhlam.tour.entity.PriceTour;

@Repository
public class DateOpenRepository {

	@Autowired
	public MongoDatabase mongoDatabase;
	
	public MongoCollection<DateOpen> dateOpenCollection;
	
	@Autowired
	public void PriceTourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		dateOpenCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class).withCodecRegistry(cRegistry);

	}
	
	
//	Get list DateOpen by listTourId and date Open
// 2. DateOpen: getListDateOpen có ngày mở bán trùng với ngày xem và nằm trong listTourId(1)
	// Input: date, listTourId(1)
	// Output: listDateOpen ==> listTourId(2)
	public List<Document> getListDateOpen(List<ObjectId> listTourId, String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Bson> pipeline2 = new ArrayList<>();
		Bson match2 = new Document("$match", 
				new Document("tourId", new Document("$in", listTourId))
				.append("dateAvailable", new Document("$eq", df.parse(date)))
				.append("status", 1));
		Bson project2 = new Document("$project", 
				new Document("tourId", new Document("$toString", "$tourId") )
				.append("_id", 0)
				.append("dateAvailable", 1));
		
		pipeline2.add(match2);
		pipeline2.add(project2);
		
			//Create ArrayList
		List<Document> listDateOpens = new ArrayList<>();
		
		
		dateOpenCollection.aggregate(pipeline2, Document.class).into(listDateOpens);
		return listDateOpens;
	}
}
