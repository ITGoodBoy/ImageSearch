package com.celestialapps.imagesearch.ui.adapter;


import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.celestialapps.imagesearch.R;
import com.celestialapps.imagesearch.network.model.response.search.Item;
import com.celestialapps.imagesearch.presentation.presenter.GlidePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ImageSearchViewHolder> {

    private Context mContext;
    private List<Item> mImages;
    private LayoutInflater mLayoutInflater;
    private GlidePresenter mGlidePresenter;
    private String mQuery;

    public ImageSearchAdapter(Context context, List<Item> images, GlidePresenter glidePresenter, String query) {
        mContext = context;
        mImages = images;
        mLayoutInflater = LayoutInflater.from(context);
        mGlidePresenter = glidePresenter;
        mQuery = query;
    }

    public void addItems(List<Item> items) {
        items.removeAll(mImages);
        mImages.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_image, parent, false);

        return new ImageSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSearchViewHolder holder, int position) {
        Item image = mImages.get(position);

        mGlidePresenter.clearImageView(holder.mAcImvImage);
        holder.mAcImvImage.setImageResource(R.drawable.ic_load_image);

        mGlidePresenter.loadImageAsyncAndSaveImageToInternalStorage(image, holder.mAcImvImage, mQuery);
    }


    @Override
    public int getItemCount() {
        return mImages.size();
    }

    static class ImageSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ac_imv_image)
        AppCompatImageView mAcImvImage;


        public ImageSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
