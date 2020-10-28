/**
 *
 */
package com.bkav.edoc.service.mineutil;

import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.services.EdocDynamicContactService;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.ed.header.*;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Mapper {

    private static final EdocDynamicContactService dynamicContactService = new EdocDynamicContactService();

    private final Gson gson = new Gson();

    public Mapper() {

    }

    public MessageHeader modelToMessageHeader(EdocDocument document, EdocDocumentDetail detail) {
        MessageHeader messageHeader = new MessageHeader();
        try {
            Organization from = new Organization();
            from.setOrganId(document.getFromOrganDomain());
            // TODO: Can lay thong tin don vi roi insert vao
            EdocDynamicContact dynamicContact = dynamicContactService.getDynamicContactByDomain(document.getFromOrganDomain());
            if (dynamicContact == null) {
                from.setOrganName(StringPool.DEFAULT_STRING);
                from.setOrganAdd(StringPool.DEFAULT_STRING);
                from.setEmail(StringPool.DEFAULT_STRING);
                from.setTelephone(StringPool.DEFAULT_STRING);
                from.setOrganizationInCharge(StringPool.DEFAULT_STRING);
                from.setFax(StringPool.DEFAULT_STRING);
                from.setWebsite(StringPool.DEFAULT_STRING);
            } else {
                from.setOrganName(dynamicContact.getName());
                from.setOrganAdd(dynamicContact.getAddress());
                from.setEmail(dynamicContact.getEmail());
                from.setTelephone(dynamicContact.getTelephone());
                from.setFax(dynamicContact.getFax());
                from.setOrganizationInCharge(dynamicContact.getInCharge());
                from.setWebsite(dynamicContact.getWebsite());
            }
            messageHeader.setFrom(from);

            String[] toStr = document.getToOrganDomain().split("#");
            List<Organization> toes = new ArrayList<>();

            for (String item : toStr) {
                Organization to = new Organization();
                to.setOrganId(item);
                EdocDynamicContact toInfo = dynamicContactService.getDynamicContactByDomain(item);
                if (toInfo == null) {
                    to.setOrganName(StringPool.DEFAULT_STRING);
                    to.setOrganAdd(StringPool.DEFAULT_STRING);
                    to.setEmail(StringPool.DEFAULT_STRING);
                    to.setTelephone(StringPool.DEFAULT_STRING);
                    to.setOrganizationInCharge(StringPool.DEFAULT_STRING);
                    to.setFax(StringPool.DEFAULT_STRING);
                    to.setWebsite(StringPool.DEFAULT_STRING);
                } else {
                    to.setOrganName(toInfo.getName());
                    to.setOrganAdd(toInfo.getAddress());
                    to.setEmail(toInfo.getEmail());
                    to.setOrganizationInCharge(toInfo.getInCharge());
                    to.setTelephone(toInfo.getTelephone());
                    to.setFax(toInfo.getFax());
                    to.setWebsite(toInfo.getWebsite());
                }
                toes.add(to);
            }
            messageHeader.setToes(toes);

            messageHeader.setDocumentId(String.valueOf(document.getDocumentId()));

            Code code = new Code();
            code.setCodeNotation(document.getCodeNotation() == null ? StringPool.DEFAULT_STRING
                    : document.getCodeNotation());
            code.setCodeNumber(document.getCodeNumber() == null ? StringPool.DEFAULT_STRING
                    : document.getCodeNumber());
            messageHeader.setCode(code);

            PromulgationInfo proInfo = new PromulgationInfo();
            proInfo.setPlace(document.getPromulgationPlace() == null ? StringPool.DEFAULT_STRING
                    : document.getPromulgationPlace());

            proInfo.setPromulgationDate(document.getPromulgationDate());
            messageHeader.setPromulgationInfo(proInfo);

            SignerInfo signerInfo = new SignerInfo();
            signerInfo.setCompetence(detail.getSignerCompetence());
            signerInfo.setFullName(detail.getSignerFullName());
            signerInfo.setPosition(detail.getSignerPosition());
            messageHeader.setSignerInfo(signerInfo);

            DocumentType type = new DocumentType();
            type.setType(document.getDocumentType());
            type.setTypeName(document.getDocumentTypeName());
            messageHeader.setDocumentType(type);

            messageHeader.setSubject(document.getSubject() == null ? StringPool.DEFAULT_STRING
                    : document.getSubject());
            messageHeader.setContent(detail.getContent() == null ? StringPool.DEFAULT_STRING
                    : detail.getContent());
            if (detail.getDueDate() != null) {
                messageHeader.setDueDate(detail
                        .getDueDate());
            }

            String[] placeStrings = detail.getToPlaces().split("#");
            List<String> places = new ArrayList<>();
            if (placeStrings.length == 1 && placeStrings[0] == null) {
                places.add(StringPool.DEFAULT_STRING);
            } else {
                Collections.addAll(places, placeStrings);
            }

            messageHeader.setToPlaces(places);

            String[] appendixesStrings = detail.getAppendixes().split("#");
            List<String> appendixes = new ArrayList<>();
            if (appendixesStrings.length == 1 && appendixesStrings[0] == null){
                appendixes.add(StringPool.DEFAULT_STRING);
            } else {
                Collections.addAll(appendixes, appendixesStrings);
            }
            String responseFor = detail.getResponseFor();
            if (responseFor != null) {
                // when revoke document, get response for to set into header
                Type responseForListType = new TypeToken<ArrayList<ResponseFor>>() {
                }.getType();
                ArrayList<ResponseFor> responseForArray = gson.fromJson(responseFor, responseForListType);
                messageHeader.setResponseFor(responseForArray);
            }

            OtherInfo other = new OtherInfo();
            other.setTyperNotation(detail.getTyperNotation() == null ? StringPool.DEFAULT_STRING
                    : detail.getTyperNotation());

            other.setPageAmount(detail.getPageAmount());
            other.setPromulgationAmount(detail.getPromulgationAmount());
            other.setPriority(document.getPriority());
            other.setSphereOfPromulgation(detail.getSphereOfPromulgation() == null ? StringPool.DEFAULT_STRING : detail
                    .getSphereOfPromulgation());
            other.setAppendixes(appendixes);
            messageHeader.setSteeringType(document.getDocumentDetail().getSteeringType().ordinal());
            messageHeader.setOtherInfo(other);
        } catch (Exception e) {
            LOGGER.error("Error when convert document to message header with document id " + document.getDocumentId() + " cause " + e);
        }
        return messageHeader;

    }

    public Attachment attachmentModelToServiceEntity(EdocAttachment attachment) throws IOException {

        Attachment attachmentEntity = new Attachment();

        attachmentEntity.setName(attachment.getName());

       /* String rootPath = _attGlobal.getAttachmentPath();

        StringBuilder filePath = new StringBuilder();*/

        // Giai nen file dinh kem - Neu chua nen se tra ve duong dan hien
        // tai, neu da nen se giai nen va tra ve dia chi tuyet doi cua file
        // da duoc giai nen
        File file = archiveUtil.decompressFile(attachment);

        attachmentEntity.setInputStream(new FileInputStream(file));

        attachmentEntity.setContent(file);

        String contentTransfer = "base64";

        attachmentEntity.setContentTransferEncoded(contentTransfer);

        attachmentEntity.setContentType(attachment.getType());

        attachmentEntity.setDescription(attachment.getName());

        return attachmentEntity;
    }

    public void parseBusinessInfo(TraceHeaderList traceHeaderList) {
        String businessInfo = traceHeaderList.getBusinessInfo();
        if (businessInfo == null || businessInfo.isEmpty()) return;
        // check business doc type
        if (traceHeaderList.getBusiness().getBusinessDocType() == EdocTraceHeaderList.BusinessDocType.REPLACE.ordinal()) {
            // when replace document, get replacement info set to trace header list
            /*Type replacementInfoListType = new TypeToken<ArrayList<ReplacementInfo>>() {
            }.getType();
            ArrayList<ReplacementInfoList> replacementInfoArray = gson.fromJson(businessInfo, replacementInfoListType);*/
            ReplacementInfoList replacementInfoList = gson.fromJson(businessInfo, ReplacementInfoList.class);
            traceHeaderList.getBusiness().setReplacementInfoList(replacementInfoList);
        } else if (traceHeaderList.getBusiness().getBusinessDocType() == EdocTraceHeaderList.BusinessDocType.UPDATE.ordinal()) {
            // when update document, get business document info set to trace header list
            BusinessDocumentInfo businessDocumentInfo = gson.fromJson(businessInfo, BusinessDocumentInfo.class);
            traceHeaderList.getBusiness().setBusinessDocumentInfo(businessDocumentInfo);
        }
    }

    public List<MessageStatus> traceInfoToStatusEntity(List<EdocTrace> traces) {
        List<MessageStatus> result = new ArrayList<>();
        if (traces == null || traces.size() == 0) return null;
        for (EdocTrace trace : traces) {
            MessageStatus status = new MessageStatus();
            status.setDescription(trace.getComment());
            status.setStatusCode(String.valueOf(trace.getStatusCode()));
            if (trace.getTimeStamp() != null) {
                status.setTimestamp(trace.getTimeStamp());
            }
            Organization from = new Organization();
            from.setOrganId(trace.getFromOrganDomain());
            from.setOrganAdd(trace.getOrganAdd());
            from.setOrganName(trace.getOrganName());
            from.setEmail(trace.getEmail());
            from.setFax(trace.getFax());
            from.setTelephone(trace.getTelephone());
            from.setWebsite(trace.getWebsite());
            from.setOrganizationInCharge(trace.getOrganizationInCharge());
            status.setFrom(from);
            ResponseFor responseFor = new ResponseFor();
            responseFor.setOrganId(trace.getToOrganDomain());
            responseFor.setCode(trace.getCode());
            responseFor.setDocumentId(trace.getEdxmlDocumentId());
            if (trace.getPromulgationDate() != null) {
                responseFor.setPromulgationDate(trace.getPromulgationDate());
            }
            status.setResponseFor(responseFor);
            StaffInfo staffInfo = new StaffInfo();
            staffInfo.setDepartment(trace.getDepartment());
            staffInfo.setEmail(trace.getEmail());
            staffInfo.setMobile(trace.getStaffMobile());
            staffInfo.setStaff(trace.getStaffName());
            status.setStaffInfo(staffInfo);

            result.add(status);
        }
        return result;
    }

    private static final ArchiveUtil archiveUtil = new ArchiveUtil();
    private static final Logger LOGGER = Logger.getLogger(Mapper.class);
}
