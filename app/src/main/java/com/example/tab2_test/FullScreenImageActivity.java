package com.example.tab2_test;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    final int[] likesArr = {0};
    private final String server_url = "http://192.249.19.244:1880";
    private String file_name;
    private String group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        ImageButton imageButton = (ImageButton) findViewById(R.id.likeButton);
        final TextView imageTextView = (TextView) findViewById(R.id.likeTextView);

        int likes = 0;
        group_name = PreferenceManager.getString(getApplicationContext(), "group_name");

        //이미지 Uri 받기
        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            file_name = callingActivityIntent.getStringExtra( "file_name");
            likes = Integer.parseInt(callingActivityIntent.getStringExtra("likes"));
            String file_url = server_url + "/download/" + group_name + "+" + file_name;

            if(file_name != null && fullScreenImageView != null) {
                Glide.with(this).load(file_url).into(fullScreenImageView);
            }
        }

        likesArr[0] = likes;
        imageTextView.setText("" + likes);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesArr[0]++;
                imageTextView.setText("" + likesArr[0]);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("likes", ""+likesArr[0]);

        class UpdateLikes extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                HttpRequestHelper helper = new HttpRequestHelper();
                helper.UPDATE_IMAGE_LIKES(file_name, group_name, ""+ likesArr[0]);

                return null;
            }
        }

        if(file_name != null && group_name != null){
            UpdateLikes updateLikes = new UpdateLikes();
            updateLikes.execute();
        }
    }

    //원래 공유버튼이 있었는데,, 없어져서 일단 주석처리함
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.full_image_share, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.image_share_menu);
//        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        shareActionProvider.setShareIntent(createShareIntent());
//        return true;
//    }

//    //이미지 공유
//    private Intent createShareIntent() {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");
//        shareIntent.putExtra(Intent.EXTRA_STREAM, mImageUri);
//        return shareIntent;
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        Intent shareIntent = createShareIntent();
//        startActivity(Intent.createChooser(shareIntent, "send to"));
//        return true;
//    }
}
