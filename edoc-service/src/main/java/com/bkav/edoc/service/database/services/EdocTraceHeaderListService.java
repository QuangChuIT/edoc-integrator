package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.EdocDocumentDaoImpl;
import com.bkav.edoc.service.database.daoimpl.EdocTraceHeaderDaoImpl;
import com.bkav.edoc.service.database.daoimpl.EdocTraceHeaderListDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocTraceHeader;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import com.bkav.edoc.service.xml.base.header.Business;
import com.bkav.edoc.service.xml.base.header.StaffInfo;
import com.bkav.edoc.service.xml.base.header.TraceHeader;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EdocTraceHeaderListService {
    private final EdocDocumentDaoImpl documentDaoImpl = new EdocDocumentDaoImpl();
    private final EdocTraceHeaderListDaoImpl traceHeaderListDaoImpl = new EdocTraceHeaderListDaoImpl();
    private final EdocTraceHeaderDaoImpl traceHeaderDaoImpl = new EdocTraceHeaderDaoImpl();

    /**
     * get trace header list by doc id
     *
     * @param documentId
     * @return
     */
    public TraceHeaderList getTraceHeaderListByDocId(long documentId) {
        TraceHeaderList traceHeaderList = null;
        // get list trace
        EdocTraceHeaderList edocTraceHeaderList = traceHeaderListDaoImpl.getTraceHeaderListByDocId(documentId);
        if (edocTraceHeaderList != null) {
            //Hibernate.initialize(edocTraceHeaderList.getTraceHeaders());
            Set<EdocTraceHeader> edocTraceHeaders = edocTraceHeaderList.getTraceHeaders();

            traceHeaderList = new TraceHeaderList();

            Business business = new Business();
            business.setPaper(edocTraceHeaderList.getPaper());
            business.setBusinessDocReason(edocTraceHeaderList.getBusinessDocReason());
            business.setBusinessDocType(edocTraceHeaderList.getBusinessDocType().ordinal());
            String businessInfo = edocTraceHeaderList.getBusinessInfo();
            StaffInfo staffInfo = new StaffInfo();
            staffInfo.setMobile(edocTraceHeaderList.getMobile());
            staffInfo.setStaff(edocTraceHeaderList.getStaff());
            staffInfo.setEmail(edocTraceHeaderList.getEmail());
            staffInfo.setDepartment(edocTraceHeaderList.getDepartment());
            business.setStaffInfo(staffInfo);

            List<TraceHeader> traceHeaders = new ArrayList<>();
            for (EdocTraceHeader edocTraceHeader : edocTraceHeaders) {
                // add trace header
                TraceHeader traceHeader = new TraceHeader();
                traceHeader.setOrganId(edocTraceHeader.getOrganDomain());
                traceHeader.setTimestamp(edocTraceHeader.getTimeStamp());
                traceHeaders.add(traceHeader);
            }

            traceHeaderList.setTraceHeaders(traceHeaders);
            traceHeaderList.setBusiness(business);
            traceHeaderList.setBusinessInfo(businessInfo);
        }
        return traceHeaderList;
    }

    public void createTraceHeaderList(EdocTraceHeaderList edocTraceHeaderList) {
        Session currentSession = traceHeaderListDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            traceHeaderListDaoImpl.persist(edocTraceHeaderList);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            traceHeaderListDaoImpl.closeCurrentSession();
        }
    }

    public void createTraceHeader(EdocTraceHeader edocTraceHeader) {
        Session currentSession = traceHeaderDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            traceHeaderDaoImpl.persist(edocTraceHeader);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            traceHeaderDaoImpl.closeCurrentSession();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocTraceHeaderListService.class);
}
