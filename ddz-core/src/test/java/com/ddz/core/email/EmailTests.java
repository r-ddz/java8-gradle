package com.ddz.core.email;

import com.ddz.core.email.domain.Response.EmailResponse;
import com.ddz.core.email.domain.request.EmailRequest;
import com.ddz.core.email.domain.request.EmailRequestFactory;
import com.ddz.core.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(classes = EmailTestApplication.class)
public class EmailTests {

    @Autowired
    @Qualifier("springEmailServiceImpl") // 多个实现的时候，指定具体的Bean名称，或者在其中一个Bean上使用@Primary注解设置为主要Bean。
//    @Qualifier("hutoolEmailServiceImpl")
    private EmailService emailService;

    @Test
    public void testSendSimpleEmail() {
        System.out.println("=============================== 开始 ===============================");
        EmailRequest request = EmailRequestFactory.builder()
                .addTo("596675559@qq.com")
                .subject("测试邮件")
                .content("<h3>测试邮件</h3><p>请查收附件中的文件。</p><br><p>此邮件由系统自动发送</p>")
                .addAttachment("测试文件1.pdf", new File("C:\\Users\\74066\\Desktop\\测试文件1.pdf"), false) // 测试文件不删
                .addAttachment("测试文件2.xlsx", new File("C:\\Users\\74066\\Desktop\\测试文件2.xlsx")) // 测试删一个
                .build();
        EmailResponse<?> response =emailService.sendEmail(request);

        System.out.println("=============================== response.getMsg() ===============================");
        System.out.println(response.getMsg());
        System.out.println("=============================== response.getMsg() ===============================");
        System.out.println("=============================== 结束 ===============================");
    }
}

