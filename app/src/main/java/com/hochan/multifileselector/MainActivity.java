package com.hochan.multifileselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.MultiFileSelectorFragment;
import com.hochan.multi_file_selector.data.BaseFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private ImageButton btnSelectImage, btnSelectAudio, btnSelectVideo,
            btnSelectText, btnSelectAll;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        btnSelectImage = (ImageButton) findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(this);
        btnSelectAudio = (ImageButton) findViewById(R.id.btn_select_audio);
        btnSelectAudio.setOnClickListener(this);
        btnSelectVideo = (ImageButton) findViewById(R.id.btn_select_video);
        btnSelectVideo.setOnClickListener(this);
        btnSelectText = (ImageButton) findViewById(R.id.btn_select_text);
        btnSelectText.setOnClickListener(this);
        btnSelectAll = (ImageButton) findViewById(R.id.btn_select_all);
        btnSelectAll.setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MultiFileSelectorActivity.class);
        switch (v.getId()){
            case R.id.btn_select_image:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, BaseFile.TYPE_IMAGE);
                startActivityForResult(intent, BaseFile.TYPE_IMAGE);
                break;
            case R.id.btn_select_audio:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, BaseFile.TYPE_AUDIO);
                startActivityForResult(intent, BaseFile.TYPE_AUDIO);
                break;
            case R.id.btn_select_video:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, BaseFile.TYPE_VIDEO);
                //startActivityForResult(intent, BaseFile.TYPE_VIDEO);
                break;
            case R.id.btn_select_text:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, BaseFile.TYPE_MEDIANONE);
                startActivityForResult(intent, BaseFile.TYPE_MEDIANONE);
                break;
            case R.id.btn_select_all:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, BaseFile.TYPE_ALL);
                startActivityForResult(intent, BaseFile.TYPE_ALL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ArrayList<String> resultList = data.getStringArrayListExtra(
                    MultiFileSelectorFragment.EXTRA_RESULT);
            for(String str : resultList){
                System.out.println(str);
                tvResult.append(str+"\n");
            }
        }
    }
}
