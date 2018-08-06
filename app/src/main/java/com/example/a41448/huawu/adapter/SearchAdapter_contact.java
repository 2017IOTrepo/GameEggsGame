package com.example.a41448.huawu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Contacts.Contact;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.utils.PlayersUtils;

import java.util.ArrayList;

public class SearchAdapter_contact extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Players> mArrayList;
    public static final int WithoutImage = 1,WithImage = 0;

    public SearchAdapter_contact(ArrayList<Players> arrayList){
        this.mArrayList = arrayList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate( R.layout.fragment_contacts_item,parent,false);
        ViewHolder imageViewHolder = new ViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).contact_image.setImageResource(R.drawable.chat_default_user_avatar);
        ((ViewHolder) holder).contact_name.setText(mArrayList.get(position).getUserAccontId());
        ((ViewHolder) holder).contact_sex.setText(PlayersUtils.setSex(mArrayList.get(position).isSex()));
        ((ViewHolder) holder).last_message.setText(mArrayList.get(position).getLastMessage());
        ((ViewHolder) holder).online.setImageResource(PlayersUtils.setOnline(mArrayList.get(position).isOnline()));
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contact_image;
        TextView contact_name;
        TextView contact_sex;
        TextView last_message;
        ImageView online;


        public ViewHolder(View itemView) {
            super(itemView);
            contact_image = (ImageView) itemView.findViewById( R.id.contact_image );
            contact_name = (TextView) itemView.findViewById( R.id.contact_name_text );
            contact_sex = (TextView) itemView.findViewById( R.id.sex_contacts );
            last_message = (TextView)itemView.findViewById(R.id.last_message);
            online = (ImageView)itemView.findViewById(R.id.contact_online);
        }
    }

    public void setFilter(ArrayList<Players> FilteredDataList) {
        mArrayList = FilteredDataList;
        notifyDataSetChanged();
    }

}