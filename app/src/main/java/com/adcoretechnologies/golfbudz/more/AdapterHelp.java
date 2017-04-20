package com.adcoretechnologies.golfbudz.more;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adcore on 2/6/2017.
 */

public class AdapterHelp extends
        RecyclerView.Adapter<AdapterHelp.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoFaq> allItems;

    public AdapterHelp(ArrayList<BoFaq> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterHelp.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.row_faqlist,
                parent, false);

        AdapterHelp.ViewHolder vh = new AdapterHelp.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterHelp.ViewHolder holder, final int position) {
        final BoFaq item = getItem(position);


        holder.tvAns.setText(item.getDescription());
        holder.tvQus.setText(item.getTitle());

    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoFaq getItem(int position) {
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


        @BindView(R.id.tvQus)
        TextView tvQus;
        @BindView(R.id.tvAns)
        TextView tvAns;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}
