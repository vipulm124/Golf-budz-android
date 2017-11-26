package com.adcoretechnologies.golfbudz.chat;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/9/2017.
 */

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.ViewHolder> {


    private List<MyChat> allUsers;
    Context comtext;
    String name;

    public MyChatAdapter(List<MyChat> user, Activity comtext) {
        this.allUsers = user;
        this.comtext = comtext;

    }

    @Override
    public MyChatAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_mychat, viewGroup, false);
        return new MyChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyChatAdapter.ViewHolder viewHolder, final int i) {
        final MyChat item = getItem(i);

        viewHolder.tvMsg.setText(item.getMessage());
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvDate.setText(item.getPostedOn());

        Common.showRoundImage(comtext, viewHolder.ivPic, item.getImage());

        viewHolder.rlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_CHAT_ITEM_CLICK,i,"",item));

            }
        });

    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public MyChat getItem(int position) {
        return allUsers.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMsg)
        TextView tvMsg;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivPic)
        ImageView ivPic;

        @BindView(R.id.rlChat)
        RelativeLayout rlChat;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}