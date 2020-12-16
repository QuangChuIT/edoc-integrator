package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDailyCounterDaoImpl;
import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EdocDailyCounterService {
    private final EdocDailyCounterDaoImpl edocDailyCounterDao = new EdocDailyCounterDaoImpl();
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();

    public boolean checkExistCounter(Date date) {
        return edocDailyCounterDao.checkExistCounter(date);
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        edocDailyCounterDao.createDailyCounter(dailyCounter);
    }

    public List<EPublicStat> getStatsDetail(String organDomain, Date fromDate, Date toDate) {
        List<EPublicStat> ePublicStats = new ArrayList<>();

        Session session = edocDailyCounterDao.openCurrentSession();
        try {
            List<OrganizationCacheEntry> contacts = edocDynamicContactService.getDynamicContactsByFilterDomain(organDomain);
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
                ePublicStat.setOrganDomain(organDomain);
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

    public EPublic getStat(String organDomain) {
        EPublic ePublic = new EPublic();
        Long total = edocDailyCounterDao.getStat();
        ePublic.setTotal(total);
        ePublic.setTotalOrgan(edocDynamicContactService.countOrgan(organDomain));
        ePublic.setDateTime(DateUtils.format(new Date(), DateUtils.VN_DATETIME_FORMAT_NEW));
        return ePublic;
    }

    private final static Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
