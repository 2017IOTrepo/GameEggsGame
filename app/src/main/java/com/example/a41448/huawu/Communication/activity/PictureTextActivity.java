package com.example.a41448.huawu.Communication.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a41448.huawu.Communication.utils.Constants;
import com.example.a41448.huawu.Communication.utils.PhotoUtils;
import com.example.a41448.huawu.R;

public class PictureTextActivity extends AppCompatActivity {


    private ImageView mImageView;

    private TextView mTextView;

    private ScrollView mScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_picture_text );

        mScrollView = (ScrollView) findViewById( R.id.pic_to_text );
        mImageView = (ImageView) findViewById( R.id.image_view );
        mTextView = (TextView) findViewById( R.id.text_view );
        mTextView.setSelected( true );
        mTextView.setTextIsSelectable( true );

        Intent intent = getIntent();
        String path = intent.getStringExtra( "path" );
        String text = intent.getStringExtra( "text" );
        String url = intent.getStringExtra( Constants.IMAGE_URL);
        mTextView.setText( text );
        PhotoUtils.displayImageCacheElseNetwork(mImageView, path, url);
        //添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle( "文字识别" );
        }
        mScrollView.smoothScrollTo( 0,0 );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
