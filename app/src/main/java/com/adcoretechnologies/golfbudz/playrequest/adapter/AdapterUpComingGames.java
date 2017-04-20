package com.adcoretechnologies.golfbudz.playrequest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.home.model.BoService;
import com.adcoretechnologies.golfbudz.playrequest.RequestDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rehan on 1/14/2017.
 */

public class AdapterUpComingGames extends
        RecyclerView.Adapter<AdapterUpComingGames.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoService> allItems;

    public AdapterUpComingGames(ArrayList<BoService> allItems) {
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
        final BoService item = getItem(position);


       // holder.tvMessage.setText(item.getServiceDescription());
        //Common.showBigImage(context, holder.ivServiceImage, item.getImagePath());
        holder.llUpCominglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, RequestDetailActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoService getItem(int position) {
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
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}

