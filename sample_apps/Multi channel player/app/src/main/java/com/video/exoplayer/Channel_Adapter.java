package com.video.exoplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

interface Channel_listener {
    public void onClick(int item);
}

public class Channel_Adapter extends RecyclerView.Adapter<Channel_Adapter.ChannelViewHolder> {
    private static Channel_listener listener;
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ChannelViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public Button button;
        public ChannelViewHolder(RelativeLayout v) {
            super(v);
            textView = v.findViewById(R.id.channel_name);
            button = v.findViewById(R.id.channel_play);
            button.setOnClickListener(v12 -> {
                int itemPosition = getLayoutPosition();
                listener.onClick(itemPosition);
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Channel_Adapter(String[] myDataset, Channel_listener listener) {
        mDataset = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Channel_Adapter.ChannelViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_layout, parent, false);
        ChannelViewHolder vh = new ChannelViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}