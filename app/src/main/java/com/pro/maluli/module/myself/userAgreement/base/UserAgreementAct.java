package com.pro.maluli.module.myself.userAgreement.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.ProtocolEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.module.myself.userAgreement.base.adapter.UserAgreementAdapter;
import com.pro.maluli.module.myself.userAgreement.base.presenter.IUserAgreementContraction;
import com.pro.maluli.module.myself.userAgreement.base.presenter.UserAgreementPresenter;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class UserAgreementAct extends BaseMvpActivity<IUserAgreementContraction.View,
        UserAgreementPresenter> implements IUserAgreementContraction.View {

    @BindView(R.id.blackListRl)
    RecyclerView blackListRl;
    UserAgreementAdapter userAgreementAdapter;
    private List<ProtocolEntity.ListBean> listBeans = new ArrayList<>();

    @Override
    public UserAgreementPresenter initPresenter() {
        return new UserAgreementPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_user_agreement;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("用户协议");
        setBackPress();
        blackListRl.setLayoutManager(new LinearLayoutManager(this));
        userAgreementAdapter = new UserAgreementAdapter(listBeans);
        blackListRl.setAdapter(userAgreementAdapter);

        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        userAgreementAdapter.addChildClickViewIds(R.id.protocolDetailLL);
        // 设置子控件点击监听
        userAgreementAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("PROTOCOL_ID", String.valueOf(listBeans.get(position).getId()));
                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }
        });

    }


    @Override
    public void doBusiness() {
        presenter.getProtocoList();
    }

    @Override
    public void setProtocolSuccess(ProtocolEntity data) {
        listBeans.clear();
        listBeans.addAll(data.getList());
        userAgreementAdapter.notifyDataSetChanged();
    }
}