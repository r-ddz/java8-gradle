package com.ddz.demo.test;

import lombok.Getter;

/**
 * 系统模块枚举
 *
 * 用于标识系统中不同的功能模块，便于统一管理和维护
 */
@Getter
public enum SysModuleEnum {

    STAFF_MANAGEMENT("STAFF", "用户管理", "负责系统用户的增删改查及权限分配"),

    USER_MANAGEMENT("USER", "账号管理", "负责系统账号的增删改查及权限分配"),

    STAFF_ROLE_MANAGEMENT("STAFF_ROLE", "用户角色管理", "负责系统用户和角色绑定"),

    STAFF_BUSINESS_ORG_MANAGEMENT("STAFF_BUSINESS_ORG", "用户业务组织管理", "负责系统用户和业务组织绑定"),
    BUSINESS_ORG("BUSINESS_ORG", "业务组织管理", "负责业务组织管理"),
    ROLE("ROLE", "角色管理", "负责系统角色的启用禁用删除等"),
    ;

    /** 模块编码 */
    private final String code;

    /** 模块名称 */
    private final String name;

    /** 模块描述 */
    private final String description;

    SysModuleEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     */
    public static SysModuleEnum fromCode(String code) {
        for (SysModuleEnum module : SysModuleEnum.values()) {
            if (module.code.equalsIgnoreCase(code)) {
                return module;
            }
        }
        return null;
    }
}
