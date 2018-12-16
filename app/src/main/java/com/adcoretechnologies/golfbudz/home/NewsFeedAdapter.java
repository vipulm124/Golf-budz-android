package com.adcoretechnologies.golfbudz.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.home.model.BoPost;
import com.adcoretechnologies.golfbudz.home.model.PojoPost;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rehan on 11/29/2016.
 */

public class NewsFeedAdapter extends
        RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> implements View.OnClickListener {
    Context context;

    String userId;
    private static ArrayList<BoPost> allItems;
    private List<String> myImgList;
    private StorageReference storageRef;
    private List<String> userLocalUritoShare = new ArrayList<>();
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
        //holder.tvName.setText(item.getUserName());
        Uri profileImage = Uri.parse(item.getProfileImage());
        ///Common.showBigImage(context, holder.feedImage1, parts[0]);
        Common.showSmallRoundImage(context,holder.ivProfilepic,item.getProfileImage());
        //holder.ivProfilepic.setImageURI(profileImage);
        holder.tvDescription.setText(item.getText());
        holder.tvLike.setText(item.getLikeCount() + " Like");
        holder.tvComment.setText(item.getCommentCount() + " Comment");
        //holder.tvDate.setText(item.getCreatedAt());
        String likeUsers = item.getLikes();
        if (likeUsers != null && likeUsers.contains(userId)) {
          holder.ivLiked.setVisibility(View.VISIBLE);
         holder.ivLike.setVisibility(View.GONE);
        holder.llLike.setClickable(false);
        }
        //Common.showRoundImage(context, holder.ivProfilepic, item.getUserImgUrl());
        if (item.getPostType().equals(Const.IMAGE)) {
            holder.llFeedImages.setVisibility(View.VISIBLE);
            holder.feedImage1.setVisibility(View.VISIBLE);
            try {
                String[] parts = item.getblogImage().split("\\|");
                Common.showBigImage(context, holder.feedImage1, parts[0]);
            } catch (Exception e) {
                holder.feedImage1.setVisibility(View.GONE);
            }
            getPicsURL(item);
           if (myImgList.size() == 1) {
                Common.showBigImage(context, holder.feedImage1, myImgList.get(0));
                holder.feedImage1.setVisibility(View.VISIBLE);
                holder.feedThirdLayout.setVisibility(View.GONE);
                holder.feedSeconLayout.setVisibility(View.GONE);
                holder.feedFourthLayout.setVisibility(View.GONE);
                holder.llVideo.setVisibility(View.GONE);

            }  else if (myImgList.size() == 2) {
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

           Bitmap thumb = ThumbnailUtils.createVideoThumbnail(String.valueOf(""),
                    MediaStore.Images.Thumbnails.MINI_KIND);
            holder.ivVideothumb.setImageBitmap(thumb);
//
//
//            holder.ivVideo.setVisibility(View.VISIBLE);
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
       //     holder.ivVideoPlay.setOnClickListener(new View.OnClickListener() {
       //         @Override
       //        public void onClick(View v) {
       //             context.startActivity(new Intent(context, VideoActivity.class).putExtra("link", item.getVideo()));
       //         }
       //     });

        } else {
            holder.llFeedImages.setVisibility(View.GONE);
            holder.feedImage1.setVisibility(View.GONE);
            holder.feedThirdLayout.setVisibility(View.GONE);
            holder.feedSeconLayout.setVisibility(View.GONE);
            holder.llVideo.setVisibility(View.GONE);
        }

        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, item,context,position);
            }
        });
        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_SHARE_CLICK, position, "", item));
            }
        });
        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_NEWS_COMMENT_CLICK, position, item.getid()));
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getLikeStatus() == "false" || item.getLikeStatus()==null || item.getLikeStatus().isEmpty())
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
                context.startActivity(new Intent(context, VideoPlayActivity.class).putExtra("link", item.getblogImage()));
            }
        });
    }
    public void showMenu(View view, final BoPost boPost, final Context  context,final int pos) {
       PopupMenu menu = new PopupMenu(context, view);
        Menu popupMenu = menu.getMenu();
       /*  if(boPost.getUserId().equals(Pref.Read(context,Const.PREF_USER_ID))){
            // menu.getMenu().getItem(1).setVisible(false);
             popupMenu.findItem(R.id.item_delte).setEnabled(false);
        }else     popupMenu.getItem(1).setEnabled(true);
*/

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item_share:
                        share(boPost);
                        break;
                        // Toast.makeText(context, "under process", Toast.LENGTH_SHORT).show(); break;
                    case R.id.item_delete:
                      deletePost(context,boPost,pos);
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_feed);
        menu.show();
        //if the post is not from the current user, then don't allow that user to delete it. He can still share it
        if(!boPost.getUserId().equals(Pref.Read(context,Const.PREF_USER_ID)))
        {
            menu.getMenu().getItem(1).setVisible(false);
        }
    }
    public void deletePost(Context context, BoPost boPost, final int pos) {
        String userId = Pref.Read(context, Const.PREF_USER_ID);
        if(userId!=null && !userId.equals("") && (boPost.getUserId().equals(Pref.Read(context,Const.PREF_USER_ID)))){
            IApiService service = APIHelper.getAppServiceMethod();
            Call<PojoPost> call = service.deletePostBypostId(userId, boPost.getid());
            call.enqueue(new Callback<PojoPost>() {
                @Override
                public void onResponse(Call<PojoPost> call, Response<PojoPost> response) {
                    if (response.isSuccessful()) {
                        PojoPost pojo = response.body();
                        if (pojo.getStatus() == Const.STATUS_SUCCESS) {
                            allItems.remove(pos);
                            notifyDataSetChanged();

                        } else if (pojo.getStatus() == Const.STATUS_FAILED) {
                        } else if (pojo.getStatus() == Const.STATUS_ERROR) {
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<PojoPost> call, Throwable t) {
                }
            });}
    }
    private void share(BoPost bofeed) {

        if (bofeed.getPostType().equals(Const.IMAGE)) {
            new DownloadManager(bofeed).execute(myImgList);
        }
        else if (bofeed.getPostType().equals(Const.VIDEO)) {
            Common.shareUserVideo(context, bofeed.getText(), bofeed.getblogImage(), bofeed.getVideo(), Common.getPlaystoreUrl(), bofeed.getUserId());
        } else if (bofeed.getPostType().equals(Const.TEXT)) {
            Common.shareStatus(context, bofeed.getText(), Common.getPlaystoreUrl(), bofeed.getblogImage());

        }

    }
    private void getPicsURL(BoPost gallery) {
        try {
            myImgList = new ArrayList<String>(Arrays.asList(gallery.getblogImage().split("\\|")));
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
        @BindView(R.id.ivAction)
        ImageView ivAction;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
    protected class DownloadManager extends AsyncTask<List<String>, Integer, Boolean> {
        ProgressDialog PD;
        String reciverLocalPath;
        BoPost bofeed;

        public DownloadManager(BoPost bofeed) {
            this.bofeed = bofeed;
        }

        @Override
        protected Boolean doInBackground(List<String>... params) {
            //List<String> passedFiles = params[0];
    //       List<String> passedFiles = Arrays.asList(bofeed.getImage().split("\\|"));
    //       try {
    //           userLocalUritoShare.clear();
    //           for (int i = 0; i < passedFiles.size(); i++) {
    //               String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //               File destinationFile = new File(Common.getStorageDirectoryOfImages(), timeStamp + "_" + i + ".jpg");
    //               reciverLocalPath = destinationFile.getAbsolutePath();
    //               userLocalUritoShare.add(reciverLocalPath);
    //               DownloadFile(passedFiles.get(i), reciverLocalPath);


    //           }
    //       } catch (Exception e) {
    //           e.printStackTrace();
    //       }
            return true;
        }

        @Override
        protected void onPreExecute() {
            PD = ProgressDialog.show(context, null, "Please Wait ...", true);
            PD.setCancelable(true);
        }

        protected void onPostExecute(Boolean result) {
            PD.dismiss();
            if (result) {
                log("download success : " + userLocalUritoShare);
                //Common.sharePhoto(context, userLocalUritoShare, bofeed.getText(), Common.getPlaystoreUrl(), bofeed.getUserName());

            } else log("download failed");
        }


    }

    protected boolean DownloadFile(String urlString, String destinationPath) {
        log("input url : " + urlString);
        log("destination file path : " + destinationPath);
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
            urlConnection.connect();

            FileOutputStream fileOutput = new FileOutputStream(destinationPath);
            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Common.logException(context, "Error downloading file", e, null);
            return false;
        }
    }
}
