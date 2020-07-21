package com.example.tab2_test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder> {

    private Cursor mMediaStoreCursor;
    private final Activity mActivity;
    private OnClickThumbListener mOnClickThumbListener;
    ArrayList<ImageFile> file_name_list = new ArrayList<ImageFile>();
    private final String server_url = "http://192.249.19.244:1880";



    public interface OnClickThumbListener {
        void OnClickImage(Uri imageUri);
        void OnClickVideo(Uri videoUri);
    }
    public MediaStoreAdapter(Activity activity, OnClickThumbListener onClickThumbListener) {
        this.mActivity = activity;
        this.mOnClickThumbListener = onClickThumbListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_image_view, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String group_name = PreferenceManager.getString(mActivity.getApplicationContext(),"group_name");

        String url = server_url + "/download/" + group_name + "+" + file_name_list.get(position).name;

        Glide.with(mActivity)
                .load(url)
                .thumbnail(
                        Glide.with(mActivity)
                        .load(R.drawable.thumbnail)
                        .override(960, 900)
                )
                .centerCrop()
                .override(960, 900)
                .into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        return file_name_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.mediastoreImageView);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent fullScreenIntent = new Intent(view.getContext(), FullScreenImageActivity.class);
                    try{
                        ImageFile imageFile = file_name_list.get(getAdapterPosition());
                        String file_name = imageFile.getName();
                        int likes = imageFile.getLikes();

                        fullScreenIntent.putExtra("file_name", file_name);
                        fullScreenIntent.putExtra("likes", "" + likes);

                        mActivity.startActivity(fullScreenIntent);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }

        public ImageView getImageView() {
            return mImageView;
        }

        @Override
        public void onClick(View v) {
            getOnClickUri(getAdapterPosition());
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private void getOnClickUri(int position) {

        String file_name = file_name_list.get(position).name;
        String group_name = PreferenceManager.getString(mActivity.getApplicationContext(), "group_name");
        String file_url = server_url + "/download/" + group_name + "+" + file_name;

        Uri mediaUri = Uri.parse(file_url);

        mOnClickThumbListener.OnClickImage(mediaUri);

//        switch (mMediaStoreCursor.getInt(mediaTypeIndex)) {
//            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
//                mOnClickThumbListener.OnClickImage(mediaUri);
//                break;
//            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
//                mOnClickThumbListener.OnClickVideo(mediaUri);
//                break;
//            default:
//        }
    }
}
