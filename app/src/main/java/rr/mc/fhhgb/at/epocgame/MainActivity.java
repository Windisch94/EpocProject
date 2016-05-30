package rr.mc.fhhgb.at.epocgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "brain2machine";
    // random comment by ralph
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //IEdk.IEE_EngineConnect(this,"");
        Button button = null;
        button = (Button) findViewById(R.id.buttonPlay);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonPractise);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonHowto);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonHighscore);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.buttonPlay: {
                Log.i(TAG, "Button play pressed");
                Intent i = new Intent(this, PlayActivity.class);
                startActivity(i);
            }
            break;

            case R.id.buttonPractise: {
                Log.i(TAG, "Button practise pressed");
                Intent i = new Intent(this, PractiseActivity.class);
                startActivity(i);
            }
            break;

            case R.id.buttonHowto: {
                Log.i(TAG, "Button howto pressed");
                Intent i = new Intent(this, HowtoActivity.class);
                startActivity(i);
            }
            break;
            case R.id.buttonHighscore: {
                Log.i(TAG, "Button highscore pressed");
                Intent i = new Intent(this, HighscoreActivity.class);
                startActivity(i);
            }
            break;
            default:
                Log.e(TAG, "unknown onClick ID encountered..");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
