package com.ddz.demo.platform.send.pusher;

import com.ddz.demo.platform.send.domain.PlatformConfig;
import com.ddz.demo.platform.send.enums.PlatformApiEnum;
import com.ddz.demo.platform.send.enums.PlatformEnum;
import com.ddz.demo.platform.send.service.DataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlatformPusher {

    @Autowired
    private DataPusher httpDataPusherImpl;

    @Autowired
    private DataPusher mqDataPusherImpl;

    @Autowired
    private DataPusher mysqlDataPusherImpl;

    public static final Map<String, Map<String, DataHandler>> DATA_HANDLER_SERVICES = new ConcurrentHashMap<>();

    static {
        for (PlatformApiEnum platformApiEnum : PlatformApiEnum.values()) {
            DATA_HANDLER_SERVICES.put(platformApiEnum.name(), new ConcurrentHashMap<>());
        }
    }

    public String push(PlatformConfig config) {
        return "推送成功";
    }


    private DataPusher getDataPusher (PlatformConfig config) {
//        switch (config.getApi()) {
//            case PlatformEnum.TB:
//                return httpDataPusherImpl;
//            case PlatformEnum.JD:
//                return mqDataPusherImpl;
//            case PlatformEnum.PDD:
//                return mysqlDataPusherImpl;
//            default:
//                return null;
//        }
        return null;
    }





}
