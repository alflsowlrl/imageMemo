package com.example.tab2_test;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
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

        checkPermission();

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
        PreferenceManager.setString(mContext, "user_id", "" + user_id);
        Log.d("myApp", "this is user id: " + PreferenceManager.getString(mContext, "user_id"));




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

    void checkPermission() {
        ArrayList<String> premissionInRequest = new ArrayList<String>();
        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.WRITE_CONTACTS);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                premissionInRequest.add(permission);
            }
        }

        if(!premissionInRequest.isEmpty()){
            requestPermission(premissionInRequest);
        }

    }

    void requestPermission(ArrayList<String> permissions) {
        // 2. 권한 요청
        String[] premissionInRequest = new String[permissions.size()];
        int j = 0;
        for(int i = 0; i < permissions.size(); i++){
            String permission = permissions.get(i);
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                premissionInRequest[j++] = permission;
            }
        }

        ActivityCompat.requestPermissions( this, (String[]) premissionInRequest, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200){
            ArrayList<Integer> results = new ArrayList<Integer>();
            for(int i = 0; i < grantResults.length; i++){
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
            }
        }

    }
}