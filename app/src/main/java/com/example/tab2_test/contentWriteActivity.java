package com.example.tab2_test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class contentWriteActivity extends AppCompatActivity {
    Mypainter mypainter;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_contents);
        setTitle("Graphics");

        checkpermission();
        init();
    }

    public void init()
    {
        mypainter = (Mypainter)findViewById(R.id.painter);


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
                mypainter.Open(getExternalPath() + "hwimg/img.jpg");
                break;
                
            case R.id.btnsave:
                mypainter.Save(getExternalPath() + "hwimg/img.jpg");
                break;
        }

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