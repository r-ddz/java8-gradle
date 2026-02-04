package com.ddz.core.validate;

import com.ddz.core.validate.controller.TestUserController;
import com.ddz.core.validate.domain.TestUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ValidateTestApplication.class)
public class ValidateTests {

    @Autowired
    private TestUserController testUserController;

    @Test
    public void test() {
        System.out.println("================================ 校验开始 ================================");

        TestUserDTO user = new TestUserDTO();
        String str = testUserController.testUser(user);
        System.out.println(str);
        System.out.println("================================ 校验结束 ================================");
    }

}

