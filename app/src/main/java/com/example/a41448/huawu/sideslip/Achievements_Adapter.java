package com.example.a41448.huawu.sideslip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a41448.huawu.R;

import java.util.List;

public class Achievements_Adapter extends RecyclerView.Adapter implements View.OnClickListener{
    private Context context;
    private List<Achievement.DataBean>dataBeanList;
    public Achievements_Adapter(Achievement achievement, List<Achievement.DataBean> lists) {
      this.context = achievement;
      this.dataBeanList = lists;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(context)
                .inflate(R.layout.cradview_achievement,parent,false);

        itemView.setOnClickListener(Achievements_Adapter.this);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position,List payloads) {
     ItemHolder itemHolder =(ItemHolder) holder;
     if (payloads.isEmpty()){
         itemHolder.itemView.setTag(position);
         itemHolder.imageView.setTag(position);
         itemHolder.textView.setTag(position);
         Achievement.DataBean dataBean = dataBeanList.get(position);
         itemHolder.textView.setText(dataBean.getText());
         itemHolder.imageView_class.setImageResource(dataBeanList.get(position).getImageView());
     }else{
         int type = (int)payloads.get(0);
         switch(type){
             case 0:
                 itemHolder.imageView_class.setImageResource(dataBeanList.get(position).getImageView());
                 break;
         }
     }
     itemHolder.imageView.setOnClickListener(Achievements_Adapter.this);
    }
    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public  class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public  ImageView imageView_class;


        public ItemHolder( View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_achievements);
            textView = itemView.findViewById(R.id.tv_achievement);
            imageView_class = itemView.findViewById(R.id.iv_class);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(View view, int position);
    }
    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();

        if (mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.iv_achievements:
                    mOnItemClickListener.onClick(view,position);
                    break;
                default:
                    break;
            }
        }
    }
}
