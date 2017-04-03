package com.golf.budz.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.golf.budz.auth.PojoUser;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.base.BoEventData;
import com.golf.budz.core.components.FragmentDataLoader;
import com.golf.budz.friends.PojoFriend;
import com.golf.budz.home.JSONResponse;
import com.golf.budz.home.NewsFeedAdapter;
import com.golf.budz.home.R;
import com.golf.budz.home.RequestInterface;
import com.golf.budz.home.comment.CommentsActivity;
import com.golf.budz.home.model.BoPost;
import com.golf.budz.home.model.BoService;
import com.golf.budz.home.model.PojoService;
import com.golf.budz.model.Notification;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends BaseActivity {

    private NotificationAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoNoti> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager manager;
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
         manager = new LinearLayoutManager(this);
        allItems = new ArrayList<>();
        allItems.clear();
        adapter = new NotificationAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        getAllNotification();
    }

    @Override
    public void log(String message) {

    }

    IApiService apiService;

    public void getAllNotification() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoNoti> call = apiService.getNotifications(userId);
        call.enqueue(new Callback<PojoNoti>() {
            @Override
            public void onResponse(Call<PojoNoti> call, Response<PojoNoti> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoNoti pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        bindData(pojo.getAllItems());
                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
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
            public void onFailure(Call<PojoNoti> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No request found");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoNoti> allItems) {
        adapter = new NotificationAdapter(allItems);
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
            case BoEventData.EVENT_NOTI_ACCEPT_CLICK: {
                BoNoti noti = (BoNoti) object;
                performAction(noti,Const.ACCEPT,id);
                break;
            }
            case BoEventData.EVENT_NOTI_CANCEL_CLICK: {
                BoNoti noti = (BoNoti) object;
                performAction(noti,Const.CANCEL,id);
                break;
            }
        }
    }

  /*  private void performAcceptance(BoNoti noti, final int position) {
        showProgressDialog("Performing your request", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoNoti> call = service.acceptByuserid(userId, noti.getUserId(),noti.get_id());
        call.enqueue(new Callback<PojoNoti>() {
            @Override
            public void onResponse(Call<PojoNoti> call, Response<PojoNoti> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoNoti pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        adapter.notifyItemRemoved(position);
                        toast(pojo.getMessage());
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
            public void onFailure(Call<PojoNoti> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }*/
    private void performAction(BoNoti noti, final String status, final int position) {
        showProgressDialog("Performing operation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoFriend> call = service.friendStatus(status,noti.getFriendId(),userId,noti.get_id());
        call.enqueue(new Callback<PojoFriend>() {
            @Override
            public void onResponse(Call<PojoFriend> call, Response<PojoFriend> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoFriend pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        adapter.notifyItemRemoved(position);

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
   /* private void performCancellation(BoNoti noti, final int position) {
        showProgressDialog("Performing your request", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoNoti> call = service.cancelByuserid(userId, noti.getUserId(),noti.get_id());
        call.enqueue(new Callback<PojoNoti>() {
            @Override
            public void onResponse(Call<PojoNoti> call, Response<PojoNoti> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoNoti pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojo.getMessage());
                        adapter.notifyItemRemoved(position);
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
            public void onFailure(Call<PojoNoti> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }*/

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