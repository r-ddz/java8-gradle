package com.ddz.demo.dingtalk.service;

import cn.hutool.core.util.StrUtil;
import com.ddz.demo.dingtalk.config.DingTalkConfig;
import com.ddz.demo.dingtalk.helper.DefaultDingTalkHelper;
import com.ddz.demo.dingtalk.helper.DingTalkHelper;
import com.ddz.demo.dingtalk.helper.NullDingTalkHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class DingTalkService {

    private static final DingTalkHelper NULL_HELPER = new NullDingTalkHelper();

    @Autowired
    private DingTalkConfig dingTalkConfig;

    public DingTalkHelper createHelper(String robotKey) {
        if (StrUtil.isBlank(robotKey)) {
            log.error("入参不能为空");
            return NULL_HELPER;
        }
        String secret = null;
        String accessToken = null;
        if (dingTalkConfig != null && dingTalkConfig.getRobot() != null) {
            DingTalkConfig.RobotCfg robotCfg = dingTalkConfig.getRobot().get(robotKey);
            if (robotCfg != null) {
                secret = robotCfg.getSecret();
                accessToken = robotCfg.getToken();
            }
        }
        if (StrUtil.isBlank(secret) || StrUtil.isBlank(accessToken)) {
            log.error("找不到钉钉机器人配置: {}", robotKey);
            return NULL_HELPER;
        }
        return new DefaultDingTalkHelper(secret, accessToken);
    }




    // ========================= 静态工具类玩法 ===============================

    private static DingTalkService instance;
    @PostConstruct
    public void init() {
        instance = this;
    }

    public static DingTalkHelper create(String robotKey) {
        return instance.createHelper(robotKey);
    }

}
