package com.example.tab2_test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;

import static android.graphics.BitmapFactory.decodeResource;

public class contentWriteActivity extends AppCompatActivity {
    Mypainter mypainter;
    CheckBox checkBox;

    ImageView emoji00, emoji01, emoji02, emoji03, emoji04, emoji05, emoji06, emoji07, emoji08, emoji09, emoji10, emoji11, emoji12, emoji13;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 0;
    private static final int CROP_FROM_ALBUM = 1;

    private final int IMAGE_RESULT = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_contents);
        setTitle("그림일기\uD83C\uDFA8");

        checkpermission();
        init();



    }

    public void init()
    {
        mypainter = (Mypainter)findViewById(R.id.painter);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mypainter.checkboxcheck(isChecked);
            }
        });

        emoji00 = findViewById(R.id.emoji00);
        emoji01 = findViewById(R.id.emoji01);
        emoji02 = findViewById(R.id.emoji02);
        emoji03 = findViewById(R.id.emoji03);
        emoji04 = findViewById(R.id.emoji04);
        emoji05 = findViewById(R.id.emoji05);
        emoji06 = findViewById(R.id.emoji06);
        emoji07 = findViewById(R.id.emoji07);
        emoji08 = findViewById(R.id.emoji08);
        emoji09 = findViewById(R.id.emoji09);
        emoji10 = findViewById(R.id.emoji10);
        emoji11 = findViewById(R.id.emoji11);
        emoji12 = findViewById(R.id.emoji12);
        emoji13 = findViewById(R.id.emoji13);

        emoji00.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji00));
            }
        });
        emoji01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji01));
            }
        });
        emoji02.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji02));
            }
        });
        emoji03.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji03));
            }
        });
        emoji04.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji04));
            }
        });
        emoji05.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji05));
            }
        });
        emoji06.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji06));
            }
        });
        emoji07.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji07));
            }
        });
        emoji08.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji08));
            }
        });
        emoji09.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji09));
            }
        });
        emoji10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji10));
            }
        });
        emoji11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji11));
            }
        });
        emoji12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji12));
            }
        });
        emoji13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mypainter.setStampImg(decodeResource(getResources(), R.drawable.emoji13));
            }
        });

        //디렉토리 생성
        String path = getExternalPath();
        File file = new File(path + "hwimg");
        file.mkdir();

//        String mkdirerrormsg = "디렉토리 생성";
//        if(file.isDirectory() == false)
//        {
//            mkdirerrormsg = "디렉토리 생성 오류";
//        }
//        Toast.makeText(this,mkdirerrormsg,Toast.LENGTH_SHORT).show();

    }




    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menublur://블러 체크메뉴
                if(item.isChecked())
                {
                    item.setChecked(false);
                    mypainter.blur(false);
                }
                else
                {
                    item.setChecked(true);
                    mypainter.blur(true);
                }
                break;

            case R.id.menupenwb: //펜 굵기 체크메뉴
                if (item.isChecked())
                {
                    item.setChecked(false);
                    mypainter.penwidth(false);
                }
                else
                {
                    item.setChecked(true);
                    mypainter.penwidth(true);
                }
                break;

            case R.id.menured:
                mypainter.penRed();
                break;

            case R.id.menublue:
                mypainter.penBlue();
                break;

            case R.id.menublack:
                mypainter.penBlack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //버튼
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnerase:
                mypainter.Eraser();
                break;

            case R.id.btnopen:
                mypainter.Eraser();
                Intent intent  = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);


                startActivityForResult(intent, IMAGE_RESULT);
//                mypainter.Open(getExternalPath() + "hwimg/img.jpg");
                break;

            case R.id.btnsave:
                mypainter.Save(getExternalPath() + "hwimg/img.jpg");
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = getImageFilePath(data);

        mypainter.Open(filePath);
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor =getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public void checkpermission()
    {

        int permissioninfo = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this,
//                    "SDCard 쓰기 권한 있음",Toast.LENGTH_SHORT).show();
        }
        else {

            //사실 이프문 안써도되는데
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,
                        "어플리케이션 설정에서 저장소 사용 권한을 허용해주세요",Toast.LENGTH_SHORT).show();

                //이밑에꺼 해야 권한허용 대화상자가 다시뜸
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);  // 이 100은 리퀘스트코드다
            }
        }
    }


    public String getExternalPath()
    {
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED))
        {
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            //sdPath = "mnt/sdcard/";
        }
        else
        {
            sdPath = getFilesDir() + "";
        }
//        Toast.makeText(getApplicationContext(),sdPath,Toast.LENGTH_SHORT).show();
        return sdPath;
    }



}