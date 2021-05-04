package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDailyCounterDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.*;
import java.util.stream.Collectors;

public class EdocDailyCounterService {
    private final EdocDailyCounterDaoImpl edocDailyCounterDao = new EdocDailyCounterDaoImpl();
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();
    private final EdocDocumentService edocDocumentService = new EdocDocumentService();

    private int vpubnd_sent = 0, vpubnd_received = 0;
    private String vpubndName = "";

    public boolean checkExistCounter(Date date) {
        return edocDailyCounterDao.checkExistCounter(date);
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        edocDailyCounterDao.createDailyCounter(dailyCounter);
    }

    public List<EPublicStat> getStatsDetail(Date fromDate, Date toDate, String keyword, boolean isGetAllAgency) {
        List<EPublicStat> ePublicStats = new ArrayList<>();
        vpubnd_sent = 0;
        vpubnd_received = 0;
        vpubndName = "";
        boolean hasKeyword = false;
        List<OrganizationCacheEntry> contacts;

        Session session = edocDailyCounterDao.openCurrentSession();
        try {
            if (isGetAllAgency) {
                contacts = edocDynamicContactService.getDynamicContactsByAgency(true);
            } else {
                if (keyword == null) {
                    contacts = edocDynamicContactService.getLevel3Contact();
                }
                else {
                    hasKeyword = true;
                    contacts = edocDynamicContactService.getOrganByKeyword(keyword);
                }
            }
            contacts = contacts.stream().filter(o -> !((o.getDomain().charAt(10)) == 'A')).collect(Collectors.toList());

            for (OrganizationCacheEntry contact : contacts) {
                String organId = contact.getDomain();

                EPublicStat parentOrgan = callStatStoredProcedure(fromDate, toDate, session, contact);
                //Set<OrganizationCacheEntry>

                if (parentOrgan == null)
                    continue;

                String regex = "." + organId.split("\\.")[2] +
                        "." + organId.split("\\.")[3];

                List<OrganizationCacheEntry> childOrgan = edocDynamicContactService.getOrganByKeyword(regex);

                Set<EPublicStat> childOrganStatSet = new HashSet<>();

                for (OrganizationCacheEntry child : childOrgan) {
                    String childDomain = child.getDomain();
                    if (!childDomain.equals(organId)) {
                        EPublicStat childOrganStat = callStatStoredProcedure(fromDate, toDate, session, child);
                        childOrganStatSet.add(childOrganStat);
                    }
                }

                parentOrgan.setChildOrgan(childOrganStatSet);
                ePublicStats.add(parentOrgan);
            }
            if (!hasKeyword || keyword.equals(PropsUtil.get("edoc.domain.vpubnd.1"))) {
                EPublicStat ePublicStat = new EPublicStat();
                ePublicStat.setLastUpdate(new Date());
                ePublicStat.setOrganDomain(PropsUtil.get("edoc.domain.vpubnd.1"));
                ePublicStat.setOrganName(vpubndName);
                ePublicStat.setSent(vpubnd_sent);
                ePublicStat.setReceived(vpubnd_received);
                long total = vpubnd_sent + vpubnd_received;
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

    /*public List<EPublicStat> getStatsDetail(Date fromDate, Date toDate, String keyword) {
        List<EPublicStat> ePublicStats = new ArrayList<>();
        int vpubnd_sent = 0;
        int vpubnd_received = 0;
        String vpubndName = "";
        boolean hasKeyword = false;

        Session session = edocDailyCounterDao.openCurrentSession();
        List<OrganizationCacheEntry> contacts;
        try {
            //List<OrganizationCacheEntry> contacts = edocDynamicContactService.getDynamicContactsByAgency(true);

            if (keyword == null) {
                contacts = edocDynamicContactService.getDynamicContactsByAgency(true);
                //contacts = edocDynamicContactService.getLevel3Contact();
            } else {
                contacts = edocDynamicContactService.getOrganByKeyword(keyword);
                hasKeyword = true;
            }
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
                if (contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.0")) ||
                        contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.1"))) {
                    if (contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.1")))  {
                        vpubndName += contact.getName();
                    }
                    vpubnd_sent += sent;
                    vpubnd_received += received;
                } else {
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
            }
            if (!hasKeyword || keyword.equals(PropsUtil.get("edoc.domain.vpubnd.1"))) {
                EPublicStat ePublicStat = new EPublicStat();
                ePublicStat.setLastUpdate(new Date());
                ePublicStat.setOrganDomain(PropsUtil.get("edoc.domain.vpubnd.1"));
                ePublicStat.setOrganName(vpubndName);
                ePublicStat.setSent(vpubnd_sent);
                ePublicStat.setReceived(vpubnd_received);
                long total = vpubnd_sent + vpubnd_received;
                ePublicStat.setTotal(total);
                ePublicStats.add(ePublicStat);
            }
        } catch (Exception e) {
            LOGGER.error("Error get stat document detail cause " + e);
        } finally {
            edocDailyCounterDao.closeCurrentSession(session);
        }
        return ePublicStats;
    }*/

    private EPublicStat callStatStoredProcedure (Date fromDate, Date toDate, Session session, OrganizationCacheEntry contact) {

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

        storedProcedureQuery.setParameter("organId", contact.getDomain());
        int sent = 0;
        int received = 0;
        if(storedProcedureQuery.getOutputParameterValue("totalSent") != null){
            sent = (Integer) storedProcedureQuery.getOutputParameterValue("totalSent");
        }
        if( storedProcedureQuery.getOutputParameterValue("totalReceived") != null){
            received = (Integer) storedProcedureQuery.getOutputParameterValue("totalReceived");
        }
        if (contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.0")) ||
                contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.1"))) {
            vpubnd_sent += sent;
            vpubnd_received += received;
            if (contact.getDomain().equals(PropsUtil.get("edoc.domain.vpubnd.1")))  {
                vpubndName = contact.getName();
            }
            return null;
        } else {
            EPublicStat ePublicStat = new EPublicStat();
            ePublicStat.setLastUpdate(new Date());
            ePublicStat.setOrganDomain(contact.getDomain());
            ePublicStat.setOrganName(contact.getName());
            ePublicStat.setSent(sent);
            ePublicStat.setReceived(received);
            long total = sent + received;
            ePublicStat.setTotal(total);
            return ePublicStat;
        }
    }

    public List<EdocStatisticDetail> getStatisticSentReceivedExtDetail(String fromDate, String toDate, String organDomain) {
        Map<String, EdocStatisticDetail> dailyCounterMap = new HashMap<>();
        List<EdocStatisticDetail> edocStatDetails = new ArrayList<>();

        if (organDomain != null) {
            EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(organDomain);
            if (contact.getAgency()) {
                LOGGER.info("Start count with organ " + organDomain + "!!!!!!!!!!");
                EdocStatisticDetail edocStatisticDetail = new EdocStatisticDetail();
                int receivedExt = edocDocumentService.countReceivedExtDocument(fromDate, toDate, true, organDomain);
                edocStatisticDetail.setOrganDomain(organDomain);
                edocStatisticDetail.setReceived_ext(receivedExt);

                int receivedInt = edocDocumentService.countReceivedExtDocument(fromDate, toDate, false, organDomain);
                edocStatisticDetail.setReceived_int(receivedInt);
                int totalRecived = receivedExt + receivedInt;
                edocStatisticDetail.setTotal_received(totalRecived);
                LOGGER.info("Total received: " + totalRecived);

                List<Long> listDocCode = edocDocumentService.getDocCodeByOrganDomain(fromDate, toDate, organDomain);
                LOGGER.info("Has " + listDocCode.size() + " documents!!!!");
                /*for (long doc_code: listDocCode) {
                    LOGGER.info("Start at documentID " + doc_code);
                    if (edocAttachmentService.checkSignedAttachment(doc_code)) {
                        int signed = edocStatisticDetail.getSigned() + 1;
                        edocStatisticDetail.setSigned(signed);
                    } else {
                        int not_signed = edocStatisticDetail.getNot_signed() + 1;
                        edocStatisticDetail.setNot_signed(not_signed);
                    }
                }

                 */
                edocStatDetails.add(edocStatisticDetail);
            }
        } else {
            List<EdocDynamicContact> organs = EdocDynamicContactServiceUtil.getDynamicContactByAgency(true);

            for (EdocDynamicContact organ: organs) {
                String domain = organ.getDomain();
                LOGGER.info("Start count with organ " + domain + "!!!!");
                EdocStatisticDetail edocStatDetail = new EdocStatisticDetail();

                int receivedExt = edocDocumentService.countReceivedExtDocument(fromDate, toDate, true, domain);
                edocStatDetail.setOrganDomain(domain);
                edocStatDetail.setReceived_ext(receivedExt);

                int receivedInt = edocDocumentService.countReceivedExtDocument(fromDate, toDate, false, domain);
                edocStatDetail.setReceived_int(receivedInt);
                int totalRecived = receivedExt + receivedInt;
                edocStatDetail.setTotal_received(totalRecived);
                LOGGER.info("Total received: " + totalRecived);

                List<Long> listDocCode = edocDocumentService.getDocCodeByOrganDomain(fromDate, toDate, domain);
                LOGGER.info("Has " + listDocCode.size() + " documents!!!!");
                /*for (long doc_code: listDocCode) {
                    LOGGER.info("Start at documentID " + doc_code);
                    if (edocAttachmentService.checkSignedAttachment(doc_code)) {
                        int signed = edocStatDetail.getSigned() + 1;
                        edocStatDetail.setSigned(signed);
                    } else {
                        int not_signed = edocStatDetail.getNot_signed() + 1;
                        edocStatDetail.setNot_signed(not_signed);
                    }
                }

                 */
                dailyCounterMap.put(domain, edocStatDetail);
                LOGGER.info("End count organ domain " + domain);
            }
            for (Map.Entry<String, EdocStatisticDetail> entry : dailyCounterMap.entrySet()) {
                EdocStatisticDetail dailyStat = entry.getValue();
                edocStatDetails.add(dailyStat);
            }
        }

        return edocStatDetails;
    }

    public static void main(String[] args) {
        EdocDailyCounterService edocDailyCounterService = new EdocDailyCounterService();
        Date fromDateValue = DateUtils.parse("01-03-2021");
        Date toDateValue = DateUtils.parse("13-03-2021");
        //System.out.println(new Gson().toJson(edocDailyCounterService.getStatisticSentReceivedExtDetail("2020-12-01", "2020-12-31", null)));
        System.out.println(new Gson().toJson(edocDailyCounterService.getStatsDetail(fromDateValue, toDateValue, null, false)));
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

    public String getSentReceivedForChart(int year, String organDomain) {
        Map<String, List<Long>> map = new HashMap<>();
        List<Long> sent, moreSent;
        List<Long> received, moreReceived;
        sent = edocDailyCounterDao.getSentByMonth(year, organDomain);
        received = edocDailyCounterDao.getReceivedByMonth(year, organDomain);
        /*if (organDomain.equals(PropsUtil.get("edoc.domain.vpubnd.0"))) {
            moreSent = edocDailyCounterDao.getSentByMonth(year, PropsUtil.get("edoc.domain.vpubnd.1"));
            sent.addAll(moreSent);
            moreReceived = edocDailyCounterDao.getSentByMonth(year, PropsUtil.get("edoc.domain.vpubnd.1"));
            received.addAll(moreReceived);
        }*/
        map.put("sent", sent);
        map.put("received", received);
        return new Gson().toJson(map);
    }

    private final static Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
