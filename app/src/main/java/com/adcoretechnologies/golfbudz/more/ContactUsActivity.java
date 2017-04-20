package com.adcoretechnologies.golfbudz.more;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.etTilte)
    EditText etTilte;
    @BindView(R.id.etMsg)
    EditText etMsg;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnSend)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {

    }


    @OnClick(R.id.btnSend)
    public void onSend() {

        String title = etTilte.getText().toString();
        String message = etMsg.getText().toString();
        String email = etEmail.getText().toString();


        if (TextUtils.isEmpty(title)) {
            etTilte.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(message)) {
            etMsg.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter");
            return;
        }
        contactUs(title, message,email);
    }

    private void contactUs(String title, String msg, String email) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoContact> call = service.contactUs(userId, title,msg,email);
        call.enqueue(new Callback<PojoContact>() {
            @Override
            public void onResponse(Call<PojoContact> call, Response<PojoContact> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoContact pojoUser = response.body();
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
            public void onFailure(Call<PojoContact> call, Throwable t) {
                hideDialog();
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });

    }


    private void redirectToNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void log(String message) {

    }







    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}