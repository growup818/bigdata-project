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
import org.springframework.scheduling.annotation.EnableAsync;

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

	@Value("${spring.kafka.producer.buffer-memory}")
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
		//服务器端口配置
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
		// 如果请求失败，生产者会自动重试
		configs.put(ProducerConfig.RETRIES_CONFIG, producerRetries);
		// Request发送请求，即Batch批处理，以减少请求次数，该值即为每次批处理的大小
		configs.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
		/**
		 * 这将指示生产者发送请求之前等待一段时间，希望更多的消息填补到未满的批中。这类似于TCP的算法，例如上面的代码段，
		 * 可能100条消息在一个请求发送，因为我们设置了linger(逗留)时间为1毫秒，然后，如果我们没有填满缓冲区，
		 * 这个设置将增加1毫秒的延迟请求以等待更多的消息。 需要注意的是，在高负载下，相近的时间一般也会组成批，即使是
		 * linger.ms=0。在不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
		 */
		configs.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
		/**
         * 控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。
         * 当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值通过max.block.ms设定， 之后它将抛出一个TimeoutException。
         */
		configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, producerBufferMemory);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
		// configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
		// ObjectSerializer.class);
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);

		// 0：不保证消息的到达确认，只管发送，低延迟但是会出现消息的丢失，在某个server失败的情况下，有点像TCP
        // 1：发送消息，并会等待leader 收到确认后，一定的可靠性
        // -1：发送消息，等待leader收到确认，并进行复制操作后，才返回，最高的可靠性
        //props.put(ProducerConfig.ACKS_CONFIG, "0");
        
		// 用于配置send数据或partitionFor函数得到对应的leader时，最大的等待时间，默认值为60秒
        // HH 警告：如无法连接 kafka 会导致程序卡住，尽量不要设置等待太久
        //props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 100);

        // 消息发送的最长等待时间
        //props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 100);
		
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
