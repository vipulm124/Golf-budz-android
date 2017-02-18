package com.golf.budz.playrequest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.golf.budz.auth.PojoUser;
import com.golf.budz.core.base.BaseFragment;
import com.golf.budz.home.MainActivity;
import com.golf.budz.home.R;
import com.golf.budz.playrequest.model.BoPlay;
import com.golf.budz.playrequest.model.PojoPlay;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.golf.budz.utils.api.APIHelper;
import com.golf.budz.utils.api.IApiService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRequestFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

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
    @BindView(R.id.radioMale)
    RadioButton radioMale;
    @BindView(R.id.radioYes)
    RadioButton radioYes;
    @BindView(R.id.radioGroupGender)
    RadioGroup radioGroupGender;
    /*@BindView(R.id.seekBar1)
    SeekBar seekBar1;*/
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.llRefine)
    LinearLayout llRefine;
    View view;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day, Hour, Minute;
    private FragmentActivity myContext;
    String profession, industry, venue,location;
    RadioButton radioHolesButton, radioRoundButton, radioRefineButton, radioHandicapButton, radioGenderButton;
    TimePickerDialog timePickerDialog ;
    int age=16;
    public CreateRequestFragment() {
        // Required empty public constructor
    }

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
        int selectedHolesId = radioGroupHoles.getCheckedRadioButtonId();
        radioHolesButton = (RadioButton) view.findViewById(selectedHolesId);

        int selectedRoundeId = radoGroupRound.getCheckedRadioButtonId();
        radioRoundButton = (RadioButton) view.findViewById(selectedRoundeId);


        int selectedRefineId = radioGroupRefine.getCheckedRadioButtonId();
        radioRefineButton = (RadioButton) view.findViewById(selectedRefineId);

        int selectedHandicapId = radioGroupHandicap.getCheckedRadioButtonId();
        radioHandicapButton = (RadioButton) view.findViewById(selectedHandicapId);

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
        getVenues();
        getIndustry();
        getProfession();
        getLocation();

        DiscreteSeekBar discreteSeekBar1 = (DiscreteSeekBar) view.findViewById(R.id.customSeekBar);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
              age=value * 1;
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
        datePickerDialog.setTitle("Select Date From DatePickerDialog");
        datePickerDialog.show(myContext.getFragmentManager(), "DatePickerDialog");
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
        etTeeOffTime.setText(hourOfDay+":"+minute);
    }
    private void getVenues() {
        List<String> categories = new ArrayList<String>();
        categories.add("Delhi");
        categories.add("Agra");
        categories.add("Mumbai");
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
        List<String> categories = new ArrayList<String>();
        categories.add("Delhi");
        categories.add("Agra");
        categories.add("Mumbai");
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
        List<String> categories = new ArrayList<String>();
        categories.add("Club Member");
        categories.add("Business man");
        categories.add("Service man");
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
        List<String> categories = new ArrayList<String>();
        categories.add("USA");
        categories.add("India");
        categories.add("Germany");
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
        if(radioRefineButton.getText().toString().equals("Yes")){
            boPlay.setGender(radioGenderButton.getText().toString());
            boPlay.setHandicap(radioHandicapButton.getText().toString());
            boPlay.setIndustry(industry);
            boPlay.setLocations(location);
            boPlay.setProfession(profession);
            boPlay.setAge(String.valueOf(age));
        }else{
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
        performSubmission(boPlay);
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
}
