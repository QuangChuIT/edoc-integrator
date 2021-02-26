/**
 *
 */
package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DocumentIds", namespace = StringPool.TARGET_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPendingDocumentIDResponse {

    @XmlElement(name = "DocumentId")
    protected List<Long> documentId;

    /**
     * @return the documentIds
     */
    public List<Long> getDocumentIds() {
        return documentId;
    }

    /**
     * @param documentId
     *            the documentIds to set
     */
    public void setDocumentIds(List<Long> documentId) {
        this.documentId = documentId;
    }

    /**
     * @param documentId
     */
    public GetPendingDocumentIDResponse(List<Long> documentId) {
        this.documentId = documentId;
    }

    public GetPendingDocumentIDResponse() {
        documentId = new ArrayList<>();
    }


}