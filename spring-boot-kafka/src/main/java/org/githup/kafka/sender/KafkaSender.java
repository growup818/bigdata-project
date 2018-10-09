package org.githup.kafka.sender;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.githup.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 发送服务
 * @author sdc
 *
 */
@Component
public class KafkaSender {
 
	private static final Logger log = LoggerFactory.getLogger(KafkaSender.class);

	@Autowired
	private KafkaTemplate<Object, Object> kafkaTemplate;

	/**
	 * 同步发送数据
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void sendSync() throws InterruptedException, ExecutionException {
		Message message = new Message();
		message.setId(System.currentTimeMillis());
		message.setMsg(UUID.randomUUID().toString());
		message.setSendTime(new Date());
		log.info("+++++++++ message = {}", JSONObject.toJSONString(message));
		
		Future<SendResult<Object, Object>> future = kafkaTemplate.send("kafka-queue", JSONObject.toJSONString(message));
		try {
			//最多等待两秒时间，超过两秒则报异常信息
			Object value = future.get(2, TimeUnit.SECONDS);
			log.info(" +++++++++++ future send result object " + JSONObject.toJSONString(value));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
