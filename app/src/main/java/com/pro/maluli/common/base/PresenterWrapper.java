package com.pro.maluli.common.base;

import android.content.Context;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.networkRequest.Api;
import com.pro.maluli.common.networkRequest.ApiFactory;


public class PresenterWrapper<V extends BaseView> extends BasePresenter<V>{
    protected Api mService;
    protected Api mService1;

    public PresenterWrapper(Context context) {
        super(context);
        mService = ApiFactory.create();
        mService1 = ApiFactory.create();
    }
}
