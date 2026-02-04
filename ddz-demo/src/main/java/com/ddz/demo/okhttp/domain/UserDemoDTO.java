package com.ddz.demo.okhttp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "userDemoDTO") // 根元素名称，标识这是 XML 的根元素
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDemoDTO {

    private Long id;
    private String code;
    private String name;
    private Integer age;
    private String remark;

    /**
     * 仅用于构建测试数据
     *
     * @return
     */
    public static UserDemoDTO buildUser() {
        return new UserDemoDTO(1L, "123456", "张三", 18, "测试备注");
    }

    /**
     * 仅用于构建测试数据
     *
     * @return
     */
    public static List<UserDemoDTO> buildUsers(int sise) {
        List<UserDemoDTO> users = new ArrayList<>();
        for (int i = 0; i < sise; i++) {
            users.add(new UserDemoDTO(1L+i, 10000+i +"", "张三"+i, 18+i, "测试备注"+i));
        }
        return users;
    }

}
