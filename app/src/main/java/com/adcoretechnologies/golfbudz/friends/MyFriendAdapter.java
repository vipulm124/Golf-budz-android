package com.adcoretechnologies.golfbudz.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/29/2016.
 */

public class MyFriendAdapter extends
        RecyclerView.Adapter<MyFriendAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoFriend> allItems;

    public MyFriendAdapter(ArrayList<BoFriend> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyFriendAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.myfriend_card_view,
                parent, false);

        MyFriendAdapter.ViewHolder vh = new MyFriendAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyFriendAdapter.ViewHolder holder, final int position) {
        final BoFriend item = getItem(position);

//        Log.e("allow pic",allItems.get(position).getRefId()+"");

        holder.tvName.setText(item.getFirstName() +" "+item.getLastName());

        if(item.getStatus()==null){
            holder.tvStatus.setText(Const.ADD_FRIENDS);
        }
        else if(item.getStatus().equals(Const.ACCEPT)){
            holder.tvStatus.setText(Const.BLOCK);
            item.setStatus(Const.BLOCK);
        }else if(item.getStatus().equals(Const.BLOCK)){
            holder.tvStatus.setText(Const.UNBLOCK);
            item.setStatus(Const.UNBLOCK);
        }else if(item.getStatus().equals(Const.SENT)){
            holder.tvStatus.setText(Const.SENT);
            item.setStatus(Const.SENT);
        }else if(item.getStatus().equals(Const.PENDING)){
            holder.tvStatus.setText(Const.ACCEPT);
            item.setStatus(Const.ACCEPT);
        }

        Log.e("pic",item.getProfileImage()+"");

        Common.showRoundImage(context, holder.ivPic, item.getProfileImage());
        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_FPROFILE_CLICK,position,item.getUserId()));
            }
        });
        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_FUNFRIEND_CLICK,position,"",item));
            }
        });
        holder.tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_FMESSAGECLICK,position,"",item));
            }
        });
        holder.tvSendPlayReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_FSENDREQ_CLICK,position,item.getUserId()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoFriend getItem(int position) {
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


        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvView)
        TextView tvView;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvMsg)
        TextView tvMsg;
        @BindView(R.id.tvSendPlayReq)
        TextView tvSendPlayReq;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
