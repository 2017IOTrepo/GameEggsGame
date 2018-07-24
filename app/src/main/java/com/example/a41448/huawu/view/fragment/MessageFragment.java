package com.example.a41448.huawu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

import java.util.ArrayList;
import java.util.Random;

public class MessageFragment extends Fragment implements SearchView.OnQueryTextListener {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private ArrayList<SearchTag> mSearchTagArrayList,filteredDataList;
    private SearchAdapter mSearchAdapter;
    private EditText mEditText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener ;

    public static Fragment newInstance(){
        Bundle bundle = new Bundle();
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);
        return messageFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_message,container,false);
        initQuestions();
        mOnRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
            @Override
            public void onItemClickListaner(View v, int position) {
                SearchTag searchTag =  mSearchTagArrayList.get( position );
            }
        };
        mRecyclerView = (RecyclerView) view.findViewById( R.id.card_recycler_view );
        mRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ));
        mRecyclerView.addItemDecoration( new DividerItemDecoration( getContext(), DividerItemDecoration.VERTICAL ) );
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.swipe_refresh_message );
        mSwipeRefreshLayout.setColorSchemeResources( R.color.orange );
        mSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        } );
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

        updateUI();
        return view;
    }

    private void updateUI(){
        mSearchAdapter = new SearchAdapter( mSearchTagArrayList,mOnRecyclerviewItemClickListener);
        mRecyclerView.setAdapter( mSearchAdapter );
    }
    private void initQuestions() {
        mSearchTagArrayList = new ArrayList<>(  );
        mSearchTagArrayList.add(new SearchTag("中北大学安卓实验室","金浩：[图片]",
                R.drawable.message_1,0,"下午8:29"));
        mSearchTagArrayList.add(new SearchTag("宇宙无敌项目组","李浩：刚看见",
                R.drawable.message_2,0,"上午7:22"));
        mSearchTagArrayList.add(new SearchTag("1707004716事务通知群","李一帆：[图片]",
                R.drawable.message_3,0,"下午1:19"));
        mSearchTagArrayList.add(new SearchTag("中北大学学习交流群","小明：这个咋做啊？",
                R.drawable.picture_4,0,"上午6:01"));
        mSearchTagArrayList.add(new SearchTag("丁逸群","有一个未接电话",
                R.drawable.picture_11,0,"昨天"));
        mSearchTagArrayList.add(new SearchTag("武智鹏","你刚刚去哪了",
                R.drawable.p1,0,"昨天"));
        mSearchTagArrayList.add(new SearchTag("魏祥一","我在唐久这儿",
                R.drawable.picture_6,0,"星期一"));
        mSearchTagArrayList.add(new SearchTag("马骕骎","有点慌......",
                R.drawable.picture_4,0,"星期日"));
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

    private void refreshMessage(){
        new Thread( new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep( 2000 );
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.getLayoutManager().scrollToPosition( 0 );
                        mSearchTagArrayList.clear();
                        for (int i = 0; i < 10; i++) {
                            Random random = new Random( );
                            int index = random.nextInt( mQuestions.length );
                            mSearchTagArrayList.add( mQuestions[index] );
                        }
                        mSearchAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing( false );
                    }
                } );
            }
        } );
    }

    //基础基类
    private SearchTag[] mQuestions = {
            new SearchTag("中北大学安卓实验室","金浩：[图片]",
                    R.drawable.message_1,0,"下午8:29"),
            new SearchTag("宇宙无敌项目组","李浩：刚看见",
                    R.drawable.message_2,0,"上午7:22"),
            new SearchTag("1707004716事务通知群","李一帆：[图片]",
                    R.drawable.message_3,0,"下午1:19"),
            new SearchTag("中北大学学习交流群","小明：这个咋做啊？",
                    R.drawable.picture_4,0,"上午6:01"),
            new SearchTag("丁逸群","有一个未接电话",
                    R.drawable.picture_11,0,"昨天"),
            new SearchTag("武智鹏","你刚刚去哪了",
                    R.drawable.p1,0,"昨天"),
            new SearchTag("魏祥一","我在唐久这儿",
                    R.drawable.picture_6,0,"星期一"),
            new SearchTag("马骕骎","有点慌......",
                    R.drawable.picture_4,0,"星期日")
    };

}
