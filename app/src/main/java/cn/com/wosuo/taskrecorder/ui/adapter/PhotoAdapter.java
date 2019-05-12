package cn.com.wosuo.taskrecorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.vo.PhotoUpload;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private List<PhotoUpload> sPhotoUpload;
    private LayoutInflater layoutInflater;
    private List<String> initPaths;

    public PhotoAdapter(Context context, List<String> initPaths) {
        layoutInflater = LayoutInflater.from(context);
        sPhotoUpload = new ArrayList<>(initPaths.size());
        this.initPaths = initPaths;
    }

    public void addPaths(List<String> addPaths){
        initPaths.addAll(addPaths);
        notifyDataSetChanged();
//        TODO: Add info from image.
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_task_photo, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Picasso.get().load("file://" + initPaths.get(position)).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return initPaths == null ? 0 : initPaths.size();
    }


    public class PhotoHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.photo_type_spinner) Spinner mPhotoTypeSpinner;
//        @BindView(R.id.photo_detail_input) EditText mPhotoDetailEditText;
//        @BindView(R.id.local_image) ImageView mImageView;
        ImageView mImageView;
        Spinner mPhotoTypeSpinner;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoTypeSpinner = itemView.findViewById(R.id.photo_type_spinner);
            mImageView = itemView.findViewById(R.id.local_image);
        }

        void bind(String path){
            Picasso.get().load("file://" + path).into(mImageView);
        }
    }

    public List<PhotoUpload> retrieveData(){
        return sPhotoUpload;
    }
}
