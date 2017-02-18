package com.golf.budz.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.golf.budz.core.base.BoEventData;
import com.golf.budz.event.EventListAdapter;
import com.golf.budz.home.model.BoPost;
import com.golf.budz.home.model.BoService;
import com.golf.budz.model.Notification;
import com.golf.budz.utils.Common;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static android.media.ThumbnailUtils.createVideoThumbnail;

/**
 * Created by Rehan on 11/29/2016.
 */

public class NewsFeedAdapter extends
        RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> implements View.OnClickListener {
    Context context;

    String userId;
    private ArrayList<BoPost> allItems;

    public NewsFeedAdapter(ArrayList<BoPost> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.newsfed_cardview,
                parent, false);
         userId = Pref.Read(context, Const.PREF_USER_ID);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BoPost item = getItem(position);
        holder.tvName.setText(item.getUserName());

        holder.tvDescription.setText(item.getText());
        holder.tvLike.setText(item.getLikeCount() + " Like");
        holder.tvComment.setText(item.getCommentCount() + " Comment");
        holder.tvDate.setText(item.getCreatedAt());
       ArrayList<String> likeUsers= item.getLikes();
        if (likeUsers.contains(userId)) {
            holder.ivLiked.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.GONE);
            holder.llLike.setClickable(false);
        }
        if (item.getPostType().equals(Const.IMAGE)) {
            holder.feedImage1.setVisibility(View.VISIBLE);
            try {
                String[] parts = item.getImage().split("\\|");
                Common.showBigImage(context, holder.feedImage1, parts[0]);
            } catch (Exception e) {
                holder.feedImage1.setVisibility(View.GONE);
            }
            Common.showBigImage(context, holder.ivProfilepic, item.getUserImgUrl());

        } else if (item.getPostType().equals(Const.VIDEO)) {
            holder.ivVideo.setVisibility(View.VISIBLE);
            //specify the location of media file
            Uri uri = Uri.parse(item.getVideo());

            holder.ivVideo.setVideoURI(uri);
            holder.ivVideo.requestFocus();
            holder.ivVideo.start();


        } else {
            holder.feedImage1.setVisibility(View.GONE);
            holder.ivVideo.setVisibility(View.GONE);
        }

        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_SHARE_CLICK,position,"",item));
            }
        });
        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_COMMENT_CLICK,position,item.get_id()));
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isLikeStatus()==false)
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_LIKE_CLICK,position,"false",item));
                else{
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_LIKE_CLICK,position,"true",item));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoPost getItem(int position) {
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

        @BindView(R.id.ivProfilepic)
        ImageView ivProfilepic;
        @BindView(R.id.tvName)

        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvLike)
        TextView tvLike;
        @BindView(R.id.tvComment)
        TextView tvComment;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.feedImage1)
        ImageView feedImage1;
        @BindView(R.id.ivVideo)
        VideoView ivVideo;

        @BindView(R.id.llLike)
        LinearLayout llLike;
        @BindView(R.id.llComment)
        LinearLayout llComment;
        @BindView(R.id.llShare)
        LinearLayout llShare;
        @BindView(R.id.ivLike)
        ImageView ivLike;
        @BindView(R.id.ivLiked)
        ImageView ivLiked;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
