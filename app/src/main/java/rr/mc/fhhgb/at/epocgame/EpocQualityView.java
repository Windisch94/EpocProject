package rr.mc.fhhgb.at.epocgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.emotiv.insight.IEmoStateDLL;

/**
 * Created by Windisch on 06.06.2016.
 */
public class EpocQualityView extends View {

    private Bitmap headBitmap;
    int [] qualityValues = new int[16];
    private Bitmap[] imageQualityList = new Bitmap[4];
    public Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
            qualityValues = IEmoStateDLL.IS_GetContactQualityFromAllChannels(); //<-- DE METHODE!
            String s = "";
            for (int d: qualityValues
                    ) {
                s+= d+", ";
            }
            Log.d("bla",s);
        }
    };
    Handler h;

    public EpocQualityView(Context context, DisplayMetrics metrics) {
        super(context);
        h = new Handler();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        headBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.head_epoc,options);
        this.headBitmap = EpocQualityView.getResizedBitmap(this.headBitmap,metrics.heightPixels-300,metrics.widthPixels);
        imageQualityList[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.no_signal_epoc);
        imageQualityList[0] = EpocQualityView.getResizedBitmap(imageQualityList[0],70,70);
        imageQualityList[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.bad_signal_epoc);
        imageQualityList[1] = EpocQualityView.getResizedBitmap(imageQualityList[1],70,70);
        imageQualityList[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.poor_signal_epoc);
        imageQualityList[2] = EpocQualityView.getResizedBitmap(imageQualityList[2],70,70);
        imageQualityList[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.good_signal_epoc);
        imageQualityList[3] = EpocQualityView.getResizedBitmap(imageQualityList[3],70,70);


    }

    public void update() {
        this.invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = 10;
        int y = 35;
        canvas.drawBitmap(headBitmap,0,25,null);

        for (int i =0; i<16;i++) {
            if (this.qualityValues[i] >3) {
                this.qualityValues[i] = 3;
            }


            draw(canvas,this.qualityValues[i],x,y);
            x+=70;
            if(x==this.getWidth()) {
                x = 0;
                y+=70;
            }
        }

        h.postDelayed(r,1000);

    }

    public void draw(Canvas canvas, int value, int x, int y) {
        switch (value) {
            case 0: canvas.drawBitmap(this.imageQualityList[0],x,y,null); break;
            case 1: canvas.drawBitmap(this.imageQualityList[1],x,y,null); break;
            case 2: canvas.drawBitmap(this.imageQualityList[2],x,y,null); break;
            case 3: canvas.drawBitmap(this.imageQualityList[3],x,y,null); break;
            default: break;
        }
    }

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
