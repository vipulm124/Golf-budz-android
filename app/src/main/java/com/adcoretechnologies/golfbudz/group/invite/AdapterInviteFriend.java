package com.adcoretechnologies.golfbudz.group.invite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.friends.BoFriend;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/17/2017.
 */

public class AdapterInviteFriend extends
        RecyclerView.Adapter<AdapterInviteFriend.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoFriend> allItems;

    public AdapterInviteFriend(ArrayList<BoFriend> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterInviteFriend.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_invite_friend,
                parent, false);

        AdapterInviteFriend.ViewHolder vh = new AdapterInviteFriend.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterInviteFriend.ViewHolder holder, final int position) {
        final BoFriend item = getItem(position);

        holder.rlFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    item.setSelected(!item.isSelected());
                if (item.isSelected()) {
                    holder.rlFriend.setBackgroundResource(R.drawable.bg_yellow_trans);

                } else {
                    holder.rlFriend.setBackgroundResource(R.drawable.bg_black_filter);
                }
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_FRIEND_ITEM_CLICK,position,item.getUserId()));

            }
        });

        holder.tvUsername.setText(item.getFirstName());
        Common.showRoundImage(context, holder.ivPic, item.getProfileImage());

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



        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.ivPic)
        ImageView ivPic;

        @BindView(R.id.rlFriend)
        RelativeLayout rlFriend;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}