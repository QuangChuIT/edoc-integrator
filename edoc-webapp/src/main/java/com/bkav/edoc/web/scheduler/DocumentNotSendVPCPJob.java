package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.DocumentNotSendVPCPBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class DocumentNotSendVPCPJob extends QuartzJobBean {
    private DocumentNotSendVPCPBean sendTelegramDocumentVPCPBean;

    public void setSendTelegramDocumentVPCPBean(DocumentNotSendVPCPBean sendTelegramDocumentVPCPBean) {
        this.sendTelegramDocumentVPCPBean = sendTelegramDocumentVPCPBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Check send telegram message for document not send VPCP at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean runScheduler = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.daily.send.telegram.vpcp"), true);
        if(runScheduler){
            LOGGER.info("Run scheduler send telegram message for document not send VPCP !!!!!!!!!");
            sendTelegramDocumentVPCPBean.runScheduleDocumentNotSendVPCP();
            LOGGER.info("End scheduler send telegram for document not send message !!!!!!!!!");
        }
    }

    private static final Logger LOGGER = Logger.getLogger(DocumentNotSendVPCPJob.class);
}
