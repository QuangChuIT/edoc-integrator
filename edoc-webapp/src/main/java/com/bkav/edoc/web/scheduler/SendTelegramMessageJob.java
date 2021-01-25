package com.bkav.edoc.web.scheduler;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.scheduler.bean.SendMessageToTelegramBean;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class SendTelegramMessageJob extends QuartzJobBean {
    private SendMessageToTelegramBean sendTelegramMessageBean;

    public void setSendTelegramMessageBean(SendMessageToTelegramBean sendTelegramMessageBean) {
        this.sendTelegramMessageBean = sendTelegramMessageBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Check send telegram message at " + DateUtils.format(new Date(), DateUtils.DEFAULT_DATETIME_FORMAT));
        boolean runScheduler = GetterUtil.getBoolean(PropsUtil.get("edoc.app.schedule.run.daily.send.telegram"), true);
        if(runScheduler){
            LOGGER.info("Run scheduler send telegram message !!!!!!!!!");
            sendTelegramMessageBean.runScheduleSendMessageToTelegram();
            LOGGER.info("End scheduler send telegram message !!!!!!!!!");
        }
    }

    private static final Logger LOGGER = Logger.getLogger(SendTelegramMessageJob.class);
}
