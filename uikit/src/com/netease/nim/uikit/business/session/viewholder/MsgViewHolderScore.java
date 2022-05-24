package com.netease.nim.uikit.business.session.viewholder;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.GoSettingEvent;
import com.netease.nim.uikit.business.session.myCustom.extension.PhoneAttachment;
import com.netease.nim.uikit.business.session.myCustom.extension.ScoreAttachment;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;

import org.greenrobot.eventbus.EventBus;

public class MsgViewHolderScore extends MsgViewHolderBase {

    private TextView contentGiftTv;    // 红包名称
    private ImageView giftImg;

    public MsgViewHolderScore(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.item_score;
    }

    @Override
    public void inflateContentView() {
        giftImg = findViewById(R.id.giftImg);
        contentGiftTv = findViewById(R.id.contentGiftTv);
    }

    @Override
    public void bindContentView() {
        ScoreAttachment attachment = (ScoreAttachment) message.getAttachment();

        Glide.with(context).load(attachment.getIcon()).into(giftImg);
        contentGiftTv.setText("请评价一下我的服务吧");
        contentGiftTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        contentGiftTv.getPaint().setAntiAlias(true);//抗锯齿
        contentGiftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgDirectionEnum direct = message.getDirect();
                if (direct == MsgDirectionEnum.Out) {
                    return;
                }
                GoSettingEvent goSettingEvent = new GoSettingEvent(message.getSessionId());
                goSettingEvent.setCanScore(true);
                EventBus.getDefault().post(goSettingEvent);
            }
        });
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    @Override
    public void onItemClick() {
    }
}
