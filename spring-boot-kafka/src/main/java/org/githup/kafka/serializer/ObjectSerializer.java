package org.githup.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

public class ObjectSerializer implements Serializer<Object> {

	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	public byte[] serialize(String topic, Object data) {
		// TODO Auto-generated method stub
		return SerializationUtils.serialize(data);
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	
}
