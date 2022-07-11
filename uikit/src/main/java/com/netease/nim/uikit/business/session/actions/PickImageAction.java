package com.netease.nim.uikit.business.session.actions;

import android.content.Intent;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public abstract class PickImageAction extends BaseAction {

    public static final String MIME_JPEG = "image/jpeg";
    private static final int PICK_IMAGE_COUNT = 9;
    private boolean multiSelect;

    protected PickImageAction(int iconResId, int titleId, boolean multiSelect) {
        super(iconResId, titleId);
        this.multiSelect = multiSelect;
    }

    protected abstract void onPicked(File file);

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
        showSelector(getTitleId(), requestCode, multiSelect);
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode, final boolean multiSelect) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(PICK_IMAGE_COUNT);
        ImagePickerLauncher.selectImage(getActivity(), requestCode, option, titleId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.PICK_IMAGE) {
            if (data == null) {
                ToastHelper.showToastLong(getActivity(), R.string.picker_image_error);
                return;
            }
            sendImageAfterSelfImagePicker(data);
        }
    }


    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(getActivity(), data, new SendImageHelper.Callback() {
            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);
            }
        });
    }

}
