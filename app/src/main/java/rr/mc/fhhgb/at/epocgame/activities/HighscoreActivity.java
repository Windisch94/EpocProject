package rr.mc.fhhgb.at.epocgame.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.adapter.HighscoreAdapter;
import rr.mc.fhhgb.at.epocgame.model.Player;

public class HighscoreActivity extends AppCompatActivity {

    ArrayList<Player> myPlayers = new ArrayList<Player>();
    private Cursor cursor = null;
    private SQLiteDatabase highscoreDB = null;
    private Button clearHighscore;
    private ListView lv;
    private HighscoreAdapter highScoreAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        clearHighscore = (Button) findViewById(R.id.clearHighscoreButton);
        clearHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highscoreDB = openOrCreateDatabase("HIGHSCORE",MODE_PRIVATE,null);
                highscoreDB.execSQL("DELETE FROM HIGHSCORE_DATA;");
                lookUpData();
                highscoreDB.close();
                highScoreAdapter.notifyDataSetChanged();





            }
        });


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


        lv = (ListView) findViewById(R.id.highscoreList);
        highScoreAdapter = new HighscoreAdapter(this,myPlayers);
        lv.setAdapter(highScoreAdapter);
       // lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,results));

    }

    private void lookUpData() {
        myPlayers.clear();
        cursor = highscoreDB.rawQuery("SELECT USERNAME, SCORE FROM HIGHSCORE_DATA ORDER BY SCORE DESC LIMIT 10",null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndex("USERNAME"));
                    int score = cursor.getInt(cursor.getColumnIndex("SCORE"));
                    myPlayers.add(new Player(username,score));

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
