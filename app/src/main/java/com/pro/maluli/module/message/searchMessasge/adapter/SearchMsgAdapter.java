package com.pro.maluli.module.message.searchMessasge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.myselfView.StarBar;
import com.pro.maluli.module.home.startLive.adapter.provide.AudioProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.GiftProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.NotificationProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.SystemProvide;
import com.pro.maluli.module.home.startLive.adapter.provide.TextProvide;
import com.pro.maluli.module.message.searchMessasge.SearchContentEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchMsgAdapter extends BaseProviderMultiAdapter<SearchContentEntity> {
    public SearchMsgAdapter() {
        super();
        // 注册 Provider
        addItemProvider(new MsgProvide());
        addItemProvider(new UserProvide());
    }
//    public SearchMsgAdapter(List<IMMessage> data, Context context1) {
//        super(R.layout.item_msg_search, data);
//        datas = data;
//        context = context1;
//    }

//    @Override
//    protected void convert(@NotNull SearchMsgAdapter.ViewHolder baseViewHolder, IMMessage bean) {
//
//        HeadImageView circleImageView = baseViewHolder.getView(R.id.img_head);
//        circleImageView.loadBuddyAvatar(bean);
//        baseViewHolder.tv_nickname.setText(bean.getFromNick());
//        baseViewHolder.tv_message.setText(bean.getContent());
//        baseViewHolder.tv_date_time.setText(TimeUtils.millis2String(bean.getTime()));
//
//
//    }

    @Override
    protected int getItemType(@NotNull List<? extends SearchContentEntity> list, int i) {
        if (list.get(i).getTypee()==1){
            return 1;
        }
        return 2;
    }
}
