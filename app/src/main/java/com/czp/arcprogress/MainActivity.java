package com.czp.arcprogress;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.czp.library.ArcProgress;
import com.czp.library.OnTextCenter;
import com.czp.library.onImageCenter;

public class MainActivity extends AppCompatActivity {
    ArcProgress mProgress,mProgress1,mProgress02,mProgress03;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (ArcProgress) findViewById(R.id.myProgress);
        mProgress1 = (ArcProgress) findViewById(R.id.myProgress01);
        mProgress02 = (ArcProgress) findViewById(R.id.myProgress02);
        mProgress03 = (ArcProgress) findViewById(R.id.myProgress03);

        mProgress.setOnCenterDraw(new OnTextCenter());
        mProgress1.setOnCenterDraw(new OnTextCenter());
        mProgress02.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPaint.setStrokeWidth(35);
                textPaint.setColor(getResources().getColor(R.color.textColor));
                String progressStr = String.valueOf(progress+"%");
                float textX = x-(textPaint.measureText(progressStr)/2);
                float textY = y-((textPaint.descent()+textPaint.ascent())/2);
                canvas.drawText(progressStr,textX,textY,textPaint);
            }
        });
        mProgress03.setOnCenterDraw(new onImageCenter(this,R.mipmap.git));
        addProrgress(mProgress);
        addProrgress(mProgress1);
        addProrgress(mProgress02);
        addProrgress(mProgress03);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArcProgress progressBar = (ArcProgress) msg.obj;
            progressBar.setProgress(msg.what);
        }
    };

    public void addProrgress(ArcProgress progressBar) {
        Thread thread = new Thread(new ProgressThread(progressBar));
        thread.start();
    }

    class ProgressThread implements Runnable{
        int i= 0;
        private ArcProgress progressBar;
        public ProgressThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }
        @Override
        public void run() {
            for(;i<=100;i++){
                if(isFinishing()){
                    break;
                }
                Message msg = new Message();
                msg.what = i;
                Log.e("DEMO","i == "+i);
                msg.obj = progressBar;
                SystemClock.sleep(100);
                handler.sendMessage(msg);
            }
        }
    }
}
