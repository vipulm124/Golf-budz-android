package com.adcoretechnologies.golfbudz.playrequest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.core.base.BaseFragment;
import com.adcoretechnologies.golfbudz.friends.PojoFriend;
import com.adcoretechnologies.golfbudz.home.MainActivity;
import com.adcoretechnologies.golfbudz.playrequest.model.BoDropVales;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoDropValues;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.radio0)
    RadioButton radio0;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.radioGroupHoles)
    RadioGroup radioGroupHoles;
    @BindView(R.id.etDay)
    EditText etDay;
    @BindView(R.id.etVenue)
    Spinner etVenue;
    @BindView(R.id.etTeeOffTime)
    EditText etTeeOffTime;
    @BindView(R.id.radioSocial)
    RadioButton radioSocial;
    @BindView(R.id.radioBuisness)
    RadioButton radioBuisness;
    @BindView(R.id.radoGroupRound)
    RadioGroup radoGroupRound;
    @BindView(R.id.etPlayrequestInfo)
    EditText etPlayrequestInfo;
    @BindView(R.id.etHandicapCount)
    EditText etHandicapCount;
    @BindView(R.id.radioRefineYes)
    RadioButton radioRefineYes;
    @BindView(R.id.radioRefineNo)
    RadioButton radioRefineNo;
    @BindView(R.id.radioGroupRefine)
    RadioGroup radioGroupRefine;
    @BindView(R.id.radioHandYes)
    RadioButton radioHandYes;
    @BindView(R.id.radioHandNo)
    RadioButton radioHandNo;
    @BindView(R.id.radioGroupHandicap)
    RadioGroup radioGroupHandicap;
    @BindView(R.id.etlocation)
    Spinner etlocation;
    @BindView(R.id.etIndustry)
    Spinner etIndustry;
    @BindView(R.id.etProfession)
    Spinner etProfession;
    @BindView(R.id.spAffliate)
    Spinner spAffliate;
    @BindView(R.id.radioMale)
    RadioButton radioMale;
    @BindView(R.id.radioYes)
    RadioButton radioYes;
    @BindView(R.id.radioGroupGender)
    RadioGroup radioGroupGender;
    @BindView(R.id.radioGroupPlayers)
    RadioGroup radioGroupPlayers;
    @BindView(R.id.radioGroupInvitation)
    RadioGroup radioGroupInvitation;
    @BindView(R.id.radioP1)
    RadioButton radioP1;
    @BindView(R.id.radioP2)
    RadioButton radioP2;
    @BindView(R.id.radioP3)
    RadioButton radioP3;

    @BindView(R.id.radioInviteall)
    RadioButton radioInviteall;
    @BindView(R.id.radioInviteonly)
    RadioButton radioInviteonly;
    /*@BindView(R.id.seekBar1)
    SeekBar seekBar1;*/
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.llRefine)
    LinearLayout llRefine;
    @BindView(R.id.llhandicapCount)
    LinearLayout llhandicapCount;
    View view;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day, Hour, Minute;
    private FragmentActivity myContext;
    String profession, industry, venue, location, selAffiliate;
    RadioButton radioHolesButton, radioRoundButton, radioRefineButton, radioHandicapButton, radioGenderButton,radioInviationButton;
    TimePickerDialog timePickerDialog;
    int age = 16;
    List<String> allUsersList = new ArrayList<>();
    List<String> allUserIdList = new ArrayList<>();
    String[] studentnameArray;
    List<String> mSelectedItems;
    public CreateRequestFragment() {
        // Required empty public constructor
    }
    String reciversId="",showreciversId="",requestType="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_round, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        requestType="Public Invite";
        int selectedHolesId = radioGroupHoles.getCheckedRadioButtonId();
        radioHolesButton = (RadioButton) view.findViewById(selectedHolesId);

        int selectedRoundeId = radoGroupRound.getCheckedRadioButtonId();
        radioRoundButton = (RadioButton) view.findViewById(selectedRoundeId);


        int selectedRefineId = radioGroupRefine.getCheckedRadioButtonId();
        radioRefineButton = (RadioButton) view.findViewById(selectedRefineId);

        int selectedHandicapId = radioGroupHandicap.getCheckedRadioButtonId();
        radioHandicapButton = (RadioButton) view.findViewById(selectedHandicapId);
        radioGroupHandicap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioHandicapButton = (RadioButton) view.findViewById(checkedId);
                if (radioHandicapButton.getText().toString().equals("Yes")) {
                    llhandicapCount.setVisibility(View.VISIBLE);
                } else {
                    llhandicapCount.setVisibility(View.GONE);
                }
            }
        });
        int selectedInviataionId = radioGroupInvitation.getCheckedRadioButtonId();
        radioInviationButton = (RadioButton) view.findViewById(selectedInviataionId);
        radioGroupInvitation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioInviationButton = (RadioButton) view.findViewById(checkedId);
               requestType= radioInviationButton.getText().toString();
                if (radioInviationButton.getText().toString().equals("Public Invite")) {
                 // toast("1");
                } else {
                    openUserlist();
                }
            }
        });

        int selectedPlayersId = radioGroupPlayers.getCheckedRadioButtonId();
        radioP1 = (RadioButton) view.findViewById(selectedPlayersId);

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        radioGenderButton = (RadioButton) view.findViewById(selectedGenderId);
        radioGroupRefine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                            radioRefineButton = (RadioButton) view.findViewById(checkedId);
                                                            if (radioRefineButton.getText().toString().equals("Yes")) {
                                                                llRefine.setVisibility(View.VISIBLE);
                                                            } else {
                                                                llRefine.setVisibility(View.GONE);
                                                            }
                                                        }
                                                    }
        );
        if (radioRefineButton.getText().toString().equals("Yes")) {
            llRefine.setVisibility(View.VISIBLE);
        } else {
            llRefine.setVisibility(View.GONE);
        }
        if (radioHandicapButton.getText().toString().equals("Yes")) {
            llhandicapCount.setVisibility(View.VISIBLE);
        } else {
            llhandicapCount.setVisibility(View.GONE);
        }
        getVenues();
        getIndustry();
        getProfession();
        getLocation();
        setAffliates();
        getAllUsers();
        DiscreteSeekBar discreteSeekBar1 = (DiscreteSeekBar) view.findViewById(R.id.customSeekBar);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                age = value * 1;
                return value * 1;
            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @OnClick(R.id.etDay)
    public void onDateSelection() {
        datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#009688"));
        datePickerDialog.setTitle("Select Date From Golfing Budz");
        final Calendar c = Calendar.getInstance();
        datePickerDialog.show(myContext.getFragmentManager(), "DatePickerDialogto");
        datePickerDialog.setMinDate(c);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int Month, int Day) {
        String date = Day + "-" + (Month + 1) + "-" + year;

        etDay.setText(date);

    }

    @OnClick(R.id.etTeeOffTime)
    public void selTime() {
        timePickerDialog = TimePickerDialog.newInstance(this, Hour, Minute, true);

        timePickerDialog.setThemeDark(false);

        // timePickerDialog.show(false);

        timePickerDialog.setAccentColor(Color.parseColor("#009688"));

        timePickerDialog.setTitle("Select Time From TimePickerDialog");
        timePickerDialog.show(myContext.getFragmentManager(), "TimePickerDialog");


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        etTeeOffTime.setText(hourOfDay + ":" + minute);
    }

    private void getVenues() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllVenues();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setVenues(pojoUser.getAllItems());
                        toast(pojoUser.getMessage());
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
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });

    }

    private void setVenues(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Golf Club");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        etVenue.setAdapter(dataAdapter);
        etVenue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                venue = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getIndustry() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllIndustry();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setIndustry(pojoUser.getAllItems());
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
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });

    }

    private void setIndustry(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Industry");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        etIndustry.setAdapter(dataAdapter);

        etIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                industry = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getProfession() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllProfession();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {
                hideDialog();
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
                    toast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }

    private void setProfession(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Profession");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        etProfession.setAdapter(dataAdapter);
        etProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profession = parent.getItemAtPosition(position).toString();
            }

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
                hideDialog();
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
                    toast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }

    private void setLocation(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Region");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        etlocation.setAdapter(dataAdapter);
        etlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.btnSubmit)
    public void onSubmit() {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);
        String userName = Pref.Read(getActivity(), Const.PREF_USER_DISPLAY_NAME);
        String userImage = Pref.Read(getActivity(), Const.PREF_USE_IMAGE_PATH);


        String day = etDay.getText().toString();
        //String venue = etVenue.getText().toString();
        String teeofftime = etTeeOffTime.getText().toString();
        String playrequest = etPlayrequestInfo.getText().toString();
        // String location = etlocation.getText().toString();
        // String industry = etIndustry.getText().toString();
        //  String profession = etProfession.getText().toString();


        if (TextUtils.isEmpty(day)) {
            etDay.setError("Input Field");
            return;
        }
        if (venue.equalsIgnoreCase("Select Golf Club")) {
            toast("Please select club");
            return;
        }
        if (TextUtils.isEmpty(teeofftime)) {
            etTeeOffTime.setError("Input Field");
            return;
        }
        if (TextUtils.isEmpty(playrequest)) {
            etPlayrequestInfo.setError("Input Field");
            return;
        }
        BoPlay boPlay = new BoPlay();
        boPlay.setDay(day);
        boPlay.setNoOfHoles(radioHolesButton.getText().toString());
        if (radioRefineButton.getText().toString().equals("Yes")) {
            if (location.equalsIgnoreCase("Select Region")) {
                toast("Please select region");
                return;
            }
            if (industry.equalsIgnoreCase("Select Industry")) {
                toast("Please select industry");
                return;
            }
            if (profession.equalsIgnoreCase("Select Profession")) {
                toast("Please select profession");
                return;
            } else if (selAffiliate.equalsIgnoreCase("Select Affiliated")) {
                Toast.makeText(getActivity(), "Please select affiliated", Toast.LENGTH_SHORT).show();
                return;
            }
            boPlay.setGender(radioGenderButton.getText().toString());
            boPlay.setHandicap(radioHandicapButton.getText().toString());
            boPlay.setIndustry(industry);
            boPlay.setLocations(location);
            boPlay.setProfession(profession);
            boPlay.setAge(String.valueOf(age));
            boPlay.setAffiliated(selAffiliate);
            boPlay.setGolfClub(venue);
            boPlay.setNoOfHandicaps(etHandicapCount.getText().toString());
        } else {
            boPlay.setGender("");
            boPlay.setHandicap("");
            boPlay.setIndustry("");
            boPlay.setLocations("");
            boPlay.setProfession("");
            boPlay.setAge(String.valueOf(""));
        }

        boPlay.setRedefineRequest(radioRefineButton.getText().toString());
        boPlay.setRequestInfo(playrequest);
        boPlay.setTeeOffTime(teeofftime);
        boPlay.setType(radioRoundButton.getText().toString());
        boPlay.setUserId(userId);
        boPlay.setUserImgUrl(userImage);
        boPlay.setUserName(userName);
        boPlay.setVenue(venue);
        boPlay.setPlayers(radioP1.getText().toString());
        boPlay.setRequestType(requestType);
        boPlay.setRecieverIds(reciversId);
        performSubmission(boPlay);
    }

    private void setAffliates() {
        final List<String> typeList = new ArrayList<>();
        typeList.add("Select Affiliated");
        typeList.add("Yes");
        typeList.add("No");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, typeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAffliate.setAdapter(dataAdapter);
        spAffliate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selAffiliate = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void performSubmission(BoPlay boPlay) {
        showProgressDialog(getResources().getString(R.string.performing_operation), "Please wait...");

        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = service.craeteRequest(boPlay);
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojoUser = response.body();
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
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });

    }

    private void redirectToNextScreen() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void log(String message) {

    }
    private void getAllUsers() {
        showProgressDialog("Loading", "Please wait");
        IApiService apiService = APIHelper.getAppServiceMethod();
        Call<PojoUser> call = apiService.getAllUsers();
        call.enqueue(new Callback<PojoUser>() {
            @Override
            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoUser pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());

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
            public void onFailure(Call<PojoUser> call, Throwable t) {
                hideDialog();
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }


    private void bindData(ArrayList<BoUser> allItems) {
        for (int i = 0; i < allItems.size(); i++) {
            allUsersList.add(allItems.get(i).getFirstName());
            allUserIdList.add(allItems.get(i).getUserId());
        }
        studentnameArray = allUsersList.toArray(new String[allUsersList.size()]);
    }

    public void openUserlist() {
        mSelectedItems = new ArrayList();
        if(studentnameArray.length==0 ||studentnameArray==null)
        {toast("No users found");}else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Select Users")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(studentnameArray, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        mSelectedItems.add(allUserIdList.get(which));
                                    } else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                        mSelectedItems.remove(Integer.valueOf(allUserIdList.get(which)));
                                    }
                                }
                            })
                    // Set the action buttons
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            getReciversId();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            builder.show();}
    }
    private void getReciversId() {
        for (int i=0;i<mSelectedItems.size();i++){
            reciversId +=  mSelectedItems.get(i)+"|";
           // showreciversId +=  "Users "+mSelectedItems.get(i)+",";
        }
        reciversId = reciversId.substring(0, reciversId.length()-1);

    }
}
