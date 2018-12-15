package com.github818.kafka.sender;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * Kafka消息生产者
 * @author YI
 * @data 2018-8-11 11:22:36
 */
@Slf4j
@Component
public class KafkaProducer {
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topicName, String jsonData) {
//        log.info("向kafka推送数据:[{}]", jsonData);
        try {
            kafkaTemplate.send(topicName, jsonData);
        } catch (Exception e) {
//            log.error("发送数据出错！！！{}{}", topicName, jsonData);
//            log.error("发送数据出错=====>", e);
        }

        //消息发送的监听器，用于回调返回信息
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
//                log.info("topic：" + topic + " onSuccess");
            }

            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
//                log.error("ERROR: " + exception.getMessage());
            }

            public boolean isInterestedInSuccess() {
//                log.info("数据发送完毕");
                return false;
            }
        });
    }
}