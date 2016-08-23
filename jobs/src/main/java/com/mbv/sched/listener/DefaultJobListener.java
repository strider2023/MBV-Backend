package com.mbv.sched.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by manusrivastava on 12/07/14.
 */
public class DefaultJobListener implements JobListener
{
    private static Logger logger = LoggerFactory.getLogger(DefaultJobListener.class);

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    public void jobToBeExecuted(JobExecutionContext context)
    {
        logger.info("Job to be executed : " + context.getJobDetail().getFullName());
    }

    public  void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)
    {
        logger.info("Job was executed : " + context.getJobDetail().getFullName());
        if(jobException != null)
        {
            logger.error("Error executing job : " + context.getJobDetail().getFullName(), jobException.getUnderlyingException());
        }
    }

    public void jobExecutionVetoed(JobExecutionContext context)
    {
        logger.info("Job execution vetoed : " + context.getJobDetail().getFullName());
    }
}

