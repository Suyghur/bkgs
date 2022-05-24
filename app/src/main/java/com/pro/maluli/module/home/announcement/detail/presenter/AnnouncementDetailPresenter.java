package com.pro.maluli.module.home.announcement.detail.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.NoticeEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class AnnouncementDetailPresenter extends BasePresenter<IAnnouncementDetailContraction.View> implements IAnnouncementDetailContraction.Presenter {
    public String id;

    public AnnouncementDetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void getNoticDetailInfo() {
        add(mService.getNoticeList(id)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<NoticeEntity.ListBean>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<NoticeEntity.ListBean> response) {
                        mView.setDetailInfo(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
