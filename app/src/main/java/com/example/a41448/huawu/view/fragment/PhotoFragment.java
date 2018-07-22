package com.example.a41448.huawu.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.utils.PictureUtils;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

public class PhotoFragment extends DialogFragment {
    public static final String EXTRA_PHOTO_PATH = "file";
    private ImageView imageview;

    public static PhotoFragment newInstance(URL url){
        Bundle args = new Bundle(  );
        args.putSerializable( EXTRA_PHOTO_PATH, (Serializable) url );
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Uri uri = (Uri) getArguments().getSerializable( EXTRA_PHOTO_PATH);
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.dialog_photo,null);
        imageview = (ImageView) view.findViewById( R.id.crime_photo );
        Glide.with( getContext() ).load( uri ).into( imageview );
        return new AlertDialog.Builder( getActivity() )
                .setView( view )
                .setPositiveButton(android.R.string.ok,null )
                .create();
    }


}
