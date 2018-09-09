package com.example.a41448.huawu.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder>{
    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new LinearViewHolder(  )
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LinearViewHolder extends ViewHolder{
        public LinearViewHolder(View itemView) {
            super( itemView );
        }
    }
}
