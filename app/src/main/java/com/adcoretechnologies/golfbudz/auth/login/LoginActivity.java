package com.adcoretechnologies.golfbudz.auth.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
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

import com.adcoretechnologies.golfbudz.MyApplication;
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
import com.facebook.HttpMethod;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    @BindView(R.id.back_arrow_login)
    ImageView back_arrow_login;
    TwitterLoginButton login_button_twitter;
    ProgressDialog progress;
    long twitter_id;
    TwitterSession session;
    private CallbackManager callbackManager;
//    private LoginButton loginButton;
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
    private LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //for facebook init
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        //End
        setContentView(R.layout.activity_login);





        ButterKnife.bind(this);
        init();
//        loginTwitter();

    }



    @Override
    public void init() {
        gso = ((MyApplication) getApplication()).getGoogleSignInOptions();
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(LoginActivity.this, this);
        initilaize();
      //  printHashKey(this);
    }

    @OnClick(R.id.back_arrow_login)
    public void onBackClicked()
    {
        finish();
    }

//    @OnClick(R.id.ivFacebook)
//    public void onSignupFbClick() {
//        loginButton.callOnClick();
//    }
@OnClick(R.id.ivFacebook)
public void onSigninFbClick() {
    loginButton.callOnClick();
}

    @OnClick(R.id.llgp)
    public void onSigninGpClick() {
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
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                final AccessToken facebookToken = loginResult.getAccessToken();
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
                            } else if (oldProfile != null) {
                                facebook_id = oldProfile.getId();
                                full_name = oldProfile.getName();
                                f_name = oldProfile.getFirstName();
                                l_name = oldProfile.getLastName();
                                profile_image = String.valueOf(oldProfile.getProfilePictureUri(150, 150));
                            }
                            final GraphRequest request = GraphRequest.newMeRequest(
                                    facebookToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            String email = null;
                                            String birthday = null;
                                            try {
                                                if (object.getString("email") != null && !object.getString("email").equals("")) {
                                                    email = object.getString("email");
                                                }
                                                if (object.getString("id") != null && !object.getString("id").equals("")) {
                                                    facebook_id = object.getString("id");
                                                    socialRegisteration(f_name, l_name, email, profile_image, "facebook", facebook_id);
                                                } else {
                                                    disconnectFromFacebook();
                                                }

                                                log("SignInByFacebook=" + "f_name==" + f_name + "" + "l_name==" + l_name + "profile_image==" + profile_image + "facebook_id==" + facebook_id);

                                            } catch (JSONException e) {
                                                disconnectFromFacebook();
                                                toast("Please provide the permissions");
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    };
                } catch (Exception e) {
                    Common.logException(getApplicationContext(), "Error while logging in", e, null);
                }

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
    //This function will option signing intent

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                Log.e("gogle act",account.toString());
                handleSignInResult(result);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

   /* private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("Google", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String name = user.getDisplayName();
                            String[] parts = name.split("\\s+");
                            if (parts.length == 2) {
                                f_name = parts[0];
                                l_name = parts[1];

                            } else if (parts.length == 3) {
                                f_name = parts[0];
                                String middlename = parts[1];
                                l_name = parts[2];

                            }
                            String email = user.getEmail();
                            Uri photo = user.getPhotoUrl();
                            String personPhotoUrl = photo.toString();

                            Log.e("google login",f_name +  "  " + l_name +" " +" " + email + " ");

                            socialRegisteration(f_name, l_name, email, personPhotoUrl, "google", acct.getId());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }*/
    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        String personPhotoUrl = "";
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct == null) {
                toast("Not able to receive profile information from your account");
                return;
            }
            String email = acct.getEmail();
            if ("".equals(email)) {
                toast("Not able to retrieve email id from your account. Please try with other account");
                return;
            }
            String personName = acct.getDisplayName();
            try {
                personPhotoUrl = acct.getPhotoUrl() == null ? "" : acct.getPhotoUrl().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (personName != null && !personName.equals("")) {
                String[] parts = personName.split("\\s+");
                if (parts.length == 2) {
                    f_name = parts[0];
                    l_name = parts[1];

                } else if (parts.length == 3) {
                    f_name = parts[0];
                    String middlename = parts[1];
                    l_name = parts[2];

                }
            } else {
                log("Can not get your name from account");
            }
            log("SignInByGoogle=" + "email==" + email + "f_name==" + f_name + "" + "l_name==" + l_name + "profile_image==" + personPhotoUrl + "google_id==" + acct.getId());
            socialRegisteration(f_name, l_name, email, personPhotoUrl, "google", acct.getId());

        } else {
            toast("Please check your internet connection");
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
        Intent intent = new Intent(this, RegisterActivity.class).putExtra(Const.FROM, "signup");
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
//        loginButton.callOnClick();
        // LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    @OnClick(R.id.lltwtr)
    public void onTwitterLogin() {
        login_button_twitter.callOnClick();
    }

   /* @OnClick(R.id.llgp)
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
*/
    /*
    social login
     */
    private void socialRegisteration(final BoUser user) {
        IApiService apiService = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = apiService.loginSocial(user.getEmail(), "", toke, imei, Const.DEVICE_TYPE, user.getSocialToken());
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

        log("socialRegisteration=" + "firstname==" + firstname + "" + "l_name==" + l_name + "image==" + image + "socialId==" + socialId);
        if (socialId != null && !socialId.equals("")) {
            toke = FirebaseInstanceId.getInstance().getToken();
            imei = Common.getImei(this);
            BoUser boUser = new BoUser();
            boUser.setFirstName(firstname);
            boUser.setLastName(lastname);
            boUser.setEmail(gmail);
            boUser.setSocialToken(socialId);
            boUser.setProfileImage(image);
            boUser.setLanguage("en");
            boUser.setDeviceType(Const.DEVICE_TYPE);
            boUser.setImeiNo(imei);
            boUser.setPushId(toke);
            socialRegisteration(boUser);
        } else {
            toast("There are some issue with social login");
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

    }

}

