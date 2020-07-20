package com.example.tab2_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class groupPage extends Activity {
    ImageView groupImage;
    TextView groupText;
    Button content_button;

    //*들어가는 내용(이미지, 텍스트)이 그룹마다 달라야함*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_listview);

        content_button = findViewById(R.id.content_button);

        final ListView group_list = (ListView)findViewById(R.id.group_list);

        //데이터를 저장하게 되는 리스트
        List<String> list = new ArrayList<>();

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.group_category, list);

        //리스트뷰의 어댑터를 지정해준다.
        group_list.setAdapter(adapter);

        //버튼 클릭시 게시글 추가 페이지로 이동
        content_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), contentWriteActivity.class);

                view.getContext().startActivity(intent);
            }
        });

    }


}