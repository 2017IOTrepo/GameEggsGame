package com.example.a41448.huawu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Track;

import java.util.ArrayList;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder>{


    private Context context;
    private ArrayList<Track> mTracks;

    public LinearAdapter(Context context,ArrayList<Track> tracks){
        this.context = context;
        this.mTracks = tracks;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearViewHolder view = new LinearViewHolder( LayoutInflater.from(context).inflate( R.layout.track_item,parent,false ) );
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {
        Track track = mTracks.get( position );
        holder.beginTime.setText( track.getBeginTime() );
        holder.beginPlace.setText( track.getBeginPlace() );
        holder.finishTime.setText( track.getFinishTime() );
        holder.finishPlace.setText( track.getFinishPlace() );
        holder.start_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );
        holder.delete_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );
        holder.share_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        } );
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView beginTime,beginPlace,finishTime,finishPlace;
        private Button start_button,delete_button,share_button;

        public LinearViewHolder(View itemView) {
            super( itemView );
            beginTime = itemView.findViewById( R.id.track_begin_time );
            beginPlace = itemView.findViewById( R.id.track_begin );
            finishTime = itemView.findViewById( R.id.track_finish_time );
            finishPlace = itemView.findViewById( R.id.track_finish );
            start_button = (Button) itemView.findViewById( R.id.track_mark );
            delete_button = (Button) itemView.findViewById( R.id.track_delete );
            share_button = (Button) itemView.findViewById( R.id.track_share );
        }
    }
}
