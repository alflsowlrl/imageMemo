package com.example.tab2_test;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by guswn_000 on 2017-05-18.
 */

public class Mypainter extends View {
    int oldX, oldY = -1;
    Bitmap mbitmap;
    Canvas mcanvas;
    Paint mpaint = new Paint();

    Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.emoji00);


    boolean check;


    public Mypainter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(3);
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);


    }

    public Mypainter(Context context) {
        super(context);
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(3);
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setStampImg(Bitmap img){
        this.img = img;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        mbitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas();
        //빗맵과 캔버스 연결
        mcanvas.setBitmap(mbitmap);
        mcanvas.drawColor(Color.WHITE);

//        drawStamp();

    }

    private void drawStamp(int X, int Y)
    {
        Bitmap bigimg = Bitmap.createScaledBitmap(img,img.getWidth()/2,img.getHeight()/2,false);
        mcanvas.drawBitmap(bigimg,X,Y,mpaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mbitmap != null)
            canvas.drawBitmap(mbitmap,0,0,null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldX = X;
            oldY = Y;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (oldX != -1) {
                if (!check) //체크안됐음
                {
                    mcanvas.drawLine(oldX, oldY, X, Y, mpaint);
                }
                invalidate();
                oldX = X;
                oldY = Y;
            }
        }

        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if(oldX != -1)
            {
                if(check)
                {
                    drawStamp(X,Y);
                }
                else
                {
                    mcanvas.drawLine(oldX,oldY,X,Y,mpaint);
                }
            }
            invalidate();
            oldX = -1; oldY = -1;
        }

        return true;
    }

    public void checkboxcheck(boolean checked)
    {
        check = checked;
    }

    //menu
    public void blur(boolean onoff) {
        if (onoff == true) {
            BlurMaskFilter blur = new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER);
            mpaint.setMaskFilter(blur);
        } else {
            mpaint.setMaskFilter(null);
        }
    }


    public void penwidth(boolean onoff) {
        if (onoff == true) {
            mpaint.setStrokeWidth(5);
        } else {
            mpaint.setStrokeWidth(3);
        }
    }

    public void penRed() {
        mpaint.setColor(Color.RED);
    }

    public void penBlue() {
        mpaint.setColor(Color.BLUE);
    }

    public void penBlack() {
        mpaint.setColor(Color.BLACK);
    }

    //button
    public void Eraser() {
        mbitmap.eraseColor(Color.WHITE);
        invalidate();
    }


    //갤러리에서 이미지 불러오기로 바꾸기
    public void Open(String filename) {
        try {
            FileInputStream in = new FileInputStream(filename);
            mcanvas.scale(0.5f, 0.5f);
            mcanvas.drawBitmap(BitmapFactory.decodeStream(in), 0, 0, mpaint);
            mcanvas.scale(2.0f, 2.0f);
            in.close();
            invalidate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "저장된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
        }
    }

    //DB에 이미지 저장하기로 바꾸기
    public void Save(String filename) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
        }
    }

    //서버에 이미지 업로드
    public void Upload(String filename, String server_url){

        File file = convertBitmapToFile(filename, mbitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("productImg",filename,requestBody);
        Gson gson = new GsonBuilder().setLenient().create();
        UploadService uploadService = new Retrofit.Builder().
                baseUrl(server_url).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build().
                create(UploadService.class);

        String id = PreferenceManager.getString(getContext(), "user_id");
        final String group_name = PreferenceManager.getString(getContext(), "group_name");

        uploadService.postImage(id, group_name, imageBody).enqueue(new Callback<FileNameBody>() {
            @Override
            public void onResponse(Call<FileNameBody> call, Response<FileNameBody> response) {
                Log.d("upload", response.body().file_name);

                final String  file_name = response.body().file_name;

                class AddFileName extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        HttpRequestHelper helper = new HttpRequestHelper();
                        helper.ADD_FILE_NAME(group_name, file_name);

                        return null;
                    }
                }

                AddFileName addFileName = new AddFileName();
                addFileName.execute();


            }

            @Override
            public void onFailure(Call<FileNameBody> call, Throwable t) {
                Log.d("upload", "error");
            }
        });
    }

    private File convertBitmapToFile(String fileName, Bitmap bitmap){
        //create a file to write bitmap data
        File file = new File(getContext().getCacheDir(), fileName);

        try{
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitMapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitMapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}