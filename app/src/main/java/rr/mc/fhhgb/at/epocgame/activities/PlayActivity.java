package rr.mc.fhhgb.at.epocgame.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.views.BackgroundView;

/**
 * Activity for the game
 * @author Windischhofer, Rohner
 */
public class PlayActivity extends AppCompatActivity {

    static int currentAction; // EPOC+ currentAction
    static float currentPower; // EPOC+ currentPower


    //UI elements
    public TextView distanceTV;
    public TextView timeTV;
    private ProgressBar powerProgress;
    private Button startButton;
    private Button nudgeButton;
    private BackgroundView bgv;
    private ImageView ballImageView;

    private Handler updatePowerProgressHandler = new Handler();
    private Runnable increasePowerProgressRunnable;
    private Runnable decreasePowerProgressRunnable;
    Timer timerListenAction;

    private boolean increase = false;
    private boolean shouldRun = true;
    private boolean isBackAllowed = true;

    // animation for the ball
    private RotateAnimation r;
    private AnimationSet animationSet;

    int progressValue = 0;
    int distance = 0;



    //power meter, mit schnellerem background
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initRotateAnimation();
        nudgeButton.setEnabled(false);

        bgv = (BackgroundView) findViewById(R.id.backgroundView);
        distanceTV = (TextView) findViewById(R.id.distanceTV);
        timeTV = (TextView) findViewById(R.id.timeTV);
        startButton = (Button) findViewById(R.id.startButton);
        powerProgress = (ProgressBar) findViewById(R.id.progress_bar_play);
        nudgeButton = (Button) findViewById(R.id.nudgeButton);
        ballImageView = (ImageView) findViewById(R.id.playBallImage);

        //deactivate button if EPOC is connected and start the timer for updating the UI
        if (MainActivity.isEPOC) {
            nudgeButton.setAlpha(0);
            timerListenAction = new Timer();
            timerListenAction.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handlerUpdateUI.sendEmptyMessage(0);
                }
            },0,20);
        }


        increasePowerProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (shouldRun) {
                    if (progressValue <= 100) {
                        if (increase) {
                            powerProgress.setProgress(progressValue);
                            progressValue += 2;
                            bgv.getBackgroundModel().speedUp(progressValue);
                            updatePowerProgressHandler.postDelayed(this, 50);
                        }




                    } else {

                        progressValue = 100;
                        powerProgress.setProgress(progressValue);
                        bgv.getBackgroundModel().speedUp(progressValue);

                    }
                }


            }
        };

        decreasePowerProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (shouldRun) {
                    if (progressValue >= 0) {
                        if(!increase) {
                            powerProgress.setProgress(progressValue);
                            progressValue -= 2;
                            bgv.getBackgroundModel().speedUp(progressValue);
                            updatePowerProgressHandler.postDelayed(this, 50);

                        }

                    } else {

                        progressValue = 0;
                        powerProgress.setProgress(progressValue);
                        bgv.getBackgroundModel().speedUp(progressValue);

                    }
                }


            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!MainActivity.isEPOC) {
                    nudgeButton.setEnabled(true);
                }
                isBackAllowed = false;
                shouldRun = true;
                bgv.getBackgroundModel().setSpeed(5);
                initRotateAnimation();
                ballImageView.startAnimation(animationSet);
                startButton.setAlpha(0);
                startButton.setEnabled(false);
                new CountDownTimer(20000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timeTV.setText("Zeit: " + millisUntilFinished / 1000 + "sek.");
                    }

                    public void onFinish() {
                        timeTV.setText("Zeit: 20 sek.");
                        bgv.getBackgroundModel().setSpeed(0);
                        distance = bgv.getBackgroundModel().distance;
                        isBackAllowed = true;
                        ballImageView.clearAnimation();
                        if (MainActivity.isEPOC) {
                            timerListenAction.cancel();
                        }
                        startButton.setAlpha(1);
                        startButton.setEnabled(true);
                        final String username;
                        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
                        username = preferences.getString("Name", "");
                        AlertDialog.Builder doneDialog = new AlertDialog.Builder(PlayActivity.this);
                        doneDialog.setMessage("Gratulation " + username + "! Du hast eine Weite von " + distance + " Metern erreicht!");
                        doneDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase highscoreDB = openOrCreateDatabase("HIGHSCORE", MODE_PRIVATE, null);
                                highscoreDB.execSQL("INSERT INTO HIGHSCORE_DATA VALUES('" + username + "'," + distance + ");");
                                distance = 0;
                                finish();
                                Intent i = new Intent(PlayActivity.this, HighscoreActivity.class);
                                startActivity(i);
                            }
                        });
                        bgv.getBackgroundModel().distance = 0;
                        shouldRun = false;
                        AlertDialog alertDialog = doneDialog.create();
                        alertDialog.show();


                    }
                }.start();

            }
        });

        nudgeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //button released
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    increase = false;
                    updatePowerProgressHandler.post(decreasePowerProgressRunnable);
                    // button pressed
                }else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    increase = true;
                    updatePowerProgressHandler.post(increasePowerProgressRunnable);
                }
                return false;
            }
        });
    }

    // handler for updating the view
    Handler handlerUpdateUI=new Handler(){
        public void handleMessage(Message msg) {
            toggleSpeed();
        }
    };

    /**
     * toggles the speed in case of the epoc+ strength
     */
    private void toggleSpeed() {
        powerProgress.setProgress((int)(currentPower*100));
    }

    @Override
    public void onBackPressed() {
        if (isBackAllowed) {
            super.onBackPressed();
        }

    }

    /**
     * if app gets resumed
     */
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

    /**
     * initialize the rotate animation for the ball
     */
    public void initRotateAnimation() {
        r = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(Animation.INFINITE);
        r.setRepeatMode(Animation.RESTART);
        r.setDuration(1500);


        animationSet = new AnimationSet(false);
        animationSet.addAnimation(r);

    }
}


