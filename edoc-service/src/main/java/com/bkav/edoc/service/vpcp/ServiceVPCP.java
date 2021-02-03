package com.bkav.edoc.service.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.Content;
import com.bkav.edoc.service.xml.base.builder.BuildException;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.status.Status;
import com.bkav.edoc.service.xml.status.builder.StatusXmlBuilder;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.vpcp.services.KnobstickServiceImp;
import com.vpcp.services.VnptProperties;
import com.vpcp.services.model.GetChangeStatusResult;
import com.vpcp.services.model.MessageType;
import com.vpcp.services.model.SendEdocResult;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.List;

public class ServiceVPCP {

    private static ServiceVPCP INSTANCE;

    private final KnobstickServiceImp knobstickServiceImp;

    private static final VnptProperties vnptProperties;

    static public ServiceVPCP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceVPCP();
        }
        return INSTANCE;
    }

    public ServiceVPCP() {
        this.knobstickServiceImp = new KnobstickServiceImp(vnptProperties);
    }

    public SendEdocResult sendDocument(MessageHeader messageHeader,
                                       TraceHeaderList traceHeaderList, List<AttachmentCacheEntry> attachments) throws BuildException {
        Content content = EdxmlBuilder.getInstance().create_edoc(messageHeader, traceHeaderList, attachments);
        String filePath = content.getContent().getPath();
        JSONObject header = new JSONObject();
        header.put("from", messageHeader.getFrom().getOrganId());
        header.put("servicetype", "eDoc");
        header.put("messagetype", MessageType.edoc);
        return this.knobstickServiceImp.sendEdoc(header.toString(), filePath);
    }

    public SendEdocResult sendStatus(Status status, String path) {
        SendEdocResult sendEdocResult = null;
        try {
            // build status edxml
            Content content = StatusXmlBuilder.build(status, path);
            String filePath = content.getContent().getPath();
            MessageStatus messageStatus = (MessageStatus) status.getHeader().getMessageHeader();
            JSONObject header = new JSONObject();
            header.put("from", messageStatus.getFrom().getOrganId());
            header.put("servicetype", "eDoc");
            header.put("messagetype", MessageType.status);
            sendEdocResult = this.knobstickServiceImp.sendEdoc(header.toString(), filePath);
        } catch (BuildException e) {
            // TODO Auto-generated catch block
            LOGGER.error("Error when send status to VPCP " + e);
        }
        return sendEdocResult;
    }

    public GetChangeStatusResult confirmReceived(String documentId, String status) {
        JSONObject header = new JSONObject();
        header.put("status", status);
        header.put("docid", documentId);
        return this.knobstickServiceImp.updateStatus(header.toString());
    }

    static {
        String vpcpEndpoint = PropsUtil.get("VPCP.endpoint");
        String systemId = PropsUtil.get("VPCP.systemId");
        String token = PropsUtil.get("VPCP.token");
        String attachmentDir = PropsUtil.get("VPCP.attachment.dir");
        int maxConnection = GetterUtil.getInteger(PropsUtil.get("VPCP.maxConnection"));
        int retry = GetterUtil.getInteger(PropsUtil.get("VPCP.retry"));
        vnptProperties = new VnptProperties(vpcpEndpoint, systemId, token, attachmentDir, maxConnection, retry);
    }

    private final static Logger LOGGER = Logger.getLogger(ServiceVPCP.class);
}
