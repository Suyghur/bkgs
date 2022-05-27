package com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.AnchorImgEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.adapter.DefaultItemAnimator;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.adapter.EditImgAdapter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.presenter.EditImgPresenter;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addImg.presenter.IEditImgContraction;
import com.pro.maluli.module.myself.anchorInformation.fragment.anchorImage.addSyImg.ImgMarkAct;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class EditImgAct extends BaseMvpActivity<IEditImgContraction.View, EditImgPresenter> implements IEditImgContraction.View {

    EditImgAdapter blackListAdapter;
    @BindView(R.id.nodataTipsTv)
    TextView nodataTipsTv;
    @BindView(R.id.right_tv)
    TextView right_tv;
    @BindView(R.id.watchListRl)
    RecyclerView watchListRl;
    @BindView(R.id.nodataView)
    View nodataView;
    @BindView(R.id.addImg)
    ImageView addImg;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    String imgMark = "0";//1 添加水印，0不添加
    String imgUrl;
    private boolean isEdit;
    private int deletePosition;
    private boolean isSort;
    private List<AnchorImgEntity.PictureBean> listBeans = new ArrayList<>();
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        //线性布局和网格布局都可以使用
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFrlg = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFrlg, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件  下面注释的代码，滑动后数据和条目错乱，被舍弃
//            Collections.swap(datas,viewHolder.getAdapterPosition(),target.getAdapterPosition());
//            ap.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());

            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(listBeans, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(listBeans, i, i - 1);
                }
            }
            blackListAdapter.notifyItemMoved(fromPosition, toPosition);
            isSort = true;
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //侧滑删除可以使用；
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        /**
         * 长按选中Item的时候开始调用
         * 长按高亮
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.c_transparent));
                //获取系统震动服务//震动70毫秒
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(70);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原高亮
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
//            blackListAdapter.notifyDataSetChanged();  //完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    });

    @Override
    public EditImgPresenter initPresenter() {
        return new EditImgPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_edit_img;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("主播图片");
        setBackPress();
        nodataTipsTv.setText("暂无数据");
        right_tv.setText("编辑");
        watchListRl.setLayoutManager(new GridLayoutManager(this, 3));
        blackListAdapter = new EditImgAdapter(listBeans, this);
        watchListRl.setAdapter(blackListAdapter);
        blackListAdapter.addChildClickViewIds(R.id.deleteImg, R.id.anchorListRiv);
        blackListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                switch (view.getId()) {
                    case R.id.anchorListRiv:
                        ArrayList<String> urls = new ArrayList<>();
                        for (int i = 0; i < listBeans.size(); i++) {
                            urls.add(listBeans.get(i).getUrl());
                        }
                        CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                        bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                        bigPictureDialog.setArguments(bundle);
                        bigPictureDialog.show(getSupportFragmentManager(), "CheckBigPictureDialog");

                        break;
                    case R.id.deleteImg:
                        presenter.deleteImage(listBeans.get(position).getId());
                        deletePosition = position;
                        break;
                }

            }
        });
        watchListRl.setItemAnimator(new DefaultItemAnimator());
        helper.attachToRecyclerView(watchListRl);
        helper.attachToRecyclerView(null);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        int resultCode = result.getResultCode();
                        if (resultCode == Activity.RESULT_OK) {
                            imgMark = data.getStringExtra("ImageType");
                        }
                        presenter.subMitImg(imgUrl, imgMark);
                    }
                });
    }

    @OnClick({R.id.right_tv, R.id.addImg})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                if (isEdit) {
                    isEdit = false;
                    right_tv.setText("编辑");
                    helper.attachToRecyclerView(null);
                    if (isSort) {
                        presenter.subSortImg(sortImg());
                        isSort = false;
                    }
                    isSort = false;
                } else {
                    isEdit = true;
                    helper.attachToRecyclerView(watchListRl);
                    right_tv.setText("保存");
                }
                blackListAdapter.setCanDelete(isEdit);
                break;
            case R.id.addImg:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .isEnableCrop(true)
//                        .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                        .withAspectRatio(2, 3)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(1)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                // onResult Callback
                                String sad = "";
                                List<File> files = new ArrayList<>();
                                for (int i = 0; i < result.size(); i++) {
                                    File file = new File(result.get(i).getCutPath());
                                    files.add(file);
                                }
                                presenter.subImg(files);
                            }

                            @Override
                            public void onCancel() {
                                // onCancel Callback
                            }
                        });
                break;
        }
    }

    private String sortImg() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < listBeans.size(); i++) {
            stringBuffer.append(listBeans.get(i).getId() + ",");
        }
        return stringBuffer.toString();
    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void setImageSuccess(AnchorImgEntity data) {
        isEdit = false;
        blackListAdapter.setCanDelete(false);
        if (data.getPicture().size() > 0) {
            watchListRl.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
            listBeans.clear();
            listBeans.addAll(data.getPicture());
            blackListAdapter.notifyDataSetChanged();
        } else {
            watchListRl.setVisibility(View.VISIBLE);
            nodataView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getImg();
    }

    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data) {
        imgUrl = data.getUrl().get(0);
        Intent intent = new Intent(EditImgAct.this, ImgMarkAct.class);
        intent.putExtra("Img_url", imgUrl);
//        presenter.subMitImg(data.getUrl().get(0));
        intentActivityResultLauncher.launch(intent);
    }

    @Override
    public void setDeleteSuccess() {

        listBeans.remove(deletePosition);
        blackListAdapter.notifyItemRemoved(deletePosition);
//必须调用这行代码
        blackListAdapter.notifyItemRangeChanged(deletePosition, listBeans.size());
    }

}