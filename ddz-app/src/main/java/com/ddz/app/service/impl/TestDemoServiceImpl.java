package com.ddz.app.service.impl;

import com.ddz.app.service.TestDemoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestDemoServiceImpl implements TestDemoService {

    @Override
    public String getServiceInfo() {
        return "DDZ测试服务 - 版本 1.0.0";
    }

    @Override
    public String testIntegration() {
        // 这里可以测试其他模块的集成
        return "集成测试准备就绪";
    }

    @Override
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("memory", Runtime.getRuntime().totalMemory());
        status.put("processors", Runtime.getRuntime().availableProcessors());
        status.put("javaVersion", System.getProperty("java.version"));
        return status;
    }

}
