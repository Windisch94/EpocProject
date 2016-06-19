package rr.mc.fhhgb.at.epocgame.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import rr.mc.fhhgb.at.epocgame.R;

public class PractiseActivity extends AppCompatActivity {


    private RadioGroup radioTypeGroup;
    private RadioButton radioTypeButton;
    private ProgressBar progressBarTraining;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private boolean isBackAllowed = true;
    private Thread thread;
    private Button btn_train;
    private Runnable progressBarRunnable;
    private ImageView ballImage;
    private AnimationSet animationSet;
    private RotateAnimation r;
    private TranslateAnimation t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);
        ballImage = (ImageView) findViewById(R.id.ballPractise);
        initRotateAndTranslateAnimation();


        btn_train = (Button) findViewById(R.id.button_training);
        radioTypeGroup = (RadioGroup) findViewById(R.id.radioGroup_training);
        progressBarTraining = (ProgressBar) findViewById(R.id.progress_bar);

        btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_train.getText().equals("Abbrechen")) {
                    enableUIElements();
                    ballImage.clearAnimation();
                   // av.reset();
                    progressBarTraining.setProgress(0);
                    isBackAllowed = true;

                }else {
                    radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
                    progressBarStatus = 0;
                    disableUIElements();
                    if (radioTypeButton.getText().equals("Neutral")) {
                        initRotateAnimation();
                    } else if (radioTypeButton.getText().equals("Anstoßen")) {
                        initRotateAndTranslateAnimation();

                    }
                    ballImage.startAnimation(animationSet);
                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                                while (progressBarStatus < 100 && !isBackAllowed) {
                                    progressBarStatus += 6;
                                    if (progressBarStatus == 96) {
                                        progressBarStatus = 100;
                                    }
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    progressBarRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarTraining.setProgress(progressBarStatus);
                                            if (progressBarStatus == 100) {

                                                enableUIElements();
                                                Toast.makeText(PractiseActivity.this, "Training " + radioTypeButton.getText() + " successful", Toast.LENGTH_SHORT).show();
                                                progressBarTraining.setProgress(0);
                                                ballImage.clearAnimation();
                                                //TODO EPOC+ Anbindung

                                            }
                                        }
                                    };
                                    if(!isBackAllowed) {
                                        progressBarHandler.post(progressBarRunnable);
                                    }

                                }


                        }
                    });
                    thread.start();
                }




            }
        });


    }

    public void disableUIElements() {

        isBackAllowed = false;
        for (int i = 0; i < radioTypeGroup.getChildCount(); i++) {
            radioTypeGroup.getChildAt(i).setEnabled(false);
        }
       // btn_train.setEnabled(false);
        btn_train.setText("Abbrechen");



    }

    public void enableUIElements() {
        isBackAllowed = true;
        for (int i = 0; i < radioTypeGroup.getChildCount(); i++) {
            radioTypeGroup.getChildAt(i).setEnabled(true);
        }
        //btn_train.setEnabled(true);
        btn_train.setText("Trainieren");
    }

    private void initRotateAndTranslateAnimation() {
        r = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(Animation.INFINITE);
        r.setRepeatMode(Animation.RESTART);
        r.setDuration(1500);
        RotateAnimation r2 = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        int left = ballImage.getLeft();
        int top = ballImage.getTop();
        int right = ballImage.getRight();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        t = new TranslateAnimation(left,left+300,top,top);
        t.setDuration(2000);
        t.setRepeatMode(Animation.REVERSE);
        t.setRepeatCount(Animation.INFINITE);
        animationSet = new AnimationSet(false);
        animationSet.addAnimation(r);
       // animationSet.addAnimation(t);
    }

    public void initRotateAnimation() {
        r = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(Animation.INFINITE);
        r.setRepeatMode(Animation.RESTART);
        r.setDuration(1500);
        animationSet = new AnimationSet(false);
        animationSet.addAnimation(r);
    }


    @Override
    public void onBackPressed() {
        if (isBackAllowed) {
            super.onBackPressed();
        }

    }
}
