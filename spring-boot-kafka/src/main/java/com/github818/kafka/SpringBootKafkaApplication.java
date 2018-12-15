package com.github818.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot starter
 * 
 * 备注：平常测试的类
 * 
 * @author sdc
 *
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.github818"})
public class SpringBootKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaApplication.class, args);
	}
	
}
