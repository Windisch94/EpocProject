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

    public void speedUp(int progress) {

        if (progress == 100) {
            speed = -25;
        }else {
            speed = -5 - ((progress/10)%10)*2;
        }




    }

}
