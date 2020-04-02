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
	public void proccess(String record) throws JsonMappingException, JsonProcessingException {
		Struct struct = new ObjectMapper().readValue(record, Struct.class);
		System.out.println(struct.getString("op"));
	}
}
