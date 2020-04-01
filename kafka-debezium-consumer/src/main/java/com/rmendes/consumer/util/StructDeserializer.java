package com.rmendes.consumer.util;

import org.apache.kafka.common.protocol.types.Struct;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class StructDeserializer extends JsonbDeserializer<Struct>{

	public StructDeserializer() {
		super(Struct.class);
		// TODO Auto-generated constructor stub
	}

}
