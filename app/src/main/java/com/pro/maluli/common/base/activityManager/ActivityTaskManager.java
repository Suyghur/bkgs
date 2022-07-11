package com.pro.maluli.common.base.activityManager;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.pro.maluli.ktx.utils.Logger;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Activity栈管理类，当Activity被创建是压栈，销毁时出栈
 */
public class ActivityTaskManager {

    private static final Singleton<ActivityTaskManager> SINGLETON =
            new Singleton<ActivityTaskManager>() {
                @Override
                protected ActivityTaskManager create() {
                    return new ActivityTaskManager();
                }
            };
    private final CopyOnWriteArrayList<Activity> ACTIVITY_ARRAY = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Activity> SINGLE_INSTANCE_ACTIVITY_ARRAY =
            new CopyOnWriteArrayList<>();

    public static ActivityTaskManager getInstance() {
        return SINGLETON.get();
    }

    public void put(@NonNull Activity targetActivity) {
        Logger.d(targetActivity.toString());
        boolean hasActivity = false;
        for (Activity activity : ACTIVITY_ARRAY) {
            if (targetActivity == activity) {
                hasActivity = true;
                break;
            }
        }
        if (!hasActivity) {
            ACTIVITY_ARRAY.add(targetActivity);
        }
    }

    public void remove(@NonNull Activity targetActivity) {
        for (Activity activity : ACTIVITY_ARRAY) {
            if (targetActivity != null) {
                if (targetActivity == activity) {
                    ACTIVITY_ARRAY.remove(targetActivity);
                    break;
                }
            }
        }
    }

    public void putSingleInstanceActivity(Activity targetActivity) {
        boolean hasActivity = false;
        for (Activity activity : SINGLE_INSTANCE_ACTIVITY_ARRAY) {
            if (targetActivity == activity) {
                hasActivity = true;
                break;
            }
        }
        if (!hasActivity) {
            SINGLE_INSTANCE_ACTIVITY_ARRAY.add(targetActivity);
        }
    }

    public void removeSingleInstanceActivity(@NonNull Activity targetActivity) {
        for (Activity activity : SINGLE_INSTANCE_ACTIVITY_ARRAY) {
            if (targetActivity != null) {
                if (targetActivity == activity) {
                    SINGLE_INSTANCE_ACTIVITY_ARRAY.remove(targetActivity);
                    break;
                }
            }
        }
    }

    public CopyOnWriteArrayList<Activity> getSingleInstanceActivityArray() {
        return SINGLE_INSTANCE_ACTIVITY_ARRAY;
    }

    public Activity getTopActivity() {
        if (ACTIVITY_ARRAY.isEmpty()) {
            return null;
        }
        return ACTIVITY_ARRAY.get(0);
    }

    public Activity getLastActivity() {
        if (ACTIVITY_ARRAY.isEmpty()) {
            return null;
        }
        return ACTIVITY_ARRAY.get(ACTIVITY_ARRAY.size() - 1);
    }
}
