package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.GetDocumentBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class GetDocumentJob extends QuartzJobBean {

    private GetDocumentBean getDocumentBean;

    public void setGetDocumentBean(GetDocumentBean getDocumentBean) {
        this.getDocumentBean = getDocumentBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Start get document from vpcp at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean isRunSchedule = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.GetDocumentJob"), false);
        if (isRunSchedule) {
            getDocumentBean.runSchedulerGetDocument();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(GetDocumentJob.class);
}

