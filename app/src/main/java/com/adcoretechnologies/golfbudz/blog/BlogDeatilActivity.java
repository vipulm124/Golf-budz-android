package com.adcoretechnologies.golfbudz.blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;

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
       BoBlogs boBlog= (BoBlogs) intent.getSerializableExtra(Const.EXTRA_BLOG_ID);

        tvTilte.setText(boBlog.getUserName());
        tvDesc.setText(boBlog.getText());
        tvPosted.setText(boBlog.getUpdatedAt());
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
