package com.rmendes.consumer.processor;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.common.protocol.types.Struct;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class EventReader {

	@Incoming("account-events")
	public void proccess(String record) {
		try {
			Struct struct = new ObjectMapper().readValue(record, Struct.class);
			System.out.println(record);
			System.out.println(struct);
			System.out.println(struct.getString("op"));
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
