package rr.mc.fhhgb.at.epocgame.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.adapter.HighscoreAdapter;
import rr.mc.fhhgb.at.epocgame.model.Player;

public class HighscoreActivity extends AppCompatActivity {

    ArrayList<Player> myPlayers = new ArrayList<Player>();
    List<String> results = new ArrayList<String>();
    private Cursor cursor = null;

    private SQLiteDatabase highscoreDB = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);


        try {
            highscoreDB = openOrCreateDatabase("HIGHSCORE",MODE_PRIVATE,null);
            createTable();
            lookUpData();
        }catch (SQLiteException e) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
        finally
        {

            if (highscoreDB != null)
                //highscoreDB.execSQL("DELETE FROM HIGHSCORE_DATA");
            highscoreDB.close();
        }


        ListView lv = (ListView) findViewById(R.id.highscoreList);
        HighscoreAdapter highScoreAdapter = new HighscoreAdapter(this,myPlayers);
        //lv.setAdapter(highScoreAdapter);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,results));

    }

    private void lookUpData() {
        cursor = highscoreDB.rawQuery("SELECT USERNAME, SCORE FROM HIGHSCORE_DATA ORDER BY SCORE DESC",null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
                    int score = cursor.getInt(cursor.getColumnIndex("SCORE"));
                    myPlayers.add(new Player(username,score));
                    results.add(username+": "+score+"m");
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void createTable() {
       highscoreDB.execSQL("CREATE TABLE IF NOT EXISTS HIGHSCORE_DATA (USERNAME VARCHAR, SCORE INT);");
        //highscoreDB.execSQL("INSERT INTO HIGHSCORE_DATA VALUES('Max Mustermann',0);");
        //highscoreDB.execSQL("INSERT INTO HIGHSCORE_DATA VALUES('Linda Musterfrau',0);");
    }
}
