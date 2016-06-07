package rr.mc.fhhgb.at.epocgame;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class PlayActivity extends AppCompatActivity {

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        textViewCountdown = (TextView) findViewById(R.id.textViewCountdown);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);

        buttonMoveBgd = (Button) findViewById(R.id.buttonMoveBgd);
        buttonMoveBall = (Button) findViewById(R.id.buttonMoveBall);
        buttonStart = (Button) findViewById(R.id.buttonStart);

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
                //moveBall();
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
        bm = EpocQualityView.getResizedBitmap(bm, dm.heightPixels + 40, dm.widthPixels + 200);
        Bitmap bm2 = ((BitmapDrawable) backgroundTwo.getDrawable()).getBitmap();
        bm2 = EpocQualityView.getResizedBitmap(bm2, dm.heightPixels + 40, dm.widthPixels + 200);


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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Play Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://rr.mc.fhhgb.at.epocgame/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Play Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://rr.mc.fhhgb.at.epocgame/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
