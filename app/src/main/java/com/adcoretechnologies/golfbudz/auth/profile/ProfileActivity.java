package com.adcoretechnologies.golfbudz.auth.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.auth.Register.BoCity;
import com.adcoretechnologies.golfbudz.auth.Register.PojoCity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.ComponentItemSelector;
import com.adcoretechnologies.golfbudz.core.components.ComponentLocationItemSelector;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.playrequest.model.BoDropVales;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoDropValues;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.RoundedImageView;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.etFrstnme)
    EditText etFrstnme;
    @BindView(R.id.etLstmne)
    EditText etLstmne;
    @BindView(R.id.etEmail)
    EditText etEmail;


    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.llClubDeatl)
    LinearLayout llClubDeatl;

    boolean checked = false;
    List<String> locationList = new ArrayList<String>();
    List<String> roleitems = new ArrayList<String>();
    ComponentLocationItemSelector citySelector;
    ComponentItemSelector roleSelector;
    @BindView(R.id.etClubName)
    EditText etClubName;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etSubrub)
    EditText etSubrub;
    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.etOperatingHours)
    EditText etOperatingHours;
    @BindView(R.id.etContactNo)
    EditText etContactNo;
    String userType;
    @BindView(R.id.etSex)
    EditText etSex;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.etHandicap)
    EditText etHandicap;
    @BindView(R.id.etAffiliate)
    EditText etAffiliate;
    @BindView(R.id.etCorses)
    EditText etCorses;
    /*  @BindView(R.id.spHandicap)
      Spinner spHandicap;*/
   /* @BindView(R.id.spAffiliate)
    Spinner spAffiliate;*/
   /* @BindView(R.id.spCourses)
    Spinner spCourses;*/
    @BindView(R.id.etRefer)
    EditText etRefer;
    /* @BindView(R.id.spRefer)
     Spinner spRefer;*/
    @BindView(R.id.etProfession)
    EditText etProfession;
    /* @BindView(R.id.spProfession)
     Spinner spProfession;*/
    @BindView(R.id.etLocation)
    EditText etLocation;
    /*@BindView(R.id.spLocation)
    Spinner spLocation;*/

    @BindView(R.id.etRound)
    EditText etRound;

    @BindView(R.id.etLike)
    EditText etLike;
    @BindView(R.id.etSocialLike)
    EditText etSocialLike;
    @BindView(R.id.etType)
    EditText etType;
    @BindView(R.id.llCousre)
    LinearLayout llCousre;
    @BindView(R.id.llhandicap)
    LinearLayout llhandicap;
    @BindView(R.id.etHandicapCount)
    EditText etHandicapCount;

String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ProfileActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Const.EXTRA_USER_ID);

        getProfile(userId);

    }


    private void getProfile(String userId) {
        showProgressDialog("", "Please wait...");
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.getProfile(userId);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojoUser.getMessage());
                        binddata(pojoUser.getAllItems().get(0));
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

    private void binddata(BoUser user) {
        if (user != null) {
            if (user.getUserType().equals("Individual")) {
                llClubDeatl.setVisibility(View.GONE);
            } else {
                etDesc.setText(user.getDescription());
                etAddress.setText(user.getAddress());
                etCity.setText(user.getCity());
                etSubrub.setText(user.getSubRub());
                etContactNo.setText(user.getContact());
                etOperatingHours.setText(user.getOperatingHours());
                etType.setText(user.getUserType());
            }
            etType.setText(user.getUserType());
            etFrstnme.setText(user.getFirstName());
            etLstmne.setText(user.getLastName());
            etEmail.setText(user.getEmail());
            etClubName.setText(user.getClubName());

            etSex.setText(user.getSex());
            etAge.setText(user.getAge());
            etProfession.setText(user.getProfession());


            etHandicapCount.setText(user.getNoOfHandicap());
            etHandicap.setText(user.getHandicap());

            etRefer.setText(user.getRefer());

            etAffiliate.setText(user.getAffiliated());

            etLocation.setText(user.getLocation());
            etRound.setText(user.getRoundsPerMonth());
            etLike.setText(user.getPlayWithUs());
            etSocialLike.setText(user.getPlayWithOther());
            Common.showRoundImage(getApplicationContext(), ivPic, user.getProfileImage());

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void log(String message) {

    }


}
