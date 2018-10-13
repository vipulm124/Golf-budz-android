package com.adcoretechnologies.golfbudz.home.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.home.model.BoPost;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;


import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity {
    private ScrollGalleryView scrollGalleryView;
    List<String> imrUrlList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        init();
    }

    @Override
    public void init() {
        Intent intent=getIntent();
        BoPost post= (BoPost) intent.getSerializableExtra(Const.EXTRA_POST);
//        //setTitle(post.getUserName());
        scrollGalleryView = (ScrollGalleryView)findViewById(R.id.scroll_gallery_view);

        String[] parts = post.getblogImage().split("\\|");

        for (String urls : parts) {
            //do something interesting here
           imrUrlList.add(urls);
        }
        List<MediaInfo> infos = new ArrayList<>(imrUrlList.size());
        for (String url : imrUrlList)
            infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
              /*  .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                    @Override public boolean isImage() {
                        return true;
                    }

                    @Override public void loadMedia(Context context, ImageView imageView,
                                                    MediaLoader.SuccessCallback callback) {

                        callback.onSuccess();
                    }

                    @Override public void loadThumbnail(Context context, ImageView thumbnailView,
                                                        MediaLoader.SuccessCallback callback) {

                        callback.onSuccess();
                    }
                }))
*/
                .addMedia(infos);

    }
    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }
    @Override
    public void log(String message) {

    }
}
