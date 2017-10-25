package com.adcoretechnologies.golfbudz.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.profile.ProfileActivity;
import com.adcoretechnologies.golfbudz.chat.ChatDashboradActivity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.playrequest.PlayRequestActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFriendActivity extends BaseActivity {

    private MyFriendAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoFriend> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
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
        adapter = new MyFriendAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        getMyFriends();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void log(String message) {

    }
    IApiService apiService;

    public void getMyFriends() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
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
                        updateViews(0);
                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
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
    public void onSearch(String text) {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        Log.e("id",userId  + " "+text);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoFriend> call = apiService.getSearchFriends(userId,text);
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
            fragmentLoader.setDataEmpty("No Friends Found");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoFriend> allItems) {
        adapter = new MyFriendAdapter(allItems);
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
            case BoEventData.EVENT_EVENT_FPROFILE_CLICK: {
              startActivity(new Intent(this, ProfileActivity.class).putExtra(Const.EXTRA_USER_ID,data));
                finish();
                break;
            }
            case BoEventData.EVENT_EVENT_FUNFRIEND_CLICK: {
                BoFriend  user= (BoFriend) object;
                if(user.getStatus()==null){
                    addFriend(user.getUserId(),id);
                }else{
                performAction(user.getUserId(),user.getStatus(),id);}
                break;
            }
            case BoEventData.EVENT_EVENT_FMESSAGECLICK: {
                BoFriend  friend= (BoFriend) object;
//                Log.e("refid",friend.getRefId());
                if (friend.getRefId().equals("1")) {
                    Intent intent = new Intent(this, ChatDashboradActivity.class);
                    intent.putExtra(Const.EXTRA_CHAT_WITH, friend.getFirstName());
                    intent.putExtra(Const.EXTRA_CHANNEL_ID, friend.getChanelId());
                    intent.putExtra(Const.EXTRA_IMAGE_URL, friend.getProfileImage());
                    intent.putExtra(Const.EXTRA_CHATWITH_ID, friend.getUserId());
                    startActivity(intent);
                }else
                {
                    Toast.makeText(this,"Send friend request first",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case BoEventData.EVENT_EVENT_FSENDREQ_CLICK: {
                startActivity(new Intent(this, PlayRequestActivity.class).putExtra(Const.EXTRA_FRAGMENT_DISPLAY_COUNT, "0"));
                //sendPlayReq(data);
                break;
            }
        }
    }


    private void performAction(String friendId, final String status, final int position) {
        showProgressDialog("Performing operation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Log.e("status",status+friendId + "   "+userId+"");
        Call<PojoFriend> call = service.friendStatus(status,friendId,userId,"");
        call.enqueue(new Callback<PojoFriend>() {
            @Override
            public void onResponse(Call<PojoFriend> call, Response<PojoFriend> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoFriend pojoUser = response.body();
                    Log.e("status",pojoUser.getStatus()+"");
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojoUser.getMessage());
                        Log.e("status sucess",pojoUser.getStatus()+"");
                        adapter.getItem(position).setStatus(status);
                        adapter.notifyDataSetChanged();
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

    private void addFriend(String friendId,final int position) {
        showProgressDialog("Performing operation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoFriend> call = service.addFriend(friendId,userId);
        call.enqueue(new Callback<PojoFriend>() {
            @Override
            public void onResponse(Call<PojoFriend> call, Response<PojoFriend> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoFriend pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                       // toast(pojoUser.getMessage());
                        adapter.getItem(position).setStatus(Const.SENT);
                        adapter.notifyDataSetChanged();
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
