package com.adcoretechnologies.golfbudz.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.FragmentImageUpload;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends BaseActivity implements FragmentImageUpload.ImageUploadListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.etTilte)
    EditText etTilte;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etTime)
    EditText etTime;

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.ivDateicon)
    ImageView ivDateicon;
    @BindView(R.id.ivTimeicon)
    ImageView ivTimeicon;
    @BindView(R.id.btnPost)
    Button btnPost;
    String picUrls;
    private ArrayList<String> allUploadedImage;
    private FragmentImageUpload fragmentImageUploader;
    private static final int REQUEST_IMAGE_PICK = 1;
    DatePickerDialog datePickerDialog ;
    TimePickerDialog timePickerDialog ;
    int Year, Month, Day, Hour, Minute;
    private static final int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {
        fragmentImageUploader = (FragmentImageUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentImageUpload);
    }
    @OnClick(R.id.ivDateicon)
    public void selDate() {
        datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);

        datePickerDialog.setThemeDark(false);

        datePickerDialog.showYearPickerFirst(false);

        datePickerDialog.setAccentColor(Color.parseColor("#009688"));

        datePickerDialog.setTitle("Select Date From Golfing Budz");
       // datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
        final Calendar c = Calendar.getInstance();
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
        datePickerDialog.setMinDate(c);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int Month, int Day) {
        String date = Day + "-" + (Month + 1) + "-" + year;

        etDate.setText(date);

    }
    @OnClick(R.id.ivTimeicon)
    public void selTime() {
        timePickerDialog = TimePickerDialog.newInstance(this, Hour, Minute, true);

        timePickerDialog.setThemeDark(false);

       // timePickerDialog.show(false);

        timePickerDialog.setAccentColor(Color.parseColor("#009688"));

        timePickerDialog.setTitle("Select Time From TimePickerDialog");
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");


    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        etTime.setText(hourOfDay+":"+minute);
    }
    @OnClick(R.id.btnPost)
    public void onPost() {
        getPicsURL();
        String title = etTilte.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();

        if (TextUtils.isEmpty(title)) {
            etTilte.setError("Please enter");
            return;
        }else
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Please enter");
            return;
        }else
        if (TextUtils.isEmpty(date)) {
            etDate.setError("Please enter");
            return;
        }
        else
        if (TextUtils.isEmpty(time)) {
            etTime.setError("Please enter");
            return;
        }
        createEvent(title, description,date,time);
    }

    private void createEvent(String title, String description, String date, String time) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.createEvent(userName,title, description, date,time,picUrls,"",userId);
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
        Intent intent = new Intent(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public void log(String message) {

    }

    @Override
    public void onImageUploadComplete(ArrayList<String> allUploadedUri) {
        this.allUploadedImage = allUploadedUri;
    }

    @Override
    public void onImageUploadFailed() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {

                Uri selectedImage = imageReturnedIntent.getData();
                if (selectedImage != null) {
                    fragmentImageUploader.addNewUri(selectedImage);
                }
                Bundle extras2 = imageReturnedIntent.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    //  getImageUri(this,photo);
                    fragmentImageUploader.addNewUri(getImageUri(this, photo));
                }
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(imageReturnedIntent);
        }
        /*switch (requestCode) {
            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    fragmentImageUploader.addNewUri(selectedImage);
                }
                break;
        }*/
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void onCaptureImageResult(Intent data) {
        fragmentImageUploader.onCaptureImageResult(data);
        Uri selectedImage = data.getData();
        fragmentImageUploader.addNewUri(selectedImage);
    }
    private void getPicsURL() {
        try{
            for(int i=0; i<allUploadedImage.size();i++){
                picUrls=   allUploadedImage.get(i)+"|";
            }}catch (Exception e){}
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
