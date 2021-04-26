package com.bo.salesdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bo.salesdemo.R;

import com.bo.salesdemo.model.SingleItemModel;

import java.util.List;

public class ViewCartRecyclerViewAdapter extends
        RecyclerView.Adapter<ViewCartRecyclerViewAdapter.ViewHolder> {

    private List<SingleItemModel> mSingleItemModelList;
    private Context mContext;

    private int lastSelectedPosition = -1;

    private OnItemClickListener mOnItemClickListener;

    public ViewCartRecyclerViewAdapter(Context context, List<SingleItemModel> singleItemModelList) {
        mSingleItemModelList = singleItemModelList;
        mContext = context;
    }

    public ViewCartRecyclerViewAdapter(Context ctx, List<SingleItemModel> singleItemModelList,
            OnItemClickListener itemClickListener) {
        mSingleItemModelList = singleItemModelList;
        mContext = ctx;
        mOnItemClickListener = itemClickListener;
    }

    public void setList(List<SingleItemModel> singleItemModelList) {
        mSingleItemModelList = singleItemModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewCartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_1, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCartRecyclerViewAdapter.ViewHolder holder,
            int position) {
        SingleItemModel singleItemModel = mSingleItemModelList.get(position);
        holder.title.setText(singleItemModel.getProductName());
        holder.price.setText(singleItemModel.getPrice());
        holder.bids.setText(singleItemModel.getBids());

        holder.mWebImageView.setVisibility(View.VISIBLE);
//        if (holder.mWebImageView != null) {
//            if (singleItemModel.getThumbnailUrl() != null) {
//                holder.mWebImageView.setImageUrl(singleItemModel.getThumbnailUrl());
//                holder.mWebImageView.loadImage();
//            } else {
//                holder.mWebImageView.setNoImageDrawable(R.drawable.placeholder);
//            }
//        }

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        // holder.selectionState.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return mSingleItemModelList == null ? 0 : mSingleItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mWebImageView;
        TextView title;
        TextView price;
        TextView bids;
        RadioButton selectionState;

        public ViewHolder(@NonNull View view) {
            super(view);
            mWebImageView = view.findViewById(R.id.gallery_icon);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            bids = view.findViewById(R.id.bids);


//            selectionState.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    lastSelectedPosition = ViewHolder.this.getAdapterPosition();
//                    mOnItemClickListener.onItemClick(
//                            mSingleItemModelList.get(lastSelectedPosition).getPrice(),
//                            ViewHolder.this.getAdapterPosition());
//                    this.n();
//
//                }
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String productName, int pos);
    }
}
