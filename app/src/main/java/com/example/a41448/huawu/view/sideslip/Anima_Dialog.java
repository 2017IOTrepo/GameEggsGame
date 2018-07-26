package com.example.a41448.huawu.view.sideslip;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.a41448.huawu.R;

/**
 * Created by Administrator on 2017/3/29.
 */

public class Anima_Dialog extends DialogFragment {
    private Button btn1;
    public static  Anima_Dialog getIntent(){
        Anima_Dialog df =new Anima_Dialog();
        return df;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.maina,null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn1 = (Button) v.findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.animotion);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        v.startAnimation(animation);
        return v;
    }

    @Override
    public void onStart() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        super.onStart();
    }


}
