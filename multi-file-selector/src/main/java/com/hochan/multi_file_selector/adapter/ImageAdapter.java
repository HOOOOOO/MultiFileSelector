package com.hochan.multi_file_selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hochan.multi_file_selector.MultiImageSelectorFragment;
import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.MediaFile;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ImageAdapter extends RecyclerView.Adapter{

    private ArrayList<MediaFile> mImages = new ArrayList<>();
    private ArrayList<MediaFile> mSelectedImages = new ArrayList<>();
    private Context mContext;

    private final int mGridWidth;

    public ImageAdapter(Context context, int column){
        this.mContext = context;
        this.mGridWidth = ScreenTools.getScreenWidth(context)/column;
    }

    public void setData(ArrayList<MediaFile> images){
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        File imageFile = new File(mImages.get(position).getmPath());
        if(imageFile.exists()) {
            Picasso.with(mContext)
                    .load(imageFile)
                    .tag(MultiImageSelectorFragment.TAG)
                    .resize(mGridWidth, mGridWidth)
                    .centerCrop()
                    .into(imageViewHolder.sivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        public SquaredImageView sivImage, sivShadow;

        public ImageViewHolder(View itemView) {
            super(itemView);
            sivImage = (SquaredImageView) itemView.findViewById(R.id.siv_image);
            sivShadow = (SquaredImageView) itemView.findViewById(R.id.siv_shadow);
        }
    }
}