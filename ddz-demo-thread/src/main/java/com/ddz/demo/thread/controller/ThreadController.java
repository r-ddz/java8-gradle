package com.ddz.demo.thread.controller;

import com.ddz.demo.thread.service.DemoPoolTaskExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/demo/thread")
public class ThreadController {

    @Autowired
    private DemoPoolTaskExecutorService demoPoolTaskExecutorService;

    /**
     * get 请求：简单请求
     */
    @GetMapping("/test")
    public String test() {
        for (int i = 0; i < 100; i++) {
            demoPoolTaskExecutorService.test1(i);
//            demoPoolTaskExecutorService.test2();
        }
        return "success";
    }



}
