package org.githup.kafka.consumer;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 消费者
 * 
 * @author sdc
 *
 */
@Component
public class KafkaConsumer {

	private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	/**
	 * 监听队列的消息
	 *
	 * @param record
	 * @param topic
	 */
	@KafkaListener(id = "QUEUE", topics = "kafka-queue")
	public void consume(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		// java 8 特性 新类Optional
		// 用途：项目里时长会有这样问题，调用一个方法获得返回值，但是不能直接用这个返回值作为参数来调用其它的方法，因为怕空指针，所以会经常去
		// 校验，一般项目里都会有工具类。但是Java 8 里内部出现了一个这个类，来应用的。
		Optional<?> kafkaMessage = Optional.ofNullable(record.value());
		
		//判断是否存在，不存在，存在即可去获取值
		if (kafkaMessage.isPresent()) {
			Object message = kafkaMessage.get();

			logger.info("Listener  topic： +++++++++++++++ Topic:" + topic);
			logger.info("Listener  record： +++++++++++++++ Record:" + record);
			logger.info("Listener message ： +++++++++++++++ Message:" + message);
		}
		
		//偏移量
		long offset = record.offset();
		//分区
		long partition = record.partition();
		
		logger.info("Listener message exist partition： +++++++++++++++ partition:" + partition);
		logger.info("Listener message exist offset ： +++++++++++++++ offset:" + offset);
	}

}