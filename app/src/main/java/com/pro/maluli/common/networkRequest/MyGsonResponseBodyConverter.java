package com.pro.maluli.common.networkRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pro.maluli.common.base.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            JSONObject jsonObject = JSONObject.parseObject(response);

            if (jsonObject.containsKey("data") && jsonObject.containsKey("code") && jsonObject.containsKey("message")) {
                return gson.fromJson(response, type);
            } else {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setCode(200);
                baseResponse.setData(response);
                response = gson.toJson(baseResponse);
                return gson.fromJson(response, type);
            }

        } finally {
            value.close();
        }
    }

}
