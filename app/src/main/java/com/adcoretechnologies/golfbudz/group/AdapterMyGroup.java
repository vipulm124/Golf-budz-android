package com.adcoretechnologies.golfbudz.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adcore on 2/17/2017.
 */

public class AdapterMyGroup extends
        RecyclerView.Adapter<AdapterMyGroup.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoGroup> allItems;

    public AdapterMyGroup(ArrayList<BoGroup> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterMyGroup.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_invite_friend,
                parent, false);

        AdapterMyGroup.ViewHolder vh = new AdapterMyGroup.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterMyGroup.ViewHolder holder, final int position) {
        final BoGroup item = getItem(position);

       /* holder.rlFriend.setOnClickListener(new View.OnClickListener() {
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
*/
        holder.tvUsername.setText(item.getTitle());
        Common.showRoundImage(context, holder.ivPic, item.getImage());

    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoGroup getItem(int position) {
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