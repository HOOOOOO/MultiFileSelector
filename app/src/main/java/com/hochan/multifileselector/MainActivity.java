package com.hochan.multifileselector;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.MultiImageSelectorFragment;
import com.hochan.multi_file_selector.data.MediaFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private Button btnSelectImage, btnSelectAudio, btnSelectVideo, btnSelectText;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(this);
        btnSelectAudio = (Button) findViewById(R.id.btn_select_audio);
        btnSelectAudio.setOnClickListener(this);
        btnSelectVideo = (Button) findViewById(R.id.btn_select_video);
        btnSelectVideo.setOnClickListener(this);
        btnSelectText = (Button) findViewById(R.id.btn_select_text);
        btnSelectText.setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, MultiFileSelectorActivity.class);
        switch (v.getId()){
            case R.id.btn_select_image:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, MediaFile.TYPE_IMAGE);
                startActivityForResult(intent, MediaFile.TYPE_IMAGE);
                break;
            case R.id.btn_select_audio:
                intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, MediaFile.TYPE_AUDIO);
                startActivityForResult(intent, MediaFile.TYPE_AUDIO);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ArrayList<String> resultList = data.getStringArrayListExtra(
                    MultiImageSelectorFragment.EXTRA_RESULT);
            for(String str : resultList){
                System.out.println(str);
                tvResult.append(str+"\n");
            }
        }
    }
}
