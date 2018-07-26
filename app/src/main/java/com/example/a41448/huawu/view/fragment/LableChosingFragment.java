package com.example.a41448.huawu.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.view.activity.MainActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.a41448.huawu.R.menu.lable_chosing_menu;


public class LableChosingFragment extends Fragment {
    private View view;
    private GridView hasChosedGridView;
    private GridView notChosedGridView;
    private Toolbar lableToolbar;
    private Players players;
    private List<String> lables;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lable_chosing_fragment, container, false);
        initView();
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(lableToolbar);
        initToolbar();

        return view;
    }

    @Override
    public void onDestroy() {
        update();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lable_chosing_menu, menu);
    }

    private void initToolbar() {

        lableToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                players.setLables(lables);

                switch (item.getItemId()){
                    case R.id.skip_lable_chose:

                        break;

                    case R.id.complete_lable_chose:
                        players.setFirstLogin(false);
                        break;
                }

                update();
                return true;
            }
        });
    }

    private void update() {
        players.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    MainActivity.startActivity(context);
                }else {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        lables = new ArrayList<>();
        players = BmobUser.getCurrentUser(Players.class);
        context = getContext();
        hasChosedGridView = (GridView) view.findViewById(R.id.label_check_gv);
        notChosedGridView = (GridView) view.findViewById(R.id.label_gv);
        lableToolbar = (Toolbar)view.findViewById(R.id.lable_toolbar);
    }
}
