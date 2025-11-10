package com.ddz.core.email.domain.request;

import com.ddz.core.email.domain.attachment.EmailAttachment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailRequestBuilder {

    private EmailRequest emailRequest;

    public EmailRequestBuilder() {
        this.emailRequest = EmailRequestFactory.createEmailRequest();
        // 一些默认值
        this.html(true);
    }

    public EmailRequest build() {
        return this.emailRequest;
    }

    public EmailRequestBuilder addTo(String... tos) {
        return addTo(Arrays.asList(tos));
    }

    public EmailRequestBuilder addTo(List<String> tos) {
        if (this.emailRequest.getTo() == null) {
            this.emailRequest.setTo(new ArrayList<>());
        }
        this.emailRequest.getTo().addAll(tos);
        return this;
    }

    public EmailRequestBuilder addCc(String... ccs) {
        return addCc(Arrays.asList(ccs));
    }

    public EmailRequestBuilder addCc(List<String> ccs) {
        if (this.emailRequest.getCc() == null) {
            this.emailRequest.setCc(new ArrayList<>());
        }
        this.emailRequest.getCc().addAll(ccs);
        return this;
    }

    public EmailRequestBuilder addBcc(String... bcc) {
        return addBcc(Arrays.asList(bcc));
    }

    public EmailRequestBuilder addBcc(List<String> bcc) {
        if (this.emailRequest.getBcc() == null) {
            this.emailRequest.setBcc(new ArrayList<>());
        }
        this.emailRequest.getBcc().addAll(bcc);
        return this;
    }

    public EmailRequestBuilder subject(String subject) {
        this.emailRequest.setSubject(subject);
        return this;
    }

    public EmailRequestBuilder content(String content) {
        this.emailRequest.setContent(content);
        return this;
    }

    public EmailRequestBuilder html(boolean html) {
        this.emailRequest.setHtml(html);
        return this;
    }

    public EmailRequestBuilder addAttachment(EmailAttachment attachment) {
        if (this.emailRequest.getAttachments() == null) {
            this.emailRequest.setAttachments(new ArrayList<>());
        }
        this.emailRequest.getAttachments().add(attachment);
        return this;
    }

    public EmailRequestBuilder addAttachment(String fileName, byte[] bytes) {
        return addAttachment(new EmailAttachment(fileName, bytes));
    }

    public EmailRequestBuilder addAttachment(String fileName, File file) {
        return addAttachment(fileName, file, true);
    }

    public EmailRequestBuilder addAttachment(String fileName, File file, boolean tempFileAutoDelete) {
        return addAttachment(new EmailAttachment(fileName, file, tempFileAutoDelete));
    }

}
