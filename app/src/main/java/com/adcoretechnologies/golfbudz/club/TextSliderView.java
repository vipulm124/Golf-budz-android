package com.adcoretechnologies.golfbudz.club;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.adcoretechnologies.golfbudz.R;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by Rehan on 11/29/2016.
 */

public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.sliderlayout,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
//        TextView description = (TextView)v.findViewById(R.id.description);
//        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
