package com.adcoretechnologies.golfbudz.items;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.ShowAlert;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyItemActivity extends BaseActivity {

    private BuyItemAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoItems> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        fragmentLoader = (FragmentDataLoader) getSupportFragmentManager().findFragmentById(R.id.fragmentLoader);
        if (fragmentLoader == null)
            toast("null");
        else
            init();

    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        allItems = new ArrayList<>();
        adapter = new BuyItemAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size(),"");
        getBuyItems();
    }


    @Override
    public void log(String message) {

    }
    IApiService apiService;

    public void getBuyItems() {
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoItems> call = apiService.buyItem();
        call.enqueue(new Callback<PojoItems>() {
            @Override
            public void onResponse(Call<PojoItems> call, Response<PojoItems> response) {

                hideDialog();
//                fragmentLoader.hideDataLoading();
                if (response.isSuccessful()) {
                    PojoItems pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        toast(pojo.getMessage());
                        updateViews(0,pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                        updateViews(0, pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0, "Something went wrong");
                }
            }


            @Override
            public void onFailure(Call<PojoItems> call, Throwable t) {
                updateViews(0, "");
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }
    private void updateViews(int size, String message) {
        if (size == 0) {
//            fragmentLoader.setDataEmpty("No services are available in your region for the chosen category");
            fragmentLoader.setDataEmpty(message);
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoItems> allItems) {
        adapter = new BuyItemAdapter(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size(), "");
    }
    public void onEventMainThread(BoEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {
            case BoEventData.EVENT_BUY_ITEM_CLICK: {
                buyItem(data);
                break;
            }

        }
    }
    private void buyItem(String itemId) {
        showProgressDialog("Performing operation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoItems> call = service.buyItems(userId,itemId);
        call.enqueue(new Callback<PojoItems>() {
            @Override
            public void onResponse(Call<PojoItems> call, Response<PojoItems> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoItems pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                     /*   new SweetAlertDialog(BuyItemActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Congratulations")
                                .setContentText("You have buy this item.")
                                .show();*/
                        ShowAlert.showAlertDialog(BuyItemActivity.this,"Congratulations","You successfully bought this  item.",false);
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                        toast(pojoUser.getMessage());
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                        toast(pojoUser.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<PojoItems> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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