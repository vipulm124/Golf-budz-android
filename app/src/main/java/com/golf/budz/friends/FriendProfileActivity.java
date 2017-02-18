package com.golf.budz.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.golf.budz.auth.PojoUser;
import com.golf.budz.auth.profile.ChnagePasswordActivity;
import com.golf.budz.auth.profile.EditProfileActivity;
import com.golf.budz.auth.profile.ProfileActivity;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.home.MainActivity;
import com.golf.budz.home.R;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.RoundedImageView;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adcore on 2/7/2017.
 */

public class FriendProfileActivity extends BaseActivity {



    @BindView(R.id.ivProfile)
    RoundedImageView ivProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLName)
    TextView tvLName;
    @BindView(R.id.namelayout)
    LinearLayout namelayout;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.mobilelayout)
    LinearLayout mobilelayout;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.emaillayout)
    LinearLayout emaillayout;
    @BindView(R.id.tvHandcap)
    TextView tvHandcap;
    @BindView(R.id.handicaplayout)
    LinearLayout handicaplayout;
    @BindView(R.id.tvStrength)
    TextView tvStrength;
    @BindView(R.id.strengthlayout)
    LinearLayout strengthlayout;
    @BindView(R.id.tvWeakness)
    TextView tvWeakness;
    @BindView(R.id.weaklayout)
    LinearLayout weaklayout;
    @BindView(R.id.tvRounds)
    TextView tvRounds;
    @BindView(R.id.requestperlayout)
    LinearLayout requestperlayout;
    @BindView(R.id.tvGolfbag)
    TextView tvGolfbag;
    @BindView(R.id.tvGamesPlayes)
    TextView tvGamesPlayes;
    @BindView(R.id.golfbaglayout)
    LinearLayout golfbaglayout;
    @BindView(R.id.gamesplayedlayout)
    LinearLayout gamesplayedlayout;
    @BindView(R.id.profilelayout)
    LinearLayout profilelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public void init() {
        getProfile();
    }

    private void getProfile() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        showProgressDialog(getResources().getString(R.string.retrieving), "Please wait...");
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.getProfile(userId);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        binddata(pojoUser);
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

    private void binddata(PojoUser pojoUser) {
        if (pojoUser.getAllItems().size() > 0) {
            if (pojoUser.getAllItems().get(0).getFirstName().equals("")) {
                namelayout.setVisibility(View.GONE);
            } else {
                tvName.setText(pojoUser.getAllItems().get(0).getFirstName());
            }
            if (pojoUser.getAllItems().get(0).getLastName().equals("")) {
                namelayout.setVisibility(View.GONE);
            } else {
                tvLName.setText(pojoUser.getAllItems().get(0).getLastName());
            }
            if (pojoUser.getAllItems().get(0).getEmail().equals("")) {
                emaillayout.setVisibility(View.GONE);
            } else {
                tvEmail.setText(pojoUser.getAllItems().get(0).getEmail());
            }

            if (pojoUser.getAllItems().get(0).getHandicap().equals("")) {
                handicaplayout.setVisibility(View.GONE);
            } else {
                tvHandcap.setText(pojoUser.getAllItems().get(0).getHandicap());
            }
            if (pojoUser.getAllItems().get(0).getStrength().equals("")) {
                strengthlayout.setVisibility(View.GONE);
            } else {
                tvStrength.setText(pojoUser.getAllItems().get(0).getStrength());
            }
            if (pojoUser.getAllItems().get(0).getWeakness().equals("")) {
                weaklayout.setVisibility(View.GONE);
            } else {
                tvWeakness.setText(pojoUser.getAllItems().get(0).getWeakness());
            }
            if (pojoUser.getAllItems().get(0).getContact().equals("")) {
                mobilelayout.setVisibility(View.GONE);
            } else {
                tvMobile.setText(pojoUser.getAllItems().get(0).getContact());
            }
           /* if(pojoUser.getAllItems().get(0).getGolfbag().equals("")) {
                golfbaglayout.setVisibility(View.GONE);
            }
            else{
                tvGolfbag.setText(pojoUser.getAllItems().get(0).getContact());}

            if(pojoUser.getAllItems().get(0).getRounds().equals("")) {
                requestperlayout.setVisibility(View.GONE);
            }
            else{
                tvRounds.setText(pojoUser.getAllItems().get(0).getContact());}*/
            Common.showRoundImage(getApplicationContext(), ivProfile, pojoUser.getAllItems().get(0).getProfileImage());

        }
    }

    @Override
    public void log(String message) {

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, MyFriendActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
//        super.onBackPressed();
    }
}
