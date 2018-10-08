package org.githup.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.githup.kafka.serializer.ObjectDeserializer;
import org.githup.kafka.serializer.ObjectSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode;


/**
 * kafka配置
 * 
 * @author sdc
 *
 */
@Configuration
@EnableKafka
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String producerBootstrapServers; // 生产者连接Server地址

	@Value("${spring.kafka.producer.retries}")
	private String producerRetries; // 生产者重试次数

	@Value("${spring.kafka.producer.batchSize}")
	private String producerBatchSize;

	@Value("${spring.kafka.producer.lingerMs}")
	private String producerLingerMs;

	@Value("${spring.kafka.producer.bufferMemory}")
	private String producerBufferMemory;

	@Value("${spring.kafka.consumer.bootstrapServers}")
	private String consumerBootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

	@Value("${spring.kafka.consumer.enable-auto-commit}")
	private String consumerEnableAutoCommit;

	@Value("${spring.kafka.consumer.autoCommitIntervalMs}")
	private String consumerAutoCommitIntervalMs;

	@Value("${spring.kafka.consumer.sessionTimeoutMs}")
	private String consumerSessionTimeoutMs;

	@Value("${spring.kafka.consumer.maxPollRecords}")
	private String consumerMaxPollRecords;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String consumerAutoOffsetReset;

	/**
	 * ProducerFactory
	 * 
	 * @return
	 */
	@Bean
	public ProducerFactory<Object, Object> producerFactory() {
		Map<String, Object> configs = new HashMap<String, Object>(); // 参数
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
		configs.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
		configs.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
		configs.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
		configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerBufferMemory);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
		// configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
		// ObjectSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);

		return new DefaultKafkaProducerFactory<Object, Object>(configs);
	}

	/**
	 * KafkaTemplate
	 * 
	 * @param producerFactory
	 * @return
	 */
	@Bean
	public KafkaTemplate<Object, Object> kafkaTemplate() {
		return new KafkaTemplate<Object, Object>(producerFactory(), true);
	}

	/**
	 * ConsumerFactory
	 * 
	 * @return
	 */
	@Bean
	public ConsumerFactory<Object, Object> consumerFactory() {
		Map<String, Object> configs = new HashMap<String, Object>(); // 参数
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
		configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerAutoCommitIntervalMs);
		configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
		configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords); // 批量消费数量
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
		// configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
		// ObjectDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class); // 需要把原来的消息删掉，不然会出现反序列化失败的问题

		return new DefaultKafkaConsumerFactory<Object, Object>(configs);
	}

	/**
	 * 添加KafkaListenerContainerFactory，用于批量消费消息
	 * 
	 * @return
	 */
	@Bean
	public KafkaListenerContainerFactory<?> batchContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
		containerFactory.setConsumerFactory(consumerFactory());
		containerFactory.setConcurrency(4);
		containerFactory.setBatchListener(true); // 批量消费
		containerFactory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);

		return containerFactory;
	}
}
