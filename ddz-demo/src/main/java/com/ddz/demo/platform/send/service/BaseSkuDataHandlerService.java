package com.ddz.demo.platform.send.service;

import com.ddz.demo.platform.send.enums.PlatformApiEnum;
import com.ddz.demo.platform.send.enums.PlatformEnum;

public class BaseSkuDataHandlerService implements DataHandler{
    @Override
    public String handle(String data) {
        return "";
    }

    @Override
    public String getApi() {
        return PlatformApiEnum.SKU.name();
    }

    @Override
    public String getPlatformCode() {
        return PlatformEnum.BASE.name();
    }

}
