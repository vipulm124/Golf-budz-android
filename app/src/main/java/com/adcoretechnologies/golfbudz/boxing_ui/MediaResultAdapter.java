package com.adcoretechnologies.golfbudz.boxing_ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Adcore on 6/17/2017.
 */

public class MediaResultAdapter extends RecyclerView.Adapter {
    private ArrayList<BaseMedia> mList;

    public MediaResultAdapter() {
        mList = new ArrayList<>();
    }

    public void setList(List<BaseMedia> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    public void clear(List<BaseMedia> list) {
        if (list == null) {
            return;
        }
        mList.clear();
       notifyDataSetChanged();
    }
    List<BaseMedia> getMedias() {
        if (mList == null || mList.size() <= 0 || !(mList.get(0) instanceof ImageMedia)) {
            return null;
        }
        return mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_boxing_simple_media_item, parent, false);
        int height = parent.getMeasuredHeight() / 4;
        view.setMinimumHeight(height);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MediaViewHolder) {
            MediaViewHolder mediaViewHolder = (MediaViewHolder) holder;
            mediaViewHolder.mImageView.setImageResource(BoxingManager.getInstance().getBoxingConfig().getMediaPlaceHolderRes());
            BaseMedia media = mList.get(position);
            final String path;
            if (media instanceof ImageMedia) {
                path = ((ImageMedia) media).getThumbnailPath();
            } else {
                path = media.getPath();
            }
            BoxingMediaLoader.getInstance().displayThumbnail(mediaViewHolder.mImageView, path, 150, 150);
            mediaViewHolder.itemView.setTag(position);
            mediaViewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_POST_IMAGE_UPLOAD_REMOVE, position));
                }
            });
            mediaViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_POST_IMAGE_UPLOAD_EDIT, position,path));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

}

class MediaViewHolder extends RecyclerView.ViewHolder {
   public ImageView mImageView;
    public ImageView ivRemove;
    public ImageView ivEdit;
   MediaViewHolder(View itemView) {
       super(itemView);
       mImageView = (ImageView) itemView.findViewById(R.id.media_item);
       ivRemove = (ImageView) itemView.findViewById(R.id.ivRemove);
       ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
   }
}