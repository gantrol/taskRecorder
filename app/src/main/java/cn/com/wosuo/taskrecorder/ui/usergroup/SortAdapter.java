package cn.com.wosuo.taskrecorder.ui.usergroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.UserSortModel;


public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<UserSortModel> mData;
    private Context mContext;

    public SortAdapter(Context context, List<UserSortModel> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
    }

    @Override
    public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTag = view.findViewById(R.id.tag);
        viewHolder.tvName = view.findViewById(R.id.name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SortAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getInitial());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v ->
                    mOnItemClickListener.onItemClick(holder.itemView, position));

        }

        holder.tvName.setText(this.mData.get(position).getName());

        holder.tvName.setOnClickListener(view ->
                Toast.makeText(mContext, mData.get(position).getName(),Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     */
    public void updateList(List<UserSortModel> list){
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getInitial().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getInitial();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}