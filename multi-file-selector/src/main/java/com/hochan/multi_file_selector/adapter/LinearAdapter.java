package com.hochan.multi_file_selector.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.AudioFile;
import com.hochan.multi_file_selector.data.BaseFile;
import com.hochan.multi_file_selector.data.NoneMediaFile;
import com.hochan.multi_file_selector.data.VideoFile;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.tool.GetVideoThumbnail;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.tool.Tool;
import com.hochan.multi_file_selector.view.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/5/19.
 */
public class LinearAdapter extends RecyclerView.Adapter{

    final public static Uri SARTWORKURI = Uri
            .parse("content://media/external/audio/albumart");

    private Context mContext;
    private List<BaseFile> mBaseFiles = new ArrayList<>();
    private List<BaseFile> mSelectedBaseFiles = new ArrayList<>();
    private int mAlbumSize;
    private int mType;

    private MediaFileAdapterListener mAdapterListener;

    public LinearAdapter(Context context, int type){
        this.mContext = context;
        this.mType = type;
        this.mAlbumSize = ScreenTools.dip2px(context, 50);
    }

    public void setData(List<BaseFile> mBaseFiles){
        this.mBaseFiles = mBaseFiles;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_linear_item, parent, false);
        return new LinearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LinearViewHolder viewHolder = (LinearViewHolder) holder;
        if(mType == MultiFileSelectorActivity.TYPE_AUDIO) {
            AudioFile audioFile = (AudioFile) mBaseFiles.get(position);
            if (mSelectedBaseFiles.contains(audioFile)) {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_checked);
            } else {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_unchecked);
            }
            viewHolder.tvName.setText(audioFile.getName());
            viewHolder.tvDuration.setText(Tool.getDurationFormat(audioFile.getmDuration()));

            Picasso.with(mContext)
                    .load(audioFile.getmAlbumUri())
                    .placeholder(R.drawable.icon_list_audiofile)
                    .resize(mAlbumSize, mAlbumSize)
                    .centerCrop()
                    .into(viewHolder.sivIcon);
        }else if(mType == MultiFileSelectorActivity.TYPE_MEDIANONE){
            NoneMediaFile noneMediaFile = (NoneMediaFile) mBaseFiles.get(position);

            if (mSelectedBaseFiles.contains(noneMediaFile)) {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_checked);
            } else {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_unchecked);
            }

            viewHolder.tvName.setText(noneMediaFile.getName());
            viewHolder.tvDuration.setVisibility(View.GONE);
            switch (noneMediaFile.getType()){
                case NoneMediaFile.TYPE_PDF:
                    viewHolder.sivIcon.setImageResource(R.drawable.icon_list_pdf);
                    break;
                case NoneMediaFile.TYPE_TXT:
                    viewHolder.sivIcon.setImageResource(R.drawable.icon_list_txtfile);
                    break;
                case NoneMediaFile.TYPE_DOCX:
                    viewHolder.sivIcon.setImageResource(R.drawable.icon_list_doc);
                    break;
                case NoneMediaFile.TYPE_XLS:
                    viewHolder.sivIcon.setImageResource(R.drawable.icon_list_excel);
                    break;
            }
        }else if(mType == MultiFileSelectorActivity.TYPE_VIDEO){
            VideoFile videoFile = (VideoFile) mBaseFiles.get(position);
            if (mSelectedBaseFiles.contains(videoFile)) {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_checked);
            } else {
                viewHolder.ivCheck.setImageResource(R.drawable.checkbox_unchecked);
            }
            viewHolder.tvName.setText(videoFile.getName());
            viewHolder.tvDuration.setText(Tool.getDurationFormat(videoFile.getmDuration()));

            String thumbnail = videoFile.getmThumbnailPath();
            if(!TextUtils.isEmpty(thumbnail)) {
                java.io.File file = new java.io.File(thumbnail);
                Picasso.with(mContext)
                        .load(file)
                        .placeholder(R.drawable.icon_list_videofile)
                        .into(viewHolder.sivIcon);
            }else{
                viewHolder.sivIcon.setImageResource(R.drawable.icon_list_videofile);
                viewHolder.sivIcon.setTag(videoFile.getPath());
                GetVideoThumbnail getVideoThumbnail = new GetVideoThumbnail(viewHolder.sivIcon, videoFile.getPath());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBaseFiles.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvName, tvDuration;
        public SquaredImageView sivIcon;
        public ImageView ivCheck;

        public LinearViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            sivIcon = (SquaredImageView) itemView.findViewById(R.id.siv_icon);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mSelectedBaseFiles.contains(mBaseFiles.get(getPosition()))){
                ivCheck.setImageResource(R.drawable.checkbox_unchecked);
                mSelectedBaseFiles.remove(mBaseFiles.get(getPosition()));
            }else{
                ivCheck.setImageResource(R.drawable.checkbox_checked);
                mSelectedBaseFiles.add(mBaseFiles.get(getPosition()));
            }
            mAdapterListener.fileSelected(mSelectedBaseFiles.size());
        }
    }

    public void setmAdapterListener(MediaFileAdapterListener mAdapterListener) {
        this.mAdapterListener = mAdapterListener;
    }

    public List<BaseFile> getmSelectedFiles() {
        return mSelectedBaseFiles;
    }
}
