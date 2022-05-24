package com.pro.maluli.common.view.dialogview.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.VideoEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;
import com.pro.maluli.module.video.fragment.videoFragment.presenter.IVideoFragmentContraction;

import io.reactivex.functions.Consumer;

public class CommentFragmentPresenter extends BasePresenter<ICommentFragmentContraction.View> implements ICommentFragmentContraction.Presenter {
    public CommentFragmentPresenter(Context context) {
        super(context);
    }

    public int page;

    @Override
    public void getCommentVideo(String videoId) {
        add(mService.getCommentVideo(videoId, String.valueOf(page))
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
    public void subComment(String videoId, String s) {
        add(mService.subCommentVideo(videoId, s)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<VideoEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<VideoEntity> response) {
                        String sdad = response.toString();
//                        mView.userInfo();
                        mView.commentSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }
}
