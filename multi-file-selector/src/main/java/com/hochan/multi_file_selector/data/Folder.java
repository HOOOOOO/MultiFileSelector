package com.hochan.multi_file_selector.data;

import android.net.Uri;
import android.text.TextUtils;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;

import java.util.List;

/**
 *
 * Created by hochan on 2016/5/17.
 */
public class Folder {

    private String mName;
    private String mPath;
    private List<BaseFile> mBaseFiles;
    private int mMediaType;
    private Uri mFolderCover;

    public Folder(int mediaType, String mName, String mPath, List<BaseFile> mBaseFiles) {
        this.mMediaType = mediaType;
        this.mName = mName;
        this.mPath = mPath;
        this.mBaseFiles = mBaseFiles;
        switch (mMediaType){
            case MultiFileSelectorActivity.TYPE_IMAGE:
                mFolderCover = Uri.fromFile(new java.io.File(mBaseFiles.get(0).getPath()));
                break;
            case MultiFileSelectorActivity.TYPE_AUDIO:
                mFolderCover = ((AudioFile) mBaseFiles.get(0)).getmAlbumUri();
                break;
            case MultiFileSelectorActivity.TYPE_VIDEO:
                String thumbnailPath = ((VideoFile) mBaseFiles.get(0)).getmThumbnailPath();
                if(!TextUtils.isEmpty(thumbnailPath)){
                    java.io.File file = new java.io.File(thumbnailPath);
                    mFolderCover = Uri.fromFile(file);
                }
                break;
        }
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public List<BaseFile> getmFiles() {
        return mBaseFiles;
    }

    public void setmFiles(List<BaseFile> mBaseFiles) {
        this.mBaseFiles = mBaseFiles;
    }

    public Uri getmFolderCover() {
        return mFolderCover;
    }

    public void setmFolderCover(Uri mFolderCover) {
        this.mFolderCover = mFolderCover;
    }

    public int getmMediaType() {
        return mMediaType;
    }
}
