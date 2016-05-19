package com.hochan.multi_file_selector.data;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class Folder {

    private String mName;
    private String mPath;
    private List<MediaFile> mMediaFiles;
    private int mMediaType;
    private Uri mFolderCover;

    public Folder(int mediaType, String mName, String mPath, List<MediaFile> mMediaFiles) {
        this.mMediaType = mediaType;
        this.mName = mName;
        this.mPath = mPath;
        this.mMediaFiles = mMediaFiles;
        switch (mMediaType){
            case MediaFile.TYPE_IMAGE:
                mFolderCover = Uri.parse(mMediaFiles.get(0).getmPath());
                break;
            case MediaFile.TYPE_AUDIO:
                mFolderCover = ((AudioFile)mMediaFiles.get(0)).getmAlbumUri();
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

    public List<MediaFile> getmMediaFiles() {
        return mMediaFiles;
    }

    public void setmMediaFiles(List<MediaFile> mMediaFiles) {
        this.mMediaFiles = mMediaFiles;
    }

    public Uri getmFolderCover() {
        return mFolderCover;
    }

    public void setmFolderCover(Uri mFolderCover) {
        this.mFolderCover = mFolderCover;
    }
}
