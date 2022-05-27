package com.pro.maluli.module.message.searchMessasge.adapter;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.pro.maluli.module.message.searchMessasge.SearchContentEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        if (list.get(i).getTypee() == 1) {
            return 1;
        }
        return 2;
    }
}
