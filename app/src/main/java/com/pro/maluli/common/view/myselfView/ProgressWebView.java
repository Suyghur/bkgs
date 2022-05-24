package com.pro.maluli.common.view.myselfView;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressWebView extends WebView {

    private int maxHeight = -1;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // setWebViewClient(new WebViewClient(){});
    }


    public void setMaxHeight(int height) {
        maxHeight = height;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (maxHeight > -1 && getMeasuredHeight() > maxHeight) {
            setMeasuredDimension(getMeasuredWidth(), maxHeight);
        }
    }
}