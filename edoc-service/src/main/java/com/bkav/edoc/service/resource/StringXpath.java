/**
 *
 */
package com.bkav.edoc.service.resource;

public class StringXpath {

    static public String translateQuery = "translate(local-name(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')";

    static public String SERVICE_OPENRATIONS = "//*[" + translateQuery + "='operation']";

    // Attachment
	/*static public String ATT_VALUE = "//*[" + translateQuery + "='value'][1]";	
	static public String ATT_NAME = "//*[" + translateQuery + "='name'][1]";
	static public String ATT_CONTENT = "//*[" + translateQuery + "='attachment'][1]/@Content-Type";
	static public String ATT_ENCODING = "//*[" + translateQuery + "='attachment'][1]/@Content-Transfer-Encoding";*/

    static public String ATT_VALUE = "//*[local-name()='value'][1]";
    static public String ATT_NAME = "//*[local-name()='Reference']/@href=cid:dinhkem1[1]";
    static public String ATT_CONTENT = "//*[local-name()='Attachment'][1]/@Content-Type";
    static public String ATT_ENCODING = "//*[local-name()='Attachment'][1]/@Content-Transfer-Encoding";
    // End Attachment

    //Signature
    static public String SIGNATURE = "//*[local-name()='Signature']";

    // Envelope
    static public String ENV_MESSAGE_HEADER = "//*[" + translateQuery + "='messageheader'][1]";
    static public String HEADER_TRACE_HEADER_LIST = "//*[" + translateQuery + "='traceheaderlist'][1]";
    static public String HEADER_ERROR_LIST = "//*[" + translateQuery + "='errorlist'][1]";
    // End envelope

    // MessageHeader
    static public String MESSAGE_HEADER_FROM = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from'][1]";
    static public String MESSAGE_HEADER_FROM_ORGAN_ID = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='organid']";
    static public String MESSAGE_HEADER_FROM_ORGAN_IN_CHARGE = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='organizationincharge']";
    static public String MESSAGE_HEADER_FROM_ORGAN_NAME = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='organname']";
    static public String MESSAGE_HEADER_FROM_ORGAN_ADDR = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='organadd']";
    static public String MESSAGE_HEADER_FROM_ORGAN_EMAIL = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='email']";
    static public String MESSAGE_HEADER_FROM_ORGAN_TELEPHONE = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='telephone']";
    static public String MESSAGE_HEADER_FROM_ORGAN_FAX = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='fax']";
    static public String MESSAGE_HEADER_FROM_ORGAN_WEBSITE = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='from']//*[" + translateQuery + "='website']";


    static public String MESSAGE_HEADER_TO = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to']";
    static public String MESSAGE_HEADER_TO_ORGAN_ID = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='organid']";
    static public String MESSAGE_HEADER_TO_ORGAN_NAME = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='organname']";
    static public String MESSAGE_HEADER_TO_ORGAN_ADDR = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='organadd']";
    static public String MESSAGE_HEADER_TO_ORGAN_EMAIL = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='email']";
    static public String MESSAGE_HEADER_TO_ORGAN_TELEPHONE = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='telephone']";
    static public String MESSAGE_HEADER_TO_ORGAN_FAX = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='fax']";
    static public String MESSAGE_HEADER_TO_ORGAN_WEBSITE = "//*[" + translateQuery + "='messageheader']//*[" + translateQuery + "='to'][%d]//*[" + translateQuery + "='website']";


    static public String MESSAGE_HEADER_DOCUMENT_ID = "//*[" + translateQuery + "='documentid'][1]";

    static public String MESSAGE_HEADER_SUBJECT = "//*[" + translateQuery + "='subject'][1]";
    static public String MESSAGE_HEADER_CONTENT = "//*[" + translateQuery + "='content'][1]";
    static public String MESSAGE_HEADER_RESPONSE_DATE = "//*[" + translateQuery + "='duedate'][1]";
    static public String MESSAGE_HEADER_TOPLACES = "//*[" + translateQuery + "='toplaces'][1]";
    static public String MESSAGE_HEADER_OTHER_INFO = "//*[" + translateQuery + "='otherinfo'][1]";
    static public String MESSAGE_HEADER_TRACE_HEADER = "//*[" + translateQuery + "='traceheader'][1]";
    static public String MESSAGE_HEADER_APPENDIXES = "//*[" + translateQuery + "='otherinfo'][1]//*[" + translateQuery + "='appendixes'][1]";

    // End MessageHeader

    // From and To
    static public String FROM_AND_TO_ORGANID = "//*[" + translateQuery + "='organId'][1]";
    static public String FROM_AND_TO_ORGANADD = "//*[" + translateQuery + "='organadd'][1]";
    static public String FROM_AND_TO_EMAIL = "//*[" + translateQuery + "='email'][1]";
    static public String FROM_AND_TO_TELEPHONE = "//*[" + translateQuery + "='telephone'][1]";
    static public String FROM_AND_TO_FAX = "//*[" + translateQuery + "='fax'][1]";
    static public String FROM_AND_TO_WEB = "//*[" + translateQuery + "='website'][1]";
    // End From and To

    // Code
    static public String MESSAGE_HEADER_CODE = "//*[" + translateQuery + "='code'][1]";
    static public String MESSAGE_HEADER_CODE_NUMBER = "//*[" + translateQuery + "='code']//*[" + translateQuery + "='codenumber']";
    static public String MESSAGE_HEADER_CODE_NOTATION = "//*[" + translateQuery + "='code']//*[" + translateQuery + "='codenotation']";
    static public String CODE_NUMBER = "//*[" + translateQuery + "='codenumber'][1]";//*[local-name()=''][1]
    static public String CODE_NOTATION = "//*[" + translateQuery + "='codenotation'][1]";
    // End Code

    // PromylgationInfo
    static public String MESSAGE_HEADER_PROMULGATION_INFO = "//*[" + translateQuery + "='promulgationinfo'][1]";
    static public String MESSAGE_HEADER_PROMULGATION_INFO_PLACE = "//*[" + translateQuery + "='promulgationinfo']//*[" + translateQuery + "='place']";
    static public String MESSAGE_HEADER_PROMULGATION_INFO_DATE = "//*[" + translateQuery + "='promulgationinfo']//*[" + translateQuery + "='promulgationdate']";
    static public String PROINFO_PLACE = "//*[" + translateQuery + "='place'][1]";
    static public String PROINFO_DATE = "//*[" + translateQuery + "='promulgationdate'][1]";
    // End PromylgationInfo

    // Document Type
    static public String MESSAGE_HEADER_DOCUMENT_TYPE = "//*[" + translateQuery + "='documenttype'][1]";
    static public String MESSAGE_HEADER_DOCUMENT_TYPE_TYPE = "//*[" + translateQuery + "='documenttype']//*[" + translateQuery + "='type']";
    static public String MESSAGE_HEADER_DOCUMENT_TYPE_TYPE_NAME = "//*[" + translateQuery + "='documenttype']//*[" + translateQuery + "='typename']";
    static public String DOCTYPE_TYPE = "//*[" + translateQuery + "='type'][1]";
    static public String DOCTYPE_TYPENAME = "//*[" + translateQuery + "='typename'][1]";
    // End Document Type

    // Author
    static public String MESSAGE_HEADER_AUTHOR = "//*[" + translateQuery + "='signerinfo'][1]";
    static public String AUTHOR_COMPETENCE = "//*[" + translateQuery + "='competence'][1]";
    static public String AUTHOR_FUNCTION = "//*[" + translateQuery + "='position'][1]";
    static public String AUTHOR_FULLNAME = "//*[" + translateQuery + "='fullname'][1]";
    // End Author

    //ToPlaces
    static public String TOPLACES_PLACE = "//*[" + translateQuery + "='toplaces']//*[" + translateQuery + "='place']";
    //End ToPlaces

    //Appendixes
    static public String APPENDIXES_APPENDIX = "//*[" + translateQuery + "='appendixes']//*[" + translateQuery + "='appendix']";
    //End Appendixes

    //Other info
    static public String OTHER_INFO_PRIORITY = "//*[" + translateQuery + "='priority'][1]";
    static public String OTHER_INFO_SPHERE_OF_PROMULGARION = "//*[" + translateQuery + "='sphereofpromulgation'][1]";
    static public String OTHER_INFO_TYPENOTATION = "//*[" + translateQuery + "='typernotation'][1]";
    static public String OTHER_INFO_PROMULGAION_AMOUNT = "//*[" + translateQuery + "='promulgationamount'][1]";
    static public String OTHER_INFO_PAGE_AMOUNT = "//*[" + translateQuery + "='pageamount'][1]";
    //End Other info

    //ReportService
    static public String REPORT_DOCUMENT_ID = "//*[" + translateQuery + "='document']//*[" + translateQuery + "='documentId'][1]";
    static public String REPORT_DOCUMENT_SUBJECT = "//*[" + translateQuery + "='document']//*[" + translateQuery + "='subject'][1]";
    static public String REPORT_DOCUMENT_CODE_NUMBER = "//*[" + translateQuery + "='document']//*[" + translateQuery + "='code']//*[" + translateQuery + "='codenumber']";
    static public String REPORT_DOCUMENT_CODE_NOTATION = "//*[" + translateQuery + "='document']//*[" + translateQuery + "='code']//*[" + translateQuery + "='codenotation']";
    static public String REPORT_DOCUMENT_DESCRIPTION = "//*[" + translateQuery + "='document']//*[" + translateQuery + "='description'][1]";
    static public String REPORT_TYPE = "//*[" + translateQuery + "='type'][1]";
    //End ReportService
}

