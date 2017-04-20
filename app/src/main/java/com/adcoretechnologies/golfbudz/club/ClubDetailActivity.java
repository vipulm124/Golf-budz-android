package com.adcoretechnologies.golfbudz.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClubDetailActivity extends BaseActivity {
    @BindView(R.id.slider)
    SliderLayout slider;
    @BindView(R.id.sliderlayout)
    RelativeLayout sliderlayout;
    @BindView(R.id.clubhead)
    TextView clubhead;
    @BindView(R.id.tvClubName)
    TextView tvClubName;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvOperatingHorus)
    TextView tvOperatingHorus;
    @BindView(R.id.tvContactNo)
    TextView tvContactNo;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    private SliderLayout mDemoSlider;
    int[] imageIds = new int[]{
            R.drawable.ic_email_white_24px, R.drawable.logo_old}; // This is your array with resource id of each image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        BoUser boUser = (BoUser) intent.getSerializableExtra(Const.EXTRA_CLUB_DETAIL);
        tvAddress.setText(boUser.getAddress());
        tvDescription.setText(boUser.getDescription());
        tvClubName.setText(boUser.getClubName());
        tvContactNo.setText(boUser.getContact());
        tvOperatingHorus.setText(boUser.getOperatingHours());

        setBannerSlider();
    }

    private void setBannerSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        for (int k = 0; k < imageIds.length; k++) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(imageIds[k])
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

    }

    @Override
    public void log(String message) {

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
