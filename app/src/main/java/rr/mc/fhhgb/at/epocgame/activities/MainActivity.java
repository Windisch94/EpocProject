package rr.mc.fhhgb.at.epocgame.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emotiv.insight.IEmoStateDLL;

import java.util.Timer;
import java.util.TimerTask;

import rr.mc.fhhgb.at.epocgame.R;
import rr.mc.fhhgb.at.epocgame.model.EngineConnector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "brain2machine";
    static boolean isEPOC = false;
    int MY_PERMISSIONS_REQUEST_BLUETOOTH;
    EngineConnector engineConnector;
    TextView batteryStatus;
    TextView connectionStatus;
    TextView usernameTV;
    ProgressDialog progressDialog;
    int MY_PERMISSIONS_REQUEST_ACCESSLOCATION;
    AlertDialog.Builder alertNotConnected;
    AlertDialog alertDialog;
    private boolean isStartedQuality = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDatabase();
        //Alert for no Connection
        alertNotConnected = new AlertDialog.Builder(this);
        alertNotConnected.setMessage("Du bist nicht mit einem EPOC+ Gerät verbunden und kannst daher nicht trainieren!");


        //Alert for Connection to EPOC+
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Willst du dich mit einem EPOC+ Gerät verbinden?");
        alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEPOC= true;
                EngineConnector.setContext(MainActivity.this);
                engineConnector = EngineConnector.shareInstance();
                progressDialog = ProgressDialog.show(MainActivity.this,"Verbindungsaufbau","EPOC+ Gerät wird verbunden",true);
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEPOC=false;
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        accessPermissions(); //access permissions on runtime for android >6.0
        batteryStatus = (TextView) findViewById(R.id.batteryText);
        connectionStatus = (TextView) findViewById(R.id.signalText);
        usernameTV = (TextView) findViewById(R.id.usernameTV);
        SharedPreferences preferences = getSharedPreferences("username",MODE_PRIVATE);
        String username;
        if ((username = preferences.getString("Name",null)) != null) {
            usernameTV.setText("Eingeloggt als: "+username);
        }




        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() { // TimerTask für die BatteryConnection
            @Override
            public void run() {
                if (isEPOC) {
                    runOnUiThread(new Runnable() { // Update View in Thread
                        @Override
                        public void run() {
                            if (engineConnector!= null) {
                                if (engineConnector.isConnected) {
                                    connectionStatus.setTextColor(Color.parseColor("#2E7D32"));
                                    connectionStatus.setText("Verbunden");
                                    connectionStatus.setEnabled(true);
                                    connectionStatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(MainActivity.this,ConnectionActivity.class);
                                            startActivity(i);
                                        }
                                    });
                                    setBatteryStatus(IEmoStateDLL.IS_GetBatteryChargeLevel()[0]);
                                    progressDialog.dismiss();
                                    if (!isStartedQuality) {
                                        Intent i = new Intent(MainActivity.this,ConnectionActivity.class);
                                        startActivity(i);
                                        isStartedQuality = true;
                                    }




                                } else {
                                    connectionStatus.setTextColor(Color.RED);
                                    connectionStatus.setText("Nicht verbunden");
                                    connectionStatus.setEnabled(false);
                                    // set state so it doesnt jump in between values
                                    setBatteryStatus(0);
                                    progressDialog.show();

                                }
                            }
                        }
                    });
                }


            }
        };
        timer.schedule(timerTask, 0, 1000);

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

    /**
     * creates the highscore database if not exists
     */
    private void createDatabase() {
        SQLiteDatabase highscoreDB = openOrCreateDatabase("HIGHSCORE",MODE_PRIVATE,null);
        highscoreDB.execSQL("CREATE TABLE IF NOT EXISTS HIGHSCORE_DATA (USERNAME VARCHAR, SCORE INT);");
    }


    /**
     * Setzt den Batterystatus anhand der ID die geliefert wird
     * @param id Die ID, die vom System kommt
     */
    private void setBatteryStatus(int id) {
        switch (id) {
            case -1:
                //batteryStatus.setText("-");
                break;
            case 0:
                batteryStatus.setText("-");
                break;
            case 1:
                batteryStatus.setText("Akku: 20%");
                break;
            case 2:
                batteryStatus.setText("Akku: 40%");
                break;
            case 3:
                batteryStatus.setText("Akku: 60%");
                break;
            case 4:
                batteryStatus.setText("Akku: 80%");
                break;
            case 5:
                batteryStatus.setText("Akku: 100%");
                break;
            default:
                batteryStatus.setText("-");
        }
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.buttonPlay: {
                if (isEPOC) {
                    if ((!engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_NEUTRAL.ToInt())
                            || !engineConnector.checkTrained(IEmoStateDLL.IEE_MentalCommandAction_t.MC_PUSH.ToInt()))) {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                MainActivity.this);
                        // set title
                        alertDialogBuilder.setTitle("Unvollständiges Training");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Sie müssen zuerst alle zwei Aktionen trainiert haben um spielen zu können!")
                                .setCancelable(false)
                                .setNeutralButton("OK",null);


                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }else {
                        Intent i = new Intent(MainActivity.this, PlayActivity.class);
                        startActivity(i);
                    }

                }else {
                    Intent i = new Intent(MainActivity.this, PlayActivity.class);
                    startActivity(i);
                }

            }
            break;

            case R.id.buttonPractise: {
                if(engineConnector == null || !engineConnector.isConnected) {
                        alertNotConnected.setNeutralButton("OK",null);
                        AlertDialog alertDialog = alertNotConnected.create();
                        alertDialog.show();
                }else {
                    Intent i = new Intent(MainActivity.this, PractiseActivity.class);
                    startActivity(i);
                }
                Log.i(TAG, "Button practise pressed");

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









    private void accessPermissions() {
        // Request & set Permissions
        //Bluetooth
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESSLOCATION);
            }
        }

        // access fineLocation
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH);

        // MainActivity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);
            }
        }
    }
}
