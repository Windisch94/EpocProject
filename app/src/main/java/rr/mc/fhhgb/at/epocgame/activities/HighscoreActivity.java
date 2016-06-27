package rr.mc.fhhgb.at.epocgame.activities;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.model.Player;

public class HighscoreActivity extends AppCompatActivity {

    ArrayList<Player> myPlayers = new ArrayList<Player>();

    private SQLiteDatabase highscoreDB = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        RelativeLayout view = (RelativeLayout) findViewById(R.id.highscoreView);

        try {
            highscoreDB = openOrCreateDatabase("HIGHSCORE",MODE_PRIVATE,null);
            createTable();
        }catch (SQLiteException e) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }


        ListView lv = (ListView) findViewById(R.id.highscoreList);

    }

    private void createTable() {
       // highscoreDB.execSQL();
    }
}
