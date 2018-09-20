package org.flume.business.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * flume log quartz
 * 
 * @author sdc
 *
 */
public class LogWorkScheduledJob extends QuartzJobBean {

	private Logger LOG = Logger.getLogger(LogWorkScheduledJob.class);

	private LogWorkJob logWorkJob;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			// 开始执行具体的任务
			logWorkJob.printLog();
		} catch (Exception e) {
			LOG.error("日志信息任务报错:", e);
			e.printStackTrace();
		}
	}

	public void setLogWorkJob(LogWorkJob logWorkJob) {
		this.logWorkJob = logWorkJob;
	}

}
