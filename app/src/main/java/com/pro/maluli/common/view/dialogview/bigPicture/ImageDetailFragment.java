package com.pro.maluli.common.view.dialogview.bigPicture;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.photoview.PhotoViewAttacher;
import com.makeramen.roundedimageview.RoundedImageView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

//import uk.co.senab.photoview.PhotoView;
//import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
//    private RoundedImageView mImageView;
    private PhotoViewAttacher mAttacher;
    private PhotoView mImageView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url")
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment,
                container, false);
        mImageView = v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

//        mAttacher = new PhotoViewAttacher(mImageView);

//        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//
//            @Override
//            public void onPhotoTap(View arg0, float arg1, float arg2) {
//                ((CheckBigPictureDialog) getParentFragment()).dissmiss();
//            }
//        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBigPictureDialog) getParentFragment()).dissmiss();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GlideUtils.loadRectImage(getActivity(),mImageUrl,mImageView,20);
//        ImageLoader.getInstance().displayImage(mImageUrl, mImageView,
//                new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view,
//                                                FailReason failReason) {
//                        String message = null;
//                        switch (failReason.getType()) {
//                            case IO_ERROR:
//                                message = "下载错误";
//                                break;
//                            case DECODING_ERROR:
//                                message = "图片无法显示";
//                                break;
//                            case NETWORK_DENIED:
//                                message = "网络有问题，无法下载";
//                                break;
//                            case OUT_OF_MEMORY:
//                                message = "图片太大无法显示";
//                                break;
//                            case UNKNOWN:
//                                message = "未知的错误";
//                                break;
//                        }
//                        Toast.makeText(getActivity(), message,
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view,
//                                                  Bitmap loadedImage) {
//                        mAttacher.update();
//                    }
//                });
    }
}