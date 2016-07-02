package rr.mc.fhhgb.at.epocgame.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.emotiv.insight.IEmoStateDLL;
import com.emotiv.insight.MentalCommandDetection;

import java.util.Timer;
import java.util.TimerTask;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.model.EngineConnector;
import rr.mc.fhhgb.at.epocgame.model.EngineInterface;

public class PractiseActivity extends AppCompatActivity implements EngineInterface {


    private EngineConnector engineConnector;

    private RadioGroup radioTypeGroup;
    private RadioButton radioTypeButton;
    private ProgressBar progressBarTraining;
    private boolean isBackAllowed = true;
    private Button btn_train;
    private Button btn_clear;
    private ImageView ballImage;
    private AnimationSet animationSet;
    private RotateAnimation r;
    private TranslateAnimation t;
    boolean isTraining = false;
    private TimerTask timerTask;
    private Timer timer;
    int userId = 0;
    int count =0;

    Handler handlerUpdateUI=new Handler(){
        public void handleMessage(Message msg) {
                    count ++;
                    int trainingTime=(int) MentalCommandDetection.IEE_MentalCommandGetTrainingTime(userId)[1]/1000;
                    if(trainingTime > 0)
                        progressBarTraining.setProgress(count / trainingTime);
                    if (progressBarTraining.getProgress() >= 100) {
                        timerTask.cancel();
                        timer.cancel();
                    }
        };
    };

    public void setTimerTask()
    {
        count = 0;
        timerTask=new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                handlerUpdateUI.sendEmptyMessage(0);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);
        ballImage = (ImageView) findViewById(R.id.ballPractise);
        engineConnector = EngineConnector.shareInstance();
        engineConnector.delegate = this;




        btn_train = (Button) findViewById(R.id.button_training);
        btn_clear = (Button) findViewById(R.id.clearTrainingButton);
        radioTypeGroup = (RadioGroup) findViewById(R.id.radioGroup_training);
        radioTypeButton = (RadioButton) findViewById(R.id.radio_neutral);
        if (engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt())) {
            radioTypeButton.setBackgroundColor(Color.GREEN);
        }
        radioTypeButton = (RadioButton) findViewById(R.id.radio_nudge);
        if (engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH.ToInt())) {
            radioTypeButton.setBackgroundColor(Color.GREEN);
        }

        progressBarTraining = (ProgressBar) findViewById(R.id.progress_bar);


        btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (MainActivity.isEPOC) {
                        if (!engineConnector.isConnected) {
                            Toast.makeText(PractiseActivity.this, "Du bist nicht mit dem Headset verbunden", Toast.LENGTH_SHORT).show();
                        } else {
                            radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
                            disableUIElements();
                            if (radioTypeButton.getText().equals("Neutral")) {
                                initRotateAnimation();
                                startTraining(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL);
                                ballImage.startAnimation(animationSet);
                            } else if (radioTypeButton.getText().equals("Anstoßen")) {
                                initRotateAndTranslateAnimation();
                                engineConnector.enableMentalcommandActions(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH);
                                startTraining(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH);
                                ballImage.startAnimation(animationSet);
                            }
                        }
                    }






            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
                if (radioTypeButton.getText().equals("Neutral")) {
                    engineConnector.trainingClear(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt());
                }else {
                    engineConnector.trainingClear(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH.ToInt());
                }

            }
        });


    }

    private void startTraining(IEmoStateDLL.IEE_MentalCommandAction_t mcNeutral) {
        isTraining = engineConnector.startTrainingMetalcommand(isTraining,mcNeutral);

    }

    public void disableUIElements() {

        isBackAllowed = false;
        btn_clear.setEnabled(false);
        btn_train.setEnabled(false);
        for (int i = 0; i < radioTypeGroup.getChildCount(); i++) {
            radioTypeGroup.getChildAt(i).setEnabled(false);
        }



    }

    public void enableUIElements() {
        isBackAllowed = true;
        btn_clear.setEnabled(true);
        btn_train.setEnabled(true);
        for (int i = 0; i < radioTypeGroup.getChildCount(); i++) {
            radioTypeGroup.getChildAt(i).setEnabled(true);
        }

    }

    /**
     * inits the Rotate and Translate Animation for the nudge Training
     */
    private void initRotateAndTranslateAnimation() {
        r = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(Animation.INFINITE);
        r.setRepeatMode(Animation.RESTART);
        r.setDuration(1500);

        int left = ballImage.getLeft();
        int top = ballImage.getTop();
        int right = ballImage.getRight();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        t = new TranslateAnimation(0,width - right-30,0,0); // 30 padding

        t.setDuration(3000);
        t.setRepeatMode(Animation.REVERSE);
        t.setRepeatCount(Animation.INFINITE);
        animationSet = new AnimationSet(false);
        animationSet.addAnimation(r);
        animationSet.addAnimation(t);
    }

    /**
     * inits the rotate animation for the neutral training
     */
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
            if ((!engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt())
                    || !engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH.ToInt()))) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PractiseActivity.this);
                // set title
                alertDialogBuilder.setTitle("Unvollständiges Training");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Sie müssen alle zwei Aktionen trainiert haben um spielen zu können, wirklich fortfahren?")
                        .setCancelable(false)
                        .setPositiveButton("Ja",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,int which) {
                                        PractiseActivity.this.finish();
                                    }
                                })
                        .setNegativeButton("Nein",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else {
                super.onBackPressed();
            }


        }

    }

    @Override
    public void trainStarted() {
        timer = new Timer();
        setTimerTask();
        timer.schedule(timerTask , 0, 10);

    }

    @Override
    public void trainSucceed() {
        enableUIElements();

        ballImage.clearAnimation();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PractiseActivity.this);
        // set title
        alertDialogBuilder.setTitle("Training Erfolgreich");
        // set dialog message
        alertDialogBuilder
                .setMessage("Das Trainieren war erfolgreich, wollen sie das Training speichern?")
                .setCancelable(false)
                .setPositiveButton("Ja",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,int which) {
                                engineConnector.setTrainControl(MentalCommandDetection.IEE_MentalCommandTrainingControl_t.MC_ACCEPT.getType());
                                progressBarTraining.setProgress(0);
                            }
                        })
                .setNegativeButton("Nein",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                engineConnector.setTrainControl(MentalCommandDetection.IEE_MentalCommandTrainingControl_t.MC_REJECT.getType());
                                progressBarTraining.setProgress(0);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void trainFailed() {
        Toast.makeText(this,"Verbindungsfehler",Toast.LENGTH_SHORT).show();
        isTraining = false;
    }

    @Override
    public void trainCompleted() {
        radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
        radioTypeButton.setBackgroundColor(Color.GREEN);
        Log.e("Succeed","Training complete");

        isTraining = false;

    }

    @Override
    public void trainRejected() {
        Log.e("Reject","Training Rejected");
        radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
        radioTypeButton.setBackgroundColor(Color.RED);
        isTraining = false;

    }

    @Override
    public void trainReset() {
        isTraining = false;

    }

    @Override
    public void trainErased() {
        radioTypeButton = (RadioButton) findViewById(radioTypeGroup.getCheckedRadioButtonId());
        radioTypeButton.setBackgroundColor(Color.TRANSPARENT);
        Toast.makeText(PractiseActivity.this,"Training von "+radioTypeButton.getText()+" wurde gelöscht",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void userAdd(int userId) {
        this.userId = userId;
    }

    @Override
    public void userRemoved() {

    }

    @Override
    public void currentAction(int typeAction, float power) {

    }
}
