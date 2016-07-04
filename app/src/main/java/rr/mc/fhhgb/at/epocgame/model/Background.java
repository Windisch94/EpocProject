package rr.mc.fhhgb.at.epocgame.model;


import android.graphics.Bitmap;

/**
 * model class for the background
 */
public class Background {

    public int speed = 0;
    public int distance = 0;

    private int x;
    private Bitmap bitmap;


    /**
     * constructor
     * @param bitmap the bitmap which represents the background
     */
    public Background(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * updates the x coordinate
     */
    public void update() {
        x += speed;
        if (x <= -bitmap.getWidth()) {
            x = 0;
        }
    }

    /**
     * calculates the Distance
     * @return the distance in m
     */
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

    /**
     * speeds up the backgrounds speed
     * @param progress progress in %
     */
    public void speedUp(int progress) {

        if (progress == 100) {
            speed = -95;
        }else {
            speed = -5 - ((progress/10)%10)*9;
        }




    }

}
