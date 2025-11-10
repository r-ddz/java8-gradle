package com.ddz.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestDemoController {

    @GetMapping("/demo")
    public String demoPage(Model model) {
        model.addAttribute("title", "DDZ测试模块");
        model.addAttribute("message", "欢迎使用DDZ测试平台");
        model.addAttribute("version", "1.0.0");
        return "test/demo";
    }

    @GetMapping("/api/health")
    @ResponseBody
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "ddz-test");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/api/info")
    @ResponseBody
    public Map<String, Object> serviceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "DDZ测试服务");
        info.put("description", "用于测试其他模块功能的集成测试平台");
        info.put("version", "1.0.0");
        return info;
    }

}
