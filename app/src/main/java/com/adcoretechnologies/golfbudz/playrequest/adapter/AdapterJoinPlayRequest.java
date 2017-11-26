package com.adcoretechnologies.golfbudz.playrequest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.adcoretechnologies.golfbudz.R.id.pair_up_join_request;

/**
 * Created by Rehan on 1/14/2017.
 */

public class AdapterJoinPlayRequest extends
        RecyclerView.Adapter<AdapterJoinPlayRequest.ViewHolder> implements View.OnClickListener {
    Context context;


    private static ArrayList<BoPlay> allItems;

    public AdapterJoinPlayRequest(ArrayList<BoPlay> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterJoinPlayRequest.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_joinplayrequest,
                parent, false);

        AdapterJoinPlayRequest.ViewHolder vh = new AdapterJoinPlayRequest.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterJoinPlayRequest.ViewHolder holder, final int position) {
        final BoPlay item = getItem(position);

        holder.tvName.setText(item.getUserName());
        Common.showRoundImage(context, holder.ivPic, item.getUserImgUrl());
        holder.tvDesc.setText(item.getRequestInfo());
        holder.joinDate.setText(item.getDay());
        holder.teaOffTime.setText(item.getTeeOffTime());
        holder.golfRegion.setText(item.getLocations());
        holder.golfClub.setText(item.getGolfClub()
        );
        holder.llJoinRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_PLAY_REQ_CLICK,position,"",item));
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoPlay getItem(int position) {
        return allItems.get(position);
    }

    private void log() {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;


        @BindView(R.id.llJoinRequest)
        LinearLayout llJoinRequest;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        @BindView(R.id.pair_up_join_request)
         TextView pairJoin;
        @BindView(R.id.tvDateJoin)
        TextView joinDate;
        @BindView(R.id.tvTeaOff)
        TextView teaOffTime;
        @BindView(R.id.tvRegion)
        TextView golfRegion;
        @BindView(R.id.tvGolfClub)
        TextView golfClub;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;

        }

    }
}
