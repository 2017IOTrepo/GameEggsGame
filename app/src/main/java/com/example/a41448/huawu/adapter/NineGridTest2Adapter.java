package com.example.a41448.huawu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.Contacts.NineGridTestModel;
import com.example.a41448.huawu.chatUI.utils.NineGridTestLayout;

import java.util.List;

public class NineGridTest2Adapter extends RecyclerView.Adapter<NineGridTest2Adapter.ViewHolder> {

    private Context mContext;
    private List<NineGridTestModel> mList;
    protected LayoutInflater inflater;

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
        View convertView = inflater.inflate( R.layout.item_nine_grid,parent,false );
        ViewHolder viewHolder = new ViewHolder( convertView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layout.setIsShowAll( mList.get( position ).isShowAll );
        holder.layout.setUrlList( mList.get( position ).urlList );
    }

    @Override
    public int getItemCount() {
        return getListSize( mList );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NineGridTestLayout layout;

        public ViewHolder(View itemView) {
            super( itemView );
            layout = (NineGridTestLayout) itemView.findViewById( R.id.layout_nine_grid);
        }
    }

    private int getListSize(List<NineGridTestModel> list){
        if (list == null || list.size() == 0){
            return 0;
        }
        return list.size();
    }
}
