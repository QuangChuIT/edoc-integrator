package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.web.scheduler.bean.EmailSenderBean;

public class TestEmail {
    public static void main(String[] args) {
        EmailSenderBean emailSenderBean = new EmailSenderBean();
        try {
            System.out.println("Start sending email");
            emailSenderBean.runScheduleSendEmail();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Done");
    }
}
