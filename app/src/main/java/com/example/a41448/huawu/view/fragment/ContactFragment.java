package com.example.a41448.huawu.view.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.utils.PlayersUtils;
import com.example.a41448.huawu.view.activity.ContactActivity;
import com.example.a41448.huawu.adapter.SearchAdapter_contact;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Contacts.Contact;
import com.example.a41448.huawu.base.Contacts.UserInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.datatype.BmobFile;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class ContactFragment extends Fragment implements SwipeRefreshLayout.OnClickListener,SearchView.OnQueryTextListener{
    private static String UserID = "11";
    private Context mContext;
    private static final String PAGE = "page";
    private static String country_selector = "国家";
    private static int OPENAGAIN = 0;
    private Button countryButton;
    //TabLayout的页数
    private int mPAge;
    private ArrayList<Players> filteredDataList;
    private SearchAdapter_contact mSearchAdapter;
    private EditText mEditText;
    //先获取联系人列表
    private  ArrayList<Players> contactList = new ArrayList<Players>();
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserInfo info;
    private String name;
    private String studyLanguage;
    private String nativeLanguage;
    private String languageLevel;

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;

    private Players[] mContacts= {

    };

    public static Fragment newInstance(int page){
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE,page);
        ContactFragment homeFragment = new ContactFragment();
        //用于防止因旋转而造成的Fragment重建
        homeFragment.setArguments(bundle);
        //返回一个Fragment
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        countryButton = (Button) view.findViewById( R.id.country_selector );
        mRecyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler_view_message);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration( new DividerItemDecoration( getContext(), DividerItemDecoration.VERTICAL ) );
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_contact);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContacts();
            }
        });
        countryButton.setOnClickListener( this );
        mSearchAdapter = new SearchAdapter_contact(contactList);
        SearchView searchView = (SearchView) view.findViewById( R.id.searchView_message );
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredDataList = (ArrayList<Players>) filter(contactList, newText);
                mSearchAdapter.setFilter(filteredDataList);
                return true;
            }
        } );
        OPENAGAIN = 1;
        //在创建View的同时更新UI
        updateUI();
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPAge = getArguments().getInt(PAGE);
        requestcontact();
        refreshContacts();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        filteredDataList = filter(contactList,newText);
        mSearchAdapter.setFilter( filteredDataList );
        return true;
    }
    private class ContactsHolder extends RecyclerView.ViewHolder{
        View contactView;
        ImageView contact_image;
        TextView contact_name;
        TextView contact_sex;
        TextView last_message;
        ImageView online;


        public ContactsHolder(View itemView) {
            super(itemView);
            contact_image = (ImageView) itemView.findViewById( R.id.contact_image );
            contact_name = (TextView) itemView.findViewById( R.id.contact_name_text );
            contact_sex = (TextView) itemView.findViewById( R.id.sex_contacts );
            last_message = (TextView)itemView.findViewById(R.id.last_message);
            online = (ImageView)itemView.findViewById(R.id.contact_online);
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactsHolder>{
        private List<Players> mContacts;

        public ContactAdapter(List<Players> contactList){
            mContacts = contactList;
        }

        @NonNull
        @Override
        public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_contacts_item,parent,false);
            final ContactsHolder holder = new ContactsHolder(view);

            holder.contactView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取点击位置的position
                    int position = holder.getAdapterPosition();
                    Players contact = contactList.get(position);
                    Intent intent = new Intent(mContext, ContactActivity.class);
                    intent.putExtra( ContactActivity.CONTACT_NAME,contact.getUserAccontId());
                    intent.putExtra(ContactActivity.CONTACT_IAMGE_ID,R.drawable.chat_default_user_avatar);
                    intent.putExtra( ContactActivity.USERID,UserID );

                    intent.putExtra( "nativeLanguage","1" );
                    intent.putExtra( "learnLanguage","1");
                    intent.putExtra(  "languageLevel","1");
                    mContext.startActivity(intent);
                }
            });
            holder.contact_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Players contact = contactList.get(position);
                }
            });
            return holder;
        }
        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {

            Players contact = mContacts.get(position);
            //Glide.with(getContext()).load(getContext().getDrawable(contact.getImagrId())).into(holder.mImageView);
//            holder.mImageView.setImageResource(contact.getImagrId());
            holder.contact_name.setText(contact.getUserAccontId());
            holder.online.setImageResource(PlayersUtils.setOnline(contact.isOnline()));
            holder.contact_image.setImageResource( R.drawable.chat_default_user_avatar );
            holder.contact_sex.setText(PlayersUtils.setSex(contact.isSex()));
            holder.last_message.setText( contact.getLastMessage() );

        }
        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        
    }

    //创建方法用于更新UI
    private void updateUI() {

        //绑定联系人的adapter
        mAdapter = new ContactAdapter(contactList);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void refreshContacts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //线程沉睡以便看到刷新的效果
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactList.clear();
                        Players contact = new Players( name, true, null,
                                null, true);
                        contactList.add( 0,contact );
                        mAdapter.notifyItemInserted( 0 );
                        mAdapter.notifyItemChanged(  0,0);
                        mRecyclerView.getLayoutManager().scrollToPosition( 0 );
                        for (int i = 0; i < 15; i++) {
                            Random random = new Random(  );
                            int index = random.nextInt(mContacts.length);
                            contactList.add(mContacts[index]);
                        }
                        mAdapter.notifyDataSetChanged();
                        //刷新结束后隐藏进度条
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

    }


    private void requestcontact(){
        OkGo.<String>post( "http://47.95.7.169:8080/getUserInfo")
                .tag( this )
                .isMultipart(true)
                .params( "UserID",UserID)
                .execute( new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i( "return","all"  + response.body());
                        info = JSON.parseObject(response.body(),UserInfo.class  );
                        name = info.getName();
                        studyLanguage = info.getStudyLanguage();
                        nativeLanguage = info.getNativeLanguage();
                        languageLevel = info.getLanguageLevel();
                        Log.i( "return", "name : " + info.getName() );
                        Log.i( "return","email : " + info.getEamil() );
                        Log.i( "return","id : " + info.getUserID() );
                        UserID += 1;

                    }
                } );

    }
//    private void reinitContacts(List<Contact>  contactList){
//        contactList.clear();
//        Contact contact = new Contact( name,R.drawable.picture,R.drawable.ic_dot_24dp,
//                studyLanguage,nativeLanguage,languageLevel);
//        contactList.add( 0,contact );
//        mAdapter.notifyItemInserted( 0 );
//        mRecyclerView.getLayoutManager().scrollToPosition( 0 );
//        for (int i = 0; i < 10; i++) {
//            Random random = new Random(  );
//            int index = random.nextInt(mContacts.length);
//            contactList.add(mContacts[index]);
//        }
//
//    }

    private ArrayList<Players> filter(ArrayList<Players> dataList, String newText) {
        newText = newText.toLowerCase();
        String text_1,text_2,text_3;
        filteredDataList = new ArrayList<>( );
        for(Players dataFromDataList:contactList){
            text_1 = dataFromDataList.getUserAccontId().toLowerCase();

            if(text_1.contains(newText) ){
                filteredDataList.add(dataFromDataList);
            }
        }
        return filteredDataList;
    }

    /**
     * 显示单选对话框
     */
    public void showSingleChioceDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("选择国家:");
        builder.setIcon(R.mipmap.ic_launcher);
        final String[] items = new String[]{"中国","美国",
                "日本","韩国","法国","俄国","泰国","阿拉伯","越南","希腊"};
        builder.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {/*设置单选条件的点击事件*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                country_selector = items[which];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                countryButton.setText( country_selector );


            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "CANCEL", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.country_selector:
                showSingleChioceDialog( getView() );
                break;
            default:
                break;
        }
    }

}

