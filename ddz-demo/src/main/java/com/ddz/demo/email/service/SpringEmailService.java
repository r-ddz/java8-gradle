package com.ddz.demo.email.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class SpringEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.from:}")
    private String from;

    /**
     * 发送邮件 Demo
     * @return
     */
    public void sendEmailDemo() {
        // 附件
        File file = new File("C:\\Users\\74066\\Desktop\\测试文件1.pdf");
        String fileName = "测试文件1.pdf";

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            // 设置发件人
            helper.setFrom(StrUtil.isNotBlank(from) ? from : username);
            // 收件人，可以添加多个，add方法可以一个个追加
            helper.setTo("xxxxxxx@qq.com");
            // 抄送
//            helper.setCc("xxxxxxx@qq.com");
            // 密送
//            helper.setBcc("xxxxxxx@qq.com");
            // 主题
            helper.setSubject("邮件标题");
            // 内容，将图片转换为Base64嵌入html
            helper.setText(buildHtmlEmailText("九州通", "static/images/jzt_logo.png"), true);
            // 附件
            helper.addAttachment(fileName, file);
            // 发送邮件
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("【发送邮件】构建 helper 异常 => {}", e.getMessage(), e);
        } catch (MailException e) {
            log.error("【发送邮件】发生异常 => {}", e.getMessage(), e);
        } finally {
            // 临时文件尝试自动清理
//            tryDeleteFile(file);
        }
        log.info("【SpringEmailService】发送邮件成功");
    }

    /**
     * 构建HTML邮件内容
     * @param company
     * @param logoPath
     * @return
     */
    private String buildHtmlEmailText(String company, String logoPath) {
        String logoHtml = imageToBase64(logoPath);
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        if (StrUtil.isNotEmpty(logoHtml)) {
            html.append("<img src='data:image/png;base64,").append(logoHtml).append("' style='max-width: 380px;'><br><br>");
        }
        html.append("<p><b style='font-size: 16px; color: #2c3e50;'>尊敬的").append(company).append("公司：</b></p><br>");
        html.append("<p><b style='font-size: 16px; color: #2c3e50;'>您好！以下是您的上季九州通业务简报，请查收</b></p><br>");
        html.append("</body></html>");
        return html.toString();
    }

    /**
     * 图片转换为Base64
     * @param logoPath
     * @return
     */
    private String imageToBase64(String logoPath) {
        try {
            ClassPathResource resource = new ClassPathResource(logoPath);
            if (resource.exists()) {
                byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (IOException e) {
            log.error("转换图片为Base64失败: {}", logoPath, e);
        }
        return "";
    }

    /**
     * 临时文件尝试自动清理
     * @param file
     */
    private void tryDeleteFile(File file) {
        try {
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // 记录删除失败的日志，但不中断其他文件的删除操作
            // 可以根据实际需求使用具体的日志框架
            // 捕获其他可能的异常
        }
    }

}
