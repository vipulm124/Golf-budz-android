package com.adcoretechnologies.golfbudz.blog;

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
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/20/2017.
 */

public class AdapterOnCourse extends
        RecyclerView.Adapter<AdapterOnCourse.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoBlog> allItems;

    public AdapterOnCourse(ArrayList<BoBlog> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterOnCourse.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_blog,
                parent, false);

        AdapterOnCourse.ViewHolder vh = new AdapterOnCourse.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterOnCourse.ViewHolder holder, final int position) {
        final BoBlog item = getItem(position);

        holder.rlBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_BLOG_ITEM_CLICK,position,"",item));
            }
        });
        holder.tvDate.setText(item.getShortText());
        holder.tvName.setText(item.getTitle());
        holder.tvDesc.setText(item.getShortText());
        Common.showBigImage(context, holder.ivPic, item.getUserImgUrl());

    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoBlog getItem(int position) {
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
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivPic)
        ImageView ivPic;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}