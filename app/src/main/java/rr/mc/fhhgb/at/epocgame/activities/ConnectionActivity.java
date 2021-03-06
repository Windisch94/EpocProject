package rr.mc.fhhgb.at.epocgame.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import rr.mc.fhhgb.at.epocgame.views.EpocQualityView;

/**
 * Activity for displaying the the signal state of the electrodes
 * @author Windischhofer, Rohner
 */
public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // dont turn off display
        View v = new EpocQualityView(this, metrics); //custom View
        v.setBackgroundColor(Color.BLACK);
        setContentView(v);

    }
}
