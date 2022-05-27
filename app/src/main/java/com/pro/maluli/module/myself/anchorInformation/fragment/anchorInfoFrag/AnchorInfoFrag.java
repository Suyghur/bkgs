package com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.view.myselfView.LabelsView;
import com.pro.maluli.module.myself.anchorInformation.editLabel.EditLabelAct;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorInfoFrag.anchorIntro.AnchorIntroAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页信息
 *
 * @author 23203
 */
public class AnchorInfoFrag extends BaseMvpFragment<IAnchorInfoContraction.View, AnchorInfoPresenter> implements IAnchorInfoContraction.View {
    @BindView(R.id.anchorInfoTv)
    TextView anchorInfoTv;
    @BindView(R.id.editJsTv)
    TextView editJsTv;
    @BindView(R.id.editLabelTv)
    TextView editLabelTv;
    @BindView(R.id.lableTipsTv)
    TextView lableTipsTv;
    @BindView(R.id.labeleLV)
    LabelsView labeleLV;
    AnchorInfoEntity entity;

    public static Fragment newInstance(String anchorId) {
        AnchorInfoFrag treasureGameFrag = new AnchorInfoFrag();
        Bundle bundle = new Bundle();
        bundle.putString("ANCHOR_ID", anchorId);
        treasureGameFrag.setArguments(bundle);
        return treasureGameFrag;
    }

    @Override
    public AnchorInfoPresenter initPresenter() {
        return new AnchorInfoPresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {
        presenter.getAnchorInfo();
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);
        presenter.anchorID = getArguments().getString("ANCHOR_ID");
    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_anchor_info;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);

    }

    @OnClick({R.id.editLabelTv, R.id.editJsTv})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.editLabelTv:
                gotoActivity(EditLabelAct.class);
                break;
            case R.id.editJsTv:
                Bundle bundle = new Bundle();
                bundle.putString("JIAN_JIE", entity.getDesc());
                bundle.putString("max_desc", entity.getMax_desc());
                gotoActivity(AnchorIntroAct.class, false, bundle);
                break;
        }
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getAnchorInfo();
    }

    @Override
    public void setAnchorInfo(AnchorInfoEntity data) {
        entity = data;
        if (data.getIs_edit() == 1) {
            editLabelTv.setVisibility(View.VISIBLE);
            editJsTv.setVisibility(View.VISIBLE);
        } else {
            editLabelTv.setVisibility(View.GONE);
            editJsTv.setVisibility(View.GONE);
        }

        if (data.getTags().size() == 0) {
            labeleLV.setVisibility(View.GONE);
            lableTipsTv.setVisibility(View.VISIBLE);
        } else {
            labeleLV.setVisibility(View.VISIBLE);
            lableTipsTv.setVisibility(View.GONE);
            labeleLV.setSelectType(LabelsView.SelectType.NONE);
            //第一步：设置数据源
            labeleLV.setLabels(data.getTags(), new LabelsView.LabelTextProvider<AnchorInfoEntity.TagsBean>() {
                @Override
                public CharSequence getLabelText(TextView label, int position, AnchorInfoEntity.TagsBean data) {
                    return data.getTag_name();////根据data和position返回label需要显示的数据。
                }
            });
        }
        if (TextUtils.isEmpty(data.getDesc())) {
            anchorInfoTv.setText("暂无数据");
        } else {
            anchorInfoTv.setText(data.getDesc());

        }
    }
}