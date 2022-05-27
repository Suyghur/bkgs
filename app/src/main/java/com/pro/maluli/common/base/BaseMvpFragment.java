package com.pro.maluli.common.base;

import android.os.Bundle;

/**
 *
 */

public abstract class BaseMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment {


    //是否执行唤醒监听
    public boolean wakeListener = true;
    public boolean isHidden = true;
    protected P presenter;

    public abstract P initPresenter();

    public abstract void onWakeBusiness();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = initPresenter();
        presenter.attach((V) this);
        super.onCreate(savedInstanceState);
    }


    //唤醒
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (presenter == null) {
            return;
        }
        if (!hidden && wakeListener) {
            onWakeBusiness();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden && wakeListener) {
            onWakeBusiness();
        }

    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }
}
