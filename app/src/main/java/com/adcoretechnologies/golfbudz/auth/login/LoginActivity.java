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
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

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

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    //private static final int RC_SIGN_IN = 007;
    private static final int RC_FB_SIGNIN = 005;
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
    @BindView(R.id.lltwtr)
    ImageView lltwtr;
    @BindView(R.id.llgp)
    ImageView llgp;
    TwitterLoginButton login_button_twitter;
    ProgressDialog progress;
    long twitter_id;
    TwitterSession session;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private SignInButton btnSignIn_google;
    private String facebook_id, type, f_name, l_name, gender, profile_image, full_name, email_id, twitterImage, username, email_twitter;
    String toke, imei;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
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
        session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();




        ButterKnife.bind(this);
        init();
        loginTwitter();

    }

    @Override
    public void init() {
        initilaize();
        // printHashKey(this);
        ///start facebook
 /*       callbackManager = CallbackManager.Factory.create();
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
                                    socialRegisteration(f_name, l_name, email, profile_image, "facebook", facebook_id);

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
        });*/
    }
    @OnClick(R.id.ivFacebook)
    public void onSignupFbClick() {
        loginButton.callOnClick();
    }

    @OnClick(R.id.llgp)
    public void onSignupGpClick() {
        signIn();
    }
    private void initilaize() {
        toke = FirebaseInstanceId.getInstance().getToken();
        imei = Common.getImei(this);
        // printKeyHash(this);
        mAuth = FirebaseAuth.getInstance();

        if (Pref.isLoggedIn(this)) {
            LoginManager.getInstance().logOut();
        }
        ///start facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    ProfileTracker mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                            if (newProfile != null) {
                                facebook_id = newProfile.getId();
                                full_name = newProfile.getName();
                                f_name = newProfile.getFirstName();
                                l_name = newProfile.getLastName();
                                profile_image = String.valueOf(newProfile.getProfilePictureUri(150, 150));
                                ;

                            }
                        }
                    };
                } catch (Exception e) {
                    Common.logException(getApplicationContext(), "Error while logging in", e, null);
                }
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String email = null;
                                String birthday = null;
                                try {
                                    email = object.getString("email");
                                    socialRegisteration(f_name, l_name, email, profile_image, "facebook", facebook_id);

                                } catch (JSONException e) {
                                    toast("email not found");
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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
        //for google
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    //This function will option signing intent
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            //Calling a new function to handle signin
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        String personPhotoUrl = "";
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            try {
                personPhotoUrl = acct.getPhotoUrl().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String email = acct.getEmail();
            String[] parts = personName.split("\\s+");
            if (parts.length == 2) {
                f_name = parts[0];
                l_name = parts[1];

            } else if (parts.length == 3) {
                f_name = parts[0];
                String middlename = parts[1];
                l_name = parts[2];

            }


            socialRegisteration(f_name, l_name, email, personPhotoUrl, "google", acct.getId());

        } else {
            toast("Not able to login");
        }
    }
    public void loginTwitter() {
        login_button_twitter = (TwitterLoginButton) findViewById(R.id.login_button_twitter);
        login_button_twitter.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("Sucess", "sucess");
                TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(true, false, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        try {
                            User user = userResult.data;
                            twitterImage = user.profileImageUrl;
                            username = user.name;

                            twitter_id = user.id;
                            email_twitter = user.email;

                            log("Image is:" + twitterImage);
                            log("user name is:" + username);
                            log("twitterId" + twitter_id);
                            toast("Name" + username + "\n " + "email" + email_twitter + "\n " + "UserImage_url" + twitterImage);
                            socialRegisteration(f_name, l_name, email_twitter, profile_image, "facebook", facebook_id);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }

                });

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("Fail", "fail");
                Toast.makeText(LoginActivity.this, "App not installed", Toast.LENGTH_LONG).show();
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
//        Log.e("token=", toke);
        String imei = Common.getImei(this);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.login(userName, password, Const.DEVICE_TYPE, imei, toke,"");
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
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            login_button_twitter.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (requestCode == RC_FB_SIGNIN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            log("" +
                    " else");
        }

    }*/

    /*private void handleSignInResult(GoogleSignInResult result) {
        Log.d("LoginActivity", "handleSignInResult:" + result.isSuccess());
        Log.d("result", "result:" + result.toString());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("LoginActivity", "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e("LoginActivity", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            toast("Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);


        } else {
            log("inside else part");
        }
    }*/

    @OnClick(R.id.ivFacebook)
    public void onFacebook() {
        loginButton.callOnClick();
        // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    @OnClick(R.id.lltwtr)
    public void onTwitterLogin() {
        login_button_twitter.callOnClick();
    }

    @OnClick(R.id.llgp)
    public void OnGoogleLogin() {
        btnSignIn_google = (SignInButton) findViewById(R.id.btn_sign_in);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        btnSignIn_google.callOnClick();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("LoginActiivty", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    /*
    social login
     */
    private void socialRegisteration(final BoUser user) {
        IApiService apiService = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = apiService.login(user.getEmail(), "", toke, imei, Const.DEVICE_TYPE, user.getSocialToken());
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser puser = response.body();
                    if (puser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(puser.getMessage());
                        saveUserDetail(puser.getAllItems().get(0));
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (puser.getStatus() == Const.STATUS_FAILED) {
                        openNextScreen(user);
                        // toast(user.getMessage());
                    } else if (puser.getStatus() == Const.STATUS_ERROR) {
                        toast(puser.getMessage());
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



    private void openNextScreen(BoUser boUser) {
        startActivity(new Intent(this, RegisterActivity.class).putExtra(Const.USER, boUser).putExtra(Const.FROM, "social"));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void socialRegisteration(String firstname, String lastname, String gmail, String image, String type, String socialId) {
        toke = FirebaseInstanceId.getInstance().getToken();
        imei = Common.getImei(this);
        BoUser boUser = new BoUser();
        boUser.setFullName(firstname);
        boUser.setEmail(gmail);
        boUser.setSocialToken(socialId);
        boUser.setProfileImage(image);
        boUser.setLanguage("en");
        boUser.setDeviceType(Const.DEVICE_TYPE);
        boUser.setImeiNo(imei);
        boUser.setPushId(toke);
        socialRegisteration(boUser);

    }
    @Override
    public void onStart() {
        super.onStart();


    }

}

