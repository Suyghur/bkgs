package com.pro.maluli.module.home.base.applyForAnchor;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.ApplyLimitEntity;
import com.pro.maluli.common.entity.HomeInfoEntity;
import com.pro.maluli.common.entity.ImageEntity;
import com.pro.maluli.common.entity.UpdateImgEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.utils.glideImg.GlideEngine;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.home.base.applyForAnchor.presenter.ApplyForAnchorPresenter;
import com.pro.maluli.module.home.base.applyForAnchor.presenter.IApplyForAnchorContraction;
import com.pro.maluli.module.myself.myAccount.appeal.adapter.AppealAdapter;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
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
public class ApplyForAnchorAct extends BaseMvpActivity<IApplyForAnchorContraction.View,
        ApplyForAnchorPresenter> implements IApplyForAnchorContraction.View {

    @BindView(R.id.selectClassLL)
    LinearLayout selectClassLL;
    @BindView(R.id.inputTrueNameEt)
    EditText inputTrueNameEt;
    @BindView(R.id.inputNumberEt)
    EditText inputNumberEt;
    @BindView(R.id.inputCarEt)
    EditText inputCarEt;
    @BindView(R.id.personTv)
    EditText personTv;
    @BindView(R.id.inputMaxTv)
    TextView inputMaxTv;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.idCarZmIv)
    ImageView idCarZmIv;
    @BindView(R.id.idCarZmRl)
    RelativeLayout idCarZmRl;
    @BindView(R.id.idCarFmIv)
    ImageView idCarFmIv;
    @BindView(R.id.idCarFmRl)
    RelativeLayout idCarFmRl;
    @BindView(R.id.shouchiImg)
    ImageView shouchiImg;
    @BindView(R.id.shouchiRl)
    RelativeLayout shouchiRl;
    @BindView(R.id.selectImgRv)
    RecyclerView selectImgRv;
    @BindView(R.id.checkImg)
    ImageView checkImg;
    @BindView(R.id.yinshiTv)
    TextView yinshiTv;
    @BindView(R.id.classificationTv)
    TextView classificationTv;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.shenheStatusTv)
    TextView shenheStatusTv;
    @BindView(R.id.AuditContentTv)
    TextView AuditContentTv;
    AppealAdapter adapter;
    List<File> files = new ArrayList<>();
    private List<ImageEntity> imageEntities = new ArrayList<>();
    private List<HomeInfoEntity.CategoryBean.ListBean> categorylist = new ArrayList<>();
    private List<List<HomeInfoEntity.CategoryBean.ListBean.ChildBean>> cardItem = new ArrayList<>();
    private int classificationId = -1;
    private String font_card_image;//???????????????
    private String back_card_image;//???????????????
    private String holding_card_image;//???????????????
    private String certificate_image;//????????????
    private String reason;
    private int maxText = 200;
    private int maxImg = 3;
    private OptionsPickerView pvCustomOptions;

    @Override
    public ApplyForAnchorPresenter initPresenter() {
        return new ApplyForAnchorPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        categorylist = (List<HomeInfoEntity.CategoryBean.ListBean>) getIntent().getSerializableExtra("classification");
        reason = getIntent().getStringExtra("REASON");
        if (!TextUtils.isEmpty(reason)) {
            shenheStatusTv.setText("???????????????");
            AuditContentTv.setText(reason);
        }
        for (int i = 0; i < categorylist.size(); i++) {
            List<HomeInfoEntity.CategoryBean.ListBean.ChildBean> childBeans = new ArrayList<>();
            for (int j = 0; j < categorylist.get(i).getChildren().size(); j++) {
                childBeans.add(categorylist.get(i).getChildren().get(j));
            }
            cardItem.add(childBeans);
        }
        try {
            categorylist.remove(0);
            cardItem.remove(0);
        } catch (Exception e) {

        }

    }

    @Override
    public int setR_Layout() {
        return R.layout.act_apply_for_anchor;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("????????????");
        setBackPress();
        imageEntities.add(new ImageEntity("", 0));
        adapter = new AppealAdapter(imageEntities);
        selectImgRv.setLayoutManager(new GridLayoutManager(this, 3));
        selectImgRv.setAdapter(adapter);
//         ?????????????????????????????????id???????????????????????????convert????????????
        adapter.addChildClickViewIds(R.id.selectImgRiv, R.id.deleteImg);
// ???????????????????????????
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.selectImgRiv:
                        selectFormLocation(position);
                        break;
                    case R.id.deleteImg:
                        imageEntities.remove(position);//???????????????,????????????????????????????????????
                        boolean isAdd = false;
                        for (int i = 0; i < imageEntities.size(); i++) {
                            if (imageEntities.get(i).getType() == 0) {
                                isAdd = true;
                            }
                        }
                        if (!isAdd) {
                            imageEntities.add(new ImageEntity("??????", 0));
                        }
//                        adapter.notifyDataSetChanged();
                        adapter.notifyItemRemoved(position);//????????????????????????
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount()); //????????????????????????????????????????????????
                        break;
                }

            }
        });
        personTv.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//?????????????????????
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//???????????????????????????
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView??????????????????
                inputMaxTv.setText(s.length() + "/" + maxText);
                selectionStart = personTv.getSelectionStart();
                selectionEnd = personTv.getSelectionEnd();
                if (wordNum.length() > maxText) {
                    //????????????????????????????????????????????????
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    personTv.setText(s);
                    personTv.setSelection(tempSelection);//?????????????????????
                }

            }
        });

        String str = "????????????????????????????????????";

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("???") + 1;//????????????????????????
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (AntiShake.check(widget.getId())) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("PROTOCOL_ID", "5");
                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.c_8e1d77));
                ds.setUnderlineText(false);
            }
        }, start, start + 4, 0);

        yinshiTv.setMovementMethod(LinkMovementMethod.getInstance());
        yinshiTv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private void selectFormLocation(int position) {
        if (imageEntities.get(position).getType() == 1) {
            return;
        }
        int max = maxImg + 1 - imageEntities.size();
        PictureSelector.create(ApplyForAnchorAct.this)
                .openGallery(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(max)
//                .selectionData(selectImg)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // onResult Callback
                        for (int i = 0; i < result.size(); i++) {
                            imageEntities.add(imageEntities.size() - 1 >= 0 ? imageEntities.size() - 1 : 0
                                    , new ImageEntity(result.get(i).getCutPath(), 1));
                        }
                        if (imageEntities.size() > maxImg) {
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

    private void selectOneIMg(int type) {
        PictureSelector.create(ApplyForAnchorAct.this)
                .openGallery(PictureMimeType.ofImage())
                .isEnableCrop(true)
                .freeStyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        List<File> files = new ArrayList<>();
                        for (int i = 0; i < result.size(); i++) {
                            File file = new File(result.get(i).getCutPath());
                            files.add(file);
                        }
                        presenter.subImg(files, type);

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

    private void initCustomOptionPicker() {//??????????????????????????????????????????
        /**
         * @description
         *
         * ???????????????
         * ?????????????????????id??? optionspicker ?????? timepicker ??????????????????????????????????????????????????????????????????
         * ???????????????demo ????????????????????????layout?????????
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //?????????????????????????????????????????????
                classificationId = categorylist.get(options1).getChildren().get(option2).getId();
                classificationTv.setText(categorylist.get(options1).getTitle() + "-"
                        + categorylist.get(options1).getChildren().get(option2).getTitle());
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        LinearLayout dismissLL = (LinearLayout) v.findViewById(R.id.dismissLL);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        dismissLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .isDialog(false)
                .setOutSideCancelable(false)
                .build();

        pvCustomOptions.setPicker(categorylist, cardItem);//????????????
        pvCustomOptions.show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getVideoCanTime();
    }

    @OnClick({R.id.selectClassLL, R.id.idCarZmRl,
            R.id.idCarFmRl, R.id.shouchiRl,
            R.id.checkImg, R.id.yinshiTv, R.id.submitTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectClassLL:
                initCustomOptionPicker();
                break;
//            case R.id.idCarZmIv:
////                selectOneIMg(1);
//                break;
            case R.id.idCarZmRl:
                selectOneIMg(1);
                break;
//            case R.id.idCarFmIv:
//                break;
            case R.id.idCarFmRl:
                selectOneIMg(2);
                break;
//            case R.id.shouchiImg:
//                break;
            case R.id.shouchiRl:
                selectOneIMg(3);
                break;
            case R.id.checkImg:
                checkImg.setSelected(!checkImg.isSelected());
                break;
            case R.id.yinshiTv:
                break;
            case R.id.submitTv:
                if (!checkContent()) {
                    return;
                }
                files.clear();
                for (int i = 0; i < imageEntities.size(); i++) {
                    if (imageEntities.get(i).getType() == 1) {
                        File file = new File(imageEntities.get(i).getUrl());
                        files.add(file);
                    }
                }
                if (files.size() == 0) {
                    ToastUtils.showShort("???????????????????????????");
                    return;
                }
                presenter.subImg(files, 4);


                break;
        }
    }

    private boolean checkContent() {
        if (classificationId == -1) {
            ToastUtils.showShort("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(inputTrueNameEt.getText().toString().trim())) {
            ToastUtils.showShort("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(inputNumberEt.getText().toString().trim())) {
            ToastUtils.showShort("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(inputCarEt.getText().toString().trim())) {
            ToastUtils.showShort("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(personTv.getText().toString().trim())) {
            ToastUtils.showShort("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(font_card_image)) {
            ToastUtils.showShort("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(back_card_image)) {
            ToastUtils.showShort("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(holding_card_image)) {
            ToastUtils.showShort("????????????????????????????????????");
            return false;
        }
//        if (TextUtils.isEmpty(certificate_image)) {
//            ToastUtils.showShort("???????????????????????????");
//            return false;
//        }
        if (!checkImg.isSelected()) {
            ToastUtils.showShort("???????????????????????????");
            return false;
        }
        return true;

    }

    /**
     * @param data
     * @param type 1//????????????
     *             2 ???????????????
     *             3???????????????
     *             4????????????
     */
    @Override
    public void setUpdateImgSuccess(UpdateImgEntity data, int type) {
        switch (type) {
            case 1:
                font_card_image = data.getUrl().get(0);
                GlideUtils.loadImage(ApplyForAnchorAct.this, font_card_image, idCarZmIv);
                break;
            case 2:
                back_card_image = data.getUrl().get(0);
                GlideUtils.loadImage(ApplyForAnchorAct.this, back_card_image, idCarFmIv);
                break;
            case 3:
                holding_card_image = data.getUrl().get(0);
                GlideUtils.loadImage(ApplyForAnchorAct.this, holding_card_image, shouchiImg);
                break;
            case 4:
                certificate_image = StringUtils.sortImg(data.getUrl());
                presenter.subApplyForAnchor(classificationId, inputTrueNameEt.getText().toString().trim()
                        , inputNumberEt.getText().toString().trim(), inputCarEt.getText().toString().trim()
                        , personTv.getText().toString().trim(), font_card_image, back_card_image, holding_card_image, certificate_image);
                break;
        }

    }

    @Override
    public void setVideoTime(ApplyLimitEntity data) {
        maxText = Integer.parseInt(data.getMax_desc());
        maxImg = Integer.parseInt(data.getMax_picture());
        inputMaxTv.setText("0/" + maxText);
    }
}