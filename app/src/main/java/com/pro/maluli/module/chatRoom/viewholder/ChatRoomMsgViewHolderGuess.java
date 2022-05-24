package com.pro.maluli.module.chatRoom.viewholder;

import android.widget.ImageView;

import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomMsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.pro.maluli.R;
import com.netease.nim.uikit.business.session.myCustom.extension.GuessAttachment;

/**
 * Created by hzliuxuanlin on 17/9/15.
 */
public class ChatRoomMsgViewHolderGuess extends ChatRoomMsgViewHolderBase {

    private GuessAttachment guessAttachment;
    private ImageView imageView;

    public ChatRoomMsgViewHolderGuess(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.chatroom_rock_paper_scissors;
    }

    @Override
    protected void inflateContentView() {
        imageView = (ImageView) view.findViewById(R.id.message_rock_paper_scissors_body);
    }

    @Override
    protected boolean isShowBubble() {
        return true;
    }

    @Override
    protected boolean isShowHeadImage() {
        return true;
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        guessAttachment = (GuessAttachment) message.getAttachment();
        switch (guessAttachment.getValue().getDesc()) {
            case "石头":
                imageView.setImageResource(R.drawable.ic_add_box_black_24dp);
                break;
            case "剪刀":
                imageView.setImageResource(R.drawable.ic_add_box_black_24dp);
                break;
            case "布":
                imageView.setImageResource(R.drawable.ic_add_box_black_24dp);
                break;
            default:
                break;
        }
        imageView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);

    }
}
