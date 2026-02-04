package com.ddz.demo.platform.send.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台枚举
 * @author admin
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    /** 默认平台 */
    BASE("默认BASE平台"),
    /** 淘宝平台 */
    TB("淘宝"),
    /** 淘宝平台 */
    JD("京东"),
    /** 淘宝平台 */
    PDD("拼多多");

    private final String desc;

}
