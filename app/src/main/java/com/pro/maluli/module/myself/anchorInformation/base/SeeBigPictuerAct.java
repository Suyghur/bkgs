package com.pro.maluli.module.myself.anchorInformation.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.photoview.PhotoViewAttacher;
import com.pro.maluli.R;
import com.pro.maluli.common.utils.glideImg.GlideUtils;

public class SeeBigPictuerAct extends AppCompatActivity {
    private PhotoView image;
    private String imgUrl;
    private ImageView finishIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_big_pictuer);

        imgUrl = getIntent().getStringExtra("imgUrl");
        image = (PhotoView) findViewById(R.id.image);
        finishIv = findViewById(R.id.finishIv);
        new PhotoViewAttacher(image);
        GlideUtils.loadImageNoImage(this, imgUrl, image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finishIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}