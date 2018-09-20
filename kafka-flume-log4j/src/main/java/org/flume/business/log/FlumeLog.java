package org.flume.business.log;

import org.apache.log4j.Logger;

/**
 * log日志测试
 * 
 * @author sdc
 *
 */
public class FlumeLog {

	private static Logger logger = Logger.getLogger(FlumeLog.class);

	public static void main(String[] args) throws InterruptedException {
		int i = 0;
		while (true) {
			i++;
			logger.warn("flume log output " + i + " time!");
			System.out.println("java system out print info");
			Thread.sleep(1000);
		}
	}

}
