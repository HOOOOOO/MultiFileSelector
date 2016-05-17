package com.hochan.multi_file_selector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hochan.multi_file_selector.R;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.SquaredImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class FolderAdapter extends BaseAdapter{

    private Context mContext;
    private List<Folder> mFolders = new ArrayList<>();
    private int mFolderIconSize;

    public FolderAdapter(Context context){
        this.mContext = context;
        mFolderIconSize = ScreenTools.dip2px(80, mContext);
    }

    public void setData(List<Folder> folders){
        this.mFolders = folders;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size();
    }

    @Override
    public Folder getItem(int position) {
        return mFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FolderViewHolder folderViewHolder = null;
        if(convertView == null){
            folderViewHolder = new FolderViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_folder_item, parent, false);
            folderViewHolder.sivIcon = (SquaredImageView) convertView.findViewById(R.id.siv_folder_image);
            folderViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_folder_name);
            folderViewHolder.tvPath = (TextView) convertView.findViewById(R.id.tv_folder_path);
            convertView.setTag(folderViewHolder);
        }else{
            folderViewHolder = (FolderViewHolder) convertView.getTag();
        }
        folderViewHolder.tvName.setText(getItem(position).getmName());
        if(getItem(position).getmPath() == null)
            folderViewHolder.tvPath.setVisibility(View.GONE);
        else {
            folderViewHolder.tvPath.setText(getItem(position).getmPath());
            folderViewHolder.tvPath.setVisibility(View.VISIBLE);
        }
        Picasso.with(mContext)
                .load(new File(getItem(position).getmMediaFiles().get(0).getmPath()))
                .resize(mFolderIconSize, mFolderIconSize)
                .centerCrop()
                .into(folderViewHolder.sivIcon);
        return convertView;
    }

    class FolderViewHolder{
        public SquaredImageView sivIcon;
        public TextView tvName, tvPath;
    }
}
