package com.example.a41448.huawu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.example.a41448.huawu.R;

import java.io.FileNotFoundException;

public class PrePhototActivity extends AppCompatActivity {


    ImageView picture_uri;
    public static final String IMAGE_URI = "image_uri";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pre_photot );
        picture_uri = (ImageView) findViewById( R.id.picture_1 );
        Intent intent = getIntent();
        Uri imageUri = Uri.parse( intent.getStringExtra( IMAGE_URI ) );
        try{
            Bitmap bitmap = BitmapFactory.decodeStream( getContentResolver().openInputStream( imageUri ) );
            picture_uri.setImageBitmap( bitmap );
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }
}
