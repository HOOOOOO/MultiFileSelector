package com.hochan.multifileselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.MultiFileSelectorFragment;
import com.hochan.multi_file_selector.data.File;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private ImageButton btnSelectImage, btnSelectAudio, btnSelectVideo, btnSelectText;
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

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MultiFileSelectorActivity.class);
        switch (v.getId()){
            case R.id.btn_select_image:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_IMAGE);
                startActivityForResult(intent, File.TYPE_IMAGE);
                break;
            case R.id.btn_select_audio:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_AUDIO);
                startActivityForResult(intent, File.TYPE_AUDIO);
                break;
            case R.id.btn_select_video:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_VIDEO);
                startActivityForResult(intent, File.TYPE_VIDEO);
                break;
            case R.id.btn_select_text:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_MEDIANONE);
                startActivityForResult(intent, File.TYPE_MEDIANONE);
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
