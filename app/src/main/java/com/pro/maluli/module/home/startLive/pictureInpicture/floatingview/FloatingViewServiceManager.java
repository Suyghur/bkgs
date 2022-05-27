package com.pro.maluli.module.home.startLive.pictureInpicture.floatingview;

import android.app.Activity;

/**
 * @author #Suyghur.
 * Created on 2022/5/27
 */
public class FloatingViewServiceManager {

    public static FloatingViewServiceManager getInstance() {
        return FloatingViewServiceManagerHolder.INSTANCE;
    }

    public void init(Activity activity) {

    }

    public void show() {

    }

    public void hide() {

    }

    private static final class FloatingViewServiceManagerHolder {
        private static final FloatingViewServiceManager INSTANCE = new FloatingViewServiceManager();

        /**
         * 防止单例对象在反序列化时重新生成对象
         */
        private static Object readResolve() {
            return INSTANCE;
        }
    }
}
