package com.rmendes.consumer.processor;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class EventReader {

	@Incoming("account-events")
	public void proccess(String record) {
		try {
			JsonNode jsonNode = new ObjectMapper().readTree(record);
			System.out.println("Type of Record: " + jsonNode.path("payload").path("op").asText());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}
}
