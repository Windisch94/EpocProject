package rr.mc.fhhgb.at.epocgame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = new EpocQualityView(this);
        v.setBackgroundColor(Color.BLACK);
        setContentView(v);

    }
}
