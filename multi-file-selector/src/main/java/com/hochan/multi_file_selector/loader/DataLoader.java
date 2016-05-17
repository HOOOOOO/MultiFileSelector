package com.hochan.multi_file_selector.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.data.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,          //0
            MediaStore.Images.Media.DISPLAY_NAME,  //1
            MediaStore.Images.Media.DATE_ADDED,    //2
            MediaStore.Images.Media.MIME_TYPE,     //3
            MediaStore.Images.Media.SIZE,          //4
            MediaStore.Images.Media._ID };         //5

    private final String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,           //0
            MediaStore.Video.Media.DISPLAY_NAME,   //1
            MediaStore.Video.Media.DATE_ADDED,     //2
            MediaStore.Video.Media.MIME_TYPE,      //3
            MediaStore.Video.Media.SIZE,           //4
            MediaStore.Video.Media._ID,            //5
            MediaStore.Video.Media.DURATION};      //6

    private final String[] AUDIO_PROJECTION = {
            MediaStore.Audio.Media.DATA,           //0
            MediaStore.Audio.Media.DISPLAY_NAME,   //1
            MediaStore.Audio.Media.DATE_ADDED,     //2
            MediaStore.Audio.Media.MIME_TYPE,      //3
            MediaStore.Audio.Media.SIZE,           //4
            MediaStore.Audio.Media._ID,            //5
            MediaStore.Audio.Media.ARTIST,         //6
            MediaStore.Audio.Media.DURATION};      //7

    private Context mContext;
    private int mType;
    private List<Folder> mFolders;

    public DataLoader(Context context, int type){
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (mType){
            case MediaFile.TYPE_IMAGE:
                CursorLoader acursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        //?对应后面的selectionArgs用于转义特殊字符
                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ",
                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                return acursorLoader;
            case MediaFile.TYPE_VIDEO:
                CursorLoader bcursorLoader = new CursorLoader(mContext,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        VIDEO_PROJECTION,
                        IMAGE_PROJECTION[4]+">0 AND ",
                        null, IMAGE_PROJECTION[2]+" DESC");
                return bcursorLoader;
            case MediaFile.TYPE_AUDIO:
                //CursorLoader ccursorLoader = new
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null)
            if(data.getCount() > 0) {
                switch (mType) {
                    case MediaFile.TYPE_IMAGE:
                        handleImageData(data);
                        break;
                    case MediaFile.TYPE_AUDIO:
                        handleAudioData(data);
                        break;
                    case MediaFile.TYPE_VIDEO:
                        handleVideoData(data);
                        break;
                }
            }
    }

    private void handleVideoData(Cursor data) {
        ArrayList<MediaFile> videoFiles = new ArrayList<>();
        data.moveToFirst();
        do{
            String path = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
            String name = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
            String dateAdded = data.getString(
                    data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
            String size = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
            MediaFile videoFile = new MediaFile(
                    MediaFile.TYPE_VIDEO, name, path, dateAdded, size);
            videoFile.setmDuration(
                    data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[6])));
            videoFiles.add(videoFile);
        }while (data.moveToNext());
        if(mCallBack != null)
            mCallBack.finish(videoFiles, mFolders);
    }

    private void handleAudioData(Cursor data) {
    }

    private void handleImageData(Cursor data) {
        mFolders = new ArrayList<>();
        ArrayList<MediaFile> imageFiles = new ArrayList<>();
        data.moveToFirst();
        do{
            String path = data.getString(
                    data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String name = data.getString(
                    data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
            String dataAdded = data.getString(
                    data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
            String size = data.getString(
                    data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
            MediaFile imageFile = new MediaFile(
                    MediaFile.TYPE_IMAGE, name, path, dataAdded, size);
            imageFiles.add(imageFile);
            File folderFile = new File(path).getParentFile();
            if(folderFile.exists()){
                if(folderFile != null && folderFile.exists()){
                    Folder tmpFolder = getFolderByPath(folderFile.getAbsolutePath());
                    if(tmpFolder == null){
                        List<MediaFile> mediaFiles = new ArrayList<>();
                        mediaFiles.add(imageFile);
                        Folder folder = new Folder(
                                folderFile.getName(), folderFile.getAbsolutePath(), mediaFiles);
                        mFolders.add(folder);
                    }else{
                        tmpFolder.getmMediaFiles().add(imageFile);
                    }
                }
            }
        }while (data.moveToNext());
        if (mCallBack != null)
            mCallBack.finish(imageFiles, mFolders);
    }

    private Folder getFolderByPath(String path){
        for(Folder folder : mFolders){
            if(path.equals(folder.getmPath()))
                return folder;
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface DataLoaderCallBack{
        public void finish(List<MediaFile> mediaFiles, List<Folder> folders);
    }

    private DataLoaderCallBack mCallBack;

    public void setCallBack(DataLoaderCallBack callBack){
        this.mCallBack = callBack;
    }
}
