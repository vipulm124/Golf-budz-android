package com.adcoretechnologies.golfbudz.home.comment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends BaseActivity {

    private AdapterComment adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoCommnet> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    String postid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        allItems = new ArrayList<>();
        adapter = new AdapterComment(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        Intent intent=getIntent();
        if(intent!=null){
         postid=intent.getStringExtra(Const.EXTRA_POST_ID);
          getAllComments(postid);
        }
    }

    @Override
    public void log(String message) {

    }
    IApiService apiService;

    public void getAllComments(String postId) {
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        fragmentLoader.setDataLoading("Please wait...");
        Call<PojoComment> call = apiService.getCommentsByPostId(postId);
        call.enqueue(new Callback<PojoComment>() {
            @Override
            public void onResponse(Call<PojoComment> call, Response<PojoComment> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoComment pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
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
            public void onFailure(Call<PojoComment> call, Throwable t) {
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

    private void bindData(ArrayList<BoCommnet> allItems) {
        this.allItems = allItems;
        adapter = new AdapterComment(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
    }
         @OnClick(R.id.ivSend)
         public void onSend(){
             String messageText = etComment.getText().toString();
             if (TextUtils.isEmpty(messageText)) {
                 etComment.setError("Input Message");
                 return;
             }

             onMsgSend(messageText);
             etComment.setText("");
         }

    private void onMsgSend(final String messageText) {
        showProgressDialog("Sending Message", "Please wait...");
        final String userId = Pref.Read(this, Const.PREF_USER_ID);
        final String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        final String userImage = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.commentByUserId(userId,userName, postid,messageText,userImage);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        BoCommnet commnet = new BoCommnet();
                        commnet.setUserImgUrl(userImage);
                        commnet.setComment(messageText);
                        commnet.setUserName(userName);
                        commnet.setUserId(userId);
                        commnet.setPostId(postid);

                       allItems.add(commnet);
                        bindData(allItems);

                        toast(pojoUser.getMessage());

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
            public void onFailure(Call<PojoUser> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CommentsActivity.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

}