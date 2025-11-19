package com.ddz.core.email;

import com.ddz.core.email.service.HutoolEmailService;
import com.ddz.core.email.service.SpringEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EmailTestApplication.class)
public class EmailTests {

    @Autowired
    private SpringEmailService springEmailService;

    @Test
    public void testSpringEmail() {
        springEmailService.sendEmailDemo();
    }

    @Autowired
    private HutoolEmailService hutoolEmailService;

    @Test
    public void testHutoolEmail() {
        hutoolEmailService.sendEmailDemo();
    }

}

