package com.ddz.demo.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {

    private Long id;
    private String code;
    private String name;
    private Integer age;
    private String sex;
    private String status;
    private String remark;

}
