package com.pro.maluli.common.view.dialogview.gift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

import java.util.List;

/**
 * Created by ZXM on 2016/9/12.
 */
public class GridViewAdapter extends BaseAdapter {
    private List<GiftEntity.ListBean> mDatas;
    private LayoutInflater inflater;
    private Context mContext;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 8;

    public GridViewAdapter(Context context, List<GiftEntity.ListBean> mDatas, int curIndex) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.mContext = context;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public GiftEntity.ListBean getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_main_message, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.moneyTv = (TextView) convertView.findViewById(R.id.moneyTv);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.item_layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize，
         */
        GiftEntity.ListBean model = getItem(position);
        viewHolder.tv.setText(model.getTitle());
        viewHolder.moneyTv.setText(model.getMoney()+"");
        GlideUtils.loadImage(mContext, model.getLogo(), viewHolder.iv);
        return convertView;
    }


    class ViewHolder {
        public RelativeLayout item_layout;
        public TextView tv, moneyTv;
        public ImageView iv;
    }

}