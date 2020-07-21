package com.example.tab2_test;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PhonePopup extends Activity {
    EditText EditGroup;
//    Contact contact = null;

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group_popup);

        //UI 객체생성
        EditGroup = (EditText) findViewById(R.id.txtText);

        Button phoneMod = findViewById(R.id.phoneCancel);
        Button phoneAdd = findViewById(R.id.phoneAdd);

        phoneMod.setOnClickListener(new View.OnClickListener() {
//            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class GroupAdd extends AsyncTask<Void, Void, Void>{
                    @Override
                    protected Void doInBackground(Void... voids) {
                        HttpRequestHelper helper = new HttpRequestHelper();
                        helper.REGISTER_GROUP(EditGroup.getText().toString());

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        finish();
                    }
                }

                GroupAdd groupAdd = new GroupAdd();
                groupAdd.execute();

                Toast.makeText(getApplicationContext(), "그룹: " + EditGroup.getText().toString() + "이 추가되었습니다.", Toast.LENGTH_SHORT).show();


            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP: {
                LinearLayout view = findViewById(R.id.phoneAddPopup);

                Rect rect = new Rect();
                view.getLocalVisibleRect(rect);

                if(!(rect.left < event.getX() && event.getX() < rect.right && rect.top < event.getY() && event.getY() < rect.bottom)){
                    finish();
                }
            }
        }
        return true;
    }


}
