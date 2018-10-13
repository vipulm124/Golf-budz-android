package com.adcoretechnologies.golfbudz.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.boxing_ui.ui.BoxingActivity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.FragmentVideoUpload;
import com.adcoretechnologies.golfbudz.home.model.PojoPost;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDatapostActivity extends BaseActivity  {
    @BindView(R.id.etText)
    EditText etText;


    @BindView(R.id.btnPost)
    Button btnPost;
    private ArrayList<String> allUploadedImage=new ArrayList<>();
    private FragmentVideoUpload fragmentVideoUploader;
    private static final int REQUEST_VIEDO_PICK = 3;
    String picUrls;
    @BindView(R.id.ivVideoPlay)
    ImageView ivVideoPlay;

    @BindView(R.id.ivCross)
    ImageView ivCross;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    private static final int REQUEST_CODE_VIDEO = 1;
    private StorageReference storageRef;
    Bitmap thumb;
    String localPath,text;

    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_datapost);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        try {
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);
        } catch (ClassCastException ex) {
            throw new RuntimeException("PLease initilalize firebase");
        }
        init();
    }

    @Override
    public void init() {
    }


    @OnClick(R.id.ivImage)
    public void onSelect() {
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.VIDEO).needCamera(R.drawable.ic_boxing_camera_white).needGif();
        Boxing.of(config).withIntent(VideoDatapostActivity.this, BoxingActivity.class).start(VideoDatapostActivity.this, REQUEST_CODE_VIDEO);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_VIDEO) {
                final List<BaseMedia> medias = Boxing.getResult(data);
                localPath=medias.get(0).getPath();
                // uploadVideoToStorage(medias.get(0).getPath());
                 thumb = ThumbnailUtils.createVideoThumbnail(medias.get(0).getPath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                ivImage.setImageBitmap(thumb);

                           }

        }
    }
    private void uploadImageToStorage(final String file, final String localPath) {
        updateProgress();
        final StorageReference imageRef = storageRef.child("Images/" + "image_" + System.currentTimeMillis() + ".jpg");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(file));
            uploadTask = imageRef.putStream(stream);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                    uploadVideoToStorage(Uri.fromFile(new File(localPath)).toString(), uploadedUrl.toString());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(Uri.parse(path), projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void uploadVideoToStorage(final String file, final String thumUrl) {
        final StorageReference imageRef = storageRef.child("Videos/" + "video_" + System.currentTimeMillis() + ".mp3");
        InputStream stream = null;
        try {
            //stream = new FileInputStream(new File(file));
            stream = getContentResolver().openInputStream(Uri.parse(file));
            uploadTask = imageRef.putStream(stream);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDismiss();
                    Common.logException(VideoDatapostActivity.this, "Viedo uploading failed", exception, null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    progressDismiss();
                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                   // post(uploadedUrl.toString(), thumUrl);
                    addPost(uploadedUrl.toString(), thumUrl);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ProgressDialog ringProgressDialog;
    boolean progressUploadig = true;

    private void updateProgress() {
        ringProgressDialog = ProgressDialog.show(this, "Please wait ...", "Uploading ...", false);
        ringProgressDialog.setCancelable(false);

    }

    private void progressDismiss() {
        ringProgressDialog.dismiss();
    }

    @OnClick(R.id.btnPost)
    public void onPost() {

         text = etText.getText().toString();


        if (TextUtils.isEmpty(text)) {
            etText.setError("Please enter");
            return;
        }else if(localPath.equals("")){
            toast("Please upload images");
            return;
        }
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(localPath,
                MediaStore.Images.Thumbnails.MINI_KIND);
        uploadImageToStorage(getImageUri(this, thumb), localPath);
    }
    private void addPost( String videoUrl, String thumbUrl) {
        showProgressDialog("Performing creation", "Please wait...");
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String userName = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        String userImage = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoPost> call = service.addPost(userId,text,videoUrl,Const.VIDEO,thumbUrl);
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