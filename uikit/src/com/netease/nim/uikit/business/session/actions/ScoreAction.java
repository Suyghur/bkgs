package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.model.location.LocationProvider;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ScoreAction extends BaseAction {
    private final static String TAG = "LocationAction";

    public ScoreAction() {
        super(R.drawable.ic_action_sorce, R.string.input_panel_score);
    }

    @Override
    public void onClick() {
        GoSettingEvent goSettingEvent = new GoSettingEvent(getAccount());
        goSettingEvent.setScore(true);
        goSettingEvent.setSeeNotic(false);
        EventBus.getDefault().post(goSettingEvent);
    }
}
