package com.hochan.multi_file_selector.adapter;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.AudioFile;
import com.hochan.multi_file_selector.data.MediaFile;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AudioAdapter extends RecyclerView.Adapter{

    final public static Uri SARTWORKURI = Uri
            .parse("content://media/external/audio/albumart");

    private Context mContext;
    private List<MediaFile> mMediaFiles = new ArrayList<>();
    private List<MediaFile> mSelectedAudios = new ArrayList<>();
    private int mAlbumSize;

    private MediaFileAdapterListener mAdapterListener;

    public AudioAdapter(Context context){
        this.mContext = context;
        this.mAlbumSize = ScreenTools.dip2px(context, 50);
    }

    public void setData(List<MediaFile> mMediaFiles){
        this.mMediaFiles = mMediaFiles;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_audio_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
        AudioFile audioFile = (AudioFile)mMediaFiles.get(position);
        if(mSelectedAudios.contains(mMediaFiles.get(position))){
            audioViewHolder.ivMask.setVisibility(View.VISIBLE);
        }else{
            audioViewHolder.ivMask.setVisibility(View.GONE);
        }
        audioViewHolder.tvName.setText(mMediaFiles.get(position).getmName());
        //audioViewHolder.tvDuration.setText(String.valueOf(mMediaFiles.get(position).getmDuration()));
        long duration = audioFile.getmDuration();
        int minute = (int) (duration/1000/60);
        int second = (int) (duration/1000%60);
        audioViewHolder.tvDuration.setText(String.format("%s分%s秒", minute, second));

        Picasso.with(mContext)
                .load(audioFile.getmAlbumUri())
                .resize(mAlbumSize, mAlbumSize)
                .centerCrop()
                .into(audioViewHolder.sivIcon);
    }

    @Override
    public int getItemCount() {
        return mMediaFiles.size();
    }

    class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvName, tvDuration;
        public SquaredImageView sivIcon;
        public ImageView ivMask;

        public AudioViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            sivIcon = (SquaredImageView) itemView.findViewById(R.id.siv_icon);
            ivMask = (ImageView) itemView.findViewById(R.id.iv_mask);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mSelectedAudios.contains(mMediaFiles.get(getPosition()))){
                ivMask.setVisibility(View.INVISIBLE);
                mSelectedAudios.remove(mMediaFiles.get(getPosition()));
            }else{
                ivMask.setVisibility(View.VISIBLE);
                mSelectedAudios.add(mMediaFiles.get(getPosition()));
            }
            mAdapterListener.fileSelected(mSelectedAudios.size());
        }
    }

    /**
     * @Description 获取专辑封面
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setmAdapterListener(MediaFileAdapterListener mAdapterListener) {
        this.mAdapterListener = mAdapterListener;
    }
}
