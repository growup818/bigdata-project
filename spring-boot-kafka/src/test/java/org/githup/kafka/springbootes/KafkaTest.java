package org.githup.kafka.springbootes;

import java.util.concurrent.ExecutionException;

import org.githup.kafka.SpringBootKafkaApplication;
import org.githup.kafka.consumer.KafkaConsumer;
import org.githup.kafka.sender.KafkaSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试
 * 
 * @author sdc
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootKafkaApplication.class)
public class KafkaTest {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaTest.class);

	@Autowired
	private KafkaSender kafkaSender;
	
	@Autowired
	private KafkaConsumer kafkaConsumer;
	
	@Test
	public void test1() throws InterruptedException, ExecutionException {
		kafkaSender.sendSync();
	}
	
}
