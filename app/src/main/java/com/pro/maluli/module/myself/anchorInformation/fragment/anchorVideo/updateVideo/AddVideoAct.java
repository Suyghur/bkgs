package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hw.videoprocessor.VideoProcessor;
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
    private int maxVideoTime;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

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
        editGgEt.addTextChangedListener(new TextWatcher() {
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
                //TextView显示剩余字数
                inputMaxTv.setText(s.length() + "/" + 200);
                selectionStart = editGgEt.getSelectionStart();
                selectionEnd = editGgEt.getSelectionEnd();
                if (wordNum.length() > 200) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    editGgEt.setText(s);
                    editGgEt.setSelection(tempSelection);//设置光标在最后
                }

            }
        });
        intentActivityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
                        .videoQuality(0)
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
        addvideoIv.performClick();
    }

    @Override
    public void addVideoSuccess(String msg) {
        ToastUtils.showShort(msg);
        finish();
    }
}