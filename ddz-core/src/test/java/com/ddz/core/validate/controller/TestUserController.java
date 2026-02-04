package com.ddz.core.validate.controller;

import com.ddz.core.validate.domain.TestUserDTO;
import com.ddz.core.validate.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TestUserController {

    @Autowired
    private TestUserService testUserService;

    @Validated
    public String testUser(@Valid TestUserDTO user) {
        return testUserService.testUser(user);
    }

}
