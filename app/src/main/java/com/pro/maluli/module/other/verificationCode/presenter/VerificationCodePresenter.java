package com.pro.maluli.module.other.verificationCode.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.networkRequest.ResponseCode;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.common.utils.AcacheUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.pro.maluli.common.utils.preferences.Preferences.saveLoginInfo;

public class VerificationCodePresenter extends BasePresenter<IVerificationCodeContraction.View> implements IVerificationCodeContraction.Presenter {
    public VerificationCodePresenter(Context context) {
        super(context);
    }

    @Override
    public void checkCode(String mobile, String codeType, String input) {
        showLoading(mContext);
        add(mService.changePassword(mobile, input, "", codeType)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        String asda = response.toString();
                        mView.setGoSetPassWord(mobile, input);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
//        mView.setCommission("'");
    }

    @Override
    public void getVerifiCationCode(String mobile) {
        showLoading(mContext);
        add(mService.getVerificationCode(mobile,"1")
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

    /**
     * 短信登陆如果没有设置密码，跳到设置密码页面
     *
     * @param mobile
     * @param type
     * @param input
     */
    @Override
    public void codeLogin(String mobile, String type, String input) {
        showLoading(mContext);
        add(mService.login(mobile, input, type, "", "")
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        if (response.getCode() == ResponseCode.NO_LOGIN_PWD) {
                            mView.setGoSetPassWord(mobile, input);
                            return;
                        }

                        Gson gson = new Gson();
                        String myJson = gson.toJson(response.getData());
                        String token = JSONObject.parseObject(myJson).getString("token");
                        AcacheUtil.saveToken(mContext, token);
                        String accid = JSONObject.parseObject(myJson).getString("accid");
                        String accid_token = JSONObject.parseObject(myJson).getString("accid_token");
                        LoginInfo loginInfo = new LoginInfo(accid, accid_token);
                        saveLoginInfo(loginInfo);
                        mView.loginSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
    }

    @Override
    public void bindMobile(String mobile, String openid, String type, String code) {
        showLoading(mContext);
        add(mService.bindMobile(mobile, code, type, openid)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        if (response.getCode() == ResponseCode.NO_LOGIN_PWD) {
                            mView.setGoSetPassWord(mobile, code);
                            return;
                        }

                        Gson gson = new Gson();
                        String myJson = gson.toJson(response.getData());
                        String token = JSONObject.parseObject(myJson).getString("token");
                        AcacheUtil.saveToken(mContext, token);
                        String accid = JSONObject.parseObject(myJson).getString("accid");
                        String accid_token = JSONObject.parseObject(myJson).getString("accid_token");
                        LoginInfo loginInfo = new LoginInfo(accid, accid_token);
                        saveLoginInfo(loginInfo);
                        ToastUtils.showShort("登录成功");
                        mView.loginSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        dismissLoading(mContext);
                    }
                }));
    }

    @Override
    public void register(String mobile, String inputCode) {
        showLoading(mContext);
        add(mService.register(mobile, inputCode)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        dismissLoading(mContext);
                        mView.setGoSetPassWord(mobile, inputCode);
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
