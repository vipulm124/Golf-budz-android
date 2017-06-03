package com.adcoretechnologies.golfbudz.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adcore on 2/1/2017.
 */

public class EventDetailActivity extends BaseActivity {
    @BindView(R.id.slider)
    SliderLayout mDemoSlider;
    @BindView(R.id.custom_indicator)
    PagerIndicator customIndicator;
    @BindView(R.id.sliderlayout)
    RelativeLayout sliderlayout;
    @BindView(R.id.tvTilte)
    TextView tvTilte;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.activity_buy_item_detail)
    RelativeLayout activityBuyItemDetail;

List<String> items= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Const.EXTRA_EVENT_ID)) {
            String eventId = intent.getExtras().getString(Const.EXTRA_EVENT_ID);
            getDetail(eventId);
        }
    }

    private void getDetail(String eventId) {
        showProgressDialog("Fetching data", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
       // toast(userName + userId);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoEvent> call = service.getEventDetail(eventId);
        call.enqueue(new Callback<PojoEvent>() {
            @Override
            public void onResponse(Call<PojoEvent> call, Response<PojoEvent> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoEvent pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojo.getMessage());
                        bindData(pojo.getAllItems().get(0));

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
            public void onFailure(Call<PojoEvent> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }

    private void bindData(BoEvent item) {
        if (item != null) {
            tvTilte.setText(item.getTitle());
            tvDescription.setText(item.getDescription());
            String[] parts = item.getImage().split("\\|");
        for (int i = 0; i < parts.length; i++) {

                items.add(parts[i]);


        }
            if (mDemoSlider != null)
                addSliderToSlideShow(items);
        }
    }

    @Override
    public void log(String message) {

    }

    private void addSliderToSlideShow(List<String> items) {


        for (String name : items) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(name)
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

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