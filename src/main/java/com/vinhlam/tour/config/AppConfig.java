package com.vinhlam.tour.config;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Configuration
public class AppConfig {
	

	@Value("${spring.data.mongodb.database}")
	private String mongoDB;

	@Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
	
	@Bean
	public MongoDatabase mongoDatabase() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		return MongoClients.create().getDatabase(mongoDB).withCodecRegistry(cRegistry);
	}
}
