package com.bkav.edoc.service.util;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import com.bkav.edoc.service.xml.base.header.BusinessDocumentInfo;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.ReplacementInfoList;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
    private static final Gson gson = new Gson();

    public static List<Long> convertToListLong(List list) {
        if (list == null) return null;
        List<Long> result = new ArrayList<>();
        for (Object item : list) {
            Double value = Double.parseDouble(item.toString());
            result.add(value.longValue());
        }
        return result;
    }

    public static List<EdocTrace> convertToListTrace(List list) {
        if (list == null) return null;
        List<EdocTrace> result = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof EdocTrace) {
                result.add((EdocTrace) item);
            }
        }
        return result;
    }

    public static List<AttachmentCacheEntry> convertToListAttachment(List list) {
        if (list == null) return null;
        List<AttachmentCacheEntry> result = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof AttachmentCacheEntry) {
                result.add((AttachmentCacheEntry) item);
            }
        }
        return result;
    }

    public static List<Organization> convertToOrganization(List list) {
        if (list == null) {
            return null;
        } else {
            List<Organization> organizations = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Organization) {
                    organizations.add((Organization) item);
                }
            }
            return organizations;
        }
    }

    /**
     * get to organ domain from list tos
     *
     * @param toes - list to organ
     * @return String
     */
    public static String getToOrganDomain(List<Organization> toes) {

        StringBuilder toOrganDomainBuilder = new StringBuilder();

        for (int i = 0; i < toes.size(); i++) {
            toOrganDomainBuilder.append(toes.get(i).getOrganId());

            if (toes.size() > 1 && i < toes.size() - 1) {
                toOrganDomainBuilder.append("#");
            }

        }
        return toOrganDomainBuilder.toString();
    }

    /**
     * @param traceHeaderList - trace header list object
     * @return businessInfo
     */
    public static String getBusinessInfo(TraceHeaderList traceHeaderList) {
        if (traceHeaderList.getBusiness() == null) return null;
        String businessInfo = null;
        // check business doc type
        long businessDocType = traceHeaderList.getBusiness().getBusinessDocType();
        if (businessDocType == EdocTraceHeaderList.BusinessDocType.UPDATE.ordinal()) {
            // get business document info
            BusinessDocumentInfo businessDocumentInfo = traceHeaderList.getBusiness().getBusinessDocumentInfo();
            businessInfo = gson.toJson(businessDocumentInfo);
        } else if (businessDocType == EdocTraceHeaderList.BusinessDocType.REPLACE.ordinal()) {
            // get replacement info
            ReplacementInfoList replacementInfoList = traceHeaderList.getBusiness().getReplacementInfoList();
            businessInfo = gson.toJson(replacementInfoList);
        }

        return businessInfo;
    }
}
