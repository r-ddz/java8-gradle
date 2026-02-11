package com.ddz.demo.dingtalk;

import com.ddz.demo.dingtalk.utils.DingTalkUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DingTalkTests {

    @Test
    public void test1() {

        String secret = "SECeeaf158057195cb3c3e1007422ef9f5fa38193546aa7c10799c0bbfd759a4b32";
        String accessToken = "e7488672d2f5cf7aee06e4d3daa811e11c66335a74a92938ec8c7fb882e67003";

        String title = "测试 title";
        // 定义模板，markdown场景下，需要在内容里加入 @对象
        String template =   "**首行加粗字体** <br>" +
                            "环境： **{}** <br>" +
                            "参数1：**{}** <br>" +
                            "参数2：**{}** <br> @ZIY00220103 @all";

        DingTalkUtil.create(secret, accessToken)
                .markdown(title, template, "dev", "测试12344", "测试666888")
//                .atAll()
                .atUser("ZIY00220103")
                .send();
    }

    @Test
    public void test2() {
        String secret = "SECeeaf158057195cb3c3e1007422ef9f5fa38193546aa7c10799c0bbfd759a4b32";
        String accessToken = "e7488672d2f5cf7aee06e4d3daa811e11c66335a74a92938ec8c7fb882e67003";

        // 内部群才能精确 @个人
        DingTalkUtil.create(secret, accessToken)
                .text("测试text121231224234")
//                .atAll()
                .atUser("ZIY00220103")
                .send();
    }

    @Test
    public void test3() {

        String secret = "SEC225a1a9f8ecfb6169a086eb8c4f49b768e7b4defbbfd92f447327a21cf767eea";
        String accessToken = "8a75234acfe48e03a927b80c5c1ff3e05709dc67ca9f7a68ea18e2a8cd34a6b0";

        // 非内部群只能 @所有人，不支持精确 @个人
        DingTalkUtil.create(secret, accessToken)
                .text("测试text121231224234")
                .atAll()
                .send();
    }

    @BeforeAll
    public static void before(){
        System.out.println("================================= 测试开始 =================================");
    }

    @AfterAll
    public static void after(){
        System.out.println("================================= 测试结束 =================================");
    }

}
