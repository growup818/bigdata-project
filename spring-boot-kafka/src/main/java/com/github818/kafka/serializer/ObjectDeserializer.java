package com.github818.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

public class ObjectDeserializer implements Deserializer<Object> {

	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	/**
	 * 反序列化
	 */
	public Object deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

	public void close() {

	}
	
}
