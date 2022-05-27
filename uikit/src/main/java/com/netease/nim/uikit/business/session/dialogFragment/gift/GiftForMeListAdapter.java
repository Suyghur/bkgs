package com.netease.nim.uikit.business.session.dialogFragment.gift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.my.GiftForMeEntity;
import com.netease.nim.uikit.common.ui.imageview.CircleImageView;


public class GiftForMeListAdapter extends RecyclerView.Adapter<GiftForMeListAdapter.BaseViewHolder> {
    private Context context;
    private GiftForMeEntity giftForMeEntity;

    public GiftForMeListAdapter(Context context, GiftForMeEntity entity) {
        this.context = context;
        this.giftForMeEntity = entity;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GiftForMeListAdapter.BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gift_for_me, null));
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        GiftForMeEntity.ListBean listBean = giftForMeEntity.getList().get(position);
        Glide.with(context).load(listBean.getMember().getAvatar()).into(holder.avaterCiv);
        holder.userNameTv.setText(listBean.getMember().getNickname());
        holder.tiemTv.setText(listBean.getCreated_at());
        holder.giftType.setText(listBean.getGift().getTitle());
        holder.moneyTv.setText(listBean.getGift().getMoney() + "金额");

    }

    @Override
    public int getItemCount() {
        return giftForMeEntity == null ? 0 : giftForMeEntity.getList().size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avaterCiv;
        private TextView userNameTv, tiemTv, giftType, moneyTv;

        public BaseViewHolder(View itemView) {
            super(itemView);
            avaterCiv = (CircleImageView) itemView.findViewById(R.id.avaterCiv);
            userNameTv = (TextView) itemView.findViewById(R.id.userNameTv);
            tiemTv = (TextView) itemView.findViewById(R.id.tiemTv);
            giftType = (TextView) itemView.findViewById(R.id.giftType);
            moneyTv = (TextView) itemView.findViewById(R.id.moneyTv);
        }
    }
}
