package com.adcoretechnologies.golfbudz.items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.FragmentImageUpload;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellItemActivity extends BaseActivity implements FragmentImageUpload.ImageUploadListener {
    @BindView(R.id.etTilte)
    EditText etTilte;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.btnPost)
    Button btnPost;

    private ArrayList<String> allUploadedImage;
    private FragmentImageUpload fragmentImageUploader;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_CAMERA = 2;
    String picUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public void init() {

        fragmentImageUploader = (FragmentImageUpload) getSupportFragmentManager().findFragmentById(R.id.fragmentImageUpload);
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

    @OnClick(R.id.btnPost)
    public void onPost() {
        getPicsURL();
        String title = etTilte.getText().toString();
        String description = etDesc.getText().toString();
        String price = etPrice.getText().toString();


        if (TextUtils.isEmpty(title)) {
            etTilte.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(description)) {
            etDesc.setError("Please enter");
            return;
        } else if (TextUtils.isEmpty(price)) {
            etPrice.setError("Please enter");
            return;
        }
        PerformSelling(title, description, price);
    }
    private void getPicsURL() {
        try{
            for(int i=0; i<allUploadedImage.size();i++){
                picUrls=   allUploadedImage.get(i)+"|";
            }}catch (Exception e){}
    }
    private void PerformSelling(String title, String description, String price) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoItems> call = service.sellItem(userId, title, price, picUrls,description );
        call.enqueue(new Callback<PojoItems>() {
            @Override
            public void onResponse(Call<PojoItems> call, Response<PojoItems> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoItems pojo = response.body();
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
            public void onFailure(Call<PojoItems> call, Throwable t) {
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