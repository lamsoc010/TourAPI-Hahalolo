package com.vinhlam.tour.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinhlam.tour.entity.DateOpen;

@Repository
public interface DateOpenRepository extends MongoRepository<DateOpen, String>{

}
