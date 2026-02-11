package com.ddz.demo.dingtalk.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * 钉钉消息发送工具类
 *
 * 在线文档：https://open.dingtalk.com/document/dingstart/group-template-robot-sends-group-chat-message
 *
 * @author ddz
 */
@Slf4j
public class DingTalkUtil {

    private static final String SERVER_URL = "https://oapi.dingtalk.com/robot/send";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String SIGN_ALGORITHMS = "HmacSHA256";

    private final String secret;
    private final String accessToken;
    private final OapiRobotSendRequest request;
    private final OapiRobotSendRequest.At requestAt;

    public static DingTalkUtil create(String secret, String accessToken) {
        return new DingTalkUtil(secret, accessToken);
    }

    private DingTalkUtil(String secret, String accessToken) {
        this.secret = secret;
        this.accessToken = accessToken;
        this.request = new OapiRobotSendRequest();
        this.requestAt = new OapiRobotSendRequest.At();
    }

    public void send() {
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + this.secret;
            Mac mac = Mac.getInstance(SIGN_ALGORITHMS);
            mac.init(new SecretKeySpec(this.secret.getBytes(CHARSET_NAME), SIGN_ALGORITHMS));
            byte[] signData = mac.doFinal(stringToSign.getBytes(CHARSET_NAME));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),CHARSET_NAME);

            //sign字段和timestamp字段必须拼接到请求URL上，否则会出现 310000 的错误信息
            DingTalkClient client = new DefaultDingTalkClient(SERVER_URL + "?sign="+sign+"&timestamp="+timestamp);
            this.request.setAt(this.requestAt);
            OapiRobotSendResponse rsp = client.execute(this.request, this.accessToken);
            if (!rsp.isSuccess()) {
                log.error("发送钉钉消息失败：{}", rsp.getBody());
            }
        } catch (Exception e) {
            log.error("发送钉钉消息失败", e);
        }
    }

    public DingTalkUtil markdown(String title, String text) {
        this.request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        this.request.setMarkdown(markdown);
        return this;
    }

    public DingTalkUtil markdown(String title, String template, Object... params) {
        String text = StrUtil.format(template, params);
        return markdown(title, text);
    }

    public DingTalkUtil text(String content) {
        this.request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        this.request.setText(text);
        return this;
    }

    public DingTalkUtil text(String template, Object... params) {
        String content = StrUtil.format(template, params);
        return text(content);
    }

    public DingTalkUtil link(String title, String messageUrl, String text) {
        this.request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setTitle(title);
        link.setText(text);
        link.setMessageUrl(messageUrl);
        this.request.setLink(link);
        return this;
    }

    public DingTalkUtil atAll() {
        this.requestAt.setIsAtAll(true);
        return this;
    }

    public DingTalkUtil atUser(String... userIds) {
        if (CollUtil.isEmpty(requestAt.getAtUserIds())) {
            this.requestAt.setAtUserIds(new java.util.ArrayList<String>());
        }
        this.requestAt.getAtUserIds().addAll(Arrays.asList(userIds));
        return this;
    }

    public DingTalkUtil setAtUserIds(String... userIds) {
        this.requestAt.setAtUserIds(Arrays.asList(userIds));
        return this;
    }

    public DingTalkUtil atMobile(String... mobiles) {
        if (CollUtil.isEmpty(requestAt.getAtMobiles())) {
            this.requestAt.setAtMobiles(new java.util.ArrayList<String>());
        }
        this.requestAt.getAtMobiles().addAll(Arrays.asList(mobiles));
        return this;
    }

    public DingTalkUtil setAtMobiles(String... mobiles) {
        this.requestAt.setAtMobiles(Arrays.asList(mobiles));
        return this;
    }

}
