package com.adcoretechnologies.golfbudz.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/16/2016.
 */

public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoNoti> allItems;

    public NotificationAdapter(ArrayList<BoNoti> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.noti_card_view,
                parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BoNoti item = getItem(position);


        holder.tvMessage.setText(item.getTitle());
        Common.showSmallRoundImage(context, holder.ivPic, item.getUserImgUrl());
        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NOTI_ACCEPT_CLICK,position,"",item));
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                view.findViewById(R.id.ivAccept).startAnimation(shake);
                notifyDataSetChanged();
            }
        });
        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NOTI_CANCEL_CLICK,position,"",item));
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                view.findViewById(R.id.ivCancel).startAnimation(shake);
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoNoti getItem(int position) {
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


        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        @BindView(R.id.ivAccept)
        ImageView ivAccept;
        @BindView(R.id.ivCancel)
        ImageView ivCancel;
        @BindView(R.id.llInput)
        LinearLayout llInput;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
