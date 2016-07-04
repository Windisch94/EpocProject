package rr.mc.fhhgb.at.epocgame.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import rr.mc.fhhgb.at.epocgame.R;

/**
 * Start Activity
 * @author Windischhofer, Rohner
 */
public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {


    //UI elements
    EditText nameET;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        //get Username if one exists
        SharedPreferences preferences = getSharedPreferences("username",MODE_PRIVATE);
        String name = preferences.getString("Name",null);
        button = (Button) findViewById(R.id.btnApply);
        button.setOnClickListener(this);

        nameET = (EditText) findViewById(R.id.txtName);

        if (name != null) {
            nameET.setText(name);
        }
    }


    @Override
    public void onClick(View v) {
        //store new username
        SharedPreferences preferences = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", String.valueOf(nameET.getText()));
        editor.commit();

        Intent i = new Intent(UsernameActivity.this, MainActivity.class);
        startActivity(i);


    }

}
