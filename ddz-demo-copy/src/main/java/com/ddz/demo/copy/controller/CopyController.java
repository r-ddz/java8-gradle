package com.ddz.demo.copy.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ddz.demo.copy.json.ObjectMapperUtil;
import com.ddz.demo.copy.mapstruct.BeanConvertUtil;
import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import com.ddz.demo.core.model.dto.UserDTO;
import com.ddz.demo.core.model.info.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/demo/copy")
public class CopyController {

    @Autowired
    private BeanConvertUtil beanConvertUtil;

    /**
     * get 请求：简单请求
     */
    @GetMapping("/test")
    public String test() {
        UserDemo userDemo = UserDemoFactory.buildUser();

        // 编译生成get set 性能接近手写
        UserDTO userDTO = beanConvertUtil.copy(userDemo);


        log.info("【测试】 主线程已执行完，copy结果：{}", userDTO);
        return userDTO.toString();
    }


    @GetMapping("/test2")
    public String test2() {

        UserDemo userDemo = UserDemoFactory.buildUser();

        UserDTO userDTO = new UserDTO();

        // 反射实现，性能低于mapstruct，而且不支持深度拷贝
        BeanUtil.copyProperties(userDemo, userDTO);

        log.info("【测试2】 主线程已执行完，copy结果：{}", userDTO);
        return userDTO.toString();
    }


    @GetMapping("/test3")
    public String test3() {

        UserDemo userDemo = UserDemoFactory.buildUser();

        /*
            性能较差：需要序列化和反序列化实现，但自由度高，甚至可以转 map 对象，支持深度拷贝
            有说法称性能略高于 mapstruct，但未验证，估计两者相差不大，而且如果特别大的对象，序列化可能会慢于反射

            灵活性高：支持复杂对象图
            支持注解：可以使用Jackson注解控制序列化
            类型转换：自动处理不同类型转换
         */

        UserDTO userDTO = ObjectMapperUtil.JSON.convertValue(userDemo, UserDTO.class);
        UserInfo userInfo = ObjectMapperUtil.JSON.convertValue(userDemo, UserInfo.class);
        Map map = ObjectMapperUtil.JSON.convertValue(userDemo, Map.class);

        log.info("【测试3】 主线程已执行完，copy结果 userDTO：{}", userDTO);
        log.info("【测试3】 主线程已执行完，copy结果 userInfo：{}", userInfo);
        log.info("【测试3】 主线程已执行完，copy结果 map：{}", map);
        return userDTO.toString();
    }


}
