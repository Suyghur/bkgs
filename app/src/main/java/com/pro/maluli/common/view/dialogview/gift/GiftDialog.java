package com.pro.maluli.common.view.dialogview.gift;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.netease.nim.uikit.business.session.activity.my.GiftEntity;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.myself.myAccount.recharge.RechargeAct;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class GiftDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private ViewPager viewPager;
    private LinearLayout idotLayout;//知识圆点
    private List<View> mPagerList;//页面集合
    private LayoutInflater mInflater;
    private RelativeLayout topClickRl, contentRl;
    private TextView gotRechargeTv, myBlanceTv;
    private List<GiftEntity.ListBean> mDataList = new ArrayList<>();

    private int currPage;

    /*总的页数*/
    private int pageCount;
    /*每一页显示的个数*/
    private int pageSize = 8;
    /*当前显示的是第几页*/
    private int curIndex = 0;
    private GridViewAdapter[] arr = new GridViewAdapter[3];
    private OnSelectGiftListener onSelectGiftListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_gift_main_message);
        mDetailDialog.setCancelable(true);
        mDetailDialog.setCanceledOnTouchOutside(true);
//        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        mInflater = LayoutInflater.from(getActivity());
        viewPager = mDetailDialog.findViewById(R.id.viewpager);
        gotRechargeTv = mDetailDialog.findViewById(R.id.gotRechargeTv);
        topClickRl = mDetailDialog.findViewById(R.id.topClickRl);
        idotLayout = mDetailDialog.findViewById(R.id.ll_dot);
        myBlanceTv = mDetailDialog.findViewById(R.id.myBlanceTv);
        contentRl = mDetailDialog.findViewById(R.id.contentRl);
        GiftEntity entity = (GiftEntity) getArguments().getSerializable("GIFT_INFO");
        myBlanceTv.setText(entity.getMoney() + "");
        mDataList.addAll(entity.getList());
        topClickRl.setOnClickListener(this::onClick);
        gotRechargeTv.setOnClickListener(this::onClick);
        contentRl.setOnClickListener(this::onClick);
        initValues();
        //更改标题
        return mDetailDialog;
    }

    private void initValues() {

        //初始化图标资源

        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDataList.size() * 1.0 / pageSize);

        //viewpager
        mPagerList = new ArrayList<View>();
        for (int j = 0; j < pageCount; j++) {
            final GridView gridview = (GridView) mInflater.inflate(R.layout.bottom_vp_gridview, viewPager, false);
            final GridViewAdapter gridAdapter = new GridViewAdapter(getActivity(), mDataList, j);
            gridview.setAdapter(gridAdapter);
            arr[j] = gridAdapter;
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (onSelectGiftListener != null) {
                        onSelectGiftListener.comfirm(mDataList.get(position));
                    }

                }
            });
            mPagerList.add(gridview);
        }

        viewPager.setAdapter(new ViewPagerAdapter(mPagerList, getActivity()));
        setOvalLayout();
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            idotLayout.addView(mInflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        idotLayout.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.shape_ffffff_4);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                arr[0].notifyDataSetChanged();
                arr[1].notifyDataSetChanged();
                arr[2].notifyDataSetChanged();

                // 取消圆点选中
                idotLayout.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.shape_30ffffff_4);
                // 圆点选中
                idotLayout.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.shape_ffffff_4);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void setSelectGiftListener(OnSelectGiftListener onFreezeTipsListener) {
        this.onSelectGiftListener = onFreezeTipsListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topClickRl:
                dismiss();
                break;
            case R.id.gotRechargeTv:
                Intent intent = new Intent(getActivity(), RechargeAct.class);
                intent.putExtra("isJump", true);
                startActivity(intent);
                dismiss();
                break;
        }
    }

    public interface OnSelectGiftListener {
        void comfirm(GiftEntity.ListBean id);//0去申述，2去绑定
    }
}
