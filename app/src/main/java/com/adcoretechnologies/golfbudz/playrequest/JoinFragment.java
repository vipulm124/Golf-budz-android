package com.adcoretechnologies.golfbudz.playrequest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseFragment;
import com.adcoretechnologies.golfbudz.playrequest.adapter.AdapterJoinPlayRequest;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rehan on 1/14/2017.
 */

public class JoinFragment extends BaseFragment {
    @BindView(R.id.pbStatus)
    ProgressBar pbStatus;
    @BindView(R.id.ivStatus)
    ImageView ivStatus;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.llStatus)
    LinearLayout llStatus;
    private AdapterJoinPlayRequest adapter;

    private ArrayList<BoPlay> allItems;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    public JoinFragment() {
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
        View view = inflater.inflate(R.layout.fragment_join, null);

        ButterKnife.bind(this, view);

        init();
        return view;
    }

    @Override
    public void init() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        allItems = new ArrayList<>();
        adapter = new AdapterJoinPlayRequest(allItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        updateViews(allItems.size());
        fillData();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.ivSearch)
    public void onSearch() {
        onSearch(etSearch.getText().toString());
    }

    IApiService apiService;

    public void fillData() {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = apiService.getAllPlayReq(userId);
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                updateViews(0);
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }

    private void updateViews(int size) {
        if (size == 0) {
            tvStatus.setText("No data avilable");
            pbStatus.setVisibility(View.GONE);
            ivStatus.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            llStatus.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoPlay> allItems) {
        adapter = new AdapterJoinPlayRequest(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
    }

    public void onSearch(String text) {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);
        String userName = Pref.Read(getActivity(), Const.PREF_USER_DISPLAY_NAME);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();

        Call<PojoPlay> call = apiService.getSearchJoinReq(text);
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                updateViews(0);
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }

    @Override
    public void log(String message) {

    }
    @OnClick(R.id.fab)
    public void onClick(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Filter By:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Handicap");
        arrayAdapter.add("Industry");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                if(strName.equals("Handicap"))
                filterData(strName);
                else
                    filterIndustryData(strName);
                dialog.dismiss();

            }
        });
        builderSingle.show();
    }
    public void filterData(String filterByHandicap) {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);

            apiService = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = apiService.getAllFilterPlayReq(userId,"yes","");
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                updateViews(0);
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }
    public void filterIndustryData(String filterByHandicap) {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);

        apiService = APIHelper.getAppServiceMethod();
        Call<PojoPlay> call = apiService.getAllFilterPlayReq(userId,"","hindustan");
        call.enqueue(new Callback<PojoPlay>() {
            @Override
            public void onResponse(Call<PojoPlay> call, Response<PojoPlay> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoPlay pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        //toast(pojo.getMessage());
                        bindData(pojo.getAllItems());
                        // updateCategories(position);

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                        //toast(pojo.getMessage());
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoPlay> call, Throwable t) {
                updateViews(0);
                Common.logException(getActivity(), "Internal server error", t, null);
            }
        });
    }
}
