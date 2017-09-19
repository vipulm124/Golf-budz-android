package com.adcoretechnologies.golfbudz.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 9/14/2017.
 */

public class VideoPlayActivity extends BaseActivity {
    @BindView(R.id.vvLink)
    VideoView vvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        // getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        init();

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if(intent!=null) {
            String link = intent.getStringExtra("link");
            MediaController mc = new MediaController(this);
            mc.setAnchorView(vvLink);
            mc.setMediaPlayer(vvLink);
            Uri video = Uri.parse(link);
            vvLink.setMediaController(mc);
            vvLink.setVideoURI(video);
            vvLink.start();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);



        }
    }
    @Override
    public void log(String message) {
        super.log(getClass().getSimpleName(), message);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

