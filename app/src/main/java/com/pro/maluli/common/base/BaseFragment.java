package com.pro.maluli.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pro.maluli.common.base.BasePresenter;
import com.pro.maluli.common.base.BaseView;


public abstract class BaseFragment extends Fragment implements BaseView {
    Toast mToast;
    public View mainView;

    /**
     * 鍩烘湰鍒濆鍖栧伐浣滄斁鍦ㄨ繖涓柟娉� 濡� P绫�
     */
    public abstract void baseInitialization();

    public abstract int setR_Layout();

    /**
     * 鎺т欢鍒濆鍖栧伐浣滄斁鍦ㄨ繖涓柟娉�
     */
    public abstract void viewInitialization();

    /**
     * 涓氬姟閫昏緫鏀惧湪杩欎釜鏂规硶 濡傝幏鍙栫綉缁滄暟鎹�
     */
    public abstract void doBusiness();

    protected Context mContext;
    protected BasePresenter basePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInitialization();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mainView == null) {
            mainView = inflater.inflate(setR_Layout(), container, false);
            viewInitialization();
        }
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doBusiness();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainView = null;
    }

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onDestroyView() {
        if (basePresenter != null) {
//            basePresenter.clear();
        }
        super.onDestroyView();
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
        Intent intent = new Intent(getActivity(), clz);
        if (ex != null) {
            intent.putExtras(ex);
        }
        startActivity(intent);
        if (isCloseCurrentActivity) {
            getActivity().finish();
        }
    }
}
