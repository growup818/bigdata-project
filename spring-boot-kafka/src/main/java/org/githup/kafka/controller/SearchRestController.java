package org.githup.kafka.controller;

import org.githup.kafka.constants.KafkaWebStatusEnum;
import org.githup.kafka.constants.ResponseVo;
import org.githup.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka服务
 * 
 * @author sdc
 *
 */
@RestController
@RequestMapping("/search")
public class SearchRestController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(SearchRestController.class);

	@RequestMapping(value = "/test")
	public ResponseVo<?> test() throws Exception{
		Message message = new Message();
		//判空
		return generateResponseVo(KafkaWebStatusEnum.SUCCESS, null);
	}
	
}
