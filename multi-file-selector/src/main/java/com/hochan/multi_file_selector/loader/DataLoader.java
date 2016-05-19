package com.hochan.multi_file_selector.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hochan.multi_file_selector.data.AudioFile;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.data.ImageFile;
import com.hochan.multi_file_selector.data.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/7.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {


    private static ArrayList<String> IMAGE_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<String> AUDIO_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<String> VIDEO_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<ArrayList<String>> MEDIA_PROJECTION_LIST = new ArrayList<>();

    static {
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media.DATA);          //0
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media.DISPLAY_NAME);  //1
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media.DATE_ADDED);    //2
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media.SIZE);          //3
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media.MIME_TYPE);     //4
        IMAGE_PROJECTION_LIST.add(MediaStore.Images.Media._ID);           //5

        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.DATA);           //0
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.DISPLAY_NAME);   //1
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.DATE_ADDED);     //2
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.SIZE);           //3
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.MIME_TYPE);      //4
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media._ID);            //5
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.ARTIST);         //6
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.DURATION);       //7
        AUDIO_PROJECTION_LIST.add(MediaStore.Audio.Media.ALBUM_ID);       //8

        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.DATA);           //0
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.DISPLAY_NAME);   //1
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.DATE_ADDED);     //2
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.SIZE);           //3
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.MIME_TYPE);      //4
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media._ID);            //5
        VIDEO_PROJECTION_LIST.add(MediaStore.Video.Media.DURATION);       //6

        MEDIA_PROJECTION_LIST.add(MediaFile.TYPE_IMAGE, IMAGE_PROJECTION_LIST);
        MEDIA_PROJECTION_LIST.add(MediaFile.TYPE_AUDIO, AUDIO_PROJECTION_LIST);
        MEDIA_PROJECTION_LIST.add(MediaFile.TYPE_VIDEO, VIDEO_PROJECTION_LIST);
    }

    private Context mContext;
    private int mType;
    private List<Folder> mFolders;
    private List<MediaFile> mMediaFiles;

    public DataLoader(Context context, int type){
        this.mContext = context;
        this.mType = type;
        System.out.println("dataloder type:"+type);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        System.out.println("dataloder:"+id);
        switch (id){
            case MediaFile.TYPE_IMAGE:
                System.out.println(IMAGE_PROJECTION_LIST.get(3));
                System.out.println(IMAGE_PROJECTION_LIST.get(4));
                System.out.println(IMAGE_PROJECTION_LIST.get(2));
                CursorLoader acursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        (String[]) IMAGE_PROJECTION_LIST.toArray(new String[IMAGE_PROJECTION_LIST.size()]),
                        //?对应后面的selectionArgs用于转义特殊字符
                        IMAGE_PROJECTION_LIST.get(3)+">0 AND "+IMAGE_PROJECTION_LIST.get(4)+"=? OR "+IMAGE_PROJECTION_LIST.get(4)+"=? ",
                        new String[]{"image/jpeg", "image/png"},
                        IMAGE_PROJECTION_LIST.get(2) + " DESC");
                return acursorLoader;
            case MediaFile.TYPE_VIDEO:
                CursorLoader bcursorLoader = new CursorLoader(mContext,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        (String[]) VIDEO_PROJECTION_LIST.toArray(),
                        VIDEO_PROJECTION_LIST.get(3)+">0 AND ",
                        null, VIDEO_PROJECTION_LIST.get(2)+" DESC");
                return bcursorLoader;
            case MediaFile.TYPE_AUDIO:
                CursorLoader ccursorLoader = new CursorLoader(mContext,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        (String[]) AUDIO_PROJECTION_LIST.toArray(new String[AUDIO_PROJECTION_LIST.size()]),
                        AUDIO_PROJECTION_LIST.get(3)+">0 ",
                        null, AUDIO_PROJECTION_LIST.get(2)+" DESC");
                return ccursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            if (data.getCount() > 0) {
                System.out.println("corsur不为空:"+data.getCount());
                data.moveToFirst();
                mFolders = new ArrayList<>();
                mMediaFiles = new ArrayList<>(data.getCount());
                do {
                    String displayName = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(1)));
                    String path = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(0)));
                    String dateAdded = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(2)));
                    String size = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(3)));

                    switch (mType) {
                        case MediaFile.TYPE_IMAGE:
                            ImageFile imageFile = new ImageFile(MediaFile.TYPE_IMAGE,
                                    displayName, path, dateAdded, size);
                            mMediaFiles.add(imageFile);
                            addToFolder(imageFile);
                            break;
                        case MediaFile.TYPE_AUDIO:
                            AudioFile audioFile = new AudioFile(MediaFile.TYPE_AUDIO,
                                    displayName, path, dateAdded, size);
                            audioFile.setmDuration(
                                    data.getLong(data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(7))));
                            audioFile.setmAlbumId(data.getLong(data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(8))));
                            mMediaFiles.add(audioFile);
                            addToFolder(audioFile);
                            break;
                        case MediaFile.TYPE_VIDEO:
                            break;
                    }

                } while (data.moveToNext());
                if (mCallBack != null) {
                    System.out.println(mMediaFiles.size());
                    mCallBack.finish(mMediaFiles, mFolders);
                }
            } else {
                System.out.println("查询结果为空" + data.getCount());
            }
        }else {
            System.out.println("cursor为空");
        }
    }

    private void addToFolder(MediaFile mediaFile){
        File folderFile = new File(mediaFile.getmPath()).getParentFile();
        if (folderFile.exists()) {
            if (folderFile != null && folderFile.exists()) {
                Folder tmpFolder = getFolderByPath(folderFile.getAbsolutePath());
                if (tmpFolder == null) {
                    List<MediaFile> mediaFiles = new ArrayList<>();
                    mediaFiles.add(mediaFile);
                    Folder folder = new Folder(mType,
                            folderFile.getName(), folderFile.getAbsolutePath(), mediaFiles);
                    mFolders.add(folder);
                } else {
                    tmpFolder.getmMediaFiles().add(mediaFile);
                }
            }
        }
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
