package com.pro.maluli.module.home.oneToOne.queue.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.ImageEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.app.MyAppliaction;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QueueAdapter extends BaseQuickAdapter<ReserveEntity.InfoBean.AppointListBean, BaseViewHolder> {
    List<ReserveEntity.InfoBean.AppointListBean> datas;
    private Context context;

    public QueueAdapter(List<ReserveEntity.InfoBean.AppointListBean> data, Context context) {
        super(R.layout.item_queue_person, data);
        datas = data;
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ReserveEntity.InfoBean.AppointListBean entity) {
        CircleImageView audienceImgClv = baseViewHolder.getView(R.id.audienceImgClv);
        TextView audienceNameTv = baseViewHolder.getView(R.id.audienceNameTv);
        // type =0 添加图片按钮 type=1 加载本地图片
        GlideUtils.loadImage(context, entity.getAvatar(), audienceImgClv);
        audienceNameTv.setText(entity.getNickname());
    }

}
