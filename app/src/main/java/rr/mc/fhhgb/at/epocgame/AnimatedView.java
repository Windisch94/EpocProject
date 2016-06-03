package rr.mc.fhhgb.at.epocgame;

/**
 * Created by Windisch on 30.05.2016.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AnimatedView extends ImageView {
    private Context mContext;
    int x = -1;
    int y = -1;
    private int xVelocity = 5;
    private Handler h;
    private final int FRAME_RATE = 30;
    public boolean shouldDraw = false;

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
    }

    public Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public void onDraw(Canvas c) {
        BitmapDrawable ball = (BitmapDrawable) mContext.getResources()
                .getDrawable(R.drawable.ball);
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