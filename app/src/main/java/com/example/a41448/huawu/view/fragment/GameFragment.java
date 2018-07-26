package com.example.a41448.huawu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.a41448.huawu.R;

import com.example.a41448.huawu.tools.manager.BuilderManager;
import com.example.a41448.huawu.view.activity.gamehallActivity;
import com.example.a41448.huawu.view.activity.mainGameActivity;
import com.example.a41448.huawu.view.activity.pTopActivity;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;


public class GameFragment extends Fragment implements View.OnClickListener{

    private BoomMenuButton bmb,bmb_1;
    private static ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    private ArrayList<Pair> piecesAndButtons_1 = new ArrayList<>();

    private CardView hallBtn,ptopBtn,modelBtn;


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
        //炫酷BoomButton的初始化
        initBoombtn( view );
        initBtn(view);

        return view;
    }

    private void initBtn(View view){
        hallBtn = (CardView) view.findViewById( R.id.game_hall );
        ptopBtn = (CardView) view.findViewById( R.id.pTopWith );
        modelBtn = (CardView) view.findViewById( R.id.model );
        hallBtn.setOnClickListener( this );
        ptopBtn.setOnClickListener( this );
        modelBtn.setOnClickListener( this );
    }

    private void initBoombtn(View view){
        bmb = (BoomMenuButton) view.findViewById( R.id.bmb );
        assert bmb != null;
        bmb.setButtonEnum( ButtonEnum.Ham );
        bmb.setPiecePlaceEnum( PiecePlaceEnum.HAM_1 );
        bmb.setButtonPlaceEnum( ButtonPlaceEnum.HAM_1 );
        bmb.addBuilder( BuilderManager.getHamButtonBuilder());

        ListView listView = (ListView) view.findViewById( R.id.list_view );
        assert listView != null;
        listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1,
                BuilderManager.getHamButtonData(piecesAndButtons)));
//        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            }
//        } );
        bmb.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons.get(9).first);
        bmb.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons.get(9).second);
        bmb.clearBuilders();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            bmb.addBuilder( BuilderManager.getHamButtonBuilder() );
        }

        bmb_1 = (BoomMenuButton) view.findViewById(R.id.bmb_1);
        assert bmb != null;
        bmb_1.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb_1.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        bmb_1.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
        bmb_1.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());

        ListView listView_1 = (ListView) view.findViewById( R.id.list_view_1 );
        assert listView_1 != null;
        listView_1.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1,
                BuilderManager.getCircleButtonData(piecesAndButtons_1)));
//        listView_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e( "wanghao", String.valueOf( position ) );
//
//            }
//        });
        bmb_1.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons_1.get(228).first);
        bmb_1.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons_1.get(228).second);
        bmb_1.clearBuilders();
        for (int i = 0; i < bmb_1.getPiecePlaceEnum().pieceNumber(); i++)
            bmb_1.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.game_hall:
                startActivity( new Intent( getContext(),gamehallActivity.class ) );
                break;
            case R.id.pTopWith:
                startActivity( new Intent( getContext(),pTopActivity.class ) );
                break;
            case R.id.model:
                startActivity( new Intent( getContext(),mainGameActivity.class ) );
                break;
            default:

                break;
        }

    }
}
