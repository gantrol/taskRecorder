package cn.com.wosuo.taskrecorder.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.com.wosuo.taskrecorder.R;
import cn.com.wosuo.taskrecorder.util.FinalMap;
import cn.com.wosuo.taskrecorder.vo.PhotoUpload;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private String[] sPhotoType = FinalMap.getPhotoTypeList();
    private List<PhotoUpload> sPhotoUpload;
    private LayoutInflater layoutInflater;
    private int taskID;
//    private List<String> initPaths;

    public PhotoAdapter(Context context, List<String> initPaths, int taskID) {
        layoutInflater = LayoutInflater.from(context);
        sPhotoUpload = new ArrayList<>(initPaths.size());
        this.taskID = taskID;
//        this.initPaths = initPaths;
    }

    public void addPaths(List<String> addPaths){
//        initPaths.addAll(addPaths);
        for (String path: addPaths){
            PhotoUpload photoUpload = new PhotoUpload();
            photoUpload.setPath(path);
            photoUpload.setTaskID(taskID);
            sPhotoUpload.add(photoUpload);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_task_photo, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.bind(sPhotoUpload.get(position), position);
    }

    @Override
    public int getItemCount() {
        return sPhotoUpload == null ? 0 : sPhotoUpload.size();
    }


    public class PhotoHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        Spinner mPhotoTypeSpinner;
        TextInputEditText mPhotoDetailInput;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.local_image);
            mPhotoTypeSpinner = itemView.findViewById(R.id.photo_type_spinner);
            mPhotoTypeSpinner.setAdapter(
                    new ArrayAdapter<>(itemView.getContext(),
                            android.R.layout.simple_list_item_1, sPhotoType
                    ));
            mPhotoDetailInput = itemView.findViewById(R.id.photo_detail_input);
        }

        void bind(PhotoUpload photoUpload, int positionParent){
//            TODO: 利用Picasso的缓存？避免浪费？
            Picasso.get().load("file://" + photoUpload.getPath()).fit().into(mImageView);
            mPhotoDetailInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    photoUpload.setDescription(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mPhotoTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    photoUpload.setSubID(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    public List<PhotoUpload> retrieveData(){
        return sPhotoUpload;
    }
}
