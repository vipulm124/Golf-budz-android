package com.golf.budz.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.golf.budz.auth.login.LoginActivity;
import com.golf.budz.auth.profile.ProfileActivity;
import com.golf.budz.blog.BLogActivity;
import com.golf.budz.chat.MyChatActivity;
import com.golf.budz.club.MyClubActivity;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.base.BoEventData;
import com.golf.budz.core.components.FragmentDataLoader;
import com.golf.budz.event.EventActivity;
import com.golf.budz.event.PojoEvent;
import com.golf.budz.friends.MyFriendActivity;
import com.golf.budz.group.MyGroupActivity;
import com.golf.budz.home.comment.AdapterComment;
import com.golf.budz.home.comment.CommentsActivity;
import com.golf.budz.home.model.BoPost;
import com.golf.budz.home.model.PojoPost;
import com.golf.budz.items.BuyItemActivity;
import com.golf.budz.items.SellItemActivity;
import com.golf.budz.more.MoreActivity;
import com.golf.budz.notification.NotificationActivity;
import com.golf.budz.playrequest.PlayRequestActivity;
import com.golf.budz.group.CreatePageActivity;
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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String url;


    private NewsFeedAdapter adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoPost> allItems = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llPost)
    LinearLayout llPost;
    @BindView(R.id.llVideo)
    LinearLayout llVideo;
    @BindView(R.id.llGallery)
    LinearLayout llGallery;
    @BindView(R.id.btnPost)
    Button btnPost;
    @BindView(R.id.etPost)
    EditText etPost;
    /* @BindView(R.id.tvName)*/
    TextView tvName;
    ImageView ivPic;
    Menu menu;
    BottomSheetBehavior behavior;
    private BottomSheetDialog mBottomSheetDialog;
    private AdapterComment mAdapter;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        tvName = (TextView) header.findViewById(R.id.tvName);
        ivPic = (ImageView) header.findViewById(R.id.ivPic);
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
        adapter = new NewsFeedAdapter(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        updateViews(allItems.size());
        fillData();
        setupUserProfile();


    }
    //////////////Bottom Sheet for PojoComment//////////////////////


    ////////////////////////////////////
    private void setupUserProfile() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        String userImage = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        String userType = Pref.Read(this, Const.PREF_USER_TYPE);
        if (userName != null && (!userName.isEmpty() && (!userId.equals("")))) {
            tvName.setText(userName);
            Common.showRoundImage(getApplicationContext(), ivPic, userImage);
            //menu.getItem(R.id.nav_login).setVisible(false);
            MenuItem target = menu.findItem(R.id.nav_logout);
            target.setVisible(true);
            if (userType.equals("Club")) {
                MenuItem sellMneu = menu.findItem(R.id.nav_sellitems);
                sellMneu.setVisible(true);
            }
        } else {
            MenuItem target = menu.findItem(R.id.nav_login);
            target.setVisible(true);
        }

    }

    @Override
    public void log(String message) {

    }

    IApiService apiService;

    public void fillData() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoPost> call = apiService.getAllPostByUserId(userId);
        call.enqueue(new Callback<PojoPost>() {
            @Override
            public void onResponse(Call<PojoPost> call, Response<PojoPost> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPost pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                        // toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPost> call, Throwable t) {
                updateViews(0);
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No post are available");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoPost> allItems) {
        this.allItems = allItems;
        adapter = new NewsFeedAdapter(allItems);
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

            case BoEventData.EVENT_NEWS_LIKE_CLICK: {
                BoPost post = (BoPost) object;
                if (data.equals("false")) {
                    int increaseCount = Integer.parseInt(post.getLikeCount()) + 1;
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
                    int decreaseCount = Integer.parseInt(post.getLikeCount()) - 1;
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

                performLike(id, post.get_id(), post.getLikeCount(), data);
                break;
            }
            case BoEventData.EVENT_NEWS_SHARE_CLICK: {
                BoPost post = (BoPost) object;
                shareApp(post);
                break;
            }
            case BoEventData.EVENT_NEWS_COMMENT_CLICK: {
                startActivity(new Intent(this, CommentsActivity.class).putExtra(Const.EXTRA_POST_ID, data));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            }
        }
    }

    private void shareApp(BoPost post) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = post.getText();
            sAux = sAux + post.getImage();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) { //e.toString();
//            Common.logException(getApplicationContext(), getResources().getString(R.string.error_share_app), e, null);
        }
    }

    private void performLike(final int position, String newsId, final String likeCount, final String data) {

        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoEvent> call = service.postLike(userName, userId, newsId);
        call.enqueue(new Callback<PojoEvent>() {
            @Override
            public void onResponse(Call<PojoEvent> call, Response<PojoEvent> response) {

                if (response.isSuccessful()) {
                    PojoEvent pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.llGallery)
    public void onPSlectGallery() {
        startActivity(new Intent(this, ImageDataPostActivity.class));
    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        String postText = etPost.getText().toString();
        if (TextUtils.isEmpty(postText)) {
            etPost.setError("Please enter");
            return;
        }
        etPost.setText("");
        addPost(postText);
    }

    @OnClick(R.id.llVideo)
    public void onPSlectVideo() {
        startActivity(new Intent(this, VideoDatapostActivity.class));
    }

    private void addPost(final String text) {
        showProgressDialog("Performing creation", "Please wait...");
        final String userId = Pref.Read(this, Const.PREF_USER_ID);
        final String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        final String userImage = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoPost> call = service.addPost(userName, userId, text, "", "", Const.TEXT, "0", "0", userImage);
        call.enqueue(new Callback<PojoPost>() {
            @Override
            public void onResponse(Call<PojoPost> call, Response<PojoPost> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPost pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        BoPost post = new BoPost();
                        post.setLikeCount("0");
                        post.setCommentCount("0");
                        post.setPostType(Const.TEXT);
                        post.setUserImgUrl(userImage);
                        post.setText(text);
                        post.setUserName(userName);
                        post.setUserId(userId);
                        post.setImage("");
                        post.setVideo("");
                        post.setLikes(new ArrayList<String>());
                        allItems.add(0, post);
                        adapter = new NewsFeedAdapter(allItems);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
            public void onFailure(Call<PojoPost> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            startActivity(new Intent(this, PlayRequestActivity.class).putExtra(Const.EXTRA_FRAGMENT_DISPLAY_COUNT, "1"));
            return true;
        }
        if (id == R.id.action_create) {
            startActivity(new Intent(this, PlayRequestActivity.class).putExtra(Const.EXTRA_FRAGMENT_DISPLAY_COUNT, "0"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
             startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_event) {
            startActivity(new Intent(MainActivity.this, EventActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        } else if (id == R.id.nav_notification) {

            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        } else if (id == R.id.nav_buyitems) {
            startActivity(new Intent(MainActivity.this, BuyItemActivity.class));
        } else if (id == R.id.nav_logout) {
            performLogout(item);

        } else if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_more) {
            startActivity(new Intent(MainActivity.this, MoreActivity.class));
        } else if (id == R.id.nav_friend) {
            startActivity(new Intent(MainActivity.this, MyFriendActivity.class));
        } else if (id == R.id.nav_club) {
            startActivity(new Intent(MainActivity.this, MyClubActivity.class));
        } else if (id == R.id.nav_round) {
            startActivity(new Intent(this, PlayRequestActivity.class).putExtra(Const.EXTRA_FRAGMENT_DISPLAY_COUNT, "0"));
        } else if (id == R.id.nav_createpage) {
            startActivity(new Intent(MainActivity.this, CreatePageActivity.class));
        } else if (id == R.id.nav_sellitems) {
            startActivity(new Intent(MainActivity.this, SellItemActivity.class));
        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(MainActivity.this, MyChatActivity.class));

        } else if (id == R.id.nav_mygroup) {
            startActivity(new Intent(MainActivity.this, MyGroupActivity.class));

        } else if (id == R.id.nav_share) {
            Common.shareApp(this);

        } else if (id == R.id.nav_oncourse) {
            startActivity(new Intent(MainActivity.this, BLogActivity.class));

        } else if (id == R.id.nav_daledgolfchat) {
            startActivity(new Intent(MainActivity.this, BLogActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout(MenuItem item) {

        Pref.clearOtherPref(this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}
