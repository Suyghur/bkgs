package com.pro.maluli.module.myself.setting.feedback.feedBackDetail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.entity.FeedBackDetailEntity;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.StringUtils;
import com.pro.maluli.common.view.dialogview.BaseTipsDialog;
import com.pro.maluli.common.view.dialogview.bigPicture.CheckBigPictureDialog;
import com.pro.maluli.module.myself.setting.feedback.feedBackDetail.adapter.FeedBackDetailAdapter;
import com.pro.maluli.module.myself.setting.feedback.feedBackDetail.adapter.FeedBackDetailGirdAdapter;
import com.pro.maluli.module.myself.setting.feedback.feedBackDetail.presenter.FeedBackDetailPresenter;
import com.pro.maluli.module.myself.setting.feedback.feedBackDetail.presenter.IFeedBackDetailContraction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class FeedBackDetailAct extends BaseMvpActivity<IFeedBackDetailContraction.View,
        FeedBackDetailPresenter> implements IFeedBackDetailContraction.View {
    FeedBackDetailAdapter adapter;
    @BindView(R.id.right_tv)
    TextView right_tv;
    @BindView(R.id.replyXrl)
    RecyclerView replyXrl;
    @BindView(R.id.feedTv)
    TextView feedTv;
    @BindView(R.id.feedXrl)
    RecyclerView feedXrl;
    @BindView(R.id.msgTypeTv)
    TextView msgTypeTv;
    @BindView(R.id.msgNumberTv)
    TextView msgNumberTv;
    @BindView(R.id.subTimeTv)
    TextView subTimeTv;
    @BindView(R.id.noReptLL)
    LinearLayout noReptLL;
    FeedBackDetailGirdAdapter feedBackDetailGirdAdapter;
    private List<FeedBackDetailEntity.ReplyBean> replyBeans;
    private String id;
    private List<String> ReportImgs;

    @Override
    public FeedBackDetailPresenter initPresenter() {
        return new FeedBackDetailPresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
        id = getIntent().getStringExtra("FeedBackID");
        ReportImgs = new ArrayList<>();
        replyBeans = new ArrayList<>();
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_feed_back_detail;
    }

    @Override
    public void viewInitialization() {
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText("删除");
        setBackPress();
        setTitleTx("反馈详情");
        replyXrl.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedBackDetailAdapter(replyBeans, this);
        replyXrl.setAdapter(adapter);

        feedXrl.setLayoutManager(new GridLayoutManager(this, 4));
        feedBackDetailGirdAdapter = new FeedBackDetailGirdAdapter(ReportImgs, this);
        feedXrl.setAdapter(feedBackDetailGirdAdapter);

        feedBackDetailGirdAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                ArrayList<String> urls = new ArrayList<>();
                for (int i = 0; i < ReportImgs.size(); i++) {
                    urls.add(ReportImgs.get(i));
                }
                CheckBigPictureDialog bigPictureDialog = new CheckBigPictureDialog();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(CheckBigPictureDialog.EXTRA_IMAGE_URLS, urls);
                bundle.putInt(CheckBigPictureDialog.EXTRA_IMAGE_INDEX, position);
                bigPictureDialog.setArguments(bundle);
                bigPictureDialog.show(getSupportFragmentManager(), "CheckBigPictureDialog");
            }
        });


    }

    @OnClick({R.id.right_tv})
    public void OnclickView(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.right_tv:
                BaseTipsDialog baseTipsDialog1 = new BaseTipsDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("showContent", "是否删除该反馈记录，删除后不可恢复");
                baseTipsDialog1.setArguments(bundle2);
                baseTipsDialog1.show(getSupportFragmentManager(), "BaseTipsDialog");
                baseTipsDialog1.setOnConfirmListener(new BaseTipsDialog.OnBaseTipsListener() {
                    @Override
                    public void comfirm() {
                        presenter.deleteFeedBack(id);
                    }
                });

                break;
        }
    }

    @Override
    public void doBusiness() {
        presenter.getFeedbackDetail(id);
    }

    @Override
    public void setDetail(FeedBackDetailEntity data) {
        ReportImgs.clear();
        ReportImgs.addAll(data.getReport().getImages());
        feedBackDetailGirdAdapter.notifyDataSetChanged();
        feedTv.setText(data.getReport().getContent());
        msgTypeTv.setText("联系方式：" + StringUtils.StringToNull(data.getReport().getRel_type()));
        msgNumberTv.setText("联系账号：" + StringUtils.StringToNull(data.getReport().getRel()));
        subTimeTv.setText("提交日期：" + StringUtils.StringToNull(data.getReport().getCreated_at()));


        if (data.getReply().size() == 0) {
            noReptLL.setVisibility(View.GONE);
        } else {
            noReptLL.setVisibility(View.VISIBLE);
            replyBeans.clear();
            replyBeans.addAll(data.getReply());
            adapter.notifyDataSetChanged();
        }

    }
}