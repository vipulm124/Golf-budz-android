package com.adcoretechnologies.golfbudz.group;

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
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.FragmentImageUpload;
import com.adcoretechnologies.golfbudz.core.components.FragmentVideoUpload;
import com.adcoretechnologies.golfbudz.group.invite.InviteFriendActivity;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePageActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener, FragmentImageUpload.ImageUploadListener, FragmentVideoUpload.VideoUploadListener {


    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.etCat)
    EditText etCat;
    @BindView(R.id.etHours)
    EditText etHours;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.ivVideo)
    ImageView ivVideo;
    @BindView(R.id.ivGallery)
    ImageView ivGallery;
    @BindView(R.id.ivInvite)
    ImageView ivInvite;

    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    TimePickerDialog timePickerDialog;
    int Year, Month, Day, Hour, Minute;
    private ArrayList<String> allUploadedImage;
    private ArrayList<String> allUploadedVideo;
    private FragmentImageUpload fragmentImageUploader;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_CAMERA = 2;
    private FragmentVideoUpload fragmentVideoUploader;
    private static final int REQUEST_VIEDO_PICK = 3;
    String friendIds = "", picUrls = "", videoUrls = "", name = "", description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        fragmentImageUploader = (FragmentImageUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentImageUpload);
        fragmentVideoUploader = (FragmentVideoUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentVideoUpload);
        fragmentImageUploader.setVisibility(false);
        fragmentVideoUploader.setVisibility(false);
        Intent intent = getIntent();
        if (intent != null) {
            friendIds = intent.getStringExtra(Const.EXTRA_FRIEND_ID);
            name = intent.getStringExtra("name");
            description = intent.getStringExtra("description");
            etName.setText(name);
            etDesc.setText(description);

        }
    }


    @OnClick(R.id.btnUpdate)
    public void onUpdate() {
        // getPicsURL();
        name = etName.getText().toString();
        description = etDesc.getText().toString();
        String category = etCat.getText().toString();
        String operatingHour = etHours.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(description)) {
            etDesc.setError("Please enter");
            return;
        } /*else if (TextUtils.isEmpty(category)) {
            etCat.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(operatingHour)) {
            etHours.setError("Please enter");
            return;
        }*/
        createGroup(name, description, category, operatingHour);
    }

    private void createGroup(String name, String description, String category, String operatingHour) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.createGroup(userName, name, description, category, operatingHour, getPicsURL(), getVideoURL(), userId, friendIds);
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

    @OnClick(R.id.etHours)
    public void operatingHours() {
        timePickerDialog = TimePickerDialog.newInstance(this, Hour, Minute, true);

        timePickerDialog.setThemeDark(false);

        // timePickerDialog.show(false);

        timePickerDialog.setAccentColor(Color.parseColor("#009688"));

        timePickerDialog.setTitle("Select Time From TimePickerDialog");
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        etHours.setText(hourOfDay + ":" + minute);
    }

    @Override
    public void log(String message) {

    }

    @OnClick(R.id.ivGallery)
    public void openGallery() {
        fragmentImageUploader.setVisibility(true);
    }

    @OnClick(R.id.ivVideo)
    public void openVideo() {
        fragmentVideoUploader.setVisibility(true);
    }

    @OnClick(R.id.ivInvite)
    public void openInvitation() {
        startActivity(new Intent(this, InviteFriendActivity.class).putExtra("name", etName.getText().toString()).putExtra("description", etDesc.getText().toString()));
    }

    @Override
    public void onImageUploadComplete(ArrayList<String> allUploadedUri) {
        this.allUploadedImage = allUploadedUri;
    }

    @Override
    public void onVideoUploadComplete(ArrayList<String> allUploadedUri) {
        this.allUploadedVideo = allUploadedUri;
    }

    @Override
    public void onVideoUploadFailed() {

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
            else if (requestCode == REQUEST_VIEDO_PICK) {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    fragmentVideoUploader.addNewUri(selectedImage);
                }
            }
        }

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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private String getPicsURL() {
        try {
            for (int i = 0; i < allUploadedImage.size(); i++) {
                picUrls = allUploadedImage.get(i) + "|";
            }
        } catch (Exception e) {
        }
        return picUrls;
    }

    private String getVideoURL() {
        try {
            for (int i = 0; i < allUploadedVideo.size(); i++) {
                videoUrls = allUploadedVideo.get(i) + "|";
            }
        } catch (Exception e) {
        }
        return videoUrls;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
