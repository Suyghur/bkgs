package com.pro.maluli.common.view.dialogview;

import static com.luck.picture.lib.tools.ScreenUtils.getScreenHeight;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pro.maluli.R;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.view.dialogview.presenter.CommentFragmentPresenter;
import com.pro.maluli.common.view.dialogview.presenter.ICommentFragmentContraction;
import com.pro.maluli.common.view.dialogview.presenter.adapter.CommentListAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * 上传图片，选择上传方式
 */
public class CommentDF extends BottomSheetDialogFragment implements View.OnClickListener {

    public OnSelectGoldenListener onSelectGoldenListener;
    CommentFragmentPresenter presenter;
    private Dialog mDetailDialog;
    private String videoId;
    ICommentFragmentContraction.View view = new ICommentFragmentContraction.View() {
        @Override
        public void setVideoInfo(VideoEntity data) {

        }

        @Override
        public void commentSuccess() {
            presenter.page = 1;
            presenter.getCommentVideo(videoId);
        }

        @Override
        public void onError(int code, String msg) {

        }
    };
    private CommentListAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = super.onCreateDialog(savedInstanceState);
//        mDetailDialog = new Dialog(getActivity(), R.style.dialog_bottom);
//        mDetailDialog.setContentView(R.layout.dialog_comment);
        mDetailDialog.setCanceledOnTouchOutside(true);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        mDetailDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        Window win = mDetailDialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.SOFT_INPUT_ADJUST_NOTHING);//设置使软键盘弹出的时候dialog不会被顶起
        win.setWindowAnimations(R.style.dialog_bottom);//这里设置dialog的进出动画

        //设置背景为透明
        mDetailDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        mDetailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        videoId = getArguments().getString("VideoId");
        presenter = new CommentFragmentPresenter(getActivity());
        presenter.attach(view);
        return mDetailDialog;
    }

    public void setOnConfirmListener(OnSelectGoldenListener onSelectGoldenListener) {
        this.onSelectGoldenListener = onSelectGoldenListener;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // 在这里将view的高度设置为精确高度，即可屏蔽向上滑动不占全屏的手势。如果不设置高度的话 会默认向上滑动时dialog覆盖全屏
        View view = inflater.inflate(R.layout.dialog_comment, container, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getScreenHeight(getActivity()) * 2 / 3));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        TextView sendTv = view.findViewById(R.id.sendTv);
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //写评论的弹出框
                SendCommentDialogFragment dialogFragment = new SendCommentDialogFragment();
                dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getName());
                dialogFragment.setOnConfirmListener(new SendCommentDialogFragment.OnSendMsgListener() {
                    @Override
                    public void sendMsg(String s) {
                        presenter.subComment(videoId, s);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCommentVideo(videoId);
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
                mBottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());//让dialog的内容显示完整
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

    public interface OnSelectGoldenListener {
        void goldenSelect();
    }
}

