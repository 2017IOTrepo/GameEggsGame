package com.example.a41448.huawu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Contacts.NineGridTestModel;
import com.example.a41448.huawu.Communication.utils.NineGridLayout;
import com.example.a41448.huawu.Communication.utils.NineGridTestLayout;
import com.example.a41448.huawu.view.activity.CommentActivity;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class NineGridTest2Adapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder> {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;
    View convertView;
    NineGridTestModel nineGridTestModel;

    public NineGridTest2Adapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from( context );
    }

    public void setList(List<NineGridTestModel> list){
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        convertView = inflater.inflate( R.layout.item_nine_grid,parent,false );
        ViewHolder viewHolder = new ViewHolder( convertView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        nineGridTestModel = mList.get( position );
        holder.layout.setIsShowAll( nineGridTestModel.isShowAll );
        holder.layout.setUrlList( nineGridTestModel.urlList );
        convertView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nineGridTestModel = mList.get( position );
                Intent intent = new Intent(mContext, CommentActivity.class );
                intent.putExtra("comment_name",nineGridTestModel.name);
                intent.putExtra( "comment_image",nineGridTestModel.imageUri );
                intent.putExtra( "comment_time",nineGridTestModel.time );
                intent.putExtra( "comment_detail",nineGridTestModel.detail );
                mContext.startActivity( intent );

            }
        } );
        Glide.with( mContext ).load(nineGridTestModel.image).into( holder.mImageView );
        holder.contact_name.setText( nineGridTestModel.name );
        holder.time.setText( nineGridTestModel.time );
        holder.detail.setText( nineGridTestModel.detail );

    }

    @Override
    public int getItemCount() {
        return getListSize( mList );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;
        ImageView mImageView,country_picture,comment;
        TextView contact_name,time,detail,like_size;
        ShineButton mShineButton;

        public ViewHolder(View itemView) {
            super( itemView );
            layout = (NineGridTestLayout) itemView.findViewById( R.id.layout_nine_grid);
            mImageView = (ImageView) itemView.findViewById(R.id.image );
            contact_name = (TextView) itemView.findViewById( R.id.name );
            time = (TextView) itemView.findViewById( R.id.time );
            detail = (TextView) itemView.findViewById( R.id.detail);
            mShineButton = (ShineButton) itemView.findViewById( R.id.po_image1 );
            like_size = (TextView) itemView.findViewById( R.id.like_size );
            comment = (ImageView) itemView.findViewById( R.id.comment );
        }
    }

    private int getListSize(List<NineGridTestModel> list){
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }
}
