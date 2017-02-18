package com.golf.budz.auth.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.golf.budz.auth.BoUser;
import com.golf.budz.auth.GetImageContent;
import com.golf.budz.auth.PojoUser;
import com.golf.budz.auth.Register.BoCity;
import com.golf.budz.auth.Register.PojoCity;
import com.golf.budz.auth.Register.RegisterActivity;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.components.ComponentItemSelector;
import com.golf.budz.home.MainActivity;
import com.golf.budz.home.R;
import com.golf.budz.utils.CheckNetworkConnection;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.ConnectionAlertDialog;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.ShowAlert;
import com.golf.budz.utils.Validations;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.etFrstnme)
    EditText etFrstnme;
    @BindView(R.id.etLstmne)
    EditText etLstmne;
    @BindView(R.id.etEmail)
    EditText etEmail;




    @BindView(R.id.btSgnupbtn)
    Button btSgnupbtn;

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
    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.etOperatingHours)
    EditText etOperatingHours;
    @BindView(R.id.etContactNo)
    EditText etContactNo;
    String userType;
    private DatabaseReference mDatabase;
    private boolean isUploading;
    private Uri selectedImage;
    private static final int REQUEST_IMAGE_PICK = 1;
    private boolean isProfilePicChanged;
    private StorageReference storageRef;
    private ArrayList<Uri> allItems;
    private UploadTask uploadTask;
    private ArrayList<String> allUploadedUri;
    String userChoosenTask="";
    private static final int REQUEST_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = EditProfileActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        allItems = new ArrayList<>();
        allUploadedUri = new ArrayList<>();
        getAllCountries();
        getRoles();
        getProfile();
        try {
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);


        } catch (ClassCastException ex) {
            throw new RuntimeException("PLease initilalize firebase");
        }
    }

    @OnClick({R.id.ivPic})
    public void onProfilePicUpdate() {
        showImagePicker();
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

    public void showImagePicker() {
        if (isUploading) {
            toast(getResources().getString(R.string.image_uploading_toast));
        } else {
            selectImage();
            /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);//one can be replaced with any action code*/
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Common.checkPermission(EditProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_IMAGE_PICK);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Common.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {

                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    ivPic.setImageURI(selectedImage);
                }
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    ivPic.setImageBitmap(photo);
                    isProfilePicChanged = true;
                    String compressedImage = SiliCompressor.with(getApplicationContext()).compress(getImageUri(this,photo).toString());
                    selectedImage = Uri.fromFile(new File(compressedImage));
                    uploadImageToStorage(compressedImage, allItems.size() - 1);
                    ivPic.setImageURI(selectedImage);
                }
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        switch (requestCode) {

           /* case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    isProfilePicChanged = true;
                    String compressedImage = SiliCompressor.with(getApplicationContext()).compress(selectedImage.toString());
                    selectedImage = Uri.fromFile(new File(compressedImage));
                    uploadImageToStorage(compressedImage, allItems.size() - 1);
                    ivPic.setImageURI(selectedImage);
                }
                break;*/


        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void onCaptureImageResult(Intent data) {
        Uri selectedImage = data.getData();
        ivPic.setImageURI(selectedImage);
        isProfilePicChanged = true;
        String compressedImage = SiliCompressor.with(getApplicationContext()).compress(selectedImage.toString());
        selectedImage = Uri.fromFile(new File(compressedImage));
        uploadImageToStorage(compressedImage, allItems.size() - 1);
        ivPic.setImageURI(selectedImage);
    }
    private void uploadImageToStorage(final String file, final int position) {
        StorageReference imageRef = storageRef.child("Images/"+"image_" + System.currentTimeMillis() + ".jpg");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(file));


            uploadTask = imageRef.putStream(stream);

            isUploading = true;
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isUploading = false;
                    Common.logException(getApplicationContext(), "Image uploading failed", exception, null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    isUploading = false;

                    Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                    log("Upload complete for URI : " + file);
                    allUploadedUri.add(uploadedUrl.toString());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log("File not found for upload: " + file);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userType = parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void getAllCountries() {

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoCity> call = service.getAllCountry();
        call.enqueue(new Callback<PojoCity>() {
            @Override
            public void onResponse(Call<PojoCity> call, Response<PojoCity> response) {

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

    private void getProfile() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        showProgressDialog("Reteriving", "Please wait...");
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.getProfile(userId);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        toast(pojoUser.getMessage());
                        binddata(pojoUser.getAllItems().get(0));
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

    private void binddata(BoUser user) {
        if (user != null) {
            if (user.getUserType().equals("Individual")) {
                llClubDeatl.setVisibility(View.GONE);
            } else {
                etDesc.setText(user.getDescription());
                etAddress.setText(user.getAddress());
                etCity.setText(user.getCity());
                etSubrub.setText(user.getSubRub());
                etContactNo.setText(user.getContact());
                etOperatingHours.setText(user.getOperatingHours());
            }
            etFrstnme.setText(user.getFirstName());
            etLstmne.setText(user.getLastName());
            etEmail.setText(user.getEmail());
            etClubName.setText(user.getClubName());
            Common.showRoundImage(getApplicationContext(), ivPic, user.getProfileImage());
            try {
                selectedImage = Uri.parse(user.getProfileImage());
            } catch (Exception ex) {
                log("Profile pic url is not available");
            }
        }
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
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        String toke = FirebaseInstanceId.getInstance().getToken();
        String imei = Common.getImei(this);
        if (v.getId() == R.id.btSgnupbtn) {
            String fName = etFrstnme.getText().toString();
            String lName = etLstmne.getText().toString();
            String email = etEmail.getText().toString();
            String club = etClubName.getText().toString();
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
            else {

                BoUser user = new BoUser();
                user.setUserId(userId);
                user.setEmail(email);

                user.setContact(contactNo);
                user.setFirstName(fName);
                user.setLastName(lName);
                user.setCountry("India");
                if(allUploadedUri.size()>0)
                user.setProfileImage(allUploadedUri.get(0));
                else
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
                updateProfile(user);
            }

        }
    }

    private void updateProfile(BoUser user) {
        showProgressDialog("Updating", "Please wait...");

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.updateProfile(user.getUserId(),user);
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

        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    @Override
    public void log(String message) {

    }

}
