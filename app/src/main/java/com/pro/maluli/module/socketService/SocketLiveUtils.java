package com.pro.maluli.module.socketService;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pro.maluli.common.entity.OnTwoOneSocketEntity;
import com.pro.maluli.ktx.utils.Logger;
import com.pro.maluli.module.socketService.event.OTOEvent;
import com.pro.maluli.module.socketService.event.OnTwoOneStartEntity;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public enum SocketLiveUtils {
    INSTANCE;
    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    public boolean isRunning = false;
    public String socketUrl;
    boolean isFinish = false;
    private WebSocketClient mWebSocketClient;//唯一
    private Handler mHandler = new Handler();

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
                    Logger.d("开启重连");
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
            if (mWebSocketClient != null) {
                return;
            }
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
//                    msg_id 0-推送失败 1-正常推送 2-设定时间 3-加时 4-开始 5-结束
//                    FSocketRes res = new FSocketRes();
//                    res.fromJSONAuto(s);
                    Logger.e("onMessage: " + s);
                    OTOEvent oneEvent = new OTOEvent();
                    switch (msgType) {
                        case 1000://连接成功提示
                            break;
                        case 1://一对一预约页面
                            OnTwoOneStartEntity entity = JSONObject.parseObject(JSON.parseObject(s).getString("data"), OnTwoOneStartEntity.class);
                            oneEvent.setEntity(entity);
                            oneEvent.setMsg_id(msgType);
                            EventBus.getDefault().post(oneEvent);
                            break;
                        case 5://结束直播
                        case 6://评分
                        case 7://用户异常退出
                        case 403://用户异常退出
                        case 404://用户异常退出
                            oneEvent.setMsg_id(msgType);
                            EventBus.getDefault().post(oneEvent);
                            break;
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Logger.e("onClose: SOCKET 已关闭");
//                    isFinish=true;
                }

                @Override
                public void onError(Exception e) {
                    Logger.e("onClose: SOCKET 已关闭,连接出错, err: " + e.getLocalizedMessage());
                    e.printStackTrace();
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
    }    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.e("心跳包检测websocket连接状态");
            if (isFinish) {
                closeConnect();
                return;
            }
            if (mWebSocketClient != null) {
                if (mWebSocketClient.isClosed()) {
//                    reconnectWs();
                    mWebSocketClient = null;
                    initSocket(socketUrl);
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
            if (mHandler != null) {
                mHandler.removeCallbacks(heartBeatRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWebSocketClient = null;
        }
    }

    public interface SocketListener {
        void OnTwoOneYY(OnTwoOneSocketEntity entity);
    }





}