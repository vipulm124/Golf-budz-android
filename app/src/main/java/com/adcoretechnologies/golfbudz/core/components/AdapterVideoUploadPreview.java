package com.adcoretechnologies.golfbudz.core.components;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 2/6/2017.
 */

public class AdapterVideoUploadPreview extends
        RecyclerView.Adapter<AdapterVideoUploadPreview.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE_EMPTY = 1;
    private static final int VIEW_TYPE_IMAGE_UPLOAD = 0;
    Context context;

    private List<Uri> allItems;

    public AdapterVideoUploadPreview(List<Uri> allItems) {
        this.allItems = allItems;
    }

    @Override
    public AdapterVideoUploadPreview.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        context = parent.getContext();
        View v = null;

        if (viewType == VIEW_TYPE_IMAGE_EMPTY) {
            v = LayoutInflater.from(context).inflate(R.layout.item_posts_video,
                    parent, false);
        } else if (viewType == VIEW_TYPE_IMAGE_UPLOAD) {
            v = LayoutInflater.from(context).inflate(R.layout.ga,
                    parent, false);
        }

        AdapterVideoUploadPreview.ViewHolder vh = new AdapterVideoUploadPreview.ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterVideoUploadPreview.ViewHolder holder, final int position) {

        if (getItemViewType(position) == VIEW_TYPE_IMAGE_EMPTY) {
            final Uri item = getItem(position);
            holder.videoView.setVideoURI(item);
            holder.videoView.start();
            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_POST_IMAGE_UPLOAD_REMOVE, position));
                }
            });
        } else {
            holder.linearLayoutVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_POST_VIDEO_UPLOAD, 0));
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return allItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == allItems.size())
            return VIEW_TYPE_IMAGE_UPLOAD;
        else return VIEW_TYPE_IMAGE_EMPTY;
    }

    public Uri getItem(int position) {
        return allItems.get(position);
    }

    private void log(String message) {
        Log.d(Const.DEBUG_TAG, getClass().getSimpleName() + " :" + message);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivVideo)
        @Nullable
        VideoView videoView;

        @BindView(R.id.ivRemove)
        @Nullable
        ImageView ivRemove;

        @BindView(R.id.previewVideo)
        @Nullable
        LinearLayout linearLayoutVideo;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
