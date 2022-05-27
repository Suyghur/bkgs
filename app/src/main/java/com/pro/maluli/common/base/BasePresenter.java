package com.pro.maluli.common.base;

import android.app.Activity;
import android.content.Context;

import com.pro.maluli.common.networkRequest.Api;
import com.pro.maluli.common.networkRequest.ApiFactory;
import com.pro.maluli.common.networkRequest.ErrorConsumer;
import com.pro.maluli.common.utils.LoadingUtils;
import com.pro.maluli.common.utils.NetUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 23203
 */
public abstract class BasePresenter<V extends BaseView> implements IClear {
    protected final long RETRY_TIMES = 0;
    protected final CompositeDisposable mDisposables = new CompositeDisposable();
    protected V mView;
    protected Context mContext;
    protected Api mService;
    protected Api mService1;

    public BasePresenter(Context context) {
        mContext = context;
        mService = ApiFactory.create();
        mService1 = ApiFactory.create();
    }

    protected ObservableTransformer getTransformer() {
        return new ObservableTransformer<Object, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<Object> observable) {
                return observable.retry(RETRY_TIMES)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (!NetUtil.isConnected(mContext)) {
                                    disposable.dispose();
                                    mView.onError(2000, "网络连接异常,请检查网络");
                                    dismissLoading(mContext);
                                }
                            }
                        })
                        .doOnError(new ErrorConsumer(mView))
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                dismissLoading(mContext);
                            }
                        });
            }
        };
    }

    /**
     * 显示加载框
     */
    public void showLoading(final Context mContext) {
        if (mContext == null) {
            return;
        }
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtils.getLoadingUtils().showLoadingView(mContext);
            }
        });
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoading(final Context mContext) {
        if (mContext == null) {
            return;
        }
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoadingUtils.getLoadingUtils().hideLoadingView(mContext);
                } catch (Exception e) {

                }
            }
        });
    }

    protected void add(Disposable disposable) {
        mDisposables.add(disposable);
    }

    @Override
    public void clear() {
        mDisposables.clear();
    }

    public void attach(V view) {
        this.mView = view;
    }

    public void detach() {
        //this.view = null;
    }

}
