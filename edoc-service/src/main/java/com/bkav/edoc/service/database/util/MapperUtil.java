package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.cache.*;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.services.EdocDocumentService;
import com.bkav.edoc.service.database.services.EdocDynamicContactService;
import com.bkav.edoc.service.database.services.EdocPriorityService;
import com.bkav.edoc.service.kernel.util.StringUtil;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.xml.base.header.ResponseFor;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.header.OtherInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

public class MapperUtil {
    private static final EdocDynamicContactService EDOC_DYNAMIC_CONTACT_SERVICE = new EdocDynamicContactService();
    private static final EdocPriorityService EDOC_PRIORITY_SERVICE = new EdocPriorityService();
    private static final AttachmentGlobalUtil ATTACHMENT_GLOBAL_UTIL = new AttachmentGlobalUtil();
    private static final EdocDocumentService EDOC_DOCUMENT_SERVICE = new EdocDocumentService();
    private final static String SEPARATOR = File.separator;
    private final static Gson gson = new Gson();

    public static DocumentCacheEntry updateDocument(EdocDocument document) {
        LOGGER.info("Update code for document with id " + document.getDocumentId());
        String codeNation = document.getCodeNotation();
        if (codeNation.contains("#")) {
            codeNation = codeNation.substring(0, codeNation.indexOf("#"));
        }
        String codeNumber = document.getCodeNumber();
        String code = codeNumber + "/" + codeNation;
        document.setDocCode(code);
        EDOC_DOCUMENT_SERVICE.updateDocument(document);
        LOGGER.info("Update code success for document with id " + document.getDocumentId());
        return new DocumentCacheEntry();
    }

    public static DocumentCacheEntry documentToCached(EdocDocument document) {
        try {
            DocumentCacheEntry documentCacheEntry = new DocumentCacheEntry();
            documentCacheEntry.setDocumentId(document.getDocumentId());
            documentCacheEntry.setEdXMLDocId(document.getEdXMLDocId());
            documentCacheEntry.setCreateDate(document.getCreateDate());
            documentCacheEntry.setModifiedDate(document.getModifiedDate());
            documentCacheEntry.setSubject(document.getSubject());
            documentCacheEntry.setPromulgationDate(document.getPromulgationDate());
            documentCacheEntry.setPromulgationPlace(document.getPromulgationPlace());
            documentCacheEntry.setDocumentType(document.getDocumentType());
            documentCacheEntry.setDocumentTypeName(document.getDocumentTypeName());
            documentCacheEntry.setSendExt(document.getSendExt());
            documentCacheEntry.setVisited(document.getVisited());
            String codeNation = document.getCodeNotation();
            if (codeNation.contains("#")) {
                codeNation = codeNation.substring(0, codeNation.indexOf("#"));
            }
            documentCacheEntry.setShortenSubject(StringUtil.shorten(document.getSubject(), 90));
            documentCacheEntry.setCodeNotation(codeNation);
            documentCacheEntry.setCodeNumber(document.getCodeNumber());
            if (document.getDocCode() == null) {
                String docCode = document.getCodeNumber() + "/" + document.getCodeNotation();
                documentCacheEntry.setDocCode(docCode);
            } else {
                documentCacheEntry.setDocCode(document.getDocCode());
            }
            documentCacheEntry.setSentDate(document.getSentDate());
            String fromOrganDomain = document.getFromOrganDomain();
            OrganizationCacheEntry fromOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(fromOrganDomain);
            documentCacheEntry.setFromOrgan(fromOrganCache);
            String toOrgan = document.getToOrganDomain();
            String[] toOrgans = toOrgan.split("#");
            List<OrganizationCacheEntry> toContacts = new ArrayList<>();
            for (String toDomain : toOrgans) {
                OrganizationCacheEntry toOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(toDomain);
                toContacts.add(toOrganCache);
            }
            EdocPriority priority = EDOC_PRIORITY_SERVICE.findById(document.getPriority());
            documentCacheEntry.setPriority(priority);
            documentCacheEntry.setToOrgan(toContacts);
            documentCacheEntry.setDraft(document.getDraft());
            documentCacheEntry.setVisible(document.getVisible());
            DocumentDetailCacheEntry documentDetailCacheEntry = new DocumentDetailCacheEntry();
            if (document.getDocumentDetail() != null) {
                documentDetailCacheEntry = MapperUtil.modelToDocumentDetailCached(document.getDocumentDetail());
            }
            documentCacheEntry.setDocumentDetail(documentDetailCacheEntry);
            /*Set<EdocAttachment> attachments = document.getAttachments();
            List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
            if (attachments.size() > 0) {
                for (EdocAttachment attachment : attachments) {
                    AttachmentCacheEntry attachmentCacheEntry = MapperUtil.modelToAttachmentCache(attachment);
                    attachmentCacheEntry.setDocumentId(document.getDocumentId());
                    attachmentCacheEntries.add(attachmentCacheEntry);
                }
            }
            documentCacheEntry.setAttachments(attachmentCacheEntries);
            List<TraceCacheEntry> traces = new ArrayList<>();
            Set<EdocTrace> edocTraces = document.getTraces();
            if (edocTraces.size() > 0) {
                for (EdocTrace edocTrace : edocTraces) {
                    TraceCacheEntry traceCacheEntry = MapperUtil.modelToTraceCache(edocTrace);
                    traceCacheEntry.setDocumentId(document.getDocumentId());
                    traces.add(traceCacheEntry);
                }
            }
            traces.sort(Comparator.comparing(TraceCacheEntry::getTimeStamp));
            documentCacheEntry.setTraces(traces);
            TraceHeaderListCacheEntry traceHeaderListCacheEntry = MapperUtil.modelToTraceHeaderListCache(document.getTraceHeaderList());
            traceHeaderListCacheEntry.setDocumentId(document.getDocumentId());
            documentCacheEntry.setTraceHeaderList(traceHeaderListCacheEntry);

            List<NotificationCacheEntry> notificationCacheEntries = new ArrayList<>();

            Set<EdocNotification> notifications = document.getNotifications();

            if (notifications.size() > 0) {
                for (EdocNotification notification : notifications) {
                    NotificationCacheEntry notificationCacheEntry = MapperUtil.modelToNotificationCache(notification);
                    notificationCacheEntry.setDocumentId(document.getDocumentId());
                    notificationCacheEntries.add(notificationCacheEntry);
                }
            }
            documentCacheEntry.setNotifications(notificationCacheEntries);*/
            return documentCacheEntry;
        } catch (Exception e) {
            LOGGER.error("Error convert document model to cache entry with document id "
                    + document.getDocumentId() + " clause " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static DocumentCacheEntry modelToDocumentCached(EdocDocument document) {
        try {
            DocumentCacheEntry documentCacheEntry = new DocumentCacheEntry();
            documentCacheEntry.setDocumentId(document.getDocumentId());
            documentCacheEntry.setEdXMLDocId(document.getEdXMLDocId());
            documentCacheEntry.setCreateDate(document.getCreateDate());
            documentCacheEntry.setModifiedDate(document.getModifiedDate());
            documentCacheEntry.setSubject(document.getSubject());
            documentCacheEntry.setPromulgationDate(document.getPromulgationDate());
            documentCacheEntry.setPromulgationPlace(document.getPromulgationPlace());
            documentCacheEntry.setDocumentType(document.getDocumentType());
            documentCacheEntry.setDocumentTypeName(document.getDocumentTypeName());
            documentCacheEntry.setSendExt(document.getSendExt());
            documentCacheEntry.setVisited(document.getVisited());
            String codeNation = document.getCodeNotation();
            if (codeNation.contains("#")) {
                codeNation = codeNation.substring(0, codeNation.indexOf("#"));
            }
            documentCacheEntry.setShortenSubject(StringUtil.shorten(document.getSubject(), 90));
            documentCacheEntry.setCodeNotation(codeNation);
            documentCacheEntry.setCodeNumber(document.getCodeNumber());
            if (document.getDocCode() == null) {
                String docCode = document.getCodeNumber() + "/" + document.getCodeNotation();
                documentCacheEntry.setDocCode(docCode);
            } else {
                documentCacheEntry.setDocCode(document.getDocCode());
            }
            documentCacheEntry.setSentDate(document.getSentDate());
            String fromOrganDomain = document.getFromOrganDomain();
            OrganizationCacheEntry fromOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(fromOrganDomain);
            documentCacheEntry.setFromOrgan(fromOrganCache);
            String toOrgan = document.getToOrganDomain();
            String[] toOrgans = toOrgan.split("#");
            List<OrganizationCacheEntry> toContacts = new ArrayList<>();
            for (String toDomain : toOrgans) {
                OrganizationCacheEntry toOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(toDomain);
                toContacts.add(toOrganCache);
            }
            EdocPriority priority = EDOC_PRIORITY_SERVICE.findById(document.getPriority());
            documentCacheEntry.setPriority(priority);
            documentCacheEntry.setToOrgan(toContacts);
            documentCacheEntry.setDraft(document.getDraft());
            documentCacheEntry.setVisible(document.getVisible());
            DocumentDetailCacheEntry documentDetailCacheEntry = new DocumentDetailCacheEntry();
            if (document.getDocumentDetail() != null) {
                documentDetailCacheEntry = MapperUtil.modelToDocumentDetailCached(document.getDocumentDetail());
            }
            documentCacheEntry.setDocumentDetail(documentDetailCacheEntry);
            Set<EdocAttachment> attachments = document.getAttachments();
            List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
            if (attachments.size() > 0) {
                for (EdocAttachment attachment : attachments) {
                    AttachmentCacheEntry attachmentCacheEntry = MapperUtil.modelToAttachmentCache(attachment);
                    attachmentCacheEntry.setDocumentId(document.getDocumentId());
                    attachmentCacheEntries.add(attachmentCacheEntry);
                }
            }
            documentCacheEntry.setAttachments(attachmentCacheEntries);
            List<TraceCacheEntry> traces = new ArrayList<>();
            Set<EdocTrace> edocTraces = document.getTraces();
            if (edocTraces.size() > 0) {
                for (EdocTrace edocTrace : edocTraces) {
                    TraceCacheEntry traceCacheEntry = MapperUtil.modelToTraceCache(edocTrace);
                    traceCacheEntry.setDocumentId(document.getDocumentId());
                    traces.add(traceCacheEntry);
                }
            }
            traces.sort(Comparator.comparing(TraceCacheEntry::getTimeStamp));
            documentCacheEntry.setTraces(traces);
            TraceHeaderListCacheEntry traceHeaderListCacheEntry = MapperUtil.modelToTraceHeaderListCache(document.getTraceHeaderList());
            traceHeaderListCacheEntry.setDocumentId(document.getDocumentId());
            documentCacheEntry.setTraceHeaderList(traceHeaderListCacheEntry);

            List<NotificationCacheEntry> notificationCacheEntries = new ArrayList<>();

            Set<EdocNotification> notifications = document.getNotifications();

            if (notifications.size() > 0) {
                for (EdocNotification notification : notifications) {
                    NotificationCacheEntry notificationCacheEntry = MapperUtil.modelToNotificationCache(notification);
                    notificationCacheEntry.setDocumentId(document.getDocumentId());
                    notificationCacheEntries.add(notificationCacheEntry);
                }
            }
            documentCacheEntry.setNotifications(notificationCacheEntries);
            return documentCacheEntry;
        } catch (Exception e) {
            LOGGER.error("Error convert document model to cache entry with document id "
                    + document.getDocumentId() + " clause " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }


    public static AttachmentCacheEntry modelToAttachmentCache(EdocAttachment attachment) {
        AttachmentCacheEntry attachmentCacheEntry = new AttachmentCacheEntry();
        try {
            attachmentCacheEntry.setAttachmentId(attachment.getAttachmentId());
            attachmentCacheEntry.setCreateDate(attachment.getCreateDate());
            attachmentCacheEntry.setFullPath(attachment.getFullPath());
            attachmentCacheEntry.setSize(attachment.getSize());
            attachmentCacheEntry.setOrganDomain(attachment.getOrganDomain());
            attachmentCacheEntry.setName(attachment.getName());
            attachmentCacheEntry.setFileType(attachment.getType());
            attachmentCacheEntry.setToOrganDomain(attachment.getToOrganDomain());
            String rootPath = ATTACHMENT_GLOBAL_UTIL.getAttachmentPath();
            String specPath = rootPath +
                    (rootPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                    attachment.getFullPath();
            attachmentCacheEntry.setRelativePath(specPath);
        } catch (Exception e) {
            LOGGER.error("Error convert edoc attachment model to cache entry " + e);
        }
        return attachmentCacheEntry;
    }


    public static NotificationCacheEntry modelToNotificationCache(EdocNotification notification) {
        NotificationCacheEntry notificationCacheEntry = new NotificationCacheEntry();

        notificationCacheEntry.setCreateDate(notification.getDateCreate());
        notificationCacheEntry.setDueDate(notification.getDueDate());
        notificationCacheEntry.setModifiedDate(notification.getModifiedDate());
        notificationCacheEntry.setNotificationId(notification.getNotificationId());
        String receiverId = notification.getReceiverId();
        OrganizationCacheEntry toOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(receiverId);
        notificationCacheEntry.setToOrganization(toOrganCache);
        notificationCacheEntry.setTaken(notification.getTaken());
        notificationCacheEntry.setSendNumber(notification.getSendNumber());

        return notificationCacheEntry;
    }

    public static TraceHeaderCacheEntry modelToTraceHeaderCached(EdocTraceHeader traceHeader) {
        TraceHeaderCacheEntry traceHeaderCacheEntry = new TraceHeaderCacheEntry();

        traceHeaderCacheEntry.setOrganDomain(traceHeader.getOrganDomain());
        traceHeaderCacheEntry.setTimeStamp(traceHeader.getTimeStamp());

        return traceHeaderCacheEntry;
    }


    public static TraceHeaderListCacheEntry modelToTraceHeaderListCache(EdocTraceHeaderList traceHeaderList) {
        TraceHeaderListCacheEntry traceHeaderListCacheEntry = new TraceHeaderListCacheEntry();
        try {
            traceHeaderListCacheEntry.setBusinessDocReason(traceHeaderList.getBusinessDocReason());
            traceHeaderListCacheEntry.setBusinessDocType(traceHeaderList.getBusinessDocType());
            traceHeaderListCacheEntry.setBusinessInfo(traceHeaderList.getBusinessInfo());
            traceHeaderListCacheEntry.setDepartment(traceHeaderList.getDepartment());
            traceHeaderListCacheEntry.setEmail(traceHeaderList.getEmail());
            traceHeaderListCacheEntry.setMobile(traceHeaderList.getMobile());
            traceHeaderListCacheEntry.setStaff(traceHeaderList.getStaff());
            traceHeaderListCacheEntry.setPaper(traceHeaderList.getPaper());
            traceHeaderList.setStaff(traceHeaderList.getStaff());

            Set<EdocTraceHeader> traceHeaders = traceHeaderList.getTraceHeaders();

            Set<TraceHeaderCacheEntry> traceHeaderCacheEntries = new HashSet<>();
            if (traceHeaders.size() > 0) {
                for (EdocTraceHeader traceHeader : traceHeaders) {
                    TraceHeaderCacheEntry traceHeaderCacheEntry = MapperUtil.modelToTraceHeaderCached(traceHeader);
                    traceHeaderCacheEntry.setTraceHeaderId(traceHeaderList.getDocumentId());
                    traceHeaderCacheEntry.setDocumentId(traceHeaderList.getDocumentId());
                    traceHeaderCacheEntries.add(traceHeaderCacheEntry);
                }
            }

            traceHeaderListCacheEntry.setTraceHeaders(traceHeaderCacheEntries);
        } catch (Exception e) {
            LOGGER.error("Error when convert edoc trace header list to cached entry " + e);
        }
        return traceHeaderListCacheEntry;
    }

    public static OrganizationCacheEntry modelToOrganCache(EdocDynamicContact contact) {
        OrganizationCacheEntry organizationCacheEntry = new OrganizationCacheEntry();
        try {
            organizationCacheEntry.setAddress(contact.getAddress());
            organizationCacheEntry.setDomain(contact.getDomain());
            organizationCacheEntry.setEmail(contact.getEmail());
            organizationCacheEntry.setId(contact.getId());
            organizationCacheEntry.setInCharge(contact.getInCharge());
            organizationCacheEntry.setName(contact.getName());
            organizationCacheEntry.setTelephone(contact.getTelephone());
            organizationCacheEntry.setToken(contact.getToken());
            organizationCacheEntry.setParent(contact.getParent());
            organizationCacheEntry.setStatus(contact.getStatus());
        } catch (Exception e) {
            LOGGER.error("Error when convert dynamic contact to cached entry cause " + e);
        }

        return organizationCacheEntry;
    }

    public static TraceCacheEntry modelToTraceCache(EdocTrace trace) {
        TraceCacheEntry traceCacheEntry = new TraceCacheEntry();

        traceCacheEntry.setCode(trace.getCode());
        traceCacheEntry.setComment(trace.getComment());
        traceCacheEntry.setDepartment(trace.getDepartment());
        traceCacheEntry.setEdxmlDocumentId(trace.getEdxmlDocumentId());
        traceCacheEntry.setEnable(trace.getEnable());
        traceCacheEntry.setPromulgationDate(traceCacheEntry.getPromulgationDate());
        traceCacheEntry.setServerTimeStamp(traceCacheEntry.getServerTimeStamp());
        traceCacheEntry.setStaffEmail(trace.getStaffEmail());
        traceCacheEntry.setStaffMobile(trace.getStaffMobile());
        traceCacheEntry.setStaffName(trace.getStaffName());
        traceCacheEntry.setStatusCode(trace.getStatusCode());
        traceCacheEntry.setTraceId(trace.getTraceId());
        traceCacheEntry.setTimeStamp(trace.getTimeStamp());
        String fromOrgan = trace.getFromOrganDomain();
        String toOrgan = trace.getToOrganDomain();
        OrganizationCacheEntry fromOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(fromOrgan);
        OrganizationCacheEntry toOrganCache = EDOC_DYNAMIC_CONTACT_SERVICE.getOrganizationCache(toOrgan);
        traceCacheEntry.setFromOrgan(fromOrganCache);
        traceCacheEntry.setToOrgan(toOrganCache);

        return traceCacheEntry;
    }

    public static DocumentDetailCacheEntry modelToDocumentDetailCached(EdocDocumentDetail edocDocumentDetail) {
        DocumentDetailCacheEntry documentDetailCacheEntry = new DocumentDetailCacheEntry();
        try {

            documentDetailCacheEntry.setAppendixes(edocDocumentDetail.getAppendixes());
            documentDetailCacheEntry.setContent(edocDocumentDetail.getContent());
            documentDetailCacheEntry.setDocumentId(edocDocumentDetail.getDocumentId());
            documentDetailCacheEntry.setDueDate(edocDocumentDetail.getDueDate());
            documentDetailCacheEntry.setPageAmount(edocDocumentDetail.getPageAmount());
            documentDetailCacheEntry.setPromulgationAmount(edocDocumentDetail.getPromulgationAmount());
            documentDetailCacheEntry.setResponseFor(edocDocumentDetail.getResponseFor());
            documentDetailCacheEntry.setSignerCompetence(edocDocumentDetail.getSignerCompetence());
            documentDetailCacheEntry.setSignerFullName(edocDocumentDetail.getSignerFullName());
            documentDetailCacheEntry.setSignerPosition(edocDocumentDetail.getSignerPosition());
            documentDetailCacheEntry.setSphereOfPromulgation(edocDocumentDetail.getSphereOfPromulgation());
            documentDetailCacheEntry.setSteeringType(edocDocumentDetail.getSteeringType());
            documentDetailCacheEntry.setToPlaces(edocDocumentDetail.getToPlaces());
            documentDetailCacheEntry.setTyperNotation(edocDocumentDetail.getTyperNotation());
        } catch (Exception e) {
            LOGGER.error("Error convert document detail to cached entry " + e);
        }
        return documentDetailCacheEntry;

    }

    public static UserCacheEntry modelToUserCache(User user) {
        UserCacheEntry userCacheEntry = new UserCacheEntry();
        try {
            userCacheEntry.setEmailAddress(user.getEmailAddress());
            userCacheEntry.setLastLoginDate(user.getLastLoginDate());
            userCacheEntry.setLastLoginIP(user.getLastLoginIP());
            userCacheEntry.setStatus(user.getStatus());
            userCacheEntry.setUserId(user.getUserId());
            userCacheEntry.setUsername(user.getUsername());
            userCacheEntry.setDisplayName(user.getDisplayName());
            EdocDynamicContact edocDynamicContact = user.getDynamicContact();
            if (edocDynamicContact != null) {
                OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(edocDynamicContact);
                userCacheEntry.setOrganization(organizationCacheEntry);
            } else {
                userCacheEntry.setOrganization(new OrganizationCacheEntry());
            }

            userCacheEntry.setOnSso(user.isSso());
        } catch (Exception e) {
            LOGGER.error("Error when convert user entry to cached entry cause " + e);
        }

        return userCacheEntry;
    }

    public static EdocDocument modelToEdocDocument(MessageHeader messageHeader) {
        // get info of eDoc document
        Date sentDate = new Date();
        String edXMLDocId = messageHeader.getDocumentId();
        String subject = messageHeader.getSubject();
        String codeNumber = messageHeader.getCode().getCodeNumber();
        String codeNotation = messageHeader.getCode().getCodeNotation();

        Date promulgationDate = messageHeader
                .getPromulgationInfo().getPromulgationDate();

        String promulgationPlace = messageHeader.getPromulgationInfo().getPlace();
        int type = messageHeader.getDocumentType().getType();
        String documentTypeName = messageHeader.getDocumentType().getTypeName();
        int documentTypeDetail = messageHeader.getDocumentType().getTypeDetail();

        int priorityId = messageHeader.getOtherInfo().getPriority();
        String toOrganDomain = CommonUtil.getToOrganDomain(messageHeader.getToes());
        String fromOrganDomain = messageHeader.getFrom().getOrganId();
        if (codeNotation.contains("#")) {
            codeNotation = codeNotation.substring(0, codeNotation.indexOf("#"));
        }
        String docCode = codeNumber + "/" + codeNotation;

        EdocDocument newDocument = new EdocDocument();
        newDocument.setEdXMLDocId(edXMLDocId);
        newDocument.setSubject(subject);
        newDocument.setCodeNumber(codeNumber);
        newDocument.setDocCode(docCode);
        newDocument.setCodeNotation(codeNotation);
        newDocument.setPromulgationDate(promulgationDate);
        newDocument.setPromulgationPlace(promulgationPlace);
        newDocument.setDocumentType(type);
        newDocument.setDocumentTypeName(documentTypeName);
        newDocument.setDocumentTypeDetail(documentTypeDetail);
        newDocument.setPriority(priorityId);
        newDocument.setToOrganDomain(toOrganDomain);
        newDocument.setFromOrganDomain(fromOrganDomain);
        newDocument.setSentDate(sentDate);
        newDocument.setDraft(false);
        newDocument.setSendExt(false);
        newDocument.setVisited(false);
        Date currentDate = new Date();
        newDocument.setCreateDate(currentDate);
        newDocument.setModifiedDate(currentDate);
        newDocument.setVisible(true);
        return newDocument;
    }

    public static EdocDocumentDetail modelToDocumentDetail(MessageHeader messageHeader) {
        // get info of document detail
        String content = messageHeader.getContent();
        String signerCompetence = messageHeader.getSignerInfo().getCompetence();
        String signerPosition = messageHeader.getSignerInfo().getPosition();
        Date dueDate = messageHeader.getDueDate();
        StringBuilder toPlacesBuffer = new StringBuilder();
        String toPlaceStr = "";
        List<String> toPlaces = messageHeader.getToPlaces();
        if (toPlaces != null && toPlaces.size() > 0) {
            for (String toPlace : toPlaces) {
                toPlacesBuffer.append(toPlace);
                toPlacesBuffer.append("#");
            }
            toPlaceStr = toPlacesBuffer.toString().substring(0, toPlacesBuffer.toString().length() - 1);
        }

        OtherInfo otherInfo = messageHeader.getOtherInfo();

        List<String> appendixes = otherInfo.getAppendixes();
        StringBuilder appendixesStringBuilder = new StringBuilder();
        String appendixesStr = "";
        if (appendixes != null && appendixes.size() > 0) {
            for (String appendix : appendixes) {
                appendixesStringBuilder.append(appendix);
                appendixesStringBuilder.append("#");
            }
            appendixesStr = appendixesStringBuilder.toString().substring(0, appendixesStringBuilder.length() - 1);
        }
        String sphereOfPromulgation = messageHeader.getOtherInfo()
                .getSphereOfPromulgation();
        String typerNotation = messageHeader.getOtherInfo().getTyperNotation();
        int pageAmount = messageHeader.getOtherInfo().getPageAmount();
        int promulgationAmount = messageHeader.getOtherInfo().getPromulgationAmount();
        String singerFullName = messageHeader.getSignerInfo().getFullName();
        int steeringTypeInt = messageHeader.getSteeringType();
        EdocDocumentDetail.SteeringType steeringType = EdocDocumentDetail.SteeringType.values()[steeringTypeInt];
        List<ResponseFor> responseFors = messageHeader.getResponseFor();
        String responseFor = null;
        if (responseFors != null && responseFors.size() > 0) {
            responseFor = gson.toJson(responseFors);
        }

        // create document detail
        EdocDocumentDetail documentDetail = new EdocDocumentDetail();
        documentDetail.setContent(content);
        documentDetail.setSignerCompetence(signerCompetence);
        documentDetail.setSignerPosition(signerPosition);
        documentDetail.setDueDate(dueDate);
        documentDetail.setToPlaces(toPlaceStr);
        documentDetail.setSphereOfPromulgation(sphereOfPromulgation);
        documentDetail.setTyperNotation(typerNotation);
        documentDetail.setPageAmount(pageAmount);
        documentDetail.setSignerFullName(singerFullName);
        documentDetail.setResponseFor(responseFor);
        documentDetail.setAppendixes(appendixesStr);
        documentDetail.setPromulgationAmount(promulgationAmount);
        documentDetail.setSteeringType(steeringType);
        return documentDetail;
    }

    private static final Logger LOGGER = Logger.getLogger(MapperUtil.class);
}
