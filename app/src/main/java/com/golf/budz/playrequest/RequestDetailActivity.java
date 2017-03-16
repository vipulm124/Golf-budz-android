package com.golf.budz.playrequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.golf.budz.blog.BoBlog;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.home.R;
import com.golf.budz.playrequest.model.BoPlay;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestDetailActivity extends BaseActivity {


    @BindView(R.id.ivPic)
    RoundedImageView ivPic;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvVenue)
    TextView tvVenue;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvHoles)
    TextView tvHoles;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvTeaOffTime)
    TextView tvTeaOffTime;
    @BindView(R.id.tvPlayers)
    TextView tvPlayers;
    @BindView(R.id.tvHandicap)
    TextView tvHandicap;
    @BindView(R.id.tvIndustry)
    TextView tvIndustry;
    @BindView(R.id.tvProfession)
    TextView tvProfession;
    @BindView(R.id.tvGender)
    TextView tvGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        setTitle("Detail");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        init();

    }

    @Override
    public void init() {
        Intent intent = getIntent();
        BoPlay boPlay = (BoPlay) intent.getSerializableExtra(Const.EXTRA_REQ_ID);

        tvName.setText(boPlay.getUserName());
        tvDesc.setText(boPlay.getRequestInfo());
        tvDay.setText(boPlay.getDay());
        tvGender.setText(boPlay.getGender());
        tvHandicap.setText(boPlay.getHandicap());
        tvHoles.setText(boPlay.getNoOfHoles());
        tvIndustry.setText(boPlay.getIndustry());
        tvPlayers.setText(boPlay.getPlayers());
        tvProfession.setText(boPlay.getProfession());
        tvTeaOffTime.setText(boPlay.getTeeOffTime());

        tvVenue.setText(boPlay.getVenue());
        Common.showBigImage(this, ivPic, boPlay.getUserImgUrl());
    }

    @Override
    public void log(String message) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            startActivity(new Intent(this, PlayRequestActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
//        super.onBackPressed();
    }
}