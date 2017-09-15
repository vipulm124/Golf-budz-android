package com.adcoretechnologies.golfbudz.core.components;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseFragment;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/4/2017.
 */

public class FragmentVideoUpload extends BaseFragment {


    private static final int REQUEST_VIEDO_PICK = 3;
    @BindView(R.id.rvImages)
    RecyclerView recyclerView;
    private AdapterVideoUploadPreview adapter;
    private StorageReference storageRef;
    private ArrayList<Uri> allItems;
    private UploadTask uploadTask;
    private ArrayList<String> allUploadedUri;
    private LinearLayoutManager layoutManager;
    private VideoUploadListener uploadListener;
    private View view;
    private boolean isUploading;

    public FragmentVideoUpload() {
        // Required empty public constructor
    }

    public static FragmentVideoUpload newInstance() {
        return new FragmentVideoUpload();
    }

    public boolean isUploading() {
        return isUploading;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_upload, null);

        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void setVisibility(boolean shouldVisible) {
        view.setVisibility(shouldVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void init() {
        allItems = new ArrayList<>();
        allUploadedUri = new ArrayList<>();
        try {
            uploadListener = (VideoUploadListener) getActivity();
        } catch (ClassCastException ex) {
            throw new RuntimeException("PLease implement interface ImageUploadListener");
        }

        adapter = new AdapterVideoUploadPreview(allItems);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        try {
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);


        } catch (ClassCastException ex) {
            throw new RuntimeException("PLease initilalize firebase");
        }
    }
private void genrateThumb(Uri selectedImage){
    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedImage.toString(),
            MediaStore.Images.Thumbnails.MINI_KIND);
    //ivCover.setImageBitmap(thumb);

}

    private void uploadVideoToStorage(final String file, final int position) {
        StorageReference imageRef = storageRef.child("Viedos/" + "video_" + System.currentTimeMillis() + ".mp3");
        InputStream stream = null;

        try {
            //stream = new FileInputStream(new File(file));
            stream = getContext().getContentResolver().openInputStream(Uri.parse(file));

            uploadTask = imageRef.putStream(stream);
            isUploading = true;
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isUploading = false;
                    updateProgress(position, true);
                    uploadListener.onVideoUploadFailed();
                    Common.logException(getContext(), "Viedo uploading failed", exception, null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    isUploading = false;
                    updateProgress(position, false);

                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                    log("Upload complete for URI : " + file);
                    allUploadedUri.add(uploadedUrl.toString());
                    uploadListener.onVideoUploadComplete(allUploadedUri);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    updateProgress(position, (int) progress);
                }
            });
        } catch (Exception e) {
            toast("error");
            e.printStackTrace();
        }
    }

    private void updateProgress(int position, boolean isFailed) {
        View view = layoutManager.findViewByPosition(position);
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pbImageUpload);
        if (pb != null) {
            if (isFailed)
                pb.getProgressDrawable().setColorFilter(
                        Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            else
                pb.getProgressDrawable().setColorFilter(
                        Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            log("progress is null");
        }
    }

    private void updateProgress(int position, int progress) {
        View view = layoutManager.findViewByPosition(position);
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pbImageUpload);
        if (pb != null) {
            pb.setProgress(progress);
        } else {
            log("progress is null");
        }
    }

    public void addNewUri(Uri selectedImage) {
        allItems.add(selectedImage);
        adapter.notifyDataSetChanged();
//        uploadImageToStorage(selectedImage, allItems.size() - 1);
        //String newFileName = compressImage(selectedImage.toString());
genrateThumb(selectedImage);
        uploadVideoToStorage(selectedImage.toString(), allItems.size() - 1);

    }

    public void removeItem(int position) {
        int clickedItem = recyclerView.getChildCount() - 2;

        if (isUploading && clickedItem != position) {
            toast("Please wait for upload to finish");
            return;
        }

        if (position == clickedItem && uploadTask != null) {
            uploadTask.cancel();
            isUploading = false;
        } else
            allUploadedUri.remove(position);

        allItems.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void onEventMainThread(BoEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {

            case BoEventData.EVENT_POST_VIDEO_UPLOAD: {
                showImagePicker();
                break;
            }
            case BoEventData.EVENT_POST_IMAGE_UPLOAD_REMOVE: {
                int pos = id;
                // Remove item from list of above position
                removeItem(pos);
                break;
            }
        }
    }

    public void showImagePicker() {
        if (isUploading) {
            toast("Please wait for uploading to be done.");
        } else {
        /*    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            getActivity().startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);*///one can be replaced with any action code
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select a Video "), REQUEST_VIEDO_PICK);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void log(String message) {

    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), getContext().getString(R.string.app_name) + "/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public interface VideoUploadListener {
        void onVideoUploadComplete(ArrayList<String> allUploadedUri);

        void onVideoUploadFailed();
    }
    public interface  getFIlePath{
       
    }
}