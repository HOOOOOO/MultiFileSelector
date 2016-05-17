package com.hochan.multi_file_selector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.hochan.multi_file_selector.adapter.FolderAdapter;
import com.hochan.multi_file_selector.adapter.ImageAdapter;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.data.MediaFile;
import com.hochan.multi_file_selector.loader.DataLoader;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.RecycleViewDivider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class MultiImageSelectorFragment extends Fragment implements DataLoader.DataLoaderCallBack{

    public static final String TAG = "multi_image_selector";

    private Context mContext;
    private DataLoader mLoaderCallback = null;
    private ImageAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private int mCurrentAlbum = 0;

    //view
    private RecyclerView rclvImages;
    private Button btnAlbums, btnOpera;
    private ListPopupWindow mFolderPopupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image_selector, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
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
        mFolderAdapter = new FolderAdapter(mContext);
        rclvImages.setAdapter(mImageAdapter);
        rclvImages.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_SETTLING)
                    Picasso.with(mContext).pauseTag(TAG);
                else
                    Picasso.with(mContext).resumeTag(TAG);
            }
        });
        btnAlbums = (Button) view.findViewById(R.id.btn_albums);
        btnAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFolderPopupWindow == null)
                    createPopupWindow();
                if(mFolderPopupWindow.isShowing()){
                    mFolderPopupWindow.dismiss();
                }else{
                    mFolderPopupWindow.show();
                    int index = mCurrentAlbum == 0 ? 0 : mCurrentAlbum-1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });
        btnOpera = (Button) view.findViewById(R.id.btn_opera);
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

    private void createPopupWindow(){
        mFolderPopupWindow = new ListPopupWindow(mContext);
        mFolderPopupWindow.setContentWidth(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        mFolderPopupWindow.setWidth(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setHeight(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setAnchorView(btnAlbums);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFolderPopupWindow.dismiss();
                mImageAdapter.setData((mFolderAdapter.getItem(position)).getmMediaFiles());
                mCurrentAlbum = position;
                btnAlbums.setText(mFolderAdapter.getItem(position).getmName());
            }
        });
    }

    @Override
    public void finish(List<MediaFile> mediaFiles, List<Folder> folders) {
        System.out.println(folders.size());
        mImageAdapter.setData((ArrayList<MediaFile>) mediaFiles);
        Folder folder = new Folder("所有图片", null, mediaFiles);
        folders.add(0, folder);
        mFolderAdapter.setData(folders);
    }
}
