package com.adcoretechnologies.golfbudz.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends BaseActivity {

    private EventListAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoEvent> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    IApiService apiService;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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
        adapter = new EventListAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        getAllEvents();
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {

        Intent i = new Intent(getApplicationContext(), CreateEventActivity.class);
        startActivity(i);
    }

    @Override
    public void log(String message) {

    }


    public void getAllEvents() {

        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoEvent> call = apiService.getEvents();
        call.enqueue(new Callback<PojoEvent>() {
            @Override
            public void onResponse(Call<PojoEvent> call, Response<PojoEvent> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoEvent pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        // toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoEvent> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No events found");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoEvent> allItems) {
        adapter = new EventListAdapter(allItems);
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
            case BoEventData.EVENT_EVENT_SERVICE_CLICK: {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra(Const.EXTRA_EVENT_ID, data);
                startActivity(intent);
                break;
            }
            case BoEventData.EVENT_EVENT_LIKE_CLICK: {
                BoEvent event = (BoEvent) object;
                if (data.equals("false")) {
                    int increaseCount = Integer.parseInt(event.getLikeCount()) + 1;
                    adapter.getItem(id).setLikeCount(String.valueOf(increaseCount));
                    adapter.getItem(id).setLikeStatus(true);

                    View view = manager.getChildAt(id);

                    ImageView imageViewLiked = (ImageView) view.findViewById(R.id.ivLiked);
                    imageViewLiked.setVisibility(View.VISIBLE);
                    ImageView imageViewLike = (ImageView) view.findViewById(R.id.ivLike);
                    imageViewLike.setVisibility(View.GONE);
                    TextView tvLike=(TextView) view.findViewById(R.id.tvLike);
                    tvLike.setText(increaseCount+" Likes");
                } else {
                    int decreaseCount = Integer.parseInt(event.getLikeCount()) - 1;
                    adapter.getItem(id).setLikeCount(String.valueOf(decreaseCount));
                    adapter.getItem(id).setLikeStatus(false);
                    View view = manager.getChildAt(id);
                    ImageView imageViewLiked = (ImageView) view.findViewById(R.id.ivLiked);
                    imageViewLiked.setVisibility(View.GONE);
                    ImageView imageViewLike = (ImageView) view.findViewById(R.id.ivLike);
                    imageViewLike.setVisibility(View.VISIBLE);
                    TextView tvLike=(TextView) view.findViewById(R.id.tvLike);
                    tvLike.setText(decreaseCount+" Likes");

                }
                performLike(id, event);
                break;
            }
            case BoEventData.EVENT_EVENT_SHARE_CLICK: {
                BoEvent event = (BoEvent) object;
                shareApp(event);
                break;
            }
            case BoEventData.EVENT_EVENT_ATTENDING_CLICK: {
                BoEvent event = (BoEvent) object;
                if (data.equals("false")) {

                    Log.e("status event",data.toString() + "   " + manager.getChildAt(id));
                    View view = manager.getChildAt(id);
                    ImageView imageViewLiked = (ImageView) view.findViewById(R.id.ivAttended);
                    imageViewLiked.setVisibility(View.VISIBLE);
                    ImageView imageViewLike = (ImageView) view.findViewById(R.id.ivAttend);
                    imageViewLike.setVisibility(View.GONE);

                } else {
                    View view = manager.getChildAt(id);
                    ImageView imageViewLiked = (ImageView) view.findViewById(R.id.ivAttended);
                    imageViewLiked.setVisibility(View.GONE);
                    ImageView imageViewLike = (ImageView) view.findViewById(R.id.ivAttend);
                    imageViewLike.setVisibility(View.VISIBLE);
                }
                performAttending(id, event);
                break;
            }
        }
    }

    private void performAttending(int id, BoEvent event) {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoEvent> call = service.attendByuserid(userName, userId, event.get_id());
        call.enqueue(new Callback<PojoEvent>() {
            @Override
            public void onResponse(Call<PojoEvent> call, Response<PojoEvent> response) {

                if (response.isSuccessful()) {
                    PojoEvent pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {

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

                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void shareApp(BoEvent event) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = event.getTitle();
            sAux = sAux + event.getImage();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) { //e.toString();
//            Common.logException(getApplicationContext(), getResources().getString(R.string.error_share_app), e, null);
        }
    }

    private void performLike(final int position, final BoEvent event) {

        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoEvent> call = service.likeEvent(userName, userId, event.get_id());
        call.enqueue(new Callback<PojoEvent>() {
            @Override
            public void onResponse(Call<PojoEvent> call, Response<PojoEvent> response) {

                if (response.isSuccessful()) {
                    PojoEvent pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                      /*  int increaseCount = Integer.parseInt(event.getLikeCount()) + 1;
                        adapter.getItem(position).setLikeCount(String.valueOf(increaseCount));
                        adapter.getItem(position).setLikeStatus(true);
                        adapter.notifyDataSetChanged();
                        View view = manager.getChildAt(position);
                        ImageView imageViewLiked = (ImageView) view.findViewById(R.id.ivLiked);
                        imageViewLiked.setVisibility(View.VISIBLE);
                        ImageView imageViewLike = (ImageView) view.findViewById(R.id.ivLike);
                        imageViewLike.setVisibility(View.GONE);*/

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