package com.pro.maluli.module.myself.myAccount.recharge;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.pro.maluli.R;
import com.pro.maluli.common.base.BaseMvpActivity;
import com.pro.maluli.common.constant.AppIdConstants;
import com.pro.maluli.common.entity.PayInfoEntity;
import com.pro.maluli.common.entity.RechargeEntity;
import com.pro.maluli.common.entity.WechatpayEntity;
import com.pro.maluli.common.utils.AntiShake;
import com.pro.maluli.common.utils.StatusbarUtils;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.common.utils.alipay.PayResult;
import com.pro.maluli.common.utils.glideImg.GlideUtils;
import com.pro.maluli.module.myself.myAccount.recharge.adapter.RechargePayTypeAdapter;
import com.pro.maluli.module.myself.myAccount.recharge.presenter.IRechargeContraction;
import com.pro.maluli.module.myself.myAccount.recharge.presenter.RechargePresenter;
import com.pro.maluli.module.myself.userAgreement.protocolDetail.ProtocolDetailAct;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Kingsley
 * @date 2021/6/15
 */
public class RechargeAct extends BaseMvpActivity<IRechargeContraction.View, RechargePresenter> implements IRechargeContraction.View {
    private static final int SDK_PAY_FLAG = 1;
    RechargePayTypeAdapter adapter;
    @BindView(R.id.payTypeRlv)
    RecyclerView payTypeRlv;
    @BindView(R.id.avatarWithdrawCiv)
    CircleImageView avatarWithdrawCiv;
    @BindView(R.id.wdUsrrNamtTv)
    TextView wdUsrrNamtTv;
    @BindView(R.id.canWithdrawTipsTv)
    TextView canWithdrawTipsTv;
    @BindView(R.id.canWithdrawTv)
    TextView canWithdrawTv;
    @BindView(R.id.selectPayTv)
    LinearLayout selectPayTv;
    @BindView(R.id.selectAlipayLL)
    LinearLayout selectAlipayLL;
    @BindView(R.id.loseMoneyTv)
    TextView loseMoneyTv;
    @BindView(R.id.successXyIv)
    ImageView successXyIv;
    @BindView(R.id.wechatIv)
    ImageView wechatIv;
    @BindView(R.id.alipayIv)
    ImageView alipayIv;
    @BindView(R.id.nowPayTv)
    TextView nowPayTv;
    @BindView(R.id.xieyiTv)
    TextView xieyiTv;
    List<RechargeEntity.PayBean> entities;
    private int selectPosition;
    private int selectId = -1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ??????????????????????????????????????????????????????????????????????????????
                        ToastUtils.showShort("???????????????????????????");
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public RechargePresenter initPresenter() {
        return new RechargePresenter(this);
    }

    @Override
    public void baseInitialization() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ffffff"));
        BarUtils.setStatusBarLightMode(this, true);
        StatusbarUtils.setStatusBarView(this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_recharge;
    }

    @Override
    public void viewInitialization() {
        setTitleTx("???????????????");
        setBackPress();
        entities = new ArrayList<>();

        adapter = new RechargePayTypeAdapter(entities);
        payTypeRlv.setLayoutManager(new GridLayoutManager(this, 3));
        payTypeRlv.setAdapter(adapter);
        // ?????????????????????????????????id???????????????????????????convert????????????
        adapter.addChildClickViewIds(R.id.payTypeLL);
        // ???????????????????????????
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.payTypeLL) {
                    selectPosition = position;
                    for (int i = 0; i < entities.size(); i++) {
                        if (i != selectPosition) {
                            entities.get(i).setSelect(false);
                        } else {
                            entities.get(i).setSelect(true);
                            loseMoneyTv.setText(entities.get(position).getMoney() + "");
                            selectId = entities.get(position).getId();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        String str = "??????????????????????????????????????????";

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
                bundle.putString("PROTOCOL_ID", "4");
                gotoActivity(ProtocolDetailAct.class, false, bundle);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.c_8e1d77));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);

        xieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
        xieyiTv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void doBusiness() {
        presenter.getPayInfo();
    }

    @Override
    public void setPayInfo(RechargeEntity data) {
        GlideUtils.loadImage(RechargeAct.this, data.getMember().getAvatar(), avatarWithdrawCiv);
        wdUsrrNamtTv.setText(data.getMember().getNickname());
        canWithdrawTv.setText(data.getMember().getMoney() + "");
        entities.clear();
        entities.addAll(data.getPay());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setOrderInfo(PayInfoEntity data) {

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeAct.this);
                Map<String, String> result = alipay.payV2(data.getPay(), true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // ??????????????????
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void setWeChatOrderInfo(WechatpayEntity data) {

        if (ToolUtils.isWeixinAvilible(RechargeAct.this)) {
            PayReq req = new PayReq();
            req.appId = AppIdConstants.APP_ID_WX;
            req.partnerId = data.getPay().getPartnerid();
            req.prepayId = data.getPay().getPrepayid();
            req.packageValue = data.getPay().getPackageX();
            req.nonceStr = data.getPay().getNoncestr();
            req.timeStamp = String.valueOf(data.getPay().getTimestamp());
            req.sign = data.getPay().getSign();
            IWXAPI msgApi = WXAPIFactory.createWXAPI(RechargeAct.this, AppIdConstants.APP_ID_WX, false);
            msgApi.sendReq(req);
        } else {
            ToastUtils.showShort("??????????????????");
        }
    }


    @OnClick({R.id.selectPayTv, R.id.selectAlipayLL, R.id.successXyIv, R.id.nowPayTv})
    public void onClick(View view) {
//        if (!ToolUtils.isFastClick()) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.selectPayTv://????????????
                selectPayType(0);
                break;
            case R.id.selectAlipayLL://???????????????
                selectPayType(1);
                break;
            case R.id.successXyIv:
                if (successXyIv.isSelected()) {
                    successXyIv.setSelected(false);
                } else {
                    successXyIv.setSelected(true);
                }
                break;
            case R.id.nowPayTv:
                if (!successXyIv.isSelected()) {
                    ToastUtils.showShort("????????????????????????");
                    return;
                }
                if (selectId == -1) {
                    ToastUtils.showShort("??????????????????????????????");
                    return;
                }
                if (alipayIv.isSelected()) {
                    presenter.subAlipay("2", String.valueOf(selectId));
                } else if (wechatIv.isSelected()) {
                    presenter.subWechatPay("1", String.valueOf(selectId));
                } else {
                    ToastUtils.showShort("?????????????????????");
                }
                break;
        }
    }

    private void selectPayType(int i) {
        if (i == 0) {
            alipayIv.setSelected(false);
            wechatIv.setSelected(true);
        } else {
            alipayIv.setSelected(true);
            wechatIv.setSelected(false);
        }
    }
}