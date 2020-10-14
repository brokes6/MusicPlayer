package com.example.musicplayerdome.api;

import android.util.Log;

import com.example.musicplayerdome.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 网络请求工具
 * Created By fxb on 2019/7/12
 */
public class ApiEngine {
    private static final String TAG = "ApiEngine";
    private volatile static ApiEngine apiEngine;
    private Retrofit retrofit;

    private ApiEngine() {

        //添加网络拦截器
        NetWorkInterceptor netWorkInterceptor = new NetWorkInterceptor();
        //解析返回结果的Interceptor
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        //缓存
//        int size = 1024 * 1024 * 100;
//        File cacheFile = new File(App.getContext().getCacheDir(), "OkHttpCache");
//        Cache cache = new Cache(cacheFile, size);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApplication.getContext()));

        //拦截器
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(netWorkInterceptor)
                .addInterceptor(responseInterceptor)
                .cookieJar(cookieJar)
//                .cache(cache)
                .build();

        Gson gson = new Gson();
        if (ApiService.BASE_URL==null){
            throw new IllegalArgumentException("请检查api文件夹中的ApiService的BASE_URL地址是否正确，如果是使用本地服务，请确保在同一个WLAN（不能是手机端的热点）下");
        }
        //开启retrofit
        retrofit = new Retrofit.Builder()
                //指定主url
                .baseUrl(ApiService.BASE_URL)
                //指定拦截器
                .client(client)
                //指定使用Gson解析
                .addConverterFactory(GsonConverterFactory.create(gson))
                //设置支持Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //获取实例
    public static ApiEngine getInstance() {
        if (apiEngine == null) {
            synchronized (ApiEngine.class) {
                if (apiEngine == null) {
                    apiEngine = new ApiEngine();
                }
            }
        }
        return apiEngine;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
