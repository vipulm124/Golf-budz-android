package com.golf.budz.club;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.components.FragmentDataLoader;
import com.golf.budz.home.R;
import com.golf.budz.home.model.BoService;
import com.golf.budz.home.model.PojoService;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinClubActivity extends BaseActivity {

    private JoinClubAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoService> allItems;
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
        adapter = new JoinClubAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        fillData("69076bdb-7c37-11e6-8e1d-3e418cfa425b", 0);
    }

    @Override
    public void log(String message) {

    }

    IApiService apiService;

    public void fillData(String categoryId, final int position) {
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoService> call = apiService.getServicesByCategoryId(categoryId);
        call.enqueue(new Callback<PojoService>() {
            @Override
            public void onResponse(Call<PojoService> call, Response<PojoService> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoService pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoService> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No services are available in your region for the chosen category");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoService> allItems) {
        adapter = new JoinClubAdapter(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
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