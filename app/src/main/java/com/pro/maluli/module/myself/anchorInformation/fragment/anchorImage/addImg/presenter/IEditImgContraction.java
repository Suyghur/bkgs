package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.presenter;

import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.base.IClear;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;

import java.io.File;
import java.util.List;

public interface IEditImgContraction extends BaseView {
    interface View extends BaseView {

        void setUpdateImgSuccess(UpdateImgEntity data);

        void setDeleteSuccess();

        void setImageSuccess(AnchorImgEntity data);
    }

    interface Presenter extends IClear {

        void subImg(List<File> files);

        void getImg();

        void deleteImage(int id);

        void subMitImg(String url, String mark);

        void subSortImg(String sortImg);
    }
}
