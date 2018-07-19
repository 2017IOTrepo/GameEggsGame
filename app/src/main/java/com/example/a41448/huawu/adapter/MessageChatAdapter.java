package com.example.a41448.huawu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Message.ChatMessageBean;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private List<ChatMessageBean> userList = new ArrayList<ChatMessageBean>(  );
    private ArrayList<String> imageList = new ArrayList<>();
    private HashMap<Integer,Integer> imagePosition = new HashMap<>(  );
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型
    private int mMinItemWidth;
    private int mMaxIItemWidth;
    public Handler handler;
    private Animation an;
    private SendErrorListener sendErrorListener;
    private VoiceIsRead voiceIsRead;
    public List<String> unReadPosition = new ArrayList<String>();
    private int voicePlayPosition = -1;
    private LayoutInflater mLayoutInflater;
    private boolean isGif = true;




    public interface SendErrorListener {
        public void onClick(int position);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }

    public interface VoiceIsRead {
        public void voiceOnClick(int position);
    }

    public MessageChatAdapter(Context context,List<ChatMessageBean> userList){
        this.context = context;
        this.userList = userList;
        mLayoutInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxIItemWidth = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
        handler = new Handler();
    }


    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setImagePosition(HashMap<Integer, Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case TO_USER_MSG:
                view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_msgto_list_item ,parent,false);
                holder = new ToUserMsgViewHolder( view );
                break;
            case TO_USER_IMG:
                view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.layout_imageto_list_item,parent,false);
                holder = new ToUserImgViewHolder( view );
                break;
            case TO_USER_VOICE:
                view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_recorder,parent,false );
                holder = new ToUserVoiceViewHolder( view );
                break;
        }
        return holder;
    }



    class ToUserMsgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;

        private TextView chat_time;

        private TextView chat_text;

        public ToUserMsgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view.findViewById( R.id.chat_icon );
            chat_time = (TextView) view.findViewById( R.id.chat_time );
            chat_text = (TextView) view.findViewById( R.id.msg_context );

        }
    }

    class ToUserImgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;

        private TextView chat_time;

        private ImageView chat_image;

        private LinearLayout image_group;

        public ToUserImgViewHolder(View view) {
            super( view );
            headicon = (ImageView) view.findViewById( R.id.chat_image_icon );
            chat_time = (TextView) view.findViewById( R.id.chat_image_time );
            chat_image = (ImageView) view.findViewById( R.id.chat_image);
            image_group = (LinearLayout) view.findViewById( R.id.right_image_layout );
        }
    }

    class ToUserVoiceViewHolder extends RecyclerView.ViewHolder {

        ImageView headicon;
        TextView seconds;
        View length;

        public ToUserVoiceViewHolder(View view) {
            super( view );
            seconds = (TextView) view.findViewById(R.id.id_recorder_time);
            length = view.findViewById(R.id.id_recorder_length);
            headicon = (ImageView) view.findViewById( R.id.id_icon );
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageBean tbub = userList.get( position );
        int itemViewType = getItemViewType( position );
        switch (itemViewType){
            case TO_USER_MSG:
                toMsgUserLayout((ToUserMsgViewHolder) holder, tbub, position);
                break;
            case TO_USER_IMG:
                toImgUserLayout((ToUserImgViewHolder) holder, tbub, position);
                break;
            case TO_USER_VOICE:

                break;
        }

    }


    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
         if (position != 0){
             String showtime = getTime( tbub.getContent_time(),userList.get( position - 1 ).getContent() );
             if (showtime != null){
                 holder.chat_time.setVisibility( View.VISIBLE );
                 holder.chat_time.setText( showtime );
             }else {
                 holder.chat_time.setVisibility( View.GONE );
             }
         }else {
             String showTimme = getTime( tbub.getContent_time(),null );
             holder.chat_time.setVisibility( View.VISIBLE );
             holder.chat_time.setText( showTimme );
         }
         holder.chat_text.setVisibility( View.VISIBLE );
         holder.chat_text.setText( tbub.getContent());
    }

    private void toImgUserLayout(final ToUserImgViewHolder holder, final ChatMessageBean tbub, final int position) {


    }



    public String getTime(String time,String before){
        String show_time = null;
        if (before != null){
            try{
                DateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                java.util.Date now = df.parse( time );
                java.util.Date date = df.parse( before );
                long l = now.getTime() - date.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min >= 1) {
                    show_time = time.substring( 11 );
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            show_time = time.substring( 11 );
        }
        String getDay = getDay( time );
        if (show_time != null && getDay != null)
            show_time = getDay + " " + show_time;
        return show_time;
    }

    public String getDay(String time) {
        String showDay = null;
        String nowTime = returnTime();
        try {
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            java.util.Date now = df.parse( nowTime );
            java.util.Date date = df.parse( time );
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            if (day >= 365) {
                showDay = time.substring( 0, 10 );
            }else if (day >= 1 && day < 365){
                showDay = time.substring( 5,10 );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return showDay;
    }
    public static String returnTime () {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss" );
            String date = sDateFormat.format( new java.util.Date() );
            return date;
    }

    @Override
    public int getItemCount () {
        return 0;
    }
}
