package com.adcoretechnologies.golfbudz.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.ShowAlert;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends BaseActivity {


    @BindView(R.id.cvAboutus)
    CardView cvAboutus;
    @BindView(R.id.cvContacus)
    CardView cvContacus;
    @BindView(R.id.cvHelp)
    CardView cvHelp;
    @BindView(R.id.cvRate)
    CardView cvRate;
    @BindView(R.id.cvPolicy)
    CardView cvPolicy;
    @BindView(R.id.cvTc)
    CardView cvTc;
    @BindView(R.id.cvUpdate)
    CardView cvUpdate;
    @BindView(R.id.cvShare)
    CardView cvShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {

    }

    @OnClick(R.id.cvAboutus)
    public void perfromAbout() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }
    @OnClick(R.id.cvContacus)
    public void perfromContact() {
        startActivity(new Intent(this, ContactUsActivity.class));
    }
    @OnClick(R.id.cvTc)
    public void perfromtc() {
        startActivity(new Intent(this, TermsConditionActivity.class));
    }
    @OnClick(R.id.cvShare)
    public void perfrmShare() {
        Common.shareApp(this);
    }
    @OnClick(R.id.cvUpdate)
    public void perfrmUpdate() {
        //Common.openPlayStoreApp(this);
      /*  SweetAlertDialog sd = new SweetAlertDialog(this);

        sd.setCancelable(true);
        sd.setCanceledOnTouchOutside(true);
        sd.setConfirmText("Ok");
        sd.setTitleText("No update found.");
        sd.show();*/
        ShowAlert.showAlertDialog(this,"No update found.","",false);
    }
    @OnClick(R.id.cvRate)
    public void perfrmRate() {
        Common.openPlayStoreApp(this);
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
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
