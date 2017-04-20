package com.adcoretechnologies.golfbudz.items;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Common;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Rehan on 11/16/2016.
 */

public class BuyItemAdapter extends
        RecyclerView.Adapter<BuyItemAdapter.ViewHolder> implements View.OnClickListener {
    Context context;


    private ArrayList<BoItems> allItems;

    public BuyItemAdapter(ArrayList<BoItems> allItems) {
        this.allItems = allItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BuyItemAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.buyitems_card_view,
                parent, false);

        BuyItemAdapter.ViewHolder vh = new BuyItemAdapter.ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final BuyItemAdapter.ViewHolder holder, final int position) {
        final BoItems item = getItem(position);


        holder.tvCost.setText(item.getPrice());
        holder.tvDesc.setText(item.getDescription());
        holder.tvTitle.setText(item.getTitle());
        if(item.getImage().equals("")){}else{
        String[] parts = item.getImage().split("\\|");
        Common.showBigImage(context, holder.ivImage, parts[0]);}
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new BoEventData(BoEventData.EVENT_BUY_ITEM_CLICK,position,item.getId()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public BoItems getItem(int position) {
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


        @BindView(R.id.tvDesc)
        TextView tvDesc;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCost)
        TextView tvCost;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.btnBuy)
        Button btnBuy;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }
}