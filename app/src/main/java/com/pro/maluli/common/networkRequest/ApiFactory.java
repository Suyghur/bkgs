package com.pro.maluli.common.networkRequest;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.app.BKGSApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiFactory {
    private static ApiFactory instance;

    private ApiFactory() {
    }

    public static Api create() {
        if (instance == null) {
            synchronized (ApiFactory.class) {
                if (instance == null) {
                    instance = new ApiFactory();
                }
            }
        }
        return instance.get();
    }

    private Api get() {
        Gson gson = new GsonBuilder()
                .setLenient()
//                .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
//                .setPrettyPrinting() //对结果进行格式化，增加换行
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();
                                Request.Builder requestBuilder = request.newBuilder();
                                String token = AcacheUtil.getToken(BKGSApplication.getApp(), false);
                                requestBuilder.addHeader("Authorization", TextUtils.isEmpty(token) ? "" : token);
                                requestBuilder.addHeader("X-Device-Id", JPushInterface.getRegistrationID(BKGSApplication.getApp()));
                                return chain.proceed(requestBuilder.build());
                            }
                        })
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(@NonNull String message) {
                                Logger.d(message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .addConverterFactory(MyGsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
