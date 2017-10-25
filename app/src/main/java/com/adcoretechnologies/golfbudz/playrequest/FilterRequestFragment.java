package com.adcoretechnologies.golfbudz.playrequest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.playrequest.model.BoDropVales;
import com.adcoretechnologies.golfbudz.playrequest.model.FilterRequest;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoDropValues;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adcore on 4/20/2017.
 */

public class FilterRequestFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.spType)
    Spinner spType;
    @BindView(R.id.spAffliate)
    Spinner spAffliate;
    @BindView(R.id.spRegion)
    Spinner spRegion;
    @BindView(R.id.spIndustry)
    Spinner spIndustry;
    @BindView(R.id.spProfession)
    Spinner spProfession;
    @BindView(R.id.spClub)
    Spinner spClub;
    @BindView(R.id.btnApply)
    Button btnApply;

    String status = "", fromDate = "", toDate = "";
    @BindView(R.id.etNohandicap)
    EditText etNohandicap;
    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.rlAffiliate)
    RelativeLayout rlAffiliate;
    @BindView(R.id.rlRegion)
    RelativeLayout rlRegion;
    @BindView(R.id.rlIndustry)
    RelativeLayout rlIndustry;
    @BindView(R.id.rlProfession)
    RelativeLayout rlProfession;
    @BindView(R.id.rlClub)
    RelativeLayout rlClub;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;

    public static FilterRequestFragment getInstance() {
        return new FilterRequestFragment();
    }

    View view;
    String selTpe = "", selValue = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.bottomsheet_request, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        setFilterType();
        setAffliates();
        getIndustry();
        getProfession();
        getLocation();
        getVenues();
    }

    @OnClick(R.id.etDate)
    public void addDateForFilter()
    {
        datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);
        datePickerDialog.setThemeDark(false);
        datePickerDialog.showYearPickerFirst(false);
        datePickerDialog.setAccentColor(Color.parseColor("#009688"));
        datePickerDialog.setTitle("Select Date From Golfing Budz");
        final Calendar c = Calendar.getInstance();
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialogto");
        datePickerDialog.setMinDate(c);
    }

    @OnClick(R.id.btnApply)
    public void filterApply() {

        if (selTpe.equals("Date")) {
            String selValue = etDate.getText().toString();
            if (TextUtils.isEmpty(selValue)) {
                Toast.makeText(getActivity(), "Please the date", Toast.LENGTH_SHORT).show();
                return;
            }}else
        if (selTpe.equals("Handicap")) {
            String selValue=etNohandicap.getText().toString();
        if (TextUtils.isEmpty(selValue)) {
            Toast.makeText(getActivity(), "Please enter no. of handicap", Toast.LENGTH_SHORT).show();
            return;
        }}else
        if (selValue.equalsIgnoreCase("Select Region")) {
            Toast.makeText(getActivity(), "Please select region", Toast.LENGTH_SHORT).show();
            return;
        }
       else if (selValue.equalsIgnoreCase("Select Industry")) {
            Toast.makeText(getActivity(), "Please select industry", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selValue.equalsIgnoreCase("Select Profession")) {
            Toast.makeText(getActivity(), "Please select profession", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selValue.equalsIgnoreCase("Select Golf course")) {
            Toast.makeText(getActivity(), "Please select golf course", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selValue.equalsIgnoreCase("Select Affiliated")) {
            Toast.makeText(getActivity(), "Please select affiliated", Toast.LENGTH_SHORT).show();
            return;
        }
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setType(selTpe);
        filterRequest.setValue(selValue);
        EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_REQUEST_FILTER_APPLY, 0, filterRequest));
    }

    private void getIndustry() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllIndustry();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setIndustry(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
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
        spIndustry.setAdapter(dataAdapter);

        spIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selValue = parent.getItemAtPosition(position).toString();
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
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setProfession(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
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
        spProfession.setAdapter(dataAdapter);
        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selValue = parent.getItemAtPosition(position).toString();
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
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setLocation(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
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
        spRegion.setAdapter(dataAdapter);
        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selValue = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setFilterType() {
        final List<String> typeList = new ArrayList<>();
        typeList.add("Date");
        typeList.add("Handicap");
        typeList.add("Affiliated");
        typeList.add("Region");
        typeList.add("Golf course");
        typeList.add("Industry");
        typeList.add("Profession");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, typeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(dataAdapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTpe = typeList.get(position);
                if (selTpe.equals("Date")){
                    etDate.setVisibility(View.VISIBLE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.GONE);
                }
              else  if (selTpe.equals("Handicap")) {
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.VISIBLE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.GONE);

                }else  if (selTpe.equals("Affiliated")) {
                    etNohandicap.setText("");
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.VISIBLE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.GONE);

                }else  if (selTpe.equals("Region")) {
                    etNohandicap.setText("");
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.VISIBLE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.GONE);

                }
                else  if (selTpe.equals("Golf course")) {
                    etNohandicap.setText("");
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.VISIBLE);

                }
                else  if (selTpe.equals("Industry")) {
                    etNohandicap.setText("");
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.VISIBLE);
                    rlProfession.setVisibility(View.GONE);
                    rlClub.setVisibility(View.GONE);

                }
                else  if (selTpe.equals("Profession")) {
                    etNohandicap.setText("");
                    etDate.setVisibility(View.GONE);
                    etNohandicap.setVisibility(View.GONE);
                    rlAffiliate.setVisibility(View.GONE);
                    rlRegion.setVisibility(View.GONE);
                    rlIndustry.setVisibility(View.GONE);
                    rlProfession.setVisibility(View.VISIBLE);
                    rlClub.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                selValue = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getVenues() {
        IApiService service = APIHelper.getAppServiceMethod();
        Call<PojoDropValues> call = service.getAllVenues();
        call.enqueue(new Callback<PojoDropValues>() {
            @Override
            public void onResponse(Call<PojoDropValues> call, Response<PojoDropValues> response) {
                if (response.isSuccessful()) {
                    PojoDropValues pojoUser = response.body();
                    if (pojoUser.getStatus() == Const.STATUS_SUCCESS) {
                        setVenues(pojoUser.getAllItems());
                    } else if (pojoUser.getStatus() == Const.STATUS_FAILED) {
                    } else if (pojoUser.getStatus() == Const.STATUS_ERROR) {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<PojoDropValues> call, Throwable t) {
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });

    }

    private void setVenues(ArrayList<BoDropVales> allItems) {
        List<String> categories = new ArrayList<String>();
        categories.add("Select Golf course");
        for (int i = 0; i < allItems.size(); i++) {
            categories.add(allItems.get(i).getDisplayName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner_dropdown, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spClub.setAdapter(dataAdapter);
        spClub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selValue = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {

        String date = day + "-" + (month + 1) + "-" + year;
        etDate.setText(date);
    }
}
