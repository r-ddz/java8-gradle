package com.ddz.core.email.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ddz.core.email.domain.Response.EmailResponse;
import com.ddz.core.email.domain.attachment.EmailAttachment;
import com.ddz.core.email.domain.request.EmailRequest;
import com.ddz.core.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class SpringEmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.from:}")
    private String from;

    @Override
    public EmailResponse<?> sendEmail(EmailRequest request) {

        // 创建邮件
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            // 设置发件人
            helper.setFrom(StrUtil.isNotBlank(from) ? from : username);
            // 收件人
            helper.setTo(request.getTo().toArray(new String[request.getTo().size()]));
            // 抄送
            if (CollUtil.isNotEmpty(request.getCc())) {
                helper.setCc(request.getCc().toArray(new String[request.getCc().size()]));
            }
            // 密送
            if (CollUtil.isNotEmpty(request.getBcc())) {
                helper.setBcc(request.getBcc().toArray(new String[request.getBcc().size()]));
            }
            // 主题
            helper.setSubject(request.getSubject());
            // 内容
            helper.setText(request.getContent(), request.isHtml());
            // 附件
            if (CollUtil.isNotEmpty(request.getAttachments())) {
                for (EmailAttachment attachment : request.getAttachments()) {
                    if (attachment.isBigFile() && attachment.getFile() != null) {
                        helper.addAttachment(attachment.getFileName(), attachment.getFile());
                    } else if (attachment.getBytes() != null) {
                        helper.addAttachment(attachment.getFileName(), new ByteArrayResource(attachment.getBytes()));
                    }
                }
            }
            // 发送邮件
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("【发送邮件】构建 helper 异常 => {}", e.getMessage(), e);
            return EmailResponse.error("【发送邮件】构建 helper 异常: " + e.getMessage(), e);
        } catch (MailException e) {
            log.error("【发送邮件】发生异常 => {}", e.getMessage(), e);
            return EmailResponse.error("发送邮件失败: " + e.getMessage(), e);
        } finally {
            // 临时文件尝试自动清理
            request.tryDeleteFile();
        }
        return EmailResponse.success("【SpringEmailServiceImpl】发送邮件成功");
    }

}
