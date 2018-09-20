package org.flume.business.quartz;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * flume log work job
 * 
 * @author sdc
 *
 */
@Component("logWorkJob")
public class LogWorkJob {

	private static Logger LOG = LoggerFactory.getLogger(LogWorkJob.class); 
	
	private static volatile boolean isAuxiliaryWorkJob= false;
	
	/**
	 * 处理数据 
	 * 
	 * @throws Exception
	 */
	public void printLog() throws Exception {
		
		// 当天的日期
		String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		if (isAuxiliaryWorkJob) {
			LOG.info("上个定时任务没有完成,稍后执行 +" + date);
			return;
		}
		
		isAuxiliaryWorkJob = true;

		/***********************log print start*****************************/
		try {
			LOG.info(" print info ");
			LOG.debug(" print debug ");
			LOG.error(" print error ");
		} finally {
			isAuxiliaryWorkJob = false;
		}
		/******************************** log print end ************************/
		
	}
}
