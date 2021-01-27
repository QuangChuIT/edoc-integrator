package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDailyCounterDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.*;

public class EdocDailyCounterService {
    private final EdocDailyCounterDaoImpl edocDailyCounterDao = new EdocDailyCounterDaoImpl();
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();

    public boolean checkExistCounter(Date date) {
        return edocDailyCounterDao.checkExistCounter(date);
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        edocDailyCounterDao.createDailyCounter(dailyCounter);
    }

    public List<EPublicStat> getStatsDetail(Date fromDate, Date toDate) {
        List<EPublicStat> ePublicStats = new ArrayList<>();

        Session session = edocDailyCounterDao.openCurrentSession();
        try {
            List<OrganizationCacheEntry> contacts = edocDynamicContactService.getDynamicContactsByAgency(true);
            for (OrganizationCacheEntry contact : contacts) {
                String organId = contact.getDomain();
                StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery("GetStat");
                storedProcedureQuery.registerStoredProcedureParameter("fromDate", java.sql.Date.class, ParameterMode.IN);
                storedProcedureQuery.registerStoredProcedureParameter("toDate", java.sql.Date.class, ParameterMode.IN);
                storedProcedureQuery.registerStoredProcedureParameter("organId", String.class, ParameterMode.IN);
                storedProcedureQuery.registerStoredProcedureParameter("totalSent", Integer.class, ParameterMode.OUT);
                storedProcedureQuery.registerStoredProcedureParameter("totalReceived", Integer.class, ParameterMode.OUT);
                if(fromDate == null || toDate == null){
                    java.sql.Date date = null;
                    storedProcedureQuery.setParameter("fromDate", date);
                    storedProcedureQuery.setParameter("toDate", date);
                } else {
                    storedProcedureQuery.setParameter("fromDate", fromDate);
                    storedProcedureQuery.setParameter("toDate", toDate);
                }

                storedProcedureQuery.setParameter("organId", organId);
                int sent = 0;
                int received = 0;
                if(storedProcedureQuery.getOutputParameterValue("totalSent") != null){
                    sent = (Integer) storedProcedureQuery.getOutputParameterValue("totalSent");
                }
                if( storedProcedureQuery.getOutputParameterValue("totalReceived") != null){
                    received = (Integer) storedProcedureQuery.getOutputParameterValue("totalReceived");
                }
                EPublicStat ePublicStat = new EPublicStat();
                ePublicStat.setLastUpdate(new Date());
                ePublicStat.setOrganDomain(organId);
                ePublicStat.setOrganName(contact.getName());
                ePublicStat.setSent(sent);
                ePublicStat.setReceived(received);
                long total = sent + received;
                ePublicStat.setTotal(total);
                ePublicStats.add(ePublicStat);
            }
        } catch (Exception e) {
            LOGGER.error("Error get stat document detail cause " + e);
        } finally {
            edocDailyCounterDao.closeCurrentSession(session);
        }
        return ePublicStats;
    }

    public EPublic getStat() {
        EPublic ePublic = new EPublic();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Long total = edocDailyCounterDao.getStat(year);
        ePublic.setTotal(total);
        ePublic.setTotalOrgan(edocDynamicContactService.countOrgan(true));
        ePublic.setDateTime(DateUtils.format(new Date(), DateUtils.VN_DATETIME_FORMAT_NEW));
        return ePublic;
    }

    public List<String> getSentReceivedDocByYear(String year) {
        Session session = edocDailyCounterDao.openCurrentSession();
        try {
            StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery("GetSentReceivedDocument");
            storedProcedureQuery.registerStoredProcedureParameter("year", String.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("year", year);
            List list = storedProcedureQuery.getResultList();
            List<String> result = new ArrayList<>();

            for (Object o: list) {
                result.add(new Gson().toJson(o));
            }

            return result;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            edocDailyCounterDao.closeCurrentSession(session);
        }
    }

    public static void main(String[] args) {
        EdocDailyCounterService edocDailyCounterService = new EdocDailyCounterService();
        System.out.println(edocDailyCounterService.getStat().getTotal());
    }

    private final static Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
