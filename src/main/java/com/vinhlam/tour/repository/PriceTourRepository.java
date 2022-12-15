package com.vinhlam.tour.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinhlam.tour.entity.PriceTour;

@Repository
public interface PriceTourRepository extends MongoRepository<PriceTour, String>{

}
