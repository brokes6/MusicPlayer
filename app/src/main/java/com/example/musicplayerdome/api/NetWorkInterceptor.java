package com.example.musicplayerdome.api;


import com.example.musicplayerdome.util.NetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 */
class NetWorkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //无网络时强制使用缓存
        //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去
        if (!NetUtil.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (NetUtil.isConnected()) {
            //有网络时，设置超时为0
            //当然如果你想在有网络的情况下都直接走网络，那么只需要
            //将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
            int maxStale = 0;
            response.newBuilder()
                    .header("Cache-Control", "public,max-age=" + maxStale)
                    .build();
        } else {
            //无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cache, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
