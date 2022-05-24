package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.adapter.DefaultItemAnimator;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.adapter.EditImgAdapter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter.ImgMarkPresenter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter.IImgMarkContraction;
import com.yalantis.ucrop.view.OverlayView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class ImgMarkAct extends BaseMvpActivity<IImgMarkContraction.View, ImgMarkPresenter> implements IImgMarkContraction.View {
    @BindView(R.id.pictureRiv)
    RoundedImageView pictureRiv;
    @BindView(R.id.addMarkIv)
    ImageView addMarkIv;
    private List<AnchorImgEntity.PictureBean> listBeans = new ArrayList<>();
private String imgUrl;
    @Override
    public ImgMarkPresenter initPresenter() {
        return new ImgMarkPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        imgUrl=getIntent().getStringExtra("Img_url");

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_add_mark_img;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("图片上传");
        if (!TextUtils.isEmpty(imgUrl)){
            GlideUtils.loadImage(ImgMarkAct.this,imgUrl,pictureRiv);
        }
    }

    @OnClick({R.id.addMarkIv, R.id.submitTv, R.id.leftImg_ly})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.addMarkIv:
                if (addMarkIv.isSelected()){
                    addMarkIv.setSelected(false);
                }else {
                    addMarkIv.setSelected(true);
                }
                break;
            case R.id.submitTv:

            case R.id.leftImg_ly:
                Intent intent = new Intent();
                if (addMarkIv.isSelected()) {
                    intent.putExtra("ImageType", "1");
                } else {
                    intent.putExtra("ImageType", "0");
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        if (addMarkIv.isSelected()) {
            intent.putExtra("ImageType", "1");
        } else {
            intent.putExtra("ImageType", "0");
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void doBusiness() {
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}