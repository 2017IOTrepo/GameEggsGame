package com.example.a41448.huawu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Message.Msg;;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View itemView) {
            super( itemView );
            leftLayout = (LinearLayout) itemView.findViewById( R.id.left_layout);
            rightLayout = (LinearLayout) itemView.findViewById( R.id.right_layout );
            leftMsg = (TextView) itemView.findViewById( R.id.left_msg);
            rightMsg = (TextView) itemView.findViewById( R.id.right_msg );
        }
    }
    public MsgAdapter(List<Msg> msgList){
        mMsgList = msgList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.msg_item,parent,false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Msg msg  = mMsgList.get( position );
        if (msg.getType() == Msg.TYPE_RECEVIED){
            //如果是收到的消息，则显示左边的布局，隐藏右边的布局
            holder.leftLayout.setVisibility( View.VISIBLE );
            holder.rightLayout.setVisibility( View.GONE );
            holder.leftMsg.setText( msg.getContent() );
        }else if(msg.getType() == Msg.TYOE_SEND){
            //如果是右边发送的消息，则显示右边的消息，隐藏左边的消息
            holder.rightLayout.setVisibility( View.VISIBLE );
            holder.leftLayout.setVisibility( View.GONE );
            holder.rightMsg.setText( msg.getContent() );
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mMsgList.get( position ).getType();
    }


    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
