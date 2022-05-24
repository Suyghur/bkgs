package com.pro.maluli.module.myself.base;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.constant.ACEConstant;
import com.pro.maluli.common.entity.SeeLiveUserEntity;
import com.pro.maluli.common.entity.UserInfoEntity;
import com.pro.maluli.common.eventBus.SettingSeeView;
import com.pro.maluli.common.utils.ACache;
import com.pro.maluli.common.utils.AcacheUtil;
import com.pro.maluli.common.utils.PackageUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.home.oneToMore.StartOneToMoreLive.StartOneToMoreLiveAct;
import com.pro.maluli.module.myself.anchorInformation.base.AnchorInformationAct;
import com.pro.maluli.module.myself.base.presenter.IMyselfContraction;
import com.pro.maluli.module.myself.base.presenter.MyselfPresenter;
import com.pro.maluli.module.myself.fans.MyFansAct;
import com.pro.maluli.module.myself.myAccount.base.MyAccountAct;
import com.pro.maluli.module.myself.personalCenter.PersonalCenterAct;
import com.pro.maluli.module.myself.setting.base.SettingAct;
import com.pro.maluli.module.myself.userAgreement.base.UserAgreementAct;
import com.pro.maluli.module.myself.watchlist.WatchListAct;
import com.pro.maluli.module.other.login.LoginAct;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 首页信息
 *
 * @author 林
 */
public class MyselfFrag extends BaseMvpFragment<IMyselfContraction.View, MyselfPresenter> implements IMyselfContraction.View {
    @BindView(R.id.settingLL)
    LinearLayout settingLL;
    @BindView(R.id.mine_mian_ll)
    LinearLayout mine_mian_ll;
    @BindView(R.id.avatarCiv)
    CircleImageView avatarCiv;
    @BindView(R.id.userNameTv)
    TextView userNameTv;
    @BindView(R.id.genderImg)
    ImageView genderImg;
    @BindView(R.id.userIdTv)
    TextView userIdTv;
    @BindView(R.id.levelImg)
    ImageView levelImg;
    @BindView(R.id.fansTv)
    TextView fansTv;
    @BindView(R.id.subscriptionTv)
    TextView subscriptionTv;
    @BindView(R.id.anchorLL)
    LinearLayout anchorLL;
    @BindView(R.id.fansListLL)
    LinearLayout fansListLL;
    @BindView(R.id.myNemberLL)
    LinearLayout myNemberLL;
    @BindView(R.id.hasMessageImg)
    ImageView hasMessageImg;
    @BindView(R.id.userXyLL)
    LinearLayout userXyLL;
    @BindView(R.id.topLinear1)
    LinearLayout topLinear1;
    @BindView(R.id.topLinear2)
    LinearLayout topLinear2;
    @BindView(R.id.topLinear3)
    LinearLayout topLinear3;
    @BindView(R.id.gotoLoginTv)
    TextView gotoLoginTv;
    @BindView(R.id.userIdTips)
    TextView userIdTips;
    @BindView(R.id.myselfSrl)
    SmartRefreshLayout myselfSrl;
    private String anchorId;

    @Override
    public MyselfPresenter initPresenter() {
        return new MyselfPresenter(getActivity());
    }

    @Override
    public void onWakeBussiness() {
        if (!ToolUtils.isLogin(getActivity(), false)) {
            topLinear1.setVisibility(View.GONE);
            topLinear2.setVisibility(View.GONE);
            topLinear3.setVisibility(View.GONE);
            anchorLL.setVisibility(View.GONE);
            gotoLoginTv.setVisibility(View.VISIBLE);
            GlideUtils.loadHeardImg(getActivity(), "null", avatarCiv);
            return;
        }
        presenter.getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ToolUtils.isLogin(getActivity(), false)) {
            topLinear1.setVisibility(View.GONE);
            topLinear2.setVisibility(View.GONE);
            topLinear3.setVisibility(View.GONE);
            anchorLL.setVisibility(View.GONE);
            gotoLoginTv.setVisibility(View.VISIBLE);
            GlideUtils.loadHeardImg(getActivity(), "null", avatarCiv);
            return;
        }
        presenter.getUserInfo();
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);


    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_myself;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        BarUtils.addMarginTopEqualStatusBarHeight(mine_mian_ll);
        myselfSrl.setEnableLoadMore(false);
        myselfSrl.setRefreshHeader(new ClassicsHeader(getActivity()));
        /**
         * 下拉刷新
         */
        myselfSrl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.getUserInfo();
                myselfSrl.finishRefresh(1000);
            }
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public void doBusiness() {
    }

    @OnClick({R.id.anchorLL, R.id.myNemberLL, R.id.settingLL,
            R.id.userXyLL, R.id.gotoLoginTv, R.id.avatarCiv, R.id.watchListLL, R.id.fansListLL})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.anchorLL:
                if (ToolUtils.isLogin(getActivity(), true)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("AnchorID", anchorId);
                    gotoActivity(AnchorInformationAct.class, false, bundle);
                }

                break;
            case R.id.myNemberLL:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                gotoActivity(MyAccountAct.class);
                break;
            case R.id.settingLL:

                gotoActivity(SettingAct.class);
                break;
            case R.id.userXyLL:
                gotoActivity(UserAgreementAct.class);
                break;
            case R.id.gotoLoginTv:
                //去登录
                gotoActivity(LoginAct.class);
                break;
            case R.id.avatarCiv:

                if (ToolUtils.isLogin(getActivity(), true)) {
                    if (userInfoEntity != null && userInfoEntity.getIs_teenager() == 1) {
                        ToastUtils.showShort("已开启青少年模式不能修改");
                        return;
                    }
                    gotoActivity(PersonalCenterAct.class);
                }
                break;
            case R.id.watchListLL:
                if (ToolUtils.isLogin(getActivity(), true)) {
                    gotoActivity(WatchListAct.class);
                }
                break;
            case R.id.fansListLL:
                if (ToolUtils.isLogin(getActivity(), true)) {
                    gotoActivity(MyFansAct.class);
                }
                break;
        }
    }

    UserInfoEntity userInfoEntity;

    /**
     * 获取首页数据成功
     *
     * @param response
     */
    @Override
    public void setUserInfoSuccess(UserInfoEntity response) {
        userInfoEntity = response;
        anchorId = String.valueOf(response.getAnchor_id());
        ACache.get(mContext).put(ACEConstant.USERINFO, response);
        GlideUtils.loadHeardImg(getActivity(), response.getAvatar(), avatarCiv);
        GlideUtils.loadImageNoImage(getActivity(), response.getAnchor_level(), levelImg);
        userNameTv.setText(response.getNickname());
        userIdTv.setText(response.getAnchor_no() + "");

        topLinear1.setVisibility(View.VISIBLE);
        topLinear2.setVisibility(View.VISIBLE);
        topLinear3.setVisibility(View.VISIBLE);
        anchorLL.setVisibility(View.GONE);
        gotoLoginTv.setVisibility(View.GONE);

        if ("男".equalsIgnoreCase(response.getSex())) {
            genderImg.setVisibility(View.VISIBLE);
            genderImg.setSelected(false);
        } else if ("女".equalsIgnoreCase(response.getSex())) {
            genderImg.setVisibility(View.VISIBLE);
            genderImg.setSelected(true);
        } else {
            genderImg.setVisibility(View.GONE);
        }
        if (response.getIs_anchor() == 1) {
            topLinear2.setVisibility(View.VISIBLE);
            fansListLL.setVisibility(View.VISIBLE);
            anchorLL.setVisibility(View.VISIBLE);
            userIdTips.setVisibility(View.VISIBLE);
            userIdTv.setVisibility(View.VISIBLE);
        } else {
            topLinear2.setVisibility(View.GONE);
            userIdTips.setVisibility(View.GONE);
            fansListLL.setVisibility(View.GONE);
            anchorLL.setVisibility(View.GONE);
            userIdTv.setVisibility(View.GONE);
        }
        fansTv.setText(response.getFans() + "");
        subscriptionTv.setText(response.getSubs() + "");

        if (!PackageUtils.getVersionName(getActivity()).equalsIgnoreCase(response.getAndroid_version())||response.getNew_report()==1) {
            hasMessageImg.setVisibility(View.VISIBLE);
        } else {
            hasMessageImg.setVisibility(View.GONE);
        }

    }
}