package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.vpcp.ServiceVPCP;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("getStatusVPCPBean")
public class GetStatusVPCPBean {

    public void runSchedulerGetStatus() {
        LOGGER.info("Prepare get sts from vpcp at " + DateUtils.format(new Date()));
        ServiceVPCP.getInstance().getStatus();
        LOGGER.info("Get status from vpcp at " + DateUtils.format(new Date()) + " done !!!!!!!!!");
    }

    private static final Logger LOGGER = Logger.getLogger(GetStatusVPCPBean.class);
}
