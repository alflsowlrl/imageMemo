package com.example.tab2_test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GroupPageTab extends FragmentTab implements LoaderManager.LoaderCallbacks<Cursor>, MediaStoreAdapter.OnClickThumbListener{
    ImageView groupImage;
    TextView groupText;
    Button content_button;

    private final static int READ_EXTERNAL_STORAGE_PERMMISSION_RESULT = 0;
    private final static int MEDIASTORE_LOADER_ID = 0;

    private MediaStoreAdapter mMediaStoreAdapter;
    private RecyclerView mThumbnailRecyclerView;
    private LinearLayout mLinearLayout;

    private HttpRequestHelper http_helper = new HttpRequestHelper();

    //*들어가는 내용(이미지, 텍스트)이 그룹마다 달라야함*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.group_listview, container, false);

        mThumbnailRecyclerView = (RecyclerView) mLinearLayout.findViewById(R.id.thumbnailRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 1);
        mThumbnailRecyclerView.setLayoutManager(gridLayoutManager);

        mMediaStoreAdapter = new MediaStoreAdapter(this.getActivity(), this);
        mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);

        content_button = mLinearLayout.findViewById(R.id.content_button);

        //리스트뷰의 어댑터를 지정해준다.
        mThumbnailRecyclerView.setAdapter(mMediaStoreAdapter);

        //버튼 클릭시 게시글 추가 페이지로 이동
        content_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), contentWriteActivity.class);

                view.getContext().startActivity(intent);

                //그룹 이름 저장

            }
        });

        loadDBFiles();

        checkReadExternalStoragePermission();

        return mLinearLayout;
    }
    private void loadDBFiles(){
        final ArrayList<String> result = new ArrayList<String>();

        final String group_name = PreferenceManager.getString(getContext(),"group_name");
        final ArrayList<String> fileNameList = new ArrayList<String>();

        class LoadFileNames extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                String result = http_helper.GETAllFileNames(group_name);



                try {

                    JSONArray array = new JSONArray(result);
                    for (int index = 0; index < array.length(); ++index) {
                        JSONObject offerObject = array.getJSONObject(index);
                        String fileName = offerObject.getString("file_name");

                        Log.d("gallery", fileName);



                        fileNameList.add(fileName);

                    }

                    Collections.reverse(fileNameList);


                } catch (JSONException e) {
                    Log.d("myApp", e.toString());
                } catch (Exception e) {
                    Log.d("myApp", e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mMediaStoreAdapter.file_name_list = fileNameList;
                mMediaStoreAdapter.notifyDataSetChanged();
            }
        }

        LoadFileNames loadFileNames = new LoadFileNames();
        loadFileNames.execute();



    }

    @Override
    public void onResume() {
        super.onResume();
        loadDBFiles();
    }

    //갤러리 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case READ_EXTERNAL_STORAGE_PERMMISSION_RESULT:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.getActivity().getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null, this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkReadExternalStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Start cursor loader
                this.getActivity().getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null,  this);
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this.getContext(), "App needs to view thumbnails", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_PERMMISSION_RESULT);
            }
        } else {
            // Start cursor loader
            this.getActivity().getSupportLoaderManager().initLoader(MEDIASTORE_LOADER_ID, null,  this);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MEDIA_TYPE
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        return new CursorLoader(
                this.getContext(),
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMediaStoreAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMediaStoreAdapter.changeCursor(null);
    }

    @Override
    public void OnClickImage(Uri imageUri) {
        Intent fullScreenIntent = new Intent(this.getContext(), FullScreenImageActivity.class);
        fullScreenIntent.setData(imageUri);
        startActivity(fullScreenIntent);
    }

    @Override
    public void OnClickVideo(Uri videoUri) {
//        Intent videoPlayIntent = new Intent(this.getContext(), VideoPlayActivity.class);
//        videoPlayIntent.setData(videoUri);
//        startActivity(videoPlayIntent);

    }
}