package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.MemberOption;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.dialogAdapter.GuestAdapter;
import com.pro.maluli.common.view.dialogview.dialogAdapter.OnlineMemberAdapter;
import com.pro.maluli.common.view.myselfView.MaxHeightRecyclerView;
import com.pro.maluli.module.socketService.event.OnTwoOneStartEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class GuestListDialog extends DialogFragment implements View.OnClickListener {
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView nodataTv;
    private EditText inputOnlineEt;
    private RelativeLayout dismissTopRl;
    private MaxHeightRecyclerView onlineMemberRl;
    private boolean idSeeCancel;//是否显示取消按钮
    GuestAdapter adapter;
    List<OnTwoOneStartEntity.SpecialListBean> specialListBeans = new ArrayList<>();
    List<OnTwoOneStartEntity.SpecialListBean> memberSearch = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_online_member);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        inputOnlineEt = mDetailDialog.findViewById(R.id.inputOnlineEt);
        onlineMemberRl = mDetailDialog.findViewById(R.id.onlineMemberRl);
        nodataTv = mDetailDialog.findViewById(R.id.nodataTv);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        dismissTopRl = mDetailDialog.findViewById(R.id.dismissTopRl);
        specialListBeans = (List<OnTwoOneStartEntity.SpecialListBean>) getArguments().getSerializable("Guest_list");
        onlineMemberRl.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GuestAdapter(specialListBeans, getActivity());
        dismissLL.setOnClickListener(this::onClick);
        dismissTopRl.setOnClickListener(this::onClick);
        onlineMemberRl.setAdapter(adapter);

        adapter.addChildClickViewIds(R.id.statusTv);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                if (onFreezeTipsListener != null) {
                    if (memberSearch.size() > 0) {
                        onFreezeTipsListener.comfirm(String.valueOf(memberSearch.get(position).getId()), memberSearch.get(position).getNickname());
                    } else {
                        onFreezeTipsListener.comfirm(String.valueOf(specialListBeans.get(position).getId()), specialListBeans.get(position).getNickname());
                    }
                }
                mDetailDialog.dismiss();
            }
        });

        inputOnlineEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    //此处做逻辑处理
                    if (TextUtils.isEmpty(inputOnlineEt.getText().toString().trim())) {
                        ToastUtils.showShort("请输入需要搜索的内容");
                    } else {
                        memberSearch.clear();
                        for (int j = 0; j < specialListBeans.size(); j++) {
                            if (inputOnlineEt.getText().toString().trim().contains(specialListBeans.get(j).getNickname())) {
                                memberSearch.add(specialListBeans.get(j));

                            }
                        }
                        hasDataSeeView(memberSearch);
                        adapter.setList(memberSearch);
                    }


                    return true;
                }
                return false;
            }
        });
        inputOnlineEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    memberSearch.clear();
                    adapter.setList(specialListBeans);
                }

            }
        });

        return mDetailDialog;
    }

    private void hasDataSeeView(List<OnTwoOneStartEntity.SpecialListBean> list) {
        if (list.size() == 0) {
            nodataTv.setVisibility(View.VISIBLE);
            onlineMemberRl.setVisibility(View.GONE);
        } else {
            nodataTv.setVisibility(View.GONE);
            onlineMemberRl.setVisibility(View.VISIBLE);
        }
    }

    private OnBaseTipsListener onFreezeTipsListener;

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public void setData(List<OnTwoOneStartEntity.SpecialListBean> chatRoomMembers) {
        this.specialListBeans.clear();
        this.specialListBeans.addAll(chatRoomMembers);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnBaseTipsListener {
        void comfirm(String accid, String nickName);//0去申述，2去绑定
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissLL:
            case R.id.dismissTopRl:
            case R.id.cancelTv:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }
}
