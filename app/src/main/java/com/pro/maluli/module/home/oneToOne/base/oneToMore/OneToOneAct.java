package com.pro.maluli.module.home.oneToOne.base.oneToMore;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.lava.nertc.sdk.NERtc;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.LastTimeLiveEntity;
import com.pro.maluli.common.entity.OneToOneEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.WindowSoftModeAdjustResizeExecutor;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.home.oneToMore.base.oneToMore.OneToMoreAct;
import com.pro.maluli.module.home.oneToOne.queue.OneToOneQueueAct;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class OneToOneAct extends BaseMvpActivity<IOneToOneContraction.View, OneToOnePresenter> implements IOneToOneContraction.View {


    @BindView(R.id.finishIv)
    ImageView finishIv;
    @BindView(R.id.subImgLiveBgLL)
    LinearLayout subImgLiveBgLL;
    @BindView(R.id.livebgRiv)
    RoundedImageView livebgRiv;
    @BindView(R.id.inputLiveTitleEt)
    EditText inputLiveTitleEt;
    @BindView(R.id.inputMaxTv)
    TextView inputMaxTv;
    @BindView(R.id.xieyiTv)
    TextView xieyiTv;
    @BindView(R.id.startLiveTv)
    TextView startLiveTv;
    private String imgUrl;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private boolean isGranted = true;
    @Override
    public OneToOnePresenter initPresenter() {
        return new OneToOnePresenter(this);
    }

    @Override
    public void baseInitialization() {
//        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
//        BarUtils.setStatusBarLightMode(this, true);
        BarUtils.transparentStatusBar(this);
        WindowSoftModeAdjustResizeExecutor.assistActivity(this);
//        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_one_to_one;
    }

    @Override
    public void viewInitialization() {
        inputLiveTitleEt.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 15) {
                    inputMaxTv.setText(s.length() + "/" + 15);
                }
                inputMaxTv.setText(s.length() + "/" + 15);
                selectionStart = inputLiveTitleEt.getSelectionStart();
                selectionEnd = inputLiveTitleEt.getSelectionEnd();
                if (wordNum.length() > 15) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    inputLiveTitleEt.setText(s);
                    inputLiveTitleEt.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        String str = "开启直播则默认同意《主播协议》";

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《")+1;//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (AntiShake.check(widget.getId())) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("PROTOCOL_ID", "5");
                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.c_8e1d77));
                ds.setUnderlineText(false);
            }
        }, start, start + 4, 0);

        xieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
        xieyiTv.setText(ssb, TextView.BufferType.SPANNABLE);
        requestPermissionsIfNeeded();
    }
    private void requestPermissionsIfNeeded() {

        final List<String> missedPermissions = NERtc.checkPermission(this);
        if (missedPermissions.size() > 0) {
            isGranted = false;
            ActivityCompat.requestPermissions(this, missedPermissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        } else {
            isGranted = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {//
            case PERMISSION_REQUEST_CODE://如果申请权限回调的参数
                boolean hasGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(OneToOneAct.this, permissions[i]);
                        if (showRequestPermission) {
                            showToast("权限未申请");
                        }
                        hasGranted = false;
                    }
                }
                isGranted = hasGranted;

                break;

        }

    }
    @Override
    public void doBusiness() {
        presenter.getFansListInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.finishIv, R.id.subImgLiveBgLL, R.id.startLiveTv})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.finishIv:
                finish();
                break;
            case R.id.subImgLiveBgLL:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .isEnableCrop(true)
                        .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(1)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // onResult Callback
                                String sad = "";
                                List<File> files = new ArrayList<>();
                                for (int i = 0; i < result.size(); i++) {
                                    File file = new File(result.get(i).getCutPath());
                                    files.add(file);
                                }
                                presenter.subImg(files);
                            }

                            @Override
                            public void onCancel() {
                                // onCancel Callback
                            }
                        });
                break;
            case R.id.startLiveTv:
                if (!ToolUtils.isFastClick()) {
                    return;
                }
                if (TextUtils.isEmpty(inputLiveTitleEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入主播间标题");
                    return;
                }
                if (TextUtils.isEmpty(imgUrl)) {
                    ToastUtils.showShort("请选择主播封面");
                    return;
                }

                presenter.startLive("1", inputLiveTitleEt.getText().toString().trim(), imgUrl);
                break;
        }
    }

    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data) {
        imgUrl = data.getUrl().get(0);
        GlideUtils.loadImage(OneToOneAct.this, imgUrl, livebgRiv);
    }

    @Override
    public void setStartInfo(OneToOneEntity data) {
        Bundle bundle = new Bundle();
        bundle.putString("LIVE_BG", imgUrl);
        bundle.putString("LIVE_TITLE", inputLiveTitleEt.getText().toString().trim());
        bundle.putString("ANCHOR_ID", "");
        bundle.putBoolean("IS_FRIST_LIVE",true);
        gotoActivity(OneToOneQueueAct.class, true, bundle);
    }

    @Override
    public void setLastLive(LastTimeLiveEntity data) {
        if (data != null && !TextUtils.isEmpty(data.getImage())) {
            imgUrl = data.getImage();
            GlideUtils.loadImage(OneToOneAct.this, data.getImage(), livebgRiv);
            inputLiveTitleEt.setText(data.getTitle());
        }
    }
}