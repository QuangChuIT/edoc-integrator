package com.bkav.edoc.web.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocTraceServiceUtil;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;
import com.bkav.edoc.web.util.TokenUtil;
import com.vpcp.services.AgencyServiceImp;
import com.vpcp.services.KnobstickServiceImp;
import com.vpcp.services.VnptProperties;
import com.vpcp.services.model.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceVPCP {
    private static ServiceVPCP INSTANCE;

    private final KnobstickServiceImp knobstickServiceImp;
    private final AgencyServiceImp agencyService;
    private static final VnptProperties vnptProperties;
    private static final String pStart = "---------";

    static public ServiceVPCP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceVPCP();
        }
        return INSTANCE;
    }

    public ServiceVPCP() {
        this.agencyService = new AgencyServiceImp(vnptProperties);
        this.knobstickServiceImp = new KnobstickServiceImp(vnptProperties);
    }

    public void GetAgencies() {
        LOGGER.info("------------------------------ Get Agencies invoke --------------------------------------------------------");
        try {
            GetAgenciesResult getAgenciesResult = this.agencyService.getAgenciesList("{}");
            if (getAgenciesResult != null) {
                LOGGER.info("--------------------------------------------------- " + "status:" + getAgenciesResult.getStatus());
                LOGGER.info("--------------------------------------------------- " + "Desc:" + getAgenciesResult.getErrorDesc());
                LOGGER.info("--------------------------------------------------- " + "Size:" + getAgenciesResult.getAgencies().size());
                List<Agency> agencies = getAgenciesResult.getAgencies();
                int success = 0;
                int error = 0;
                int duplicate = 0;
                if (agencies.size() > 0) {
                    for (Agency agency : agencies) {
                        String domain = agency.getCode();
                        String name = agency.getName();
                        String mail = agency.getMail();
                        if ((domain == null || domain.equals("")) || (name == null || name.equals(""))) {
                            error++;
                        }
                        EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(domain);
                        if (edocDynamicContact != null) {
                            duplicate++;
                        } else {
                            EdocDynamicContact contact = new EdocDynamicContact();
                            contact.setEmail(mail);
                            contact.setDomain(domain);
                            contact.setName(name);
                            String token = TokenUtil.getRandomNumber(domain, name);
                            contact.setToken(token);
                            EdocDynamicContactServiceUtil.createContact(contact);
                            success++;
                        }
                    }
                    String message = "Successfully synchronized organization from VPCP success: " +
                            success + " error: " + error + " duplicate: " + duplicate + " !";
                    LOGGER.info(message);
                } else {
                    LOGGER.warn("Get agencies with size 0 not process !!!!!!");
                }
            } else {
                LOGGER.warn("Get agencies result null not process !!!!!!");
            }
        } catch (Exception e) {
            LOGGER.error("Error synchronized organization from VPCP cause " + Arrays.toString(e.getStackTrace()));
        }
    }

    // Register for organ to VPCP
    public RegisterAgencyResult AgencyRegister(String data) {
        LOGGER.info("----------------------------------Register Agency to VPCP Invoke----------------------");
        RegisterAgencyResult registerAgencyResult = this.agencyService.registerAgency("{}", data);
        try {
            if (registerAgencyResult != null) {
                LOGGER.info("-----------------------------------" + "status:" + registerAgencyResult.getStatus());
                LOGGER.info("-----------------------------------" + "Desc:" + registerAgencyResult.getErrorDesc());
                LOGGER.info("-----------------------------------" + "error code:" + registerAgencyResult.getErrorCode());
            } else {
                LOGGER.warn("Register agency to VPCP not process !!!!!!");
            }
        } catch (Exception e) {
            LOGGER.error("Error to register agency to VPCP cause " + e);
        }
        return registerAgencyResult;
    }

    // Delete register organ to VPCP
    public DeleteAgencyResult DeleteAgencyRegister(String jsonHeader) {
        LOGGER.info("----------------------------------Delete register for agency to VPCP Invoke----------------------");
        DeleteAgencyResult deleteAgencyResult = this.agencyService.deleteAgency(jsonHeader);
        try {
            if (deleteAgencyResult != null) {
                LOGGER.info("-----------------------------------" + "status:" + deleteAgencyResult.getStatus());
                LOGGER.info("-----------------------------------" + "Desc:" + deleteAgencyResult.getErrorDesc());
                LOGGER.info("-----------------------------------" + "error code:" + deleteAgencyResult.getErrorCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error to delete register agency cause " + e);
        }
        return deleteAgencyResult;
    }

    static {
        String vpcpEndpoint = PropsUtil.get("VPCP.endpoint");
        String systemId = PropsUtil.get("VPCP.systemId");
        String token = PropsUtil.get("VPCP.token");
        String attachmentDir = PropsUtil.get("VPCP.attachment.dir");
        int maxConnection = GetterUtil.getInteger(PropsUtil.get("VPCP.maxConnection"));
        int retry = GetterUtil.getInteger(PropsUtil.get("VPCP.retry"));
        vnptProperties = new VnptProperties(vpcpEndpoint, systemId, token, attachmentDir, maxConnection, retry);
    }

    private final static Logger LOGGER = Logger.getLogger(com.bkav.edoc.service.vpcp.ServiceVPCP.class);
}
