package com.pro.maluli.module.message.base;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.MessageListEntity;
import com.pro.maluli.common.eventBus.SettingSeeView;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.reminder.ReminderManager;
//import com.pro.maluli.common.wyy.view.SessionHelper;
import com.pro.maluli.module.message.base.presenter.IMessageContraction;
import com.pro.maluli.module.message.base.presenter.MessagePresenter;
import com.pro.maluli.module.message.lineUp.LineUpAct;
import com.pro.maluli.module.message.reserveMessage.ReserveMessageAct;
import com.pro.maluli.module.message.searchMessasge.SearchMsgAct;
import com.pro.maluli.module.message.systemNotification.SystemNotificationAct;
import com.pro.maluli.module.myself.anchorInformation.base.AnchorInformationAct;
import com.pro.maluli.module.socketService.event.OTOEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
public class MessageFrag extends BaseMvpFragment<IMessageContraction.View, MessagePresenter> implements IMessageContraction.View {
    private LinearLayout mine_mian_ll;
    private RecentContactsFragment fragment;
    @BindView(R.id.systemLL)
    LinearLayout systemLL;
    @BindView(R.id.yuyueLL)
    LinearLayout yuyueLL;
    @BindView(R.id.hasMessageImg)
    TextView hasMessageImg;
    @BindView(R.id.yuyueMessageTv)
    TextView yuyueMessageTv;
    @BindView(R.id.queenMessageTv)
    TextView queenMessageTv;
    @BindView(R.id.emptyBg)
    TextView emptyBg;
    @BindView(R.id.recentLL)
    LinearLayout recentLL;

    @Override
    public MessagePresenter initPresenter() {
        return new MessagePresenter(getActivity());
    }

    @Override
    public void onWakeBussiness() {
//        presenter.getUserInfo();
        presenter.getMessageList();
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(getActivity(), Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(getActivity(), true);

    }


    @Override
    public int setR_Layout() {
        return R.layout.frag_message;
    }

    @Override
    public void viewInitialization() {
        ButterKnife.bind(this, mainView);
        mine_mian_ll = (LinearLayout) mainView.findViewById(R.id.mine_mian_ll);
        BarUtils.addMarginTopEqualStatusBarHeight(mine_mian_ll);
        isHidden = false;
    }


    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<>(fragments.size());

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

    @OnClick({R.id.systemLL, R.id.yuyueLL, R.id.lineUpLL, R.id.searchMsgLL})
    public void onViewClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.systemLL:
                gotoActivity(SystemNotificationAct.class);
                break;
            case R.id.yuyueLL:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                gotoActivity(ReserveMessageAct.class);
                break;
            case R.id.lineUpLL:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                gotoActivity(LineUpAct.class);
                break;
            case R.id.searchMsgLL:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                gotoActivity(SearchMsgAct.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        presenter.getMessageList();
        if (!ToolUtils.isLogin(getActivity(), false)) {
            hasMessageImg.setVisibility(View.GONE);
            queenMessageTv.setVisibility(View.GONE);
            yuyueMessageTv.setVisibility(View.GONE);
            recentLL.setVisibility(View.GONE);
            emptyBg.setVisibility(View.VISIBLE);
        } else {
            emptyBg.setVisibility(View.GONE);
            recentLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * "is_anchor": 1,是否主播 <number>
     * "system_notice_count": 2,系统消息数量 <number>
     * "anchor_appoint_count": 1,主播预约数量 <number>
     * "appoint_count": 0我的预约数量 <number>
     *
     * @param data
     */
    @Override
    public void setMessageSuccess(MessageListEntity data) {
        if (data.getIs_anchor() == 1) {
            yuyueLL.setVisibility(View.VISIBLE);
            if (data.getAnchor_appoint_count() > 0) {
                yuyueMessageTv.setVisibility(View.VISIBLE);
                yuyueMessageTv.setText(data.getAnchor_appoint_count() + "");
            } else {
                yuyueMessageTv.setVisibility(View.GONE);
            }
        } else {
            yuyueLL.setVisibility(View.GONE);
        }

        if (data.getSystem_notice_count() > 0) {
            hasMessageImg.setText(data.getSystem_notice_count() + "");
            hasMessageImg.setVisibility(View.VISIBLE);
        } else {
            hasMessageImg.setVisibility(View.GONE);

        }
        if (data.getAppoint_count() > 0) {
            queenMessageTv.setVisibility(View.VISIBLE);
            queenMessageTv.setText(data.getAppoint_count() + "");
        } else {
            queenMessageTv.setVisibility(View.GONE);
        }
        int hasNewsNumber = data.getAnchor_appoint_count() + data.getAppoint_count() + data.getSystem_notice_count();
        SettingSeeView settingSeeView = new SettingSeeView(hasNewsNumber);
        EventBus.getDefault().post(settingSeeView);
    }
}