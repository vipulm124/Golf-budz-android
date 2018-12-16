package com.adcoretechnologies.golfbudz.auth.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.auth.Register.BoCity;
import com.adcoretechnologies.golfbudz.auth.Register.PojoCity;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.components.ComponentItemSelector;
import com.adcoretechnologies.golfbudz.core.components.ComponentLocationItemSelector;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.playrequest.model.BoDropVales;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoDropValues;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
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
import java.io.IOException;
import java.nio.file.ProviderMismatchException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

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

    boolean checked = false;
    List<String> locationList = new ArrayList<String>();
    List<String> professionList = new ArrayList<String>();
    List<String> roleitems = new ArrayList<String>();
    ComponentLocationItemSelector citySelector;
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
    @BindView(R.id.etSex)
    EditText etSex;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.spHandicap)
    Spinner spHandicap;
    @BindView(R.id.spAffiliate)
    Spinner spAffiliate;
    @BindView(R.id.spCourses)
    Spinner spCourses;
    @BindView(R.id.spRefer)
    Spinner spRefer;
    @BindView(R.id.spProfession)
    Spinner spProfession;
    @BindView(R.id.spLocation)
    Spinner spLocation;

    @BindView(R.id.etRound)
    EditText etRound;

    @BindView(R.id.etLike)
    EditText etLike;
    @BindView(R.id.etSocialLike)
    EditText etSocialLike;
    @BindView(R.id.etType)
    EditText etType;
    @BindView(R.id.llCousre)
    LinearLayout llCousre;
    @BindView(R.id.llhandicap)
    LinearLayout llhandicap;
    @BindView(R.id.etHandicapCount)
    EditText etHandicapCount;
    @BindView(R.id.etlocation)
    Spinner etLocation;
    private DatabaseReference mDatabase;
    private boolean isUploading;
    private Uri selectedImage;
    private static final int REQUEST_IMAGE_PICK = 1;
    private boolean isProfilePicChanged;
    private StorageReference storageRef;
    private ArrayList<Uri> allItems;
    private UploadTask uploadTask;
    private ArrayList<String> allUploadedUri;
    String userChoosenTask = "";
    private static final int REQUEST_CAMERA = 2;
    String affliateval, handicapval, corsesval, referval, professionval;
    final List<String> course = new ArrayList<String>();
    String location;

    List<String> affliateList;
    List<String> handicapList;
    List<String> referList;

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

        affliate();
        handicap();
//        getCorses();
        preferOption();
        getProfession();
        getLocation();
        allItems = new ArrayList<>();
        allUploadedUri = new ArrayList<>();
        getAllCountries();
        getProfile();

        try {
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);
        } catch (ClassCastException ex) {
            throw new RuntimeException("Please initilalize firebase");
        }
    }


    @OnClick({R.id.ivPic})
    public void onProfilePicUpdate() {
        showImagePicker();
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
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Common.checkPermission(EditProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Common.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
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
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                       // String compressedImage = SiliCompressor.with(getApplicationContext()).compress(uri.toString());
                        uploadImageToStorage(uri, allItems.size() - 1);
                        ivPic.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                isProfilePicChanged = true;
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
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
       /* Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        Uri selectedImage=getImageUri(this,imageBitmap);
        ivPic.setImageURI(selectedImage);
        isProfilePicChanged = true;
        String compressedImage = SiliCompressor.with(getApplicationContext()).compress(selectedImage.toString());
        selectedImage = Uri.fromFile(new File(compressedImage));
        uploadImageToStorage(compressedImage, allItems.size() - 1);
        ivPic.setImageURI(selectedImage);*/
    }
    private void uploadImageToStorage(final Uri file, final int position) {
        //  TODo replace with item name provided by user
        StorageReference imageRef = storageRef.child("image_" + file.getLastPathSegment());
        uploadTask = imageRef.putFile(file);

        isUploading = true;
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                isUploading = false;
                //updateProgress(position, true);
               // uploadListener.onImageUploadFailed();
                Common.logException(getApplicationContext(), "Image uploading failed", exception, null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                isUploading = false;
               // updateProgress(position, false);

                Uri uploadedUrl = taskSnapshot.getDownloadUrl();
                log("Upload complete for URI : " + file.getLastPathSegment());
                allUploadedUri.add(uploadedUrl.toString());
               // uploadListener.onImageUploadComplete(allUploadedUri);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //updateProgress(position, (int) progress);
            }
        });
    }

    private void uploadImageToStorage(final String file, final int position) {
        StorageReference imageRef = storageRef.child("Images/" + "image_" + System.currentTimeMillis() + ".jpg");
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


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        userType = parent.getItemAtPosition(position).toString();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }*/


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
                    toast("Something went wrong while fetching countries");
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
            locationList.add(allItems.get(i).getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, locationList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spLocation.setAdapter(dataAdapter);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = locationList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void bindDataProfession(ArrayList<BoProfession> allItems) {
//        for (int i = 0; i < allItems.size(); i++) {
//            professionList.add(allItems.get(i).getName());
//        }
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, locationList);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spProfession.setAdapter(dataAdapter);
//        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                professionval = professionList.get(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

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
                        //toast(pojoUser.getMessage());
                        binddata(pojoUser.getAllItems().get(0));
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                        toast(pojoUser.getMessage());
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                        toast(pojoUser.getMessage());
                    }
                } else {
                    toast("Something went wrong while fetching profile");
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
                etType.setText(user.getUserType());
            }
            etType.setText(user.getUserType());
            etFrstnme.setText(user.getFirstName());
            etLstmne.setText(user.getLastName());
            etEmail.setText(user.getEmail());
            etClubName.setText(user.getClubName());

            etSex.setText(user.getSex());
            etAge.setText(user.getAge());

            int index = professionList.indexOf(user.getProfession());
            spProfession.setSelection(index);

            int indexhandicap = handicapList.indexOf(user.getHandicap());
            spHandicap.setSelection(indexhandicap);
            etHandicapCount.setText(user.getNoOfHandicap());

            int indexrefer = referList.indexOf(user.getRefer());
            spRefer.setSelection(indexrefer);

            int indexaffliate = referList.indexOf(user.getAffiliated());
            spAffiliate.setSelection(indexaffliate);

            int indexLocation = locationList.indexOf(user.getLocation());
            spLocation.setSelection(indexLocation);

            etRound.setText(user.getRoundsPerMonth());
            etLike.setText(user.getPlayWithUs());
            etSocialLike.setText(user.getPlayWithOther());
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
            String handicapCount = etHandicapCount.getText().toString();

            String sex = etSex.getText().toString();
            String age = etAge.getText().toString();
            String handicap = handicapval;
            String affliate = affliateval;
            String profession = professionval;
            String refer = referval;
            String round = etRound.getText().toString();
            String like = etLike.getText().toString();
            String sociallike = etSocialLike.getText().toString();
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
           else if(refer.equalsIgnoreCase("Select you prefer")){
                toast("Please select you prefer");
            }
            else if(profession.equalsIgnoreCase("Please select profession")){
                toast("Please select profession");
            }
            /*else if(.equalsIgnoreCase("Please select industry")){
                toast("Please select industry");
            }*/
            else {
               /* if(citySelector==null)
                    toast("something is wrong");
                else{
                    location = citySelector.getSelectedItem();}*/
                BoUser user = new BoUser();
                user.setUserId(userId);
                user.setEmail(email);

                user.setContact(contactNo);
                user.setFirstName(fName);
                user.setLastName(lName);
                user.setCountry("India");

                if (allUploadedUri.size() > 0) {
                    user.setProfileImage(allUploadedUri.get(0));

                }
                else {
                    user.setProfileImage(Pref.Read(this, Const.PREF_USE_IMAGE_PATH));

                }
                user.setDob("");
                user.setHandicap(handicap);
                user.setStrength("");
                user.setWeakness("");
                user.setDeviceId(toke);
                user.setImeiNo(imei);
                user.setDeviceType(Const.DEVICE_TYPE);
                user.setUserType(userType);
                user.setClubName(club);
                user.setAge(age);
                user.setDescription(description);
                user.setAddress(address);
                user.setCity(city);
                user.setSubRub(subrub);
                user.setOperatingHours(operatingHours);
                user.setSex(sex);
                user.setAffiliated(affliate);
                user.setProfession(profession);
                user.setRoundsPerMonth(round);
                user.setRefer(refer);
                user.setPlayWithUs(like);
                user.setPlayWithOther(sociallike);
                user.setLocation(location);
                user.setCourse(corsesval);
                user.setNoOfHandicap(handicapCount);
                updateProfile(user);
            }

        }
    }

    private void updateProfile(BoUser user) {
        showProgressDialog("Updating", "Please wait...");

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = service.updateProfile(user.getUserId(), user);
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

    @OnClick(R.id.etSex)
    public void selGneder() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Male");
        arrayAdapter.add("Female");
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strGender = arrayAdapter.getItem(which);
                        etSex.setText(strGender);
                    }
                });
        AlertDialog dialog = builderSingle.create();
        dialog.show();
    }

    @OnClick(R.id.etLike)
    public void selLikeChoice() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Yes");
        arrayAdapter.add("No");
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strGender = arrayAdapter.getItem(which);
                        etLike.setText(strGender);
                    }
                });
        final AlertDialog dialog = builderSingle.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    @OnClick(R.id.etSocialLike)
    public void selSLikeChoice() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Yes");
        arrayAdapter.add("No");
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strGender = arrayAdapter.getItem(which);
                        etSocialLike.setText(strGender);
                    }
                });
        final AlertDialog dialog = builderSingle.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }


    private void affliate() {
        // Spinner Drop down elements
        affliateList = new ArrayList<String>();
        affliateList.add("No");
        affliateList.add("Yes");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, affliateList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spAffiliate.setAdapter(dataAdapter);
        spAffiliate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                affliateval = affliateList.get(position).toString();
                if (affliateval.equalsIgnoreCase("yes")) {
                    llCousre.setVisibility(View.VISIBLE);
                } else if (affliateval.equalsIgnoreCase("No")) {
                    llCousre.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void handicap() {
        // Spinner Drop down elements
        handicapList = new ArrayList<String>();
        handicapList.add("No");
        handicapList.add("Yes");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, handicapList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spHandicap.setAdapter(dataAdapter);
        spHandicap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handicapval = handicapList.get(position).toString();
                if (handicapval.equals("Yes")) {
                    llhandicap.setVisibility(View.VISIBLE);
                } else {
                    llhandicap.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void preferOption() {
        // Spinner Drop down elements
        referList = new ArrayList<String>();
        referList.add("Select you prefer");
        referList.add("Walking");
        referList.add("Taking golf cart");
        referList.add("Both");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, referList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spRefer.setAdapter(dataAdapter);
        spRefer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                referval = referList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getCorses() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        IApiService apiService = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = apiService.getAllClub(userId);
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindClubData(pojo.getAllItems());

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong while fetching corses");
                }
            }


            @Override
            public void onFailure(Call<PojoUser> call, Throwable t) {
                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void bindClubData(ArrayList<BoUser> allItems) {
        for (int i = 0; i < allItems.size(); i++) {
            course.add(allItems.get(i).getClubName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, course);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spCourses.setAdapter(dataAdapter);
        spCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                corsesval = course.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getLocation() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllRegions();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {

                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setLocation(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                        toast(pojoUser.getMessage());
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                        toast(pojoUser.getMessage());
                    }
                } else {
                    toast("Something went wrong while fetching locations");
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {

                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }


    private void getProfession() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllProfession();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {

                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setProfession(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                        toast(pojoUser.getMessage());
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                        toast(pojoUser.getMessage());
                    }
                } else {
                    toast("Something went wrong while fetching professions");
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {

                Common.logException(getApplicationContext(), "Internal server error", t, null);
            }
        });
    }

    private void setProfession(ArrayList<BoDropVales> allItems) {
         List<String>  professionList = new ArrayList<String>();
        professionList.add("Professions");
        for (int i = 0; i < allItems.size(); i++) {
            professionList.add(allItems.get(i).getDisplayName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, professionList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spProfession.setAdapter(dataAdapter);
        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                professionval = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setLocation(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Region");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        etLocation.setAdapter(dataAdapter);
        etLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
