package com.ddz.core.email.domain.request;

/**
 * 邮件消息工厂类，用于创建各种类型的邮件消息
 */
public class EmailRequestFactory {

    /**
     * 创建基础邮件
     */
    public static EmailRequest createEmailRequest() {
        EmailRequest emailRequest = new EmailRequest();
        return emailRequest;
    }

    /**
     * 获取邮件建造者
     * @return EmailRequestBuilder实例
     */
    public static EmailRequestBuilder builder() {
        return new EmailRequestBuilder();
    }

}
