package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;

public class ImgMarkPresenter extends BasePresenter<IImgMarkContraction.View> implements IImgMarkContraction.Presenter {
    public int page = 1;

    public ImgMarkPresenter(Context context) {
        super(context);
    }

}
