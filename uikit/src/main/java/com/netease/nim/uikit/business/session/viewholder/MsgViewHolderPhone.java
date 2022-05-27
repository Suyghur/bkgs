package com.netease.nim.uikit.business.session.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.myCustom.extension.PhoneAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderPhone extends MsgViewHolderBase {

    private TextView contentGiftTv;    // 红包名称
    private ImageView giftImg;

    public MsgViewHolderPhone(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.item_phone;
    }

    @Override
    public void inflateContentView() {
        giftImg = findViewById(R.id.giftImg);
        contentGiftTv = findViewById(R.id.contentGiftTv);
    }

    @Override
    public void bindContentView() {
        PhoneAttachment attachment = (PhoneAttachment) message.getAttachment();
        Glide.with(context).load(attachment.getIcon()).into(giftImg);
        contentGiftTv.setText(attachment.getMsg());
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_green_bg;
    }

    @Override
    public void onItemClick() {
    }
}
