package com.hochan.multi_file_selector.data;

import com.hochan.multi_file_selector.R;

import java.util.ArrayList;

/**
 *
 * Created by hochan on 2016/5/10.
 */
public class BaseFile {

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
