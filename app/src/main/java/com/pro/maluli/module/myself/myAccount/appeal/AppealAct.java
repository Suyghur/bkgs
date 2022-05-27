package com.pro.maluli.module.myself.myAccount.appeal;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.ImageEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.view.popwindow.SelectChatWindow;
import com.pro.maluli.module.myself.myAccount.appeal.adapter.AppealAdapter;
import com.pro.maluli.module.myself.myAccount.appeal.presenter.AppealPresenter;
import com.pro.maluli.module.myself.myAccount.appeal.presenter.IAppealContraction;
import com.yalantis.ucrop.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class AppealAct extends BaseMvpActivity<IAppealContraction.View, AppealPresenter> implements IAppealContraction.View {
    AppealAdapter adapter;

    @BindView(R.id.selectImgRv)
    RecyclerView selectImgRv;
    @BindView(R.id.inputContentEt)
    EditText inputContentEt;
    @BindView(R.id.inputNumberEt)
    EditText inputNumberEt;
    @BindView(R.id.submitFeedTv)
    TextView submitFeedTv;
    @BindView(R.id.feedTypeTv)
    TextView feedTypeTv;
    @BindView(R.id.selectChatRl)
    LinearLayout selectChatRl;
    List<File> files = new ArrayList<>();
    private List<ImageEntity> imageEntities = new ArrayList<>();
    private String msgType = "phone";
    private String images;
    private String liveId;
    private String reportUserid;
    private String subType;//1 主播申诉  2举报直播间，3意见反馈,4冻结申诉，5私信举报

    @Override
    public AppealPresenter initPresenter() {
        return new AppealPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        subType = getIntent().getStringExtra("SUB_TYPE");
        liveId = getIntent().getStringExtra("liveId");
        reportUserid = getIntent().getStringExtra("reportUserid");
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_appeal;
    }

    @Override
    public void viewInitialization() {
        switch (subType) {
            case "1":
                setTitleTx("直播申诉");
                break;
            case "2":
                setTitleTx("举报");
                break;
            case "3":
                setTitleTx("反馈");
                break;
            case "4":
                setTitleTx("冻结申诉");
                break;
            case "5":
                setTitleTx("举报");
                break;
        }

        setBackPress();
        imageEntities.add(new ImageEntity("添加", 0));
        boolean adfa = imageEntities.contains("添加");
        adapter = new AppealAdapter(imageEntities);
        selectImgRv.setLayoutManager(new GridLayoutManager(this, 4));
        selectImgRv.setAdapter(adapter);
//         先注册需要点击的子控件id（注意，请不要写在convert方法里）
        adapter.addChildClickViewIds(R.id.selectImgRiv, R.id.deleteImg);
// 设置子控件点击监听
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.selectImgRiv:
                        selectFormLocation(position);
                        break;
                    case R.id.deleteImg:
                        imageEntities.remove(position);//删除数据源,移除集合中当前下标的数据

                        boolean isAdd = false;
                        for (int i = 0; i < imageEntities.size(); i++) {
                            if (imageEntities.get(i).getType() == 0) {
                                isAdd = true;
                            }
                        }
                        if (!isAdd) {
                            imageEntities.add(new ImageEntity("添加", 0));
                        }

//                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(position);//刷新被删除的地方
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount()); //刷新被删除数据，以及其后面的数据
                        break;
                }

            }
        });
    }

    private void selectFormLocation(int position) {
        if (imageEntities.get(position).getType() == 1) {
//            ArrayList<String> urls = new ArrayList<>();
//            for (int i = 0; i < imageEntities.size(); i++) {
//                if (imageEntities.get(position).getType()==1){
//                    urls.add(imageEntities.get(i).getUrl());
//                }
//            }
//            CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
//            bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
//            bigPictureDialog.setArguments(bundle);
//            bigPictureDialog.show(getSupportFragmentManager(), "CheckBigPictureDialog");

            return;
        }
        int max = 5 - imageEntities.size();
        PictureSelector.create(AppealAct.this)
                .openGallery(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(max)
//                .selectionData(reselectImg)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // onResult Callback
//                        files.clear();
                        for (int i = 0; i < result.size(); i++) {
                            imageEntities.add(imageEntities.size() - 1 >= 0 ? imageEntities.size() - 1 : 0
                                    , new ImageEntity(result.get(i).getCutPath(), 1));
                        }
                        if (imageEntities.size() > 4) {
                            imageEntities.remove(imageEntities.size() - 1);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }


    @Override
    public void doBusiness() {

    }

    @OnClick({R.id.selectChatRl, R.id.submitFeedTv})
    public void onClickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.selectChatRl:
                new SelectChatWindow(this, selectChatRl, new SelectChatWindow.PayTypeAllListener() {
                    @Override
                    public void onSelectItem(int position) {
                        if (position == 0) {
                            msgType = "phone";
                            feedTypeTv.setText("手机");
                        } else {
                            msgType = "email";
                            feedTypeTv.setText("邮箱");
                        }


                    }
                });
                break;
            case R.id.submitFeedTv:
                if (!ToolUtils.isFastClick()) {
                    return;
                }
                submitFeedTv.setEnabled(false);
                if (TextUtils.isEmpty(inputContentEt.getText().toString().trim())) {
                    ToastUtils.showShort("举报/申诉/意见反馈内容不能为空");
                    submitFeedTv.setEnabled(true);
                    return;
                }
                if (msgType.equalsIgnoreCase("phone")) {
                    if (!RegexUtils.isMobileExact(inputNumberEt.getText().toString().trim())) {
                        ToastUtils.showShort("请输入正确的手机号码");
                        submitFeedTv.setEnabled(true);
                        return;
                    }
                } else {
                    if (!RegexUtils.isEmail(inputNumberEt.getText().toString().trim())) {
                        ToastUtils.showShort("请输入正确邮箱");
                        submitFeedTv.setEnabled(true);
                        return;
                    }
                }
                files.clear();
                for (int i = 0; i < imageEntities.size(); i++) {
                    if (imageEntities.get(i).getType() == 1) {
                        File file = new File(imageEntities.get(i).getUrl());
                        files.add(file);
                    }
                }
                if (files.size() > 0) {
                    presenter.subImg(files);
                    return;
                }
                subAppeal();


                break;
        }
    }

    //1 主播申诉  2举报直播间，3意见反馈,4冻结申诉，5私信举报
    private void subAppeal() {
        String content = inputContentEt.getText().toString().trim();
        switch (subType) {
            case "1":
                presenter.liveAppeal(content, images, msgType, inputNumberEt.getText().toString().trim());

                break;
            case "2":
                presenter.reportLive(content,
                        images, msgType, inputNumberEt.getText().toString().trim(), "1", "", liveId);
                break;
            case "3":
                presenter.subFeedBlack(content,
                        images, msgType, inputNumberEt.getText().toString().trim());
                break;
            case "4":
                presenter.drawCashReport(content, images, msgType, inputNumberEt.getText().toString().trim());
                break;
            case "5":
                presenter.reportLive(content,
                        images, msgType, inputNumberEt.getText().toString().trim(), "0", reportUserid, "");
                break;
        }
    }

    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data) {
        images = StringUtils.sortImg(data.getUrl());
        subAppeal();
    }
}