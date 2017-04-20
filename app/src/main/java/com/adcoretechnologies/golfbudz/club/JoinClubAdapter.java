package com.adcoretechnologies.golfbudz.club;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.home.model.BoService;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Rehan on 1/6/2017.
 */

public class JoinClubAdapter extends
        RecyclerView.Adapter<JoinClubAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoService> allItems;

    public JoinClubAdapter(ArrayList<BoService> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public JoinClubAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_joinclucb,
                parent, false);

        JoinClubAdapter.ViewHolder vh = new JoinClubAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final JoinClubAdapter.ViewHolder holder, final int position) {
        final BoService item = getItem(position);


        // holder.tvMessage.setText(item.getServiceDescription());
        //Common.showBigImage(context, holder.ivServiceImage, item.getImagePath());

    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoService getItem(int position) {
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


        /*@BindView(R.id.rlclub)
        RelativeLayout rlclub;*/
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

    }
}
