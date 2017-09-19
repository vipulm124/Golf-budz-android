package com.adcoretechnologies.golfbudz.playrequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.RoundedImageView;
import com.adcoretechnologies.golfbudz.utils.ShowAlert;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adcore on 5/29/2017.
 */

public class UpComingGanesDetailActivity extends BaseActivity {


    @BindView(R.id.ivPic)
    ImageView ivPic;
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
    @BindView(R.id.btnJoin)
    Button btnJoin;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    int reuestId;
    String userStatus;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvlocation)
    TextView tvlocation;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvDate)
    TextView tvDate;

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
        btnCancel.setVisibility(View.GONE);
        btnJoin.setVisibility(View.GONE);
        Intent intent = getIntent();
        BoPlay boPlay = (BoPlay) intent.getSerializableExtra(Const.EXTRA_REQ_ID);
        reuestId = boPlay.getId();
        userStatus = boPlay.getUserStatus();
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
        tvAge.setText(boPlay.getAge());
        tvDate.setText(boPlay.getDateCreated());
        tvlocation.setText(boPlay.getLocations());

        tvVenue.setText(boPlay.getVenue());
        Common.showBigImage(this, ivPic, boPlay.getUserImgUrl());
        if (userStatus != null) {
            if (userStatus.equalsIgnoreCase("cancel")) {
                btnCancel.setVisibility(View.GONE);
            } else if (userStatus.equalsIgnoreCase("join")) {
                btnJoin.setVisibility(View.GONE);
            }
        } else {
        }
    }

    @Override
    public void log(String message) {

    }



    public void cancelReq() {
        showProgressDialog("Sending your request", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService apiService = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = apiService.cancelPlayReq(reuestId, userId, "cancel");
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojo.getMessage());
                        startActivity(new Intent(UpComingGanesDetailActivity.this, PlayRequestActivity.class));
                        finish();
                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {

                    toast("Something went wrong");

                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
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