package com.golf.budz.friends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.golf.budz.chat.ChatDashboradActivity;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.base.BaseFragment;
import com.golf.budz.core.base.BoEventData;
import com.golf.budz.core.components.FragmentDataLoader;
import com.golf.budz.home.R;
import com.golf.budz.playrequest.adapter.AdapterJoinPlayRequest;
import com.golf.budz.playrequest.model.BoPlay;
import com.golf.budz.playrequest.model.PojoPlay;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAllPlayReqActivity extends BaseActivity {

    private MyAllPlayReqAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoPlay> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_play_req);
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
        adapter = new MyAllPlayReqAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        fillData();

    }
    IApiService apiService;
    public void fillData() {
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = apiService.getAllPlayReq();
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }
    private void sendPlayReq(String friendId) {
        showProgressDialog("Performing operation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoFriend> call = service.sendPlayReqByuserId(userId,friendId);
        call.enqueue(new Callback<PojoFriend>() {
            @Override
            public void onResponse(Call<PojoFriend> call, Response<PojoFriend> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoFriend pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                       finish();
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
            public void onFailure(Call<PojoFriend> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }
    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No request created. Please create request first to send.");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoPlay> allItems) {
        adapter = new MyAllPlayReqAdapter(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
    }

    public void onEventMainThread(BoEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {

            case BoEventData.EVENT_PLAY_REQ_SEND_CLICK: {
                sendPlayReq(data);
                break;
            }
        }
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
    public void log(String message) {

    }
}
