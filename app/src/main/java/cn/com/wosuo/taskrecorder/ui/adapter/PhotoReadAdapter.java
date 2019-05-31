package cn.com.wosuo.taskrecorder.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.CropSquareTransformation;
import cn.com.wosuo.taskrecorder.util.DateUtil;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.util.ImageTransformation;
import cn.com.wosuo.taskrecorder.vo.PhotoResult;

import static cn.com.wosuo.taskrecorder.api.Urls.HostString.API_HOST;

public class PhotoReadAdapter extends ListAdapter<PhotoResult, PhotoReadAdapter.PhotoHolder> {

    private final static String[] sPhotoTypeList = FinalMap.getPhotoTypeList();

    public PhotoReadAdapter() {
        super(diffUtilCallback);
    }

    private static DiffUtil.ItemCallback<PhotoResult> diffUtilCallback = new DiffUtil.ItemCallback<PhotoResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull PhotoResult oldItem, @NonNull PhotoResult newItem) {
            return oldItem.getPhotoID() == newItem.getPhotoID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PhotoResult oldItem, @NonNull PhotoResult newItem) {
            return oldItem.getPath().equals(oldItem.getPath());
        }
    };

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_read_photo, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        PhotoResult photoResult = getItem(position);
        holder.bind(photoResult);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mPhotoTagTV;
        TextView mPhotoDecTV;
        TextView mPhotoAuthorTV;
        TextView mPhotoDateTV;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.remote_image);
            mPhotoTagTV = itemView.findViewById(R.id.photo_tag_tv);
            mPhotoDecTV = itemView.findViewById(R.id.photo_detail_tv);
            mPhotoAuthorTV = itemView.findViewById(R.id.photo_author_tv);
            mPhotoDateTV = itemView.findViewById(R.id.photo_date_tv);
        }

        void bind(PhotoResult photoResult){
            String imageHttpPath = "https://" + API_HOST + photoResult.getPath();
            Picasso.get().load(imageHttpPath)
                    .placeholder(R.drawable.ic_autorenew_black_24dp)
                    .error(android.R.drawable.stat_notify_error)
                    .transform(ImageTransformation.getTransformation(mImageView))
                    .transform(new CropSquareTransformation())
                    .into(mImageView);
            mPhotoTagTV.setText(sPhotoTypeList[photoResult.getSubID()]);
            mPhotoDecTV.setText(photoResult.getDescription());
            mPhotoAuthorTV.setText(photoResult.getAuthor().getName());
            mPhotoDateTV.setText(DateUtil.unixTimestampToFullDateString(photoResult.getPhotoTime()));
        }

    }
}
