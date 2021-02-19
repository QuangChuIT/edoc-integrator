package com.bkav.edoc.sdk;

import com.bkav.edoc.sdk.edxml.entity.*;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.entity.env.Header;
import com.bkav.edoc.sdk.edxml.mineutil.ArchiveMime;
import com.bkav.edoc.sdk.edxml.util.BusinessDocType;
import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.google.common.collect.Lists;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageBuilder {

    public static void createEdoc_new() {
        Envelop ed = new Envelop();
        // khoi tao code cho van ban
        Code code = new Code("7816", "VPCP-TTĐT");

        // khoi tao cac don vi gui va nhan
        Organization org1 = new Organization("000.00.00.G22", "Văn phòng Chính phủ", "Văn phòng Chính phủ",
                "Số 1 Hoàng Hoa Thám, Quận Ba Đình, Hà Nội", "vpcp@gov.vn", "8043100", "048989898789", "vpcp.vn");

        Organization org5 = new Organization("000.00.00.H41", "UBND Tỉnh Nghệ An", "UBND Tỉnh Nghệ An", "Số 03, đường Trường Thi, Thành phố Vinh, Tỉnh Nghệ An, Việt Nam",
                "nghean@gov.vn", "0383 840418", "0383 843049", "www.nghean.vn");

        Organization org6 = new Organization("000.00.00.H14", "UBND Tỉnh Cao Bằng", "UBND Tỉnh Cao Bằng", "Số 011 - Đường Hoàng Đình Giong - Thành Phố Cao Bằng, Tỉnh Cao Bằng",
                "caobang@gov.vn", "02063852139", "0202183239", "www.caobang.gov.vn");

        Organization org7 = new Organization("000.00.00.G17", "Sở Thông tin truyền thông Hải Phòng", "Sở Thông tin truyền thông Hải Phòng", "số 62 - Võ Thị Sáu - Ngô Quyền - Hải Phòng",
                "bxd@moc.gov.vn", "(84-4) 3821 5137", "(023)37366617", "www.haiphong.gov.vn");
        // khoi tao don vi gui
        Organization from = org1;

        // khoi tao danh sach don vi nhan
        List<Organization> toes = Lists.newArrayList(org5, org6, org7);

        // khoi tao thong tin ban hanh
        PromulgationInfo promulgationInfo = new PromulgationInfo("Hà Nội", DateUtils.parse(new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date())));

        // khoi tao loai van ban
        DocumentType docType = new DocumentType(1, "Công văn");

        // khoi tao thong tin nguoi ky
        SignerInfo signerInfo = new SignerInfo("TL. BO TRUONG", "Cuc truong cuc ung dung cong nghe thong tin", "Nguyen Thanh Phuc");

        // khai bao tieu de
        String subject = "V/v kết nối, liên thông phần mềm quản lý văn bản";
        // khai bao noi dung
        String context = "V/v kết nối, liên thông phần mềm quản lý văn bản";

        // khoi tao header
        MessageHeader header = new MessageHeader(from, toes, code, promulgationInfo, docType, subject,
                context, null, signerInfo, new Date(), null, null);

        // dia diem noi nhan va luu van ban
        header.addToPlace("Cac bo va co quan ngang bo");
        header.addToPlace("Uy ban nhan dan cac tinh, TP");

        // khoi tao loai chi dao
        header.setSteeringType(1);

        // khoi tao cac thong tin khac cua van ban
        OtherInfo otherInfo = new OtherInfo();
        otherInfo.setPriority(0);
        otherInfo.setPromulgationAmount(2);
        otherInfo.setPageAmount(22);
        otherInfo.setSphereOfPromulgation("Liên thông văn bản quốc gia");
        List<String> paramList = new ArrayList<String>();
        paramList.add("Công văn về việc kết nối, liên thông phần mềm quản lý văn bản");
        otherInfo.setTyperNotation("TVC");
        otherInfo.setAppendixes(paramList);
        header.setOtherInfo(otherInfo);

        // khoi tao thong tin ma dinh danh cua van ban
        header.setDocumentId("000.00.00.G22,2015/09/30,7816/VPCP-TTĐT");

        // khoi tao thong tin van ban phan hoi va phuc dap
        ResponseFor responseFor = new ResponseFor("000.00.00.H41", "267/VPCP-TTĐT", new Date(), "000.00.04.G14,2012/08/06,267/VPCP-TTĐT");
        ResponseFor responseFor2 = new ResponseFor("000.00.00.H14", "267/VPCP-TTĐT", new Date(), "000.00.04.G14,2012/08/06,267/VPCP-TTĐT");

        // add responseFor
        //header.setResponseFor(responseFor);
        //header.setResponseFor(responseFor2);

        // khoi tao cac thong tin traceHeader
        TraceHeaderList trList = new TraceHeaderList();
        TraceHeader trace1 = new TraceHeader();
        trace1.setOrganId("000.00.00.G22");
        trace1.setTimestamp(DateUtils.parse(new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date())));
        trList.addTraceHeader(trace1);
        //TraceHeader trace2 = new TraceHeader();
        //trace2.setOrganId("000.00.00.H41");
        //trace2.setTimestamp(DateUtils.parse(new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date())));
        //trList.addTraceHeader(trace2);

        // khoi tao danh sach cac truong thong tin duoc van ban cap nhat
        BusinessDocumentInfo businessDocumentInfo = new BusinessDocumentInfo();
        businessDocumentInfo.setDocumentReceiver("1");
        businessDocumentInfo.setDocumentReceiver("1");

        // khoi tao danh sach cac don vi nhan bi thay doi khi cap nhat van ban
        ReceiverList receiverList = new ReceiverList();
        Receiver receiver = new Receiver("000.00.00.H41", "0");
        Receiver receiver2 = new Receiver("000.00.00.H14", "1");
        receiverList.addReceiver(receiver);
        receiverList.addReceiver(receiver2);

        // khoi tao thong tin ve nguoi xu ly
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setDepartment("Văn thư văn phòng");
        staffInfo.setStaff("Nguyễn Thị Ngọc Trâm");
        staffInfo.setEmail("vanthuvanphong@gov.vn");
        staffInfo.setMobile("84912000001");

        // khoi tao thong tin danh sach van ban bi thay the
        ReplacementInfoList replacementInfoList = new ReplacementInfoList();
        ReplacementInfo replacementInfo = new ReplacementInfo();
        OrganIdList organIdList = new OrganIdList();
        organIdList.addOrganId("000.00.00.H41");
        organIdList.addOrganId("000.00.00.H14");
        replacementInfo.setOrganIdList(organIdList);
        replacementInfo.setDocumentId("000.00.00.G22,2014/02/30,7806/VPCP-TTĐT");
        replacementInfoList.addReplacementInfo(replacementInfo);
        replacementInfoList.addReplacementInfo(replacementInfo);


        // khoi tao cac thong tin danh dau loai nghiep vu van ban
        Business bussiness = new Business();
        bussiness.setBusinessDocReason("Văn bản điện tử mới");
        bussiness.setBusinessDocType(BusinessDocType.NEW.ordinal());
        bussiness.setDocumentId("000.00.00.G22,2014/02/30,7806/VPCP-TTĐT");
        bussiness.setPaper(0);
        // add addreceiverList
        businessDocumentInfo.setReceiverList(receiverList);
        // add staffInfo
        bussiness.setStaffInfo(staffInfo);
        // add BussinessDocumentInfo
        //bussiness.addBussinessDocumentInfo(bussinessDocumentInfo);
        // add ReplacementInfoList
        //bussiness.addReplacementInfoList(replacementInfoList);

        // add TraceHeaderList
        trList.setBusiness(bussiness);

        // khoi tao thong tin chu ky
        SignReference signReference = new SignReference("", "http://www.w3.org/2000/09/xmldsig#sha1",
                "FwgIqsSYJshUS2+wlOM61L+q7Aw=");
        signReference.addToTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        signReference.addToTransform("http://www.w3.org/TR/xml-exc-c14n#");
        List<SignReference> listSignReference = new ArrayList<SignReference>();
        listSignReference.add(signReference);
        SignedInfo signedInfo = new SignedInfo("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", "http://www.w3.org/2000/09/xmldsig#rsa-sha1", listSignReference);
        String SignatureValue = "GbVb8n9+qFxz466Ag0sbpVdLPs2R0+9JBekp12UyarAZjoG0W/kPZZJ1auRZDcNcrSwzgkQMpqrjwxcy3ejzbY/ON5USUPgoYNYM8p4wsgQpEAeQaZ+EWLkkEBxYjb+iWEiAjYuJI7gtJoOyENcOK4fO050SXp2ctOc32LJMA5eEI6Hw7sxhc2LAgcPiynJHdDW2Z+eut6QZiUsbIF9+S3T6u/tfLImw39dgSlCxupwLPHepxuiLOqyd08HeJGCZufg9WqRBVyLFM76uCIaPRP5wwAdx72GVjPcG2kh+2jjrt7fqtJOufJzCObtQgPgBqIvDiZCGoOM41OQMiqtF3w==";
        KeyInfo keyInfo = new KeyInfo(new X509Data("CN=user05", "MIIFqTCCBJG"));
        Signature signature = new Signature(signedInfo, SignatureValue, keyInfo);

        // khoi tao header van ban
        ed.setHeader(new Header(header, trList, signature));

        // khoi tao file dinh kem
        ed.addAttachment(new Attachment("202", "JavaScriptNotesForProfessionals.pdf", "JavaScriptNotesForProfessionals.pdf", new File("D:\\JavaScriptNotesForProfessionals.pdf")));

        // ghi file ra thu muc
        File file = ArchiveMime.getInstance().createMime(ed, "SendDocument" + "." + header.getFrom().getOrganId(), "D:\\");
        if (file != null) {
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("Error !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public static void main(String[] args) throws Exception {
        createEdoc_new();
    }
}
