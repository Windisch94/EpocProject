package rr.mc.fhhgb.at.epocgame.model;


import android.graphics.Bitmap;

public class Background {

    public int speed = 0;
    public int distance = 0;

    private int x;
    private Bitmap bitmap;



    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void update() {
        x += speed;



        if (x <= -bitmap.getWidth()) {
            x = 0;
        }
    }
    public int calculateDistance() {
        distance += speed*-1;
        return distance;
    }

    public void setSpeed(int speed) {
        this.speed = -speed;
    }

    public int getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void speedUp() {
        if(speed == -5) {
            speed = -15;
            Thread t1= new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i=0;i<5;i++) {
                        try {
                            Thread.sleep(1000);
                            speed +=2;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            t1.start();
        }


    }

}
