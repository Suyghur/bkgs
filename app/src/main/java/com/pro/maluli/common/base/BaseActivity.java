package com.pro.maluli.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pro.maluli.R;
import com.pro.maluli.common.base.activityManager.ActivityTaskManager;
import com.pro.maluli.common.utils.ToolUtils;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected Context mContext;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setR_Layout());
        mContext = this;
        ButterKnife.bind(this);
        baseInitialization();
        viewInitialization();
        doBusiness();
        ActivityTaskManager.getInstance().put(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isJump = getIntent().getBooleanExtra("isJump", false);
        if (isJump) {
            return;
        }
        if (ToolUtils.isServiceRunning(this, "com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service.FloatingViewService")) {
            return;
        }
        if (ToolUtils.isServiceRunning(this, "com.pro.maluli.module.home.startLive.pictureInpicture.floatingview.service.FloatingViewMoreService")) {
            return;
        }
        checkActivityJump();
    }

    private void checkActivityJump() {
        if (ActivityTaskManager.getInstance().getLastActivity() != null) {
            // 如果当前的activity跟添加进去的最后一个activity不是同一个的话，那么这种哦情况就有可能是最后一个activity的启动模式是SingleInstance，
            // 所以这时候就要遍历添加进去的SingleInstanceActivityArray，看是否有存在，有的话并且跟最后一个添加进去的activity是同一个的话就跳转
            // 这里设置了跳转动画，是因为单例模式的跳转动画跟其他的模式不一样，看起来很难受，设置后看起来舒服些，也可以设置别的动画，
            // 退到后台再进来会一闪，十分明显，添加跳转动画看起来也会舒服些
            if (!ActivityTaskManager.getInstance().getLastActivity().getClass().getName().equals(this.getClass().getName())) {
                if (ActivityTaskManager.getInstance().getSingleInstanceActivityArray().size() > 0) {
                    for (Activity activity : ActivityTaskManager.getInstance().getSingleInstanceActivityArray()) {
                        if (activity.getClass().getName().equals(ActivityTaskManager.getInstance().getLastActivity().getClass().getName())) {
                            ActivityTaskManager.getInstance().removeSingleInstanceActivity(activity);
                            startActivity(
                                    new Intent(this, ActivityTaskManager.getInstance().getLastActivity().getClass()));
                            overridePendingTransition(0, 0);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 基本初始化工作放在这个方法 如 P类
     */
    public abstract void baseInitialization();

    /**
     * 设置布局文件
     */
    public abstract int setR_Layout();

    /**
     * 控件初始化工作放在这个方法
     */
    public abstract void viewInitialization();

    /**
     * 业务逻辑放在这个方法 如获取网络数据
     */
    public abstract void doBusiness();

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


//    //返回监听
//    public void setBackViewPress() {
//        try {
//            findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        } catch (Exception e) {
//        }
//    }

    //设置title
    public void setTitleTx(String title_tx) {
        try {
            TextView title = findViewById(R.id.title);
            title.setText(title_tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //返回监听
    public void setBackPress() {
        try {
            View backView = findViewById(R.id.leftImg_ly);
            backView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int code, String msg) {
        showToast(msg);
    }


    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    //    //返回监听
//    public void setBackPress() {
//        try {
//            View backView = findViewById(R.id.leftImg_ly);
//            backView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        } catch (Exception e) {
//
//        }
//    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 如果不在这里移除当前activity的话，在启动另一个界面的onStart的时候，判断处于栈顶的activity，
        // 也就是ActivityTaskManager.getInstance().getLastActivity()的时候，就会出现错误。
        // 原因是，当一个activity的onPause之后就会启动另一个activity，还没经历过onDestroy，
        // 而removeActivity();只放在onDestroy中的话就会在启动的activity的onStart
        // 获取ActivityTaskManager.getInstance().getLastActivity()返回的是错误。
        // 注意事项：应该在所有主动关闭activity，就是调用finish的时候要调用此代码，比如使用toolbar的返回键的时候，也应该调用removeActivity
        removeActivity();
    }

    @Override
    public void finish() {
        removeActivity();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
    }

    /**
     * 释放资源
     */
    private void removeActivity() {
        ActivityTaskManager.getInstance().remove(this);
    }
}
