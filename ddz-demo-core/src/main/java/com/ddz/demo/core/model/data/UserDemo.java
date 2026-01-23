package com.ddz.demo.core.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDemo {

    private Long id;
    private String code;
    private String name;
    private Integer age;
    private String sex;
    private String status;
    private String remark;

}
