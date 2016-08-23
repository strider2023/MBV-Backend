package com.mbv.sched.quartz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MBVScheduler
{
    private static MBVScheduler mbvScheduler = new MBVScheduler();

    private ApplicationContext schedulerContext;

    private MBVScheduler()
    {
        schedulerContext = new ClassPathXmlApplicationContext("scheduler-context.xml");
    }

    public static MBVScheduler getInstance()
    {
        return mbvScheduler;
    }

    public static void main(String args[]) throws Exception{
        MBVScheduler.getInstance();
    }
}
