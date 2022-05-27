package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter.IImgMarkContraction;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter.ImgMarkPresenter;

import java.util.ArrayList;
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
        imgUrl = getIntent().getStringExtra("Img_url");

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_add_mark_img;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("图片上传");
        if (!TextUtils.isEmpty(imgUrl)) {
            GlideUtils.loadImage(ImgMarkAct.this, imgUrl, pictureRiv);
        }
    }

    @OnClick({R.id.addMarkIv, R.id.submitTv, R.id.leftImg_ly})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.addMarkIv:
                if (addMarkIv.isSelected()) {
                    addMarkIv.setSelected(false);
                } else {
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