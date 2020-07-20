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
            @Override
            public void onClick(View view) {
//                if(contact != null){
//                    editPhone(contact);
//                }
                finish();
            }
        });

        phoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(contact != null){
//                    removePhone(contact);
//                }
                Toast.makeText(view.getContext(), "'"+ EditGroup.getText().toString() + "'" + "을 추가하였습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    //취소 버튼 클릭
//    public void mOnCancel(View v) {
//        //데이터 전달하기
//
//        //액티비티(팝업) 닫기
//        finish();
//    }
////
//    public void mOnDel(View v) {
//        if(contact != null){
//            removePhone(contact);
//        }
//
//        Toast.makeText(v.getContext(), contact.getName() + "님이 삭제 되었습니다", Toast.LENGTH_SHORT).show();
//        //액티비티(팝업) 닫기
//        finish();
//    }


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

//    private void removePhone(final Contact contact) {
//        class RemoveContact extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                HttpRequestHelper helper = new HttpRequestHelper();
//                helper.DELETE(contact, Long.valueOf(PreferenceManager.getString(getApplicationContext(),"user_id")));
//                return null;
//            }
//        }
//
//        RemoveContact removeContact = new RemoveContact();
//        removeContact.execute();
//    }
//
//    private void editPhone(Contact contact) {
//         String[] projection = {
//                 ContactsContract.Contacts._ID,
//                 ContactsContract.Contacts.DISPLAY_NAME,
//                 ContactsContract.Contacts.LOOKUP_KEY
//         };
//
//        String displayName;
//
//        // The lookup key from the Cursor
//        String currentLookupKey;
//        // The _ID value from the Cursor
//        Long currentId = Long.valueOf(0);
//        // A content URI pointing to the contact
//        Uri selectedContactUri = null;
//
//
//        Cursor cursor = getContentResolver().query(
//            ContactsContract.Contacts.CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        );
//
//        int displayNameColumn = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//        int lookupKeyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);
//
//        while (cursor.moveToNext()) {
//            displayName = cursor.getString(displayNameColumn);
//            currentLookupKey = cursor.getString(lookupKeyIndex);
//            if (displayName != contact.getName()) {
//                continue;
//            }
//            else{
//                selectedContactUri = ContactsContract.Contacts.getLookupUri(currentId, currentLookupKey);
//            }
//        }
//
//        Intent editIntent = new Intent(Intent.ACTION_EDIT);
//
//        editIntent.setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
//
//
//        editIntent.putExtra("finishActivityOnSaveCompleted", true);
//        this.startActivity(editIntent);
//    }
}
