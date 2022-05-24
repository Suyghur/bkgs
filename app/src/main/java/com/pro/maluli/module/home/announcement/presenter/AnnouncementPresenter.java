package com.pro.maluli.module.home.announcement.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.entity.WatchListEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnnouncementPresenter extends BasePresenter<IAnnouncementContraction.View> implements IAnnouncementContraction.Presenter {
    public int page = 1;

    public AnnouncementPresenter(Context context) {
        super(context);
    }

    @Override
    public void getFansListInfo() {
        add(mService.getNoticeList()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<NoticeEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<NoticeEntity> response) {
                        mView.setNoticeInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
