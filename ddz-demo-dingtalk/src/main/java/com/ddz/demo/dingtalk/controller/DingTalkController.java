package com.ddz.demo.dingtalk.controller;

import com.ddz.demo.dingtalk.service.DingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/demo/dingtalk")
public class DingTalkController {


    @Autowired
    private DingTalkService dingTalkService;


    @GetMapping("/test")
    public String test() {

        dingTalkService.createHelper("test")
                .text("测试text121231224234")
                .send();


        dingTalkService.createHelper("robot1")
                .text("robot1   111111111")
                .send();


        DingTalkService.create("robot2")
                .text("robot2     静态玩法")
                .send();

        return "";
    }
}
