package com.demo;


import com.demo.quartz.jobs.HelloJob;
import com.demo.quartz.utils.QuartzManager;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Unit test for simple App.
 */
public class AppTest {
    Logger logger = LoggerFactory.getLogger(AppTest.class);
    @Test
    public void test1(){
        try {
            // 获取Scheduler实例
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // 具体任务
            JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(2).withRepeatCount(6);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                    .startNow().withSchedule(simpleScheduleBuilder).build();

            // 交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            /* 为观察程序运行，此设置主程序睡眠1分钟才继续往下运行（因下一个步骤是“关闭Scheduler”） */
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 关闭Scheduler
            scheduler.shutdown();

        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        }
    }
    @Test
    public void test2(){
        try{
            logger.info("------- Initializing ----------------------");

            // First we must get a reference to a scheduler
            //从调度管理器中获取调度对象
            Scheduler sched = QuartzManager.getInstanceScheduler();
            logger.info("------- Initialization Complete -----------");

            // computer a time that is on the next round minute
            Date runTime = evenMinuteDate(new Date());

            logger.info("------- Scheduling Job  -------------------");

            // define the job and tie it to our HelloJob class
            //创建相关的job信息
            JobDetail job = newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // Trigger the job to run on the next round minute
            //创建一个触发器的名称
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startAt(runTime)
                    .build();

            // Tell quartz to schedule the job using our trigger
            //设置调度相关的Job
            sched.scheduleJob(job, trigger);
            logger.info(job.getKey() + " will run at: " + runTime);

            // Start up the scheduler (nothing can actually run until the
            // scheduler has been started)
            //启动调度任务
            sched.start();

            logger.info("------- Started Scheduler -----------------");

//        try {
//            Thread.sleep(25L * 1000L);
//            // executing...
//        } catch (Exception e) {
//        }
//        //暂时停止Job任务开始执行
//        log.info("-------pauseJob.. -------------");
//        sched.pauseJob(job.getKey());
//
//        try {
//            Thread.sleep(10L * 1000L);
//        } catch (Exception e) {
//        }
            logger.info("------- resumeJob... -------------");
            //恢复Job任务开始执行
            sched.resumeJob(job.getKey());
            try {
                Thread.sleep(10L * 1000L);
                // executing...
            } catch (Exception e) {
            }


            // wait long enough so that the scheduler as an opportunity to
            // run the job!
            logger.info("------- Waiting 65 seconds... -------------");
            try {
                // wait 65 seconds to show job
                Thread.sleep(65L * 1000L);
                // executing...
            } catch (Exception e) {
            }

            // shut down the scheduler
            logger.info("------- Shutting Down ---------------------");
            sched.shutdown(true);
            logger.info("------- Shutdown Complete -----------------");

        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    @Test
    public void test3(){
        int a = 1;
        long startTime=System.currentTimeMillis();   //获取开始时间
        //测试的代码段
        for (int i = 0; i < 18000; i++) {
            a = (i+1)*1;
        }
        System.out.println(a);
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }
}
