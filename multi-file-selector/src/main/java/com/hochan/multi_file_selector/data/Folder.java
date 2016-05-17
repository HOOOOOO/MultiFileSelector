package com.hochan.multi_file_selector.data;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Folder {
    private String mName;
    private String mPath;
    private List<MediaFile> mMediaFiles;

    public Folder(String mName, String mPath, List<MediaFile> mMediaFiles) {
        this.mName = mName;
        this.mPath = mPath;
        this.mMediaFiles = mMediaFiles;
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

    public List<MediaFile> getmMediaFiles() {
        return mMediaFiles;
    }

    public void setmMediaFiles(List<MediaFile> mMediaFiles) {
        this.mMediaFiles = mMediaFiles;
    }
}
