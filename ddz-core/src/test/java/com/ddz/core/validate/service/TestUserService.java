package com.ddz.core.validate.service;

import com.ddz.core.validate.domain.TestUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class TestUserService {

    public String testUser(@Valid TestUserDTO user) {
        System.out.println("TestUserService 校验全部通过: " + user);
        return "TestUserService 校验全部通过: " + user;
    }
}
