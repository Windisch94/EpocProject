package rr.mc.fhhgb.at.epocgame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PractiseActivity extends AppCompatActivity {

    AnimatedView av;
    private RadioGroup radioTypeGroup;
    private RadioButton radioTypeButton;
    private ProgressBar progressBarTraining;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private boolean isBackAllowed = true;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);
        av = (AnimatedView) findViewById(R.id.anim_view);
        Button btn = (Button) findViewById(R.id.button_training);
        radioTypeGroup = (RadioGroup) findViewById(R.id.radioGroup_training);
        progressBarTraining = (ProgressBar) findViewById(R.id.progress_bar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTypeButton = (RadioButton)findViewById(radioTypeGroup.getCheckedRadioButtonId());
                progressBarStatus=0;
                isBackAllowed = false;
                if (radioTypeButton.getText().equals("Neutral")) {
                    av.shouldDraw = false;
                }else if(radioTypeButton.getText().equals("Anstoßen")) {
                    av.shouldDraw = true;
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressBarStatus <100) {
                            progressBarStatus += 6;
                            if (progressBarStatus == 96) {
                                progressBarStatus = 100;
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarTraining.setProgress(progressBarStatus);
                                    if (progressBarStatus == 100) {
                                        av.reset();
                                        isBackAllowed = true;
                                        Toast.makeText(PractiseActivity.this,"Training "+radioTypeButton.getText()+" successful",Toast.LENGTH_SHORT).show();
                                        progressBarTraining.setProgress(0);
                                        //TODO EPOC+ Anbindung

                                    }
                                }
                            });
                        }
                    }
                });
                thread.start();


            }
        });

        radioTypeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.stop();
                progressBarStatus=0;
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (isBackAllowed) {
            super.onBackPressed();
        }

    }
}
