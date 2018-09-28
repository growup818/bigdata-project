package org.githup.kafka.sender;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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

	private KafkaProducer<String, String> kafkaProducer;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void send() throws InterruptedException, ExecutionException {
		Message message = new Message();
		message.setId(System.currentTimeMillis());
		message.setMsg(UUID.randomUUID().toString());
		message.setSendTime(new Date());
		log.info("+++++++++ message = {}", JSONObject.toJSONString(message));
		kafkaTemplate.send("test", JSONObject.toJSONString(message));
	}

	public static void main(String[] args) {
		int[] a = new int[] { 1, 2, 3, 4, 5, 6 };
		int[] b = new int[] { 24, 5, 17, 9, 111, 12, 67 };
//		int[] c = mergeArray(a, b);
		int[] c = mergeSort(b);
		for (int i = 0; i < c.length; i++)
			System.out.println(c[i]);
	}

	// 合并排序
	public static int[] mergeArray(int[] a, int[] b) {
		int[] c = new int[a.length + b.length];
		int i = 0, j = 0, k = 0;
		while (i < a.length && j < b.length) {
			if (a[i] <= b[j]) {
				c[k++] = a[i++];
			} else {
				c[k++] = b[j++];
			}
		}

		// 如果两个数组不等长度，就会有一个多出来
		while (i < a.length) {
			c[k++] = a[i++];
		}
		while (j < b.length) {
			c[k++] = b[j++];
		}
		return c;

	}
	
	//归并排序
	public static int[] mergeSort(int[] arr) {
		if(arr.length <=1 )
			return arr;
		int num = arr.length >> 1;
		int[] leftArr = Arrays.copyOfRange(arr, 0, num);
		int[] rightArr = Arrays.copyOfRange(arr, num, arr.length);
		return mergeArray(mergeSort(leftArr), mergeSort(rightArr));
	}
	
	

}
