package com.bkav.edoc.service.commonutil;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.services.EdocDynamicContactService;
import com.bkav.edoc.service.kernel.util.FileUtil;
import com.bkav.edoc.service.kernel.util.InternetAddressUtil;
import com.bkav.edoc.service.kernel.util.MimeTypesUtil;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.util.EdXMLConfigKey;
import com.bkav.edoc.service.util.EdXMLConfigUtil;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.service.xml.ed.header.*;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Checker {
    int level = 4;
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();

    public Report checkMessageHeader(MessageHeader messageHeader)
            throws Exception {

        List<Error> errorList = new ArrayList<>();

        boolean isSuccess = true;

        errorList.addAll(checkFrom(messageHeader.getFrom()));

        errorList.addAll(checkExistTo(messageHeader.getToes()));

        for (Organization to : messageHeader.getToes()) {
            errorList.addAll(checkTo(to));
        }

        errorList.addAll(checkDuplicateTo(messageHeader.getToes()));

        errorList.addAll(checkDocumentId(messageHeader.getDocumentId()));

        errorList.addAll(checkCode(messageHeader.getCode()));

        errorList.addAll(checkPromulgationInfo(messageHeader
                .getPromulgationInfo()));

        errorList.addAll(checkDocumentType(messageHeader.getDocumentType()));

        errorList.addAll(checkSubject(messageHeader.getSubject()));

        errorList.addAll(checkContent(messageHeader.getContent()));

        errorList.addAll(checkToPlace(messageHeader.getToPlaces()));

        errorList.addAll(checkOtherInfo(messageHeader.getOtherInfo()));

        if (errorList.size() > 0) {

            isSuccess = false;

        }

        return new Report(isSuccess, new ErrorList(errorList));
    }

    public List<Organization> checkSendToVPCP(List<Organization> tos) {
        List<Organization> toesVPCP = new ArrayList<>();
        try {

            List<String> domains = tos.stream().map(Organization::getOrganId).collect(Collectors.toList());
            // Get all contact to get agency of every contact
            List<EdocDynamicContact> contacts = edocDynamicContactService.getContactsByMultipleDomains(domains);
            if (contacts.size() > 0) {
                // filter contact to send vpcp
                List<EdocDynamicContact> contactList = contacts.stream().filter(o -> !o.getAgency()).collect(Collectors.toList());
                if (contactList.size() > 0) {
                    List<String> contactOrgans = contactList.stream().map(EdocDynamicContact::getDomain).collect(Collectors.toList());
                    // filter list pending
                    for (Organization organization : tos) {
                        if (contactOrgans.contains(organization.getOrganId())) {
                            toesVPCP.add(organization);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error when check send document to VPCP " + e);
        }
        return toesVPCP;
    }

    public static void main(String[] args) {
        List<Organization> organizations = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganId("000.12.30.A36");
        Organization organization1 = new Organization();
        organization1.setOrganId("000.21.36.I03");
        Organization organization2 = new Organization();
        organization2.setOrganId("000.00.12.H23");
        Organization organization3 = new Organization();
        organization3.setOrganId("000.00.00.G09");
        organizations.add(organization);
        organizations.add(organization1);
        organizations.add(organization2);
        organizations.add(organization3);
        List<Organization> result = new Checker().checkSendToVPCP(organizations);
        System.out.println(result.size());
       /* validateJavaDate("12/29/2016");
        validateJavaDate("12-29-2016");
        validateJavaDate("12,29,2016");
        validateJavaDate("2016/12/29");*/
    }

    public ResponseFor checkSendToVPCP(ResponseFor responseFor) {
        try {
            String toDomain = responseFor.getOrganId();
            EdocDynamicContact dynamicContact = edocDynamicContactService.findContactByDomain(toDomain);
            if (dynamicContact != null) {
                if (!dynamicContact.getAgency()) {
                    return responseFor;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error when check send status to vpcp for organ domain " + responseFor.getOrganId());
        }
        return null;
    }

    public Report checkTraceHeaderList(TraceHeaderList traceList) {
        List<Error> errorList = new ArrayList<>();
        String organErrorCode = "TraceHeaderList.TraceHeader.OrganId";
        for (TraceHeader item : traceList.getTraceHeaders()) {
            // Check Domain
            if (item.getOrganId().length() == 0) {
                errorList.add(new Error(String.format("N.%s", organErrorCode),
                        "OrganId is required."));
            }
            if (checkLength(item.getOrganId(), level)) {

                errorList.add(new Error(String.format("R.%s", organErrorCode),
                        "OrganId is out of range."));
            }
        }
        boolean isSuccess = errorList.size() <= 0;
        return new Report(isSuccess, new ErrorList(errorList));
    }

    public Report checkSignature(SignatureEdoc signature) {
        List<Error> errorList = new ArrayList<>();
        Report report;
        if (signature != null) {
            KeyInfoEdoc keyInfo = signature.getKeyInfo();
            if (keyInfo != null) {
                if (keyInfo.getOrganId() != null && keyInfo.getToken() != null) {
                    boolean result = edocDynamicContactService.checkPermission(keyInfo.getOrganId(), keyInfo.getToken());
                    report = new Report(result, new ErrorList(errorList));
                } else {
                    errorList.add(new Error("M.CheckSignature", "OrganId or Token is required"));

                    report = new Report(false, new ErrorList(errorList));
                }
            } else {
                errorList.add(new Error("M.CheckSignature", "Error get KeyInfo"));

                report = new Report(false, new ErrorList(errorList));
            }
        } else {
            errorList.add(new Error("M.CheckSignature", "Error get Signature"));

            report = new Report(false, new ErrorList(errorList));
        }
        return report;
    }

    public Report checkPermission(CheckPermission checkPermission) {
        Report report;
        List<Error> errorList = new ArrayList<>();
        if (checkPermission != null) {
            if (checkPermission.getOrganId() != null && checkPermission.getToken() != null) {

                boolean result = edocDynamicContactService.checkPermission(checkPermission.getOrganId(), checkPermission.getToken());

                report = new Report(result, new ErrorList(errorList));

            } else {
                errorList.add(new Error("M.CheckPermission", "OrganId or Token is required"));

                report = new Report(false, new ErrorList(errorList));
            }

        } else {
            errorList.add(new Error("M.CheckPermission", "Error get check permission request null"));

            report = new Report(false, new ErrorList(errorList));
        }

        return report;
    }

    /**
     * @param from
     * @return
     * @throws Exception
     */
    private List<Error> checkFrom(Organization from) throws Exception {

        List<Error> errorList = new ArrayList<Error>();

        errorList.addAll(checkOrganId(from.getOrganId(), true));

        errorList.addAll(checkOrganName(from.getOrganName(), true));

        errorList.addAll(checkOrganizationInCharge(from.getOrganizationInCharge()));

        errorList.addAll(checkOrganAdd(from.getOrganAdd(), true));

        errorList.addAll(checkEmail(from.getEmail(), true));

        errorList.addAll(checkTelephone(from.getTelephone(), true));

        errorList.addAll(checkFax(from.getFax(), true));

        errorList.addAll(checkWebsite(from.getWebsite(), true));

        return errorList;
    }

    /**
     * @param tos
     * @return
     * @throws Exception
     */
    private List<Error> checkDuplicateTo(List<Organization> tos) throws Exception {

        List<Error> errorList = new ArrayList<Error>();

        if (tos != null) {
            Map<String, Organization> map = new HashMap<>();
            for (Organization to : tos) {
                if (!map.containsKey(to.getOrganId())) {
                    map.put(to.getOrganId(), to);
                } else {
                    errorList.add(new Error("N.MessageHeader.To.OrganId",
                            "To OrganId is duplicate."));
                    break;
                }

            }

        }
        return errorList;
    }

    /**
     * @param to
     * @return
     * @throws Exception
     */
    private List<Error> checkTo(Organization to) throws Exception {

        List<Error> errorList = new ArrayList<Error>();

        errorList.addAll(checkOrganId(to.getOrganId(), false));

        errorList.addAll(checkOrganName(to.getOrganName(), false));

        errorList.addAll(checkOrganAdd(to.getOrganAdd(), false));

        errorList.addAll(checkEmail(to.getEmail(), false));

        errorList.addAll(checkTelephone(to.getTelephone(), false));

        errorList.addAll(checkFax(to.getFax(), false));

        errorList.addAll(checkWebsite(to.getWebsite(), false));

        return errorList;
    }

    /**
     * @param tos
     * @return
     * @throws Exception
     */
    private List<Error> checkExistTo(List<Organization> tos) {
        final String errorCode = "N.MessageHeader.To";
        List<Error> errorList = new ArrayList<>();
        String error = "To Organization required";
        if (tos == null) {
            errorList.add(new Error(errorCode, error));
        } else {
            if (tos.isEmpty()) {

                errorList.add(new Error(errorCode, error));
            }
        }

        return errorList;
    }

    /**
     * @param code
     * @return
     */
    private List<Error> checkCode(Code code) {

        List<Error> errorList = new ArrayList<>();

        errorList.addAll(checkCodeNumber(code.getCodeNumber()));

        errorList.addAll(checkCodeNotation(code.getCodeNotation()));

        return errorList;
    }

    /**
     * @param proInfo
     * @return
     */
    private List<Error> checkPromulgationInfo(PromulgationInfo proInfo) {

        List<Error> errorList = new ArrayList<>();

        errorList.addAll(checkProPlace(proInfo.getPlace()));

        errorList.addAll(checkProDate(proInfo.getPromulgationDateValue()));

        return errorList;
    }


    /**
     * @param otherInfo
     * @return
     */
    private List<Error> checkOtherInfo(OtherInfo otherInfo) {

        List<Error> errorList = new ArrayList<>();

        errorList.addAll(checkSphereOfPromulgation(otherInfo
                .getSphereOfPromulgation()));

        errorList.addAll(checkPriority(otherInfo.getPriority()));

        errorList.addAll(checkTyperNotation(otherInfo.getTyperNotation()));

        errorList.addAll(checkPromulgationAmount(otherInfo
                .getPromulgationAmount()));

        errorList.addAll(checkPageAmount(otherInfo.getPageAmount()));

        return errorList;
    }

    /**
     * @param places
     * @return
     */
    private List<Error> checkToPlace(List<String> places) {
        List<Error> errorList = new ArrayList<>();
        if (places != null && places.size() > 0) {
            for (String place : places) {
                if (checkLength(place, 150)) {
                    errorList.add(new Error("R.MessageHeader.ToPlace.Place",
                            "Place is out of range."));
                }
            }
        }
        return errorList;
    }

    /**
     * @param subject
     * @return
     */
    private List<Error> checkSubject(String subject) {

        List<Error> errorList = new ArrayList<>();

        if (subject.isEmpty()) {

            errorList.add(new Error("N.MessageHeader.Subject",
                    "Subject is required."));
        }
        if (checkLength(subject, 500)) {

            errorList.add(new Error("R.MessageHeader.Subject",
                    "Subject is out of range."));
        }
        return errorList;
    }

    /**
     * @param content
     * @return
     */
    private List<Error> checkContent(String content) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(content, 500)) {

            errorList.add(new Error("R.MessageHeader.Content",
                    "Content is out of range."));

        }
        return errorList;
    }

    /**
     * @param documentId
     * @return
     */
    private List<Error> checkDocumentId(String documentId) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(documentId, 15)) {

            errorList.add(new Error("R.DocumentId",
                    "DocumentId is out of range."));
        }
        return errorList;
    }

    /**
     * @param docType
     * @return
     */
    private List<Error> checkDocumentType(DocumentType docType) {

        List<Error> errorList = new ArrayList<>();

        errorList.addAll(checkDocType(docType.getType()));

        errorList.addAll(checkDocTypeName(docType.getTypeName()));

        return errorList;
    }

    /**
     * @param organId
     * @param isFrom
     * @return
     * @throws Exception
     */
    private List<Error> checkOrganId(String organId, boolean isFrom)
            throws Exception {
        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("OrganId").toString();
        List<Error> errorList = new ArrayList<>();

        if (organId.isEmpty()) {

            errorList.add(new Error(String.format("N.%s", lastOfErrorCode),
                    "OrganId is required."));

        }
        if (checkLength(organId, level)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "OrganId is out of range."));

        }

        return errorList;
    }

    public Report checkAllowAttachment(String attachmentName,
                                       String attachmentType, String attachmentEncoding) {

        List<Error> errorList = new ArrayList<>();

        boolean isSuccess = true;

        if (checkLength(attachmentName, 500)) {

            isSuccess = false;

            errorList.add(new Error("R.AttachmentName",
                    "Attachment Name is out of range."));
        }

        String encoding = EdXMLConfigUtil
                .getValueByKey(EdXMLConfigKey.ATTACHMENT_ENCODING_TYPE_ALLOW);

        if (!encoding.equals("*") && encoding.length() > 0) {

            int index = encoding.toUpperCase().indexOf(
                    attachmentEncoding.toUpperCase() + ",");

            if (index == -1) {

                isSuccess = false;

                errorList.add(new Error("M.Attachment-Encoding",
                        "Attachment content transfer encoding not support."));
            }

        }

        String contentType = EdXMLConfigUtil
                .getValueByKey(EdXMLConfigKey.ATTACHMENT_TYPE_ALLOW);

        if (!contentType.equals("*") && contentType.length() > 0) {

            String fileContentType = MimeTypesUtil
                    .getContentType(attachmentName);

            if (fileContentType.toLowerCase().equals(
                    attachmentType.toLowerCase())) {

                Set<String> attachmentExtension = MimeTypesUtil
                        .getExtensions(attachmentType);

                String fileExtension = "."
                        + FileUtil.getExtension(attachmentName);

                String contentTypeExtension = null;

                for (String temp : attachmentExtension) {

                    if (temp.equals(fileExtension)) {
                        contentTypeExtension = temp;
                        break;
                    }

                }

                if (contentTypeExtension == null) {
                    errorList.add(new Error("M.Attachment-Type",
                            "Attachment content type not support."));
                } else {

                    int index = contentType.toLowerCase().indexOf(
                            contentTypeExtension.toLowerCase());

                    if (index == -1) {

                        isSuccess = false;

                        errorList.add(new Error("M.Attachment-Type",
                                "Attachment content type not support."));
                    }
                }
            } else {
                isSuccess = false;

                errorList.add(new Error("M.Attachment-Type",
                        "Attachment content type not support."));
            }

        }

        return new Report(isSuccess, new ErrorList(errorList));
    }

    /**
     * @param organName
     * @return
     */
    private List<Error> checkOrganizationInCharge(String organName) {
        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append("From").append("OrganizationInCharge").toString();
        List<Error> errorList = new ArrayList<>();

        if (checkLength(organName, 200)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "OrganizationInCharge is out of range."));
        }
        return errorList;
    }

    /**
     * @param organName
     * @param isFrom
     * @return
     */
    private List<Error> checkOrganName(String organName, boolean isFrom) {
        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("OrganName").toString();
        List<Error> errorList = new ArrayList<>();

        if (checkLength(organName, 200)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "OrganName is out of range."));
        }
        return errorList;
    }

    /**
     * @param organAdd
     * @param isFrom
     * @return
     */
    private List<Error> checkOrganAdd(String organAdd, boolean isFrom) {

        List<Error> errorList = new ArrayList<>();
        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("OrganAdd").toString();
        if (checkLength(organAdd, 250)) {

            errorList.add(new Error(String.format("R.%", lastOfErrorCode),
                    "OrganAdd is out of range."));

        }
        return errorList;
    }

    /**
     * @param email
     * @param isFrom
     * @return
     */
    private List<Error> checkEmail(String email, boolean isFrom) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("Email").toString();

        if (!email.isEmpty()) {
            if (checkLength(email, 100)) {

                errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                        "Email is out of range."));
            }
            if (!InternetAddressUtil.isValid(email)) {

                errorList.add(new Error(String.format("M.%s", lastOfErrorCode),
                        "Email invalid."));
            }
        }
        return errorList;
    }

    /**
     * @param telephone
     * @param isFrom
     * @return
     */
    private List<Error> checkTelephone(String telephone, boolean isFrom) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("Telephone").toString();

        if (checkLength(telephone, 20)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "Telephone is out of range."));
        }
        return errorList;
    }

    /**
     * @param fax
     * @param isFrom
     * @return
     */
    private List<Error> checkFax(String fax, boolean isFrom) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("Fax").toString();

        if (checkLength(fax, 20)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "Fax is out of range."));
        }
        return errorList;
    }

    /**
     * @param website
     * @param isFrom
     * @return
     */
    private List<Error> checkWebsite(String website, boolean isFrom) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append(isFrom ? "From" : "To").append("Website").toString();

        if (checkLength(website, 20)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "Website is out of range."));
        }
        if (website.matches(StringPool.URL_REGEX)) {

            errorList.add(new Error(String.format("M.%s", lastOfErrorCode),
                    "Website invalid."));
        }
        return errorList;
    }

    /**
     * @param codeNumber
     * @return
     */
    private List<Error> checkCodeNumber(String codeNumber) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append("Code").append("CodeNumber").toString();

        if (codeNumber.isEmpty()) {

            errorList.add(new Error(String.format("N.%s", lastOfErrorCode),
                    "CodeNumber is required."));
        }
        if (checkLength(codeNumber, 11)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "CodeNumber is out of range."));
        }
        return errorList;
    }

    /**
     * @param codeNotation
     * @return
     */
    private List<Error> checkCodeNotation(String codeNotation) {

        List<Error> errorList = new ArrayList<>();
        String lastOfErrorCode = new StringBuilder("MessageHeader")
                .append("Code").append("CodeNotation").toString();
        if (codeNotation.isEmpty()) {

            errorList.add(new Error(String.format("N.%s", lastOfErrorCode),
                    "CodeNotation is required."));
        }
        if (checkLength(codeNotation, 30)) {

            errorList.add(new Error(String.format("R.%s", lastOfErrorCode),
                    "CodeNotation is out of range."));
        }
        return errorList;
    }

    /**
     * @param place
     * @return
     */
    private List<Error> checkProPlace(String place) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(place, 50)) {

            errorList.add(new Error("R.MessageHeader.Promulgation.Place",
                    "Place is out of range."));
        }
        return errorList;
    }

    /**
     * @param strDate
     * @return
     */
    private List<Error> checkProDate(String strDate) {

        List<Error> errorList = new ArrayList<>();

        String lastOfErrorCode = new StringBuilder("MessageHeader").append(".PromulgationDate").toString();

        if (validateJavaDate(strDate)) {
            int result = compareDate(strDate);

            if (result == ERROR_DATE_COMPARE) {
                errorList.add(new Error(String.format("T.%s", lastOfErrorCode),
                        "M.PromulgationDate is match type yyyy/MM/dd"));
            } else if (result > 0) {
                errorList.add(new Error(String.format("M.%s", lastOfErrorCode),
                        "M.PromulgationDate can't greater current date"));
            }
        } else {
            errorList.add(new Error("M.PromulgationDate",
                    "PromulgationDate is match type yyyy/MM/dd"));
        }

        return errorList;
    }

    /**
     * @param type
     * @return
     */
    private List<Error> checkDocType(int type) {

        List<Error> errorList = new ArrayList<>();

        if (type != 1 && type != 2) {
            errorList.add(new Error("M.MessageHeader.DocumentType.Type",
                    "Document Type value is only math '1' or '2' "));
        }

        return errorList;
    }

    /**
     * @param typeName
     * @return
     */
    private List<Error> checkDocTypeName(String typeName) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(typeName, 100)) {

            errorList.add(new Error("R.MessageHeader.DocumentType.TypeName",
                    "TypeName is out of range."));

        }
        return errorList;
    }

    /**
     * @param sphere
     * @return
     */
    private List<Error> checkSphereOfPromulgation(String sphere) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(sphere, 100)) {

            errorList.add(new Error(
                    "R.MessageHeader.OtherInfo.SphereOfPromulgation",
                    "SphereOfPromulgation is out of range."));
        }
        return errorList;
    }

    /**
     * @param priority
     * @return
     */
    private List<Error> checkPriority(int priority) {

        List<Integer> allowValue = new ArrayList<>(Arrays.asList(0, 1,
                2, 3, 4));

        List<Error> errorList = new ArrayList<>();

        if (!allowValue.contains(priority)) {
            errorList.add(new Error("M.MessageHeader.OtherInfo.Priority",
                    "Priority value is only math 0, 1, 2, 3, 4"));
        }

        return errorList;
    }

    /**
     * @param typerNotation
     * @return
     */
    private List<Error> checkTyperNotation(String typerNotation) {

        List<Error> errorList = new ArrayList<>();

        if (checkLength(typerNotation, 10)) {

            errorList.add(new Error("R.MessageHeader.OtherInfo.TyperNotation",
                    "TyperNotation is out of range."));
        }
        return errorList;
    }

    /**
     * @param promulgationAmount
     * @return
     */
    private List<Error> checkPromulgationAmount(int promulgationAmount) {

        List<Error> errorList = new ArrayList<>();

        if (promulgationAmount < 0) {

            errorList.add(new Error(
                    "M.MessageHeader.OtherInfo.PromulgationAmount",
                    "Promulgation amount type of UnsignShort"));

        }

        return errorList;
    }

    /**
     * @param pageAmount
     * @return
     */
    private List<Error> checkPageAmount(int pageAmount) {

        List<Error> errorList = new ArrayList<>();
        if (pageAmount < 0) {

            errorList.add(new Error("M.MessageHeader.OtherInfo.PageAmount",
                    "Page amount type of UnsignShort"));

        }

        return errorList;
    }

    private boolean checkDate(String strDate) {

        try {
            dateFormat.parse(strDate);
            return true;
        } catch (ParseException e) {
            return false;
        }

    }

    /**
     * @param target
     * @param length
     * @return
     */
    public boolean checkLength(String target, int length) {

        if (target == null) {
            return true;
        }

        return target.split("\\.").length > length;
    }

    private int compareDate(String strDate) {
        try {
            Date resultDate = simpleDateFormat.parse(strDate);

            Date now = Calendar.getInstance().getTime();

            return resultDate.compareTo(now);
        } catch (ParseException e) {
            return ERROR_DATE_COMPARE;
        }
    }


    private boolean validateJavaDate(String strDate) {
        /* Check if date is 'null' */
        if (!strDate.trim().equals("")) {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            simpleDateFormat.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try {
                Date javaDate = simpleDateFormat.parse(strDate);
                LOGGER.info("---------------------------------- Check PromulgationDate is valid yyyy/MM/dd -----------------------------> " + javaDate);
            } catch (ParseException e) {
                LOGGER.info("---------------------------------- Check PromulgationDate is not valid yyyy/MM/dd -----------------------------> " + strDate);
                return false;
            }
        }
        return true;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final int ERROR_DATE_COMPARE = -3;

    private final static Logger LOGGER = Logger.getLogger(Checker.class);
}
