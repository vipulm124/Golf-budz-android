package com.golf.budz.auth.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.golf.budz.auth.BoUser;
import com.golf.budz.auth.PojoUser;
import com.golf.budz.auth.Register.RegisterActivity;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.home.MainActivity;
import com.golf.budz.home.R;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvNewUser)
    TextView tvNewUser;
    @BindView(R.id.tvForgot)
    TextView tvForgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {


    }


    @OnClick(R.id.btnLogin)
    public void onLogin() {
        String userName = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            etEmail.setError("Input email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Input password");
            return;
        }
        performLogin(userName, password);
    }

    private void performLogin(String userName, String password) {
        showProgressDialog("Performing login", "Please wait...");
        String toke= FirebaseInstanceId.getInstance().getToken();
        String imei= Common.getImei(this);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.login(userName, password,Const.DEVICE_TYPE,imei,toke);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        saveUserDetail(pojoUser.getAllItems().get(0));
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

    private void saveUserDetail(BoUser user) {
        Pref.saveUserDetail(getApplicationContext(),user);
    }

    private void redirectToNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tvNewUser)
    public void onRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.tvForgot)
    public void onForgot() {
        Intent intent = new Intent(this, ForgotActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void log(String message) {
        super.log(getClass().getSimpleName(), message);
    }
    @Override
    public void onBackPressed() {
        AlertDialog show = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}
