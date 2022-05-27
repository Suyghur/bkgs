package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class GiftFromMeAction extends BaseAction {
    private final static String TAG = "LocationAction";

    public GiftFromMeAction() {
        super(R.drawable.ic_action_gift, R.string.input_panel_gift_from_me);
    }

    @Override
    public void onClick() {
        GoSettingEvent goSettingEvent = new GoSettingEvent(getAccount());
        goSettingEvent.setGetGiftForMe(true);
        EventBus.getDefault().post(goSettingEvent);
    }
}
