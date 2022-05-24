package com.pro.maluli.module.other.findPassword.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.AcacheUtil;

import io.reactivex.functions.Consumer;

import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

public class FindPasswordPresenter extends BasePresenter<IFindPasswordContraction.View> implements IFindPasswordContraction.Presenter {
    public FindPasswordPresenter(Context context) {
        super(context);
    }
    @Override
    public void subMitPwd(String mobile, String code, String pwd) {
        showLoading(mContext);
        add(mService.changePassword(mobile, code, pwd, "2")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response.getData());
                        String token = JSONObject.parseObject(myJson).getString("token");
                        AcacheUtil.saveToken(mContext, token);
                        String accid = JSONObject.parseObject(myJson).getString("accid");
                        String accid_token = JSONObject.parseObject(myJson).getString("accid_token");
                        int is_first = JSONObject.parseObject(myJson).getInteger("is_first");//第一次设置密码，跳到个人中心
                        LoginInfo loginInfo = new LoginInfo(accid, accid_token);
                        saveLoginInfo(loginInfo);
                        mView.setPwdSuccess(response.getMsg(),is_first);
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
