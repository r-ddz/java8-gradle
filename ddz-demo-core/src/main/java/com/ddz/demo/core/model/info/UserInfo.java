package com.ddz.demo.core.model.info;

import lombok.Data;

@Data
public class UserInfo {

    private Long id;
    private String code;
    private String name;
    private Integer age;
    private String sex;
    private String status;
    private String remark;

}
