package com.example.a41448.huawu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.a41448.huawu.R;
import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;

import butterknife.ButterKnife;

public class GameFragment2 extends Fragment implements OnClickListener {

    int pageCount = 10;
    HollyViewPager hollyViewPager;

    public static GameFragment2 newInstance(){
        Bundle bundle = new Bundle( );
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments( bundle );
        return new GameFragment2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_game_2 ,container,false);
        ButterKnife.bind( getActivity() );
        hollyViewPager = view.findViewById( R.id.hollyViewPager );
        hollyViewPager.getViewPager().setPageMargin( getResources().getDimensionPixelOffset( R.dimen.viewpager_margin ) );
        hollyViewPager.setConfigurator( new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                return ((page + 4) % 10) / 10f;
            }
        } );
        hollyViewPager.setAdapter( new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ScrollViewFragment.newInstance( (String) getPageTitle( position ) );
            }
            @Override
            public int getCount() {
                return pageCount;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "TITLE" + position;
            }
        } );
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
