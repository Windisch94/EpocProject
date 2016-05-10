package rr.mc.fhhgb.at.epocgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        ListView lv = (ListView) findViewById(R.id.highscoreList);
        //ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myList);
        //lv.setAdapter(myarrayAdapter);
        // lv.setTextFilterEnabled(true);
    }
}
