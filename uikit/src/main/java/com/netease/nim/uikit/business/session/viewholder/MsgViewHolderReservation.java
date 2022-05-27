package com.netease.nim.uikit.business.session.viewholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.myCustom.adapter.ReservationAdapter;
import com.netease.nim.uikit.business.session.myCustom.extension.ReservationMsgAttachment;
import com.netease.nim.uikit.business.session.viewholder.reservationAct.CheckImgAct;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import java.io.Serializable;

public class MsgViewHolderReservation extends MsgViewHolderBase {

    private TextView contentGiftTv;    // 红包名称
    private RecyclerView selectImgRv;
    private ReservationAdapter adapter;

    public MsgViewHolderReservation(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.item_reservation;
    }

    @Override
    public void inflateContentView() {
        selectImgRv = view.findViewById(R.id.selectImgRv);
        contentGiftTv = view.findViewById(R.id.contentGiftTv);
    }

    @Override
    public void bindContentView() {
        try {
            ReservationMsgAttachment attachment = (ReservationMsgAttachment) message.getAttachment();
            contentGiftTv.setText(attachment.getReservation_message());
            if (attachment.getImgs() != null && attachment.getImgs().size() > 0) {
                selectImgRv.setVisibility(View.VISIBLE);
                selectImgRv.setLayoutManager(new GridLayoutManager(context, 3));
                adapter = new ReservationAdapter(context, attachment.getImgs());
                selectImgRv.setAdapter(adapter);
//                selectImgRv.addItemDecoration(new SpaceItemDecoration(context));
                adapter.setOnItemOnClickListener(new ReservationAdapter.onItemOnClickListener() {
                    @Override
                    public void onItem(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Imgs", (Serializable) attachment.getImgs());
                        bundle.putInt("position", position);
                        Intent intent = new Intent(context, CheckImgAct.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
            } else {
                selectImgRv.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
