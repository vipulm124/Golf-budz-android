package com.adcoretechnologies.golfbudz.friends;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendProfileActivity extends BaseActivity {

    @BindView(R.id.etFrstnme)
    EditText etFrstnme;
    @BindView(R.id.etLstmne)
    EditText etLstmne;
    @BindView(R.id.etEmail)
    EditText etEmail;


/*    @BindView(R.id.btSgnupbtn)
    Button btSgnupbtn;*/

    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.llClubDeatl)
    LinearLayout llClubDeatl;

    boolean checked = false;
    List<String> locationList = new ArrayList<String>();
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
    String    location;
    List<String> professionList;
    List<String> affliateList;
    List<String> handicapList;
    List<String> referList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = FriendProfileActivity.this.getWindow();
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
        getCorses();
        preferOption();
        getProfession();
        allItems = new ArrayList<>();
        allUploadedUri = new ArrayList<>();
        getAllCountries();
        getProfile();
        try {
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Const.FIREBASE_STORAGE_BUCKET_PATH);


        } catch (ClassCastException ex) {
            throw new RuntimeException("Please initilalize firebase.");
        }
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
                etType.setText(user.getUserType());
            }
            etType.setText(user.getUserType());
            etFrstnme.setText(user.getFirstName());
            etLstmne.setText(user.getLastName());
            etEmail.setText(user.getEmail());
            etClubName.setText(user.getClubName());

            etSex.setText(user.getSex());
            etAge.setText(user.getAge());
            int index=professionList.indexOf(user.getProfession());
            spProfession.setSelection(index);

            int indexhandicap=handicapList.indexOf(user.getHandicap());
            spHandicap.setSelection(indexhandicap);
            etHandicapCount.setText(user.getNoOfHandicap());

            int indexrefer=referList.indexOf(user.getRefer());
            spRefer.setSelection(indexrefer);

            int indexaffliate=referList.indexOf(user.getAffiliated());
            spAffiliate.setSelection(indexaffliate);

            int indexLocation=locationList.indexOf(user.getLocation());
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

    private void getProfession() {
        professionList = new ArrayList<String>();
        professionList.add("Buisness man");
        professionList.add("Service man");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spinner_dropdown, professionList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spProfession.setAdapter(dataAdapter);
        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                professionval = professionList.get(position).toString();

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
                    toast("Something went wrong");
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
}
