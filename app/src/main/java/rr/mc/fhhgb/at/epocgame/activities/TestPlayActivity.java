package rr.mc.fhhgb.at.epocgame.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.views.BackgroundView;

public class TestPlayActivity extends AppCompatActivity {

    private BackgroundView bgv;
    private ImageView ballImageView;
    private RotateAnimation r;
    public TextView distanceTV;
    public TextView timeTV;
    private Button startButton;
    private Button nudgeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);
        bgv = (BackgroundView)findViewById(R.id.backgroundView);
        distanceTV = (TextView) findViewById(R.id.distanceTV);
        timeTV = (TextView) findViewById(R.id.timeTV);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    bgv.getBackgroundModel().setSpeed(5);
                    ballImageView.startAnimation(r);
                    startButton.setAlpha(0);
                    startButton.setEnabled(false);
                new CountDownTimer(20000, 1000) {

                    public void onTick(long millisUntilFinished) {


                        timeTV.setText("Zeit: " + millisUntilFinished / 1000 + "sek.");


                    }

                    public void onFinish() {
                        timeTV.setText("end!");
                        bgv.getBackgroundModel().setSpeed(0);

                        ballImageView.clearAnimation();
                        startButton.setAlpha(1);
                        startButton.setEnabled(true);
                        AlertDialog.Builder doneDialog = new AlertDialog.Builder(TestPlayActivity.this);
                        doneDialog.setMessage("Gratulation! Du hast eine Weite von "+bgv.getBackgroundModel().distance+" Metern erreicht!");
                        doneDialog.setNeutralButton("Ok",null);
                        bgv.getBackgroundModel().distance = 0;
                        AlertDialog alertDialog = doneDialog.create();
                        alertDialog.show();



                    }
                }.start();

            }
        });
        nudgeButton = (Button)findViewById(R.id.nudgeButton);
        nudgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgv.getBackgroundModel().speedUp();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        r.setDuration(500);
                        try {
                            Thread.sleep(1000);
                            r.setDuration(700);
                            Thread.sleep(1000);
                            r.setDuration(900);
                            Thread.sleep(1000);
                            r.setDuration(1100);
                            Thread.sleep(1000);
                            r.setDuration(1300);
                            Thread.sleep(1000);
                            r.setDuration(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        initRotateAnimation();
        ballImageView = (ImageView) findViewById(R.id.playBallImage);



    }
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bgv.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        bgv.pause();
    }
    public void initRotateAnimation() {
        r = new RotateAnimation(0,359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(Animation.INFINITE);
        r.setRepeatMode(Animation.RESTART);
        r.setDuration(1500);

        //animationSet = new AnimationSet(false);
        //animationSet.addAnimation(r);
    }
}
