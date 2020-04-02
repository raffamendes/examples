package com.rmendes.consumer.processor;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.common.protocol.types.Struct;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class EventReader {

	@Incoming("account-events")
	public void proccess(Struct record) {
		System.out.println(record.getString("op"));
	}
}
