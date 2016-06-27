package rr.mc.fhhgb.at.epocgame.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import rr.mc.fhhgb.at.epocgame.views.EpocQualityView;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        View v = new EpocQualityView(this, metrics);
        v.setBackgroundColor(Color.BLACK);

        setContentView(v);

    }
}