package com.example.a41448.huawu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a41448.huawu.R;


public class GameFragment extends Fragment {

    public static GameFragment newInstance(){

        Bundle bundle = new Bundle(  );
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments( bundle );
        return new GameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_game,container,false );
        return view;
    }
}
