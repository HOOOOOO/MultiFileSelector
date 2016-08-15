package com.hochan.multi_file_selector.loader;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.webkit.MimeTypeMap;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.data.AudioFile;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.data.ImageFile;
import com.hochan.multi_file_selector.data.BaseFile;
import com.hochan.multi_file_selector.data.NoneMediaFile;
import com.hochan.multi_file_selector.data.VideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/5/7.
 */
public class DataLoader implements LoaderManager.LoaderCallbacks<Cursor> {


    private static ArrayList<String> IMAGE_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<String> AUDIO_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<String> VIDEO_PROJECTION_LIST = new ArrayList<>();
    private static ArrayList<String> FILE_PROJECTION_LIST = new ArrayList<>();
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

        MEDIA_PROJECTION_LIST.add(MultiFileSelectorActivity.TYPE_IMAGE, IMAGE_PROJECTION_LIST);
        MEDIA_PROJECTION_LIST.add(MultiFileSelectorActivity.TYPE_AUDIO, AUDIO_PROJECTION_LIST);
        MEDIA_PROJECTION_LIST.add(MultiFileSelectorActivity.TYPE_VIDEO, VIDEO_PROJECTION_LIST);
    }

    private Context mContext;
    private int mType;
    private List<Folder> mFolders;
    private List<BaseFile> mBaseFiles;

    public DataLoader(Context context, int type){
        this.mContext = context;
        this.mType = type;
        System.out.println("dataloder type:"+type);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MultiFileSelectorActivity.TYPE_IMAGE:
                CursorLoader acursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        (String[]) IMAGE_PROJECTION_LIST.toArray(new String[IMAGE_PROJECTION_LIST.size()]),
                        IMAGE_PROJECTION_LIST.get(3)+">0 AND "+IMAGE_PROJECTION_LIST.get(4)+"=? OR "+IMAGE_PROJECTION_LIST.get(4)+"=? ",
                        new String[]{"image/jpeg", "image/png"},
                        IMAGE_PROJECTION_LIST.get(2) + " DESC");
                return acursorLoader;
            case MultiFileSelectorActivity.TYPE_VIDEO:
                CursorLoader bcursorLoader = new CursorLoader(mContext,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        (String[]) VIDEO_PROJECTION_LIST.toArray(new String[IMAGE_PROJECTION_LIST.size()]),
                        VIDEO_PROJECTION_LIST.get(3)+">0 ",
                        null, VIDEO_PROJECTION_LIST.get(2)+" DESC");
                return bcursorLoader;
            case MultiFileSelectorActivity.TYPE_AUDIO:
                CursorLoader ccursorLoader = new CursorLoader(mContext,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        (String[]) AUDIO_PROJECTION_LIST.toArray(new String[AUDIO_PROJECTION_LIST.size()]),
                        AUDIO_PROJECTION_LIST.get(3)+">0 ",
                        null, AUDIO_PROJECTION_LIST.get(2)+" DESC");
                return ccursorLoader;
            case MultiFileSelectorActivity.TYPE_MEDIANONE:
                ContentResolver cr = mContext.getContentResolver();
                Uri uri = MediaStore.Files.getContentUri("external");
                String[] projection = null;
                String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=? OR " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "=? OR " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "=?  OR " +
                        MediaStore.Files.FileColumns.MIME_TYPE + "=? ";
                String mimeTypePdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
                String mimeTypeTxt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
                String mimeTypeXls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
                String mimeTypeDocx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
                String[] selectionArgs = new String[]{mimeTypePdf, mimeTypeTxt, mimeTypeXls, mimeTypeDocx}; // there is no ? in selection so null here
                String sortOrder = null; // unordered
                CursorLoader dcursorLoader = new CursorLoader(mContext,
                        uri, projection, selection, selectionArgs, sortOrder);
                return dcursorLoader;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null) {
            if (data.getCount() > 0) {
                data.moveToFirst();
                mFolders = new ArrayList<>();
                mBaseFiles = new ArrayList<>(data.getCount());
                do {

                    if(mType == MultiFileSelectorActivity.TYPE_MEDIANONE){

                        String mimeType = data.getString(
                                data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                        //System.out.println(mimeType);
                        int type = NoneMediaFile.TYPE_PDF;
                        switch (mimeType){
                            case "application/pdf":
                                type = NoneMediaFile.TYPE_PDF;
                                break;
                            case "text/plain":
                                type = NoneMediaFile.TYPE_TXT;
                                break;
                            case "application/vnd.ms-excel":
                                type = NoneMediaFile.TYPE_XLS;
                                break;
                        }

                        String path = data.getString(
                                data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
	                    if(path == null){
		                    continue;
	                    }
                        String name = path.substring(path.lastIndexOf("/")+1, path.length());
                        String dateAdded = data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
                        long size = data.getLong(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                        NoneMediaFile noneMediaFile = new NoneMediaFile(type, name, path, dateAdded, size);
                        mBaseFiles.add(noneMediaFile);
                        addToFolder(noneMediaFile);
                        continue;
                    }

	                String path = data.getString(
			                data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(0)));
	                if(path == null) {
		                continue;
	                }
                    String displayName = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(1)));
                    String dateAdded = data.getString(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(2)));
                    long size = data.getLong(
                            data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(3)));

                    switch (mType) {
                        case MultiFileSelectorActivity.TYPE_IMAGE:
                            ImageFile imageFile = new ImageFile(MultiFileSelectorActivity.TYPE_IMAGE,
                                    displayName, path, dateAdded, size);
                            mBaseFiles.add(imageFile);
                            addToFolder(imageFile);
                            break;
                        case MultiFileSelectorActivity.TYPE_AUDIO:
                            AudioFile audioFile = new AudioFile(MultiFileSelectorActivity.TYPE_AUDIO,
                                    displayName, path, dateAdded, size);
                            audioFile.setmDuration(
                                    data.getLong(data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(7))));
                            audioFile.setmAlbumId(data.getLong(data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(8))));
                            mBaseFiles.add(audioFile);
                            addToFolder(audioFile);
                            break;
                        case MultiFileSelectorActivity.TYPE_VIDEO:
                            VideoFile videoFile = new VideoFile(MultiFileSelectorActivity.TYPE_VIDEO,
                                    displayName, path, dateAdded, size);
                            videoFile.setmDuration(data.getLong(data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(6))));

                            long videoID = data.getLong(
                                    data.getColumnIndexOrThrow(MEDIA_PROJECTION_LIST.get(mType).get(5)));
                            //System.out.println(videoID);

                            MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(),
                                    videoID, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                            Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                                    null, MediaStore.Video.Thumbnails.VIDEO_ID + " =? ", new String[]{String.valueOf(videoID)}, null);
                            try{
                                if(cursor.moveToFirst()){
                                    String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                                    //System.out.println(thumbnail);
                                    videoFile.setmThumbnailPath(thumbnail);
                                }
                            }finally {
                                cursor.close();
                            }

                            mBaseFiles.add(videoFile);
                            addToFolder(videoFile);
                            break;
                    }

                } while (data.moveToNext());

                if (mCallBack != null) {
                    System.out.println(mBaseFiles.size());
                    mCallBack.finish(mBaseFiles, mFolders);
                }
            }
        }
    }

    private void addToFolder(BaseFile baseFile){
        File folderFile = new File(baseFile.getPath()).getParentFile();
        if (folderFile.exists()) {
	        Folder tmpFolder = getFolderByPath(folderFile.getAbsolutePath());
	        if (tmpFolder == null) {
		        List<BaseFile> baseFiles = new ArrayList<>();
		        baseFiles.add(baseFile);
		        Folder folder = new Folder(mType,
				        folderFile.getName(), folderFile.getAbsolutePath(), baseFiles);
		        mFolders.add(folder);
	        } else {
		        tmpFolder.getmFiles().add(baseFile);
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
        public void finish(List<BaseFile> baseFiles, List<Folder> folders);
    }

    private DataLoaderCallBack mCallBack;

    public void setCallBack(DataLoaderCallBack callBack){
        this.mCallBack = callBack;
    }
}
