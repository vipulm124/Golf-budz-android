package com.adcoretechnologies.golfbudz.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.home.model.BoPost;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/29/2016.
 */

public class NewsFeedAdapter extends
        RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> implements View.OnClickListener {
    Context context;

    String userId;
    private ArrayList<BoPost> allItems;
    private List<String> myImgList;
    private StorageReference storageRef;

    public NewsFeedAdapter(ArrayList<BoPost> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        ArrayList<String> likeUsers = item.getLikes();
        if (likeUsers.contains(userId)) {
            holder.ivLiked.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.GONE);
            holder.llLike.setClickable(false);
        }
        Common.showRoundImage(context, holder.ivProfilepic, item.getUserImgUrl());
        if (item.getPostType().equals(Const.IMAGE)) {
            holder.llFeedImages.setVisibility(View.VISIBLE);
            //holder.feedImage1.setVisibility(View.VISIBLE);
            /*try {
                String[] parts = item.getImage().split("\\|");
                Common.showBigImage(context, holder.feedImage1, parts[0]);
            } catch (Exception e) {
                holder.feedImage1.setVisibility(View.GONE);
            }*/
            getPicsURL(item);
            if (myImgList.size() == 1) {
                Common.showBigImage(context, holder.feedImage1, myImgList.get(0));
                holder.feedImage1.setVisibility(View.VISIBLE);
                holder.feedThirdLayout.setVisibility(View.GONE);
                holder.feedSeconLayout.setVisibility(View.GONE);
                holder.feedFourthLayout.setVisibility(View.GONE);
                holder.llVideo.setVisibility(View.GONE);

            } else if (myImgList.size() == 2) {
                Common.showBigImage(context, holder.feedSImage1, myImgList.get(0));
                Common.showBigImage(context, holder.feedSImage2, myImgList.get(1));
                holder.feedImage1.setVisibility(View.GONE);
                holder.feedThirdLayout.setVisibility(View.GONE);
                holder.feedSeconLayout.setVisibility(View.VISIBLE);
                holder.feedFourthLayout.setVisibility(View.GONE);
                holder.llVideo.setVisibility(View.GONE);

            } else if (myImgList.size() == 3) {
                Common.showBigImage(context, holder.feedTImage1, myImgList.get(0));
                Common.showBigImage(context, holder.feedTImage2, myImgList.get(1));
                Common.showBigImage(context, holder.feedTImage3, myImgList.get(2));
                holder.feedFourthLayout.setVisibility(View.GONE);
                holder.feedImage1.setVisibility(View.GONE);
                holder.feedThirdLayout.setVisibility(View.VISIBLE);
                holder.feedSeconLayout.setVisibility(View.GONE);
                holder.llVideo.setVisibility(View.GONE);

            } else if (myImgList.size() > 3) {
                Common.showBigImage(context, holder.feedFImage1, myImgList.get(0));
                Common.showBigImage(context, holder.feedFImage2, myImgList.get(1));
                Common.showBigImage(context, holder.feedFImage3, myImgList.get(2));
                int totalImages = myImgList.size() - 3;
                holder.tvCount.setText("+ " + totalImages);
                holder.feedImage1.setVisibility(View.GONE);
                holder.feedThirdLayout.setVisibility(View.GONE);
                holder.feedSeconLayout.setVisibility(View.GONE);
                holder.feedFourthLayout.setVisibility(View.VISIBLE);
                holder.llVideo.setVisibility(View.GONE);
            }

        } else if (item.getPostType().equals(Const.VIDEO)) {

           /* Bitmap thumb = ThumbnailUtils.createVideoThumbnail(String.valueOf(imageRef),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            holder.ivVideothumb.setImageBitmap(thumb);
*/

            //holder.ivVideo.setVisibility(View.VISIBLE);
            //specify the location of media file
           /* Uri uri = Uri.parse(item.getVideo());
            try {
                holder.ivVideo.setVideoURI(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.ivVideo.pause();*/
            holder.llFeedImages.setVisibility(View.GONE);
            holder.feedImage1.setVisibility(View.GONE);
            holder.feedThirdLayout.setVisibility(View.GONE);
            holder.feedSeconLayout.setVisibility(View.GONE);
            holder.llVideo.setVisibility(View.VISIBLE);
            Common.showBigImage(context, holder.ivVideothumb, item.getThumbUrl());
            holder.ivVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, VideoActivity.class).putExtra("link", item.getVideo()));
                }
            });

        } else {
            holder.llFeedImages.setVisibility(View.GONE);
            holder.feedImage1.setVisibility(View.GONE);
            holder.feedThirdLayout.setVisibility(View.GONE);
            holder.feedSeconLayout.setVisibility(View.GONE);
            holder.llVideo.setVisibility(View.GONE);
        }


        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_SHARE_CLICK, position, "", item));
            }
        });
        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_COMMENT_CLICK, position, item.get_id()));
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isLikeStatus() == false)
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_LIKE_CLICK, position, "false", item));
                else {
                    EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_LIKE_CLICK, position, "true", item));
                }
            }
        });
        holder.llFeedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_POST_IMAGES_CLICK, position, "", item));
            }
        });
        holder.ivProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_FEED_USER_CLICK, position, "", item));
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_FEED_USER_CLICK, position, "", item));
            }
        });
        holder.ivVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, VideoPlayActivity.class).putExtra("link", item.getVideo()));
            }
        });
    }

    private void getPicsURL(BoPost gallery) {
        try {
            myImgList = new ArrayList<String>(Arrays.asList(gallery.getImage().split("\\|")));
        } catch (Exception e) {
            Toast.makeText(context, "No image found", Toast.LENGTH_SHORT).show();
        }
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

        @BindView(R.id.tvCount)
        TextView tvCount;
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
        @BindView(R.id.feedSeconLayout)
        LinearLayout feedSeconLayout;
        @BindView(R.id.feedThirdLayout)
        LinearLayout feedThirdLayout;
        @BindView(R.id.feedFourthLayout)
        LinearLayout feedFourthLayout;
        @BindView(R.id.llFeedImages)
        LinearLayout llFeedImages;
        @BindView(R.id.feedTImage1Blog)
        ImageView feedTImage1;
        @BindView(R.id.feedTImage2)
        ImageView feedTImage2;
        @BindView(R.id.feedTImage3)
        ImageView feedTImage3;
        @BindView(R.id.feedFImage1)
        ImageView feedFImage1;
        @BindView(R.id.feedFImage2)
        ImageView feedFImage2;
        @BindView(R.id.feedFImage3)
        ImageView feedFImage3;
        @BindView(R.id.feedSImage1)
        ImageView feedSImage1;
        @BindView(R.id.feedSImage2)
        ImageView feedSImage2;
        @BindView(R.id.ivVideoPlay)
        ImageView ivVideoPlay;
        @BindView(R.id.ivVideothumb)
        ImageView ivVideothumb;
        @BindView(R.id.llVideo)
        LinearLayout llVideo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
