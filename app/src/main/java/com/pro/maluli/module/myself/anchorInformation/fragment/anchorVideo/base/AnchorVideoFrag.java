package com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.AnchorVideoEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorVideo.updateVideo.AddVideoAct;
import com.pro.maluli.module.video.videoact.VideoAct;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页信息
 *
 * @author 23203
 */
public class AnchorVideoFrag extends BaseMvpFragment<IAnchorVideoContraction.View, AnchorVideoPresenter> implements IAnchorVideoContraction.View {
    AnchorVideoAdapter anchorImgAdapter;
    @BindView(R.id.anchorImgRv)
    RecyclerView anchorImgRv;
    @BindView(R.id.editImg)
    TextView editImg;
    @BindView(R.id.updateVideoTv)
    TextView updateVideoTv;
    @BindView(R.id.deleteVideoAllTv)
    TextView deleteVideoAllTv;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.nodataView)
    View nodataView;
    List<AnchorInfoEntity.VideoBean> pictureBeans = new ArrayList<>();
    private boolean isShowSelect;

    @Override
    public AnchorVideoPresenter initPresenter() {
        return new AnchorVideoPresenter(getActivity());
    }

    public static Fragment newInstance(String anchorId) {
        AnchorVideoFrag treasureGameFrag = new AnchorVideoFrag();
        Bundle bundle = new Bundle();
        bundle.putString("ANCHOR_ID", anchorId);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    @Override
    public void onWakeBussiness() {
        presenter.getAnchorInfo(String.valueOf(presenter.anchorId));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getAnchorInfo(String.valueOf(presenter.anchorId));
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
        presenter.anchorId = getArguments().getString("ANCHOR_ID");
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_anchor_video;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);

        anchorImgRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        anchorImgAdapter = new AnchorVideoAdapter(pictureBeans, getActivity());
        anchorImgAdapter.setShowSelect(false);
        anchorImgRv.setAdapter(anchorImgAdapter);
        anchorImgAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("AnchorID", presenter.anchorId);
                bundle.putInt("Position", position);
                gotoActivity(VideoAct.class,false,bundle);
            }
        });
        nodataTipsTv.setText("暂无数据");
    }

    @OnClick({R.id.editImg, R.id.updateVideoTv, R.id.deleteVideoAllTv})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.editImg:
                if (anchorImgAdapter == null) {
                    return;
                }
                isShowSelect = !isShowSelect;
                ((SimpleItemAnimator) anchorImgRv.getItemAnimator()).setSupportsChangeAnimations(false);
                anchorImgAdapter.setShowSelect(isShowSelect);
                anchorImgAdapter.notifyItemRangeChanged(0, pictureBeans.size());
                if (isShowSelect) {
                    deleteVideoAllTv.setVisibility(View.VISIBLE);
                    editImg.setText("取消");
                } else {
                    editImg.setText("批量删除");
                    deleteVideoAllTv.setVisibility(View.GONE);
                }
                break;
            case R.id.updateVideoTv:
                gotoActivity(AddVideoAct.class);
                break;
            case R.id.deleteVideoAllTv://删除视频
                HashMap<Integer, Boolean> selectedMap = anchorImgAdapter.getSelectedMap();
                //方法一
//                String deletString;
                StringBuffer deletString = new StringBuffer();
                for (Map.Entry<Integer, Boolean> entry : selectedMap.entrySet()) {
                    System.out.println("方法一：key =" + entry.getKey() + "---value=" + entry.getValue());
                    if (entry.getValue()) {
                        deletString.append(entry.getKey() + ",");
                    }
                }
                if (TextUtils.isEmpty(deletString)) {
                    ToastUtils.showShort("请选择需要删除的视频");
                    return;
                }
                String deleteIdString = deletString.substring(0, deletString.length() - 1);
                presenter.deleteVideo(deleteIdString);

                break;
        }
    }

    @Override
    public void doBusiness() {
//        presenter.getAnchorInfo(String.valueOf(presenter.anchorId));
    }
//
//    @Override
//    public void setVideoSuccess(AnchorVideoEntity data) {
////        isShowSelect = false;
////        editImg.setText("批量删除");
////        deleteVideoAllTv.setVisibility(View.GONE);
//        pictureBeans.addAll(data.getVideo());
//        anchorImgAdapter.setShowSelect(false);
//        anchorImgAdapter.notifyDataSetChanged();
////        ((SimpleItemAnimator)anchorImgRv.getItemAnimator()).setSupportsChangeAnimations(false);
////        anchorImgAdapter.notifyItemRangeChanged(0,pictureBeans.size());
//
//    }

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {

        if (data.getVideo().size() == 0) {
            nodataView.setVisibility(View.VISIBLE);
            anchorImgRv.setVisibility(View.GONE);
        } else {
            pictureBeans.clear();
            pictureBeans.addAll(data.getVideo());
            anchorImgAdapter.notifyDataSetChanged();
            nodataView.setVisibility(View.GONE);
            anchorImgRv.setVisibility(View.VISIBLE);
        }
        if (data.getIs_edit() == 1) {
            isShowSelect = false;
            editImg.setText("批量删除");
            editImg.setVisibility(View.VISIBLE);
            updateVideoTv.setVisibility(View.VISIBLE);
            deleteVideoAllTv.setVisibility(View.GONE);
            anchorImgAdapter.setShowSelect(false);

        } else {
            editImg.setVisibility(View.GONE);
            updateVideoTv.setVisibility(View.GONE);
            deleteVideoAllTv.setVisibility(View.GONE);
        }
    }
}