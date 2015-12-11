/**
 * Dec 10, 2015
 * ClassJob.java
 * 2015 2:30:39 PM
 * Created By tian
 */
package com.demo.jfinal.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author tian
 * @Time Dec 10, 2015 2:30:39 PM
 */
public class JobDemo implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("JobDemo开始执行啦" + System.currentTimeMillis());
	}

}
