package com.pro.maluli.module.socketService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pro.maluli.common.networkRequest.Url;
import com.pro.maluli.common.utils.ToolUtils;
import com.pro.maluli.module.app.MyAppliaction;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by lzj on 2018/1/5.
 */
public class DataService extends Service {

    public static final String TAG = "DataService";
    public String notificationId = "data_service_id";
    public String notificationName = "data_name";
    public static final String RES_TRADELIST = "TRADELIST";

    public String defult_deptArea = "";

    private WebSocketClient mWebSocketClient;//唯一
    private DataServiceBinder mBinder;
    private int RECONNECT_TIME = 3000;
    private String socketUrl;

    private static final Intent SERVICE_INTENT = new Intent();

    static {
        SERVICE_INTENT.setComponent(new ComponentName(MyAppliaction.getApp().getPackageName(),
                "com.pro.maluli.module.socketService.DataService"));
    }

    public static Intent getIntent() {
        return SERVICE_INTENT;
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, DataService.class);
//        intent.putExtra(LOGIN_UID, uid);
        return intent;
    }

    public boolean isRunning = false;
    public Handler socketHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (!isRunning) {
//                initSocket();
            }
            isRunning = false;
            socketHandler.sendMessageDelayed(socketHandler.obtainMessage(), 5 * 1000);
            return false;
        }
    });


    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationChannel = new NotificationChannel(notificationId,
//                        notificationName, NotificationManager.IMPORTANCE_HIGH);
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.setShowBadge(true);
//                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                manager.createNotificationChannel(notificationChannel);
//            }
//            Notification notification = new Notification.Builder(this).setChannelId(notificationId)
//                    .getNotification();
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            startForeground(705682, notification);
//        }
        mBinder = new DataServiceBinder();
        socketHandler.sendMessage(socketHandler.obtainMessage());
        Log.i(TAG, " 服务创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "服务获取数据启动");
//        if (mWebSocketClient == null) {
//            initSocket();
//        } else {
//            mWebSocketClient.close();
//            initSocket();
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Binder
    public class DataServiceBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }


    //
    public void initSocket(String url) {
        try {
            URI uri;
//            String socketurl = URLEncoder.encode(url,"utf-8");
            String socketurl =url;
                uri =  URI.create(socketurl);
            if (mWebSocketClient != null ) {
                return;
            }
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i(TAG, "onOpen: SOCKET 已启动");
                }

                @Override
                public void onMessage(String s) {
                    isRunning = true;
//                    FSocketRes res = new FSocketRes();
//                    res.fromJSONAuto(s);
                    Log.i(TAG, "onMessage: " + s);

//                    switch (res.type) {
//                        case "ping":
//                            DataService.this.sendPing();
//                            break;
//                        case "market_list":
////                            saveAllMarketList(res.data);
//                            break;
//                        case "trade_dept":
////                            saveDepthList(res.data);
//                            break;
//                        case "trade_json":
////                            saveTradeJson2(res.data);
//                            break;
//                        case "trade":
//                            JsonToTrade(res.data);
//                            break;
//                        case "trade_log":
//                            saveLogList(res.data);
//                            break;
//                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i(TAG, "onClose: SOCKET 已关闭");
                }

                @Override
                public void onError(Exception e) {
                    Log.i(TAG, "onClose: SOCKET 已关闭");
                }
            };

            //信任所有证书
            try {
                if(socketurl.contains("wss:")||socketurl.contains("https:")) {
                    mWebSocketClient.setSocket(SSLSocketClient.getSSLSocketFactory().createSocket());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void sendPing() {
        try {
            if (mWebSocketClient != null) {
                mWebSocketClient.send("ping");
            }
        } catch (Exception e) {

        }
    }



//    //市场数据转化数据
//    public void saveAllMarketList(final String data) {
//        Observable.just(data).observeOn(Schedulers.io()).map(new Func1<String, List<MarketListEntity>>() {
//            @Override
//            public List<MarketListEntity> call(String data) {
//                try {
//                    List<MarketListEntity> marketListEntities = MarketListEntity.fromJSONSocket(data);
//                    return marketListEntities;
//                } catch (Exception e) {
//                    return new ArrayList<MarketListEntity>();
//                }
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<MarketListEntity>>() {
//            @Override
//            public void call(List<MarketListEntity> list) {
//                MarketListEvent marketListEvent = new MarketListEvent(data, list);
//                EventBus.getDefault().post(marketListEvent);
//            }
//        });
//
//
//    }








}
