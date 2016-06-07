package rr.mc.fhhgb.at.epocgame;

/**
 * Created by Windisch on 30.05.2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

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
    Bitmap smallBall;
    private Context mContext;
    private int xVelocity = 5;
    private Handler h;

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
    }

    public void onDraw(Canvas c) {
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        //smallBall = Bitmap.createScaledBitmap(ball, 100, 100, false);
        if (x < 0 && y < 0) {
            x = 0;
            y = this.getHeight() - this.getHeight() / 5;
        } else {
            if (shouldDraw) {
                x += xVelocity;
                if ((x > this.getWidth() - ball.getWidth()) || (x < 0)) {
                    x = 0;
                }
            }
        }

        //funktion unten
        //ball = EpocQualityView.getResizedBitmap(ball,100,100);
        c.drawBitmap(ball, 0, 0, null);
        h.postDelayed(r, FRAME_RATE);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void reset() {
        this.shouldDraw = false;

    }

    public void stopAnimation(Canvas c) {

    }

}

    /*
    public boolean shouldDraw = false;
    private int screenW;
    private int screenH;

    private float drawScaleW;
    private float drawScaleH;

    int moveRate = 10, ballY, ballX;

    private Context myContext;

    private Bitmap ball;
    private boolean dotGoing = true, gameOver = false;


    public AnimatedView(Context context) {
        super(context);
        myContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ball = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.ball);
        screenW = w;
        screenH = h;
        drawScaleW = (float) screenW / 800;
        drawScaleH = (float) screenH / 600;
        ballY = ball.getHeight();
        ballX = w/2-(ball.getWidth()/2);
    }

    public void run() {
        if (!gameOver) {
            animateDot();
            invalidate();
        }
    }
    public void reset() {
        this.shouldDraw= false;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        canvas.drawBitmap(ball, ballX, ballY, null);
        run();
    }
    private void animateDot(){
        if (dotGoing) {
            if(ballY <screenH- ball.getHeight())
                ballY += moveRate;
        }
    }
}
*/