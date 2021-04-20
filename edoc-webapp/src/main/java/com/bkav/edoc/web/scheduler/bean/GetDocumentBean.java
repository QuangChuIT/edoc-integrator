package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.vpcp.ServiceVPCP;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("getDocumentBean")
public class GetDocumentBean {

    public void runSchedulerGetDocument() {
        LOGGER.info("Prepare get documents from vpcp at " + DateUtils.format(new Date()));
        /*ServiceVPCP.getInstance().GetDocuments();*/
        LOGGER.info("Get documents from vpcp at " + DateUtils.format(new Date()) + " done !!!!!!!!!");
    }

    private static final Logger LOGGER = Logger.getLogger(GetDocumentBean.class);
}
