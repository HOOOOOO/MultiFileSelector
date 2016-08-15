package com.hochan.multi_file_selector;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hochan.multi_file_selector.data.BaseFile;

import java.util.ArrayList;

public class MultiFileSelectorActivity extends AppCompatActivity {

    public final static String TYPE_SELECT = "type_select";
    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_AUDIO = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_MEDIANONE = 3;
    public final static int TYPE_ALL = 4;

    private int mSelectType = 0;

    private Toolbar mToolbar;
    private Button btnFolders;
    private MultiFileSelectorFragment mFragment;

    public final static ArrayList<String> TYPE_NAME = new ArrayList<>();

    static {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_selector);

        TYPE_NAME.add(TYPE_IMAGE, getString(R.string.str_picture));
        TYPE_NAME.add(TYPE_AUDIO, getString(R.string.str_music));
        TYPE_NAME.add(TYPE_VIDEO, getString(R.string.str_video));
        TYPE_NAME.add(TYPE_MEDIANONE, getString(R.string.str_doc));
        TYPE_NAME.add(TYPE_ALL, getString(R.string.str_file));

        mSelectType = getIntent().getIntExtra(TYPE_SELECT, 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btnFolders = (Button) findViewById(R.id.btn_folders);
        Bundle bundle = new Bundle();
        switch (mSelectType){
            case TYPE_IMAGE:
                mToolbar.setTitle(R.string.str_select_pics);
                bundle.putInt(TYPE_SELECT, TYPE_IMAGE);
                break;
            case TYPE_AUDIO:
                mToolbar.setTitle(R.string.str_select_music);
                bundle.putInt(TYPE_SELECT, TYPE_AUDIO);
                break;
            case TYPE_VIDEO:
                mToolbar.setTitle(R.string.str_select_video);
                bundle.putInt(TYPE_SELECT, TYPE_VIDEO);
                break;
            case TYPE_MEDIANONE:
                mToolbar.setTitle(R.string.str_select_doc);
                bundle.putInt(TYPE_SELECT, TYPE_MEDIANONE);
                break;
            case TYPE_ALL:
                mToolbar.setTitle(R.string.str_select_file);
                bundle.putInt(TYPE_SELECT, TYPE_ALL);
                btnFolders.setVisibility(View.GONE);
                break;
        }

        mFragment = (MultiFileSelectorFragment) Fragment.instantiate(this,
                MultiFileSelectorFragment.class.getName(),
                bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.rl_content,
                mFragment).commit();


        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFolders.setText(getString(R.string.str_all)+ TYPE_NAME.get(mSelectType));
        btnFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.showFolderPopupWindow(mToolbar);
            }
        });
    }

    public void setFolderName(String folder){
        btnFolders.setText(folder);
    }
}
