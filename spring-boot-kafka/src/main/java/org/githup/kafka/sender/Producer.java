package org.githup.kafka.sender;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import org.w3c.dom.css.Counter;

public class Producer extends Thread {

	/*private final KafkaProducer<Integer, String> prod;

	private final String topic;

	private final Boolean isAsync;

	public Producer(String topic, Boolean isAsync) {
		super();
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.1.90.29:9092");
		props.put("client_id", "");
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		prod = new KafkaProducer<Integer, String>(props);
		this.topic = topic;
		this.isAsync = isAsync;
	}

	@Override
	public void run() {
		super.run();
		int num = 1;
		while(true) {
			String msg = "message_" + num;
			if(isAsync) {
				prod.send(new ProducerRecord<Integer, String>(topic, num ,msg), 
						new Callback() {
							
							@Override
							public void onCompletion(RecordMetadata metadata, Exception exception) {
								// TODO Auto-generated method stub
								System.out.println("#offset:" + metadata.offset());
							}
						});
			}else {//同步发送
				try {
					prod.send(new ProducerRecord<Integer, String>(topic, num ,msg)).get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			++num;
		}
	}
	
	*//***
	 * 分区设置
	 * @param topic
	 * @param key
	 * @param value
	 * @param cluster
	 * @return
	 *//*
	public int partition(String topic, byte[] key ,byte[] value, Cluster cluster) {
		List<PartitionInfo> piList = cluster.partitionsForTopic(topic);
		int numPartition = piList.size();
		if(key == null) {//消息没有key，则均衡分布
			AtomicInteger ai = new AtomicInteger();
			int nextValue = ai.incrementAndGet();
			
			List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
			if(availablePartitions.size() > 0) {
				int part = nextValue % availablePartitions.size();
				return availablePartitions.get(part).partition();
			}
			
		}
		//消息key，对消息的key进行散列化取模
		return Utils.murmur2(key) % numPartition;
	}
	
	public static void main(String[] args) {
		new Producer("test", true).start();
	}*/

}
