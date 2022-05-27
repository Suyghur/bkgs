package com.pro.maluli.module.myself.anchorInformation.editLabel.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import io.reactivex.functions.Consumer;

public class EditLabelPresenter extends BasePresenter<IEditLabelContraction.View> implements IEditLabelContraction.Presenter {
    public int page = 1;

    public EditLabelPresenter(Context context) {
        super(context);
    }

    @Override
    public void getLabel() {
        add(mService.getAnchorLabel()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        mView.setLabelSuccess(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

    @Override
    public void deleteLabel(int id) {
        add(mService.getDeleteLabel(String.valueOf(id))
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<AnchorLabelEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AnchorLabelEntity> response) {
                        mView.setDeleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                }));
    }

}
