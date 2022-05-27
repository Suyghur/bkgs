package com.pro.maluli.module.video.fragment.videoFragment.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.CommentVideoEntity;
import com.pro.maluli.common.entity.VideoEntity;

public interface IVideoFragmentContraction extends BaseView {
    interface View extends BaseView {
        void setVideoInfo(VideoEntity data);

        void setCommontInfo(CommentVideoEntity data);


        void setLikeAnNoLike(String msg);

        void setLoveVideo(String status);

        void deleteSuccess();

        void setShareVideoSuccess();

        void downVideoSuccess(String download_url);
    }

    interface Presenter extends IClear {
        void anchorSub();

        void loveVideo();

        void getCommentVideo(String videoId);

        void getVideo();

        void subComment(String videoId, String s);

        void deleteComment(int commentId);

        void shareVideo();

        void dawnLoadVideo(int video_id);
    }
}
