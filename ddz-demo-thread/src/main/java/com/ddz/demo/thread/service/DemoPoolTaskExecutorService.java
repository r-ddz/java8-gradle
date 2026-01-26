package com.ddz.demo.thread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DemoPoolTaskExecutorService {


    @Async
    public void test1(int i) {
        // 阻塞2秒
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }
        log.info("【测试默认线程池 def-task 】 任务下标：{}", i);
    }

//    @Async("bigTaskPool")
//    public void test2() {
//        log.info("【测试大任务线程池 big-task】");
//    }

}
