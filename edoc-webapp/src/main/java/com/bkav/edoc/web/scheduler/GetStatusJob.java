package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.GetStatusVPCPBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class GetStatusJob extends QuartzJobBean {

    private GetStatusVPCPBean getStatusVPCPBean;

    public void setGetStatusVPCPBean(GetStatusVPCPBean getStatusVPCPBean) {
        this.getStatusVPCPBean = getStatusVPCPBean;
    }

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     *
     * @param context
     * @see #execute
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Start get status from vpcp at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean isRunSchedule = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.GetStatusJob"), false);
        if (isRunSchedule) {
            getStatusVPCPBean.runSchedulerGetStatus();
        }
    }
    
    private static final Logger LOGGER = Logger.getLogger(GetDocumentJob.class);
}
