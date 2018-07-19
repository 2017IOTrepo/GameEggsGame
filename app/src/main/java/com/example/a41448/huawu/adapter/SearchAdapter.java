package com.example.a41448.huawu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a41448.huawu.chatUI.ui.ServiceChatActivity;
import com.example.a41448.huawu.view.fragment.MessageFragment;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Search.SearchTag;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private ArrayList<SearchTag>  mArrayList;
    public static final int WithoutImage = 1,WithImage = 0;

    private MessageFragment.OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener = null;

    public SearchAdapter(ArrayList<SearchTag> arrayList,MessageFragment.OnRecyclerviewItemClickListener mOnRecyclerviewItemClickListener){
        this.mArrayList = arrayList;
        this.mOnRecyclerviewItemClickListener = mOnRecyclerviewItemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mContext == null){
            mContext = parent.getContext();
        }
        View view ;
        switch (viewType){
            case WithImage:
                view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview1,parent,false);
                final  ViewHolder imageViewHolder = new ViewHolder(view);
                imageViewHolder.message_item.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = imageViewHolder.getAdapterPosition();
                        SearchTag searchTag =  mArrayList.get( position );
                        Intent intent = new Intent(mContext, ServiceChatActivity.class );
                        intent.putExtra( ServiceChatActivity.CONTACT_NAME, searchTag.title);
                        mContext.startActivity( intent );
                    }
                } );
                view.setOnClickListener( this );
                return imageViewHolder;
            case WithoutImage:
                view=LayoutInflater.from(parent.getContext()).inflate( R.layout.cardview1,parent,false);
                final  ViewHolder imageViewHolder_1 = new ViewHolder(view);
                imageViewHolder_1.message_item.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = imageViewHolder_1.getAdapterPosition();
                        SearchTag searchTag =  mArrayList.get( position );
                        Intent intent = new Intent(mContext, ServiceChatActivity.class );
                        intent.putExtra( ServiceChatActivity.CONTACT_NAME, searchTag.title);
                        mContext.startActivity( intent );
                    }
                } );
                view.setOnClickListener( this );
                return imageViewHolder_1;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (mArrayList.get(position).viewtype){
            case WithImage:
                ((ViewHolder) holder).message_item.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchTag searchTag =  mArrayList.get( position );
                        Intent intent = new Intent(mContext, ServiceChatActivity.class );
                        intent.putExtra( ServiceChatActivity.CONTACT_NAME, searchTag.title);
                        mContext.startActivity( intent );
                    }
                } );
                ((ViewHolder) holder).message_image.setImageResource(mArrayList.get(position).photo);
                ((ViewHolder) holder).message_name.setText(mArrayList.get(position).title);
                ((ViewHolder) holder).soontext.setText(mArrayList.get(position).about);
                ((ViewHolder) holder).message_time.setText(mArrayList.get(position).time);
                ((ViewHolder) holder).itemView.setTag( position);
                break;
            case WithoutImage:
                ((ViewHolder) holder).message_image.setImageResource(mArrayList.get(position).photo);
                ((ViewHolder) holder).message_name.setText(mArrayList.get(position).title);
                ((ViewHolder) holder).soontext.setText(mArrayList.get(position).about);
                ((ViewHolder) holder).message_time.setText(mArrayList.get(position).time);
                ((ViewHolder) holder).itemView.setTag( position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (mArrayList.get(position).viewtype == WithImage) {
            return WithImage;
        }else {
            return WithoutImage;
        }
    }

    @Override
    public void onClick(View v) {
        mOnRecyclerviewItemClickListener.onItemClickListaner( v,((int) v.getTag()) );
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView soontext;
        ImageView message_image;
        TextView message_name;
        TextView message_time;
        RelativeLayout message_item;


        public ViewHolder(View itemView) {
            super(itemView);
            message_item = (RelativeLayout) itemView.findViewById( R.id.message_item );
            message_image = (ImageView) itemView.findViewById( R.id.friend_icon );
            soontext = (TextView) itemView.findViewById( R.id.soonText );
            message_name = (TextView) itemView.findViewById( R.id.friend_name );
            message_time = (TextView) itemView.findViewById( R.id.time );
        }
    }

    public class WithoutImageViewHolder extends RecyclerView.ViewHolder{
        TextView soontext;
        ImageView message_image;
        TextView message_name;
        TextView message_time;
        CardView cardView;

        public WithoutImageViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.cardview2);
            message_image = (ImageView) itemView.findViewById( R.id.friend_icon );
            soontext = (TextView) itemView.findViewById( R.id.soonText );
            message_name = (TextView) itemView.findViewById( R.id.friend_name );
            message_time = (TextView) itemView.findViewById( R.id.time );}
    }

    public void setFilter(ArrayList<SearchTag> FilteredDataList) {
         mArrayList = FilteredDataList;
        notifyDataSetChanged();
    }

}
