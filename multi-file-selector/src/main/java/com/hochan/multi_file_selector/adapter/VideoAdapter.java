package com.hochan.multi_file_selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.BaseFile;
import com.hochan.multi_file_selector.data.VideoFile;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.tool.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by hochan on 2016/5/20.
 */
public class VideoAdapter extends RecyclerView.Adapter{

    private List<BaseFile> mVideoFiles = new ArrayList<>();
    private List<BaseFile> mSelectedVideos = new ArrayList<>();

    private Context mContext;

    private MediaFileAdapterListener mAdapterListener;

    public VideoAdapter(Context context, MediaFileAdapterListener adapterListener){
        this.mContext = context;
	    mAdapterListener = adapterListener;
    }

    public void setData(List<BaseFile> baseFiles){
        this.mVideoFiles = baseFiles;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        VideoFile videoFile = (VideoFile) mVideoFiles.get(position);
        videoViewHolder.tvVideoName.setText(videoFile.getName());
        videoViewHolder.tvVideoDuration.setText(Tool.getDurationFormat(videoFile.getmDuration()));

    }

    @Override
    public int getItemCount() {
        return mVideoFiles.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

	    private TextView tvVideoName, tvVideoDuration;

        public VideoViewHolder(View itemView) {
            super(itemView);
	        tvVideoName = (TextView) itemView.findViewById(R.id.tv_video_name);
            tvVideoDuration = (TextView) itemView.findViewById(R.id.tv_video_duration);
        }

        @Override
        public void onClick(View v) {
            if(mSelectedVideos.contains(mVideoFiles.get(getPosition()))){
                mSelectedVideos.remove(mVideoFiles.get(getPosition()));
            }else{
                mSelectedVideos.add(mVideoFiles.get(getPosition()));
            }
            mAdapterListener.fileSelected(mSelectedVideos.size());
        }
    }

}
