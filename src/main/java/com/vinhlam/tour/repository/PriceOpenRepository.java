package com.vinhlam.tour.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinhlam.tour.entity.PriceOpen;

@Repository
public interface PriceOpenRepository extends MongoRepository<PriceOpen, String>{

}
