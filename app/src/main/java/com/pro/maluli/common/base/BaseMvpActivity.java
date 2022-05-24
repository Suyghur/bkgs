package com.pro.maluli.common.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.BarUtils;
import com.pro.maluli.common.base.BaseActivity;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseView;
import com.pro.maluli.common.entity.MyAccountEntity;

/**
 * Created by hankkin on 2017/3/29.
 */

public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {

    protected P presenter;

    public abstract P initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = initPresenter();
        presenter.attach((V) this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }




}
