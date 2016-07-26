package com.demo.quartz.jobs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by carlkong on 2016/7/25.
 */
public class HelloJob implements Job {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        System.out.println("任务执行开始时间：" + simpleDateFormat.format(new Date()));
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 此任务仅打印日志便于调试、观察
        System.out.println("任务执行结束时间：" + simpleDateFormat.format(new Date()));
        this.logger.debug(this.getClass().getName() + " trigger...");
    }
}
