package com.adcoretechnologies.golfbudz.playrequest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.playrequest.adapter.AdapterUpComingGames;
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.playrequest.model.BoStatusRequest;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 5/30/2017.
 */

public class AdapterPendingUpComingGames extends
        RecyclerView.Adapter<AdapterPendingUpComingGames.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoStatusRequest> allItems;

    public AdapterPendingUpComingGames(ArrayList<BoStatusRequest> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterPendingUpComingGames.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_pending_upcominggames,
                parent, false);

        AdapterPendingUpComingGames.ViewHolder vh = new AdapterPendingUpComingGames.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterPendingUpComingGames.ViewHolder holder, final int position) {
        final BoStatusRequest item = getItem(position);

        holder.tvStatus.setText(item.getStatus());
        holder.tvName.setText(item.getFirstName()+" "+item.getLastName());
        Common.showRoundImage(context, holder.ivPic, item.getProfileImage());
       // holder.tvDesc.setText(item.getRequestInfo());
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_PLAY_REQ_UPCOMING_CLICK,position,"",item));
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoStatusRequest getItem(int position) {
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
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}

