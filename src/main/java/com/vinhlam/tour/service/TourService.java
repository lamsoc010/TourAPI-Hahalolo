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
import com.vinhlam.tour.repository.TourRepository;

import ch.qos.logback.core.model.Model;

@Service
public class TourService {

	@Autowired
	private TourRepository tourRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MongoDatabase mongoDatabase;
	
	private MongoCollection<Tour> tourCollection;
	private MongoCollection<PriceOpen> priceOpenCollection;
	private MongoCollection<PriceTour> priceTourCollection;
	private MongoCollection<DateOpen> dateOpenCollection;
	
	
	@Autowired
	public void TourService() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		
		tourCollection = mongoDatabase.getCollection("tour", Tour.class).withCodecRegistry(cRegistry);
		priceOpenCollection = mongoDatabase.getCollection("priceOpen", PriceOpen.class).withCodecRegistry(cRegistry);
		priceTourCollection = mongoDatabase.getCollection("priceTour", PriceTour.class).withCodecRegistry(cRegistry);
		dateOpenCollection = mongoDatabase.getCollection("dateOpen", DateOpen.class).withCodecRegistry(cRegistry);
		
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
//		return tourRepository.findAll();
	}
	
//	Get Tour By Filter
//	1.
//	public List<TourDTO> getListTourIs(int numSlot, String lang, String date, String currency) {
////		1. Filter list tours by lang
//		//Input: lang
//		//Output: listTour(1)
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		List<TourDTO> listToursDTO = new ArrayList<>();
//		List<Bson> pipeline1 = new ArrayList<>();
//		Bson match1 = new Document("$match", new Document("infos.lang", lang));
//		Bson project1 = new Document("$project", 
//				new Document("infos", new Document("$filter", 
//						new Document("input", "$infos")
//						.append("as", "info")
//						.append("cond", new Document("$eq", Arrays.asList("$$info.lang", lang)))) )
//				.append("_id", new Document("$toString", "$_id")));
//		pipeline1.add(match1);
//		pipeline1.add(project1);
//		tourCollection.aggregate(pipeline1, TourDTO.class).into(listToursDTO);
//		
////		2. Get list tours by date and calculate printCurrent (printCurrent = price * numSlot)
//		//Input: data, numSlot
//		//Output: listPriceOpen
//		List<Document> listDocumentPriceOpen = new ArrayList<>();
//		List<Bson> pipeline2 = new ArrayList<>();
//		System.out.println(date);
//		Bson match2 = null;
//		try {
//			match2 = new Document("$match", new Document("dateOpen", df.parse(date)));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Bson project2 = new Document("$project", 
//				new Document("priceCurrent", 
//						new Document("$multiply", Arrays.asList("$price", numSlot)))
//				.append("dataOpen", 1)
//				.append("tourId", 1)) ;
//		
//		pipeline2.add(match2);
//		pipeline2.add(project2);
//		
//		priceOpenCollection.aggregate(pipeline2, Document.class).into(listDocumentPriceOpen);
//		System.out.println();
////		3. Add priceCurrent to listTours
//		for (TourDTO tour : listToursDTO) {
//			for(Document d: listDocumentPriceOpen) {
//				System.out.println(d.get("tourId"));
//				System.out.println(tour.getId());
//				System.out.println(d.get("priceCurrent"));
//				if(d.get("tourId").toString().equals(tour.getId())) {
//					tour.setPriceCurrency(Float.parseFloat(d.get("priceCurrent").toString()));
//					tour.setSlot(numSlot);
////					tour.setDateOpen(date);
//				}
//			}
//		}
//		return listToursDTO;
//	}
	
	public List<Document> getListTours(int numSlot, String lang, String date, String currency) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 1. PriceTour: getListPriceTour có ngày được setting
			// Input: date
			// Output: listPriceTour ==> listTourId(1)
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
		List<ObjectId> listTourId1 = new ArrayList<>();
		
		priceTourCollection.aggregate(pipeline1, Document.class).into(listPriceTours);
		
			//Insert list listTourId(1)
		for (Document d : listPriceTours) {
			listTourId1.add(new ObjectId(d.getString("tourId")));
		}
			// Vấn đề: Query được rồi nhưng ngày trả về bị tụt 1 ngày sao với DB
		
		// 2. DateOpen: getListDateOpen có ngày mở bán trùng với ngày xem và nằm trong listTourId(1)
		// Input: date, listTourId(1)
		// Output: listDateOpen ==> listTourId(2)
		
		List<Bson> pipeline2 = new ArrayList<>();
		Bson match2 = new Document("$match", 
				new Document("tourId", new Document("$in", listTourId1))
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
		List<ObjectId> listTourId2 = new ArrayList<>();
		
		dateOpenCollection.aggregate(pipeline2, Document.class).into(listDateOpens);
		
			//Insert list listTourId(1)
		for (Document d : listDateOpens) {
			listTourId2.add(new ObjectId(d.getString("tourId")));
		}

		// 3. Tour: getListTour by language, listTourId(2) and numSlot lte slot in tour
		// Input: listTourId(2), lang, numSlot
		// Output: listTour
		List<Bson> pipeline3 = new ArrayList<>();
		Bson match3 = new Document("$match", 
				new Document("_id", new Document("$in", listTourId2))
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
		
		// 4. Tính totalPrice, chuyển đổi giá tiền theo current, ghép giá tiền vào tour phù hợp
				// Input: listPriceTours, listTours, numSlot, currentcy
				// Output: listTours have numSlot and priceCurrent(priceCurrent = price * numSlot)
		for(Document priceTour: listPriceTours) {
			for(Document tour: listTours) {
				if(priceTour.getString("tourId").equals(tour.getString("_id"))) {
					tour.append("currency", currency);
					tour.append("price", convertCurrencyPrice(priceTour.getString("currency"), priceTour.getInteger("price"), currency ));
					tour.append("numSlot", numSlot);
					tour.append("totalPrice", convertCurrencyPrice(priceTour.getString("currency"), priceTour.getInteger("price"), currency ) * numSlot);
				}
			}
		}
		
		
		
		
		
		return listTours;
	}
	
	
//	Convert price by currency
	public Double convertCurrencyPrice(String currencyCurrent, double price, String currencyNew) {
		double priceNew = 0;
		if(!currencyNew.equalsIgnoreCase("USD") || !currencyNew.equalsIgnoreCase("VND")) {
			priceNew = price;
		}
		if(!currencyCurrent.equals(currencyNew)) {
			if(currencyCurrent.equalsIgnoreCase("VND") && currencyNew.equalsIgnoreCase("USD")) {
				priceNew = price/23555;
			}
			if(currencyCurrent.equalsIgnoreCase("USD") && currencyNew.equalsIgnoreCase("VND")) {
				priceNew = price * 23555;
			} 
		} else {
			priceNew = price;
		}
		
//		Convert price to BigDecimal
		int scale = 2;
		BigDecimal tempBig = convertNumberToBigDecimal(priceNew, scale);
		return Double.parseDouble(tempBig.toString());
	}
	
	public BigDecimal convertNumberToBigDecimal(double price, int scale) {
		BigDecimal tempBig = new BigDecimal(Double.toString(price));
		tempBig = tempBig.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		return tempBig;
	}
	
}
