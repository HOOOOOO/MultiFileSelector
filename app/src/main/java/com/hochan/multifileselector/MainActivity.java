package com.hochan.multifileselector;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.MultiImageSelectorFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private Button btnSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_select_image:
                intent.setClass(this, MultiFileSelectorActivity.class);
                break;
        }
        startActivity(intent);
    }
}
