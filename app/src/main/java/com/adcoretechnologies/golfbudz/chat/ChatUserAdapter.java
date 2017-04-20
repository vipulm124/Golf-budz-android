package com.adcoretechnologies.golfbudz.chat;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rehan on 12/20/2016.
 */

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {


    private List<Post> allUsers;
    Context comtext;
    String name;
    public ChatUserAdapter(List<Post> user, Activity comtext) {
        this.allUsers = user;
        this.comtext=comtext;
        name = Pref.Read(comtext, Const.PREF_USER_DISPLAY_NAME);
    }

    @Override
    public ChatUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_layout, viewGroup, false);
        return new ChatUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatUserAdapter.ViewHolder viewHolder, final int i) {
        final Post item = getItem(i);
        setAlignment(viewHolder, item.getFrom());
        viewHolder.txtMessage.setText(item.getText());
        viewHolder.txtInfo.setText(item.getPostedOn());


    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public Post getItem(int position) {
        return allUsers.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.txtInfo)
        TextView txtInfo;
        @BindView(R.id.contentWithBackground)
        LinearLayout contentWithBG;
        @BindView(R.id.content)
        LinearLayout content;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
    private void setAlignment(ViewHolder holder, String from) {
        if (from.equals(name)) {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }
}
