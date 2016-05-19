package com.hochan.multi_file_selector;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hochan.multi_file_selector.data.MediaFile;

public class MultiFileSelectorActivity extends AppCompatActivity {

    public final static String TYPE_SELECT = "type_select";

    private int mSelectType = 0;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_selector);

        mSelectType = getIntent().getIntExtra(TYPE_SELECT, 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundle = new Bundle();
        switch (mSelectType){
            case MediaFile.TYPE_IMAGE:
                mToolbar.setTitle("选择图片");
                bundle.putInt(TYPE_SELECT, MediaFile.TYPE_IMAGE);
                break;
            case MediaFile.TYPE_AUDIO:
                mToolbar.setTitle("选择音乐");
                bundle.putInt(TYPE_SELECT, MediaFile.TYPE_AUDIO);
                break;
            case MediaFile.TYPE_VIDEO:
                mToolbar.setTitle("选择视频");
                break;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.rl_content,
                Fragment.instantiate(this,
                        MultiImageSelectorFragment.class.getName(),
                        bundle)).commit();


        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
