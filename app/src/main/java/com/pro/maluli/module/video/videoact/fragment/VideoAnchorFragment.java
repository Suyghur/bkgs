package com.pro.maluli.module.video.videoact.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.csz.okhttp.http.DownloadCallback;
import com.csz.okhttp.http.DownloadManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpFragment;
import com.pro.maluli.common.entity.AnchorInfoEntity;
import com.pro.maluli.common.entity.CommentVideoEntity;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.utils.HttpUtil;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.common.view.dialogview.DeleteDialog;
import com.pro.maluli.common.view.dialogview.ShareVideoDialog;
import com.pro.maluli.common.view.dialogview.presenter.InputTextMsgDialog;
import com.pro.maluli.common.view.dialogview.presenter.adapter.CommentListAdapter;
import com.pro.maluli.common.view.myselfView.LikeLayout;
import com.pro.maluli.common.view.myselfView.StarBar;
import com.pro.maluli.ktx.ext.VideoExtKt;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.video.events.CanStartEvent;
import com.pro.maluli.module.video.events.ClearPositionEvent;
import com.pro.maluli.module.video.fragment.recyclerUtils.RecyclerViewUtil;
import com.pro.maluli.module.video.fragment.recyclerUtils.SoftKeyBoardListener;
import com.pro.maluli.module.video.fragment.videoFragment.presenter.IVideoFragmentContraction;
import com.pro.maluli.module.video.fragment.videoFragment.presenter.VideoFragmentPresenter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoAnchorFragment extends BaseMvpFragment<IVideoFragmentContraction.View, VideoFragmentPresenter> implements IVideoFragmentContraction.View, View.OnClickListener {
    StandardGSYVideoPlayer videoPlayer;
    File videoFile;
    ProgressDialog pd; // 进度条对话框
    //要用Handler回到主线程操作UI，否则会报错
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //QQ登陆
                case 0:
                    ToastUtils.showShort("分享失败");
                    break;
                //微信登录
                case 1:
                    presenter.shareVideo();
                    ToastUtils.showShort("分享成功");
                    break;
            }
        }
    };
    SmartRefreshLayout commentSfl;
    VideoEntity videoBean;
    private int mCurrentPosition;
    private LinearLayout commentLL;
    private LikeLayout likeLayout;
    private TextView commentNumberTv, likeNumberTv, shareNumberTv, anchorNameTv, liveIdTv, attentionTv, serviceTv, statusTv;
    private LinearLayout LiveingLL;
    private CircleImageView anchorAvaterCiv;
    private ImageView liveingIv, leveImg, LoveIv, shareVideoIv;
    private StarBar abilityStar, serviceQualitySb;
    private boolean isSubAnchor;
    private int likeNumber;
    private String videoUrl, cover, desc;
    private RelativeLayout avatarRl;
    private TextView videoContentTv;
    private int shareNum;
    private List<CommentVideoEntity.ListBean> commentlist = new ArrayList<>();
    private AnchorInfoEntity.VideoBean videoBeans;
    private int deletePosition;
    private String anchorID;
    private BottomSheetDialog bottomSheetDialog;
    private CommentListAdapter bottomSheetAdapter;
    private RecyclerView rv_dialog_lists;
    private View nodataView;
    private long totalCount = 30;//总条数不得超过它
    private int offsetY;
    private float slideOffset = 0;
    private InputTextMsgDialog inputTextMsgDialog;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;
    private CircleImageView avaterIv;
    private TextView nameTv, anchorNumberTv;
    private boolean isCanStart;

    public static VideoAnchorFragment getNewInstance(AnchorInfoEntity.VideoBean videoBean, String anchorID) {
        VideoAnchorFragment fragment = new VideoAnchorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("url", videoBean);
        bundle.putSerializable("anchorID", anchorID);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 视频存在本地
     */
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/3gp");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    @Override
    public VideoFragmentPresenter initPresenter() {
        return new VideoFragmentPresenter(getActivity());
    }

    @Override
    public void onWakeBusiness() {

    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            String sda = "";
        }
    }

    @Override
    public void baseInitialization() {
        EventBus.getDefault().register(this);
        videoBeans = (AnchorInfoEntity.VideoBean) getArguments().getSerializable("url");
        anchorID = getArguments().getString("anchorID");
        presenter.anchor_id = anchorID;
        presenter.videoId = String.valueOf(videoBeans.getId());

        videoUrl = videoBeans.getUrl();
        cover = videoBeans.getCover();
//        MobSDK.submitPolicyGrantResult(true, null);
    }

    @Override
    public int setR_Layout() {
        mCurrentPosition = 0;
        return R.layout.frag_video_video;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attentionTv:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }
                presenter.anchorSub();
                break;
            case R.id.avatarRl://跳转到主播信息页
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("AnchorID", String.valueOf(anchorID));
//                gotoActivity(AnchorInformationAct.class, false, bundle1);
                break;
            case R.id.LoveIv:
                if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                    return;
                }

                presenter.loveVideo();
                break;
            case R.id.shareVideoIv:
                //分享
                ShareVideoDialog dialogFragment = new ShareVideoDialog();
                Bundle bundle = new Bundle();
                dialogFragment.setArguments(bundle);
                dialogFragment.setCancelable(true);
                dialogFragment.show(getChildFragmentManager(), "ForgetPwdDialog");
                dialogFragment.setOnShareAppListener(new ShareVideoDialog.OnShareAppListener() {
                    @Override
                    public void gotoShare(int type) {
                        // 1 QQ 2 微信 3朋友圈，4 QQ空间
                        switch (type) {
                            case 1:
                                ToolUtils.shareVideo(handler, QQ.NAME, videoUrl, desc, cover);
                                break;
                            case 2:
                                ToolUtils.shareVideo(handler, Wechat.NAME, videoUrl, desc, cover);
                                break;
                            case 3:
                                ToolUtils.shareVideo(handler, WechatMoments.NAME, videoUrl, desc, cover);
                                break;
                            case 4:
                                ToolUtils.shareVideo(handler, QZone.NAME, videoUrl, desc, cover);
                                break;
                        }

                    }

                    @Override
                    public void downloadVideo() {
                        if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                            return;
                        }
                        if (XXPermissions.isGranted(getActivity(), Permission.Group.STORAGE)) {
                            presenter.downloadVideo(videoBean.getVideo().getVideo_id());
                        } else {
                            XXPermissions.with(getActivity()).permission(Permission.Group.STORAGE).request(new OnPermissionCallback() {
                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    presenter.downloadVideo(videoBean.getVideo().getVideo_id());
                                }

                                @Override
                                public void onDenied(List<String> permissions, boolean never) {
                                    OnPermissionCallback.super.onDenied(permissions, never);
                                    ToastUtils.showShort("请开启权限，才能下载");
                                }
                            });
                        }
                    }
                });
                break;
        }

    }

    /***
     * 下载MP4
     */
    private void downMp4(File videoUrl1) {
        new Thread() {
            @Override
            public void run() {
                try {
                    getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + videoUrl1)));
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(String.valueOf(file))));
//                    //获取ContentResolve对象，来操作插入视频
                    ContentResolver localContentResolver = getActivity().getContentResolver();
//                    //ContentValues：用于储存一些基本类型的键值对
                    ContentValues localContentValues = getVideoContentValues(getActivity(), videoUrl1, System.currentTimeMillis());
//                    //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
                    Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort("下载成功！");
                            pd.dismiss(); // 结束掉进度条对话框
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getFileFromNew(String path) {
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //正在下载更新
        pd.setMessage("下载中...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        try {
            OkHttpClient client = HttpUtil.getHttpClient();
            Request request = new Request.Builder().url(path).build();
            Response response = null;
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                //失败的回调进行补充逻辑
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ToastUtils.showShort("下载失败");
                    pd.dismiss();
                    if (call.isCanceled()) {
                        System.out.println("远程调用取消");
                    }
                    if (call.isExecuted()) {
                        System.out.println("远程调用执行");
                    }

                }

                //成功的回调
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (call.isCanceled()) {
                        System.err.println("远程调用取消");
                    }
                    if (call.isExecuted()) {
                        System.err.println("远程调用执行成功");
                    }
                    //请求成功返回的流
                    InputStream inputStream = response.body().source().inputStream();
                    File sd1 = Environment.getExternalStorageDirectory();
                    String path1 = sd1.getPath() + "/" + String.valueOf((int) System.currentTimeMillis() / 10000);
                    File myfile1 = new File(path1);
                    if (!myfile1.exists()) {
                        myfile1.mkdir();
                    }
                    videoFile = new File(myfile1, "bkgs.mp4");
                    //下载文件保存位置
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(videoFile));
                    byte[] data = new byte[1024];
                    int len;
                    int available = inputStream.available();
                    while ((len = inputStream.read(data)) != -1) {
                        bufferedOutputStream.write(data, 0, len);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    inputStream.close();
                    downMp4(videoFile);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewInitialization() {
        videoPlayer = mainView.findViewById(R.id.videoPlayer);
        likeLayout = mainView.findViewById(R.id.likeLayout);
        commentLL = mainView.findViewById(R.id.commentLL);
        avatarRl = (RelativeLayout) mainView.findViewById(R.id.avatarRl);
        videoContentTv = (TextView) mainView.findViewById(R.id.videoContentTv);


        commentNumberTv = (TextView) mainView.findViewById(R.id.commentNumberTv);
        likeNumberTv = (TextView) mainView.findViewById(R.id.likeNumberTv);
        shareNumberTv = (TextView) mainView.findViewById(R.id.shareNumberTv);
        LiveingLL = mainView.findViewById(R.id.LiveingLL);
        anchorAvaterCiv = mainView.findViewById(R.id.anchorAvaterCiv);
        liveingIv = mainView.findViewById(R.id.liveingIv);
        anchorNameTv = mainView.findViewById(R.id.anchorNameTv);
        liveIdTv = mainView.findViewById(R.id.liveIdTv);
        leveImg = mainView.findViewById(R.id.leveImg);
        attentionTv = mainView.findViewById(R.id.attentionTv);
        LoveIv = mainView.findViewById(R.id.LoveIv);
        abilityStar = mainView.findViewById(R.id.abilityStar);
        serviceQualitySb = mainView.findViewById(R.id.serviceQualitySb);
        serviceTv = mainView.findViewById(R.id.serviceTv);
        statusTv = mainView.findViewById(R.id.statusTv);
        shareVideoIv = mainView.findViewById(R.id.shareVideoIv);

        attentionTv.setOnClickListener(this::onClick);
        LoveIv.setOnClickListener(this::onClick);
        shareVideoIv.setOnClickListener(this::onClick);
        avatarRl.setOnClickListener(this::onClick);

        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideUtils.loadImage(getActivity(), videoBeans.getCover(), imageView);

        videoContentTv.setText(videoBeans.getDesc());

        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        videoPlayer.setNeedShowWifiTip(false);
        videoPlayer.setLooping(true);
        videoPlayer.setDismissControlTime(3000);
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setThumbPlay(true);
        videoPlayer.setThumbImageView(imageView);
        videoPlayer.setUpLazy(videoBeans.getUrl(), false, null, null, "");
//        likeLayout.setLikeLayoutListener(new LikeLayout.LikeLayoutListener() {
//            @Override
//            public void setOnlike() {
//
//            }
//
//            @Override
//            public void setOnPause() {
//                if (videoPlayer.getGSYVideoManager().isPlaying()) {
//                    videoPlayer.onVideoPause();
//                } else {
//                    if (videoPlayer.getCurrentPositionWhenPlaying() == 0) {
//                        videoPlayer.startPlayLogic();
//                    } else {
//                        videoPlayer.onVideoResume(false);
//
//                    }
//                }
//                int currentPositionWhenPlaying = videoPlayer.getCurrentPositionWhenPlaying();
//            }
//        });


        commentLL.setOnClickListener(v -> {

            slideOffset = 0;
            bottomSheetDialog.show();


//            CommentDF commentDF=new CommentDF();
//            Bundle bundle=new Bundle();
//            bundle.putString("VideoId", String.valueOf(videoBean.getVideo_id()));
//            commentDF.setArguments(bundle);
//            commentDF.show(getActivity().getSupportFragmentManager(), "");
        });
        mRecyclerViewUtil = new RecyclerViewUtil();
        showSheetDialog();
    }

    private void initDataForView() {
        commentNumberTv.setText(videoBean.getVideo().getComment_num() + "");
        likeNumberTv.setText(videoBean.getVideo().getLike_num() + "");
        shareNumberTv.setText(videoBean.getVideo().getShare_num() + "");
        GlideUtils.loadImage(getActivity(), videoBean.getAnchor().getAvatar(), anchorAvaterCiv);
        if (videoBean.getAnchor().getIs_live() == 1) {//直播中
            LiveingLL.setVisibility(View.VISIBLE);
            GlideUtils.loadGif(getActivity(), R.mipmap.ic_liveing, liveingIv);

        } else {
            LiveingLL.setVisibility(View.GONE);

        }
        anchorNameTv.setText(videoBean.getAnchor().getNickname());
        GlideUtils.loadImage(getActivity(), videoBean.getAnchor().getLevel(), leveImg);
        liveIdTv.setText("ID:" + videoBean.getAnchor().getAnchor_no());

        if (videoBean.getAnchor().getIs_self() == 1) {//判断是否是主播
            attentionTv.setVisibility(View.GONE);
        } else {
            attentionTv.setVisibility(View.VISIBLE);
            if (videoBean.getAnchor().getIs_sub() == 1) {
                attentionTv.setText("取消关注");
            } else {
                attentionTv.setText("+关注");

            }
        }
        serviceTv.setText("已服务人数：" + videoBean.getAnchor().getService_num());

        abilityStar.setStarCount(videoBean.getAnchor().getService() == 0 ? 1 : videoBean.getAnchor().getService());
//        serviceQualitySb.setStarMark(data.getProfessional()==0?1:data.getProfessional());
        serviceQualitySb.setStarCount(videoBean.getAnchor().getProfessional() == 0 ? 1 : videoBean.getAnchor().getProfessional());
//        type 直播间类型 (1:一对一, 2:一对多)
        if (videoBean.getAnchor().getLive().getType() == 0) {
            statusTv.setVisibility(View.GONE);
        } else if (videoBean.getAnchor().getLive().getType() == 1) {
            statusTv.setVisibility(View.VISIBLE);
            statusTv.setText("一对一直播中");
        } else if (videoBean.getAnchor().getLive().getType() == 2) {
            statusTv.setVisibility(View.VISIBLE);
            statusTv.setText("对众直播中");
        }
        if (videoBean.getVideo().getIs_like() == 1) {
            LoveIv.setSelected(true);
        } else {
            LoveIv.setSelected(false);

        }


    }

    private void showSheetDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.show();
            return;
        }
        View view = View.inflate(getActivity(), R.layout.dialog_bottomsheet, null);


        ImageView iv_dialog_close = (ImageView) view.findViewById(R.id.dialog_bottomsheet_iv_close);
        rv_dialog_lists = (RecyclerView) view.findViewById(R.id.dialog_bottomsheet_rv_lists);
        nodataView = (View) view.findViewById(R.id.nodataView);
        RelativeLayout rl_comment = view.findViewById(R.id.rl_comment);
        avaterIv = view.findViewById(R.id.avaterIv);
        nameTv = view.findViewById(R.id.nameTv);
        anchorNumberTv = view.findViewById(R.id.anchorNumberTv);
        commentSfl = view.findViewById(R.id.commentSfl);
        commentSfl.setRefreshHeader(new ClassicsHeader(getActivity()));
        commentSfl.setRefreshFooter(new ClassicsFooter(getActivity()));
        commentSfl.setEnableNestedScroll(false);

        iv_dialog_close.setOnClickListener(v -> bottomSheetDialog.dismiss());

        rl_comment.setOnClickListener(v -> {
            if (!ToolUtils.isLoginTips(getActivity(), getChildFragmentManager())) {
                return;
            }
            initInputTextMsgDialog(null, false, null, -1);
        });

        bottomSheetAdapter = new CommentListAdapter(commentlist, getActivity());
        rv_dialog_lists.setHasFixedSize(true);
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_dialog_lists.setItemAnimator(new DefaultItemAnimator());

        rv_dialog_lists.setAdapter(bottomSheetAdapter);
        bottomSheetAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                deletePosition = position;
                int commentId = commentlist.get(position).getId();
                if (commentlist.get(position).getIs_self() == 1) {
                    DeleteDialog deleteDialog = new DeleteDialog();
                    deleteDialog.show(getChildFragmentManager(), "DeleteDialog");
                    deleteDialog.setOnConfirmListener(new DeleteDialog.OnBaseTipsListener() {
                        @Override
                        public void comfirm() {
                            presenter.deleteComment(commentId);
                        }
                    });
                }
            }
        });

        initListener();

        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.dialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        int flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        bottomSheetDialog.getWindow().getDecorView().setSystemUiVisibility(flag);
        //设置背景为透明
        bottomSheetDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        int dialogHeight = ToolUtils.getContextRect(getActivity());
        //设置弹窗大小为会屏
        bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        final BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset1) {
                slideOffset = slideOffset1;

            }
        });
    }

    private void initListener() {
        //滚动事件
        if (mRecyclerViewUtil != null) mRecyclerViewUtil.initScrollListener(rv_dialog_lists);
        mKeyBoardListener = new SoftKeyBoardListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
//        // 打开或关闭加载更多功能（默认为true）
//        bottomSheetAdapter.getLoadMoreModule().setEnableLoadMore(true);
//// 设置加载更多监听事件
//        bottomSheetAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                presenter.getCommentVideo(String.valueOf(videoBean.getVideo().getVideo_id()));
//            }
//        });
//        bottomSheetAdapter.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
//            @Override
//            public void onUpFetch() {
//                presenter.page = 1;
//            }
//        });

        commentSfl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                commentSfl.finishLoadMore(1000);
                presenter.getCommentVideo(String.valueOf(videoBean.getVideo().getVideo_id()));
            }
        });
        commentSfl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.page = 1;
                presenter.getCommentVideo(String.valueOf(videoBean.getVideo().getVideo_id()));
                commentSfl.finishRefresh(1000);
            }
        });
    }

    private int getWindowHeight() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    private void initInputTextMsgDialog(View view, final boolean isReply, final String headImg, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(getActivity(), R.style.dialog);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    if (TextUtils.isEmpty(msg)) {
                        ToastUtils.showShort("请输入你要发表的评论");
                        return;
                    }
                    presenter.subComment(presenter.videoId, msg);

                }

                @Override
                public void dismiss() {
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }

    // item滑动到原位
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComment(boolean isReply, String headImg, final int position, String msg) {
    }

    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }

    @Override
    public void doBusiness() {
        presenter.getCommentVideo(presenter.videoId);
        presenter.getVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCanStart) {
            return;
        }
        videoPlayer.getCurrentPlayer().onVideoResume(false);

//        GSYVideoManager.onResume();
        GSYVideoManager.instance().setNeedMute(false);
        if (mCurrentPosition > 0) {
            videoPlayer.onVideoResume(false);
//            EventBus.getDefault().post(new CanScrollEvent(true));
        } else {
            videoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoPlayer.startPlayLogic();
                }
            }, 200);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (getUserVisibleHint()) {
            String sda = "";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClearPosition(ClearPositionEvent clearPositionEvent) {
        if (clearPositionEvent.isClear()) {
            mCurrentPosition = 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClearPosition(CanStartEvent canStartEvent) {
        isCanStart = canStartEvent.isCanStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        likeLayout.onPause();
        videoPlayer.getCurrentPlayer().onVideoPause();
        mCurrentPosition = (int) videoPlayer.getGSYVideoManager().getCurrentPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayer.getCurrentPlayer().release();
        GSYVideoManager.releaseAllVideos();
        if (mRecyclerViewUtil != null) {
            mRecyclerViewUtil.destroy();
            mRecyclerViewUtil = null;
        }
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        bottomSheetAdapter = null;

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setVideoInfo(VideoEntity data) {

        videoBean = data;
        desc = videoBean.getVideo().getDesc();
        commentNumberTv.setText(videoBean.getVideo().getComment_num() + "");
        likeNumberTv.setText(data.getVideo().getLike_num() + "");
        shareNum = videoBean.getVideo().getShare_num();
        shareNumberTv.setText(shareNum + "");
        if (videoBean.getVideo().getIs_like() == 1) {
            LoveIv.setSelected(true);
        } else {
            LoveIv.setSelected(false);

        }
        initDataForView();
    }

    @Override
    public void setCommontInfo(CommentVideoEntity data) {
        commentSfl.finishRefresh();
        commentSfl.finishLoadMore();
        anchorNumberTv.setText("ID:" + data.getAnchor().getAnchor_no());
        nameTv.setText(data.getAnchor().getNickname());
        GlideUtils.loadImage(getActivity(), data.getAnchor().getAvatar(), avaterIv);
        commentNumberTv.setText(data.getTotal() + "");
        if (presenter.page == 1) {
            if (data.getList().size() == 0) {
                nodataView.setVisibility(View.VISIBLE);
                rv_dialog_lists.setVisibility(View.GONE);
            } else {
                presenter.page++;
                nodataView.setVisibility(View.GONE);
                rv_dialog_lists.setVisibility(View.VISIBLE);
                commentlist.clear();
                commentlist.addAll(data.getList());
                bottomSheetAdapter.notifyDataSetChanged();
            }

        } else {

            if (data.getList().size() == 0) {
            } else {
                presenter.page++;
                commentlist.addAll(data.getList());
                bottomSheetAdapter.notifyDataSetChanged();
            }
            if (data.getList().size() == 0) {
                commentSfl.finishLoadMoreWithNoMoreData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commentSfl.setNoMoreData(false);
                    }
                }, 1000);
            } else {
                presenter.page++;
                commentlist.addAll(data.getList());
                bottomSheetAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void setLikeAnNoLike(String msg) {
        if (isSubAnchor) {
            attentionTv.setSelected(true);
            attentionTv.setText("+关注");
        } else {
            attentionTv.setSelected(false);
            attentionTv.setText("取消关注");
        }
        isSubAnchor = !isSubAnchor;
    }

    @Override
    public void setLoveVideo(String status) {
//        1 点赞成功 0 取消点赞成功
        if ("1".equals(status)) {
            likeNumber++;
            LoveIv.setSelected(true);
        } else {
            likeNumber--;
            LoveIv.setSelected(false);
        }
        likeNumberTv.setText(likeNumber + "");
    }

    /**
     * 删除评论成功
     */
    @Override
    public void deleteSuccess() {
        commentlist.remove(deletePosition);
        //删除动画
        bottomSheetAdapter.notifyItemRemoved(deletePosition);
        bottomSheetAdapter.notifyDataSetChanged();
    }

    @Override
    public void setShareVideoSuccess() {
        shareNum++;
        shareNumberTv.setText(shareNum + "");
    }

    @Override
    public void downVideoSuccess(String download_url) {
        if (TextUtils.isEmpty(download_url)) {
            ToastUtils.showShort("下载视频失败");
            return;
        }
        pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //正在下载更新
        pd.setMessage("下载中...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        DownloadManager.getInstance().download(download_url, new DownloadCallback() {
            @Override
            public void onSuccess(File file) {
                Logger.d("download onSuccess file: " + file.getAbsolutePath());
                VideoExtKt.copyVideo(file.getAbsolutePath(), requireActivity());
                pd.dismiss();
                ToastUtils.showShort("下载视频成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                Logger.e("download onFailure, code: " + code + ", msg: " + msg);
                pd.dismiss();
                ToastUtils.showShort("下载视频失败");
            }

            @Override
            public void onProgress(int progress) {
                pd.setProgress(progress);
            }
        });
    }
}
