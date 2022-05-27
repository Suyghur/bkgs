package com.pro.maluli.module.other.login.presenter;

import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.AcacheUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginPresenter extends BasePresenter<ILoginContraction.View> implements ILoginContraction.Presenter {
    public LoginPresenter(Context context) {
        super(context);
    }

    @Override
    public void getWelcomInfo() {
        showLoading(mContext);
        add(mService.GetWelcomInfo()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        String asda = response.toString();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
//        mView.setCommission("'");
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

    public void uploadImage(List<File> files) {
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            //必须加filename字段
            map.put("image" + (i + 1) + "\"; filename=\"" + file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        add(mService.uploadImage(map)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        String asda = response.toString();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
//        return Api.getDefault(ApiConstants.TYPE_HOST)
//                .uploadImage(map)
//                .compose(RxSchedulers.handleResult());
    }
}
