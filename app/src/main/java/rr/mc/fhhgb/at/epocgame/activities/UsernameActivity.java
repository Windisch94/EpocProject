package rr.mc.fhhgb.at.epocgame.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import rr.mc.fhhgb.at.epocgame.R;

public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {


    public SharedPreferences preferences;
    EditText name;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        button = (Button) findViewById(R.id.btnApply);
        button.setOnClickListener(this);

        name = (EditText) findViewById(R.id.txtName);

    }


    @Override
    public void onClick(View v) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", String.valueOf(name.getText()));
        editor.apply();

        Intent i = new Intent(UsernameActivity.this, MainActivity.class);
        startActivity(i);


    }

}
