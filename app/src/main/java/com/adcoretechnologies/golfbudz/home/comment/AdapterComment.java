package com.adcoretechnologies.golfbudz.home.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adcore on 2/10/2017.
 */

public class AdapterComment extends
        RecyclerView.Adapter<AdapterComment.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoCommnet> allItems;

    public AdapterComment(ArrayList<BoCommnet> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterComment.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_comment,
                parent, false);

        AdapterComment.ViewHolder vh = new AdapterComment.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterComment.ViewHolder holder, final int position) {
        final BoCommnet item = getItem(position);

       /* holder.rlBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_BLOG_ITEM_CLICK,position,"",item));
            }
        });
        holder.tvDate.setText(item.getShortText());*/
        holder.tvUsername.setText(item.getUserName());
        holder.tvtext.setText(item.getComment());
        Common.showBigImage(context, holder.ivPic, item.getUserImgUrl());

    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoCommnet getItem(int position) {
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


        @BindView(R.id.tvtext)
        TextView tvtext;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.ivPic)
        ImageView ivPic;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}