package com.ddz.core.email.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.ddz.core.email.domain.Response.EmailResponse;
import com.ddz.core.email.domain.attachment.EmailAttachment;
import com.ddz.core.email.domain.request.EmailRequest;
import com.ddz.core.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HutoolEmailServiceImpl implements EmailService {

    @Override
    public EmailResponse<?> sendEmail(EmailRequest request) {
        try {
            // 方式1：创建MailAccount实例，会自动加载mail.setting配置进行配置，没有文件时需要在下面的代码填充属性
//            MailAccount account = new MailAccount();

            // 方式2：指定配置文件路径
             MailAccount account = new MailAccount("config/mail.setting");

            // 方式3：通过代码设置SMTP服务器、端口、发件人、授权码等信息 【这种方式可以配合config对象从yaml里拿取配置】
//            account.setHost("smtp.qq.com");
//            account.setPort(465);
//            account.setAuth(true);
//            account.setFrom("your-email@qq.com");
//            account.setUser("your-email@qq.com"); // 通常是发件邮箱
//            account.setPass("你的SMTP授权码"); // 注意是授权码而非邮箱密码[citation:2]
//            account.setSslEnable(true);

            // 发送邮件
            Mail mail = Mail.create(account)
                    // 收件人
                    .setTos(request.getTo().toArray(new String[request.getTo().size()]))
                    // 主题
                    .setTitle(request.getSubject())
                    // 内容
                    .setContent(request.getContent())
                    .setHtml(request.isHtml());
            // 抄送
            if (CollUtil.isNotEmpty(request.getCc())) {
                mail.setCcs(request.getCc().toArray(new String[request.getCc().size()]));
            }
            // 密送
            if (CollUtil.isNotEmpty(request.getBcc())) {
                mail.setBccs(request.getBcc().toArray(new String[request.getBcc().size()]));
            }
            // 附件
            List<File> files = new ArrayList<>();
            if (CollUtil.isNotEmpty(request.getAttachments())) {
                for (EmailAttachment attachment : request.getAttachments()) {
                    if (attachment.getFile() == null) {
                        return EmailResponse.error("【发送邮件失败】HutoolEmail 目前仅支持 File 类型附件");
                    }
                    files.add(attachment.getFile());
                }
            }
            if (CollUtil.isNotEmpty(files)) {
                mail.setFiles(files.toArray(new File[files.size()]));
            }
            // 发邮件
            mail.send();
        } catch (Exception e) {
            log.error("【发送邮件】发生异常 => {}", e.getMessage(), e);
            return EmailResponse.error("发送邮件失败: " + e.getMessage(), e);
        } finally {
            // 临时文件尝试自动清理
            request.tryDeleteFile();
        }
        return EmailResponse.success("【HutoolEmailServiceImpl】发送邮件成功");
    }

}
