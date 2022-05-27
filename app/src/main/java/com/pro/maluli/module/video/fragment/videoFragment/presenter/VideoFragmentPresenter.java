package com.pro.maluli.module.video.fragment.videoFragment.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.CommentVideoEntity;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class VideoFragmentPresenter extends BasePresenter<IVideoFragmentContraction.View> implements IVideoFragmentContraction.Presenter {
    public String videoId;
    public String anchor_id;
    public int page = 1;

    public VideoFragmentPresenter(Context context) {
        super(context);
    }

    @Override
    public void getVideo() {
        add(mService.getVideo(videoId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                        mView.setVideoInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void anchorSub() {
        add(mService.removeLike(anchor_id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.setLikeAnNoLike(response.getMsg());
                        getVideo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void loveVideo() {
        add(mService.likeVideo(videoId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response.getData());
                        String status = String.valueOf(JSONObject.parseObject(myJson).getInteger("status"));
                        mView.setLoveVideo(status);
                        getVideo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void getCommentVideo(String videoId) {
        add(mService.getCommentVideo(videoId, String.valueOf(page))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<CommentVideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<CommentVideoEntity> response) {

                        String sdad = response.toString();
//                        mView.userInfo();
                        mView.setCommontInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void subComment(String videoId, String s) {
        add(mService.subCommentVideo(videoId, s)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
//                        mView.userInfo();
                        ToastUtils.showShort(response.getMsg());
                        page = 1;
                        getCommentVideo(videoId);
                        getVideo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void deleteComment(int commentId) {
        add(mService.deleteCommentVideo(videoId, String.valueOf(commentId))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
//                        mView.userInfo();
                        ToastUtils.showShort(response.getMsg());
                        mView.deleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void shareVideo() {
        add(mService.subShareVideo(videoId)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
//                        mView.userInfo();
                        mView.setShareVideoSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    /**
     * 下载视频
     *
     * @param video_id
     */
    @Override
    public void dawnLoadVideo(int video_id) {
        add(mService.subDownloadVideo(String.valueOf(video_id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
//                        mView.userInfo();
//                        download_url
                        Gson gson = new Gson();
                        String myJson = gson.toJson(response);
                        JSONObject jsonObject = JSONObject.parseObject(myJson);
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = JSONObject.parseObject(data);
                        String download_url = jsonObject2.getString("download_url");
                        mView.downVideoSuccess(download_url);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
