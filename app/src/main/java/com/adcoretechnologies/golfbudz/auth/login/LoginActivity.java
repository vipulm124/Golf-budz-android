package com.adcoretechnologies.golfbudz.auth.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.auth.Register.RegisterActivity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
    @BindView(R.id.ivFacebook)
    ImageView ivFacebook;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    ProgressDialog progress;
    private String facebook_id, type, m_name, l_name, gender, profile_image, full_name, email_id;
    String toke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //for facebook init
        FacebookSdk.sdkInitialize(getApplicationContext());

        //End
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {


        // printHashKey(this);
        ///start facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                log("facebook:onSuccess:" + loginResult);
//                toast("User authenticated successfully")
                toast("logoin sucessfull");


                ProfileTracker mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                        facebook_id = newProfile.getId();
                        full_name = newProfile.getName();
                        type = "facebook";

                    }
                };
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                String email = null;
                                String birthday = null;
                                try {
                                    email = object.getString("email");
                                    birthday = object.getString("birthday"); // 01/31/1980 format

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("type", "facebook");
                                i.putExtra("facebook_id", facebook_id);
                                i.putExtra("dob", birthday);
                                i.putExtra("full_name", full_name);
                                i.putExtra("email_id", email);
                                startActivity(i);
                                finish();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                //handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                toast("request cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Common.logException(getApplicationContext(), "Facebook login error", error, null);
            }
        });
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
        toke = FirebaseInstanceId.getInstance().getToken();
        Log.e("toke=", toke);
        String imei = Common.getImei(this);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.login(userName, password, Const.DEVICE_TYPE, imei, toke);
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
        Pref.saveUserDetail(getApplicationContext(), user);
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

    //for facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.ivFacebook)
    public void onFacebook() {
        loginButton.callOnClick();
        // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public String printHashKey(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void RequestData() {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                final JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        Log.e("", json.getString("name"));
                        Log.e("", json.getString("email"));
                        Log.e("", json.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
