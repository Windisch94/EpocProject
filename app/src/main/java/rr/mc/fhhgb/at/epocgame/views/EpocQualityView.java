package rr.mc.fhhgb.at.epocgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import com.emotiv.insight.IEmoStateDLL;

import rr.mc.fhhgb.at.epocgame.R;

/**
 * View for displaying the quality status of the epoc interface
 * @author Windischhofer, Rohner
 */
public class EpocQualityView extends View {

    private Bitmap headBitmap; // Bitmap for the head image
    int [] qualityValues = new int[18]; // the status of the 16 channels
    private Bitmap[] imageQualityList = new Bitmap[4]; // no signal, bad, poor, good
    public Runnable r = new Runnable() {
        @Override
        public void run() { // Runnable for getting the quality every second
            qualityValues = IEmoStateDLL.IS_GetContactQualityFromAllChannels();
            invalidate(); //redraw when the quality was read
        }
    };
    Handler h;

    /**
     * Constructor
     * @param context context from superview
     * @param metrics display metrics from the superview
     */
    public EpocQualityView(Context context, DisplayMetrics metrics) {
        super(context);
        h = new Handler();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        headBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head_epoc,options);
        this.headBitmap = EpocQualityView.getResizedBitmap(this.headBitmap,metrics.heightPixels-200,metrics.widthPixels);

        //different quality images and resize the bitmap
        imageQualityList[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.no_signal_epoc);
        imageQualityList[0] = EpocQualityView.getResizedBitmap(imageQualityList[0],70,70);
        imageQualityList[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.bad_signal_epoc);
        imageQualityList[1] = EpocQualityView.getResizedBitmap(imageQualityList[1],70,70);
        imageQualityList[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.poor_signal_epoc);
        imageQualityList[2] = EpocQualityView.getResizedBitmap(imageQualityList[2],70,70);
        imageQualityList[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.good_signal_epoc);
        imageQualityList[3] = EpocQualityView.getResizedBitmap(imageQualityList[3],70,70);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(headBitmap,0,25,null); // head view


        // the quality states
        draw(canvas,this.qualityValues[0],this.getWidth()/8,this.getHeight()/2); // CMS
        draw(canvas,this.qualityValues[1],this.getWidth()-this.getWidth()/8-imageQualityList[0].getWidth(),this.getHeight()/2); //DRL
        draw(canvas,this.qualityValues[2],this.getWidth()/4,this.getHeight()/8); //AF3
        draw(canvas,this.qualityValues[4],this.getWidth()/15,this.getHeight()/4); //F7
        draw(canvas,this.qualityValues[5],this.getWidth()/3,this.getHeight()/4); //F3
        draw(canvas,this.qualityValues[6],this.getWidth()/5,this.getHeight()/3); //FC5
        draw(canvas,this.qualityValues[7],0,this.getHeight()/2.5f); //T7
        draw(canvas,this.qualityValues[8],this.getWidth()/5.5f,this.getHeight()/1.5f); //P7
        draw(canvas,this.qualityValues[9],this.getWidth()/3,this.getHeight()/1.2f); //O1
        draw(canvas,this.qualityValues[10],this.getWidth()-this.getWidth()/3-imageQualityList[0].getWidth(),this.getHeight()/1.2f); //O2
        draw(canvas,this.qualityValues[11],this.getWidth()-this.getWidth()/5.5f-imageQualityList[0].getWidth(),this.getHeight()/1.5f); //P8
        draw(canvas,this.qualityValues[12],this.getWidth()-imageQualityList[0].getWidth(),this.getHeight()/2.5f); //T8
        draw(canvas,this.qualityValues[13],this.getWidth()-this.getWidth()/5-imageQualityList[0].getWidth(),this.getHeight()/3); //FC6
        draw(canvas,this.qualityValues[14],this.getWidth()-this.getWidth()/3-imageQualityList[0].getWidth(),this.getHeight()/4); //F4
        draw(canvas,this.qualityValues[15],this.getWidth()-this.getWidth()/15-imageQualityList[0].getWidth(),this.getHeight()/4); //F8
        draw(canvas,this.qualityValues[17],this.getWidth()-this.getWidth()/4-imageQualityList[0].getWidth(),this.getHeight()/8); //AF4



        h.postDelayed(r,1000); // redraw every second

    }

    /**
     * draws the quality status on the canvas
     * @param canvas the canvas
     * @param value the value of the quality (0..bad, 4.. good)
     * @param x x position for the drawing
     * @param y y position for the drawing
     */
    public void draw(Canvas canvas, int value, float x, float y) {
        switch (value) {
            case 0: canvas.drawBitmap(this.imageQualityList[0],x,y,null); break;
            case 1: canvas.drawBitmap(this.imageQualityList[1],x,y,null); break;
            case 2: canvas.drawBitmap(this.imageQualityList[2],x,y,null); break;
            case 3: canvas.drawBitmap(this.imageQualityList[3],x,y,null); break;
            case 4: canvas.drawBitmap(this.imageQualityList[3],x,y,null); break;

            default: break;
        }
    }

    /**
     * resizes a given bitmap to a new height and width
     * @param bm the old bitmap
     * @param newHeight the new height of the bitmap
     * @param newWidth the new width of the bitmap
     * @return the resized bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }


}
