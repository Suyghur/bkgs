package com.pro.maluli.common.view.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.view.popwindow.adapter.PayTypeAllAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PayTypeAllPopupWindow {
    private View inView;
    private Context context;
    private PopupWindow popupWindow;
    private List<String> popData = new ArrayList<>();
    private int checkType = 999;
    private Boolean isReward=false;

    public Boolean getReward() {
        return isReward;
    }

    public void setReward(Boolean reward) {
        isReward = reward;
    }

    public interface PayTypeAllListener {
        void onSelectItem(int position, String type);
    }

    public PayTypeAllListener payTypeAllListener;

    public PayTypeAllListener getPayTypeAllListener() {
        return payTypeAllListener;
    }

    public void setPayTypeAllListener(PayTypeAllListener payTypeAllListener) {
        this.payTypeAllListener = payTypeAllListener;
    }

    public PayTypeAllPopupWindow(Context context, View view, PayTypeAllListener listener) {
        this.inView = view;
        this.context = context;
        this.payTypeAllListener = listener;
        initData();
        initPopup();
    }
    public PayTypeAllPopupWindow(Context context,boolean isReward, View view, PayTypeAllListener listener) {
        this.inView = view;
        this.isReward = isReward;
        this.context = context;
        this.payTypeAllListener = listener;
        initData();
        initPopup();
    }

    private void initData() {
        popData.add("全部");
        if (getReward()){
            popData.add("提现");
            popData.add("收入");
        }else {
            popData.add("充值");
            popData.add("支出");
        }

        popData.add("私信打赏");
        popData.add("对众直播打赏");
        popData.add("一对一直播打赏");
        popData.add("进入直播间费用");
    }

    //PopupWindow菜单详细内容显示
    //区域
    private void initPopup() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_pop_all, null);
//        popupWindow = new PopupWindow(contentView, topSelectLL.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow = new PopupWindow(contentView, inView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        LinearLayout dismissLL = contentView.findViewById(R.id.dismissLL);
        dismissLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        PayTypeAllAdapter allAdapter = new PayTypeAllAdapter(popData);
        RecyclerView allRv = contentView.findViewById(R.id.allRv);
        allRv.setLayoutManager(new LinearLayoutManager(context));
        allRv.setAdapter(allAdapter);
        allAdapter.addChildClickViewIds(R.id.contentTv);
        allAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {

//                        类型 0:百科充值,1:一对一直播打赏,2:私信打赏,3:对众直播打赏,4:进入付费直播间 5-支出 999-全部

                switch (position) {
                    case 0:
                        checkType = 999;
                        break;
                    case 1:
                        checkType = 0;
                        break;
                    case 2:
                        checkType = 5;
                        break;
                    case 3:
                        checkType = 2;
                        break;
                    case 4:
                        checkType = 3;
                        break;
                    case 5:
                        checkType = 1;
                        break;
                    case 6:
                        checkType = 4;
                        break;
                }
                if (payTypeAllListener != null) {
                    payTypeAllListener.onSelectItem(checkType, popData.get(position));
                }
                popupWindow.dismiss();

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.c_30000000));
        popupWindow.setAnimationStyle(R.style.popmenu_animation); //动画
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true); //点击pop外消失
        popupWindow.showAsDropDown(inView, 0, 0);
    }

}