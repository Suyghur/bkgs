package com.pro.maluli.module.message.searchMessasge.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MsgSearchOption;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseResponse;
import com.pro.maluli.common.entity.LiveListEntity;
import com.pro.maluli.common.entity.SearchEntity;
import com.pro.maluli.common.networkRequest.SuccessConsumer;

import java.util.List;

import io.reactivex.functions.Consumer;

public class SearchMsgPresenter extends BasePresenter<ISearchMsgContraction.View> implements ISearchMsgContraction.Presenter {
    public SearchMsgPresenter(Context context) {
        super(context);
    }

    public int page;
    public String  keyword;


    @Override
    public void getBkDetail() {
        // 搜索『关键字』最新的100条消息
        MsgSearchOption option = new MsgSearchOption();

        option.setSearchContent(keyword);
        option.setLimit(100);

        NIMClient.getService(MsgService.class).searchAllMessage(option)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>(){
                    @Override
                    public void onResult(int code, List<IMMessage> result, Throwable exception) {
                        mView.setBkDetailSuccess(result);
                        search(keyword);
                    }
                });

//        InvocationFuture<List<NimUserInfo>> searchUserInfosByKeyword(keyword);

    }
    @Override
    public void searchUser() {
        NIMClient.getService(UserService.class).searchUserInfosByKeyword(keyword).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> param) {
               mView.setUserNameInfo(param);
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    //    message/search
    public  void search(String searchKeyword){
        add(mService.searchMSg(searchKeyword)
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        getSearchHistory();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    public void getSearchHistory() {
        add(mService.getSearchMSgHistory()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<SearchEntity>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<SearchEntity> response) {
                        mView.setHistorySearch(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));

    }

    @Override
    public void deleteHistory() {
        add(mService.deleteMsgSearchHistory()
                .compose(getTransformer())
                .subscribe(new SuccessConsumer<BaseResponse<Object>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        mView.deleteSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
