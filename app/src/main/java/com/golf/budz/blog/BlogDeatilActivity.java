package com.golf.budz.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.home.R;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.RoundedImageView;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDeatilActivity extends BaseActivity {


    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvTilte)
    TextView tvTilte;
    @BindView(R.id.tvPosted)
    TextView tvPosted;
    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_deatil);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {
        Intent intent=getIntent();
       BoBlog boBlog= (BoBlog) intent.getSerializableExtra(Const.EXTRA_BLOG_ID);

        tvTilte.setText(boBlog.getShortText());
        tvDesc.setText(boBlog.getText());
        tvPosted.setText(boBlog.getCreatedAt());
        Common.showBigImage(this, ivImage, boBlog.getUserImgUrl());
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
