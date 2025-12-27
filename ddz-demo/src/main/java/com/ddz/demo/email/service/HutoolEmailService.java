package com.ddz.demo.email.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class HutoolEmailService {

    public void sendEmailDemo() {

        // 附件
        File file = new File("C:\\Users\\74066\\Desktop\\测试文件1.pdf");

        try {
            // 创建MailAccount实例，指定配置文件路径
//            MailAccount account = new MailAccount("config/mail.setting");
            MailAccount account = new MailAccount(MailAccount.MAIL_SETTING_PATHS[0]);
            // 另一种方式：通过代码设置SMTP服务器、端口、发件人、授权码等信息 【这种方式可以配合config对象从yaml里拿取配置】
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
                    .setTos("596675559@qq.com")
                    // 抄送
//                    .setCcs("xxxxxxxxx@qq.com")
                    // 密送
//                    .setBccs("xxxxxxxxxxx@qq.com")
                    // 主题
                    .setTitle("邮件hutool标题")
                    // 内容，将图片转换为Base64嵌入html
                    .setContent(buildHtmlEmailText("九州通hutool测试", "static/images/jzt_logo.png")).setHtml(true)
                    // 附件
                    .setFiles(file);
            // 发邮件
            mail.send();
        } catch (Exception e) {
            log.error("【发送邮件】发生异常 => {}", e.getMessage(), e);
        } finally {
            // 临时文件尝试自动清理
//            tryDeleteFile(file);
        }
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
