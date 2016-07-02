package rr.mc.fhhgb.at.epocgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.activities.PlayActivity;
import rr.mc.fhhgb.at.epocgame.model.Background;


public class BackgroundView extends SurfaceView implements Runnable {

    Background bg;
    private PlayActivity context;
    Thread renderThread;
    SurfaceHolder holder;
    volatile boolean running;
    TextView test;
    int i =0;


    public BackgroundView(Context context) {
        super(context);
        init(context);

    }
    public BackgroundView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.context = (PlayActivity)context;
        Bitmap background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back),metrics.widthPixels,metrics.heightPixels,false);
        this.bg = new Background(background);
        this.holder = getHolder();


    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void run() {
        long startTime = System.nanoTime();
        long startTime2 = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas(null);

                synchronized (bg) {
                    if (bg.getX()+bg.getBitmap().getWidth() >= 0 || (bg.getX() >= 0 && bg.getX() <= getWidth()))
                        canvas.drawBitmap(bg.getBitmap(), bg.getX(),0, null);
                    if (bg.getX()+bg.getBitmap().getWidth()+bg.getBitmap().getWidth() >= 0 || (bg.getX()+bg.getBitmap().getWidth() >= 0 && bg.getX() <= getWidth()))
                        canvas.drawBitmap(bg.getBitmap(), (bg.getX()+bg.getBitmap().getWidth()),0, null);
                    if(getDeltaTime(startTime) > 0.1) {
                        bg.update();


                        startTime = System.nanoTime();
                    }
                    if(getDeltaTime(startTime2) > 0.9) {
                        changeDistanceTV("Distanz: "+bg.calculateDistance()+"m");
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

        }
    }

    private float getDeltaTime(long startTime) {
        return (System.nanoTime() - startTime) / 100000000f;
    }
    public void changeDistanceTV(final String msg){
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //assign the value to the tvText from here
                context.distanceTV.setText(msg);
            }
        });
    }
    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public Background getBackgroundModel(){
        return bg;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}