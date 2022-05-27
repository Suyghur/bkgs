package com.pro.maluli.module.other.MsmLogin.presenter;

import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.AcacheUtil;

import io.reactivex.functions.Consumer;

public class MsmLoginPresenter extends BasePresenter<IMsmLoginContraction.View> implements IMsmLoginContraction.Presenter {
    public MsmLoginPresenter(Context context) {
        super(context);
    }

    @Override
    public void getVerifiCationCode(String mobile) {
        showLoading(mContext);
        add(mService.getVerificationCode(mobile, "1")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        String asda = response.toString();
                        ToastUtils.showShort(response.getMsg());
                        mView.getCodeSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dismissLoading(mContext);
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void login(String mobile, String type, String pwd, String openid) {
        add(mService.login(mobile, "", type, pwd, openid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {

                        dismissLoading(mContext);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response.getData());
                        if (response.getCode() == 1001) {
                            String openid = JSONObject.parseObject(myJson).getString("openid");
                            mView.gotoBindMobile(openid, type);
                            return;
                        }
                        String token = JSONObject.parseObject(myJson).getString("token");
                        AcacheUtil.saveToken(mContext, token);
                        String accid = JSONObject.parseObject(myJson).getString("accid");
                        String accid_token = JSONObject.parseObject(myJson).getString("accid_token");
                        LoginInfo loginInfo = new LoginInfo(accid, accid_token);
                        saveLoginInfo(loginInfo);
                        mView.loginSuccess(response.getMsg());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
    }
}
