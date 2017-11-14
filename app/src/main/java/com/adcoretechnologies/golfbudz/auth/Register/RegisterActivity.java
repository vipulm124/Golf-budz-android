package com.adcoretechnologies.golfbudz.auth.Register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.auth.login.LoginActivity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.ComponentItemSelector;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.more.TermsConditionActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.ShowAlert;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.etFrstnme)
    EditText etFrstnme;
    @BindView(R.id.etLstmne)
    EditText etLstmne;
    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etCnfrmpswrd)
    EditText etCnfrmpswrd;
    @BindView(R.id.tvIaccept)
    TextView tvIaccept;
    @BindView(R.id.tvTc)
    TextView tvTc;
    @BindView(R.id.tvTccheckBox)
    CheckBox tvTccheckBox;
    @BindView(R.id.btSgnupbtn)
    Button btSgnupbtn;
    @BindView(R.id.tandc)
    RelativeLayout tandc;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.llClubDeatl)
    LinearLayout llClubDeatl;
    @BindView(R.id.spinner)
    Spinner spinner;
    boolean checked = false;
    List<String> items = new ArrayList<String>();
    List<String> roleitems = new ArrayList<String>();
    ComponentItemSelector citySelector;
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
    @BindView(R.id.back_arrow_register)
    ImageView back_arrow_register;
    @BindView(R.id.etOperatingHours)
    EditText etOperatingHours;
    @BindView(R.id.etContactNo)
    EditText etContactNo;
    String userType;
    BoUser boUser;
    String from, token, imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();

        Intent intent = getIntent();
        boUser = (BoUser) intent.getSerializableExtra(Const.USER);
        from = intent.getStringExtra(Const.FROM);
        if (from.equals("social")) {
            etPassword.setVisibility(View.VISIBLE );
            etCnfrmpswrd.setVisibility(View.VISIBLE);

        }
        if (boUser != null) {
            etFrstnme.setText(boUser.getFullName());
            etEmail.setText(boUser.getEmail());
        }


    }

    @OnClick(R.id.back_arrow_register)
    public void onBackClicked(){

        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void init() {
        getAllCountries();
        getRoles();
        tvTccheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    checked = true;
                } else
                    checked = false;
            }
        });
    }

    private void getRoles() {

        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Individual");
        categories.add("Club");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         userType = parent.getItemAtPosition(position).toString();
        if (userType.equals("Club")) {
            llClubDeatl.setVisibility(View.VISIBLE);
        } else
            llClubDeatl.setVisibility(View.GONE);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void log(String message) {

    }

    private void getAllCountries() {
        showProgressDialog("Fetching data", "Please wait...");

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoCity> call = service.getAllCountry();
        call.enqueue(new Callback<PojoCity>() {
            @Override
            public void onResponse(Call<PojoCity> call, Response<PojoCity> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoCity pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        bindData(pojoUser.getAllItems());
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
            public void onFailure(Call<PojoCity> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void bindData(ArrayList<BoCity> allItems) {
        for (int i = 0; i < allItems.size(); i++) {
            items.add(allItems.get(i).getName());
        }
        citySelector = (ComponentItemSelector) getSupportFragmentManager().findFragmentById(R.id.componentCitySelector);
        // items = new String[]{"Noida", "Delhi"};
        citySelector.initialize(items, "Select Country");
    }

    @OnClick(R.id.tvTc)
    public void termsAndCondition()
    {
        Intent i = new Intent(RegisterActivity.this, TermsConditionActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btSgnupbtn)
    public void sungUp() {
        btSgnupbtn.setOnClickListener(this);

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
    public void onClick(View v) {

        String toke= FirebaseInstanceId.getInstance().getToken();
        String imei=Common.getImei(RegisterActivity.this);
        if (v.getId() == R.id.btSgnupbtn) {
            String fName = etFrstnme.getText().toString();
            String lName = etLstmne.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etCnfrmpswrd.getText().toString();

            String club =etClubName.getText().toString();
            String description = etDesc.getText().toString();
            String address = etAddress.getText().toString();
            String city = etCity.getText().toString();
            String subrub = etSubrub.getText().toString();
            String operatingHours = etOperatingHours.getText().toString();
            String contactNo = etContactNo.getText().toString();


            if (TextUtils.isEmpty(fName)) {
                etFrstnme.setError("Enter FirstName");
            } else if (TextUtils.isEmpty(lName)) {
                etLstmne.setError("Enter LastName");
            } else if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter Email");
            } else if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter Password");
            } else if (TextUtils.isEmpty(confirmPassword)) {
                etCnfrmpswrd.setError("Re-enter Password");
            }
           /* else if(userType.equals("Club")){
                if (TextUtils.isEmpty(club)) {
                    etClubName.setError("Enter Club Name");
                } else if (TextUtils.isEmpty(description)) {
                    etDesc.setError("Enter Description");
                } else if (TextUtils.isEmpty(address)) {
                    etAddress.setError("Enter Address");
                } else if (TextUtils.isEmpty(city)) {
                    etCity.setError("Enter City");
                } else if (TextUtils.isEmpty(subrub)) {
                    etSubrub.setError("Enter SubRub");
                }
                else if (TextUtils.isEmpty(contactNo)) {
                    etSubrub.setError("Enter contact no");
                }
            }*/
            else if (checked == false) {
                ShowAlert.showAlertDialog(this, "", "Please select terms & Condition", false);

            } else {

                BoUser user = new BoUser();
                user.setUserId("");
                user.setEmail(email);
                user.setPassword(password);
                user.setContact(contactNo);
                user.setFirstName(fName);
                user.setLastName(lName);
                user.setCountry("India");
                user.setProfileImage("");
                user.setDob("");
                user.setHandicap("");
                user.setStrength("");
                user.setWeakness("");

                user.setDeviceId(toke);
                user.setImeiNo(imei);
                user.setDeviceType(Const.DEVICE_TYPE);
                user.setUserType(userType);
                user.setClubName(club);
                user.setDescription(description);
                user.setAddress(address);
                user.setCity(city);
                user.setSubRub(subrub);
                user.setOperatingHours(operatingHours);
                Log.e("userr",user.toString());

                register(user);
            }

        }
    }

    private void register(BoUser user) {
        Log.e("click","cdsc" + user);
        showProgressDialog("Registering", "Please wait...");

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.register(user);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojo.getMessage());
                        saveUserDetail(pojo.getAllItems().get(0));
                        redirectToNextScreen();

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {

                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    redirectToNextScreen();
                    toast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<PojoUser> call, Throwable t) {
                redirectToNextScreen();
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }

    private void saveUserDetail(BoUser user) {
        Pref.saveUserDetail(getApplicationContext(), user);
    }

    private void redirectToNextScreen() {

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

    }
}
