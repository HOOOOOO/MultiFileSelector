package com.hochan.multi_file_selector;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MultiFileSelectorActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_selector);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("选择图片");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.rl_content,
                Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), null)).commit();
    }
}
