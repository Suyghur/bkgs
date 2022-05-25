package com.pro.maluli.module.socketService;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pro.maluli.common.entity.OnTwoOneSocketEntity;
import com.pro.maluli.common.entity.ReserveEntity;
import com.pro.maluli.module.socketService.event.OnTwoOneEvent;
import com.pro.maluli.toolkit.Logger;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public enum SocketUtils {
    INSTANCE;
    private WebSocketClient mWebSocketClient;//唯一
    public boolean isRunning = false;
    public String socketUrl;
    boolean isFinish = false;


    public interface SocketListener {
        void OnTwoOneYY(OnTwoOneSocketEntity entity);
    }

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.e("心跳包检测websocket连接状态");
            if (isFinish) {
                return;
            }
            if (mWebSocketClient != null) {
                if (mWebSocketClient.isClosed()) {
                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                mWebSocketClient = null;
                initSocket(socketUrl);
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    public void onStartCommand(String url) {
        initSocket(url);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
    }

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Logger.e("开启重连");
                    mWebSocketClient.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void initSocket(String url) {
        this.socketUrl = url;
        isFinish = false;
        try {
            URI uri;
            uri = URI.create(socketUrl);
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Logger.e("onOpen: SOCKET 已启动");
                }

                @Override
                public void onMessage(String s) {
                    isRunning = true;
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    int msgType = jsonObject.getInteger("msg_id");
                    Logger.e("onMessage: " + s);
                    switch (msgType) {
                        case 1000://连接成功提示
                            break;
                        case 1://一对一预约页面
                            ReserveEntity entity = JSONObject.parseObject(JSON.parseObject(s).getString("data"), ReserveEntity.class);
                            OnTwoOneEvent oneEvent = new OnTwoOneEvent(entity);
                            EventBus.getDefault().post(oneEvent);
                            break;
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Logger.e("onClose: SOCKET 已关闭");
                }

                @Override
                public void onError(Exception e) {
                    Logger.e("onClose: SOCKET 已关闭");
                }
            };

            //信任所有证书
            try {
                if (socketUrl.contains("wss:") || socketUrl.contains("https:")) {
                    mWebSocketClient.setSocket(SSLSocketClient.getSSLSocketFactory().createSocket());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            connect();
//            mWebSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    mWebSocketClient.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 断开连接
     */
    public void closeConnect() {
        isFinish = true;
        try {
            if (null != mWebSocketClient) {
                mWebSocketClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWebSocketClient = null;
        }
    }
}