/**
 *
 */
package com.bkav.edoc.edxml.base.header;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

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