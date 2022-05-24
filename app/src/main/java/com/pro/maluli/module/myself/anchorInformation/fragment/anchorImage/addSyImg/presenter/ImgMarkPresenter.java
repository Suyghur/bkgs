package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.presenter;

import android.content.Context;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.AnchorLabelEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ImgMarkPresenter extends BasePresenter<IImgMarkContraction.View> implements IImgMarkContraction.Presenter {
    public int page = 1;

    public ImgMarkPresenter(Context context) {
        super(context);
    }

}
