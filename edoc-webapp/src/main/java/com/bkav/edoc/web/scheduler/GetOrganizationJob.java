package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.GetOrganizationBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class GetOrganizationJob extends QuartzJobBean {

    private GetOrganizationBean getOrganizationBean;

    public void setGetOrganizationBean(GetOrganizationBean getOrganizationBean) {
        this.getOrganizationBean = getOrganizationBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Start get organization from vpcp at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean isRunSchedule = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.GetOrganizationJob"), false);
        if (isRunSchedule) {
            getOrganizationBean.runSchedulerGetOrgan();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(GetOrganizationJob.class);
}
