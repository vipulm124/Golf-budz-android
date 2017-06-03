package com.adcoretechnologies.golfbudz.playrequest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.home.model.BoService;
import com.adcoretechnologies.golfbudz.playrequest.RequestDetailActivity;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 1/14/2017.
 */

public class AdapterUpComingGames extends
        RecyclerView.Adapter<AdapterUpComingGames.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoPlay> allItems;

    public AdapterUpComingGames(ArrayList<BoPlay> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterUpComingGames.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_upcominggames,
                parent, false);

        AdapterUpComingGames.ViewHolder vh = new AdapterUpComingGames.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterUpComingGames.ViewHolder holder, final int position) {
        final BoPlay item = getItem(position);


        holder.tvName.setText(item.getUserName());
        Common.showRoundImage(context, holder.ivPic, item.getUserImgUrl());
        holder.tvDesc.setText(item.getRequestInfo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_PLAY_REQ_UPCOMING_CLICK,position,"",item));
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

    private void log(String message) {


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

        @BindView(R.id.llUpCominglayout)
        LinearLayout llUpCominglayout;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}

