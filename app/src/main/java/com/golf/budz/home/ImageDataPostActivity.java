package com.golf.budz.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.golf.budz.auth.PojoUser;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.components.FragmentImageUpload;
import com.golf.budz.home.model.PojoPost;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageDataPostActivity extends BaseActivity implements FragmentImageUpload.ImageUploadListener {
    @BindView(R.id.etText)
    EditText etText;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    @BindView(R.id.btnPost)
    Button btnPost;
    private ArrayList<String> allUploadedImage=new ArrayList<>();
    private FragmentImageUpload fragmentImageUploader;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_CAMERA = 2;
    String picUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_post);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        fragmentImageUploader = (FragmentImageUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentImageUpload);
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
                fragmentImageUploader.addNewUri(selectedImage);
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
      /*  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);*/
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void onCaptureImageResult(Intent data) {
       /* fragmentImageUploader.onCaptureImageResult(data);
         Uri selectedImage = data.getData();
        fragmentImageUploader.addNewUri(selectedImage);*/
        fragmentImageUploader.onCaptureImageResult(data);
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        Uri selectedImage=getImageUri(this,imageBitmap);
        fragmentImageUploader.addNewUri(selectedImage);
    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        getPicsURL();
        String text = etText.getText().toString();


        if (TextUtils.isEmpty(text)) {
            etText.setError("Please enter");
            return;
        } else if (allUploadedImage.size() == 0) {
            toast("Please upload images");
            return;
        }
        addPost(text);
    }

    private void addPost(String text) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        String userImage = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoPost> call = service.addPost(userName, userId, text, picUrls, "", Const.IMAGE, "0", "0", userImage);
        call.enqueue(new Callback<PojoPost>() {
            @Override
            public void onResponse(Call<PojoPost> call, Response<PojoPost> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPost pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojo.getMessage());
                        redirectToNextScreen();
                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<PojoPost> call, Throwable t) {
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

    private void getPicsURL() {
        try {
            for (int i = 0; i < allUploadedImage.size(); i++) {
                picUrls = allUploadedImage.get(i) + "|";
            }
        } catch (Exception e) {
        }
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
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
