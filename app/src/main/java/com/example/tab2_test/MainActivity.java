package com.example.tab2_test;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView  AddGroup;
    String GroupName;
    private HttpRequestHelper http_helper = new HttpRequestHelper();
    private ArrayList<String> group_name_list = new ArrayList<>();
    private ListView listview;
    private Context mContext;
    private GroupNameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView)findViewById(R.id.listview);
        AddGroup = (TextView)findViewById(R.id.AddGroup);

        mContext = this;

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        adapter = new GroupNameListAdapter(mContext,
                android.R.layout.simple_list_item_1, group_name_list);

        //리스트뷰의 어댑터를 지정해준다.
        listview.setAdapter(adapter);

        //그룹 목록을 서버에서 불러온다
        loadGroups();

        //user id를 저장한다.
        Intent intent = getIntent();
        Long user_id = intent.getLongExtra("id", -1L);
        PreferenceManager.setLong(mContext, "id", user_id);




        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                GroupName = adapter.getItem(position);

                //그룹 이름을 저장
                PreferenceManager.setString(mContext, "group_name", GroupName);

                //아이템 클릭시 페이지 이동
                Intent intent = new Intent(mContext, TabActivity.class);

                view.getContext().startActivity(intent);

            }
        });

        //그룹 추가하기 TEXT 클릭시
        AddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //동시에 여러개 누르면 안됨
                Intent intent = new Intent(view.getContext(), PhonePopup.class);

                view.getContext().startActivity(intent);
            }
        });


        //리스트뷰에 보여질 아이템을 추가

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadGroups();
    }

    private void loadGroups(){
        class LoadFileNames extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                String result = http_helper.GETAllGroupsNames();
                final ArrayList<String> group_name_list = new ArrayList<String>();

                try {

                    JSONArray array = new JSONArray(result);

                    Log.d("group", result);
                    for (int index = 0; index < array.length(); ++index) {
                        JSONObject offerObject = array.getJSONObject(index);
                        String gruop_name = offerObject.getString("group_name");

                        group_name_list.add(gruop_name);

                    }

                    //ui를 변경하는 것이므로 ui thread에서 내용 변경
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            adapter.setItems(group_name_list);
                            adapter.notifyDataSetChanged();
                        }
                    });


                } catch (JSONException e) {
                    Log.d("myApp", e.toString());
                } catch (Exception e) {
                    Log.d("myApp", e.toString());
                }
                return null;
            }
        }

        LoadFileNames loadFileNames = new LoadFileNames();
        loadFileNames.execute();
    }
}