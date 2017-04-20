package com.adcoretechnologies.golfbudz.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;


/**
 * Created by PHP Ninzza on 26-Aug-16.
 */
public class MenuListAdapter extends ArrayAdapter<String> {
    Context context;
    private  String[] title;


    public MenuListAdapter(Context context, String[] title){
        super(context, R.layout.menurow_list_item, title);
        this.context = context;
        this.title=title;

    }
    private class ViewHolder {
        ImageView menuimage;
        TextView txtTitle;
        LinearLayout row;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.menurow_list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.menutext);
            holder.row = (LinearLayout) convertView.findViewById(R.id.row);


            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
            holder.txtTitle.setText(title[position]);
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(title[position].equals("Electronics")){

                    }else   if(title[position].equals("Fashion")){

                    }

                }
            });
           return convertView;
    }
}
