package com.adcoretechnologies.golfbudz.playrequest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseFragment;
import com.adcoretechnologies.golfbudz.core.components.FragmentDataLoader;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.BoStatusRequest;
import com.adcoretechnologies.golfbudz.playrequest.model.FilterRequest;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.PojoStatusRequest;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adcore on 5/30/2017.
 */

public class PendingUpComingFragment extends BaseFragment {
      @BindView(R.id.pbStatus)
      ProgressBar pbStatus;
       @BindView(R.id.ivStatus)
       ImageView ivStatus;
       @BindView(R.id.tvStatus)
       TextView tvStatus;
       @BindView(R.id.llStatus)
       LinearLayout llStatus;

    private AdapterPendingUpComingGames adapter;
    private FragmentDataLoader fragmentLoader;
    private ArrayList<BoStatusRequest> allItems;
   @BindView(R.id.recyclerView)
   RecyclerView recyclerView;

    public PendingUpComingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_upcoming_games, null);

        ButterKnife.bind(this, view);

        init();
        return view;
    }

    @Override
    public void init() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        allItems = new ArrayList<>();
        adapter = new AdapterPendingUpComingGames(allItems);
        recyclerView.setLayoutManager(manager);
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(adapter);
         updateViews(allItems.size());
         fillData();
    }

    IApiService apiService;

    public void fillData() {
        String userId = Pref.Read(getActivity(), Const.PREF_USER_ID);
        if (apiService == null)
            apiService = APIHelper.getAppServiceMethod();
        Call<PojoStatusRequest> call = apiService.getAllUpcomingGames(userId);
        call.enqueue(new Callback<PojoStatusRequest>() {
            @Override
            public void onResponse(Call<PojoStatusRequest> call, Response<PojoStatusRequest> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    PojoStatusRequest pojo = response.body();
                    if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                        bindData(pojo.getAllItems());

                    } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        updateViews(0);
                    } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        updateViews(0);
                    }
                } else {
                    toast("Something went wrong");
                    updateViews(0);
                }
            }


            @Override
            public void onFailure(Call<PojoStatusRequest> call, Throwable t) {
                hideDialog();
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
        } else {
            llStatus.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(ArrayList<BoStatusRequest> allItems) {
        adapter = new AdapterPendingUpComingGames(allItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allItems.size());
    }

    @Override
    public void log(String message) {

    }




}