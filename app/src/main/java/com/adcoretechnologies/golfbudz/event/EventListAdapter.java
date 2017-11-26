package com.adcoretechnologies.golfbudz.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/16/2016.
 */

public class EventListAdapter extends
        RecyclerView.Adapter<EventListAdapter.ViewHolder> implements View.OnClickListener {
    Context context;

    String userId;
    private ArrayList<BoEvent> allItems;
    List<String> items = new ArrayList<>();

    public EventListAdapter(ArrayList<BoEvent> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.event_cardview,
                parent, false);

        EventListAdapter.ViewHolder vh = new EventListAdapter.ViewHolder(v);
        userId = Pref.Read(context, Const.PREF_USER_ID);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final EventListAdapter.ViewHolder holder, final int position) {
        final BoEvent item = getItem(position);
        String images = item.getImage();

        holder.tvName.setText(item.getTitle());
        holder.tvDescription.setText(item.getDescription());
        holder.tvDate.setText(item.getCreatedAt());
        holder.tvLike.setText(item.getLikeCount() + " Like");

        ArrayList<String> likeUsers = item.getLikes();
        if (likeUsers.contains(userId)) {
            holder.ivLiked.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.GONE);
            holder.llLike.setClickable(false);
        }
        try {
            String[] parts = images.split("\\|");
       /* for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals(item.getImage())) {
                items.add(parts[i]);

            }
        }*/

            Common.showBigImage(context, holder.ivEvent, parts[0]);
        } catch (Exception e) {
        }

        holder.rlCardEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Code to Connect the Event Detail Activity
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_SERVICE_CLICK, position, item.get_id()));
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  performLike(item.get_id(),holder);
                if (item.isLikeStatus() == false)
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_LIKE_CLICK, position, "false", item));
                else {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_LIKE_CLICK, position, "true", item));;
                }

            }
        });
        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_SHARE_CLICK, position, "", item));
            }
        });
        holder.llAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isAttendingStatus() == false) {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_ATTENDING_CLICK, position, "false", item));
                    item.attendingStatus = true;
                }
                else {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_EVENT_ATTENDING_CLICK, position, "true", item));
                    item.attendingStatus = false;
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoEvent getItem(int position) {
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


        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.ivEvent)
        ImageView ivEvent;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvLike)
        public TextView tvLike;
        @BindView(R.id.llAttend)
        LinearLayout llAttend;
        @BindView(R.id.llShare)
        LinearLayout llShare;
        @BindView(R.id.llLike)
        LinearLayout llLike;
        @BindView(R.id.rlCardEvent)
        RelativeLayout rlCardEvent;
        @BindView(R.id.ivLike)
        ImageView ivLike;
        @BindView(R.id.ivLiked)
        ImageView ivLiked;
        @BindView(R.id.ivAttend)
        ImageView ivAttend;
        @BindView(R.id.ivAttended)
        ImageView ivAttended;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }

}