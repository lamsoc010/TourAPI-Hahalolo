package com.vinhlam.tour.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vinhlam.tour.entity.tourObject.Infos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Document(collection = "tour")
public class Tour {

	@Id
	private String id;
	
	private List<Infos> infos;
	
	private int slot;
}
