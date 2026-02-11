package com.ddz.demo.dingtalk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "ddz.demo.ding-talk-config")
public class DingTalkConfig {

    private String url = "https://oapi.dingtalk.com/robot/send";

    private Map<String, RobotCfg> robot = new HashMap<>();

    @Data
    public static class RobotCfg {
        private String secret;
        private String token;
    }
}
