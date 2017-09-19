

package com.adcoretechnologies.golfbudz.boxing_ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.entity.AlbumEntity;
import com.bilibili.boxing.model.entity.impl.ImageMedia;

import java.util.ArrayList;
import java.util.List;


/**
 * Album window adapter.
 *
 * @author ChenSL
 */
public class BoxingAlbumAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String UNKNOW_ALBUM_NAME = "?";

    private int mCurrentAlbumPos;

    private List<AlbumEntity> mAlums;
    private LayoutInflater mInflater;
    private OnAlbumClickListener mAlbumOnClickListener;
    private int mDefaultRes;

    public BoxingAlbumAdapter(Context context) {
        this.mAlums = new ArrayList<>();
        this.mAlums.add(AlbumEntity.createDefaultAlbum());
        this.mInflater = LayoutInflater.from(context);
        this.mDefaultRes = BoxingManager.getInstance().getBoxingConfig().getAlbumPlaceHolderRes();
    }

    public void setAlbumOnClickListener(OnAlbumClickListener albumOnClickListener) {
        this.mAlbumOnClickListener = albumOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(mInflater.inflate(R.layout.layout_boxing_album_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
        albumViewHolder.mCoverImg.setImageResource(mDefaultRes);
        final int adapterPos = holder.getAdapterPosition();
        final AlbumEntity album = mAlums.get(adapterPos);

        if (album != null && album.hasImages()) {
            String albumName = TextUtils.isEmpty(album.mBucketName) ?
                    albumViewHolder.mNameTxt.getContext().getString(R.string.boxing_default_album_name) :album.mBucketName;
            albumViewHolder.mNameTxt.setText(albumName);
            ImageMedia media = (ImageMedia) album.mImageList.get(0);
            if (media != null) {
                BoxingMediaLoader.getInstance().displayThumbnail(albumViewHolder.mCoverImg, media.getPath(), 50, 50);
                albumViewHolder.mCoverImg.setTag(R.string.boxing_app_name, media.getPath());
            }
            albumViewHolder.mLayout.setTag(adapterPos);
            albumViewHolder.mLayout.setOnClickListener(this);
            albumViewHolder.mCheckedImg.setVisibility(album.mIsSelected ? View.VISIBLE : View.GONE);

        } else {
            albumViewHolder.mNameTxt.setText(UNKNOW_ALBUM_NAME);
            albumViewHolder.mSizeTxt.setVisibility(View.GONE);
        }
    }

    public void addAllData(List<AlbumEntity> alums) {
        mAlums.clear();
        mAlums.addAll(alums);
        notifyDataSetChanged();
    }

    public List<AlbumEntity> getAlums() {
        return mAlums;
    }

    public int getCurrentAlbumPos() {
        return mCurrentAlbumPos;
    }

    public void setCurrentAlbumPos(int currentAlbumPos) {
        mCurrentAlbumPos = currentAlbumPos;
    }

    public AlbumEntity getCurrentAlbum() {
        if (mAlums == null || mAlums.size() <= 0) {
            return null;
        }
        return mAlums.get(mCurrentAlbumPos);
    }

    @Override
    public int getItemCount() {
        return mAlums != null ? mAlums.size() : 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.album_layout) {
            if (mAlbumOnClickListener != null) {
                mAlbumOnClickListener.onClick(v, (Integer) v.getTag());
            }
        }
    }

    private static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView mCoverImg;
        TextView mNameTxt;
        TextView mSizeTxt;
        View mLayout;
        ImageView mCheckedImg;

        AlbumViewHolder(final View itemView) {
            super(itemView);
            mCoverImg = (ImageView) itemView.findViewById(R.id.album_thumbnail);
            mNameTxt = (TextView) itemView.findViewById(R.id.album_name);
            mSizeTxt = (TextView) itemView.findViewById(R.id.album_size);
            mLayout = itemView.findViewById(R.id.album_layout);
            mCheckedImg = (ImageView) itemView.findViewById(R.id.album_checked);
        }
    }

    public interface OnAlbumClickListener {
        void onClick(View view, int pos);
    }
}
