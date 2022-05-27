package com.netease.nim.uikit.business.session.myCustom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.VidwHolder> {
    public onItemOnClickListener onItemOnClickListener;
    private Context context;
    private List<String> imgs;
    private LayoutInflater inflater;

    public ReservationAdapter(Context context, List<String> imgs) {
        this.imgs = imgs;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public ReservationAdapter.onItemOnClickListener getOnItemOnClickListener() {
        return onItemOnClickListener;
    }

    public void setOnItemOnClickListener(ReservationAdapter.onItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }

    @NonNull
    @Override
    public VidwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VidwHolder(inflater.inflate(R.layout.item_reservation_img, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.VidwHolder holder, int position) {
        Glide.with(context).load(imgs.get(position)).into(holder.selectImgRiv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemOnClickListener != null) {
                    onItemOnClickListener.onItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgs == null ? 0 : imgs.size();
    }

    public interface onItemOnClickListener {
        void onItem(int position);
    }

    public class VidwHolder extends RecyclerView.ViewHolder {
        private ImageView selectImgRiv;

        public VidwHolder(View itemView) {
            super(itemView);
            selectImgRiv = (ImageView) itemView.findViewById(R.id.selectImgRiv);

        }
    }
}