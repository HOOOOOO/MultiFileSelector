package com.hochan.multi_file_selector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hochan.multi_file_selector.adapter.AllFileAdapter;
import com.hochan.multi_file_selector.adapter.LinearAdapter;
import com.hochan.multi_file_selector.adapter.FolderAdapter;
import com.hochan.multi_file_selector.adapter.ImageAdapter;
import com.hochan.multi_file_selector.adapter.VideoAdapter;
import com.hochan.multi_file_selector.data.Folder;
import com.hochan.multi_file_selector.data.BaseFile;
import com.hochan.multi_file_selector.listener.MediaFileAdapterListener;
import com.hochan.multi_file_selector.loader.DataLoader;
import com.hochan.multi_file_selector.tool.ScreenTools;
import com.hochan.multi_file_selector.view.RecycleViewDivider;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Administrator on 2016/5/14.
 */
public class MultiFileSelectorFragment extends Fragment
        implements DataLoader.DataLoaderCallBack, MediaFileAdapterListener{

    public static final String TAG = "multi_image_selector";
    public static final String EXTRA_RESULT = "extra_result";

    private Context mContext;
    private DataLoader mLoaderCallback = null;

    private ImageAdapter mImageAdapter;
    private int mImageColumn = 4;
    private int mImageGap = 2;

    private LinearAdapter mLinearAdapter;

    private VideoAdapter mVideoAdapter;
    private int mVIdeoColumn = 2;
    private int mVideoGap = 2;

    private AllFileAdapter mAllFileAdapter;

    private FolderAdapter mFolderAdapter;
    private int mCurrentFolder = 0;
    private int mSelectType;

    //view
    private RecyclerView rclvMediaFiles;
    private Button btnOpera, btnArtists;
    private ListPopupWindow mFolderPopupWindow;
    private AVLoadingIndicatorView mProgressBar;
    private LinearLayout llFolder;

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

        mSelectType = getArguments().getInt(MultiFileSelectorActivity.TYPE_SELECT);
        System.out.println(mContext.getString(R.string.str_select_type)+mSelectType);

        rclvMediaFiles = (RecyclerView) view.findViewById(R.id.rclv_images);
        btnArtists = (Button) view.findViewById(R.id.btn_artists);
        //btnFolders = (Button) view.findViewById(R.id.btn_folders);
        btnOpera = (Button) view.findViewById(R.id.btn_opera);
        btnOpera.setText(R.string.str_cancle);

        mFolderAdapter = new FolderAdapter(mContext);
        mProgressBar = (AVLoadingIndicatorView) view.findViewById(R.id.avloadingIndicatorView);
        llFolder = (LinearLayout) view.findViewById(R.id.ll_folder);

        switch (mSelectType){
            case MultiFileSelectorActivity.TYPE_IMAGE:
                btnArtists.setVisibility(View.GONE);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, mImageColumn);
                rclvMediaFiles.setLayoutManager(gridLayoutManager);
                rclvMediaFiles.addItemDecoration(new RecycleViewDivider(mContext,
                        RecycleViewDivider.ORIENTATION_BOTH, ScreenTools.dip2px(mContext, mImageGap), 0));

                mImageAdapter = new ImageAdapter(mContext, mImageColumn);
                mImageAdapter.setImageAdpaterListener(this);
                rclvMediaFiles.setAdapter(mImageAdapter);
                rclvMediaFiles.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if(newState == RecyclerView.SCROLL_STATE_SETTLING)
                            Picasso.with(mContext).pauseTag(TAG);
                        else
                            Picasso.with(mContext).resumeTag(TAG);
                    }
                });
                break;
            case MultiFileSelectorActivity.TYPE_AUDIO:
                mLinearAdapter = new LinearAdapter(mContext, MultiFileSelectorActivity.TYPE_AUDIO);
                initRecyclerView();
                break;
            case MultiFileSelectorActivity.TYPE_MEDIANONE:
                mLinearAdapter = new LinearAdapter(mContext, MultiFileSelectorActivity.TYPE_MEDIANONE);
                initRecyclerView();
                break;
            case MultiFileSelectorActivity.TYPE_VIDEO:
                mLinearAdapter = new LinearAdapter(mContext, MultiFileSelectorActivity.TYPE_VIDEO);
                initRecyclerView();
                break;
            case MultiFileSelectorActivity.TYPE_ALL:
                llFolder.setVisibility(View.VISIBLE);
                mAllFileAdapter = new AllFileAdapter(mContext);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                rclvMediaFiles.setPadding(0, 0, 0, 0);
                rclvMediaFiles.addItemDecoration(new RecycleViewDivider(mContext,
                        LinearLayout.HORIZONTAL, 1, getResources().getColor(R.color.colorDivider)));
                rclvMediaFiles.setLayoutManager(linearLayoutManager);
                rclvMediaFiles.setAdapter(mAllFileAdapter);
                mAllFileAdapter.setmAdapterListener(this);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
        }

        btnOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBackImages();
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rclvMediaFiles.setPadding(0, 0, 0, 0);
        rclvMediaFiles.addItemDecoration(new RecycleViewDivider(mContext,
                LinearLayout.HORIZONTAL, 1, getResources().getColor(R.color.colorDivider)));
        rclvMediaFiles.setLayoutManager(linearLayoutManager);
        mLinearAdapter.setmAdapterListener(this);
        rclvMediaFiles.setAdapter(mLinearAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mSelectType != MultiFileSelectorActivity.TYPE_ALL) {
            mLoaderCallback = new DataLoader(getActivity(), mSelectType);
            mLoaderCallback.setCallBack(this);
            getActivity().getSupportLoaderManager().initLoader(
                    mSelectType, null, mLoaderCallback);
        }
    }

    private void createFolderPopupWindow(){
        mFolderPopupWindow = new ListPopupWindow(mContext);
        mFolderPopupWindow.setContentWidth(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        mFolderPopupWindow.setWidth(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setHeight(ScreenTools.getScreenWidth(mContext));
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        //mFolderPopupWindow.setAnchorView(rclvMediaFiles);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFolderPopupWindow.dismiss();
                switch (mSelectType){
                    case MultiFileSelectorActivity.TYPE_IMAGE:
                        mImageAdapter.setData((mFolderAdapter.getItem(position)).getmFiles());
                        break;
                    case MultiFileSelectorActivity.TYPE_AUDIO:
                    case MultiFileSelectorActivity.TYPE_MEDIANONE:
                    case MultiFileSelectorActivity.TYPE_VIDEO:
                        mLinearAdapter.setData(mFolderAdapter.getItem(position).getmFiles());
                        break;
                }
                mCurrentFolder = position;
                ((MultiFileSelectorActivity)getActivity()).setFolderName(mFolderAdapter.getItem(position).getmName());
            }
        });
    }

    @Override
    public void finish(List<BaseFile> baseFiles, List<Folder> folders) {
        switch (mSelectType) {
            case MultiFileSelectorActivity.TYPE_IMAGE:
                mImageAdapter.setData((ArrayList<BaseFile>) baseFiles);
                Folder folder = new Folder(MultiFileSelectorActivity.TYPE_IMAGE, mContext.getString(R.string.str_all_pics), null, baseFiles);
                folders.add(0, folder);
                mFolderAdapter.setData(folders);
                System.out.println(baseFiles.size());
                break;
            case MultiFileSelectorActivity.TYPE_AUDIO:
                mLinearAdapter.setData(baseFiles);
                Folder audioFolder = new Folder(MultiFileSelectorActivity.TYPE_AUDIO, mContext.getString(R.string.str_all_music), null, baseFiles);
                folders.add(0, audioFolder);
                mFolderAdapter.setData(folders);
                System.out.println(baseFiles.size());
                break;
            case MultiFileSelectorActivity.TYPE_VIDEO:
                mLinearAdapter.setData(baseFiles);
                Folder videoFolder = new Folder(MultiFileSelectorActivity.TYPE_VIDEO, mContext.getString(R.string.str_all_videos), null, baseFiles);
                folders.add(0, videoFolder);
                mFolderAdapter.setData(folders);
                break;
            case MultiFileSelectorActivity.TYPE_MEDIANONE:
                mLinearAdapter.setData(baseFiles);
                Folder noneMediaFolder = new Folder(MultiFileSelectorActivity.TYPE_MEDIANONE, mContext.getString(R.string.str_all_documents), null, baseFiles);
                folders.add(0, noneMediaFolder);
                mFolderAdapter.setData(folders);
                break;
            default:
                System.out.println("mSelectType:"+mSelectType);
                break;
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fileSelected(int selectedCount) {
        if(selectedCount > 0)
            btnOpera.setText(String.format(mContext.getString(R.string.str_finish), Integer.valueOf(selectedCount)));
        else
            btnOpera.setText(R.string.str_cancle);
    }

    @Override
    public void recordFolder(final java.io.File file) {
        final TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.textview_record_folder, null, false);
        textView.setText(file.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllFileAdapter.updateFolder(file);
                int index = llFolder.indexOfChild(textView);
                for(int i = index+1; i <= llFolder.getChildCount(); i++)
                    llFolder.removeView(llFolder.getChildAt(i));
            }
        });
        llFolder.addView(textView);
    }

    private void sendBackImages(){
        ArrayList<String> resultList = new ArrayList<>();
        switch (mSelectType){
            case MultiFileSelectorActivity.TYPE_IMAGE:
                if(mImageAdapter.getSelectedImages().size() == 0)
                    getActivity().finish();
                else{
                    for(BaseFile baseFile : mImageAdapter.getSelectedImages())
                        resultList.add(baseFile.getPath());
                }
                break;
            case MultiFileSelectorActivity.TYPE_AUDIO:
            case MultiFileSelectorActivity.TYPE_MEDIANONE:
            case MultiFileSelectorActivity.TYPE_VIDEO:
                if(mLinearAdapter.getmSelectedFiles().size() == 0)
                    getActivity().finish();
                else{
                    for(BaseFile baseFile : mLinearAdapter.getmSelectedFiles())
                        resultList.add(baseFile.getPath());
                }
                break;
            case MultiFileSelectorActivity.TYPE_ALL:
                if(mAllFileAdapter.getmSelectedFiled().size() == 0)
                    getActivity().finish();
                else{
                    for(java.io.File file : mAllFileAdapter.getmSelectedFiled())
                        resultList.add(file.getAbsolutePath());
                }
                break;
        }
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    public void showFolderPopupWindow(Toolbar toolbar){
        if(mFolderPopupWindow == null)
            createFolderPopupWindow();
        mFolderPopupWindow.setAnchorView(toolbar);
        if(mFolderPopupWindow.isShowing()){
            mFolderPopupWindow.dismiss();
        }else{
            mFolderPopupWindow.show();
            int index = mCurrentFolder == 0 ? 0 : mCurrentFolder -1;
            mFolderPopupWindow.getListView().setSelection(index);
        }
    }

}
