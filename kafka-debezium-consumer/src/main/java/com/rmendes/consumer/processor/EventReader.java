package com.rmendes.consumer.processor;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class EventReader {

	@Incoming("account-events")
	public void proccess(String record) {
		System.out.println(record);
	}
}
