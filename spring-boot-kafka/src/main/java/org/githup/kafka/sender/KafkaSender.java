package org.githup.kafka.sender;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.githup.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class KafkaSender {
 
	private static final Logger log = LoggerFactory.getLogger(KafkaSender.class);

	@Autowired
	private KafkaTemplate<Object, Object> kafkaTemplate;

	public void send() throws InterruptedException, ExecutionException {
		Message message = new Message();
		message.setId(System.currentTimeMillis());
		message.setMsg(UUID.randomUUID().toString());
		message.setSendTime(new Date());
		log.info("+++++++++ message = {}", JSONObject.toJSONString(message));
		kafkaTemplate.send("kafka-queue", JSONObject.toJSONString(message));
	}

}
