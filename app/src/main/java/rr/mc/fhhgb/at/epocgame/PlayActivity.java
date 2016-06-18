package rr.mc.fhhgb.at.epocgame;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emotiv.insight.IEmoStateDLL;

import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity implements EngineInterface {

    Button buttonMoveBgd;
    Button buttonMoveBall;
    Button buttonStart;
    ValueAnimator animator;
    long duration = 10000L;
    int height;
    int width;
    ImageView backgroundOne;
    ImageView backgroundTwo;
    TextView textViewCountdown;
    TextView textViewDistance;
    boolean fast = false;
    long distance;
    long startTime;
    long elapsedTime;

    //nächster versuch
    ImageView imgBox;
    float power;
    float _currentPower = 5;
    boolean isTrainning = false;
    int _currentAction = IEmoStateDLL.IEE_MentalCommandAction_t.MC_RIGHT.ToInt();
    float startLeft = -1;
    float startRight = 0;
    float widthScreen = 0;
    EngineConnector engineConnector;
    int count = 0;
    Timer timer;
    TimerTask timerTask;
    Handler handlerUpdateUI = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    /*count ++;
                    int trainningTime=(int) MentalCommandDetection.IEE_MentalCommandGetTrainingTime(userId)[1]/1000;
                    if(trainningTime > 0)
                        progressBarTime.setProgress(count / trainningTime);
                    if (progressBarTime.getProgress() >= 100) {
                        timerTask.cancel();
                        timer.cancel();
                    }*/
                    break;
                case 1:
                    moveImage();
                    break;
                default:
                    //moveImage sonst nicht hier
                    moveImage();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        textViewCountdown = (TextView) findViewById(R.id.textViewCountdown);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);

        buttonMoveBgd = (Button) findViewById(R.id.buttonMoveBgd);
        buttonMoveBall = (Button) findViewById(R.id.buttonMoveBall);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        //ab hier
        engineConnector = EngineConnector.shareInstance();
        EngineConnector.delegate = this;
        init();
        imgBox = (ImageView) findViewById(R.id.ballImage);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start timer and game
                animator.start();
                startTime = System.currentTimeMillis();

                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {


                        textViewCountdown.setText("Remaining: " + millisUntilFinished / 1000 + "sec");
                        distance = (System.currentTimeMillis() - startTime);
                        //plus move ball factor
                        textViewDistance.setText("Distance: " + (distance / 1000) + "m");

                    }

                    public void onFinish() {
                        textViewCountdown.setText("end!");
                        animator.end();

                    }
                }.start();

            }
        });

        buttonMoveBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move Ball
                moveImage();
            }
        });


        buttonMoveBgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //background animation wird schneller

                if (fast == false) {
                    animator.setDuration(duration / 3);
                    fast = true;
                    buttonMoveBgd.setText("slower");

                } else {
                    animator.setDuration(duration * 3);
                    fast = false;
                    buttonMoveBgd.setText("faster");
                }

            }
        });
        backgroundOne = (ImageView) findViewById(R.id.background_one);
        backgroundTwo = (ImageView) findViewById(R.id.background_two);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        Bitmap bm = ((BitmapDrawable) backgroundOne.getDrawable()).getBitmap();
        bm = EpocQualityView.getResizedBitmap(bm, dm.heightPixels + 200, dm.widthPixels + 200);
        Bitmap bm2 = ((BitmapDrawable) backgroundTwo.getDrawable()).getBitmap();
        bm2 = EpocQualityView.getResizedBitmap(bm2, dm.heightPixels + 200, dm.widthPixels + 200);


        backgroundOne.setImageBitmap(bm);
        backgroundTwo.setImageBitmap(bm2);

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress * -1;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);

            }
        });

    }

    private void init() {
        startTrainingMentalcommand(IEmoStateDLL.IEE_MentalCommandAction_t.MC_RIGHT);
        Timer timerListenAction = new Timer();
        timerListenAction.scheduleAtFixedRate(new TimerTask() {
                                                  @Override
                                                  public void run() {
                                                      handlerUpdateUI.sendEmptyMessage(1);
                                                  }
                                              },
                0, 20);

    }

    public void startTrainingMentalcommand(IEmoStateDLL.IEE_MentalCommandAction_t MentalCommandAction) {
        isTrainning = engineConnector.startTrainingMetalcommand(isTrainning, MentalCommandAction);
        //btnTrain.setText((isTrainning) ? "Abort Trainning" : "Train");
    }

    private void moveImage() {
        power = _currentPower;
        if (isTrainning) {
            imgBox.setLeft((int) (startLeft));
            imgBox.setRight((int) startRight);
            imgBox.setScaleX(1.0f);
            imgBox.setScaleY(1.0f);
        }
        if ((_currentAction == IEmoStateDLL.IEE_MentalCommandAction_t.MC_LEFT.ToInt()) || (_currentAction == IEmoStateDLL.IEE_MentalCommandAction_t.MC_RIGHT.ToInt()) && power > 0) {

            if (imgBox.getScaleX() == 1.0f && startLeft > 0) {
                imgBox.setRight((int) widthScreen);
                power = (_currentAction == IEmoStateDLL.IEE_MentalCommandAction_t.MC_LEFT.ToInt()) ? power * 3 : power * -3;
                imgBox.setLeft((int) (power > 0 ? Math.max(0, (int) (imgBox.getLeft() - power)) : Math.min(widthScreen - imgBox.getMeasuredWidth(), (int) (imgBox.getLeft() - power))));
            }
        } else if (imgBox.getLeft() != startLeft && startLeft > 0) {
            power = (imgBox.getLeft() > startLeft) ? 6 : -6;
            imgBox.setLeft(power > 0 ? Math.max((int) startLeft, (int) (imgBox.getLeft() - power)) : Math.min((int) startLeft, (int) (imgBox.getLeft() - power)));
        }
        if (imgBox.getScaleX() != 1.0f) {
            power = (imgBox.getScaleX() < 1.0f) ? 0.03f : -0.03f;
            imgBox.setScaleX(power > 0 ? Math.min(1, (imgBox.getScaleX() + power)) : Math.max(1, (imgBox.getScaleX() + power)));
            imgBox.setScaleY(power > 0 ? Math.min(1, (imgBox.getScaleY() + power)) : Math.max(1, (imgBox.getScaleY() + power)));
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthScreen = size.x;
        startLeft = imgBox.getLeft();
        startRight = imgBox.getRight();
    }


    public void TimerTask() {
        count = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handlerUpdateUI.sendEmptyMessage(0);
            }
        };
    }

    @Override
    public void trainStarted() {
        // TODO Auto-generated method stub
        //progressBarTime.setVisibility(View.VISIBLE);
        //btnClear.setClickable(false);
        // spinAction.setClickable(false);
        timer = new Timer();
        TimerTask();
        timer.schedule(timerTask, 0, 20);
    }

    @Override
    public void trainSucceed() {

    }

    @Override
    public void trainFailed() {

    }

    @Override
    public void trainCompleted() {

    }

    @Override
    public void trainRejected() {

    }

    @Override
    public void trainReset() {

    }

    @Override
    public void trainErased() {

    }

    @Override
    public void currentAction(int typeAction, float power) {

    }


}
