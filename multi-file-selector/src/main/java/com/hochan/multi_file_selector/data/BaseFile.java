package com.hochan.multi_file_selector.data;

import java.util.ArrayList;

/**
 *
 * Created by hochan on 2016/5/10.
 */
public class BaseFile {

    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_AUDIO = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_MEDIANONE = 3;
    public final static int TYPE_ALL = 4;

    public final static ArrayList<String> TYPE_NAME = new ArrayList<>();

    static {
        TYPE_NAME.add(TYPE_IMAGE, "图片");
        TYPE_NAME.add(TYPE_AUDIO, "音频");
        TYPE_NAME.add(TYPE_VIDEO, "视频");
        TYPE_NAME.add(TYPE_MEDIANONE, "文档");
        TYPE_NAME.add(TYPE_ALL, "文件");
    }

    private int mType;
    private String mName;
    private String mPath;
    private String mDataAdded;
    private long mSize;

    public BaseFile(int mType, String mName, String mPath, String mDataAdded, long mSzie) {
        this.mType = mType;
        this.mName = mName;
        this.mPath = mPath;
        this.mDataAdded = mDataAdded;
        this.mSize = mSzie;
    }

    @Override
    public boolean equals(Object o){
        try {
            BaseFile baseFile = (BaseFile) o;
            return this.mPath.equals(baseFile.mPath);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPath() {
        return mPath;
    }

}
