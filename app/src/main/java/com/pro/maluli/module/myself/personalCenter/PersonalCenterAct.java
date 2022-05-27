package com.pro.maluli.module.myself.personalCenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.SelectGenderDialog;
import com.pro.maluli.module.main.base.MainActivity;
import com.pro.maluli.module.myself.personalCenter.presenter.IPersonalCenterContraction;
import com.pro.maluli.module.myself.personalCenter.presenter.PersonalCenterPresenter;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/29
 */
public class PersonalCenterAct extends BaseMvpActivity<IPersonalCenterContraction.View,
        PersonalCenterPresenter> implements IPersonalCenterContraction.View {

    @BindView(R.id.avatarCiv)
    CircleImageView avatarCiv;
    @BindView(R.id.userIdTv)
    TextView userIdTv;
    @BindView(R.id.inputUserNameEt)
    EditText inputUserNameEt;
    @BindView(R.id.genderTv)
    TextView genderTv;
    @BindView(R.id.right_tv)
    TextView right_tv;
    @BindView(R.id.selectGenderLL)
    LinearLayout selectGenderLL;
    private int genderType = 1;
    private String imgUrl, nickName;

    private boolean isFrisStart;//刚注册进入设置个人信息

    @Override
    public PersonalCenterPresenter initPresenter() {
        return new PersonalCenterPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        isFrisStart = getIntent().getBooleanExtra("isFrist", false);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_personal_center;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("个人信息");

        inputUserNameEt.setSelection(inputUserNameEt.getText().toString().trim().length());
        right_tv.setText("保存");
    }


    @Override
    public void doBusiness() {
        presenter.getUserinfo();
    }


    @OnClick({R.id.avatarCiv, R.id.selectGenderLL, R.id.leftImg_ly, R.id.right_tv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.avatarCiv:
//                PictureSelector.create(this)
//                        .openGallery(PictureMimeType.ofImage())
//                        .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
//                        .forResult(PictureConfig.CHOOSE_REQUEST);

                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .isEnableCrop(true)
                        .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_DISABLE)
                        .withAspectRatio(1, 1)
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
            case R.id.selectGenderLL:
                SelectGenderDialog dialogFragment = new SelectGenderDialog();
                Bundle args = new Bundle();
                dialogFragment.setArguments(args);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getSupportFragmentManager(), "SelectGenderDialog");
                dialogFragment.setOnConfirmListener(new SelectGenderDialog.OnSelectGenderListener() {
                    @Override
                    public void confirmSuccess(int genderType1) {
                        genderType = genderType1;
                        if (genderType == 1) {
                            genderTv.setText("男");
                        } else {
                            genderTv.setText("女");
                        }
                    }
                });
                break;
            case R.id.leftImg_ly:
                if (isFrisStart) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.right_tv:
                String name;
                if (inputUserNameEt.getText().toString().trim().equalsIgnoreCase(nickName)) {
                    name = "";
                } else {
                    name = inputUserNameEt.getText().toString().trim();
                }
                presenter.changerUserInfo(imgUrl, genderType, name);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // onResult Callback
//                    List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    @Override
    public void setUserInfo(UserInfoEntity entity) {
        GlideUtils.loadHeardImg(PersonalCenterAct.this, entity.getAvatar(), avatarCiv);
        imgUrl = entity.getAvatar();
        nickName = entity.getNickname();
        inputUserNameEt.setText(entity.getNickname());
        genderTv.setText(entity.getSex());

        if ("1".equalsIgnoreCase(entity.getSex())) {
            genderType = 1;
        } else {
            genderType = 2;
        }
//        if (entity.getIs_anchor() == 1) {
//            userIdTv.setVisibility(View.VISIBLE);
//            userIdTv.setText("ID：" + entity.getAnchor_id());
//        } else {
//            userIdTv.setVisibility(View.GONE);
//        }
    }

    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data) {
        imgUrl = "";
        if (data.getUrl().size() != 0) {
            imgUrl = data.getUrl().get(0);
        }
        GlideUtils.loadHeardImg(PersonalCenterAct.this, imgUrl, avatarCiv);
    }

    @Override
    public void setBackPress() {
        if (isFrisStart) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
        super.setBackPress();
    }

    @Override
    public void setUpdateSuccess(String msg) {
        ToastUtils.showShort(msg);
        if (isFrisStart) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}