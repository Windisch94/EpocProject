package rr.mc.fhhgb.at.epocgame.views;

/**
 * Created by Windisch on 30.05.2016.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import rr.mc.fhhgb.at.epocgame.R;

public class AnimatedView extends ImageView {
    private final int FRAME_RATE = 30;
    public boolean shouldDraw = false;
    public Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
    int x = -1;
    int y = -1;
    private Context mContext;
    private int xVelocity = 5;
    private Handler h;

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
    }

    public void onDraw(Canvas c) {
        BitmapDrawable ball = (BitmapDrawable) mContext.getResources()
                .getDrawable(R.drawable.ball_practise);

        if (x < 0 && y < 0) {
            x = 0;
            y = this.getHeight() - this.getHeight() / 3;
        } else {
            if (shouldDraw) {
                x += xVelocity;
                if ((x > this.getWidth() - ball.getBitmap().getWidth()) || (x < 0)) {
                    x = 0;
                }
            }
        }

        c.drawBitmap(ball.getBitmap(), x, y, null);
        h.postDelayed(r, FRAME_RATE);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void reset() {
        this.x = 0;
        this.shouldDraw= false;

    }


}