package com.hochan.multi_file_selector;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.hochan.multi_file_selector.adapter.ImageAdapter;
import com.hochan.multi_file_selector.data.MediaFile;
import com.hochan.multi_file_selector.loader.DataLoader;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.RecycleViewDivider;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/14.
 */
public class MultiImageSelectorFragment extends Fragment implements DataLoader.DataLoaderCallBack{

    public static final String TAG = "multi_image_selector";

    private Context mContext;
    private DataLoader mLoaderCallback = null;

    //view
    private RecyclerView rclvImages;
    private ImageAdapter mImageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        rclvImages = (RecyclerView) view.findViewById(R.id.rclv_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rclvImages.setLayoutManager(gridLayoutManager);
        rclvImages.addItemDecoration(new RecycleViewDivider(mContext,
                RecycleViewDivider.ORIENTATION_BOTH, ScreenTools.dip2px(mContext, 3), 0));
        mImageAdapter = new ImageAdapter(mContext, 3);
        rclvImages.setAdapter(mImageAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoaderCallback = new DataLoader(getActivity(), MediaFile.TYPE_IMAGE);
        mLoaderCallback.setCallBack(this);
        getActivity().getSupportLoaderManager().initLoader(MediaFile.TYPE_IMAGE, null, mLoaderCallback);
    }

    @Override
    public void finish(ArrayList<MediaFile> mediaFiles) {
        System.out.println(mediaFiles.size());
        mImageAdapter.setData(mediaFiles);
    }
}
