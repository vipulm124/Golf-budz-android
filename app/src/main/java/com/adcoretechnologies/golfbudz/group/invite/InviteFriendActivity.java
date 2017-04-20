package com.adcoretechnologies.golfbudz.group.invite;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.friends.BoFriend;
import com.adcoretechnologies.golfbudz.friends.PojoFriend;
import com.adcoretechnologies.golfbudz.group.CreatePageActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriendActivity extends BaseActivity {

    private AdapterInviteFriend adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoFriend> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnDone)
    Button btnDone;
    String friendIds,description="",name="";
    List<String> idList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        ButterKnife.bind(this);
        fragmentLoader = (FragmentDataLoader) getSupportFragmentManager().findFragmentById(R.id.fragmentLoader);
        if (fragmentLoader == null)
            toast("null");
        else
            init();

    }

    @Override
    public void init() {
        Intent intent=getIntent();
        if (intent!=null){
            name=intent.getStringExtra("name");
            description=intent.getStringExtra("description");}

        LinearLayoutManager manager = new LinearLayoutManager(this);
        allItems = new ArrayList<>();
        adapter = new AdapterInviteFriend(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        getAllFriends();

    }

    @Override
    public void log(String message) {

    }
    IApiService apiService;

    public void getAllFriends() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
            fragmentLoader.setDataLoading("Please wait...");
        Call<PojoFriend> call = apiService.getMyFriends(userId);
        call.enqueue(new Callback<PojoFriend>() {
            @Override
            public void onResponse(Call<PojoFriend> call, Response<PojoFriend> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoFriend pojo = response.body();
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
            public void onFailure(Call<PojoFriend> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }
    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("There are no comments");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoFriend> allItems) {
        adapter = new AdapterInviteFriend(allItems);
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
            case BoEventData.EVENT_FRIEND_ITEM_CLICK: {
                idList.add(data);
                adapter.getItem(id).setUserStatus(true);
                adapter.notifyDataSetChanged();
                break;
            }

        }
    }
    @OnClick(R.id.btnDone)
    public void onDone(){
        getFiriendIds();
        startActivity(new Intent(this, CreatePageActivity.class).putExtra(Const.EXTRA_FRIEND_ID,friendIds).putExtra("name",name).putExtra("description",description));
    }
    private void getFiriendIds() {
        try {
            for (int i = 0; i < idList.size(); i++) {
                friendIds = idList.get(i) + "|";
            }
        } catch (Exception e) {
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}