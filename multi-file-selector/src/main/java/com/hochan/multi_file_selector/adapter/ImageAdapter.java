package com.hochan.multi_file_selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hochan.multi_file_selector.MultiImageSelectorFragment;
import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.MediaFile;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    public void setData(List<MediaFile> images){
        mImages = (ArrayList<MediaFile>) images;
        notifyDataSetChanged();
    }

    public List<MediaFile> getSelectedImages(){
        return mSelectedImages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        if(mSelectedImages.contains(mImages.get(position))){
            imageViewHolder.sivMask.setVisibility(View.VISIBLE);
        }else{
            imageViewHolder.sivMask.setVisibility(View.GONE);
        }
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

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public SquaredImageView sivImage, sivMask;

        public ImageViewHolder(View itemView) {
            super(itemView);
            sivImage = (SquaredImageView) itemView.findViewById(R.id.siv_image);
            sivMask = (SquaredImageView) itemView.findViewById(R.id.siv_mask);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mSelectedImages.contains(mImages.get(getPosition()))){
                mSelectedImages.remove(mImages.get(getPosition()));
                sivMask.setVisibility(View.GONE);
            }else {
                mSelectedImages.add(mImages.get(getPosition()));
                sivMask.setVisibility(View.VISIBLE);
            }
            System.out.println("已选图片数目："+mSelectedImages.size());
            if(mAdapaterListener != null){
                mAdapaterListener.fileSelected(mSelectedImages.size());
            }
        }
    }

    private MediaFileAdapterListener mAdapaterListener;

    public void setImageAdpaterListener(MediaFileAdapterListener listener){
        this.mAdapaterListener = listener;
    }
}