package com.example.a41448.huawu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.example.a41448.huawu.adapter.SearchAdapter;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Search.SearchTag;

import java.util.ArrayList;

public class MessageFragment extends Fragment implements SearchView.OnQueryTextListener {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private ArrayList<SearchTag> mSearchTagArrayList,filteredDataList;
    private SearchAdapter mSearchAdapter;
    private EditText mEditText;



    public static Fragment newInstance(){
        Bundle bundle = new Bundle();
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);
        return messageFragment;
    }
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_message,container,false);
        initQuestion();
        mOnRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListaner(View v, int position) {
                SearchTag searchTag =  mSearchTagArrayList.get( position );
            }
        };
        mRecyclerView = (RecyclerView) view.findViewById( R.id.card_recycler_view );
        mRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ));
        mSearchAdapter = new SearchAdapter( mSearchTagArrayList,mOnRecyclerviewItemClickListener);
        mRecyclerView.setAdapter( mSearchAdapter );
        mRecyclerView.addItemDecoration( new DividerItemDecoration( getContext(), DividerItemDecoration.VERTICAL ) );
        SearchView mSearchView = (SearchView) view.findViewById( R.id.searchView );
        mSearchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filteredDataList = (ArrayList<SearchTag>) filter(mSearchTagArrayList, newText);
                mSearchAdapter.setFilter(filteredDataList);
                return true;
            }
        } );

        return view;
    }
    private void initQuestion() {
        mSearchTagArrayList = new ArrayList<>(  );
        mSearchTagArrayList.add(new SearchTag("中北大学安卓实验室","金浩：[图片]",R.drawable.icon,0,"下午8:29"));
        mSearchTagArrayList.add(new SearchTag("宇宙无敌项目组","李浩：刚看见",R.drawable.p1,0,"下午7:22"));
        mSearchTagArrayList.add(new SearchTag("1707004716事务通知群","李一帆：[图片]",R.drawable.apple_pic,0,"下午1:19"));
        mSearchTagArrayList.add(new SearchTag("中北大学学习交流群","小明：这个咋做啊？",R.drawable.orange_pic,0,"下午6:01"));
        mSearchTagArrayList.add(new SearchTag("中北大学安卓实验室","金浩：[图片]",R.drawable.icon,0,"下午8:29"));
        mSearchTagArrayList.add(new SearchTag("宇宙无敌项目组","李浩：刚看见",R.drawable.p1,0,"下午7:22"));
        mSearchTagArrayList.add(new SearchTag("1707004716事务通知群","李一帆：[图片]",R.drawable.apple_pic,0,"下午1:19"));
        mSearchTagArrayList.add(new SearchTag("中北大学学习交流群","小明：这个咋做啊？",R.drawable.orange_pic,0,"下午6:01"));

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filteredDataList = filter(mSearchTagArrayList,newText);
        mSearchAdapter.setFilter( filteredDataList );
        return true;
    }
    private ArrayList<SearchTag> filter(ArrayList<SearchTag> dataList, String newText) {
        newText = newText.toLowerCase();
        String text;
        filteredDataList = new ArrayList<>();
        for(SearchTag dataFromDataList:mSearchTagArrayList){
            text = dataFromDataList.title.toLowerCase();

            if(text.contains(newText)){
                filteredDataList.add(dataFromDataList);
            }
        }
        return filteredDataList;
    }
    public interface OnRecyclerviewItemClickListener{
        void onItemClickListaner(View v, int position);
    }
}
