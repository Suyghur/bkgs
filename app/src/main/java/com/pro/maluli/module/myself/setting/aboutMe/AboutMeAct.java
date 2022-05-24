package com.pro.maluli.module.myself.setting.aboutMe;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AboutMeEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.ForgetPwdDialog;
import com.pro.maluli.common.view.dialogview.ShareAppDialog;
import com.pro.maluli.module.myself.setting.aboutMe.presenter.AboutMePresenter;
import com.pro.maluli.module.myself.setting.aboutMe.presenter.IAboutMeContraction;
import com.pro.maluli.module.myself.setting.youthMode.YouthPassword.YouthPasswordAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AboutMeAct extends BaseMvpActivity<IAboutMeContraction.View, AboutMePresenter> implements IAboutMeContraction.View {


    @BindView(R.id.versionNumberTv)
    TextView versionNumberTv;
    @BindView(R.id.checkWebsiteTv)
    LinearLayout checkWebsiteTv;
    @BindView(R.id.hasMessageImg)
    ImageView hasMessageImg;
    @BindView(R.id.checkUpdatesTv)
    LinearLayout checkUpdatesTv;
    private String website, downLoadUrl, imgUrl;
    private boolean isCanUpdate;

    @Override
    public AboutMePresenter initPresenter() {
        return new AboutMePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }


    @Override
    public int setR_Layout() {
        return R.layout.act_about_me;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("关于APP");
        setBackPress();
        versionNumberTv.setText("版本号：" + ToolUtils.getAppVersionName(this));
    }

    @OnClick({R.id.shareAppLL, R.id.checkWebsiteTv, R.id.checkUpdatesTv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.shareAppLL:
                //分享
                ShareAppDialog dialogFragment = new ShareAppDialog();
                Bundle bundle = new Bundle();
                bundle.putString("IMG", imgUrl);
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "ForgetPwdDialog");
                dialogFragment.setOnShareAppListener(new ShareAppDialog.OnShareAppListener() {
                    @Override
                    public void gotoShare(int type) {
                        // 1 QQ 2 微信 3朋友圈，4 QQ空间
                        switch (type) {
                            case 1:
                                ToolUtils.shareImg(handler, QQ.NAME, imgUrl);
                                break;
                            case 2:
                                ToolUtils.shareImg(handler, Wechat.NAME, imgUrl);
                                break;
                            case 3:
                                ToolUtils.shareImg(handler, WechatMoments.NAME, imgUrl);
                                break;
                            case 4:
                                ToolUtils.shareImg(handler, QZone.NAME, imgUrl);
                                break;
                        }

                    }
                });
                break;
            case R.id.checkWebsiteTv:
                goToWeb(website);
                break;
            case R.id.checkUpdatesTv:
                if (isCanUpdate) {
                    BaseTipsDialog baseTipsDialog = new BaseTipsDialog();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("showContent", "有最新版本，是否去更新");
                    baseTipsDialog.setArguments(bundle1);
                    baseTipsDialog.show(getSupportFragmentManager(), "BaseTipsDialog");
                    baseTipsDialog.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            goToWeb(downLoadUrl);
                        }
                    });
                } else {
                    ToastUtils.showShort("已经是最新版本了");
                }

                break;
        }

    }

    //要用Handler回到主线程操作UI，否则会报错
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ登陆
                case 0:
                    ToastUtils.showShort("分享失败");
                    break;
                //微信登录
                case 1:
                    ToastUtils.showShort("分享成功");
                    break;
            }
        }
    };

    private void goToWeb(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void doBusiness() {
        presenter.getAboutInfo();
    }

    @Override
    public void setAboutMeInfo(AboutMeEntity data) {
        website = data.getWebsite().getUrl();
        imgUrl = data.getShare().getImage();
        downLoadUrl = data.getUpdate().getAndroid().getDownload();
        if (data.getUpdate().getAndroid().getVersion().equalsIgnoreCase(ToolUtils.getAppVersionName(this))) {
            isCanUpdate = false;
            hasMessageImg.setVisibility(View.GONE);
        } else {
            hasMessageImg.setVisibility(View.VISIBLE);
            isCanUpdate = true;
        }
    }
}