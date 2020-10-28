package com.bkav.edoc.service.commonutil;

import com.bkav.edoc.service.mineutil.ExtractMime;
import com.bkav.edoc.service.mineutil.XmlUtil;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.ErrorList;
import com.bkav.edoc.service.xml.base.header.Report;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

public class XmlChecker {

    public Report checkXmlTag(org.w3c.dom.Document envelope) {

        List<Error> errorList = new ArrayList<>();
        boolean isSuccess = true;

        Document domEnvDoc = XmlUtil.convertFromDom(envelope);

        Element rootElement = domEnvDoc.getRootElement();

        org.jdom2.Namespace envNs = rootElement.getNamespace();

        Element headerNode = extractMime.getSingerElement(rootElement,
                "Header", envNs);

        if (existTag(headerNode, new String[]{"MessageHeader"})) {
            errorList.add(new Error("N.MessageHeader",
                    "MessageHeader can not be null"));
        } else {

            Element messageHeader = extractMime.getSingerElement(headerNode,
                    "MessageHeader", ns);

            errorList.addAll(checkContentFrom(messageHeader));

            errorList.addAll(checkContentTo(messageHeader));

            // Check Code
            errorList.addAll(checkContentCode(messageHeader));

            // Check Promulgation
            errorList.addAll(checkContentProInfo(messageHeader));

            // Check DocumentType
            errorList.addAll(checkContentDocumentType(messageHeader));

            // Check Subject
            if (existTag(messageHeader, "Subject")) {
                errorList.add(new Error("N.MessageHeader.Subject",
                        "Subject can not be null"));
            }

            // Check content
            if (existTag(messageHeader, "Content")) {
                errorList.add(new Error("N.MessageHeader.Content",
                        "Content can not be null"));
            }

            // Check SignerInfo
            errorList.addAll(checkContentSignerInfo(messageHeader));

            // Check DueDate
            if (existTag(messageHeader, "DueDate")) {
                errorList.add(new Error("N.MessageHeader.DueDate",
                        "DueDate can not be null"));
            }

            // Check ToPlaces
            if (existTag(messageHeader, "ToPlaces")) {
                errorList.add(new Error("N.MessageHeader.ToPlaces",
                        "ToPlaces can not be null"));
            }

            // Check OtherInfo
            errorList.addAll(checkContentOtherInfo(messageHeader));
        }

        // Check TraceHeaderList
        if (existTag(headerNode, "TraceHeaderList")) {
            errorList.add(new Error("N.MessageHeader.TraceHeaderList",
                    "TraceHeaderList can not be null"));
        } else {
            if (existTag(headerNode, new String[]{"TraceHeaderList", "TraceHeader"})) {
                errorList.add(new Error(
                        "N.MessageHeader.TraceHeaderList.TraceHeader",
                        "TraceHeader can not be null"));
            }
        }

        // Check ErrorList
        if (existTag(headerNode, "ErrorList")) {
            errorList.add(new Error("N.MessageHeader.ErrorList",
                    "ErrorList can not be null"));
        }

        if (errorList.size() > 0) {
            isSuccess = false;
        }
        return new Report(isSuccess, new ErrorList(errorList));
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentFrom(Element messageHeader) {

        List<Error> errorList = new ArrayList<>();

        Element fromNode = extractMime.getSingerElement(messageHeader, "From",
                ns);

        if (fromNode == null) {
            errorList.add(new Error("N.MessageHeader.From",
                    "From can not be null"));
            return errorList;
        }

        if (existTag(fromNode, "OrganId")) {
            errorList.add(new Error("N.MessageHeader.From.OrganId",
                    "OrganId can not be null"));
        }

        if (existTag(fromNode, "OrganName")) {
            errorList.add(new Error("N.MessageHeader.From.OrganName",
                    "OrganName can not be null"));
        }

        if (existTag(fromNode, "OrganizationInCharge")) {
            errorList.add(new Error(
                    "N.MessageHeader.From.OrganizationInCharge",
                    "OrganizationInCharge can not be null"));
        }

        if (existTag(fromNode, "OrganAdd")) {
            errorList.add(new Error("N.MessageHeader.From.OrganAdd",
                    "Organ address can not be null"));
        }

        if (existTag(fromNode, "Email")) {
            errorList.add(new Error("N.MessageHeader.From.Email",
                    "Email address can not be null"));
        }

        if (existTag(fromNode, "Telephone")) {
            errorList.add(new Error("N.MessageHeader.From.Telephone",
                    "Telephone address can not be null"));
        }

        if (existTag(fromNode, "Fax")) {
            errorList.add(new Error("N.MessageHeader.From.Fax",
                    "Fax address can not be null"));
        }

        if (existTag(fromNode, "Website")) {
            errorList.add(new Error("N.MessageHeader.From.Website",
                    "Website address can not be null"));
        }

        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentTo(Element messageHeader) {

        List<Error> errorList = new ArrayList<>();

        List<Element> toNodes = extractMime.getMultiElement(messageHeader, "To",
                ns);

        if (toNodes == null) {
            errorList.add(new Error("N.MessageHeader.To",
                    "From can not be null"));
            return errorList;
        }

        for (Element element : toNodes) {
            if (existTag(element, "OrganId")) {
                errorList.add(new Error("N.MessageHeader.To.OrganId",
                        "OrganId can not be null"));
            }

            if (existTag(element, "OrganName")) {
                errorList.add(new Error("N.MessageHeader.To.OrganName",
                        "OrganName can not be null"));
            }

            if (existTag(element, "OrganAdd")) {
                errorList.add(new Error("N.MessageHeader.To.OrganAdd",
                        "Organ address can not be null"));
            }

            if (existTag(element, "Email")) {
                errorList.add(new Error("N.MessageHeader.To.Email",
                        "Email address can not be null"));
            }

            if (existTag(element, "Telephone")) {
                errorList.add(new Error("N.MessageHeader.To.Telephone",
                        "Telephone address can not be null"));
            }

            if (existTag(element, "Fax")) {
                errorList.add(new Error("N.MessageHeader.To.Fax",
                        "Fax address can not be null"));
            }

            if (existTag(element, "Website")) {
                errorList.add(new Error("N.MessageHeader.To.Website",
                        "Website address can not be null"));
            }
            if (errorList.size() > 0) {
                break;
            }
        }

        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentCode(Element messageHeader) {
        List<Error> errorList = new ArrayList<>();

        Element codeNode = extractMime.getSingerElement(messageHeader, "Code",
                ns);

        if (codeNode == null) {
            errorList.add(new Error("N.MessageHeader.Code",
                    "Code can not be null"));
        } else {

            if (existTag(codeNode, "CodeNumber")) {
                errorList.add(new Error("N.MessageHeader.Code.CodeNumber",
                        "CodeNumber can not be null"));
            }

            if (existTag(codeNode, "CodeNotation")) {
                errorList.add(new Error("N.MessageHeader.Code.CodeNotation",
                        "CodeNotation can not be null"));
            }
        }
        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentProInfo(Element messageHeader) {
        List<Error> errorList = new ArrayList<>();
        Element proInfoNode = extractMime.getSingerElement(messageHeader,
                "PromulgationInfo", ns);

        if (proInfoNode == null) {
            errorList.add(new Error("N.MessageHeader.PromulgationInfo",
                    "PromulgationInfo can not be null"));
        } else {

            if (existTag(proInfoNode, "Place")) {
                errorList.add(new Error(
                        "N.MessageHeader.PromulgationInfo.Place",
                        "Place can not be null"));
            }

            if (existTag(proInfoNode, "PromulgationDate")) {
                errorList.add(new Error(
                        "N.MessageHeader.PromulgationInfo.PromulgationDate",
                        "PromulgationDate can not be null"));
            }
        }
        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentDocumentType(Element messageHeader) {
        List<Error> errorList = new ArrayList<>();
        Element docTypeNode = extractMime.getSingerElement(messageHeader,
                "DocumentType", ns);

        if (docTypeNode == null) {
            errorList.add(new Error("N.MessageHeader.DocumentType",
                    "DocumentType can not be null"));
        } else {

            if (existTag(docTypeNode, "Type")) {
                errorList.add(new Error("N.MessageHeader.DocumentType.Type",
                        "Type can not be null"));
            }

            if (existTag(docTypeNode, "TypeName")) {
                errorList.add(new Error(
                        "N.MessageHeader.DocumentType.TypeName",
                        "TypeName can not be null"));
            }
        }
        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentSignerInfo(Element messageHeader) {
        List<Error> errorList = new ArrayList<>();
        Element signerInfoNode = extractMime.getSingerElement(messageHeader,
                "SignerInfo", ns);
        if (signerInfoNode == null) {
            errorList.add(new Error("N.MessageHeader.SignerInfo",
                    "SignerInfo can not be null"));
        } else {

            if (existTag(signerInfoNode, "Competence")) {
                errorList.add(new Error(
                        "N.MessageHeader.SignerInfo.Competence",
                        "Competence can not be null"));
            }

            if (existTag(signerInfoNode, "FullName")) {
                errorList.add(new Error("N.MessageHeader.SignerInfo.FullName",
                        "FullName can not be null"));
            }

            if (existTag(signerInfoNode, "Position")) {
                errorList.add(new Error("N.MessageHeader.SignerInfo.Position",
                        "Position can not be null"));
            }
        }
        return errorList;
    }

    /**
     * @param messageHeader
     * @return
     */
    private List<Error> checkContentOtherInfo(Element messageHeader) {
        List<Error> errorList = new ArrayList<>();
        Element otherInfoNode = extractMime.getSingerElement(messageHeader,
                "OtherInfo", ns);

        if (otherInfoNode == null) {
            errorList.add(new Error("N.MessageHeader.OtherInfo",
                    "OtherInfo can not be null"));
        } else {

            if (existTag(otherInfoNode, "Priority")) {
                errorList.add(new Error("N.MessageHeader.OtherInfo.Priority",
                        "Priority can not be null"));
            }

            if (existTag(otherInfoNode, "SphereOfPromulgation")) {
                errorList.add(new Error(
                        "N.MessageHeader.OtherInfo.SphereOfPromulgation",
                        "SphereOfPromulgation can not be null"));
            }

            if (existTag(otherInfoNode, "TyperNotation")) {
                errorList.add(new Error(
                        "N.MessageHeader.OtherInfo.TyperNotation",
                        "TypeNotation can not be null"));
            }

            if (existTag(otherInfoNode, "PromulgationAmount")) {
                errorList.add(new Error(
                        "N.MessageHeader.OtherInfo.PromulgationAmount",
                        "PromulgationAmount can not be null"));
            }

            if (existTag(otherInfoNode, "PageAmount")) {
                errorList.add(new Error("N.MessageHeader.OtherInfo.PageAmount",
                        "PageAmount can not be null"));
            }

            if (existTag(otherInfoNode, "Appendixes")) {
                errorList.add(new Error("N.MessageHeader.OtherInfo.Appendixes",
                        "Appendixes can not be null"));
            }
        }
        return errorList;
    }

    /**
     * @param parentElement
     * @param treePath
     * @return
     */
    private boolean existTag(Element parentElement, String[] treePath) {
        Element currentParent = parentElement;
        List<Element> childs;
        for (String item : treePath) {
            childs = currentParent.getChildren(item, ns);
            if (childs == null || childs.isEmpty()) {
                return true;
            }
            currentParent = childs.get(0);
        }
        return false;
    }

    private boolean existTag(Element parentElement, String localName) {
        List<Element> children = parentElement.getChildren(localName, ns);
        return children == null || children.isEmpty();
    }

    private static final Namespace ns = Namespace.getNamespace("edXML",
            "http://www.e-doc.vn/Schema/");

    private static final ExtractMime extractMime = new ExtractMime();
}

