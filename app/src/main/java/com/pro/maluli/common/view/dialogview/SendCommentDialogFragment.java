package com.pro.maluli.common.view.dialogview;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.ToolUtils;

import org.jetbrains.annotations.NotNull;


/**
 * 上传图片，选择上传方式
 */
public class SendCommentDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Dialog mDetailDialog;
    private int mLastDiff = 0;//键盘弹出过程中最后的高度值

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = super.onCreateDialog(savedInstanceState);
//        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
//        mDetailDialog.setContentView(R.layout.dialog_comment);
        mDetailDialog.setCanceledOnTouchOutside(true);
        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);

        Window win = mDetailDialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.SOFT_INPUT_STATE_VISIBLE | params.SOFT_INPUT_ADJUST_RESIZE);//配置输入法，避免弹出遮挡布局
        win.setWindowAnimations(R.style.dialog_bottom);//这里设置dialog的进出动画
        return mDetailDialog;
    }

    public OnSendMsgListener onSelectGoldenListener;

    public void setOnConfirmListener(OnSendMsgListener onSelectGoldenListener) {
        this.onSelectGoldenListener = onSelectGoldenListener;
    }

    public interface OnSendMsgListener {
        void sendMsg(String s);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // 在这里将view的高度设置为精确高度，即可屏蔽向上滑动不占全屏的手势。如果不设置高度的话 会默认向上滑动时dialog覆盖全屏
        View view = inflater.inflate(R.layout.dialog_comment_send, container, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return view;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogTheme);//给dialog设置主题为透明背景 不然会有默认的白色背景
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LinearLayout fl_container = view.findViewById(R.id.fl_container);
        fl_container.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                mDetailDialog.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);//获取当前界面可视部分
                int screenHeight = mDetailDialog.getWindow().getDecorView().getRootView().getHeight();//获取屏幕的高度
                int heightDifference = screenHeight - r.bottom;//此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                if (heightDifference <= 0 && mLastDiff > 0) {
                    onDismiss(mDetailDialog);//手动关闭输入法时，对话框也跟着关闭
                }
                mLastDiff = heightDifference;
            }
        });
        EditText inputMsgEt = view.findViewById(R.id.inputMsgEt);
        view.findViewById(R.id.sendTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputMsgEt.getText().toString().trim())) {
                    ToastUtils.showShort("请输入你要发表的评论");
                    return;
                }

                if (onSelectGoldenListener != null) {
                    onSelectGoldenListener.sendMsg(inputMsgEt.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final View view = getView();
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
                CoordinatorLayout.Behavior behavior = params.getBehavior();
                BottomSheetBehavior mBottomSheetBehavior = (BottomSheetBehavior) behavior;
                mBottomSheetBehavior.setHideable(false);//禁止下拉取消弹框
                mBottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());//让内容显示完整
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:

                break;
        }
    }
}

