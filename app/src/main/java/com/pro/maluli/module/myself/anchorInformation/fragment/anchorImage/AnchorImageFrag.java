package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.EditImgAct;
import com.pro.maluli.module.myself.myAccount.appeal.presenter.IAppealContraction;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页信息
 *
 * @author 23203
 */
public class AnchorImageFrag extends BaseMvpFragment<IAnchorImageContraction.View, AnchorImagePresenter> implements IAnchorImageContraction.View {
    AnchorImgAdapter anchorImgAdapter;
    @BindView(R.id.anchorImgRv)
    RecyclerView anchorImgRv;
    @BindView(R.id.editImg)
    TextView editImg;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.nodataView)
    View nodataView;
    List<AnchorInfoEntity.PictureBean> pictureBeans = new ArrayList<>();

    @Override
    public AnchorImagePresenter initPresenter() {
        return new AnchorImagePresenter(getActivity());
    }

    public static Fragment newInstance(String anchorID) {
        AnchorImageFrag treasureGameFrag = new AnchorImageFrag();
        Bundle bundle = new Bundle();
        bundle.putString("ISANCHOR", anchorID);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    @Override
    public void onWakeBusiness() {
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
        presenter.anchorId = getArguments().getString("ISANCHOR");
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_anchor_img;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        anchorImgRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        nodataTipsTv.setText("暂无数据");

    }

    @OnClick({R.id.editImg})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.editImg:
                gotoActivity(EditImgAct.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getAnchorInfo();
    }

    @Override
    public void doBusiness() {

    }

    List<AnchorInfoEntity.PictureBean> pictureBean=new ArrayList<>();

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        pictureBean.clear();
        pictureBean .addAll( data.getPicture());
        if (data.getIs_edit() == 1) {
            editImg.setVisibility(View.VISIBLE);
        } else {
            editImg.setVisibility(View.GONE);
        }
        if (pictureBean.size() > 0) {
            anchorImgRv.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
            anchorImgAdapter = new AnchorImgAdapter(pictureBean, getActivity());
            anchorImgRv.setAdapter(anchorImgAdapter);
            anchorImgAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                    ArrayList<String> urls = new ArrayList<>();
                    for (int i = 0; i < pictureBean.size(); i++) {
                        urls.add(pictureBean.get(i).getUrl());
                    }
                    CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                    bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                    bigPictureDialog.setArguments(bundle);
                    bigPictureDialog.show(getChildFragmentManager(), "CheckBigPictureDialog");
                }
            });
        } else {
            anchorImgRv.setVisibility(View.GONE);
            nodataView.setVisibility(View.VISIBLE);
        }
    }
}