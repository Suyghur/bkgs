package com.pro.maluli.common.networkRequest;

import com.google.gson.JsonParseException;
import com.pro.maluli.common.base.BaseView;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class ErrorConsumer implements Consumer<Throwable> {
    private BaseView mBaseView;

    public ErrorConsumer(BaseView baseView) {
        this.mBaseView = baseView;
    }

    @Override
    public void accept(Throwable e) throws Exception {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException
                || e instanceof ConnectException
                || e instanceof HttpException) {
            mBaseView.onError(-1, "网络太糟糕了!");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            mBaseView.onError(-1, "服务器开小差了!");
        } else {
            mBaseView.onError(-1, "服务器繁忙!!");
        }
    }
}
