package com.adcoretechnologies.golfbudz.blog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 1/14/2017.
 */

public class BlogAdapter extends
        RecyclerView.Adapter<BlogAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoBlogs> allItems;

    public BlogAdapter(ArrayList<BoBlogs> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BlogAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_blog,
                parent, false);

        BlogAdapter.ViewHolder vh = new BlogAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final BlogAdapter.ViewHolder holder, final int position) {
        final BoBlogs item = getItem(position);

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_BLOG_ITEM_CLICK,position,"",item));
            }
        });

        holder.tvDate.setText(item.getUpdatedAt());
        holder.tvName.setText(item.getUserName());
        holder.tvDesc.setText(item.getText());
        Common.showBigImage(context, holder.ivPic, item.getUserImgUrl());

        if (item.getPostType().equals("image"))
        {
            Log.e("posted text",item.getPostType() + item.getImage());
            holder.llFeedImages.setVisibility(View.VISIBLE);
            holder.feedTImage1.setVisibility(View.VISIBLE);
            Common.showBigImage(context, holder.feedTImage1, item.getImage());
        }
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoBlogs getItem(int position) {
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

        @BindView(R.id.rlBlog)
        RelativeLayout rlBlog;
        @BindView(R.id.tvView)
        TextView tvView;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivPic)
        ImageView ivPic;
        @BindView(R.id.llFeedImagesBlog)
        LinearLayout llFeedImages;
        @BindView(R.id.feed_blog_image)
        ImageView feedTImage1;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}