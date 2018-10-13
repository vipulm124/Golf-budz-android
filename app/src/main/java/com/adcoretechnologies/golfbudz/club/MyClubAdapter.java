package com.adcoretechnologies.golfbudz.club;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.BoUser;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/29/2016.
 */

public class MyClubAdapter extends
        RecyclerView.Adapter<MyClubAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoUser> allItems;

    public MyClubAdapter(ArrayList<BoUser> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyClubAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.club_card_view,
                parent, false);

        MyClubAdapter.ViewHolder vh = new MyClubAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyClubAdapter.ViewHolder holder, final int position) {
        final BoUser item = getItem(position);
        holder.tvClubName.setText(item.getClubName());
        holder.tvClubDesc.setText(item.getDescription());
        if(item.getFollowStatus().equalsIgnoreCase("true")) {
        holder.tvJoin.setText("Following");
        }
        else{
            holder.tvJoin.setText("Follow");
        }
        Common.showBigImage(context, holder.ivImg, item.getProfileImage());
         holder.rlclub.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                // context.startActivity(new Intent(context,ClubDetailActivity.class));
                 EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_CLUB_CLICK,position,"",item));
             }
         });
        holder.tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context,JoinClubActivity.class));
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_CLUB_JOIN_CLICK,position,item.getUserId()));
                //holder.tvJoin.setText("Following");

            }
        });


    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoUser getItem(int position) {
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


         @BindView(R.id.rlclub)
         RelativeLayout rlclub;
        @BindView(R.id.tvJoin)
        TextView tvJoin;
        @BindView(R.id.tvClubDesc)
        TextView tvClubDesc;
        @BindView(R.id.tvClubName)
        TextView tvClubName;
        @BindView(R.id.ivImg)
        ImageView ivImg;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
