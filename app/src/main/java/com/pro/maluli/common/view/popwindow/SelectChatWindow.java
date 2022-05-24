package com.pro.maluli.common.view.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.view.popwindow.Solve7PopupWindow;
import com.pro.maluli.common.view.popwindow.adapter.PayTypeAllAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectChatWindow {
    private View inView;
    private Context context;
    private PopupWindow mPopWindow;

    public interface PayTypeAllListener {
        void onSelectItem(int position);
    }

    public PayTypeAllListener payTypeAllListener;

    public PayTypeAllListener getPayTypeAllListener() {
        return payTypeAllListener;
    }

    public void setPayTypeAllListener(PayTypeAllListener payTypeAllListener) {
        this.payTypeAllListener = payTypeAllListener;
    }

    public SelectChatWindow(Context context, View view, PayTypeAllListener listener) {
        this.inView = view;
        this.context = context;
        this.payTypeAllListener = listener;
        initPopup();
    }


    //PopupWindow菜单详细内容显示
    //区域
    private void initPopup() {
        //设置contentView
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_pop_select_chat, null);
        //适配7.0版本
        mPopWindow = new PopupWindow(contentView, inView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


//        mPopWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_ececec_6_line));
        mPopWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.c_transparent));
        mPopWindow.setAnimationStyle(R.style.popmenu_animation); //动画
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true); //点击pop外消失
        mPopWindow.showAsDropDown(inView, 0, 0);
        TextView mobileTv = contentView.findViewById(R.id.mobileTv);
        TextView emailTv = contentView.findViewById(R.id.emailTv);
        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payTypeAllListener!=null){
                    payTypeAllListener.onSelectItem(1);
                }
                mPopWindow.dismiss();
            }
        });
        mobileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payTypeAllListener!=null){
                    payTypeAllListener.onSelectItem(0);
                }
                mPopWindow.dismiss();
            }
        });


    }

}