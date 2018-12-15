package com.github818.kafka.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github818.kafka.sender.KafkaProducer;

@Controller
@Slf4j
public class TestController {
	
    @Autowired
    private KafkaProducer kafkaProducer;

    @RequestMapping("/test")
    @ResponseBody
    public void test() {
        kafkaProducer.sendMessage("topic-1", "topic--------1");
        kafkaProducer.sendMessage("topic-2", "topic--------2");
    }
}