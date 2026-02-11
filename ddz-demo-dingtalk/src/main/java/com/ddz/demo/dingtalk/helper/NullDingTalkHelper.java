package com.ddz.demo.dingtalk.helper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NullDingTalkHelper implements DingTalkHelper{

    @Override
    public void send() {
        log.debug("【钉钉】空对象：无法发送消息，因为 Helper 创建失败");
    }

    @Override
    public DingTalkHelper markdown(String title, String text) {
        log.debug("【钉钉】空对象：忽略设置 markdown");
        return this;
    }

    @Override
    public DingTalkHelper markdown(String title, String template, Object... params) {
        log.debug("【钉钉】空对象：忽略设置 markdown");
        return this;
    }

    @Override
    public DingTalkHelper text(String content) {
        log.debug("【钉钉】空对象：忽略设置文本");
        return this;
    }

    @Override
    public DingTalkHelper text(String template, Object... params) {
        log.debug("【钉钉】空对象：忽略设置文本");
        return this;
    }

    @Override
    public DingTalkHelper link(String title, String messageUrl, String text) {
        log.debug("【钉钉】空对象：忽略设置 link");
        return this;
    }

    @Override
    public DingTalkHelper atAll() {
        log.debug("【钉钉】空对象：忽略 @所有人");
        return this;
    }

    @Override
    public DingTalkHelper atUser(String... userIds) {
        log.debug("【钉钉】空对象：忽略 @用户");
        return this;
    }

    @Override
    public DingTalkHelper setAtUserIds(String... userIds) {
        log.debug("【钉钉】空对象：忽略 @用户");
        return this;
    }

    @Override
    public DingTalkHelper atMobile(String... mobiles) {
        log.debug("【钉钉】空对象：忽略 @手机");
        return this;
    }

    @Override
    public DingTalkHelper setAtMobiles(String... mobiles) {
        log.debug("【钉钉】空对象：忽略 @手机");
        return this;
    }

}
