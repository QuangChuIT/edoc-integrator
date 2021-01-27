package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.StatDocumentBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class StatDocumentJob extends QuartzJobBean {
    private StatDocumentBean statDocumentBean;

    public StatDocumentBean getStatDocumentBean() {
        return statDocumentBean;
    }

    public void setStatDocumentBean(StatDocumentBean statDocumentBean) {
        this.statDocumentBean = statDocumentBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Start daily counter at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean runScheduler = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.daily.counter"), false);
        if(runScheduler){
            LOGGER.info("Run scheduler stat document !!!!!!!!!");
            statDocumentBean.runSchedulerStatDocument();
            LOGGER.info("End scheduler stat document !!!!!!!!!");
        }
    }

    public static void main(String[] args) {
        StatDocumentBean statDocumentBean = new StatDocumentBean();
        statDocumentBean.runSchedulerStatDocument();
    }

    private static final Logger LOGGER = Logger.getLogger(StatDocumentJob.class);
}
