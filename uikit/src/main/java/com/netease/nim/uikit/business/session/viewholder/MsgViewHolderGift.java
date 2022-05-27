package com.netease.nim.uikit.business.session.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.myCustom.extension.RedPacketAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderGift extends MsgViewHolderBase {

    private TextView contentGiftTv;    // 红包名称
    private ImageView giftImg;

    public MsgViewHolderGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.item_gift;
    }

    @Override
    public void inflateContentView() {
        giftImg = view.findViewById(R.id.giftImg);
        contentGiftTv = view.findViewById(R.id.contentGiftTv);
    }

    @Override
    public void bindContentView() {
        try {
            RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
            Glide.with(context).load(attachment.getGift_logo()).into(giftImg);
            contentGiftTv.setText("赠送" + attachment.getGift_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
