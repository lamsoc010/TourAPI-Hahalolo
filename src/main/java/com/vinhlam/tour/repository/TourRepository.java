package com.vinhlam.tour.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinhlam.tour.entity.Tour;

@Repository
public interface TourRepository extends MongoRepository<Tour, String>{

}
