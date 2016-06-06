package rr.mc.fhhgb.at.epocgame;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class PlayActivity extends AppCompatActivity {
    //NUR AUSPROBIERT! KANNST A LÖSCHEN
    Button button;
    ValueAnimator animator;
    long duration = 10000L;
    int height;
    int width;
    ImageView backgroundOne;
    ImageView backgroundTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        button = (Button)findViewById(R.id.buttonFaster);
        button = (Button) findViewById(R.id.buttonMoveBall);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //move ball

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //background animation wird schneller
                animator.setDuration(duration / 2);

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
                final float translationX = width * progress *-1;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + width);
            }
        });
        animator.start();

    }
}
