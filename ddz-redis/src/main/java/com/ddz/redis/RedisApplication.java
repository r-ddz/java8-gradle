package com.ddz.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 其实不需要这个启动类，只是没有这个启动类，编译器会认为RedisConfig所在包没有被扫描【无法自动装配。找不到 'RedisConnectionFactory' 类型的 Bean。】
//@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
