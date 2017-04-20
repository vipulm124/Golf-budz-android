package com.adcoretechnologies.golfbudz.home;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class VideoActivity extends AppCompatActivity {
    private static int REQUEST_TAKE_GALLERY_VIDEO = 1;
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
         videoView = (VideoView)findViewById(R.id.VideoView);


     /*   videoView.setVideoPath("/storage/extSdcard/DCIM/Camera/20170204_222024.mp4");

        videoView.start();*/
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {

                    videoView.setVideoURI(selectedImageUri);
                    videoView.start();
                     StorageReference   storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);
                    StorageReference imageRef = storageRef.child("Viedos/"+"video_" + System.currentTimeMillis()+".mp3");
                    InputStream stream = null;
                    try {
                        //stream = new FileInputStream(new File(selectedImageUri.toString()));
                        stream= getContentResolver().openInputStream(selectedImageUri);

                        UploadTask uploadTask = imageRef.putStream(stream);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(VideoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(VideoActivity.this, downloadUrl+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // UPDATED!
  /*  public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }*/
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
}
