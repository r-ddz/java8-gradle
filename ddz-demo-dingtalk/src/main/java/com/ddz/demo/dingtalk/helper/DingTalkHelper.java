package com.ddz.demo.dingtalk.helper;

public interface DingTalkHelper {

    String SERVER_URL = "https://oapi.dingtalk.com/robot/send";
    String CHARSET_NAME = "UTF-8";
    String SIGN_ALGORITHMS = "HmacSHA256";


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
