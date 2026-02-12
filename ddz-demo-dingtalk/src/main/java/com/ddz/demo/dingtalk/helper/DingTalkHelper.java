package com.ddz.demo.dingtalk.helper;

public interface DingTalkHelper {

    void send();

    DingTalkHelper markdown(String title, String text);

    DingTalkHelper markdown(String title, String template, Object... params);

    DingTalkHelper text(String content);

    DingTalkHelper text(String template, Object... params);

    DingTalkHelper link(String title, String messageUrl, String text);

    DingTalkHelper atAll();

    DingTalkHelper atUser(String... userIds);

    DingTalkHelper setAtUserIds(String... userIds);

    DingTalkHelper atMobile(String... mobiles);

    DingTalkHelper setAtMobiles(String... mobiles);

}
