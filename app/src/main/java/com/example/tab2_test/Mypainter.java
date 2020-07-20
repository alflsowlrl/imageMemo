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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by guswn_000 on 2017-05-18.
 */

public class Mypainter extends View {
    int oldX, oldY = -1;
    Bitmap mbitmap;
    Canvas mcanvas;
    Paint mpaint = new Paint();
    Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    boolean check, scaled;

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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mbitmap != null)
            canvas.drawBitmap(mbitmap, 0, 0, null);

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
        return true;
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
            mcanvas.drawBitmap(BitmapFactory.decodeStream(in), mcanvas.getWidth() / 2, mcanvas.getHeight() / 2, mpaint);
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
}