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
import com.pro.maluli.common.view.dialogview.dialogAdapter.OnlineMemberAdapter;
import com.pro.maluli.common.view.myselfView.MaxHeightRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by  kingsley on 2019/10/17 0017.
 * 确认付款
 */

public class OnlineMemberDialog extends DialogFragment implements View.OnClickListener {
    OnlineMemberAdapter adapter;
    List<ChatRoomMember> chatRoomMemberList = new ArrayList<>();
    List<ChatRoomMember> memberAll = new ArrayList<>();
    List<ChatRoomMember> memberSearch = new ArrayList<>();
    List<ChatRoomMember> memberAllnoChange = new ArrayList<>();
    private Dialog mDetailDialog;
    private LinearLayout dismissLL;
    private TextView onlineNumberTv, nodataTv;
    private EditText inputOnlineEt;
    private MaxHeightRecyclerView onlineMemberRl;
    private boolean idSeeCancel;//是否显示取消按钮
    private String roomId, anchorAccid;
    private OnBaseTipsListener onFreezeTipsListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
        mDetailDialog.setContentView(R.layout.dialog_online_main_member);
        mDetailDialog.setCancelable(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        onlineNumberTv = mDetailDialog.findViewById(R.id.onlineNumberTv);
        inputOnlineEt = mDetailDialog.findViewById(R.id.inputOnlineEt);
        onlineMemberRl = mDetailDialog.findViewById(R.id.onlineMemberRl);
        nodataTv = mDetailDialog.findViewById(R.id.nodataTv);
        dismissLL = mDetailDialog.findViewById(R.id.dismissLL);
        roomId = getArguments().getString("roomId");
        anchorAccid = getArguments().getString("ANCHORID");
        onlineMemberRl.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OnlineMemberAdapter(memberAll, anchorAccid, getActivity());
        dismissLL.setOnClickListener(this::onClick);
        onlineMemberRl.setAdapter(adapter);

        adapter.addChildClickViewIds(R.id.statusTv);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                jinyan(memberAll.get(position).getAccount(), !memberAll.get(position).isMuted());
            }
        });
        getMembers();

        inputOnlineEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    //此处做逻辑处理
                    if (TextUtils.isEmpty(inputOnlineEt.getText().toString().trim())) {
                        ToastUtils.showShort("请输入需要搜索的内容");
                    } else {
                        memberSearch.clear();
                        String inputString = inputOnlineEt.getText().toString().trim();
                        for (int j = 0; j < memberAllnoChange.size(); j++) {
                            String allString = memberAllnoChange.get(j).getNick();
                            boolean b = allString.contains(inputString);
                            if (b) {
                                memberSearch.add(memberAllnoChange.get(j));

                            }
                        }
                        hasDataSeeView(memberSearch);
                        adapter.setList(memberSearch);
                        List<ChatRoomMember> qwqw = memberAll;
//                        adapter.setDiffNewData(memberSearch);
//                        getMembers();
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
                    getMembers();
                }

            }
        });

        return mDetailDialog;
    }

    private void hasDataSeeView(List<ChatRoomMember> list) {
        if (list.size() == 0) {
            nodataTv.setVisibility(View.VISIBLE);
            onlineMemberRl.setVisibility(View.GONE);
        } else {
            nodataTv.setVisibility(View.GONE);
            onlineMemberRl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 禁言
     *
     * @param account 用户id
     */
    private void jinyan(String account, boolean isAdd) {
        // 以添加到禁言名单为例
        MemberOption option = new MemberOption(roomId, account);
        NIMClient.getService(ChatRoomService.class).markChatRoomMutedList(isAdd, option)
                .setCallback(new RequestCallback<ChatRoomMember>() {
                    @Override
                    public void onSuccess(ChatRoomMember param) {
                        // 成功
                        getMembers();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                    }
                });
    }

    public void setOnConfirmListener(OnBaseTipsListener onFreezeTipsListener) {
        this.onFreezeTipsListener = onFreezeTipsListener;
    }

    public void setData(List<ChatRoomMember> chatRoomMembers) {
        this.chatRoomMemberList.clear();
        this.chatRoomMemberList.addAll(chatRoomMembers);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void getMembers() {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.ONLINE_NORMAL, 0, 1000).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                chatRoomMemberList.clear();
                memberAll.clear();
                memberAllnoChange.clear();
                chatRoomMemberList.addAll(chatRoomMembers);
                getMembersGuest();
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * 非固定成员 (又称临时成员,只有在线时才能在列表中看到,数量无上限)
     */

    public void getMembersGuest() {
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.GUEST_DESC, 0, 1000).setCallback(new RequestCallback<List<ChatRoomMember>>() {
            @Override
            public void onSuccess(List<ChatRoomMember> chatRoomMembers) {
                List<ChatRoomMember> mutedList = new ArrayList<>();
                List<ChatRoomMember> normalList = new ArrayList<>();
                for (ChatRoomMember member : chatRoomMembers) {
                    if (member.isMuted()) {
                        mutedList.add(member);
                    } else {
                        normalList.add(member);
                    }
                }
                chatRoomMemberList.addAll(mutedList);
                chatRoomMemberList.addAll(normalList);

                Set set = new HashSet();
                set.addAll(chatRoomMemberList);
                memberAll.addAll(set);
                memberAllnoChange.addAll(set);
                hasDataSeeView(memberAll);
                adapter.notifyDataSetChanged();
                if (onlineNumberTv != null) {
                    onlineNumberTv.setText(memberAll.size() + "");
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comfirmTv:
                if (onFreezeTipsListener != null) {
                    onFreezeTipsListener.comfirm();
                }
            case R.id.dismissLL:
            case R.id.cancelTv:
                if (mDetailDialog != null) {
                    mDetailDialog.dismiss();
                }
                break;
        }
    }

    public interface OnBaseTipsListener {
        void comfirm();//0去申述，2去绑定
    }
}
