package com.adcoretechnologies.golfbudz.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChnagePasswordActivity extends BaseActivity {


    @BindView(R.id.etOldPass)
    EditText etOldPass;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etCPass)
    EditText etCPass;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_password);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {

    }
    @OnClick(R.id.btnUpdate)
    public void onUpdate() {
        String oldPass = etOldPass.getText().toString();
        String newPass= etPass.getText().toString();
        String confirmPass = etCPass.getText().toString();

        if (TextUtils.isEmpty(oldPass)) {
            etOldPass.setError("Input old password");
            return;
        }
        if (TextUtils.isEmpty(newPass)) {
            etPass.setError("Input new password");
            return;
        }
        if (TextUtils.isEmpty(confirmPass)) {
            etCPass.setError("Input confirm password");
            return;
        }

        performChangePass(oldPass, newPass,confirmPass);
    }

    private void performChangePass(String oldPass, String newPass, String confirmPass) {
        showProgressDialog("Performing updation", "Please wait...");
        String toke= FirebaseInstanceId.getInstance().getToken();
        String imei= Common.getImei(this);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.updatePassword(oldPass, newPass, confirmPass);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        redirectToNextScreen();
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
    private void redirectToNextScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void log(String message) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            startActivity(new Intent(ChnagePasswordActivity.this, ProfileActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

//            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ChnagePasswordActivity.this, ProfileActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
//        super.onBackPressed();
    }
}