package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.transition.FadeProvider;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.ex.DayWheelView;
import com.zyyoona7.picker.ex.MonthWheelView;
import com.zyyoona7.picker.ex.YearWheelView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class SelectTimeDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private TextView submitTv, timeStartTv, timeEndTv;
    private LinearLayout dismissLL, selectTimeLl, customizeTimeLl, selectCustomizeLL;
    private int genderType;
    private DatePickerView dpv_custom_2;
    private int timeType;//0 开始时间，1 结束时间
    private String startTime, endTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_time);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        submitTv = mDetailDialog.findViewById(R.id.submitTv);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        selectTimeLl = mDetailDialog.findViewById(R.id.selectTimeLl);
        customizeTimeLl = mDetailDialog.findViewById(R.id.customizeTimeLl);
        selectCustomizeLL = mDetailDialog.findViewById(R.id.selectCustomizeLL);

        timeStartTv = mDetailDialog.findViewById(R.id.timeStartTv);
        timeEndTv = mDetailDialog.findViewById(R.id.timeEndTv);

        dpv_custom_2 = mDetailDialog.findViewById(R.id.dpv_custom_2);
        dpv_custom_2.setTextSize(24, true);
        dpv_custom_2.setShowLabel(false);
        dpv_custom_2.setTextBoundaryMargin(20, true);
        dpv_custom_2.setShowDivider(true);
        dpv_custom_2.setDividerType(WheelView.DIVIDER_TYPE_WRAP);
        dpv_custom_2.setDividerColor(Color.parseColor("#efefef"));
        dpv_custom_2.setNormalItemTextColor(Color.parseColor("#958F94"));
        dpv_custom_2.setSelectedItemTextColorRes(R.color.c_342C33);
        dpv_custom_2.setDividerPaddingForWrap(20, true);
        YearWheelView yearWv2 = dpv_custom_2.getYearWv();
        MonthWheelView monthWv2 = dpv_custom_2.getMonthWv();
        DayWheelView dayWv2 = dpv_custom_2.getDayWv();
        monthWv2.setIntegerNeedFormat("%02d");
        dayWv2.setIntegerNeedFormat("%02d");
        dpv_custom_2.setResetSelectedPosition(true);
        dpv_custom_2.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(BaseDatePickerView datePickerView, int year,
                                       int month, int day, @Nullable Date date) {
                //                Toast.makeText(Main3Activity.this,"选中的日期："+date.toString(),Toast.LENGTH_SHORT).show();
                if (!customizeTimeLl.isSelected()) {
                    return;
                }
                String selectTime = year + "-" + month + "-" + day;
                if (timeType == 0) {
                    timeStartTv.setText(year + "-" + month + "-" + day);
                } else if (timeType == 1) {
                    timeEndTv.setText(year + "-" + month + "-" + day);
                }
            }
        });
        startTime = dpv_custom_2.getSelectedDate();
        dpv_custom_2.hideDayItem();
        selectTimeLl.setSelected(true);
        timeType = 0;
        selectCustomizeLL.setVisibility(View.INVISIBLE);
        customizeTimeLl.setSelected(false);
        timeStartTv.setSelected(true);
        timeEndTv.setSelected(false);

        dismissLL.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        selectTimeLl.setOnClickListener(this);
        customizeTimeLl.setOnClickListener(this);
        timeEndTv.setOnClickListener(this);
        timeStartTv.setOnClickListener(this);

        return mDetailDialog;
    }

    private OnSelectTimeListener onSelectTimeListener;

    public void setOnTimeListener(OnSelectTimeListener onSelectGenderListener) {
        this.onSelectTimeListener = onSelectGenderListener;
    }

    public interface OnSelectTimeListener {
        void confirmSuccess(int timeType, String startTime, String endTiem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
                mDetailDialog.dismiss();
                break;
            case R.id.timeEndTv:
                timeType = 1;
                timeStartTv.setSelected(false);
                timeEndTv.setSelected(true);
                break;
            case R.id.timeStartTv:
                timeType = 0;
                timeStartTv.setSelected(true);
                timeEndTv.setSelected(false);
                break;
            case R.id.customizeTimeLl:
                dpv_custom_2.showDayItem();
                dpv_custom_2.setShowLabel(false);
                selectTimeLl.setSelected(false);
                timeType = 0;
                selectCustomizeLL.setVisibility(View.VISIBLE);
                customizeTimeLl.setSelected(true);
                break;
            case R.id.selectTimeLl:
                //选择时间
                dpv_custom_2.hideDayItem();
                selectTimeLl.setSelected(true);
                timeType = 0;
                selectCustomizeLL.setVisibility(View.INVISIBLE);
                customizeTimeLl.setSelected(false);
                break;
            case R.id.submitTv:
                if (customizeTimeLl.isSelected()) {
                    startTime = timeStartTv.getText().toString().trim();
                    endTime = timeEndTv.getText().toString().trim();
                } else {
                    startTime = dpv_custom_2.getSelectedDate().substring(0,dpv_custom_2.getSelectedDate().lastIndexOf("-"));
                }
                if (onSelectTimeListener != null) {
                    onSelectTimeListener.confirmSuccess(timeType, startTime, endTime);
                }
                mDetailDialog.dismiss();

                break;

            default:

                break;
        }
    }
}
