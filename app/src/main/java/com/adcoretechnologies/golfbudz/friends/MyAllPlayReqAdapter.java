package com.adcoretechnologies.golfbudz.friends;

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
import com.adcoretechnologies.golfbudz.playrequest.model.BoPlay;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/20/2017.
 */

public class MyAllPlayReqAdapter extends
        RecyclerView.Adapter<MyAllPlayReqAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoPlay> allItems;

    public MyAllPlayReqAdapter(ArrayList<BoPlay> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAllPlayReqAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_joinplayrequest,
                parent, false);

        MyAllPlayReqAdapter.ViewHolder vh = new MyAllPlayReqAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyAllPlayReqAdapter.ViewHolder holder, final int position) {
        final BoPlay item = getItem(position);


        holder.tvName.setText(item.getUserName());
        Common.showBigImage(context, holder.ivPic, item.getUserImgUrl());
        holder.tvDesc.setText(item.getRequestInfo());
        holder.llJoinRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_PLAY_REQ_SEND_CLICK,position,"",item));
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


        @BindView(R.id.llJoinRequest)
        LinearLayout llJoinRequest;
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
