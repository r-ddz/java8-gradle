package com.ddz.demo.copy.controller;

import cn.hutool.core.bean.BeanUtil;
import com.ddz.demo.copy.mapstruct.BeanConvertUtil;
import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.data.UserDemoFactory;
import com.ddz.demo.core.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        // TODO 序列化方式，待补充
        return "success";
    }


}
