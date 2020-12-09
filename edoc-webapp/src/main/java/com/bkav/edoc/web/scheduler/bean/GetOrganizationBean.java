package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.vpcp.ServiceVPCP;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("getOrganizationBean")
public class GetOrganizationBean {

    public void runSchedulerGetOrgan() {
        LOGGER.info("Prepare get organization from VPCP at " + DateUtils.format(new Date()));
        ServiceVPCP.getInstance().GetAgencies();
        LOGGER.info("Get organization from vpcp at " + DateUtils.format(new Date()) + " done !!!!!!!!!");
    }

    private static final Logger LOGGER = Logger.getLogger(GetOrganizationBean.class);
}
