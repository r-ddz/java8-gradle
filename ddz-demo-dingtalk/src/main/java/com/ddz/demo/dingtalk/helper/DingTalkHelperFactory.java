package com.ddz.demo.dingtalk.helper;

import cn.hutool.core.util.StrUtil;
import com.ddz.demo.dingtalk.config.DingTalkConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DingTalkHelperFactory {

    private static final DingTalkHelper NULL_HELPER = new NullDingTalkHelper();

    @Autowired
    private DingTalkConfig dingTalkConfig;

    public DingTalkHelper create(String robotKey) {
        if (StrUtil.isBlank(robotKey)) {
            log.error("【钉钉】查找机器人入参不能为空");
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
            log.error("【钉钉】找不到钉钉机器人配置: {}", robotKey);
            return NULL_HELPER;
        }
        return new DefaultDingTalkHelper(secret, accessToken);
    }

}
