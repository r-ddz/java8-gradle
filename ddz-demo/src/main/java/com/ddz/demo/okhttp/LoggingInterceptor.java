package com.ddz.demo.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoggingInterceptor  implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 记录请求信息
        log.debug("请求: {} {}，Headers: {}",
                request.method(),
                request.url(),
                request.headers());

        long startTime = System.nanoTime();
        Response response = chain.proceed(request);
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

        // 记录响应信息
        log.debug("响应: {} {}，状态码: {}，耗时: {}ms",
                response.body().string(),
                request.url(),
                response.code(),
                elapsedTime);

        return response;
    }

}
