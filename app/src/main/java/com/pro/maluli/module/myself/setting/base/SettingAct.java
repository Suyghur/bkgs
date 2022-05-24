package com.pro.maluli.module.myself.setting.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.PackageUtils;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.alipay.AuthResult;
import com.pro.maluli.common.utils.alipay.OrderInfoUtil2_0;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.StartOneToMoreLiveAct;
import com.pro.maluli.module.myself.setting.aboutMe.AboutMeAct;
import com.pro.maluli.module.myself.setting.base.presenter.ISettingContraction;
import com.pro.maluli.module.myself.setting.base.presenter.SettingPresenter;
import com.pro.maluli.module.myself.setting.blacklist.BlackListAct;
import com.pro.maluli.module.myself.setting.changeBindMobile.ChangeBindMobileAct;
import com.pro.maluli.module.myself.setting.feedback.base.FeedBackAct;
import com.pro.maluli.module.myself.setting.youthMode.base.YouthModeAct;
import com.pro.maluli.module.other.getCode.GetCodeAct;
import com.pro.maluli.module.other.login.LoginAct;
import com.tencent.bugly.proguard.B;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class SettingAct extends BaseMvpActivity<ISettingContraction.View, SettingPresenter> implements ISettingContraction.View {

    @BindView(R.id.leftImg)
    ImageView leftImg;
    @BindView(R.id.leftImg_ly)
    RelativeLayout leftImgLy;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.bindMobileTv)
    TextView bindMobileTv;
    @BindView(R.id.wechatTv)
    TextView wechatTv;
    @BindView(R.id.alpayBindTv)
    TextView alpayBindTv;
    @BindView(R.id.bindQQTv)
    TextView bindQQTv;
    @BindView(R.id.loginTipsTv)
    TextView loginTipsTv;
    @BindView(R.id.iv_title)
    ImageView ivTitle;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.bindAlipayLL)
    LinearLayout bindAlipayLL;
    @BindView(R.id.customerNewTv)
    View customerNewTv;
    @BindView(R.id.appUpdata)
    View appUpdata;
    private UserInfoEntity entity;
    private static final int SDK_AUTH_FLAG = 3;

    @Override
    public SettingPresenter initPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_setting;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("设置");
        setBackPress();
    }

    @Override
    public void doBusiness() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ToolUtils.isLogin(SettingAct.this, false)) {
            presenter.getUserInfo();
        } else {
            loginTipsTv.setText("去登录");
        }
    }

    @OnClick({R.id.blacklistLL, R.id.ChangePwdLL, R.id.bindMobileLL,
            R.id.bindWeChatLL, R.id.BindQQLL, R.id.bindAlipayLL,
            R.id.FeedbackLL, R.id.aboutAppLL, R.id.LoginOutLL, R.id.childModelLl})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//
//            return;
//        }
        switch (view.getId()) {
            case R.id.childModelLl:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                gotoActivity(YouthModeAct.class);
                break;
            case R.id.blacklistLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                gotoActivity(BlackListAct.class);
                break;
            case R.id.ChangePwdLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(ACEConstant.GET_CODE_TYPE, "1");
                bundle.putBoolean("changePwd", true);
                gotoActivity(GetCodeAct.class, false, bundle);

                break;
            case R.id.bindMobileLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                Bundle b = new Bundle();
                b.putString("Mobile", entity.getPhone());
                gotoActivity(ChangeBindMobileAct.class,false,b);
                break;
            case R.id.bindWeChatLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (TextUtils.isEmpty(entity.getBind_wx())) {
                    ToolUtils.loginWeChat(handler, this);
                    return;
                }
                BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
                Bundle bundle1 = new Bundle();
                bundle1.putString("showContent", "已绑定微信，是否解绑当前微信");
                baseTipsDialog.setArguments(bundle1);
                baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.unbindWechat("2");
                    }
                });
                break;
            case R.id.BindQQLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (TextUtils.isEmpty(entity.getBind_qq())) {
                    ToolUtils.loginQQ(handler, this);
                    return;
                }
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "已绑定QQ，是否解绑当前QQ");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.unbindWechat("1");
                    }
                });
                break;
            case R.id.bindAlipayLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                if (TextUtils.isEmpty(entity.getBind_alipay())) {
                    authV2(bindAlipayLL);
                    return;
                }
                BaseTipsDialog baseTipsDialog2 = new BaseTipsDialog();
                Bundle bundle3 = new Bundle();
                bundle3.putString("showContent", "已绑定支付宝，是否解绑当前支付宝");
                baseTipsDialog2.setArguments(bundle3);
                baseTipsDialog2.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog2.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.unbindAlipay();
                    }
                });
                break;
            case R.id.FeedbackLL:
                if (!ToolUtils.isLoginTips(SettingAct.this, getSupportFragmentManager())) {
                    return;
                }
                gotoActivity(FeedBackAct.class);
                break;
            case R.id.aboutAppLL:
//                if (!ToolUtils.isLogin(SettingAct.this, true)) {
//                    return;
//                }
                gotoActivity(AboutMeAct.class);
                break;
            case R.id.LoginOutLL:
                if (!ToolUtils.isLogin(SettingAct.this, true)) {
                    return;
                }
                BaseTipsDialog baseTipsDialog3 = new BaseTipsDialog();
                Bundle bundle4 = new Bundle();
                bundle4.putString("showContent", "确认退出登录");
                baseTipsDialog3.setArguments(bundle4);
                baseTipsDialog3.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog3.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        AcacheUtil.loginOut(SettingAct.this);
                        gotoActivity(LoginAct.class);
                        finish();
                    }
                });

                break;
        }
    }

    private void QQWeChatBind(String type, String weChatData) {
        presenter.bindWechatAndQQ(type, weChatData);
    }

    // TODO: 2021/9/6  
    //要用Handler回到主线程操作UI，否则会报错
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ登陆
                case 1:
                    String weChatData = (String) msg.obj;
                    QQWeChatBind("1", weChatData);
                    break;
                //微信登录
                case 2:
                    String weChatData1 = (String) msg.obj;
                    QQWeChatBind("2", weChatData1);
                    break;
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        String user_id = authResult.getUser_id();
                        String auth_code = authResult.getAuthCode();
                        String alipay_open_id = authResult.getAlipayOpenId();
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        presenter.bindAlipay(auth_code, alipay_open_id, user_id);
                        ToastUtils.showShort("授权成功");
                    } else {
                        ToastUtils.showShort("授权失败");
                        // 其他状态值则为授权失败
                    }
                    break;
                }
            }
        }
    };
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2019112069363025";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "2088531588331177";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "BKGS";
    /**
     * pkcs8 格式的商户私钥。
     * <p>
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     * <p>
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    /**
     * 支付宝账户授权业务示例
     */
    public void authV2(View v) {
//        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
//                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
//                || TextUtils.isEmpty(TARGET_ID)) {
//            showAlert(this, getString(R.string.error_auth_missing_partner_appid_rsa_private_target_id));
//            return;
//        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
//        final String authInfo = info + "&" + sign;
        final String authInfo = info;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(SettingAct.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    @Override
    public void setUserInfoSuccess(UserInfoEntity response) {
        entity = response;
        if (response.getIs_anchor() == 1) {
            bindAlipayLL.setVisibility(View.VISIBLE);
        } else {
            bindAlipayLL.setVisibility(View.GONE);
        }
        String sda = ToolUtils.settingphone(entity.getPhone());
        bindMobileTv.setText(sda);
        if (TextUtils.isEmpty(entity.getBind_wx())) {
            wechatTv.setText("未绑定");
        } else {
            wechatTv.setText(StringUtils.StringToNull(entity.getBind_wx()));
        }
        if (TextUtils.isEmpty(entity.getBind_qq())) {
            bindQQTv.setText("未绑定");
        } else {
            bindQQTv.setText(StringUtils.StringToNull(entity.getBind_qq()));
        }
        if (TextUtils.isEmpty(entity.getBind_alipay())) {
            alpayBindTv.setText("未绑定");
        } else {
            alpayBindTv.setText(StringUtils.StringToNull(entity.getBind_alipay()));
        }
        //意见反馈是否有信息
        if (response.getNew_report()==1){
            customerNewTv.setVisibility(View.VISIBLE);
        }else {
            customerNewTv.setVisibility(View.GONE);
        }
        //软件是否可以更新
        if (!PackageUtils.getVersionName(SettingAct.this).equalsIgnoreCase(response.getAndroid_version())) {
            appUpdata.setVisibility(View.VISIBLE);
        } else {
            appUpdata.setVisibility(View.GONE);
        }

    }

    @Override
    public void setSuccessBind(String msg) {
        ToastUtils.showShort(msg);
        presenter.getUserInfo();
    }

}