package com.example.a41448.huawu.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a41448.huawu.R;

import com.example.a41448.huawu.tools.manager.BuilderManager;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;


public class GameFragment extends Fragment {

    private BoomMenuButton bmb,bmb_1;
    private static ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    private ArrayList<Pair> piecesAndButtons_1 = new ArrayList<>();

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
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bmb.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons.get(position).first);
                bmb.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons.get(position).second);
                bmb.clearBuilders();
                for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
                    bmb.addBuilder( BuilderManager.getHamButtonBuilder() );
                }
            }
        } );


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
        listView_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bmb_1.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons_1.get(position).first);
                bmb_1.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons_1.get(position).second);
                bmb_1.clearBuilders();
                for (int i = 0; i < bmb_1.getPiecePlaceEnum().pieceNumber(); i++)
                    bmb_1.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());
            }
        });
        return view;
    }
}
