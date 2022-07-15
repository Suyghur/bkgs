package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.CanTimeVideoEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.tailoring.TrimVideoActivity;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo.presenter.AddVideoPresenter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo.presenter.IAddVideoContraction;
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
public class AddVideoAct extends BaseMvpActivity<IAddVideoContraction.View, AddVideoPresenter> implements IAddVideoContraction.View {

    @BindView(R.id.addvideoIv)
    RoundedImageView addvideoIv;
    @BindView(R.id.editGgEt)
    EditText editGgEt;
    @BindView(R.id.inputMaxTv)
    TextView inputMaxTv;
    String videoUrl, videoUrlOnUpdate;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private int maxVideoTime;
    private int maxVideoDesc;

    @Override
    public AddVideoPresenter initPresenter() {
        return new AddVideoPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_add_video;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("发布小视频");
        setBackPress();
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    videoUrlOnUpdate = data.getStringExtra("Url");
                    if (!TextUtils.isEmpty(videoUrlOnUpdate)) {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(videoUrlOnUpdate);
                        addvideoIv.setImageBitmap(media.getFrameAtTime());
                    }

                }
            }
        });


    }

    @OnClick({R.id.addvideoIv, R.id.submitVideoTv})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.addvideoIv:
                videoUrlOnUpdate = "";
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofVideo())
                        .isEnableCrop(true)
                        .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .setCameraVideoFormat(PictureMimeType.MP4)
//                        .videoQuality(0)
                        .maxSelectNum(1)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // onResult Callback
                                Intent intent = new Intent(AddVideoAct.this, TrimVideoActivity.class);
                                intent.putExtra("videoPath", result.get(0).getRealPath());
                                intent.putExtra("maxVideoTime", maxVideoTime);
                                intentActivityResultLauncher.launch(intent);
//                                TrimVideoActivity.startActivity(AddVideoAct.this, result.get(0).getRealPath(),maxVideoTime);
//                                presenter.subImg(files);
                            }

                            @Override
                            public void onCancel() {
                                // onCancel Callback
                            }
                        });
                break;
            case R.id.submitVideoTv:
//                if (!ToolUtils.isFastClick()) {
//                    return;
//                }
                if (TextUtils.isEmpty(editGgEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入视频简介");
                    return;
                }
                if (TextUtils.isEmpty(videoUrlOnUpdate)) {
                    ToastUtils.showShort("请选择视频");
                    return;
                }
                List<File> files = new ArrayList<>();
                File file = new File(videoUrlOnUpdate);
                files.add(file);
                presenter.submitVideo(editGgEt.getText().toString().trim(), files);
//                presenter.subImg(files);

                break;
        }
    }


    @Override
    public void doBusiness() {
        presenter.getVideoCanTime();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data) {
        videoUrl = data.getUrl().get(0);
    }

    @Override
    public void setVideoTime(CanTimeVideoEntity data) {
        maxVideoTime = Integer.parseInt(data.getMax_video_time());
        maxVideoDesc = Integer.parseInt(data.getMax_video_desc());
        inputMaxTv.setText("0/" + maxVideoDesc);
        editGgEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxVideoDesc)});
        editGgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                inputMaxTv.setText(s.length() + "/" + maxVideoDesc);
            }
        });

        addvideoIv.performClick();
    }

    @Override
    public void addVideoSuccess(String msg) {
        ToastUtils.showShort(msg);
        finish();
    }
}