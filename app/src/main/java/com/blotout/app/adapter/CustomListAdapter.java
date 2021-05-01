package com.blotout.app.adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.blotout.app.R;
import com.blotout.deviceinfo.ads.Ad;

import java.util.List;


public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomViewHolder> {

    private Context context;
    private List deviceList;
    private Object object;

    public CustomListAdapter(Context context, List deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }



    public CustomListAdapter(Context context, Object object) {
        this.context = context;
        this.object = object;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if(deviceList == null) {
            if(object instanceof Ad) {
                handleAdInfo(holder, position);
            }
            return;
        }

        Object object = deviceList.get(position);

    }

    @Override
    public int getItemCount() {
        if(deviceList == null) return object.getClass().getDeclaredFields().length;
        return deviceList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView desc;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.desc = (TextView) itemView.findViewById(R.id.textDesc);
        }
    }



    private void handleAdInfo(@NonNull CustomViewHolder holder, int position) {
        if(position == 0) {
            holder.textView.setText("AdvertisingId:");
            holder.desc.setText(((Ad) object).getAdvertisingId());
        } else {
            holder.textView.setText("Allow to track ads:");
            holder.desc.setText(String.valueOf(((Ad) object).isAdDoNotTrack()));
        }
    }




    private float convertToGb(long valInBytes) {
        return Float.valueOf(String.format("%.2f", (float) valInBytes / (1024 * 1024 * 1024)));
    }
}
